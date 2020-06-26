package com.example.pricequote.utilities

import android.content.Context
import android.content.SharedPreferences

const val ITEM_SORT_KEY = "item_sort_key"

class PrefsHelper {
    companion object{
        private fun preferences(context: Context): SharedPreferences =
            context.getSharedPreferences("default", 0)

        fun setSortType(context: Context, type: Int){
            preferences(context).edit()
                .putInt(ITEM_SORT_KEY, type)
                .apply()
        }

        fun getSortType(context: Context): Int =
            preferences(context).getInt(ITEM_SORT_KEY,
                DATE_ASC
            )
    }
}