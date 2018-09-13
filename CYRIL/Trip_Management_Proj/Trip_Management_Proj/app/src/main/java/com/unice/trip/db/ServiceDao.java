package com.unice.trip.db;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import java.util.List;

@Dao
public interface ServiceDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public void InsertService(Service service);

    @Query("Select * from services where name=:serviceName")
    public Service getServiceforServiceName(String serviceName);

    @Query("Select * from services where tripId=:tripServiceId")
    public List<Service> getServiceforTripId(int tripServiceId);

    @Query("Select * from services")
    public List<Service> getServices();

    @Query("Update services set tripId=:tripid where sid =:siid ")
    public void updateServiceWithTrip(int tripid,int siid);

    @Query("Delete from services where name=:serviceName")
    public void removeService(String serviceName);

    @Query("Update services set name=:serviceName,type=:serviceType,description=:serviceDescription,source=:sourceService,destination=:destinationService,location=:locationService,condition=:conditionService,startdate=:startDateService,enddate=:endDateService,cost=:serviceCost where sid=:serviceId")
    public void updateService(int serviceId,String serviceName,String serviceType,String serviceDescription,String sourceService,String destinationService,String locationService,String conditionService,String startDateService,String endDateService,Float serviceCost);

}
