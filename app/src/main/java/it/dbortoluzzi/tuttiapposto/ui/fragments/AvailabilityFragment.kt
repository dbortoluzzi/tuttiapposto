package it.dbortoluzzi.tuttiapposto.ui.fragments


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import dagger.hilt.android.AndroidEntryPoint
import it.dbortoluzzi.data.databinding.FragmentAvailabiltiesBinding
import it.dbortoluzzi.tuttiapposto.model.Availability
import it.dbortoluzzi.tuttiapposto.ui.BaseMvpFragment
import it.dbortoluzzi.tuttiapposto.ui.activities.AvailabilitiesAdapter
import it.dbortoluzzi.tuttiapposto.ui.presenters.AvailabilityPresenter
import javax.inject.Inject

/**
 * LocationFragment
 */

@AndroidEntryPoint
class AvailabilityFragment: BaseMvpFragment<AvailabilityFragment, AvailabilityPresenter>(), AvailabilityPresenter.View {
    @Inject
    lateinit var availabilitiesAdapter: AvailabilitiesAdapter

    @Inject
    override lateinit var presenter: AvailabilityPresenter

    private lateinit var binding: FragmentAvailabiltiesBinding

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        super.onCreateView(inflater, container, savedInstanceState)

        binding = FragmentAvailabiltiesBinding.inflate(layoutInflater)

        binding.recycler.adapter = availabilitiesAdapter

        binding.newBookingBtn.setOnClickListener { presenter.newBookingClicked()}

        return binding.root
    }

    override fun renderAvailableTables(availabilities: List<Availability>) {
        availabilitiesAdapter.items = availabilities
    }

}
