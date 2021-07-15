package it.dbortoluzzi.tuttiapposto.ui.activities

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import dagger.hilt.android.AndroidEntryPoint
import it.dbortoluzzi.data.R
import it.dbortoluzzi.data.databinding.ActivityMainBinding
import it.dbortoluzzi.domain.User
import it.dbortoluzzi.tuttiapposto.di.prefs
import it.dbortoluzzi.tuttiapposto.model.PrefsValidator
import it.dbortoluzzi.tuttiapposto.ui.BaseMvpActivity
import it.dbortoluzzi.tuttiapposto.ui.presenters.MainPresenter
import javax.inject.Inject


@AndroidEntryPoint
class MainActivity : BaseMvpActivity<MainActivity, MainPresenter>(), MainPresenter.View {

    @Inject
    override lateinit var mPresenter: MainPresenter

    var currentUser: User? = null

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var navController: NavController
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        currentUser = retrieveUser(savedInstanceState)
        if (currentUser == null) {
            initializeWhenUserIsNotLogged()
        } else {
            initializeWhenUserIsLogged()
        }
    }

    override fun initializeWhenUserIsNotLogged() {
        val intent = Intent(applicationContext, LoginActivity::class.java)
        startActivity(intent)
        finish()
    }

    override fun initializeWhenUserIsLogged() {
        navController = findNavController(R.id.main_nav_host) //Initialising navController
        val headerNavigationView = binding.mainNavigationView.getHeaderView(0)
        val navUserTextView = headerNavigationView.findViewById<TextView>(R.id.nav_user);
        val userStr = if (currentUser!!.displayName?.isNotEmpty() == true) {
            currentUser!!.displayName
        } else {
            currentUser!!.email
        }
        navUserTextView.text = userStr

        appBarConfiguration = AppBarConfiguration.Builder(
                R.id.homeFragment,
                R.id.homeNoConfigFragment,
                R.id.locationFragment,
                R.id.landindBookingFragment,
                R.id.dashboardFragment) //Pass the ids of fragments from nav_graph which you d'ont want to show back button in toolbar
                .setOpenableLayout(binding.mainDrawerLayout) //Pass the drawer layout id from activity xml
                .build()

        setSupportActionBar(binding.mainToolbar) //Set toolbar

        setupActionBarWithNavController(navController, appBarConfiguration) //Setup toolbar with back button and drawer icon according to appBarConfiguration

        // to use custom menu, but now it's commented
//        binding.mainToolbar.inflateMenu(R.menu.drawer_navigation)
//        supportActionBar?.setDisplayHomeAsUpEnabled(true);

        visibilityNavElements(navController) //If you want to hide drawer or bottom navigation configure that in this function
    }

    override fun onSaveInstanceState(outState: Bundle) { // Here You have to save count value
        super.onSaveInstanceState(outState)
        outState.apply {
            putSerializable(LoginActivity.USER_DATA, currentUser)
        }
    }

    private fun retrieveUser(savedInstanceState: Bundle?): User? {
        return intent?.getSerializableExtra(LoginActivity.USER_DATA) as User?
                ?: savedInstanceState?.getSerializable(LoginActivity.USER_DATA) as User?
                ?: mPresenter.getCurrentUserLogged()
    }

    private fun visibilityNavElements(navController: NavController) {

        navController.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                R.id.homeFragment -> {
                    binding.mainNavigationView.menu.findItem(R.id.action_filter).apply {
                        isVisible = true
                    }
                }
                else -> {
                    binding.mainNavigationView.menu.findItem(R.id.action_filter).apply {
                        isVisible = false
                    }
                }
            }
            showBothNavigation()
        }
    }

    private fun hideBothNavigation() { //Hide both drawer and bottom navigation bar
        binding.mainBottomNavigationView.visibility = View.GONE
        binding.mainNavigationView.visibility = View.GONE
        binding.mainDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED) //To lock navigation drawer so that it don't respond to swipe gesture
    }

    private fun hideBottomNavigation() { //Hide bottom navigation
        binding.mainBottomNavigationView.visibility = View.GONE
        binding.mainNavigationView.visibility = View.VISIBLE
        binding.mainDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED) //To unlock navigation drawer

        binding.mainNavigationView.setupWithNavController(navController) //Setup Drawer navigation with navController
    }

    private fun showBothNavigation() {
        binding.mainBottomNavigationView.visibility = View.VISIBLE
        binding.mainNavigationView.visibility = View.VISIBLE
        binding.mainDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED)
        setupNavControl() //To configure navController with drawer and bottom navigation
    }

    private fun setupNavControl() {
        binding.mainNavigationView.setupWithNavController(navController) //Setup Drawer navigation with navController
        binding.mainBottomNavigationView.setupWithNavController(navController) //Setup Bottom navigation with navController
    }

    override fun onSupportNavigateUp(): Boolean { //Setup appBarConfiguration for back arrow
        return NavigationUI.navigateUp(navController, appBarConfiguration)
    }

    override fun onBackPressed() {
        when { //If drawer layout is open close that on back pressed
            binding.mainDrawerLayout.isDrawerOpen(GravityCompat.START) -> {
                binding.mainDrawerLayout.closeDrawer(GravityCompat.START)
            }
            else -> {
                super.onBackPressed() //If drawer is already in closed condition then go back
            }
        }
    }

    override fun onLogoutClicked(item: MenuItem) {
        mPresenter.doLogout()
    }

    override fun onSettingsClicked(item: MenuItem) {
        val startIntent = Intent(applicationContext, SettingsActivity::class.java)
        startActivity(startIntent)
    }

    override fun onFilterClicked(item: MenuItem) {
        binding.mainDrawerLayout.closeDrawer(GravityCompat.START)
        navController.navigate(R.id.filterAvailabilitiesFragment)
    }

    override fun logoutSuccess() {
        PrefsValidator.resetPrefs(prefs)
        val startIntent = Intent(applicationContext, LoginActivity::class.java)
        startActivity(startIntent)
        Toast.makeText(this, getString(R.string.logout_success), Toast.LENGTH_SHORT).show()
        finish()
    }

    override fun logoutError(errorMessage: String) {
        Log.e(TAG, "Error in logout: $errorMessage")
        Toast.makeText(this, getString(R.string.logout_error), Toast.LENGTH_SHORT).show()
    }


    override fun closeNavigationDrawer() {
        binding.mainDrawerLayout.closeDrawer(GravityCompat.START)
    }

    companion object {
        private val TAG = "MainActivity"
    }
}