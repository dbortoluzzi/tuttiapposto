package it.dbortoluzzi.tuttiapposto.ui.fragments


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.NavigationUI
import dagger.hilt.android.AndroidEntryPoint
import it.dbortoluzzi.data.R
import it.dbortoluzzi.data.databinding.FragmentFilterAvailabilitiesBinding
import it.dbortoluzzi.tuttiapposto.ui.BaseMvpFragment
import it.dbortoluzzi.tuttiapposto.ui.activities.MainActivity
import it.dbortoluzzi.tuttiapposto.ui.presenters.FilterAvailabilitiesPresenter
import it.dbortoluzzi.tuttiapposto.ui.presenters.MainPresenter
import javax.inject.Inject

/**
 * FilterAvailabilitiesFragment
 */

@AndroidEntryPoint
class FilterAvailabilitiesFragment: BaseMvpFragment<FilterAvailabilitiesFragment, FilterAvailabilitiesPresenter>(), FilterAvailabilitiesPresenter.View {

    @Inject
    override lateinit var presenter: FilterAvailabilitiesPresenter

    private lateinit var binding: FragmentFilterAvailabilitiesBinding

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        super.onCreateView(inflater, container, savedInstanceState)

        binding = FragmentFilterAvailabilitiesBinding.inflate(layoutInflater)

        binding.filterBtn.setOnClickListener { presenter.filterBtnClicked()}

        return binding.root
    }

    override fun goToAvailabilitiesPage() {
        findNavController().navigate(R.id.homeFragment)
        if (activity is MainPresenter.View) {
            (activity as MainPresenter.View).closeNavigationDrawer()
        }
    }

}
