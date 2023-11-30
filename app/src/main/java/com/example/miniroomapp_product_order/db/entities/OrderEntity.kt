package com.example.miniroomapp_product_order.db.entities


import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey


@Entity("Orders", foreignKeys = [ForeignKey(
    entity = CustomerEntity::class,
    parentColumns = ["id"],
    childColumns = ["customerID"]
)])
data class OrderEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val customerID : Int,
    val date: String


)
