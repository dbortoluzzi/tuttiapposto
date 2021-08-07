package it.dbortoluzzi.tuttiapposto.ui.fragments


import android.app.AlertDialog
import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import it.dbortoluzzi.data.R
import it.dbortoluzzi.data.databinding.FragmentLandingBookingBinding
import it.dbortoluzzi.tuttiapposto.di.prefs
import it.dbortoluzzi.tuttiapposto.ui.BaseMvpFragment
import it.dbortoluzzi.tuttiapposto.ui.presenters.LandingBookingPresenter
import it.dbortoluzzi.tuttiapposto.ui.util.Constants.BUILDING_ID
import it.dbortoluzzi.tuttiapposto.ui.util.Constants.BUNDLE_DATA
import it.dbortoluzzi.tuttiapposto.ui.util.Constants.END_DATE
import it.dbortoluzzi.tuttiapposto.ui.util.Constants.ROOM_ID
import it.dbortoluzzi.tuttiapposto.ui.util.Constants.START_DATE
import it.dbortoluzzi.tuttiapposto.ui.util.Constants.TABLE_ID
import java.util.*
import javax.inject.Inject

/**
 * AvailabilityFragment
 */

@AndroidEntryPoint
class LandingBookingFragment : BaseMvpFragment<LandingBookingFragment, LandingBookingPresenter>(), LandingBookingPresenter.View {
    @Inject
    override lateinit var presenter: LandingBookingPresenter

    private lateinit var binding: FragmentLandingBookingBinding

    var confirmBookingButtonClick: ((DialogInterface, Int) -> Unit)? = null
    var dismissBookingButtonClick: ((DialogInterface, Int) -> Unit)? = null

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?,
    ): View? {
        super.onCreateView(inflater, container, savedInstanceState)

        binding = FragmentLandingBookingBinding.inflate(layoutInflater)

        confirmBookingButtonClick = { dialog: DialogInterface, _: Int ->
            confirmBookingBtnClicked()
        }
        dismissBookingButtonClick = { dialog: DialogInterface, which: Int ->
            dismissBookingBtnClicked()
        }

        val bookingData = arguments?.get(BUNDLE_DATA) as Map<String, Any>
        presenter.searchAvailabilityToConfirm(
                prefs.companyUId!!,
                bookingData[BUILDING_ID] as String,
                bookingData[ROOM_ID] as String,
                bookingData[TABLE_ID] as String,
                bookingData[START_DATE] as Date,
                bookingData[END_DATE] as Date
        )

        return binding.root
    }

    override fun showProgressBar() {
        binding.progressBar.visibility = View.VISIBLE
    }

    override fun hideProgressBar() {
        binding.progressBar.visibility = View.GONE
    }

    override fun showNetworkError() {
        Toast.makeText(context(), getString(R.string.network_not_connected), Toast.LENGTH_LONG).show()
    }

    override fun showBookingConfirm() {
        val builder = AlertDialog.Builder(context)

        with(builder) {
            setTitle(getString(R.string.landing_book_title))
            setMessage(getString(R.string.landing_book_message))
            setPositiveButton(getString(R.string.landing_book_confirm), confirmBookingButtonClick)
            setNegativeButton(android.R.string.no, dismissBookingButtonClick)
            show()
        }
    }

    override fun showBookingNotAvailable() {
        Toast.makeText(context?.applicationContext,
                R.string.landing_book_not_available_message, Toast.LENGTH_LONG).show()

        navigateTo(R.id.action_book_to_filter)
    }

    override fun confirmBookingBtnClicked() {
        presenter.bookTable()
    }

    override fun dismissBookingBtnClicked() {
        Toast.makeText(context?.applicationContext,
                R.string.landing_book_dismiss_message, Toast.LENGTH_LONG).show()

        navigateTo(R.id.action_book_to_filter)
    }

    override fun bookingDoneWithSuccess() {
        Toast.makeText(context?.applicationContext,
                R.string.landing_book_success_message, Toast.LENGTH_LONG).show()

        navigateTo(R.id.action_book_to_home)
    }

    override fun bookingNotDoneWithError() {
        Toast.makeText(context?.applicationContext,
                R.string.landing_book_error_message, Toast.LENGTH_LONG).show()

        navigateTo(R.id.action_book_to_home)
    }

    private fun navigateTo(resId: Int) {
        val navController = findNavController()
        navController.apply {
            navigate(resId)
        }
    }

}
