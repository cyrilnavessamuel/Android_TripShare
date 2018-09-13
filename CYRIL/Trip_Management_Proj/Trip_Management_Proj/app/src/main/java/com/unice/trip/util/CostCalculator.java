package com.unice.trip.util;

import com.unice.trip.db.Service;

import java.util.List;

public class CostCalculator {

    public static  String calculateCost(List<Service> services){
        String totalCost = null;
        float normalcost=0;
        float fidelitypoint=0;
        float carbon=0;
        for(Service service:services){
            if(service.getCost()!=null) {
                normalcost += calculateSpecialCost(service);
                fidelitypoint += calculateFidelity(service);
                carbon += calculateCarbon(service);
            }
        }
        totalCost="TripCost:"+normalcost+" "+"FidelityPoint:"+fidelitypoint+" "+"CarbonImpact:"+carbon;
        return totalCost;
    }

    public static float calculateSpecialCost(Service service) {
        float specialCost = 0;

        specialCost=service.getCost();


        return specialCost;

    }

    public static float calculateFidelity(Service service) {
        float specialCost = 0;

        if (service.getCondition()!=null&& service.getCondition().contains("fidelity")) {
            String[] costs = service.getCondition().split(":");
            if (costs != null && costs.length>0) {
                specialCost+=Float.parseFloat(costs[1]);
            }
        }
        return specialCost;
    }

    public static float calculateCarbon(Service service) {
        float specialCost = 0;

        if (service.getCondition()!=null&& service.getCondition().contains("carbon")) {
            String[] costs = service.getCondition().split(":");
            if (costs != null&&costs.length>0) {
                specialCost+=Float.parseFloat(costs[1]);
            }
        }
        return specialCost;

    }
}
