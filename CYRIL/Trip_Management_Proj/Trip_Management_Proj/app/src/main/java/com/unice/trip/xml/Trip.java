package com.unice.trip.xml;


import org.simpleframework.xml.Element;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

import java.util.List;

@Root
public class Trip {

    @Element
    public String name;
    @Element
    public String source;
    @Element
    public String destination;
    @Element
    public String startdate;
    @Element
    public String enddate;
    @ElementList
    List<Service> services;

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String sender;

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

    public List<Service> getServices() {
        return services;
    }

    public void setServices(List<Service> services) {
        this.services = services;
    }
    public Trip(String name, String source, String destination, String startdate, String enddate){
        this.name=name;
        this.source=source;
        this.destination=destination;
        this.startdate=startdate;
        this.enddate=enddate;
    }

    public Trip(){
        this.name=null;
        this.source=null;
        this.destination=null;
        this.startdate=null;
        this.enddate=null;
        this.services=null;
    }

}
