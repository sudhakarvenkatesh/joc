package com.sos.joc.classes;

import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

import org.apache.commons.lang3.StringEscapeUtils;

import com.sos.jitl.reporting.db.DBItemInventoryInstance;

public class LogContent {
    
    private String accessToken;
    private static final String SPAN_LINE = "<div class=\"line %1$s\">%2$s</div>%n";
    private static final String HTML = "<!DOCTYPE html>%n<html>%n"
            + "<head>%n"
            + "  <title>JobScheduler - %1$s</title>%n"
            + "  <meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\"/>%n"
            + "  <meta name=\"viewport\" content=\"width=device-width, initial-scale=1\"/>%n"
            + "  <style type=\"text/css\">%n"
            + "    div.log                         {font-family:\"Lucida Console\",monospace;font-size:12px;line-height:1.1em;margin-left:2px;padding-left:0}%n"
            + "    div.line                        {padding-left:3em;text-indent:-3em;}%n"
            + "    .log_error                      {color:red;}%n"
            + "    .log_warn                       {color:tomato;}%n"
            + "    .log_info                       {color:black;}%n"
            + "    .log_stderr                     {color:red;}%n"
            + "    .log_debug                      {color:darkgreen;}%n"
            + "    .log_debug2                     {color:#408040;}%n"
            + "    .log_debug3                     {color:#808080;}%n"
            + "    .log_debug4                     {color:#8080FF;}%n"
            + "    .log_debug5                     {color:#8080FF;}%n"
            + "    .log_debug6                     {color:#8080FF;}%n"
            + "    .log_debug7                     {color:#8080FF;}%n"
            + "    .log_debug8                     {color:#8080FF;}%n"
            + "    .log_debug9                     {color:#a0a0a0;}%n"
            + "  </style>%n"
            + "</head>%n"
            + "<body class=\"log\">%n"
            /* only for rolling logs senseful */
//            + "  <script type=\"text/javascript\" language=\"javascript\">%n"
//            + "    var timer;%n"
//            + "    var program_is_scrolling    = true;%n"
//            + "    var error                   = false;%n"
//            + "    %n"
//            + "    start_timer();%n"
//            + "    %n"
//            + "    window.onscroll             = window__onscroll;%n"
//            + "    document.onmousewheel       = window__onscroll; /* IE, Chrome */%n"
//            + "    %n"
//            + "    if(document.addEventListener) { /* Safari, Firefox */%n"
//            + "        document.addEventListener('DOMMouseScroll', window__onscroll, false);%n"
//            + "    }%n"
//            + "    %n"
//            + "    if( window.navigator.appName == 'Netscape' ) {%n"
//            + "        window.onkeydown        = stop_timer;%n"
//            + "        window.onmousedown      = stop_timer;%n"
//            + "        window.onkeyup          = window__onscroll;%n"
//            + "        window.onmouseup        = window__onscroll;%n"
//            + "    }%n"
//            + "    %n"
//            + "    function start_timer() {%n"
//            + "        stop_timer();%n"
//            + "        if( !error )  timer = window.setInterval( \"scroll_down()\", 200 );%n"
//            + "    }%n"
//            + "    %n"
//            + "    function stop_timer() {%n"
//            + "        if( timer != undefined ) {%n"
//            + "            window.clearInterval( timer );%n"
//            + "            timer = undefined;%n"
//            + "        }%n"
//            + "    }%n"
//            + "    %n"
//            + "    function scroll_down() {%n"
//            + "        try {%n"
//            + "            program_is_scrolling = true;%n"
//            + "            window.scrollTo( document.body.scrollLeft, document.body.scrollHeight );%n"
//            + "        } catch( x ) {%n"
//            + "            error = true;%n"
//            + "            window.clearInterval( timer );%n"
//            + "            timer = undefined;%n"
//            + "        }%n"
//            + "    }%n"
//            + "    %n"
//            + "    function window__onscroll() {%n"
//            + "        if( !program_is_scrolling ) {%n"
//            + "            if( document.body.scrollTop + document.body.clientHeight == document.body.scrollHeight ) {%n"
//            + "                if( timer == undefined )  start_timer();%n"
//            + "            } else {%n"
//            + "                stop_timer();%n"
//            + "            }%n"
//            + "        }%n"
//            + "        program_is_scrolling = false;%n"
//            + "    }%n"
//            + "    %n"
//            + "  </script>%n"
            + "  <div class=\"log\">%n"
            + "%2$s</div>%n</body>%n</html>%n";
    private static final String INFO_MARKER = "[info]";
    private static final String STDERR_MARKER = "[stderr]";
    private static final String LOG_STDERR_CLASS = "log_stderr";
    private static final Map<String, String> MARKER_AND_CLASSES = new HashMap<String, String>() {

        private static final long serialVersionUID = 1L;
        {
            put(INFO_MARKER, "log_info");
            put("[ERROR]", "log_error");
            put("[WARN]", "log_warn");
            put("[debug]", "log_debug");
            put("[debug9]", "log_debug9");
            put("[debug3]", "log_debug3");
            put("[debug6]", "log_debug6");
            put("[debug2]", "log_debug2");
            put("[debug4]", "log_debug4");
            put("[debug5]", "log_debug5");
            put("[debug7]", "log_debug7");
            put("[debug8]", "log_debug8");
        }
    };

    protected DBItemInventoryInstance dbItemInventoryInstance; 
    
    public LogContent(DBItemInventoryInstance dbItemInventoryInstance, String accessToken) {
        this.dbItemInventoryInstance = dbItemInventoryInstance;
        this.accessToken = accessToken;
    }
    
    protected String getAccessToken() {
        return accessToken;
    }

    private String addStyle(String line) {
        for (String marker : MARKER_AND_CLASSES.keySet()) {
            if (line.contains(marker)) {
                String css = MARKER_AND_CLASSES.get(marker);
                if (INFO_MARKER.equals(marker) && line.contains(STDERR_MARKER)) {
                    css += " " + LOG_STDERR_CLASS;
                }
                line = String.format(SPAN_LINE, css, StringEscapeUtils.escapeHtml4(line));
                break;
            }
        }
        return line;
    }

    private String colouredLog(String log){
        Scanner scanner = new Scanner(log);
        StringBuilder s = new StringBuilder();
        while (scanner.hasNextLine()) {
            s.append(addStyle(scanner.nextLine()));
        }
        scanner.close();
        return s.toString();
    
    }
    
    public String htmlWithColouredLogContent(String log){
        return colouredLog(log);
    }
    
    public String htmlPageWithColouredLogContent(String log, String title) {
        return String.format(HTML, title, colouredLog(log));
    }
}



