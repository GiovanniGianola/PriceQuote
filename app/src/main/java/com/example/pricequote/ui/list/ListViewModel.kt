package com.example.pricequote.ui.list

import android.app.Application
import android.util.Log
import androidx.lifecycle.*
import com.example.pricequote.data.AppDatabase
import com.example.pricequote.data.InvoiceEntity
import com.example.pricequote.data.InvoiceFirestoreRepository
import com.example.pricequote.utilities.LOCAL_STORAGE
import com.example.pricequote.utilities.SampleDataProvider
import com.example.pricequote.utilities.TAG
import com.google.firebase.firestore.EventListener
import com.google.firebase.firestore.QuerySnapshot
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ListViewModel(app: Application) : AndroidViewModel(app){
    // Database reference
    private val database: AppDatabase? = AppDatabase.getInstance(app)
    private var firebaseRepository = InvoiceFirestoreRepository()

    // LiveData object containing invoices list, initialized upon startup
    var invoicesListFire: MutableLiveData<List<InvoiceEntity>>? = MutableLiveData()
    var invoicesList: LiveData<List<InvoiceEntity>>? = MutableLiveData()

    init {
        getAllInvoices()
    }

    fun getAllInvoices(): LiveData<List<InvoiceEntity>>?{
        if(LOCAL_STORAGE) {
            invoicesList = database?.invoiceDao()?.getAll()
            return invoicesList
        } else {
            firebaseRepository.getInvoicesFirestore().addSnapshotListener(EventListener<QuerySnapshot> { value, e ->
                if (e != null) {
                    Log.w(TAG, "Listen failed.", e)
                    invoicesListFire = null
                    return@EventListener
                }

                val invoices: MutableList<InvoiceEntity> = mutableListOf()
                for (doc in value!!) {
                    invoices.add(doc.toObject(InvoiceEntity::class.java))
                }
                invoicesListFire?.value = invoices.toList()
            })
        }
        Log.i(TAG, "invoicesList: $invoicesList")
        return invoicesListFire
    }

    /**
     * Add sample data
     */
    fun insertSampleInvoices() {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                val sampleInvoices = SampleDataProvider.getInvoices()
                if(LOCAL_STORAGE) {
                    database?.invoiceDao()?.insertAll(sampleInvoices)
                } else {
                    for(inv in sampleInvoices) {
                        firebaseRepository.updateInvoiceFirestore(inv).addOnFailureListener {
                            Log.e(TAG, "Failed to save sample invoices!")
                        }
                    }
                }
            }
        }
    }

    /**
     * Delete all invoices
     */
    fun deleteAllInvoices() {
        if(LOCAL_STORAGE) {
            viewModelScope.launch {
                withContext(Dispatchers.IO) {
                    database?.invoiceDao()?.deleteAll()
                }
            }
        } else {
            viewModelScope.launch {
                withContext(Dispatchers.IO) {
                    if(!invoicesListFire?.value.isNullOrEmpty())
                        firebaseRepository.deleteInvoicesFirestore(invoicesListFire?.value as ArrayList<InvoiceEntity>)
                }
            }
        }
    }

    /**
     * Delete selected invoices
     */
    fun deleteSelectedInvoices(invoices: ArrayList<InvoiceEntity>) {
        viewModelScope.launch {
            withContext(Dispatchers.IO){
                if(LOCAL_STORAGE) {
                    database?.invoiceDao()?.deleteInvoices(invoices)
                } else {
                    firebaseRepository.deleteInvoicesFirestore(invoices)
                }
            }
        }
    }
}