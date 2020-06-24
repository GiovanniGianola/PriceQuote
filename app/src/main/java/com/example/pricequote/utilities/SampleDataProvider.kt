package com.example.pricequote.utilities

import com.example.homequote.data.InvoiceDetail
import com.example.pricequote.data.InvoiceEntity
import java.util.*

@Suppress("MayBeConstant")
class SampleDataProvider {
    companion object {

        // Various text values. Simple immutable variables
        private val title1 = "A simple Invoice"
        private val space_name1 = "COMMERCIAL"
        private val category_name1 = "OFFICE"
        private val size1: InvoiceDetail.Size = InvoiceDetail.Size("Medium", 151, 300, 1.0, 600.0)
        private val note1 = "This is a note"
        private val finalPrice1 = 600.0

        private val title2 = "Another Invoice"
        private val space_name2 = "RESIDENTIAL"
        private val category_name2 = "LOFT"
        private val size2: InvoiceDetail.Size = InvoiceDetail.Size("Large", 201, 400, 1.2, 600.0)
        private val note2 = "This is a note"
        private val finalPrice2 = 600.0


        /**
         * Return a list of NoteEntity objects with various text values
         */
        fun getInvoices() = arrayListOf(
            InvoiceEntity(getDate(0), title1, space_name1, category_name1, size1,
                emptyList(), note1, finalPrice1),
            InvoiceEntity(getDate(1), title2, space_name2, category_name2, size2,
                emptyList(), note2, finalPrice2)
        )

        /**
         * Return a date object with number of milliseconds added
         */
        private fun getDate(diff: Long): Date {
            return Date(Date().time + diff)
        }
    }
}