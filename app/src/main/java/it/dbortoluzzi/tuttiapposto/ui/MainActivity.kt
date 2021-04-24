package it.dbortoluzzi.tuttiapposto.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import dagger.hilt.android.AndroidEntryPoint
import it.dbortoluzzi.data.DeviceLocationSource
import it.dbortoluzzi.data.LocationPersistenceSource
import it.dbortoluzzi.tuttiapposto.framework.FakeLocationSource
import it.dbortoluzzi.tuttiapposto.framework.InMemoryLocationPersistenceSource
import it.dbortoluzzi.data.LocationsRepository
import it.dbortoluzzi.usecases.GetLocations
import it.dbortoluzzi.usecases.RequestNewLocation
import it.dbortoluzzi.data.databinding.ActivityMainBinding
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity(), MainPresenter.View {
    @Inject
    lateinit var locationsAdapter: LocationsAdapter

    @Inject
    lateinit var persistence: LocationPersistenceSource

    @Inject
    lateinit var deviceLocation: DeviceLocationSource

    @Inject
    lateinit var locationsRepository: LocationsRepository

    @Inject
    lateinit var presenter: MainPresenter

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.recycler.adapter = locationsAdapter

        binding.newLocationBtn.setOnClickListener { presenter.newLocationClicked() }

        presenter.onCreate()
    }

    override fun onDestroy() {
        presenter.onDestroy()
        super.onDestroy()
    }

    override fun renderLocations(locations: List<Location>) {
        locationsAdapter.items = locations
    }
}