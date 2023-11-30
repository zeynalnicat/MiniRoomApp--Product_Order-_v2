package com.example.miniroomapp_product_order.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.miniroomapp_product_order.databinding.ListOrderlinesBinding
import com.example.miniroomapp_product_order.db.RoomDB
import com.example.miniroomapp_product_order.db.entities.OrderLinesEntity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class OrderLinesAdapter(
    private val listOrderLines: MutableList<OrderLinesEntity>,
    private val db: RoomDB
) :
    RecyclerView.Adapter<OrderLinesAdapter.ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = ListOrderlinesBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return listOrderLines.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val current = listOrderLines[position]
        return holder.bind(current)
    }

    inner class ViewHolder(private val binding: ListOrderlinesBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: OrderLinesEntity) {
            binding.txtOrderLinesID.text = item.id.toString()
            binding.txtOrderId.text = item.orderId.toString()

            CoroutineScope(Dispatchers.Main).launch {
                val quantity = db.orderLinesDao().getQuantity(item.productId,item.orderId)
                val price  = db.productDao().getPrice(item.productId)
                binding.txtProduct.text = db.productDao().getName(item.productId)
                binding.txtQuantity.text = quantity.toString()
                binding.txtPrice.text = (quantity*price).toString()
                binding.btnDelete.setOnClickListener {
                    delete(item)
                }
            }
        }

        private fun delete(orderLines: OrderLinesEntity) {
            CoroutineScope(Dispatchers.Main).launch {
                db.orderLinesDao().delete(orderLines)
                listOrderLines.remove(orderLines)
                notifyDataSetChanged()
            }
        }
    }
}
