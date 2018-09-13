package com.unice.trip.db;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

@Entity(tableName = "users")
public class User {

    @PrimaryKey(autoGenerate = true)
    public int id;

    public String name;

    public String email;

    public void setName(String name ){
        this.name=name;
    }

    public void setEmail(String email){
        this.email=email;
    }

    public String getName(){
        return name;
    }

    public String getEmail(){
        return email;
    }
}
