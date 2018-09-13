package com.unice.trip.util;

import com.unice.trip.db.Service;
import com.unice.trip.db.SharedService;
import com.unice.trip.db.SharedTrip;
import com.unice.trip.db.Trip;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class ConsistencyValidator {

    public static Map<Boolean,Object>  isValidTrip(Trip trip ,List<Service> services){
        Map<Boolean,Object> validationResult = new HashMap<>();
        if(isValidTripOnly(trip).containsKey(false)){
                 validationResult.put(false,isValidTripOnly(trip).get(false));
                 return validationResult;
            }
        for(int i=0;i<services.size();i++){
            if(isValidServiceOnly(services.get(i)).containsKey(false)){
                validationResult.put(false,isValidServiceOnly(services.get(i)).get(false));
                return validationResult;

            }
            if(isValidServiceforTrip(services.get(i),trip).containsKey(false)){
                validationResult.put(false,isValidServiceforTrip(services.get(i),trip).get(false));
                return validationResult;

            }

        }
        validationResult = reorderService(trip,services);

        return validationResult;
    }

    public static Map<Boolean,Object> reorderService(Trip trip,List<Service> randomServices){
        Map<Boolean,Object> finalValue = new HashMap<>();
        String tripSource = trip.getSource();
        String tripDestination = trip.getDestination();
        List<Service> orderedServices= new ArrayList<>();
        List<Service> randomCopyServices= new ArrayList<>(randomServices);
        for(Service service:randomCopyServices) {
            //Add head service
            if (tripSource.equalsIgnoreCase(service.getSource()) && tripSource.equalsIgnoreCase(service.getLocation())) {
                orderedServices.add(service);
                randomServices.remove(service);
            } else if (tripSource.equalsIgnoreCase(service.getLocation())) {
                orderedServices.add(service);
                randomServices.remove(service);
            }
        }
            if(orderedServices.size()==0){
                finalValue.put(false,"Inconsistent seervice");
                return finalValue;
            }



        List<Service> iterableunorderedServices= new ArrayList<>(randomServices);
        boolean found = false;
        int iterateOutercount=0;
        int iterateInnercount=0;
                  for(int i=0;i<orderedServices.size();i++){
                    Service service = orderedServices.get(i);
                    if(!found &&iterateOutercount>iterateInnercount){
                        finalValue.put(false,"Inconsistent service");
                        return finalValue;

                    }
                    else{
                        found =false;
                        iterateOutercount++;
                    }
                      iterableunorderedServices = randomServices;
                      for(int j=0;j<iterableunorderedServices.size();j++){
                        iterateInnercount++;
                            Service unorderservice = iterableunorderedServices.get(j);
                            if (service.getDestination().equalsIgnoreCase(unorderservice.getSource()) &&
                                    service.getDestination().equalsIgnoreCase(unorderservice.getLocation())&&
                                    dateValidator(unorderservice.getStartDate(),service.getEndDate())) {
                                orderedServices.add(unorderservice);
                                randomServices.remove(unorderservice);
                                found = true;
                            } else if (service.getDestination().equalsIgnoreCase(unorderservice.getLocation())&&
                                    dateValidator(unorderservice.getStartDate(),service.getEndDate())) {
                                orderedServices.add(unorderservice);
                                randomServices.remove(unorderservice);
                                found = true;
                            }
                        }
                    }


        finalValue.put(true,orderedServices);
        return finalValue;

    }

    public static Map<Boolean,String>  isValidTripOnly(Trip trip){
        Map<Boolean,String> validationResult = new HashMap<>();
         if(trip!=null){
             if(trip.getSource().equalsIgnoreCase(trip.getDestination())){
                 validationResult.put(false,"Failed Trip! Since Source & Destination should not be same ");
                 return validationResult;
             }
             else if(!dateValidator(trip.getStartdate(),trip.getEnddate())){
                 validationResult.put(false,"Failed Trip! Start & End Date Invalid");
                 return validationResult;
             }
             else{
                 validationResult.put(true,"Passed Trip! Start & End Date Valid");
                 return validationResult;
             }
         }
        return validationResult;
    }

    public static Map<Boolean,String>  isValidServiceOnly(Service service){
        Map<Boolean,String> validationResult = new HashMap<>();
        if(service!=null){
            if(!service.getSource().isEmpty() && !service.getDestination().isEmpty()&& service.getSource().equalsIgnoreCase(service.getDestination())){
                validationResult.put(false,"Failed Service! Since Source & Destination should not be same ");
                return validationResult;
            }
            else if(!dateValidator(service.getStartDate(),service.getEndDate())){
                validationResult.put(false,"Failed Service! Start & End Date Invalid");
                return validationResult;
            }
        }

        return validationResult;
    }

    public static Map<Boolean,String>  isValidServiceforTrip(Service service,Trip trip){
        Map<Boolean,String> validationResult = new HashMap<>();
        if(service!=null && trip!=null ){
             if(!dateValidator(service.getStartDate(),trip.getStartdate())){
                validationResult.put(false,"Failed Trip & Service! Start Date Inconsistent");
                 return validationResult;

             }
            else if(!dateValidator(service.getEndDate(),trip.getEnddate())){
                validationResult.put(false,"Failed Trip & Service! End Date Inconsistent");
                 return validationResult;

             }
        }

        return validationResult;
    }




    public static boolean dateValidator(String start,String end){
        Integer[] starts =getIntArray(start.split("/"));
        Integer[] ends = getIntArray(end.split("/"));
        if(starts[2]>ends[2]){
            return false;
        }
        else if(starts[2].equals(ends[2])){
            if(starts[1]>ends[1]){
                return false;
            }
        }
        else if(starts[2].equals(ends[2]) && starts[1].equals(ends[1])){
            if(starts[0]>ends[0]){
                return false;
            }

        }
         return true;
    }

    public static Integer[] getIntArray(String[] input){
        Integer output[] = new Integer[input.length];
        int i=0;
        for(String inp:input){
            output[i]=Integer.parseInt(inp);
            i++;
        }
        i=0;
        return output;
    }

    public static Map<Boolean,Object> reorderSharedService(SharedTrip trip, List<SharedService> randomServices){
        Map<Boolean,Object> finalValue = new HashMap<>();
        String tripSource = trip.getSource();
        String tripDestination = trip.getDestination();
        List<SharedService> orderedServices= new ArrayList<>();
        List<SharedService> randomCopyServices= new ArrayList<>(randomServices);
        for(SharedService service:randomCopyServices){
            //Add head service
            if(tripSource.equalsIgnoreCase(service.getSource())&&tripSource.equalsIgnoreCase(service.getLocation())){
                orderedServices.add(service);
                randomServices.remove(service);
            }
            else  if(tripSource.equalsIgnoreCase(service.getLocation())){
                orderedServices.add(service);
                randomServices.remove(service);
            }
            if(orderedServices.size()==0){
                finalValue.put(false,"Inconsistent seervice");
                return finalValue;
            }


        }

        List<SharedService> iterableunorderedServices= new ArrayList<>(randomServices);

        boolean found = false;
        int iterateOutercount=0;
        int iterateInnercount=0;
        for(int i=0;i<orderedServices.size();i++){
            SharedService service = orderedServices.get(i);
            if(!found &&iterateOutercount>iterateInnercount){
                finalValue.put(false,"Inconsistent service");
                return finalValue;

            }
            else{
                found =false;
                iterateOutercount++;
            }
            iterableunorderedServices = randomServices;
            for(int j=0;j<iterableunorderedServices.size();j++){
                iterateInnercount++;
                    SharedService unorderservice = iterableunorderedServices.get(j);
                    if (service.getDestination().equalsIgnoreCase(unorderservice.getSource()) &&
                            service.getDestination().equalsIgnoreCase(unorderservice.getLocation())&&
                            dateValidator(unorderservice.getStartDate(),service.getEndDate())) {
                        orderedServices.add(unorderservice);
                        randomServices.remove(unorderservice);
                        found = true;
                    } else if (service.getDestination().equalsIgnoreCase(unorderservice.getLocation())&&
                            dateValidator(unorderservice.getStartDate(),service.getEndDate())) {
                        orderedServices.add(unorderservice);
                        randomServices.remove(unorderservice);
                        found = true;
                    }
                }
            }

        finalValue.put(true,orderedServices);
        return finalValue;

    }


}
