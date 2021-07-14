package it.dbortoluzzi.tuttiapposto.ui.fragments


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import it.dbortoluzzi.data.R
import it.dbortoluzzi.data.databinding.FragmentAvailabilitiesBinding
import it.dbortoluzzi.tuttiapposto.model.Availability
import it.dbortoluzzi.tuttiapposto.ui.BaseMvpFragment
import it.dbortoluzzi.tuttiapposto.ui.activities.AvailabilitiesAdapter
import it.dbortoluzzi.tuttiapposto.ui.presenters.AvailabilityPresenter
import it.dbortoluzzi.tuttiapposto.ui.presenters.MainPresenter
import javax.inject.Inject

/**
 * AvailabilityFragment
 */

@AndroidEntryPoint
class AvailabilityFragment: BaseMvpFragment<AvailabilityFragment, AvailabilityPresenter>(), AvailabilityPresenter.View {
    @Inject
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

        binding.recycler.adapter = availabilitiesAdapter

        binding.newBookingBtn.setOnClickListener { presenter.newBookingClicked()}

        binding.filterAvailabilityBtn.setOnClickListener { findNavController().navigate(R.id.filterAvailabilitiesFragment)}

        return binding.root
    }

    override fun renderAvailableTables(availabilities: List<Availability>) {
        availabilitiesAdapter.items = availabilities
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

    override fun showNetworkError() {
        Toast.makeText(context() , getString(R.string.network_not_connected), Toast.LENGTH_LONG).show()
    }

}
