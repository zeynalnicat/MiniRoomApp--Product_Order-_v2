package com.example.miniroomapp_product_order
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.miniroomapp_product_order.databinding.FragmentAddAccountBinding
import com.example.miniroomapp_product_order.db.RoomDB
import com.example.miniroomapp_product_order.db.entities.CustomerEntity
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class AddAccountFragment : Fragment() {
   private lateinit var binding: FragmentAddAccountBinding
   private var db: RoomDB? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAddAccountBinding.inflate(inflater)
        db = RoomDB.accessDB(requireContext())
        binding.btnSave2.setOnClickListener {
            CoroutineScope(Dispatchers.Main).launch {
              val insertion = db?.customerDao()!!.insert(CustomerEntity(name = binding.edtUsername.text.toString()))
                if (insertion!=-1L){
                    Snackbar.make(requireView(),"Successfully added", Snackbar.LENGTH_SHORT).show()
                }else{
                    Snackbar.make(requireView(),"There was an error in adding", Snackbar.LENGTH_SHORT).show()
                }

            }
        }

        return binding.root
    }


}