package com.example.pricequote.utilities

import android.view.View
import android.view.ViewGroup
import android.widget.ExpandableListView

object ExpandableListViewHelper {

    fun getExpandableListViewSize(myListView: ExpandableListView, group: Int) {
        val myListAdapter = myListView.expandableListAdapter
            ?: //do nothing return null
            return
        //set listAdapter in loop for getting final size
        var totalHeight = 0
        val desiredWidth = View.MeasureSpec.makeMeasureSpec(myListView.width, View.MeasureSpec.EXACTLY)
        for (size in 0 until myListAdapter.groupCount) {
            val listItemGroup: View = myListAdapter.getGroupView(size, false, null, myListView)
            listItemGroup.measure(0, 0)
            totalHeight += listItemGroup.measuredHeight

            if (myListView.isGroupExpanded(size) && size != group || !myListView.isGroupExpanded(size) && size == group) {
                for (j in 0 until myListAdapter.getChildrenCount(size)) {
                    val listItem = myListAdapter.getChildView(size, j, false, null, myListView)
                    listItem.measure(desiredWidth, View.MeasureSpec.UNSPECIFIED)
                    totalHeight += listItem.measuredHeight
                }
            }
        }
        //setting listview item in adapter
        val params: ViewGroup.LayoutParams = myListView.layoutParams
        params.height = totalHeight + myListView.dividerHeight * (myListAdapter.groupCount - 1)
        myListView.layoutParams = params
    }
}