package com.example.pricequote.data

import android.util.Log
import com.example.pricequote.utilities.INVOICE_LIST_FIELD
import com.example.pricequote.utilities.TAG
import com.example.pricequote.utilities.USERS
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.*


class InvoiceFirestoreRepository {

    private var firestoreDatabase = FirebaseFirestore.getInstance()
    private var user = FirebaseAuth.getInstance().currentUser
    private var count: Int? = 0
    private var newId: String = "0"

    init {
        getInvoicesFirestore().addSnapshotListener(EventListener<QuerySnapshot> { value, e ->
            val querySnapshot: QuerySnapshot? = value
            count = querySnapshot?.size()
            val maxObject: String = querySnapshot?.maxBy{ it.id }?.id ?: "0"
            newId = (maxObject.toInt() + 1).toString()
        })
    }


    // save invoice to firebase
    fun updateInvoiceFirestore(invoice: InvoiceEntity): Task<Void> {
        if(invoice.id == 0) invoice.id = newId.toInt()
        return firestoreDatabase.collection(USERS).document(user!!.uid)
            .collection(INVOICE_LIST_FIELD).document(invoice.id.toString()).set(invoice)
    }

    // get saved invoices from firebase
    fun getInvoicesFirestore(): CollectionReference {
        return firestoreDatabase.collection("$USERS/${user!!.uid}/$INVOICE_LIST_FIELD")
    }

    // get saved invoice by id from firebase
    fun getInvoiceByIdFirestore(invoiceId: Int): DocumentReference {
        return firestoreDatabase.collection("$USERS/${user!!.uid}/$INVOICE_LIST_FIELD")
            .document(invoiceId.toString())
    }


    fun deleteInvoicesFirestore(invoices: ArrayList<InvoiceEntity>): Int {
        for(inv in invoices){
            val documentReference = firestoreDatabase
                .collection("$USERS/${user!!.uid}/$INVOICE_LIST_FIELD")
                .document(inv.id.toString()).delete()
        }
        return 0
    }

    fun deleteAllFirestore(): Task<Void> {
        val documentReference = firestoreDatabase
            .collection("$USERS/${user!!.uid}/$INVOICE_LIST_FIELD").document()


        return documentReference.delete()
    }
}