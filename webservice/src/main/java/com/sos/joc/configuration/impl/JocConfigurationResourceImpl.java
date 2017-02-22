package com.sos.joc.configuration.impl;

import java.time.Instant;
import java.util.Date;

import javax.ws.rs.Path;

import com.sos.hibernate.classes.SOSHibernateConnection;
import com.sos.jitl.joc.db.JocConfigurationDbItem;
import com.sos.joc.Globals;
import com.sos.joc.classes.JOCDefaultResponse;
import com.sos.joc.classes.JOCResourceImpl;
import com.sos.joc.configuration.resource.IJocConfigurationResource;
import com.sos.joc.db.configuration.JocConfigurationDbLayer;
import com.sos.joc.exceptions.JocException;
import com.sos.joc.model.configuration.Configuration;
import com.sos.joc.model.configuration.Configuration200;
import com.sos.joc.model.configuration.ConfigurationObjectType;
import com.sos.joc.model.configuration.ConfigurationOk;
import com.sos.joc.model.configuration.ConfigurationType;

@Path("configuration")
public class JocConfigurationResourceImpl extends JOCResourceImpl implements IJocConfigurationResource {

    private static final String API_CALL_READ = "./configuration";
    private static final String API_CALL_SAVE = "./configuration/save";
    private static final String API_CALL_DELETE = "./configuration/delete";
    private static final String API_CALL_SHARE = "./configuration/share";
    private static final String API_CALL_PRIVATE = "./configuration/make_private";
    private SOSHibernateConnection connection = null;
    private JocConfigurationDbLayer jocConfigurationDBLayer;

    private void init(Configuration configuration) throws Exception {
        jocConfigurationDBLayer = new JocConfigurationDbLayer(connection);

        /** check general required parameters */
        checkRequiredParameter("id", configuration.getId());

        /** set general filter */
        jocConfigurationDBLayer.getFilter().setId(configuration.getId().longValue());
        jocConfigurationDBLayer.getFilter().setInstanceId(dbItemInventoryInstance.getId());
    }

    @Override
    public JOCDefaultResponse postSaveConfiguration(String accessToken, Configuration configuration) throws Exception {
        try {
            JOCDefaultResponse jocDefaultResponse = init(API_CALL_SAVE, configuration, accessToken, configuration.getJobschedulerId(), true);
            if (jocDefaultResponse != null) {
                return jocDefaultResponse;
            }
            connection = Globals.createSosHibernateStatelessConnection(API_CALL_SAVE);
            init(configuration);

            /** check save specific required parameters */
            checkRequiredParameter("account", configuration.getAccount());
            checkRequiredParameter("configurationType", configuration.getConfigurationType().name());
            checkRequiredParameter("configurationItem", configuration.getConfigurationItem());
            if (configuration.getConfigurationType().name().equals(ConfigurationType.CUSTOMIZATION.name())) {
                /** check save customization specific required parameters */
                checkRequiredParameter("objectType", configuration.getObjectType().name());
                checkRequiredParameter("name", configuration.getName());
            }

            /** set DBItem with values from parameters */
            JocConfigurationDbItem dbItem = new JocConfigurationDbItem(); 
            dbItem.setAccount(configuration.getAccount());
            dbItem.setConfigurationType(configuration.getConfigurationType().name());
            dbItem.setObjectType(configuration.getObjectType().name());
            dbItem.setName(configuration.getName());
            dbItem.setShared(configuration.getShared());
            dbItem.setConfigurationItem(configuration.getConfigurationItem());
            dbItem.setInstanceId(dbItemInventoryInstance.getId());
            
            /** check permissions */
            boolean shareStatusMakePrivate = (dbItem != null && dbItem.getShared() && !configuration.getShared());
            boolean shareStatusMakeShare = (dbItem != null && !dbItem.getShared() && configuration.getShared());
            if (shareStatusMakePrivate 
                    && !getPermissonsJocCockpit(accessToken).getJOCConfigurations().getShare().getChange().getSharedStatus().isMakePrivate()
                    || (shareStatusMakeShare &&
                            !getPermissonsJocCockpit(accessToken).getJOCConfigurations().getShare().getChange().getSharedStatus().isMakeShared())) {
                return this.accessDeniedResponse();
            }
            Boolean owner = this.getJobschedulerUser().getSosShiroCurrentUser().getUsername().equals(dbItem.getAccount());
            Boolean permission = owner
                    || (dbItem != null && dbItem.getShared()
                    && getPermissonsJocCockpit(accessToken).getJOCConfigurations().getShare().getChange().isEditContent());
            if (!permission) {
                return this.accessDeniedResponse();
            }
            
            /** check id from parameters if DBItem is new (id==0) or has to be updated (id != 0) */
            if(configuration.getId() == null || configuration.getId() == 0) {
                dbItem.setId(null);
            } else {
                dbItem.setId(configuration.getId().longValue());
            }
            
            /** save item to DB */
            Long id = jocConfigurationDBLayer.saveConfiguration(dbItem);
            if (dbItem.getId() == null) {
                dbItem.setId(id);
            }
            ConfigurationOk ok = new ConfigurationOk();
            ok.setId(dbItem.getId().intValue());
            ok.setDeliveryDate(Date.from(Instant.now()));
            return JOCDefaultResponse.responseStatus200(ok);
        } catch (JocException e) {
            e.addErrorMetaInfo(getJocError());
            return JOCDefaultResponse.responseStatusJSError(e);
        } catch (Exception e) {
            return JOCDefaultResponse.responseStatusJSError(e, getJocError());
        } finally {
            Globals.disconnect(connection);
        }

    }

    @Override
    public JOCDefaultResponse postReadConfiguration(String accessToken, Configuration configuration) throws Exception {
        try {
            JOCDefaultResponse jocDefaultResponse = init(API_CALL_READ, configuration, accessToken, configuration.getJobschedulerId(), true);
            if (jocDefaultResponse != null) {
                return jocDefaultResponse;
            }
            connection = Globals.createSosHibernateStatelessConnection(API_CALL_READ);
            init(configuration);

            /** get item from DB with the given id */
            JocConfigurationDbItem dbItem = jocConfigurationDBLayer.getJocConfiguration(configuration.getId().longValue());
            Configuration config = setConfigurationValues(dbItem);

            /** check permissions */
            Boolean owner = this.getJobschedulerUser().getSosShiroCurrentUser().getUsername().equals(dbItem.getAccount());
            Boolean permission = owner
                    || (dbItem.getShared() && getPermissonsJocCockpit(accessToken).getJOCConfigurations().getShare().isView());
            if (!permission) {
                return this.accessDeniedResponse();
            }
            
            /** fill response */
            Configuration200 entity = new Configuration200();
            entity.setDeliveryDate(new Date());
            entity.setConfiguration(config);
            return JOCDefaultResponse.responseStatus200(entity);
        } catch (JocException e) {
            e.addErrorMetaInfo(getJocError());
            return JOCDefaultResponse.responseStatusJSError(e);
        } catch (Exception e) {
            return JOCDefaultResponse.responseStatusJSError(e, getJocError());
        } finally {
            Globals.disconnect(connection);
        }

    }

    @Override
    public JOCDefaultResponse postDeleteConfiguration(String accessToken, Configuration configuration) throws Exception {
        try {
            JOCDefaultResponse jocDefaultResponse = init(API_CALL_DELETE, configuration, accessToken, configuration.getJobschedulerId(), true);
            if (jocDefaultResponse != null) {
                return jocDefaultResponse;
            }
            connection = Globals.createSosHibernateStatelessConnection(API_CALL_DELETE);
            init(configuration);

            /** get item from DB with the given id */
            JocConfigurationDbItem dbItem = jocConfigurationDBLayer.getJocConfiguration(configuration.getId().longValue());

            /** check permissions */
            Boolean owner = this.getJobschedulerUser().getSosShiroCurrentUser().getUsername().equals(dbItem.getAccount());
            Boolean permission = owner 
                    || (dbItem.getShared()
                            && getPermissonsJocCockpit(accessToken).getJOCConfigurations().getShare().getChange().isDelete());
            if (!permission) {
                return this.accessDeniedResponse();
            }

            /** delete item */
            ConfigurationOk ok = new ConfigurationOk();
            ok.setId(dbItem.getId().intValue());
            ok.setDeliveryDate(Date.from(Instant.now()));
            jocConfigurationDBLayer.deleteConfiguration(dbItem);
            return JOCDefaultResponse.responseStatus200(ok);
        } catch (JocException e) {
            e.addErrorMetaInfo(getJocError());
            return JOCDefaultResponse.responseStatusJSError(e);
        } catch (Exception e) {
            return JOCDefaultResponse.responseStatusJSError(e, getJocError());
        } finally {
            Globals.disconnect(connection);
        }
    }

    @Override
    public JOCDefaultResponse postShareConfiguration(String accessToken, Configuration configuration) throws Exception {
        try {
            JOCDefaultResponse jocDefaultResponse = init(API_CALL_SHARE, configuration, accessToken, configuration.getJobschedulerId(),
                    getPermissonsJocCockpit(accessToken).getJOCConfigurations().getShare().getChange().getSharedStatus().isMakeShared());
            if (jocDefaultResponse != null) {
                return jocDefaultResponse;
            }
            connection = Globals.createSosHibernateStatelessConnection(API_CALL_SHARE);
            init(configuration);

            /** get item from DB with the given id */
            JocConfigurationDbItem dbItem = jocConfigurationDBLayer.getJocConfiguration(configuration.getId().longValue());
            /** set shared */
            dbItem.setShared(true);
            /** save item to DB */
            jocConfigurationDBLayer.saveConfiguration(dbItem);
            ConfigurationOk ok = new ConfigurationOk();
            ok.setId(dbItem.getId().intValue());
            ok.setDeliveryDate(Date.from(Instant.now()));
            return JOCDefaultResponse.responseStatus200(ok);
        } catch (JocException e) {
            e.addErrorMetaInfo(getJocError());
            return JOCDefaultResponse.responseStatusJSError(e);
        } catch (Exception e) {
            return JOCDefaultResponse.responseStatusJSError(e, getJocError());
        } finally {
            Globals.disconnect(connection);
        }
    }

    @Override
    public JOCDefaultResponse postMakePrivate(String accessToken, Configuration configuration) throws Exception {
        try {
            JOCDefaultResponse jocDefaultResponse = init(API_CALL_PRIVATE, configuration, accessToken, configuration.getJobschedulerId(),
                    getPermissonsJocCockpit(accessToken).getJOCConfigurations().getShare().getChange().getSharedStatus().isMakePrivate());

            if (jocDefaultResponse != null) {
                return jocDefaultResponse;
            }

            connection = Globals.createSosHibernateStatelessConnection(API_CALL_PRIVATE);
            init(configuration);

            /** get item from DB with the given id */
            JocConfigurationDbItem dbItem = jocConfigurationDBLayer.getJocConfiguration(configuration.getId().longValue());
            /** set private */
            dbItem.setShared(false);
            /** save item to DB */
            jocConfigurationDBLayer.saveConfiguration(dbItem);

            ConfigurationOk ok = new ConfigurationOk();
            ok.setId(dbItem.getId().intValue());
            ok.setDeliveryDate(Date.from(Instant.now()));
            return JOCDefaultResponse.responseStatus200(ok);
        } catch (JocException e) {
            e.addErrorMetaInfo(getJocError());
            return JOCDefaultResponse.responseStatusJSError(e);
        } catch (Exception e) {
            return JOCDefaultResponse.responseStatusJSError(e, getJocError());
        } finally {
            Globals.disconnect(connection);
        }
    }

    private Configuration setConfigurationValues(JocConfigurationDbItem dbItem) {
        Configuration config = new Configuration();
        config.setId(dbItem.getId().intValue());
        config.setAccount(dbItem.getAccount());
        config.setConfigurationType(ConfigurationType.fromValue(dbItem.getConfigurationType()));
        config.setConfigurationItem(dbItem.getConfigurationItem());
        config.setObjectType(ConfigurationObjectType.fromValue(dbItem.getObjectType()));
        config.setShared(dbItem.getShared());
        config.setName(dbItem.getName());
        config.setJobschedulerId(dbItemInventoryInstance.getSchedulerId());
        return config;
    }
    
}