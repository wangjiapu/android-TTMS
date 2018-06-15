package com.example.other.ttms.beans;

public class OrderInfo {

    private int ticket_id;
    private String order_play_name;
    private String cinema;
    private String show_time;
    private int ticket_num;
    private String src="http://f2.topitme.com/2/6a/bc/113109954583dbc6a2o.jpg";
    private boolean finished=false;

    public boolean isFinished() {
        return finished;
    }

    public void setFinished(boolean finished) {
        this.finished = finished;
    }

    public String getSrc() {
        return src;
    }

    public void setSrc(String src) {
        this.src = src;
    }

    public int getTicket_id() {
        return ticket_id;
    }

    public void setTicket_id(int ticket_id) {
        this.ticket_id = ticket_id;
    }

    public String getOrder_play_name() {
        return order_play_name;
    }

    public void setOrder_play_name(String order_play_name) {
        this.order_play_name = order_play_name;
    }

    public String getCinema() {
        return cinema;
    }

    public void setCinema(String cinema) {
        this.cinema = cinema;
    }

    public String getShow_time() {
        return show_time;
    }

    public void setShow_time(String show_time) {
        this.show_time = show_time;
    }

    public int getTicket_num() {
        return ticket_num;
    }

    public void setTicket_num(int ticket_num) {
        this.ticket_num = ticket_num;
    }


}
