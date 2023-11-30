package com.example.miniroomapp_product_order.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.miniroomapp_product_order.databinding.ListProductsBinding
import com.example.miniroomapp_product_order.db.RoomDB
import com.example.miniroomapp_product_order.models.Products


import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ProductListAdapter(private val productList: List<Products>, private val db: RoomDB) :
    RecyclerView.Adapter<ProductListAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            ListProductsBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }


    override fun getItemCount(): Int {
        return productList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val current = productList[position]
        return holder.bind(current)
    }


    inner class ViewHolder(private val binding: ListProductsBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(current: Products) {

            binding.txtProductName.text = current.name
            binding.txtOnStack.text = current.countOnStock.toString()
            binding.txtPrice.text = current.price.toString()
            binding.btnIncrease.setOnClickListener {

                if (current.quantity < current.countOnStock) {
                    CoroutineScope(Dispatchers.Main).launch {
                        current.quantity++
                        binding.txtProductCount.text = current.quantity.toString()



                    }

                }

            }
            binding.btnDecrease.setOnClickListener {
                if (current.quantity > 0) {
                    current.quantity--
                    binding.txtProductCount.text = current.quantity.toString()

                }
            }
        }

        fun getQuantity(current:Products):Int{
            return current.quantity
        }

    }

    fun getFiltered(): List<Products> {
        return productList.filter { it.quantity > 0 }
    }


}
