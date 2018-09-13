package com.unice.trip.xml;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.Index;
import android.arch.persistence.room.PrimaryKey;

import com.unice.trip.db.Trip;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

import static android.arch.persistence.room.ForeignKey.CASCADE;

@Root
public class Service {


    @Element
    String name;

    @Element
    String type;

    @Element
    String description;

    @Element
    String source;

    @Element
    String destination;

    @Element
    String location;

    @Element
    String condition;

    @Element
    String startDate;

    @Element
    String endDate;

    @Element
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

    public Service(String name, String type, String description, String source, String destination, String location, String condition, String startDate, String endDate, float cost){
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
    }

    public Service(){
        this.name=null;
        this.type=null;
        this.description=null;
        this.source=null;
        this.destination=null;
        this.location=null;
        this.condition=null;
        this.startDate=null;
        this.endDate=null;
        this.cost=null;
    }


}
