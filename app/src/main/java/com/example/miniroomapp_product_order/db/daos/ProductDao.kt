package com.example.miniroomapp_product_order.db.daos

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.miniroomapp_product_order.db.entities.ProductEntity



@Dao
interface ProductDao {

    @Query("Select * from products")
    suspend fun selectAll(): List<ProductEntity>

    @Insert
    suspend fun insert(product: ProductEntity): Long

    @Query("Select id from products where products.name=:name")
    suspend fun findId(name: String): Int


    @Query("Select name from products where products.id=:id")
    suspend fun getName(id: Int): String

    @Query("Select price from products where id=:id")
    suspend fun getPrice(id: Int): Int

    @Query("Select countOnStock from products where id=:id")
    suspend fun selectCountOnStock(id: Int): Int

    @Query("UPDATE Products SET countOnStock = countOnStock - :amount WHERE id = :id")
    suspend fun decrementStock(id: Int, amount: Int)
}