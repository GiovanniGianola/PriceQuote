package com.example.pricequote.data

import android.app.Application
import androidx.lifecycle.MutableLiveData
import com.example.homequote.data.InvoiceDetail
import com.example.pricequote.utilities.FileHelper
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types

class InvoiceRepository(val app: Application) {
    val invoiceDetailData = MutableLiveData<List<InvoiceDetail.Space>>()
    val invoiceCustomOptionData = MutableLiveData<List<InvoiceDetail.CustomOptionCategory>>()

    private val listTypeDetail = Types.newParameterizedType(
        List::class.java, InvoiceDetail.Space::class.java
    )

    private val listTypeCustomOption = Types.newParameterizedType(
        List::class.java, InvoiceDetail.CustomOptionCategory::class.java
    )

    init {
        getInvoiceDetailData()
        getInvoiceCustomOptionData()
    }

    private fun getInvoiceDetailData() {
        val text = FileHelper.getTextFromAssets(app, "invoice_config.json")
        val moshi = Moshi.Builder().build()
        val adapter: JsonAdapter<List<InvoiceDetail.Space>> = moshi.adapter(listTypeDetail)

        invoiceDetailData.value = adapter.fromJson(text) ?: emptyList()
    }

    private fun getInvoiceCustomOptionData() {
        val text = FileHelper.getTextFromAssets(app, "customize_config.json")
        val moshi = Moshi.Builder().build()
        val adapter: JsonAdapter<List<InvoiceDetail.CustomOptionCategory>> = moshi.adapter(listTypeCustomOption)

        invoiceCustomOptionData.value = adapter.fromJson(text) ?: emptyList()
    }
}