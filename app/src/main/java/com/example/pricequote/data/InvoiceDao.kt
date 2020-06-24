package com.example.pricequote.data

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface InvoiceDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertInvoice(invoice: InvoiceEntity?)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertAll(invoices: List<InvoiceEntity?>?)

    @Delete
    fun deleteInvoice(invoice: InvoiceEntity)

    @Delete
    fun deleteInvoices(invoices: ArrayList<InvoiceEntity>)

    @Query("SELECT * FROM invoice WHERE id = :id")
    fun getInvoiceById(id: Int): InvoiceEntity?

    @Query("SELECT * FROM invoice ORDER BY date ASC")
    fun getAll(): LiveData<List<InvoiceEntity>>

    @Query("DELETE FROM invoice")
    fun deleteAll(): Int

    @Query("SELECT COUNT(*) FROM invoice")
    fun getCount(): Int
}