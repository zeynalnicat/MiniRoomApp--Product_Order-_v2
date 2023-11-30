package com.example.miniroomapp_product_order.models


data class Products(
    val id:Int,
    val name: String,
    val countOnStock: Int,
    var quantity : Int,
    var price : Int,
)
