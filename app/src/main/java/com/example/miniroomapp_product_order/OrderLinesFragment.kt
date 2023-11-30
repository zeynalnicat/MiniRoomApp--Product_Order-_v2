package com.example.miniroomapp_product_order

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.miniroomapp_product_order.adapters.OrderLinesAdapter
import com.example.miniroomapp_product_order.databinding.FragmentOrderLinesBinding
import com.example.miniroomapp_product_order.db.RoomDB
import com.example.miniroomapp_product_order.db.entities.OrderLinesEntity

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class OrderLinesFragment : Fragment() {

    private lateinit var binding: FragmentOrderLinesBinding
    private var orderLists : MutableList<OrderLinesEntity>? = null
    private var adapter : OrderLinesAdapter? = null
    private var orderID :Int? = null
    private var db: RoomDB? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentOrderLinesBinding.inflate(inflater)
        db = RoomDB.accessDB(requireContext())
        getOrders()
        return binding.root
    }


    private fun getOrders(){

        arguments?.let {
            orderID =  it.getString("orderID")!!.toInt()
        }

        CoroutineScope(Dispatchers.Main).launch {
            orderLists = db?.orderLinesDao()!!.selectAllOrder(orderID!!)
            setAdapter(orderLists!!)
        }
    }

    private fun setAdapter(list:MutableList<OrderLinesEntity>){
          adapter = OrderLinesAdapter(list,db!!)
          binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
          binding.recyclerView.adapter = adapter

    }

}