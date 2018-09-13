package com.unice.trip.db;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.Index;
import android.arch.persistence.room.PrimaryKey;

import static android.arch.persistence.room.ForeignKey.CASCADE;

@Entity(tableName="sharedsingleservices")
public class SharedSingleService {

    @PrimaryKey(autoGenerate = true)
    int sid;

    String name;

    String type;

    String description;

    String source;


    String destination;

    String location;

    String condition;

    String startDate;

    String endDate;

    Float cost;


    String sender;

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getCondition() {
        return condition;
    }

    public void setCondition(String condition) {
        this.condition = condition;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }



    public int getSId() {
        return sid;
    }

    public void setSId(int sid) {
        this.sid = sid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }


    public Float getCost() {
        return cost;
    }

    public void setCost(Float cost) {
        this.cost = cost;
    }

    public SharedSingleService(String name, String type, String description, String source, String destination, String location, String condition, String startDate, String endDate, float cost, String sender){
        this.name=name;
        this.type=type;
        this.description=description;
        this.source=source;
        this.destination=destination;
        this.location=location;
        this.condition=condition;
        this.startDate=startDate;
        this.endDate=endDate;
        this.cost=cost;
        this.sender=sender;
    }


}
