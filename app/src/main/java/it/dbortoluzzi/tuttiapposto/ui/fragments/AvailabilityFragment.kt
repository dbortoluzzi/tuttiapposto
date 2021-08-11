package it.dbortoluzzi.tuttiapposto.ui.fragments


import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import it.dbortoluzzi.data.R
import it.dbortoluzzi.data.databinding.FragmentAvailabilitiesBinding
import it.dbortoluzzi.tuttiapposto.model.Availability
import it.dbortoluzzi.tuttiapposto.ui.BaseMvpFragment
import it.dbortoluzzi.tuttiapposto.ui.activities.AvailabilitiesAdapter
import it.dbortoluzzi.tuttiapposto.ui.presenters.AvailabilityPresenter
import it.dbortoluzzi.tuttiapposto.ui.presenters.MainPresenter
import it.dbortoluzzi.tuttiapposto.ui.util.Constants
import javax.inject.Inject

/**
 * AvailabilityFragment
 */

@AndroidEntryPoint
class AvailabilityFragment: BaseMvpFragment<AvailabilityFragment, AvailabilityPresenter>(), AvailabilityPresenter.View {

    lateinit var availabilitiesAdapter: AvailabilitiesAdapter

    @Inject
    override lateinit var presenter: AvailabilityPresenter

    private lateinit var binding: FragmentAvailabilitiesBinding

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        super.onCreateView(inflater, container, savedInstanceState)

        binding = FragmentAvailabilitiesBinding.inflate(layoutInflater)

        availabilitiesAdapter = AvailabilitiesAdapter(binding.emptyView).apply {
            onItemLongPress = { avail, view ->
                //creating a popup menu
                val popup = PopupMenu(activity, view)
                //inflating menu from xml resource
                popup.inflate(R.menu.availability_menu)
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
                    popup.gravity = Gravity.END
                }
                popup.menu.findItem(R.id.report_availability).isEnabled = !avail.tableAvailabilityResponseDto.alreadyReportedByUser
                //adding click listener
                popup.setOnMenuItemClickListener { item ->
                    when (item.itemId) {
                        R.id.book_table -> {
                            val navController = findNavController()
                            val mapBookingData: Map<String, Any> = presenter.newBookingBtnClicked(avail)
                            navController.apply {
                                val b = bundleOf(Constants.BUNDLE_DATA to mapBookingData)
                                navigate(R.id.action_home_to_book, b)
                            }
                        }
                        R.id.report_availability -> {
                            presenter.reportAvailabilityBtnClicked(avail)
                        }
                    }
                    false
                }
                //displaying the popup
                popup.show()
            }
        }

        binding.recycler.adapter = availabilitiesAdapter

        binding.newBookingBtn.setOnClickListener { findNavController().navigate(R.id.action_home_to_book_filter)}

        binding.filterAvailabilityBtn.setOnClickListener { findNavController().navigate(R.id.action_home_to_filter)}

        return binding.root
    }

    override fun renderAvailableTables(availabilities: List<Availability>) {
        availabilitiesAdapter.items = availabilities
    }

    override fun availabilityReportDoneWithSuccess() {
        Toast.makeText(context?.applicationContext,
                R.string.report_availability_success_message, Toast.LENGTH_LONG).show()
    }

    override fun availabilityReportNotDoneWithError() {
        Toast.makeText(context?.applicationContext,
                R.string.report_availability_error_message, Toast.LENGTH_LONG).show()
    }

    override fun showProgressBar() {
        binding.recycler.visibility = View.GONE
        binding.progressBar.visibility = View.VISIBLE
    }

    override fun hideProgressBar() {
        binding.recycler.visibility = View.VISIBLE
        binding.progressBar.visibility = View.GONE

        if (activity is MainPresenter.View) {
            (activity as MainPresenter.View).closeNavigationDrawer()
        }
    }

    // TODO: refactor
    override fun showNetworkError() {
        Toast.makeText(context() , getString(R.string.network_not_connected), Toast.LENGTH_LONG).show()
    }

}
