package com.sos.joc.orders.post.commands.start;

import java.util.ArrayList;
import java.util.List;

public class OrdersStartBody {

    private String jobschedulerId;
    private List<Order> orders = new ArrayList<Order>();

    public String getJobschedulerId() {
        return jobschedulerId;
    }

    public void setJobschedulerId(String jobschedulerId) {
        this.jobschedulerId = jobschedulerId;
    }

    public List<Order> getOrders() {
        return orders;
    }

    public void setOrders(List<Order> orders) {
        this.orders = orders;
    }

}