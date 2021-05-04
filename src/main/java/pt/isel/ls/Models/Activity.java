package pt.isel.ls.Models;

import java.sql.Date;
import java.sql.Time;

public class Activity implements Model {
    private  int aid;
    private Date date;
    private Time duration_time;
    private int sid;
    private int uid;
    private int rid;

    public Activity(int aid, Date date, Time duration_time, int sid, int uid, int rid) {
        this.aid = aid;
        this.date = date;
        this.duration_time = duration_time;
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

    public Time getDuration_time() {
        return duration_time;
    }

    public void setDuration_time(Time duration_time) {
        this.duration_time = duration_time;
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
        System.out.println("id: "+aid+" date: "+date+" duration time: "+duration_time+" sport id: "+sid+" user id: "+uid+" route id: "+rid);

    }
}
