package com.example.pricequote.ui.invoice.fragment

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseExpandableListAdapter
import android.widget.CheckBox
import android.widget.TextView
import com.example.homequote.data.InvoiceDetail
import com.example.pricequote.R


class ExpandableListAdapter(
    activity: Activity,
    private var customOptionsList: List<InvoiceDetail.CustomOptionCategory>,
    private var listener: CustomOptionFragment
) : BaseExpandableListAdapter() {

    private val inflater: LayoutInflater = activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
    private var childItem: InvoiceDetail.CustomOptionSubCategory = InvoiceDetail.CustomOptionSubCategory()
    private var count: Int = 0
    private var multiplier: Double = 1.0
    private var totalPrice: Double = 0.0
    private val countArray: MutableList<Int> = mutableListOf()

    init {
        for((count, co) in customOptionsList.withIndex()){
            countArray.add(0)
            for(subCo in co.subCategoryList) {
                if (subCo.isChecked) {
                    totalPrice += subCo.subCategoryBasePrice * multiplier
                    countArray[count] += 1
                }
            }
        }
    }

    fun updatePriceMultiplier(newMultiplier: Double){
        if(this.multiplier == newMultiplier)
            return
        this.multiplier = newMultiplier
        notifyDataSetChanged()
    }

    fun updateData(newCustomOptionsList: List<InvoiceDetail.CustomOptionCategory>){
        customOptionsList = newCustomOptionsList
        listener.onEvent()
        updateCountArray()
        notifyDataSetChanged()
    }

    fun getData(): List<InvoiceDetail.CustomOptionCategory>{
        return customOptionsList
    }

    fun cleanAllCheckBox(){
        for((count, co) in customOptionsList.withIndex()){
            co.isChecked = false
            countArray[count] = 0
            for(subCo in co.subCategoryList){
                subCo.isChecked = false
            }
        }
        notifyDataSetChanged()
    }

    private fun getCheckedPrice(): Double{
        totalPrice = 0.0
        for(co in customOptionsList){
            for(subCo in co.subCategoryList){
                if(subCo.isChecked)
                    totalPrice += subCo.subCategoryBasePrice * multiplier
            }
        }
        return totalPrice
    }

    private fun updateCountArray(){
        for((count, co) in customOptionsList.withIndex()){
            countArray[count] = 0
            for(subCo in co.subCategoryList) {
                if (subCo.isChecked) {
                    countArray[count] += 1
                }
            }
        }
    }

    override fun getGroupCount(): Int {
        return customOptionsList.size
    }

    override fun getChildrenCount(groupPosition: Int): Int {
        return customOptionsList[groupPosition].subCategoryList.size
    }

    private fun getChildren(groupPosition: Int): List<InvoiceDetail.CustomOptionSubCategory>{
        return customOptionsList[groupPosition].subCategoryList
    }

    override fun getGroup(i: Int): Any? {
        return null
    }

    override fun getChild(groupPosition: Int, childPosition: Int): InvoiceDetail.CustomOptionSubCategory {
        return customOptionsList[groupPosition].subCategoryList[childPosition]
    }

    override fun getGroupId(i: Int): Long {
        return 0
    }

    override fun getChildId(i: Int, i1: Int): Long {
        return 0
    }

    override fun hasStableIds(): Boolean {
        return false
    }

    @SuppressLint("InflateParams")
    override fun getGroupView(
        groupPosition: Int,
        b: Boolean,
        convertView: View?,
        viewGroup: ViewGroup
    ): View {
        var myConvertView = convertView
        val viewHolderParent: ViewHolderParent

        if (myConvertView == null) {
            myConvertView = inflater.inflate(R.layout.expandable_list_group, null)

            viewHolderParent = ViewHolderParent()
            viewHolderParent.tvMainCategoryName = myConvertView!!.findViewById(R.id.tvMainCategoryName)
            viewHolderParent.tvCountCheck = myConvertView.findViewById(R.id.tvCountCheck)
            viewHolderParent.cbMainCategory = myConvertView.findViewById(R.id.cbMainCategory)
            myConvertView.tag = viewHolderParent
        } else
            viewHolderParent = myConvertView.tag as ViewHolderParent

        // check the checkbox
        viewHolderParent.cbMainCategory!!.isChecked = customOptionsList[groupPosition].isChecked
        notifyDataSetChanged()

        viewHolderParent.cbMainCategory!!.setOnClickListener {

            // se la categoria è checked, metti tutti i figli a true e viceversa
            customOptionsList[groupPosition].isChecked = viewHolderParent.cbMainCategory!!.isChecked
            for(subCat in getChildren(groupPosition)) {
                val alreadyChecked: Boolean = subCat.isChecked
                subCat.isChecked = viewHolderParent.cbMainCategory!!.isChecked
                if(subCat.isChecked && !alreadyChecked) {
                    totalPrice += subCat.subCategoryBasePrice * multiplier
                } else if(!subCat.isChecked)  {
                    totalPrice -= subCat.subCategoryBasePrice * multiplier
                }
            }
            countArray[groupPosition] = if(customOptionsList[groupPosition].isChecked)
                customOptionsList[groupPosition].subCategoryList.size
            else 0

            listener.onEvent()
            notifyDataSetChanged()
        }
        viewHolderParent.tvMainCategoryName!!.text = customOptionsList[groupPosition].categoryName
        viewHolderParent.tvCountCheck!!.text = if(countArray[groupPosition] > 0)
            countArray[groupPosition].toString()
        else ""

        return myConvertView
    }

    @SuppressLint("InflateParams")
    override fun getChildView(
        groupPosition: Int,
        childPosition: Int,
        b: Boolean,
        convertView: View?,
        viewGroup: ViewGroup
    ): View {
        var myConvertView: View? = convertView
        val viewHolderChild: ViewHolderChild
        childItem = getChild(groupPosition, childPosition)

        if (myConvertView == null) {
            myConvertView = inflater.inflate(R.layout.expandable_list_item, null)

            viewHolderChild = ViewHolderChild()
            viewHolderChild.tvSubCategoryName = myConvertView!!.findViewById(R.id.tvSubCategoryName)
            viewHolderChild.tvSubCategoryDesc = myConvertView.findViewById(R.id.tvSubCategoryDesc)
            viewHolderChild.tvSubCategoryPrice = myConvertView.findViewById(R.id.tvSubCategoryPrice)
            viewHolderChild.cbSubCategory = myConvertView.findViewById(R.id.cbSubCategory)
            viewHolderChild.viewDivider = myConvertView.findViewById(R.id.viewDivider)
            myConvertView.tag = viewHolderChild
        } else {
            viewHolderChild = myConvertView.tag as ViewHolderChild
        }

        viewHolderChild.cbSubCategory!!.isChecked = childItem.isChecked
        notifyDataSetChanged()

        viewHolderChild.tvSubCategoryName!!.text = childItem.subCategoryName
        viewHolderChild.tvSubCategoryDesc!!.text = childItem.subCategoryDesc
        viewHolderChild.tvSubCategoryPrice!!.text = (childItem.subCategoryBasePrice * this.multiplier).toInt().toString()

        viewHolderChild.cbSubCategory!!.setOnClickListener {
            val currentChild = getChild(groupPosition, childPosition)
            //Log.i(TAG, "groupPosition: $groupPosition - childPosition: $childPosition")
            count = 0
            currentChild.isChecked = viewHolderChild.cbSubCategory!!.isChecked

            if(currentChild.isChecked) {
                totalPrice += currentChild.subCategoryBasePrice * multiplier
            } else {
                totalPrice -= currentChild.subCategoryBasePrice * multiplier
            }
            //Log.i(TAG, "getChild(groupPosition, childPosition).isChecked: ${getChild(groupPosition, childPosition).isChecked}")

            for (child in getChildren(groupPosition)) {
                if(child.isChecked)
                    count++
            }
            customOptionsList[groupPosition].isChecked = (count == getChildrenCount(groupPosition))
            countArray[groupPosition] = count

            listener.onEvent()
            notifyDataSetChanged()
        }
        return myConvertView
    }

    override fun isChildSelectable(i: Int, i1: Int): Boolean {
        return false
    }

    private inner class ViewHolderParent {
        internal var tvMainCategoryName: TextView? = null
        internal var tvCountCheck: TextView? = null
        internal var cbMainCategory: CheckBox? = null
    }

    private inner class ViewHolderChild {
        internal var tvSubCategoryName: TextView? = null
        internal var cbSubCategory: CheckBox? = null
        internal var tvSubCategoryDesc: TextView? = null
        internal var tvSubCategoryPrice: TextView? = null
        internal var viewDivider: View? = null
    }

    interface EventListener {
        fun onEvent()
    }
}
