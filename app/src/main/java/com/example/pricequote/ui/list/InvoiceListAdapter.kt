package com.example.pricequote.ui.list

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.pricequote.*
import com.example.pricequote.data.InvoiceEntity
import com.example.pricequote.databinding.RecyclerviewItemBinding
import com.example.pricequote.utilities.PrefsHelper
import java.text.SimpleDateFormat
import java.util.*

class InvoiceListAdapter(private var invoicesList: List<InvoiceEntity>) :
    RecyclerView.Adapter<InvoiceListAdapter.ViewHolder>() {

    private lateinit var listener: ListItemListener
    val selectedInvoices = arrayListOf<InvoiceEntity>()

    fun orderItems(by: Int){
        when(by){
            ALPH_ASC -> invoicesList = invoicesList.sortedBy { inv -> inv.title }
            ALPH_DESC -> invoicesList = invoicesList.sortedByDescending { inv -> inv.title }
            DATE_ASC -> invoicesList = invoicesList.sortedBy { inv -> inv.date }
            DATE_DESC -> invoicesList = invoicesList.sortedByDescending { inv -> inv.date }
        }
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(
            R.layout.recyclerview_item,
            parent, false
        )
        return ViewHolder(view)
    }

    /**
     * Tell the recyclerView how many items there are
     */
    override fun getItemCount(): Int {
        return invoicesList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val formatter = SimpleDateFormat("dd-MM-yy", Locale.ITALY)

        // Get the current invoice
        val invoice = invoicesList[position]

        // Manage the visuals and events
        with(holder.binding) {

            // Display the current invoice's text
            invoiceTitle.text = invoice.title
            invoicePrice.text = invoice.finalPrice?.toInt().toString()
            invoiceDate.text = formatter.format(invoice.date).toString()

            // When user clicks the container other than the button, notify the fragment
            root.setOnClickListener {
                listener.onItemClick(invoice.id)
            }

            // Display the correct button icon depending on whether the invoice has been selected
            fab.setImageResource(
                if (selectedInvoices.contains(invoice)) {
                    R.drawable.ic_check
                } else {
                    R.drawable.ic_note
                }
            )

            // When user clicks the button, toggle the selected state and notify the fragment
            fab.setOnClickListener {
                if (selectedInvoices.contains(invoice)) {
                    selectedInvoices.remove(invoice)
                    fab.setImageResource(R.drawable.ic_note)
                } else {
                    selectedInvoices.add(invoice)
                    fab.setImageResource(R.drawable.ic_check)
                }
                // Notify the fragment of the change
                listener.onItemSelectionChanged()
            }

        }
    }

    /**
     * Return the view holder for a particular row
     */
    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val binding = RecyclerviewItemBinding.bind(itemView)
    }

    /**
     * Save reference to containing fragment
     */
    fun setItemListener(listener: ListItemListener) {
        this.listener = listener
    }

    /**
     * Functions implemented in containing fragment
     */
    interface ListItemListener {
        fun onItemClick(invoiceId: Int)
        fun onItemSelectionChanged()
    }
}