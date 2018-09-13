package com.unice.trip.db;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import java.util.List;

@Dao
public interface SharedTripDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public long InsertTrip(SharedTrip trip);

    @Query("Select * from sharedtrips where name=:tripName")
    public SharedTrip getTrip(String tripName);

    @Query("Select * from sharedtrips")
    public List<SharedTrip> getTrips();

    @Query("Delete from sharedtrips where name=:tripName")
    public void removeTrip(String tripName);

    @Query("Update sharedtrips set name=:tripName,source=:sourceTrip,destination=:destinationTrip,startdate=:startDateTrip,enddate=:endDateTrip where tid=:tripId")
    public void updateTrip(int tripId, String tripName, String sourceTrip, String destinationTrip, String startDateTrip, String endDateTrip);


}
