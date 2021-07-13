package it.dbortoluzzi.tuttiapposto.ui.fragments


import android.app.DatePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.DatePicker
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import it.dbortoluzzi.data.R
import it.dbortoluzzi.data.databinding.FragmentFilterAvailabilitiesBinding
import it.dbortoluzzi.domain.Building
import it.dbortoluzzi.domain.Room
import it.dbortoluzzi.tuttiapposto.framework.SpinnerItem
import it.dbortoluzzi.tuttiapposto.model.Interval
import it.dbortoluzzi.tuttiapposto.ui.BaseMvpFragment
import it.dbortoluzzi.tuttiapposto.ui.presenters.FilterAvailabilitiesPresenter
import it.dbortoluzzi.tuttiapposto.ui.presenters.MainPresenter
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

/**
 * FilterAvailabilitiesFragment
 */

@AndroidEntryPoint
class FilterAvailabilitiesFragment : BaseMvpFragment<FilterAvailabilitiesFragment, FilterAvailabilitiesPresenter>(), FilterAvailabilitiesPresenter.View {

    @Inject
    override lateinit var presenter: FilterAvailabilitiesPresenter

    private lateinit var binding: FragmentFilterAvailabilitiesBinding

    private var cal: Calendar = Calendar.getInstance()

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?,
    ): View? {
        super.onCreateView(inflater, container, savedInstanceState)

        binding = FragmentFilterAvailabilitiesBinding.inflate(layoutInflater)

        binding.filterBtn.setOnClickListener { presenter.filterBtnClicked() }

        cal.time = presenter.getStartDateSelected() ?: Calendar.getInstance().time

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
                } else {
                    renderRooms(listOf())
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                // nothing
            }
        }

        val dateSetListener = object : DatePickerDialog.OnDateSetListener {
            override fun onDateSet(view: DatePicker, year: Int, monthOfYear: Int,
                                   dayOfMonth: Int) {
                cal.set(Calendar.YEAR, year)
                cal.set(Calendar.MONTH, monthOfYear)
                cal.set(Calendar.DAY_OF_MONTH, dayOfMonth)
                updateDateInView()
            }
        }

        // when you click on the button, show DatePickerDialog that is set with OnDateSetListener
        binding.startDateView.setOnClickListener(object : View.OnClickListener {
            override fun onClick(view: View) {
                DatePickerDialog(this@FilterAvailabilitiesFragment.context(),
                        dateSetListener,
                        // set DatePickerDialog to point to today's date when it loads up
                        cal.get(Calendar.YEAR),
                        cal.get(Calendar.MONTH),
                        cal.get(Calendar.DAY_OF_MONTH)).show()
            }

        })
        updateDateInView()

        return binding.root
    }

    private fun updateDateInView() {
        val myFormat = "dd/MM/yyyy"
        val sdf = SimpleDateFormat(myFormat, Locale.getDefault())
        binding.startDateView.text = sdf.format(cal.getTime())
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

    override fun getDateSelected(): Date {
        return cal.time
    }

    override fun getIntervalSelected(): String? {
        val spinnerItem = binding.intervalSpinner.selectedItem as SpinnerItem<*>
        return if (spinnerItem.id?.equals("") == false) {
            spinnerItem.id.toString()
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

    override fun renderIntervals(intervals: List<Interval>) {
        var spinnerRooms: List<SpinnerItem<String>> = intervals.map { SpinnerItem(it.name, it.description) }

        // Create an ArrayAdapter using a simple spinner layout and languages array
        val aa = ArrayAdapter(context(), android.R.layout.simple_spinner_item, spinnerRooms)
        // Set layout to use when the list of choices appear
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        // Set Adapter to Spinner
        binding.intervalSpinner.adapter = aa
        binding.intervalSpinner.setSelection(aa.getPosition(SpinnerItem(id = presenter.getInterval()?.name
                ?: Interval.ALL_DAY.name)))
    }

}
