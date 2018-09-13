package com.unice.trip.db;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.arch.persistence.room.Relation;

import java.util.List;

@Entity(tableName="trips")
public class Trip {

    @PrimaryKey(autoGenerate = true)
    int tid;

    public String name;
    public String source;
    public String destination;
    public String startdate;
    public String enddate;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }


    public String getStartdate() {
        return startdate;
    }

    public void setStartdate(String startdate) {
        this.startdate = startdate;
    }


    public int getTId() {
        return tid;
    }

    public void setTId(int tid) {
        this.tid = tid;
    }

    public String getEnddate() {
        return enddate;
    }

    public void setEnddate(String enddate) {
        this.enddate = enddate;
    }


    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }
    public Trip(String name,String source,String destination,String startdate,String enddate){
        this.name=name;
        this.source=source;
        this.destination=destination;
        this.startdate=startdate;
        this.enddate=enddate;
    }

}
