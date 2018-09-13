package com.unice.trip.db;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import java.util.List;

@Dao
public interface SharedSingleServiceDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public long InsertService(SharedSingleService service);

    @Query("Select * from sharedsingleservices where name=:serviceName")
    public SharedSingleService getServiceforServiceName(String serviceName);

    @Query("Select * from sharedsingleservices")
    public List<SharedSingleService> getServices();

    @Query("Delete from sharedsingleservices where name=:serviceName")
    public void removeService(String serviceName);

    @Query("Update sharedsingleservices set name=:serviceName,type=:serviceType,description=:serviceDescription,source=:sourceService,destination=:destinationService,location=:locationService,condition=:conditionService,startdate=:startDateService,enddate=:endDateService,cost=:serviceCost where sid=:serviceId")
    public void updateService(int serviceId, String serviceName, String serviceType, String serviceDescription, String sourceService, String destinationService, String locationService, String conditionService, String startDateService, String endDateService, Float serviceCost);

}
