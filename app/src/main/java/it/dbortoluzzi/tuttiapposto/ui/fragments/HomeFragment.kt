package it.dbortoluzzi.tuttiapposto.ui.fragments


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import it.dbortoluzzi.data.R
import it.dbortoluzzi.data.databinding.FragmentDashboardBinding
import it.dbortoluzzi.data.databinding.FragmentHomeBinding
import it.dbortoluzzi.tuttiapposto.di.prefs
import it.dbortoluzzi.tuttiapposto.model.PrefsValidator
import it.dbortoluzzi.tuttiapposto.ui.activities.MainActivity

class HomeFragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding

    override fun onResume() {
        super.onResume()

        // TODO: fix ui effect
        val navController = findNavController()
        navController.apply {
            if (!PrefsValidator.isConfigured(prefs)) {
                navigate(R.id.homeNoConfigFragment)
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        binding = FragmentHomeBinding.inflate(layoutInflater)


        binding.resetBtn.setOnClickListener {
            prefs.company = null

            val navController = findNavController()
            navController.apply {
                navigate(R.id.homeNoConfigFragment)
            }

        }

        return binding.root
    }


}
