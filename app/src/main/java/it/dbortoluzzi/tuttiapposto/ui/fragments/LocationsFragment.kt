package it.dbortoluzzi.tuttiapposto.ui.fragments


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import dagger.hilt.android.AndroidEntryPoint
import it.dbortoluzzi.data.DeviceLocationSource
import it.dbortoluzzi.data.LocationPersistenceSource
import it.dbortoluzzi.data.LocationsRepository
import it.dbortoluzzi.data.databinding.FragmentLocationsBinding
import it.dbortoluzzi.tuttiapposto.ui.LocationsAdapter
import it.dbortoluzzi.tuttiapposto.ui.LocationPresenter
import it.dbortoluzzi.tuttiapposto.ui.data.Location
import javax.inject.Inject

/**
 * LocationFragment
 */

@AndroidEntryPoint
class LocationsFragment: Fragment(), LocationPresenter.View {
    @Inject
    lateinit var locationsAdapter: LocationsAdapter

    @Inject
    lateinit var persistence: LocationPersistenceSource

    @Inject
    lateinit var deviceLocation: DeviceLocationSource

    @Inject
    lateinit var locationsRepository: LocationsRepository

    @Inject
    lateinit var presenter: LocationPresenter

    private lateinit var binding: FragmentLocationsBinding

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {

        binding = FragmentLocationsBinding.inflate(layoutInflater)

        binding.recycler.adapter = locationsAdapter

        binding.newLocationBtn.setOnClickListener { presenter.newLocationClicked() }

        presenter.onCreate()

        return binding.root

    }

    override fun onDestroy() {
        presenter.onDestroy()
        super.onDestroy()
    }

    override fun renderLocations(locations: List<Location>) {
        locationsAdapter.items = locations
    }

}
