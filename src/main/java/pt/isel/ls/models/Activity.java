package pt.isel.ls.models;

import java.sql.Date;
import java.sql.Time;

public class Activity implements Model {
    private int aid;
    private Date date;
    private Time durationTime;
    private int sid;
    private int uid;
    private int rid;

    public Activity(int aid, Date date, Time durationTime, int sid, int uid, int rid) {
        this.aid = aid;
        this.date = date;
        this.durationTime = durationTime;
        this.sid = sid;
        this.uid = uid;
        this.rid = rid;
    }

    public int getAid() {
        return aid;
    }

    public void setAid(int aid) {
        this.aid = aid;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Time getDurationTime() {
        return durationTime;
    }

    public void setDurationTime(Time durationTime) {
        this.durationTime = durationTime;
    }

    public int getSid() {
        return sid;
    }

    public void setSid(int sid) {
        this.sid = sid;
    }

    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }

    public int getRid() {
        return rid;
    }

    public void setRid(int rid) {
        this.rid = rid;
    }

    @Override
    public void print() {
        System.out.println("id: " + aid + " date: " + date + " duration time: " + durationTime + " sport id: "
                + sid + " user id: " + uid + " route id: " + rid);

    }

    @Override
    public String toString() {
        return "Activity{"
                + "date=" + date
                + ", durationTime=" + durationTime
                + ", sid=" + sid
                + ", uid=" + uid
                + ", rid=" + rid
                + '}';
    }
}
