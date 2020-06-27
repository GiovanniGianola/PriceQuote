package com.example.pricequote.ui.invoice

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.pricequote.utilities.NEW_INVOICE_ID
import com.example.pricequote.data.AppDatabase
import com.example.pricequote.data.InvoiceEntity
import com.example.pricequote.data.InvoiceFirestoreRepository
import com.example.pricequote.data.InvoiceRepository
import com.example.pricequote.utilities.LOCAL_STORAGE
import com.example.pricequote.utilities.TAG
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.EventListener
import com.google.firebase.firestore.QuerySnapshot
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class InvoiceViewModel(app: Application) : AndroidViewModel(app) {

    private val dataRepo = InvoiceRepository(app)
    private var firebaseRepository = InvoiceFirestoreRepository()

    val invoiceDetailData = dataRepo.invoiceDetailData
    val invoiceCustomOptionData = dataRepo.invoiceCustomOptionData

    // Local Database reference
    private val roomDatabase: AppDatabase? = AppDatabase.getInstance(app)

    // LiveData object to maintain editor state
    val currentInvoice = MutableLiveData<InvoiceEntity>()

    /**
     * Retrieve invoice from storage or create a new invoice object
     */
    fun getInvoiceById(invoiceId: Int) {
        if(LOCAL_STORAGE) {
            viewModelScope.launch {
                withContext(Dispatchers.IO) {
                    val invoice =
                        if (invoiceId != NEW_INVOICE_ID) {
                            roomDatabase?.invoiceDao()?.getInvoiceById(invoiceId)

                        } else {
                            InvoiceEntity()
                        }

                    currentInvoice.postValue(invoice)
                }
            }
        } else {
            viewModelScope.launch {
                withContext(Dispatchers.IO) {
                    if (invoiceId != NEW_INVOICE_ID) {
                        firebaseRepository.getInvoiceByIdFirestore(invoiceId)
                            .addSnapshotListener(EventListener<DocumentSnapshot> { value, e ->
                                if (e != null) {
                                    Log.w(TAG, "Listen failed.", e)
                                    currentInvoice.postValue(null)
                                    return@EventListener
                                }
                                currentInvoice.postValue(value!!.toObject(InvoiceEntity::class.java))
                            })

                    } else {
                        currentInvoice.postValue(InvoiceEntity())
                    }
                }
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
        if (LOCAL_STORAGE) {
            viewModelScope.launch {
                withContext(Dispatchers.IO) {
                    roomDatabase?.invoiceDao()?.insertInvoice(invoice)
                }
            }
        } else {
            viewModelScope.launch {
                withContext(Dispatchers.IO) {
                    firebaseRepository.updateInvoiceFirestore(invoice).addOnFailureListener {
                        Log.e(TAG, "Failed to save Invoice Firestore!")
                    }
                }
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