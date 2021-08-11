package it.dbortoluzzi.tuttiapposto.ui.fragments


import android.app.DatePickerDialog
import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.DatePicker
import android.widget.TextView
import androidx.core.os.bundleOf
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import it.dbortoluzzi.data.R
import it.dbortoluzzi.data.databinding.FragmentFilterAvailabilitiesBinding
import it.dbortoluzzi.domain.Building
import it.dbortoluzzi.domain.Room
import it.dbortoluzzi.domain.Table
import it.dbortoluzzi.tuttiapposto.framework.SpinnerItem
import it.dbortoluzzi.tuttiapposto.model.Interval
import it.dbortoluzzi.tuttiapposto.ui.BaseMvpFragment
import it.dbortoluzzi.tuttiapposto.ui.presenters.FilterAvailabilitiesPresenter
import it.dbortoluzzi.tuttiapposto.ui.presenters.MainPresenter
import it.dbortoluzzi.tuttiapposto.ui.util.Constants.BOOKING
import it.dbortoluzzi.tuttiapposto.ui.util.Constants.BUNDLE_DATA
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

/**
 * FilterAvailabilitiesFragment
 */

@AndroidEntryPoint
// TODO: rename
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

        binding.filterBtn.setOnClickListener {
            presenter.filterBtnClicked()
        }

        binding.bookBtn.setOnClickListener {
            if (checkFiltersBeforeBook()) return@setOnClickListener

            val navController = findNavController()
            val mapBookingData: Map<String, Any> = presenter.prepareFiltersData()
            navController.apply {
                val b = bundleOf(BUNDLE_DATA to mapBookingData)
                navigate(R.id.action_filter_to_book, b)
            }
        }

        binding.editBtn.setOnClickListener {
            if (checkFiltersBeforeBook()) return@setOnClickListener
            val bookingToEdit = (arguments?.get(BUNDLE_DATA) as Map<String,Any>)[BOOKING] ?: return@setOnClickListener

            val navController = findNavController()
            val mapBookingData: Map<String, Any> = presenter.prepareFiltersData() + Pair(BOOKING, bookingToEdit)
            navController.apply {
                val b = bundleOf(BUNDLE_DATA to mapBookingData)
                navigate(R.id.action_filter_to_book, b)
            }
        }

        cal.time = presenter.getStartDateSelected() ?: Calendar.getInstance().time

        initLayout()

        binding.buildingsSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                    parent: AdapterView<*>,
                    view: View?,
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

        binding.roomsSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                    parent: AdapterView<*>,
                    view: View?,
                    position: Int,
                    id: Long,
            ) {
                val buildingSelected = getBuildingSelected()
                val roomSelected = getRoomSelected()
                if (buildingSelected != null && roomSelected != null) {
                    presenter.retrieveTables(buildingSelected.first, roomSelected.first)
                } else {
                    renderTables(listOf())
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
                val datePickerDialog = DatePickerDialog(this@FilterAvailabilitiesFragment.context(),
                        dateSetListener,
                        // set DatePickerDialog to point to today's date when it loads up
                        cal.get(Calendar.YEAR),
                        cal.get(Calendar.MONTH),
                        cal.get(Calendar.DAY_OF_MONTH))
                datePickerDialog.datePicker.minDate = Date().time
                datePickerDialog.show()
            }

        })
        updateDateInView()

        return binding.root
    }

    private fun checkFiltersBeforeBook(): Boolean {
        val buildingSpinnerSelected = getBuildingSelected()
        val roomSpinnerSelected = getRoomSelected()
        val tableSpinnerSelected = getTableSelected()
        if (TextUtils.isEmpty(buildingSpinnerSelected?.first)) {
            val errorTextView = binding.buildingsSpinner.selectedView as TextView
            errorTextView.error = getString(R.string.mandatory_error)
            return true
        }
        if (TextUtils.isEmpty(roomSpinnerSelected?.first)) {
            val errorTextView = binding.roomsSpinner.selectedView as TextView
            errorTextView.error = getString(R.string.mandatory_error)
            return true
        }
        if (TextUtils.isEmpty(tableSpinnerSelected?.first)) {
            val errorTextView = binding.tablesSpinner.selectedView as TextView
            errorTextView.error = getString(R.string.mandatory_error)
            return true
        }
        return false
    }

    override fun goToAvailabilitiesPage() {
        findNavController().navigate(R.id.action_filter_to_home)
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

    override fun getTableSelected(): Pair<String, String>? {
        val spinnerItem = binding.tablesSpinner.selectedItem as SpinnerItem<*>
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
        binding.buildingsSpinner.setSelection(aa.getPosition(SpinnerItem(id = presenter.getBuildingSelected()?.uID
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
        binding.roomsSpinner.setSelection(aa.getPosition(SpinnerItem(id = presenter.getRoomSelected()?.uID
                ?: "")))
    }

    override fun renderTables(tables: List<Table>) {
        var spinnerTables: List<SpinnerItem<String>> = tables.map { SpinnerItem(it.uID, it.name) }
        spinnerTables += SpinnerItem("", getString(R.string.select_all_values))

        // Create an ArrayAdapter using a simple spinner layout and languages array
        val aa = ArrayAdapter(context(), android.R.layout.simple_spinner_item, spinnerTables)
        // Set layout to use when the list of choices appear
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        // Set Adapter to Spinner
        binding.tablesSpinner.adapter = aa
        binding.tablesSpinner.setSelection(aa.getPosition(SpinnerItem("")))
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

    private fun initLayout() {
        val requestBooking = arguments?.getBoolean(REQUEST_BOOKING, false) ?: false
        val requestEdit = arguments?.getBoolean(REQUEST_EDIT, false) ?: false
        if (requestBooking) {
            initRequestBookingLayout()
        } else if (requestEdit) {
            initRequestEditLayout()
        } else {
            initRequestFilterLayout()
        }
    }

    private fun updateDateInView() {
        val myFormat = DATE_PATTERN
        val sdf = SimpleDateFormat(myFormat, Locale.getDefault())
        binding.startDateView.text = sdf.format(cal.getTime())
    }

    private fun initRequestBookingLayout() {
        binding.filterBtn.visibility = View.GONE
        binding.editBtn.visibility = View.GONE
        binding.bookBtn.visibility = View.VISIBLE
        binding.tablesSpinner.visibility = View.VISIBLE
        binding.tablesText.visibility = View.VISIBLE
    }

    private fun initRequestEditLayout() {
        binding.bookBtn.visibility = View.GONE
        binding.filterBtn.visibility = View.GONE
        binding.editBtn.visibility = View.VISIBLE
        binding.tablesSpinner.visibility = View.VISIBLE
        binding.tablesText.visibility = View.VISIBLE
    }

    private fun initRequestFilterLayout() {
        binding.bookBtn.visibility = View.GONE
        binding.editBtn.visibility = View.GONE
        binding.filterBtn.visibility = View.VISIBLE
        binding.tablesSpinner.visibility = View.GONE
        binding.tablesText.visibility = View.GONE
    }

    companion object {
        private val TAG = "FilterAvailabilitiesFragment"
        private val REQUEST_BOOKING = "requestBooking"
        private val REQUEST_EDIT = "requestEdit"
        private val DATE_PATTERN = "dd/MM/yyyy"
    }
}
