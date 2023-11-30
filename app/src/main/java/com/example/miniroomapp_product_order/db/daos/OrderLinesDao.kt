package com.example.miniroomapp_product_order.db.daos
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.example.miniroomapp_product_order.db.entities.OrderLinesEntity



@Dao
interface OrderLinesDao {

    @Insert
    suspend fun insert(order:List<OrderLinesEntity>):List<Long>
    @Query("Select quantity from `order lines` where orderId == :id")
    suspend fun selectAll(id: Int): Int

    @Query("Select productId from `order lines` where orderId==:id")
    suspend fun getProductId(id:Int):Int

    @Query("Select id from `Order Lines` where orderId==:id")
    suspend fun getId(id:Int):Int

    @Query("Select * from `order lines` where orderId==:id")
    suspend fun getAll(id:Int):OrderLinesEntity

    @Query("Select * from `order lines` where orderId==:id")
    suspend fun selectAllOrder(id:Int):MutableList<OrderLinesEntity>

    @Query("Select quantity from `order lines` where productId=:id And orderId=:orderID")
    suspend fun getQuantity(id:Int, orderID:Int):Int


    @Query("SELECT COUNT(*) FROM `order lines` where orderId =:id")
    suspend fun getCountOrderId(id:Int):Int

    @Query("SELECT SUM(totalPrice) FROM `order lines` WHERE orderId = :orderId")
    suspend fun getTotalPriceByOrderId(orderId: Int): Int

    @Delete
    suspend fun delete(order: OrderLinesEntity)
}