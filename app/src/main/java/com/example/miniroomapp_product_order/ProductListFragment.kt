package com.example.miniroomapp_product_order

import android.app.AlertDialog
import android.app.DatePickerDialog
import android.icu.util.Calendar
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.miniroomapp_product_order.adapters.ProductListAdapter
import com.example.miniroomapp_product_order.databinding.DialogUserBinding
import com.example.miniroomapp_product_order.databinding.FragmentProductListBinding
import com.example.miniroomapp_product_order.db.RoomDB
import com.example.miniroomapp_product_order.db.entities.OrderEntity
import com.example.miniroomapp_product_order.db.entities.OrderLinesEntity
import com.example.miniroomapp_product_order.models.Products
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

import kotlinx.coroutines.withContext

class ProductListFragment : Fragment() {
    private lateinit var binding: FragmentProductListBinding
    private var db: RoomDB? = null
    private var adapter: ProductListAdapter? = null
    private var date: String? = null
    private var name: String? = null


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        binding = FragmentProductListBinding.inflate(inflater)
        db = RoomDB.accessDB(requireContext())
        setAdapter()
        setDialog()
        getSelectedItem()

        return binding.root
    }


    private fun setAdapter() {
        CoroutineScope(Dispatchers.IO).launch {
            val productLists = db?.productDao()?.selectAll() ?: emptyList()
            if (productLists.isNotEmpty()) {
                binding.txtNothing2.visibility = View.GONE
            } else {
                binding.txtNothing2.visibility = View.VISIBLE
            }
            withContext(Dispatchers.Main) {
                adapter = ProductListAdapter(productLists.map {
                    Products(
                        it.id,
                        it.name,
                        it.countOnStock,
                        0,
                        it.price
                    )
                },db!!)
                binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
                binding.recyclerView.adapter = adapter

            }
        }
    }

    private fun getSelectedItem() {
        binding.btnSave.setOnClickListener {
            val selectedItems = adapter?.getFiltered()


            CoroutineScope(Dispatchers.Main).launch {
                val customerName = name ?: ""
                val customerId = db?.customerDao()?.selectId(customerName) ?: 0
                val orderEntity = OrderEntity(customerID = customerId, date = date ?: "")
                val insertedOrderEntity = db?.orderDao()?.insert(orderEntity = orderEntity)
                val orderLinesEntity = selectedItems?.map {
                    OrderLinesEntity(
                        orderId = insertedOrderEntity!!.toInt(),
                        productId = it.id,
                        quantity = it.quantity,
                        totalPrice = it.price*it.quantity
                    )
                }

                if(selectedItems!!.isNotEmpty()) {
                    val insertion = db?.orderLinesDao()?.insert(orderLinesEntity!!)
                    selectedItems.forEach{
                        var countOnStack = db?.productDao()!!.selectCountOnStock(it.id)
                        if(countOnStack - it.quantity >=0){
                            db?.productDao()!!.decrementStock(it.id,it.quantity)
                        }
                    }
                    if (!insertion!!.contains(-1L)) {
                        Snackbar.make(requireView(), "Successfully saved! ", Snackbar.LENGTH_SHORT)
                            .show()
                    } else {
                        Snackbar.make(
                            requireView(),
                            "There was an error in insertion! ",
                            Snackbar.LENGTH_SHORT
                        ).show()
                    }
                }


            }

        }

        }


    private fun setDialog() {
        if (name == null) {
            val dialogBinding = DialogUserBinding.inflate(layoutInflater)
            val dialogView = dialogBinding.root
            val builder = AlertDialog.Builder(requireContext()).setView(dialogView)
            val dialog = builder.create()
            setSpinner(dialogBinding.spinner)
            dialogBinding.button2.setOnClickListener {
                name = dialogBinding.spinner.selectedItem.toString()
                dialog.dismiss()
            }
            dialog.show()

            dialogBinding.edt.setOnClickListener {
                val c = Calendar.getInstance()
                val mYear = c.get(Calendar.YEAR)
                val mMonth = c.get(Calendar.MONTH)
                val mDay = c.get(Calendar.DAY_OF_MONTH)
                val datePickerDialog = DatePickerDialog(
                    requireContext(),
                    { view, year, monthOfYear, dayOfMonth ->
                        date = "$dayOfMonth/${monthOfYear + 1}/$year"
                        dialogBinding.edt.setText(date)
                    },
                    mYear, mMonth, mDay
                )
                date = dialogBinding.edt.text.toString()
                datePickerDialog.show()
            }
        }
    }

    private fun setSpinner(spinner: Spinner) {
        CoroutineScope(Dispatchers.Main).launch {
            val customers = db?.customerDao()!!.selectNames()
            val arrayAdapter = ArrayAdapter(
                requireContext(),
                androidx.transition.R.layout.support_simple_spinner_dropdown_item,
                customers
            )
            spinner.adapter = arrayAdapter
            spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {

                }

                override fun onNothingSelected(p0: AdapterView<*>?) {

                }

            }
        }


    }
}



