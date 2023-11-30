package com.example.miniroomapp_product_order.db.daos

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.miniroomapp_product_order.db.entities.CustomerEntity



@Dao
interface CustomerDao {


    @Insert
    suspend fun insert(customerEntity: CustomerEntity):Long

    @Query("Select name from customers")
    suspend fun selectNames():List<String>

    @Query("Select name from customers where customers.id = :id")
    suspend fun getName(id:Int):String

    @Query("Select id from customers where customers.name=:name")
    suspend fun selectId(name:String):Int
}