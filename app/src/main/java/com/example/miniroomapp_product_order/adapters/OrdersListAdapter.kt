package com.example.miniroomapp_product_order.adapters
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.miniroomapp_product_order.databinding.ListOrdersBinding
import com.example.miniroomapp_product_order.db.RoomDB
import com.example.miniroomapp_product_order.db.entities.OrderEntity

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class OrdersListAdapter(
    private val listOrder: MutableList<OrderEntity>,
    val db: RoomDB,
    val nav: (Bundle) -> Unit
) :
    RecyclerView.Adapter<OrdersListAdapter.ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ListOrdersBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return listOrder.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val current = listOrder[position]
        return holder.bind(current)
    }

    inner class ViewHolder(private val binding: ListOrdersBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(current: OrderEntity) {
            CoroutineScope(Dispatchers.Main).launch {
                val countOrderId = db.orderLinesDao().getCountOrderId(current.id)
                if (countOrderId > 0) {
                    binding.txtCustomerName.text = db.customerDao().getName(current.customerID)
                    binding.txtDate.text = current.date
                    binding.txtOrderID.text = current.id.toString()
                    binding.txtTotalPrice.text = db.orderLinesDao().getTotalPriceByOrderId(current.id).toString()
                    itemView.setOnClickListener {
                        val bundle = Bundle()
                        bundle.putString("orderID", current.id.toString())
                        nav(bundle)
                    }
                } else {
                    db.orderDao().delete(current)
                    listOrder.remove(current)
                    notifyDataSetChanged()
                }
            }

        }

    }
}