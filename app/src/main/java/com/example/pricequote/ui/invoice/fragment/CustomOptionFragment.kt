package com.example.pricequote.ui.invoice.fragment

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.homequote.data.InvoiceDetail
import com.example.pricequote.TAG
import com.example.pricequote.data.InvoiceEntity
import com.example.pricequote.databinding.FragmentCustomOptionBinding
import com.example.pricequote.ui.invoice.InvoiceActivity
import com.example.pricequote.ui.invoice.InvoiceViewModel
import com.example.pricequote.ui.invoice.SectionsPagerAdapter
import com.example.pricequote.utilities.ExpandableListViewHelper
import kotlinx.android.synthetic.main.fragment_custom_option.*


class CustomOptionFragment : Fragment(), SectionsPagerAdapter.EventListener, ExpandableListAdapter.EventListener {

    private lateinit var binding: FragmentCustomOptionBinding
    private lateinit var viewModel: InvoiceViewModel
    private lateinit var currentInvoice: InvoiceEntity
    private lateinit var currentCustomOptionConfig: List<InvoiceDetail.CustomOptionCategory>
    private lateinit var adapter: ExpandableListAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewModel = ViewModelProvider(requireActivity()).get(InvoiceViewModel::class.java)

        viewModel.invoiceCustomOptionData.observe(viewLifecycleOwner, Observer {
            currentCustomOptionConfig = it
            adapter = ExpandableListAdapter(
                activity as AppCompatActivity,
                currentCustomOptionConfig,
                this
            )
            expandableListView.setAdapter(adapter)
            ExpandableListViewHelper.getExpandableListViewSize(expandableListView, -1)
            expandableListView.setOnGroupClickListener { _, _, groupPosition, _ ->
                ExpandableListViewHelper.getExpandableListViewSize(
                    expandableListView,
                    groupPosition
                )
                false
            }
        })


        binding = FragmentCustomOptionBinding.inflate(inflater, container, false).also{

            viewModel.currentInvoice.observe(viewLifecycleOwner, Observer { invoice ->
                currentInvoice = invoice

                currentInvoice.size?.multiplier?.let { it1 -> adapter.updatePriceMultiplier(it1) }
                Log.i(TAG,"multi: ${currentInvoice.size?.multiplier}")
                if(currentInvoice.customOptionsList.isNullOrEmpty()) currentInvoice.customOptionsList = currentCustomOptionConfig
                adapter.updateData(currentInvoice.customOptionsList!!)
            })
        }
        // Inflate the layout for this fragment
        return binding.root
    }

    companion object {
        @JvmStatic
        fun newInstance() = CustomOptionFragment()
    }

    override fun saveLocalFragmentUpdates() {
        currentInvoice.customOptionsList = adapter.getData()
    }

    override fun onEvent() {
        saveLocalFragmentUpdates()
        (activity as InvoiceActivity).updatePrice()
    }
}