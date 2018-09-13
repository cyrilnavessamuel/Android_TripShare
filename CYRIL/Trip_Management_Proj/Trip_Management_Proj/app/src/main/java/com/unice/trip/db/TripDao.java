package com.unice.trip.db;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import java.util.List;

@Dao
public interface TripDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public long InsertTrip(Trip trip);

    @Query("Select * from trips where name=:tripName")
    public Trip getTrip(String tripName);

    @Query("Select * from trips where tid=:tripId")
    public Trip getTripforID(int tripId);

    @Query("Select * from trips")
    public List<Trip> getTrips();

    @Query("Delete from trips where name=:tripName")
    public void removeTrip(String tripName);

    @Query("Update trips set name=:tripName,source=:sourceTrip,destination=:destinationTrip,startdate=:startDateTrip,enddate=:endDateTrip where tid=:tripId")
    public void updateTrip(int tripId,String tripName,String sourceTrip,String destinationTrip,String startDateTrip,String endDateTrip);


}
