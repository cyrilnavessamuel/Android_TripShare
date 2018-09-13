package com.unice.trip.db;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

@Database(entities = {Trip.class,User.class,Service.class,SharedTrip.class,SharedService.class,SharedSingleService.class},version =10)
public abstract class TripManagementDatabase extends RoomDatabase {

    private static TripManagementDatabase tripManagementDatabase;

    public abstract TripDao TripDao();

    public abstract UserDao UserDao();

    public abstract ServiceDao ServiceDao();

    public abstract SharedTripDao SharedTripDao();

    public abstract SharedServiceDao SharedServiceDao();

    public abstract SharedSingleServiceDao SharedSingleServiceDao();

    public static TripManagementDatabase getTripManagementDatabase(Context context){
        if(tripManagementDatabase==null){
            //tripManagementDatabase= Room.databaseBuilder(context.getApplicationContext(),TripManagementDatabase.class,"tripdatabase").fallbackToDestructiveMigration().build();
            tripManagementDatabase= Room.databaseBuilder(context.getApplicationContext(),TripManagementDatabase.class,"tripdatabase").allowMainThreadQueries().fallbackToDestructiveMigration().build();
        }
       return tripManagementDatabase;
    }
}
