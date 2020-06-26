package com.example.pricequote.ui.invoice.fragment

import android.os.Bundle
import android.util.Log
import android.view.Gravity
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.homequote.data.InvoiceDetail
import com.example.pricequote.utilities.CAT
import com.example.pricequote.R
import com.example.pricequote.utilities.SIZE
import com.example.pricequote.utilities.TAG
import com.example.pricequote.data.InvoiceEntity
import com.example.pricequote.databinding.FragmentCategoryBinding
import com.example.pricequote.ui.invoice.InvoiceActivity
import com.example.pricequote.ui.invoice.InvoiceViewModel
import com.example.pricequote.ui.invoice.SectionsPagerAdapter
import com.example.pricequote.utilities.UIHelper
import kotlinx.android.synthetic.main.fragment_category.*


class CategoryFragment : Fragment(), SectionsPagerAdapter.EventListener {

    private lateinit var binding: FragmentCategoryBinding
    private lateinit var viewModel: InvoiceViewModel
    private lateinit var jsonConfig: List<InvoiceDetail.Space>
    private lateinit var currentInvoice: InvoiceEntity

    private var catBtn = mutableListOf<Button>()
    private lateinit var catBtnUnfocused: Button
    private lateinit var catBtnFocus: Button

    private var sizeBtn = mutableListOf<Button>()
    private lateinit var sizeBtnUnfocused: Button
    private lateinit var sizeBtnFocus: Button

    private var currentSpaceIdx: Int = 0
    private var currentCategoryIdx: Int = 0
    private var currentSizeIdx: Int = 0


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewModel = ViewModelProvider(requireActivity()).get(InvoiceViewModel::class.java)

        viewModel.invoiceDetailData.observe(viewLifecycleOwner, Observer {
            jsonConfig = it
        })

        binding = FragmentCategoryBinding.inflate(inflater, container, false).also{

            // Set up view model observer
            viewModel.currentInvoice.observe(viewLifecycleOwner, Observer { invoice ->
                currentInvoice = invoice

                for((count, space) in jsonConfig.withIndex()){
                    if(space.spaceName.equals(currentInvoice.spaceName, ignoreCase = true)){
                        currentSpaceIdx = count
                        break
                    }
                }
                drawCategoriesButtons()

                catBtnFocus = catBtn[0]
                currentCategoryIdx = 0
                if (currentInvoice.categoryName == "") {
                    catBtnFocus = catBtn[0]
                    currentCategoryIdx = 0
                } else {
                    for ((count, btn) in catBtn.withIndex()) {
                        if (btn.text.toString().equals(currentInvoice.categoryName!!, true)) {
                            currentCategoryIdx = count
                            catBtnFocus = btn
                            break
                        }
                    }
                }
                setFocusButtonsGroup(catBtnUnfocused, catBtnFocus,
                    CAT
                )
                drawSizesButtons()

                if (currentInvoice.size == null) {
                    sizeBtnFocus = sizeBtn[0]
                    currentSizeIdx = 0
                } else {
                    for ((count, btn) in sizeBtn.withIndex()) {
                        if (btn.text.toString().contains(
                                currentInvoice.size!!.sizeName.toString(),
                                ignoreCase = true
                            )
                        ) {
                            currentSizeIdx = count
                            sizeBtnFocus = btn
                            break
                        }
                    }
                }
                setFocusButtonsGroup(sizeBtnUnfocused, sizeBtnFocus,
                    SIZE
                )
                saveLocalFragmentUpdates()
            })
        }

        // Inflate the layout for this fragment
        return binding.root
    }

    private fun drawCategoriesButtons() {
        val currentCategoryList = jsonConfig[currentSpaceIdx].categoryList

        val params = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        params.setMargins(10, 0, 10, 10)
        params.gravity = Gravity.CENTER

        val paramsBtn = LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT, 1.0F)
        paramsBtn.setMargins(10, 0, 10, 0)
        paramsBtn.gravity = Gravity.CENTER

        var hzLinearLayout = LinearLayout(activity)
        hzLinearLayout.layoutParams = params
        hzLinearLayout.orientation = LinearLayout.HORIZONTAL

        catBtn = mutableListOf()

        categoryLinearLayout.removeAllViews()

        for ((count, cat) in currentCategoryList!!.withIndex()) {
            val catButton = UIHelper.customButton(requireActivity(), count, cat.categoryName, paramsBtn)
            catButton.setOnClickListener(clickListenerCats)

            if (count % 2 == 0) {
                categoryLinearLayout.addView(hzLinearLayout)
                hzLinearLayout = LinearLayout(activity)
                hzLinearLayout.layoutParams = params
                hzLinearLayout.orientation = LinearLayout.HORIZONTAL
            }
            hzLinearLayout.addView(catButton)
            catBtn.add(catButton)
        }
        categoryLinearLayout.addView(hzLinearLayout)
        catBtnUnfocused = catBtn[0]
    }

    private val clickListenerCats = View.OnClickListener { view ->
        if (catBtn[view.id] != catBtnUnfocused) {
            currentCategoryIdx = view.id
            //currentSizeIdx = 0

            setFocusButtonsGroup(catBtnUnfocused, catBtn[view.id],
                CAT
            )

            drawSizesButtons()
            setFocusButtonsGroup(sizeBtnUnfocused, sizeBtn[currentSizeIdx],
                SIZE
            )
        }
    }

    private fun drawSizesButtons() {
        val currentSizeList = jsonConfig[currentSpaceIdx].categoryList?.get(currentCategoryIdx)?.sizeList

        val params = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        params.setMargins(10, 0, 10, 10)
        params.gravity = Gravity.CENTER

        val paramsBtn = LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 1.0F)
        paramsBtn.setMargins(10, 0, 10, 0)
        paramsBtn.gravity = Gravity.START

        var hzLinearLayout = LinearLayout(activity)
        hzLinearLayout.layoutParams = params
        hzLinearLayout.orientation = LinearLayout.HORIZONTAL

        sizeBtn = mutableListOf()

        sizeLinearLayout.removeAllViews()

        for ((count, size) in currentSizeList!!.withIndex()) {
            val sizeButton = UIHelper.customButton(
                requireActivity(),
                count,
                "${size.sizeName}\n" +
                        "${size.minSqm}${if (size.maxSqm != null) "-" + size.maxSqm else "+"} sqm",
                paramsBtn
            )
            sizeButton.setOnClickListener(clickListenerSizes)

            if (count % 2 == 0) {
                sizeLinearLayout.addView(hzLinearLayout)
                hzLinearLayout = LinearLayout(activity)
                hzLinearLayout.layoutParams = params
                hzLinearLayout.orientation = LinearLayout.HORIZONTAL
            }
            hzLinearLayout.addView(sizeButton)
            sizeBtn.add(sizeButton)
        }
        sizeLinearLayout.addView(hzLinearLayout)
        sizeBtnUnfocused = sizeBtn[0]
    }

    private val clickListenerSizes = View.OnClickListener { view ->
        if (sizeBtn[view.id] != sizeBtnUnfocused) {
            currentSizeIdx = view.id
            setFocusButtonsGroup(sizeBtnUnfocused, sizeBtn[currentSizeIdx],
                SIZE
            )
        }
    }

    private fun setFocusButtonsGroup(
        btnUnfocused: Button,
        btnFocus: Button,
        buttonsGroupName: Int
    ) {
        when (buttonsGroupName) {
            CAT -> {
                this.catBtnUnfocused = btnFocus
                updateCat()
            }
            SIZE -> {
                this.sizeBtnUnfocused = btnFocus
                updateSize()
            }
        }
        btnUnfocused.setTextColor(resources.getColor(R.color.colorPrimary, activity?.theme))
        btnUnfocused.setBackgroundResource(R.drawable.btn_space_released)

        btnFocus.setTextColor(resources.getColor(R.color.white, activity?.theme))
        btnFocus.setBackgroundResource(R.drawable.btn_space_pressed)

        (activity as InvoiceActivity).updatePrice()
    }

    companion object {
        @JvmStatic
        fun newInstance() = CategoryFragment()
    }

    private fun updateCat(){
        Log.i(TAG,"saveLocalFragmentUpdates: CategoryFragment CAT -> ${jsonConfig[currentSpaceIdx].categoryList?.get(currentCategoryIdx)?.categoryName}")
        currentInvoice.categoryName = jsonConfig[currentSpaceIdx].categoryList?.get(currentCategoryIdx)?.categoryName
    }

    private fun updateSize(){
        Log.i(TAG,"saveLocalFragmentUpdates: CategoryFragment SIZE -> ${jsonConfig[currentSpaceIdx].categoryList?.get(currentCategoryIdx)?.sizeList?.get(currentSizeIdx)}")
        currentInvoice.size = jsonConfig[currentSpaceIdx].categoryList?.get(currentCategoryIdx)?.sizeList?.get(currentSizeIdx)
    }

    override fun saveLocalFragmentUpdates() {
        Log.i(TAG,"saveLocalFragmentUpdates: CategoryFragment")
        currentInvoice.categoryName = jsonConfig[currentSpaceIdx].categoryList?.get(currentCategoryIdx)?.categoryName
        currentInvoice.size = jsonConfig[currentSpaceIdx].categoryList?.get(currentCategoryIdx)?.sizeList?.get(currentSizeIdx)
    }
}