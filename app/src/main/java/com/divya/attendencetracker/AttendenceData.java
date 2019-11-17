package com.divya.attendencetracker;

public class AttendenceData {
    String sub_id;
    String sub_name;
    int st_per;
    int tot_per;

    public String getSub_id() {
        return sub_id;
    }

    public void setSub_id(String sub_id) {
        this.sub_id = sub_id;
    }

    public String getSub_name() {
        return sub_name;
    }

    public void setSub_name(String sub_name) {
        this.sub_name = sub_name;
    }

    public int getSt_per() {
        return st_per;
    }

    public void setSt_per(int st_per) {
        this.st_per = st_per;
    }

    public int getTot_per() {
        return tot_per;
    }

    public void setTot_per(int tot_per) {
        this.tot_per = tot_per;
    }

    public AttendenceData(String sub_id, String sub_name, int st_per, int tot_per) {
        this.sub_id = sub_id;
        this.sub_name = sub_name;
        this.st_per = st_per;
        this.tot_per = tot_per;
    }
}
