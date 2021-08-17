package it.dbortoluzzi.tuttiapposto.ui.fragments


import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import it.dbortoluzzi.data.R
import it.dbortoluzzi.data.databinding.FragmentHomeNoconfigBinding
import it.dbortoluzzi.tuttiapposto.di.prefs
import it.dbortoluzzi.tuttiapposto.model.PrefsValidator
import it.dbortoluzzi.tuttiapposto.ui.activities.SettingsActivity

class HomeNoConfigFragment : Fragment() {

    private lateinit var binding: FragmentHomeNoconfigBinding

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?,
    ): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        binding = FragmentHomeNoconfigBinding.inflate(layoutInflater)

        binding.firstConfigBtn.setOnClickListener {
            val startIntent = Intent(context, SettingsActivity::class.java)
            startActivity(startIntent)
        }

        return binding.root
    }

    override fun onResume() {
        super.onResume()

        val navController = findNavController()
        navController.apply {
            if (PrefsValidator.isConfigured(prefs)) {
                navigate(R.id.nav_homeFragment)
            }
        }
    }
}
