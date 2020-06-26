package com.example.pricequote.data

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.homequote.data.InvoiceDetail.*
import com.example.pricequote.utilities.NEW_INVOICE_ID
import kotlinx.android.parcel.Parcelize
import java.util.*

/**
 * Data object and Room database structure
 * @Parcelize is here to support state management in ListFragment
 */
@Parcelize
@Entity(tableName = "invoice")
class InvoiceEntity(
    @PrimaryKey(autoGenerate = true)
    var id: Int,
    var date: Date,
    var title: String? = "",

    var spaceName: String? = "",
    var categoryName: String? = "",
    var size: Size? = null,

    var customOptionsList: List<CustomOptionCategory>? = null,

    var note: String? = "",
    var finalPrice: Double? = 0.0
) : Parcelable {
    constructor() : this(NEW_INVOICE_ID, Date(), "", "", "", null, null,"", 0.0)
    constructor(date: Date, title: String, spaceName: String, categoryName: String, size: Size, customOptionsList: List<CustomOptionCategory>, note: String, finalPrice: Double) :
            this(NEW_INVOICE_ID, date, title, spaceName, categoryName, size, customOptionsList, note, finalPrice)

    override fun toString(): String {
        return "InvoiceEntity(id=$id, date=$date, title=$title, " +
                "spaceName=$spaceName, categoryName=$categoryName, size=$size, " +
                "customOption=$customOptionsList, " +
                "note=$note, finalPrice=$finalPrice)"
    }
}