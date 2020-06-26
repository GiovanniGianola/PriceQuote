package com.example.pricequote.ui.invoice.fragment

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.pricequote.utilities.TAG
import com.example.pricequote.data.InvoiceEntity
import com.example.pricequote.databinding.FragmentInfoBinding
import com.example.pricequote.ui.invoice.InvoiceActivity
import com.example.pricequote.ui.invoice.InvoiceViewModel
import com.example.pricequote.ui.invoice.SectionsPagerAdapter


class InfoFragment : Fragment(), SectionsPagerAdapter.EventListener {

    private lateinit var binding: FragmentInfoBinding
    private lateinit var viewModel: InvoiceViewModel
    private lateinit var currentInvoice: InvoiceEntity

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewModel = ViewModelProvider(requireActivity()).get(InvoiceViewModel::class.java)

        binding = FragmentInfoBinding.inflate(inflater, container, false).also{
            // Clear the EditText view
            it.editTextInvoiceTitle.text.clear()
            it.editTextInvoiceNote.text.clear()

            // Set up view model observer
            viewModel.currentInvoice.observe(viewLifecycleOwner, Observer { invoice ->
                currentInvoice = invoice

                it.editTextInvoiceTitle.setText(currentInvoice.title)
                it.editTextInvoiceNote.setText(currentInvoice.note)

                it.editTextInvoiceTitle.addTextChangedListener(object : TextWatcher {
                    override fun afterTextChanged(p0: Editable?) {
                        currentInvoice.title = binding.editTextInvoiceTitle.text.toString()
                    }
                    override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
                    override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
                })

                it.editTextInvoiceNote.addTextChangedListener(object : TextWatcher {
                    override fun afterTextChanged(p0: Editable?) {
                        currentInvoice.note = binding.editTextInvoiceNote.text.toString()
                    }
                    override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
                    override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
                })

                (activity as InvoiceActivity).updatePrice()
            })
        }

        // Inflate the layout for this fragment
        return binding.root
    }

    override fun saveLocalFragmentUpdates() {
        Log.i(TAG,"saveLocalFragmentUpdates: InfoFragment")
        currentInvoice.title = binding.editTextInvoiceTitle.text.toString()
        currentInvoice.note = binding.editTextInvoiceNote.text.toString()
    }

    companion object {
        @JvmStatic
        fun newInstance() = InfoFragment()
    }
}