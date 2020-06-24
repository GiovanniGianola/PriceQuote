package com.example.pricequote.ui.invoice

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.pricequote.NEW_INVOICE_ID
import com.example.pricequote.data.AppDatabase
import com.example.pricequote.data.InvoiceEntity
import com.example.pricequote.data.InvoiceRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class InvoiceViewModel(app: Application) : AndroidViewModel(app){

    private val dataRepo = InvoiceRepository(app)
    val invoiceDetailData = dataRepo.invoiceDetailData
    val invoiceCustomOptionData = dataRepo.invoiceCustomOptionData

    // Database reference
    private val database: AppDatabase? = AppDatabase.getInstance(app)

    // LiveData object to maintain editor state
    val currentInvoice = MutableLiveData<InvoiceEntity>()

    /**
     * Retrieve invoice from storage or create a new invoice object
     */
    fun getInvoiceById(invoiceId: Int) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                val invoice =
                    if (invoiceId != NEW_INVOICE_ID) {
                        database?.invoiceDao()?.getInvoiceById(invoiceId)
                    } else {
                        InvoiceEntity()
                    }
                currentInvoice.postValue(invoice)
            }
        }
    }

    /**
     * Create or update an invoice
     */
    fun updateInvoice(invoice: InvoiceEntity) {

        // Remove starting and ending white space
        invoice.title = invoice.title?.trim()
        invoice.note = invoice.note?.trim()

        // Writing to the database in the background
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                database?.invoiceDao()?.insertInvoice(invoice)
            }
        }
    }

    /**
     * Create a new invoice object. Called as the editor fragment appears for a new invoice.
     */
    fun createInvoice() {
        currentInvoice.value = InvoiceEntity()
        currentInvoice.value!!.customOptionsList = invoiceCustomOptionData.value
    }
}