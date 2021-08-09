package it.dbortoluzzi.tuttiapposto.ui.fragments


import android.os.Bundle
import android.view.*
import android.widget.PopupMenu
import android.widget.Toast
import dagger.hilt.android.AndroidEntryPoint
import it.dbortoluzzi.data.R
import it.dbortoluzzi.data.databinding.FragmentBookingsBinding
import it.dbortoluzzi.tuttiapposto.model.BookingAggregate
import it.dbortoluzzi.tuttiapposto.ui.BaseMvpFragment
import it.dbortoluzzi.tuttiapposto.ui.activities.BookingsAdapter
import it.dbortoluzzi.tuttiapposto.ui.presenters.BookingsPresenter
import it.dbortoluzzi.tuttiapposto.ui.presenters.MainPresenter
import javax.inject.Inject

/**
 * BookingsFragment
 */

@AndroidEntryPoint
class BookingsFragment: BaseMvpFragment<BookingsFragment, BookingsPresenter>(), BookingsPresenter.View {

    lateinit var bookingAdapter: BookingsAdapter

    @Inject
    override lateinit var presenter: BookingsPresenter

    private lateinit var binding: FragmentBookingsBinding

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?,
    ): View? {
        super.onCreateView(inflater, container, savedInstanceState)

        binding = FragmentBookingsBinding.inflate(layoutInflater)

        bookingAdapter = BookingsAdapter(binding.emptyView).apply {
            onItemLongPress = { bookingAggregate, view ->
                //creating a popup menu
                val popup = PopupMenu(activity, view)
                //inflating menu from xml resource
                popup.inflate(R.menu.booking_menu)
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
                    popup.gravity = Gravity.END
                }
                //adding click listener
                popup.setOnMenuItemClickListener { item ->
                    when (item.itemId) {
                        R.id.delete_booking -> {
                            presenter.deleteBookingClicked(bookingAggregate.booking)
                        }
                    }
                    false
                }
                //displaying the popup
                popup.show()
            }
        }

        binding.recycler.adapter = bookingAdapter

        binding.filterBookingsBtn.setOnClickListener { showMessage("Da sviluppare")}

        return binding.root
    }

    override fun renderBookings(bookingAggregates: List<BookingAggregate>) {
        bookingAdapter.items = bookingAggregates
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
        Toast.makeText(context(), getString(R.string.network_not_connected), Toast.LENGTH_LONG).show()
    }

}
