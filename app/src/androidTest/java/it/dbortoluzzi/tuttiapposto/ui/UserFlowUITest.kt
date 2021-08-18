package it.dbortoluzzi.tuttiapposto.ui

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.contrib.DrawerActions
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.rule.ActivityTestRule
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import it.dbortoluzzi.data.R
import it.dbortoluzzi.tuttiapposto.TestConstants
import it.dbortoluzzi.tuttiapposto.di.BaseApp
import it.dbortoluzzi.tuttiapposto.ui.activities.LoginActivity
import org.hamcrest.CoreMatchers.not
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.RuleChain


@HiltAndroidTest
class UserFlowUITest {

    val hiltRule = HiltAndroidRule(this)

    val loginActivityRule = ActivityTestRule(LoginActivity::class.java, true, false)

    @get:Rule
    var testRules = RuleChain
                        .outerRule(hiltRule)
                        .around(loginActivityRule)

    @Before
    fun init() {
        hiltRule.inject()
        loginActivityRule.launchActivity(null)
        // default company
        BaseApp.prefs!!.companyUId = "FbF0or0c0NdBphbZcssm"
    }

    @Test
    fun testMainActivity() {
        onView(withId(R.id.LoginBtn)).run {
            check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
        }
        onView(withId(R.id.LoginEmail)).run {
            perform(typeText(TestConstants.FAKE_USER), closeSoftKeyboard())
        }
        onView(withId(R.id.LoginPassword)).run {
            perform(typeText(TestConstants.FAKE_PWD), closeSoftKeyboard())
        }
        onView(withId(R.id.LoginBtn)).run {
            perform(click())
        }
        // TODO: change with Idling Resource and Registry
        Thread.sleep(1000)
        onView(withId(R.id.main_nav_host)).run {
            check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
        }
        onView(withId(R.id.activityOptions)).run {
            check(ViewAssertions.matches(not(ViewMatchers.isDisplayed())))
            onView(withId(R.id.main_drawer_layout)).perform(DrawerActions.open())
            check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
            onView(withId(R.id.main_drawer_layout)).perform(DrawerActions.close())
            check(ViewAssertions.matches(not(ViewMatchers.isDisplayed())))
        }
        // bookings
        onView(withId(R.id.nav_bookingsFragment)).run {
            perform(click())
        }
        onView(withId(R.id.progressBar)).run {
            check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
        }
        Thread.sleep(3000)
        onView(withId(R.id.recycler)).run {
            check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
        }
        // availabilities
        onView(withId(R.id.nav_homeFragment)).run {
            perform(click())
        }
        onView(withId(R.id.progressBar)).run {
            check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
        }
        Thread.sleep(3000)
        onView(withId(R.id.recycler)).run {
            check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
        }
        // dashboard
        onView(withId(R.id.nav_dashboardFragment)).run {
            perform(click())
        }
        Thread.sleep(3000)
        onView(withId(R.id.progressBar)).run {
            check(ViewAssertions.matches(not(ViewMatchers.isDisplayed())))
            onView(withId(R.id.hour_occupation_bar_chart)).check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
            onView(withId(R.id.room_occupation_bar_chart)).check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
        }
        onView(withId(R.id.main_drawer_layout)).run {
            perform(DrawerActions.open())
            onView(withId(R.id.navLogout)).perform(click())
            onView(withId(R.id.LoginBtn)).check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
        }
    }
}
