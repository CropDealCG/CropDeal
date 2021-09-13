package com.cg.cropdeal.view.user


import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.cg.cropdeal.R
import com.cg.cropdeal.databinding.FragmentMarketBinding
import com.cg.cropdeal.model.adapter.MarketAdapter
import com.cg.cropdeal.model.repo.UtilRepo
import com.cg.cropdeal.viewmodel.MarketVM
import android.view.MenuInflater
import com.cg.cropdeal.model.Constants


class MarketFragment : Fragment() {

    private lateinit var binding : FragmentMarketBinding
    private lateinit var viewModel : MarketVM
    private lateinit var progressDialog : AlertDialog
    private var userType = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        setHasOptionsMenu(true)
        binding = FragmentMarketBinding.inflate(inflater,container,false)
        viewModel = ViewModelProvider(this).get(MarketVM::class.java)
        userType = activity?.getSharedPreferences(Constants.LOGINPREF,Context.MODE_PRIVATE)
             ?.getString(Constants.USERTYPE,"")!!
        return binding.root
    }



    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        progressDialog = UtilRepo().loadingDialog(view.context)
//        progressDialog.show()
        viewModel.isDataChanged()!!.observe(viewLifecycleOwner,{
            if(it!=null && it) {
                progressDialog.show()
            }
            else    progressDialog.dismiss()
        })
        if(userType==Constants.DEALER)  binding.addCropsFAB.visibility = View.GONE
        binding.addCropsFAB.setOnClickListener {
            viewModel.areBankDetailsAvailable()?.observe(viewLifecycleOwner,{
                if(it!=null){
                    if(it==true)    Navigation.findNavController(view)
                        .navigate(R.id.action_nav_market_to_crop_publish)
                    else    {
                        val dialog = AlertDialog.Builder(view.context)
                        dialog.setTitle(requireContext().getString(R.string.noBankDialogTitle))
                        dialog.setMessage(requireContext().getString(R.string.noBankDialogMessage))
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
                        val cropForFilter = it[which]

                        val list = viewModel.getFilteredList(cropForFilter)

                        viewModel.areBankDetailsAvailable()?.observe(viewLifecycleOwner,{bank->
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