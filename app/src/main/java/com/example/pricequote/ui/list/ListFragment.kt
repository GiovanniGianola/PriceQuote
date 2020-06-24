package com.example.pricequote.ui.list

import android.os.Bundle
import android.os.Handler
import android.view.*
import androidx.fragment.app.Fragment
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.pricequote.*
import com.example.pricequote.data.InvoiceEntity
import com.example.pricequote.databinding.FragmentListBinding
import com.example.pricequote.utilities.PrefsHelper

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class ListFragment : Fragment(), InvoiceListAdapter.ListItemListener  {

    private lateinit var binding: FragmentListBinding
    private lateinit var adapter: InvoiceListAdapter
    private lateinit var viewModel: ListViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Set up the viewModel
        viewModel = ViewModelProvider(this).get(ListViewModel::class.java)
    }

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        // Suppress the up button in case it was displayed by the editor fragment
        (activity as AppCompatActivity).supportActionBar?.let {
            it.setHomeButtonEnabled(false)
            it.setDisplayHomeAsUpEnabled(false)
            it.setDisplayShowHomeEnabled(false)
        }

        // Display the app name as the activity title
        requireActivity().setTitle(R.string.app_name)

        // Turn options menu on
        setHasOptionsMenu(true)

        // Configure views
        binding = FragmentListBinding.inflate(inflater, container, false).also {

            // Configure the floating action button
            it.fab.setOnClickListener {
                createInvoice()
            }

            // Set up RecyclerView divider between data items
            with(it.recyclerView) {
                this.setHasFixedSize(true)
                val divider = DividerItemDecoration(
                    requireContext(), LinearLayoutManager(requireContext()).orientation
                )
                this.addItemDecoration(divider)
            }
        } // end also{}

        // Observe and display invoices list from the view model
        viewModel.invoicesList?.observe(viewLifecycleOwner, Observer { invoicesList ->
            adapter = InvoiceListAdapter(invoicesList)
            binding.recyclerView.adapter = adapter
            adapter.setItemListener(this@ListFragment)
            // restore selected invoices state if necessary
            val selectedInvoices = savedInstanceState?.getParcelableArrayList<InvoiceEntity>(INVOICES_LIST_KEY)
            adapter.selectedInvoices.addAll(selectedInvoices ?: emptyList())
            val sort = PrefsHelper.getSortType(requireContext())
            adapter.orderItems(sort)

        }) // end observe()

        // Inflate the layout for this fragment
        return binding.root
    }

    /*  AUXILIARY FUNCTIONS */
    /**
     * Create a new invoice in editor
     */
    private fun createInvoice() {
        val action = ListFragmentDirections.actionListFragmentToInvoiceActivity(NEW_INVOICE_ID)
        findNavController().navigate(action)
    }

    /**
     * When item selections change, rebuild the options menu
     * This happens when the user touches an action button in a recyclerView item
     */
    override fun onItemSelectionChanged() {
        requireActivity().invalidateOptionsMenu()
    }

    /**
     * When the user touches the recyclerView item anywhere but the button, send the selected
     * invoice's id to the EditorFragment
     */
    override fun onItemClick(invoiceId: Int) {
        val action = ListFragmentDirections.actionListFragmentToInvoiceActivity(invoiceId)
        findNavController().navigate(action)
    }

    /**
     * Display the main fragment's options menu. There are 2 menus available.
     * If no invoices have been selected, a full options menu appears.
     * If any invoices have been selected, the options menu is replaced with a delete icon.
     */
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        val menuId =
            if (this::adapter.isInitialized && adapter.selectedInvoices.size > 0) {
                R.menu.menu_invoices_list_selected
            } else {
                R.menu.menu_invoices_list
            }
        inflater.inflate(menuId, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    /**
     * Handle options menu item selection
     */
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_load_sample -> {
                viewModel.insertSampleInvoices()
                true
            }
            R.id.action_delete_all -> {
                viewModel.deleteAllInvoices()
                clearSelectedInvoices()
                true
            }
            R.id.action_delete -> {
                viewModel.deleteSelectedInvoices(adapter.selectedInvoices)
                clearSelectedInvoices()
                true
            }
            R.id.sort_alph_asc -> {
                PrefsHelper.setSortType(requireContext(), ALPH_ASC)
                adapter.orderItems(ALPH_ASC)
                true
            }
            R.id.sort_alph_desc -> {
                PrefsHelper.setSortType(requireContext(), ALPH_DESC)
                adapter.orderItems(ALPH_DESC)
                true
            }
            R.id.sort_date_asc -> {
                PrefsHelper.setSortType(requireContext(), DATE_ASC)
                adapter.orderItems(DATE_ASC)
                true
            }
            R.id.sort_date_desc -> {
                PrefsHelper.setSortType(requireContext(), DATE_DESC)
                adapter.orderItems(DATE_DESC)
                true
            }
            else -> false
        }
    }

    /**
     * Wait a moment for async tasks to complete, then clear the selections state
     * and reset the options menu
     */
    private fun clearSelectedInvoices() {
        Handler().postDelayed({
            adapter.selectedInvoices.clear()
            requireActivity().invalidateOptionsMenu()
        }, 100)
    }

    /**
     * Upon initiating a configuration change, save the selected invoices state as a parcelable
     * ArrayList. The InvoiceEntity class has @Parcelize to make this work.
     */
    override fun onSaveInstanceState(outState: Bundle) {
        // Save selected invoices state
        outState.putParcelableArrayList(INVOICES_LIST_KEY, adapter.selectedInvoices)
        super.onSaveInstanceState(outState)
    }
}