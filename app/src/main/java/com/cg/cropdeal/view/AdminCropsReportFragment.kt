package com.cg.cropdeal.view

import android.content.DialogInterface
import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.cg.cropdeal.R
import com.cg.cropdeal.databinding.FragmentAdminCropsReportBinding
import com.cg.cropdeal.model.Constants
import com.cg.cropdeal.model.CropsReportAdapter
import com.cg.cropdeal.model.MarketAdapter
import com.cg.cropdeal.model.UtilRepo
import com.cg.cropdeal.viewmodel.AdminReportVM


class AdminCropsReportFragment : Fragment() {

    private lateinit var binding:FragmentAdminCropsReportBinding
    private lateinit var viewModel:AdminReportVM
    private lateinit var progressDialog : AlertDialog
    private var cropForFilter:String = "All" //default
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        setHasOptionsMenu(true)
        // Inflate the layout for this fragment
        binding = FragmentAdminCropsReportBinding.inflate(inflater,container,false)
        return binding.root
         }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProvider(this).get(AdminReportVM::class.java)
        progressDialog = UtilRepo(activity?.application!!).loadingDialog(view.context)
        progressDialog.show()
        binding.cropsRview.layoutManager = LinearLayoutManager(context)
        viewModel.getCropList()?.observe(viewLifecycleOwner,{list->
            if(list.isEmpty() || list!=null) {

                        binding.cropsRview.adapter = CropsReportAdapter(list)
                        progressDialog.dismiss()

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
                .setItems(
                    it.toTypedArray(),
                    DialogInterface.OnClickListener { _, which ->
                        cropForFilter = it[which]
                        Log.d("cropFilter", cropForFilter)

                        val list = viewModel.getFilteredCropList(cropForFilter)

                        binding.cropsRview.adapter = CropsReportAdapter(list)
                        //progressDialog.dismiss()


                    })
        })

        builder.create().show()


        return true
    }
    companion object {

    }
}