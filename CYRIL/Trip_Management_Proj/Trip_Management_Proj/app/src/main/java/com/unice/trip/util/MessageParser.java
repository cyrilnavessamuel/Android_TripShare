package com.unice.trip.util;

import android.util.Log;

import com.unice.trip.xml.Service;
import com.unice.trip.xml.Trip;

import java.util.ArrayList;
import java.util.List;

public class MessageParser {

    private static String messagebuffer="";
    private static int currentpart=0;
    public static String read(String message){
         String fullmessage="";

        String[] splitMessages=message.split(";");
            if (splitMessages[1].equalsIgnoreCase("<TripManager>") && currentpart == 0) {
                messagebuffer += message;
                currentpart = Integer.parseInt(splitMessages[2]) - 1;
            } else {
                messagebuffer += message;
                currentpart=currentpart-1;
            }
        if (currentpart == 0) {
            fullmessage = messagebuffer;
            messagebuffer = "";
        }
         return fullmessage;
    }

    public static Object getObjectFormat(String Message){
        Object object =null;
        String[] splitMessages = Message.split(";");
        String[] cleanMessages =new String[splitMessages.length-Integer.parseInt(splitMessages[2])];
        int k=0;
        for(String str:splitMessages) {
            if (!str.equalsIgnoreCase("TripXP")) {
                cleanMessages[k] = str;
                k++;
            }
        }
        if(cleanMessages[3].equalsIgnoreCase("<Trip>")) {
            Trip trip = new Trip();
            trip.setSender(cleanMessages[2]);
            trip.setName(cleanMessages[4]);
            trip.setSource(cleanMessages[5]);
            trip.setDestination(cleanMessages[6]);
            trip.setStartdate(cleanMessages[7]);
            trip.setEnddate(cleanMessages[8]);
            if (cleanMessages[9].equalsIgnoreCase("</Trip>") && Integer.parseInt(cleanMessages[10]) > 0) {
                List<Service> services = new ArrayList<>();
                int j = 12;
                for (int i = 0; i < Integer.parseInt(cleanMessages[10]); i++) {
                    Service service = new Service();
                    service.setName(cleanMessages[j]);
                    service.setType(cleanMessages[j + 1]);
                    service.setDescription(cleanMessages[j + 2]);
                    service.setCondition(cleanMessages[j + 3]);
                    service.setSource(cleanMessages[j + 4]);
                    service.setDestination(cleanMessages[j + 5]);
                    service.setLocation(cleanMessages[j + 6]);
                    service.setStartDate(cleanMessages[j + 7]);
                    service.setEndDate(cleanMessages[j + 8]);
                    service.setCost(Float.parseFloat(cleanMessages[j + 9]));
                    services.add(service);
                    j = j + 12;
                }
                trip.setServices(services);
            }
            object=trip;
        }else if(cleanMessages[3].equalsIgnoreCase("<Service>")){
            Service service = new Service();
            service.setSender(cleanMessages[2]);
            service.setName(cleanMessages[4]);
            service.setType(cleanMessages[5]);
            service.setDescription(cleanMessages[6]);
            service.setCondition(cleanMessages[7]);
            service.setSource(cleanMessages[8]);
            service.setDestination(cleanMessages[9]);
            service.setLocation(cleanMessages[10]);
            service.setStartDate(cleanMessages[11]);
            service.setEndDate(cleanMessages[12]);
            service.setCost(Float.parseFloat(cleanMessages[13]));
            object=service;
        }
return object;
    }


}
