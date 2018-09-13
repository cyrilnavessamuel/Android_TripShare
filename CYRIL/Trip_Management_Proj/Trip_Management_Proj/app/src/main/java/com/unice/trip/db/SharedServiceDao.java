package com.unice.trip.db;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import java.util.List;

@Dao
public interface SharedServiceDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public long InsertService(SharedService service);

    @Query("Select * from sharedservices where name=:serviceName")
    public SharedService getServiceforServiceName(String serviceName);

    @Query("Select * from sharedservices where tripId=:tripServiceId")
    public List<SharedService> getServiceforTripId(int tripServiceId);

    @Query("Select * from sharedservices")
    public List<SharedService> getServices();

    @Query("Update sharedservices set tripId=:tripid where sid =:siid ")
    public void updateServiceWithTrip(int tripid, int siid);

    @Query("Delete from sharedservices where name=:serviceName")
    public void removeService(String serviceName);

    @Query("Update sharedservices set name=:serviceName,type=:serviceType,description=:serviceDescription,source=:sourceService,destination=:destinationService,location=:locationService,condition=:conditionService,startdate=:startDateService,enddate=:endDateService,cost=:serviceCost where sid=:serviceId")
    public void updateService(int serviceId, String serviceName, String serviceType, String serviceDescription, String sourceService, String destinationService, String locationService, String conditionService, String startDateService, String endDateService, Float serviceCost);

}
