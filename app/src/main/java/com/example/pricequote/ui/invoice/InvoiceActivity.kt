package com.example.pricequote.ui.invoice

import android.app.AlertDialog
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.navArgs
import androidx.viewpager.widget.ViewPager
import androidx.viewpager.widget.ViewPager.OnPageChangeListener
import com.example.pricequote.utilities.NEW_INVOICE_ID
import com.example.pricequote.R
import com.google.android.material.tabs.TabLayout
import kotlinx.android.synthetic.main.activity_invoice.*

class InvoiceActivity : AppCompatActivity() {

    private var invoiceId: Int = NEW_INVOICE_ID
    private lateinit var viewModel: InvoiceViewModel
    private lateinit var sectionsPagerAdapter: SectionsPagerAdapter

    private var finalPrice: Double? = 0.0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_invoice)
        setSupportActionBar(findViewById(R.id.toolbar))

        // Retrieve invoice id sent from main fragment.
        val args: InvoiceActivityArgs by navArgs()
        invoiceId = args.invoiceId

        viewModel = ViewModelProvider(this).get(InvoiceViewModel::class.java)

        // Set the activity title and either create a new invoice object or
        // ask the viewModel to retrieve the existing invoice
        if (invoiceId == NEW_INVOICE_ID) {
            this.setTitle(R.string.new_invoice)
            viewModel.createInvoice()
        } else {
            this.setTitle(R.string.edit_invoice)
            viewModel.getInvoiceById(invoiceId)
        }


        sectionsPagerAdapter = SectionsPagerAdapter(this, supportFragmentManager)
        val viewPager: ViewPager = findViewById(R.id.view_pager)
        viewPager.adapter = sectionsPagerAdapter
        viewPager.addOnPageChangeListener (object: OnPageChangeListener {
            override fun onPageScrollStateChanged(state: Int) {}
            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {}
            override fun onPageSelected(position: Int) {
                sectionsPagerAdapter.notifyDataSetChanged()
            }
        })

        val tabs: TabLayout = findViewById(R.id.tabs)
        tabs.setupWithViewPager(viewPager)

        // Display Checkbox icon on toolbar
        this.supportActionBar?.let {
            it.setHomeAsUpIndicator(R.drawable.ic_check)
            it.setHomeButtonEnabled(true)
            it.setDisplayShowHomeEnabled(true)
            it.setDisplayHomeAsUpEnabled(true)
        }

    }

    override fun onBackPressed() {
        //super.onBackPressed()
        val builder = AlertDialog.Builder(this)
        builder.setTitle(R.string.exit)
        //builder.setMessage("We have a message")
        builder.setPositiveButton(R.string.save) { _, _ ->
            saveAndReturn()
        }
        builder.setNegativeButton(R.string.discard) { _, _ ->
            // Return to list fragment
            finish()
        }
        builder.setNeutralButton(R.string.cancel) { _, _ -> }
        builder.show()
    }

    /**
     * Handle options menu item selection
     */
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> saveAndReturn()
            else -> false
        }
    }

    private fun saveAndReturn(): Boolean {
        Log.i(com.example.pricequote.utilities.TAG,"UpdateDBData: InvoiceActivity")
        Log.i(com.example.pricequote.utilities.TAG,"New Invoice: ${viewModel.currentInvoice.value}")
        viewModel.currentInvoice.value?.let { viewModel.updateInvoice(it) }

        finish()
        return true
    }

    fun updatePrice() {
        finalPrice = viewModel.currentInvoice.value?.size?.basePrice

        for(co in viewModel.currentInvoice.value?.customOptionsList!!){
            for(subCo in co.subCategoryList){
                if(subCo.isChecked)
                    finalPrice =
                        finalPrice?.plus(subCo.subCategoryBasePrice * viewModel.currentInvoice.value?.size?.multiplier!!)
            }
        }
        viewModel.currentInvoice.value?.finalPrice = finalPrice
        //finalPrice = finalPrice?.plus(price)
        Log.i(com.example.pricequote.utilities.TAG,"Update Final Price: ${finalPrice?.toInt()}")
        priceInvoice.text = "${finalPrice?.toInt()}"
    }
}