package com.example.pricequote.ui.list

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.pricequote.data.AppDatabase
import com.example.pricequote.data.InvoiceEntity
import com.example.pricequote.utilities.SampleDataProvider
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ListViewModel(app: Application) : AndroidViewModel(app){
    // Database reference
    private val database: AppDatabase? = AppDatabase.getInstance(app)

    // LiveData object containing invoices list, initialized upon startup
    val invoicesList = database?.invoiceDao()?.getAll()

    /**
     * Add sample data
     */
    fun insertSampleInvoices() {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                val sampleInvoices = SampleDataProvider.getInvoices()
                database?.invoiceDao()?.insertAll(sampleInvoices)
            }
        }
    }

    /**
     * Delete all invoices
     */
    fun deleteAllInvoices() {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                database?.invoiceDao()?.deleteAll()
            }
        }
    }

    /**
     * Delete selected invoices
     */
    fun deleteSelectedInvoices(invoices: ArrayList<InvoiceEntity>) {
        viewModelScope.launch {
            withContext(Dispatchers.IO){
                database?.invoiceDao()?.deleteInvoices(invoices)
            }
        }
    }
}