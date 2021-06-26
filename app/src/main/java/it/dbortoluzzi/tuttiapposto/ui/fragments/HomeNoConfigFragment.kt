package it.dbortoluzzi.tuttiapposto.ui.fragments


import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import it.dbortoluzzi.data.R
import it.dbortoluzzi.data.databinding.FragmentHomeNoconfigBinding
import it.dbortoluzzi.tuttiapposto.di.prefs
import it.dbortoluzzi.tuttiapposto.ui.activities.LoginActivity
import it.dbortoluzzi.tuttiapposto.ui.activities.MainActivity

class HomeNoConfigFragment : Fragment() {

    private lateinit var binding: FragmentHomeNoconfigBinding

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?,
    ): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        binding = FragmentHomeNoconfigBinding.inflate(layoutInflater)

        val navController = findNavController()
        navController.apply {
            if (MainActivity.isConfigured()) {
                navigate(R.id.homeFragment)
            }
        }

        binding.firstConfigBtn.setOnClickListener {
            prefs.company = "Pippo"

            navController.apply {
                navigate(R.id.homeFragment)
            }
        }

        return binding.root
    }


}
