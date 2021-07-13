package it.dbortoluzzi.tuttiapposto.ui.fragments


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import it.dbortoluzzi.data.R
import it.dbortoluzzi.data.databinding.FragmentFilterAvailabilitiesBinding
import it.dbortoluzzi.domain.Building
import it.dbortoluzzi.domain.Room
import it.dbortoluzzi.tuttiapposto.framework.SpinnerItem
import it.dbortoluzzi.tuttiapposto.ui.BaseMvpFragment
import it.dbortoluzzi.tuttiapposto.ui.presenters.FilterAvailabilitiesPresenter
import it.dbortoluzzi.tuttiapposto.ui.presenters.MainPresenter
import javax.inject.Inject

/**
 * FilterAvailabilitiesFragment
 */

@AndroidEntryPoint
class FilterAvailabilitiesFragment : BaseMvpFragment<FilterAvailabilitiesFragment, FilterAvailabilitiesPresenter>(), FilterAvailabilitiesPresenter.View {

    @Inject
    override lateinit var presenter: FilterAvailabilitiesPresenter

    private lateinit var binding: FragmentFilterAvailabilitiesBinding

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?,
    ): View? {
        super.onCreateView(inflater, container, savedInstanceState)

        binding = FragmentFilterAvailabilitiesBinding.inflate(layoutInflater)

        binding.filterBtn.setOnClickListener { presenter.filterBtnClicked() }

        binding.buildingsSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                    parent: AdapterView<*>,
                    view: View,
                    position: Int,
                    id: Long,
            ) {
                val buildingSelected = getBuildingSelected()
                if (buildingSelected != null) {
                    presenter.retrieveRooms(buildingSelected.first)
                    // TODO:
                    showMessage("render " + buildingSelected.first + " " + buildingSelected.second)
                } else {
                    renderRooms(listOf())
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                // nothing
            }
        }

        return binding.root
    }

    override fun goToAvailabilitiesPage() {
        findNavController().navigate(R.id.homeFragment)
        if (activity is MainPresenter.View) {
            (activity as MainPresenter.View).closeNavigationDrawer()
        }
    }

    override fun getBuildingSelected(): Pair<String, String>? {
        val spinnerItem = binding.buildingsSpinner.selectedItem as SpinnerItem<*>
        return if (spinnerItem.id?.equals("") == false) {
            Pair(spinnerItem.id.toString(), spinnerItem.text.toString())
        } else {
            null
        }
    }

    override fun getRoomSelected(): Pair<String, String>? {
        val spinnerItem = binding.roomsSpinner.selectedItem as SpinnerItem<*>
        return if (spinnerItem.id?.equals("") == false) {
            Pair(spinnerItem.id.toString(), spinnerItem.text.toString())
        } else {
            null
        }
    }

    override fun renderBuildings(buildings: List<Building>) {
        var spinnerBuildings: List<SpinnerItem<String>> = buildings.map { SpinnerItem(it.uID, it.name) }
        spinnerBuildings += SpinnerItem("", getString(R.string.select_all_values))

        // Create an ArrayAdapter using a simple spinner layout and languages array
        val aa = ArrayAdapter(context(), android.R.layout.simple_spinner_item, spinnerBuildings)
        // Set layout to use when the list of choices appear
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        // Set Adapter to Spinner
        binding.buildingsSpinner.adapter = aa
        binding.buildingsSpinner.setSelection(aa.getPosition(SpinnerItem(id = presenter.getBuildingSelected()?.name
                ?: "")))
    }

    override fun renderRooms(rooms: List<Room>) {
        var spinnerRooms: List<SpinnerItem<String>> = rooms.map { SpinnerItem(it.uID, it.name) }
        spinnerRooms += SpinnerItem("", getString(R.string.select_all_values))

        // Create an ArrayAdapter using a simple spinner layout and languages array
        val aa = ArrayAdapter(context(), android.R.layout.simple_spinner_item, spinnerRooms)
        // Set layout to use when the list of choices appear
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        // Set Adapter to Spinner
        binding.roomsSpinner.adapter = aa
        binding.roomsSpinner.setSelection(aa.getPosition(SpinnerItem(id = presenter.getRoomSelected()?.name
                ?: "")))
    }

}
