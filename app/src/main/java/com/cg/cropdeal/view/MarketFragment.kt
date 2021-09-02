package com.cg.cropdeal.view


import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.fragment.app.Fragment
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.cg.cropdeal.R
import com.cg.cropdeal.databinding.FragmentMarketBinding
import com.cg.cropdeal.model.MarketAdapter
import com.cg.cropdeal.model.UtilRepo
import com.cg.cropdeal.viewmodel.MarketVM
import android.view.MenuInflater
import com.cg.cropdeal.model.Constants


class MarketFragment : Fragment() {

    private lateinit var binding : FragmentMarketBinding
    private lateinit var viewModel : MarketVM
    private lateinit var progressDialog : AlertDialog
    private var cropForFilter:String = "Potato" //default
    private var userType = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        setHasOptionsMenu(true)
        binding = FragmentMarketBinding.inflate(inflater,container,false)
        viewModel = ViewModelProvider(this).get(MarketVM::class.java)
        userType = activity?.getSharedPreferences("LoginSharedPref",Context.MODE_PRIVATE)
             ?.getString("userType","")!!
        return binding.root
    }



    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        progressDialog = UtilRepo().loadingDialog(view.context)
        progressDialog.show()
        if(userType=="dealer")  binding.addCropsFAB.visibility = View.GONE
        binding.addCropsFAB.setOnClickListener {
            viewModel.areBankDetailsAvailable()?.observe(viewLifecycleOwner,{
                if(it!=null){
                    Log.d("Observable","bank - $it")
                    if(it==true)    Navigation.findNavController(view)
                        .navigate(R.id.action_nav_market_to_crop_publish)
                    else    {
                        val dialog = AlertDialog.Builder(view.context)
                        dialog.setTitle("You cannot Add new Crops without adding your bank account details")
                        dialog.setMessage("Do you want to add now?")
                        var dialogBuilder = dialog.create()
                        dialog.setPositiveButton("OK") { _, _ ->
                            Navigation.findNavController(view).navigate(R.id.action_nav_market_to_paymentDetailsFragment)
                        }
                        dialog.setNegativeButton("No"){_,_->
                            dialogBuilder.dismiss()
                        }
                        dialogBuilder = dialog.create()
                        dialogBuilder.setCancelable(false)
                        dialogBuilder.show()
                    }
                }
            })

        }

        loadCrops()


    }
    private fun loadCrops() {
        binding.marketRview.layoutManager = LinearLayoutManager(context)
        viewModel.getCropList()?.observe(viewLifecycleOwner,{list->
            if(list.isEmpty() || list!=null) {
                viewModel.areBankDetailsAvailable()?.observe(viewLifecycleOwner,{bank->

                    if(bank!=null){
                        binding.marketRview.adapter = MarketAdapter(list,bank)
                        progressDialog.dismiss()
                    }
                })
            }
        })
    }


    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.top_option_menu,menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when(item.itemId){
            R.id.filter_menu -> {
                filterCrops()
                true
            }
            else ->
                return super.onOptionsItemSelected(item)


        }

    }

    private fun filterCrops(): Boolean {
        val builder = AlertDialog.Builder(requireContext())
        Constants.cropsList.observe(viewLifecycleOwner,{
            builder.setTitle("Select any crop")
                .setItems(it.toTypedArray(),
                    DialogInterface.OnClickListener { _, which ->
                        cropForFilter = it[which]
                        Log.d("cropFilter",cropForFilter)

                        val list = viewModel.getFilteredList(cropForFilter)

                        viewModel.areBankDetailsAvailable()?.observe(viewLifecycleOwner,{bank->
                            Log.d("Observable","Second")
                            if(bank!=null){
                                binding.marketRview.adapter = MarketAdapter(list,bank)
                                progressDialog.dismiss()
                            }
                        })


                    })
        })

        builder.create().show()


            return true
    }



}