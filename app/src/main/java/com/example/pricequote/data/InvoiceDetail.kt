package com.example.homequote.data

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

class InvoiceDetail {
    @Parcelize
    data class Space(
        val spaceName: String? = "",
        val categoryList: List<Category>? = null
    ) : Parcelable

    @Parcelize
    data class Category(
        val categoryName: String? = "",
        val sizeList: List<Size>? = null
    ) : Parcelable

    @Parcelize
    data class Size(
        val sizeName: String? = "",
        val minSqm: Int? = 0,
        val maxSqm: Int? = 0,
        val multiplier: Double = 1.0,
        val basePrice: Double = 0.0
    ) : Parcelable{
        override fun toString(): String {
            return "Size(sizeName=$sizeName, minSqm=$minSqm, maxSqm=$maxSqm, multiplier=$multiplier, basePrice=$basePrice)"
        }
    }

    @Parcelize
    data class CustomOptionCategory(
        //@Ignore
        //var categoryId: Int = 0,
        var categoryName: String = "",
        var isChecked: Boolean = false,
        var subCategoryList: List<CustomOptionSubCategory> = emptyList()
    ) : Parcelable

    @Parcelize
    data class CustomOptionSubCategory(
        //@Ignore
        //var subCategoryId: Int = 0,
        var subCategoryName: String = "",
        var subCategoryDesc: String = "",
        var subCategoryBasePrice: Double = 0.0,
        var isChecked: Boolean = false
    ) : Parcelable
}