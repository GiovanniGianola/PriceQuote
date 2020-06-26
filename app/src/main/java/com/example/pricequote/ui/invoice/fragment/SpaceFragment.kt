package com.example.pricequote.ui.invoice.fragment

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.homequote.data.InvoiceDetail
import com.example.pricequote.R
import com.example.pricequote.utilities.TAG
import com.example.pricequote.data.InvoiceEntity
import com.example.pricequote.databinding.FragmentSpaceBinding
import com.example.pricequote.ui.invoice.InvoiceActivity
import com.example.pricequote.ui.invoice.InvoiceViewModel
import com.example.pricequote.ui.invoice.SectionsPagerAdapter
import com.example.pricequote.utilities.UIHelper
import kotlinx.android.synthetic.main.fragment_space.*

class SpaceFragment : Fragment(), SectionsPagerAdapter.EventListener {

    private lateinit var binding: FragmentSpaceBinding
    private lateinit var viewModel: InvoiceViewModel
    private lateinit var jsonConfig: List<InvoiceDetail.Space>
    private lateinit var currentInvoice: InvoiceEntity

    private var spaceBtn = mutableListOf<Button>()
    private lateinit var spaceBtnUnfocused: Button
    private lateinit var spaceBtnFocus: Button

    private var currentSpaceIdx: Int = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewModel = ViewModelProvider(requireActivity()).get(InvoiceViewModel::class.java)

        viewModel.invoiceDetailData.observe(viewLifecycleOwner, Observer {
            jsonConfig = it
            drawSpacesButtons()
        })

        binding = FragmentSpaceBinding.inflate(inflater, container, false).also{

            // Set up view model observer
            viewModel.currentInvoice.observe(viewLifecycleOwner, Observer { invoice ->
                currentInvoice = invoice

                if (currentInvoice.spaceName == "") {
                    spaceBtnFocus = spaceBtn[0]
                    currentSpaceIdx = 0
                } else {
                    for((count, btn) in spaceBtn.withIndex()) {
                        if(btn.text.toString().equals(currentInvoice.spaceName!!, true)) {
                            currentSpaceIdx = count
                            spaceBtnFocus = btn
                            break
                        }
                    }
                }
                setFocusButtonsGroup(spaceBtnUnfocused, spaceBtn[currentSpaceIdx])
            })
        }

        // Inflate the layout for this fragment
        return binding.root
    }

    private fun drawSpacesButtons() {
        val params = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        params.setMargins(10, 15, 10, 10)

        for ((count, space) in jsonConfig.withIndex()) {
            val spaceButton = UIHelper.customButton(requireActivity(), count, space.spaceName, params)
            spaceButton.setOnClickListener(clickListenerSpaces)
            spaceLinearLayout.addView(spaceButton)
            spaceBtn.add(spaceButton)
        }
        spaceBtnUnfocused = spaceBtn[0]
    }

    private val clickListenerSpaces = View.OnClickListener { view ->
        if (spaceBtn[view.id] != spaceBtnUnfocused) {
            currentSpaceIdx = view.id
            updateCatSizeOnClick()
            setFocusButtonsGroup(spaceBtnUnfocused, spaceBtn[currentSpaceIdx])
        }
    }

    private fun setFocusButtonsGroup(
        btnUnfocused: Button,
        btnFocus: Button
    ) {
        //Log.i(TAG, "setFocus, currentSpaceIdx: $currentSpaceIdx")
        //Log.i(TAG, "btnUnfocused: $btnUnfocused")
        //Log.i(TAG, "btnFocus: $btnFocus")
        this.spaceBtnUnfocused = btnFocus
        saveLocalFragmentUpdates()

        btnUnfocused.setTextColor(resources.getColor(R.color.colorPrimary, activity?.theme))
        btnUnfocused.setBackgroundResource(R.drawable.btn_space_released)

        btnFocus.setTextColor(resources.getColor(R.color.white, activity?.theme))
        btnFocus.setBackgroundResource(R.drawable.btn_space_pressed)

        (activity as InvoiceActivity).updatePrice()
    }

    private fun updateCatSizeOnClick(){
        currentInvoice.categoryName = jsonConfig[currentSpaceIdx].categoryList?.get(0)?.categoryName
        currentInvoice.size = jsonConfig[currentSpaceIdx].categoryList?.get(0)?.sizeList?.get(0)
    }

    override fun saveLocalFragmentUpdates() {
        Log.i(TAG,"saveLocalFragmentUpdates: SpaceFragment -> ${jsonConfig[currentSpaceIdx].spaceName}")
        currentInvoice.spaceName = jsonConfig[currentSpaceIdx].spaceName

        if(currentInvoice.categoryName?.isEmpty()!!) updateCatSizeOnClick()
    }

    companion object {
        @JvmStatic
        fun newInstance() = SpaceFragment()
    }
}