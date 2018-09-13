
package com.unice.trip.util;

import android.content.Context;
import android.util.Log;
import android.util.Xml;

import com.unice.trip.db.Service;
import com.unice.trip.db.Trip;
import com.unice.trip.db.TripManagementDatabase;

import org.simpleframework.xml.Serializer;
import org.simpleframework.xml.core.Persister;
import org.xmlpull.v1.XmlSerializer;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;

public final class XMLUtil {

    public static File  generateXML(Object object,List<Service> services,String path) {
        File xmlFile =null;
        if (object instanceof Service) {
            Service service = (Service)object;
            xmlFile=new File(path+service.getName());
            generateServiceXML(service,xmlFile);

        } else if (object instanceof Trip) {
            Trip trip = (Trip)object;
            xmlFile=new File(path+"/"+trip.getName()+".xml");
            generateTripXML(trip,services,xmlFile);

        }
        return xmlFile;


    }

    private static  File  generateServiceXML(Service service,File file){

        com.unice.trip.xml.Service xmlService = new com.unice.trip.xml.Service();
        xmlService.setName(service.getName());
        xmlService.setType(service.getType());
        xmlService.setCondition(service.getCondition());
        xmlService.setDescription(service.getDescription());
        xmlService.setCost(service.getCost());
        xmlService.setSource(service.getSource());
        xmlService.setDestination(service.getDestination());
        xmlService.setLocation(service.getLocation());
        xmlService.setStartDate(service.getStartDate());
        xmlService.setEndDate(service.getEndDate());

        Serializer serializer = new Persister();
        try {
            serializer.write(xmlService, file);
        }catch(Exception e){
            Log.d("XMLUTIL",e.getMessage());
        }
        return file;
    }

    private static  File generateTripXML(Trip trip,List<Service> services,File file){

        com.unice.trip.xml.Trip xmlTrip = new com.unice.trip.xml.Trip();
        xmlTrip.setName(trip.getName());
        xmlTrip.setSource(trip.getSource());
        xmlTrip.setDestination(trip.getDestination());
        xmlTrip.setStartdate(trip.getStartdate());
        xmlTrip.setEnddate(trip.getEnddate());
        xmlTrip.setServices(converttoXMLServices(services));
        Serializer serializer = new Persister();
        try {
            serializer.write(xmlTrip, file);
        }catch(Exception e){
            Log.d("XMLUTIL",e.getMessage());
        }
        return file;
    }

    private static List<com.unice.trip.xml.Service> converttoXMLServices(List<Service> services){

        List<com.unice.trip.xml.Service> xmlServices = new ArrayList<>();
        for(Service service:services){
            com.unice.trip.xml.Service xmlService = new com.unice.trip.xml.Service();
            xmlService.setName(service.getName());
            xmlService.setType(service.getType());
            xmlService.setCondition(service.getCondition());
            xmlService.setDescription(service.getDescription());
            xmlService.setCost(service.getCost());
            xmlService.setSource(service.getSource());
            xmlService.setDestination(service.getDestination());
            xmlService.setLocation(service.getLocation());
            xmlService.setStartDate(service.getStartDate());
            xmlService.setEndDate(service.getEndDate());
            xmlServices.add(xmlService);
        }
        return xmlServices;
    }




/*private FileOutputStream generateServiceXML(Service service){
        String fileName="service.xml";
        try {
            FileOutputStream fileOutputStream = new FileOutputStream(fileName);
            XmlSerializer xmlSerializer = Xml.newSerializer();
            StringWriter stringWriter = new StringWriter();
            xmlSerializer.setOutput(stringWriter);
            xmlSerializer.startDocument("UTF-8",true);
            xmlSerializer.startTag(null,"TripManagement");
            xmlSerializer.startTag(null,"service");
            xmlSerializer.startTag(null,"ServiceName");
            xmlSerializer.text(service.getName());
            xmlSerializer.endTag(null,"ServiceName");
            xmlSerializer.startTag(null,"ServiceType");
            xmlSerializer.endTag(null,"ServiceType");
            xmlSerializer.startTag(null,"ServiceType");
            xmlSerializer.endTag(null,"ServiceType");
            xmlSerializer.startTag(null,"Service");


            fileOutputStream.write();

        }
        catch(IOException fileNotFoundException){
            Log.d("ServiceXML",fileNotFoundException.getMessage());
        }

                return null;
    }*/



}

