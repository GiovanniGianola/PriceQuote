package com.example.pricequote.data

import androidx.room.TypeConverter
import com.example.homequote.data.InvoiceDetail
import java.util.*

/**
 * Used by the Room components to convert between date and long values
 */
class Converter {

    class DateConverter {
        @TypeConverter
        fun fromTimestamp(value: Long): Date {
            return Date(value)
        }

        @TypeConverter
        fun dateToTimestamp(date: Date): Long {
            return date.time
        }
    }

    class SizeConverter {
        @TypeConverter
        fun stringToSize(value: String?): InvoiceDetail.Size {
            if (value == null || value.isEmpty()) {
                return InvoiceDetail.Size()
            }
            val list: List<String> = value.split(",")
            return InvoiceDetail.Size(
                list[0],
                list[1].toInt(),
                list[2].toIntOrNull(),
                list[3].toDouble(),
                list[4].toDouble()
            )
        }

        @TypeConverter
        fun sizeToString(size: InvoiceDetail.Size?): String {
            var string = ""

            if (size == null) {
                return string
            }
            string += "${size.sizeName},"
            string += "${size.minSqm},"
            string += "${size.maxSqm},"
            string += "${size.multiplier},"
            string += "${size.basePrice}"
            return string
        }
    }

    class CustomOptionConverter {
        @TypeConverter
        fun stringToCustomOption(value: String?): List<InvoiceDetail.CustomOptionCategory> {
            if (value == null || value.isEmpty()) {
                return emptyList()
            }
            val list: List<String> = value.split("&")
            val res: MutableList<InvoiceDetail.CustomOptionCategory> = mutableListOf()


            for(str in list){
                if(str.isEmpty()) break
                //Log.i(TAG,"str: ${str}\n")
                val catOptionString = str.split("#")
                //Log.i(TAG,"catOptionString: ${catOptionString[3].split("*")}\n")

                val customOption: InvoiceDetail.CustomOptionCategory = InvoiceDetail.CustomOptionCategory()
                val subCustomOptionList: MutableList<InvoiceDetail.CustomOptionSubCategory> = mutableListOf()

                for(subCategoryString in catOptionString[2].split("*")) {
                    if(subCategoryString.isEmpty()) break
                    val subCatOptionInfoString = subCategoryString.split("@")
                    val subCustomOption: InvoiceDetail.CustomOptionSubCategory = InvoiceDetail.CustomOptionSubCategory()
                    //Log.i(TAG,"subCategoryString: ${subCategoryString}")

                    //subCustomOption.subCategoryId = subCatOptionInfoString[0].toInt()
                    subCustomOption.subCategoryName = subCatOptionInfoString[0]
                    subCustomOption.subCategoryDesc = subCatOptionInfoString[1]
                    subCustomOption.isChecked = subCatOptionInfoString[2].toBoolean()
                    subCustomOption.subCategoryBasePrice = subCatOptionInfoString[3].toDouble()

                    subCustomOptionList.add(subCustomOption)
                }
                //customOption.categoryId = catOptionString[0].toInt()
                customOption.categoryName = catOptionString[0]
                customOption.isChecked = catOptionString[1].toBoolean()
                customOption.subCategoryList = subCustomOptionList
                res.add(customOption)
            }
            return res
        }

        @TypeConverter
        fun customOptionToString(customOptionList: List<InvoiceDetail.CustomOptionCategory>?): String {
            var string = ""

            if (customOptionList == null) {
                return string
            }

            for(customOption in customOptionList){
                //string += "${customOption.categoryId}#"
                string += "${customOption.categoryName}#"
                string += "${customOption.isChecked}#"

                for(subCustomOption in customOption.subCategoryList){
                    //string += "${subCustomOption.subCategoryId}@"
                    string += "${subCustomOption.subCategoryName}@"
                    string += "${subCustomOption.subCategoryDesc}@"
                    string += "${subCustomOption.isChecked}@"
                    string += "${subCustomOption.subCategoryBasePrice}*"
                }
                string += "&"
            }
            return string
        }
    }
}