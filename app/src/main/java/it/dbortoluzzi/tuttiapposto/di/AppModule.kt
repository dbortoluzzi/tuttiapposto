package it.dbortoluzzi.tuttiapposto.di

import android.app.Activity
import androidx.fragment.app.Fragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import dagger.hilt.android.components.FragmentComponent
import dagger.hilt.android.scopes.FragmentScoped
import dagger.hilt.components.SingletonComponent
import it.dbortoluzzi.data.*
import it.dbortoluzzi.tuttiapposto.api.ApiHelper
import it.dbortoluzzi.tuttiapposto.api.ApiHelperImpl
import it.dbortoluzzi.tuttiapposto.api.ApiService
import it.dbortoluzzi.tuttiapposto.framework.*
import it.dbortoluzzi.tuttiapposto.services.BookingNotificationService
import it.dbortoluzzi.tuttiapposto.ui.presenters.*
import it.dbortoluzzi.tuttiapposto.ui.util.Constants
import it.dbortoluzzi.usecases.*
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class AppModule {

    /* Global */
    @Provides
    fun provideCacheEnabled() = Constants.CACHE_ENABLED

    /* Retrofit */
    @Provides
    fun provideBaseUrl() = if (BuildConfig.DEBUG) {Constants.DEV_BASE_URL} else {Constants.PROD_BASE_URL}

    @Singleton
    @Provides
    fun provideOkHttpClient() = if (BuildConfig.DEBUG) {
        val loggingInterceptor = HttpLoggingInterceptor()
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY)
        OkHttpClient.Builder()
                .addInterceptor(loggingInterceptor)
                .readTimeout(Constants.READ_SECONDS_TIMEOUT_RETROFIT, TimeUnit.SECONDS)
                .writeTimeout(Constants.WRITE_SECONDS_TIMEOUT_RETROFIT, TimeUnit.SECONDS)
                .build()
    } else {
        OkHttpClient
                .Builder()
                .readTimeout(Constants.READ_SECONDS_TIMEOUT_RETROFIT, TimeUnit.SECONDS)
                .writeTimeout(Constants.WRITE_SECONDS_TIMEOUT_RETROFIT, TimeUnit.SECONDS)
                .build()
    }

    @Singleton
    @Provides
    fun provideRetrofit(okHttpClient: OkHttpClient, BASE_URL: String): Retrofit {
        val gson = GsonBuilder().setDateFormat(Constants.DEFAULT_DATE_FORMATTER).create()
        return Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create(gson))
                .baseUrl(BASE_URL)
                .client(okHttpClient)
                .build()
    }

    @Provides
    @Singleton
    fun provideApiService(retrofit: Retrofit) = retrofit.create(ApiService::class.java)

    @Provides
    @Singleton
    fun provideApiHelper(apiHelper: ApiHelperImpl): ApiHelper = apiHelper

    /*Sources*/
    @Provides
    @Singleton
    fun provideAuthenticationSource(firebaseAuth: FirebaseAuth, userPersistenceSource: UserPersistenceSource): AuthenticationSource = FirebaseAuthenticationSource(firebaseAuth, userPersistenceSource)

    @Provides
    @Singleton
    fun provideUserPersistenceSource(firebaseFirestore: FirebaseFirestore): UserPersistenceSource = FirebaseUserSource(firebaseFirestore)

    @Provides
    @Singleton
    fun provideCompanyPersistenceSource(firebaseFirestore: FirebaseFirestore, CACHE_ENABLED: Boolean): CompanyPersistenceSource = FirebaseCompanySource(firebaseFirestore, CACHE_ENABLED)

    @Provides
    @Singleton
    fun provideBuildingPersistenceSource(firebaseFirestore: FirebaseFirestore, CACHE_ENABLED: Boolean): BuildingPersistenceSource = FirebaseBuildingSource(firebaseFirestore, CACHE_ENABLED)

    @Provides
    @Singleton
    fun provideRoomPersistenceSource(firebaseFirestore: FirebaseFirestore, CACHE_ENABLED: Boolean): RoomPersistenceSource = FirebaseRoomSource(firebaseFirestore, CACHE_ENABLED)

    @Provides
    @Singleton
    fun provideTablePersistenceSource(firebaseFirestore: FirebaseFirestore, CACHE_ENABLED: Boolean): TablePersistenceSource = FirebaseTableSource(firebaseFirestore, CACHE_ENABLED)

    @Provides
    @Singleton
    fun provideAvailabilityReportSource(firebaseFirestore: FirebaseFirestore, CACHE_ENABLED: Boolean): AvailabilityReportSource = FirebaseAvailabilityReportSource(firebaseFirestore, CACHE_ENABLED)

    @Provides
    @Singleton
    fun provideAvailabilitiesSource(apiHelper: ApiHelper): AvailabilitiesSource = AndroidTableSource(apiHelper)

    @Provides
    @Singleton
    fun provideBookingPersistenceSource(apiHelper: ApiHelper): BookingPersistenceSource = AndroidBookingSource(apiHelper)

    @Provides
    @Singleton
    fun provideStatisticsPersistenceSource(apiHelper: ApiHelper): StatisticsPersistenceSource = AndroidStatisticsSource(apiHelper)

    @Provides
    @Singleton
    fun provideSelectedAvailabilityFiltersSource(): SelectedAvailabilityFiltersSource = InMemorySelectedAvailabilityFiltersSource()

    /*Repositories*/
    @Provides
    @Singleton
    fun provideUsersRepository(authenticationSource: AuthenticationSource): UsersRepository {
        return UsersRepository(authenticationSource)
    }

    @Provides
    @Singleton
    fun provideCompaniesRepository(companyPersistenceSource: CompanyPersistenceSource): CompaniesRepository {
        return CompaniesRepository(companyPersistenceSource)
    }

    @Provides
    @Singleton
    fun provideBuildingsRepository(buildingPersistenceSource: BuildingPersistenceSource): BuildingsRepository {
        return BuildingsRepository(buildingPersistenceSource)
    }

    @Provides
    @Singleton
    fun provideRoomsRepository(roomPersistenceSource: RoomPersistenceSource): RoomsRepository {
        return RoomsRepository(roomPersistenceSource)
    }

    @Provides
    @Singleton
    fun provideTablesRepository(availabilitiesSource: AvailabilitiesSource, tablePersistenceSource: TablePersistenceSource): TablesRepository {
        return TablesRepositoryImpl(availabilitiesSource, tablePersistenceSource)
    }

    @Provides
    @Singleton
    fun provideAvailabilityReportsRepository(availabilityReportSource: AvailabilityReportSource): AvailabilityReportsRepository {
        return AvailabilityReportsRepository(availabilityReportSource)
    }

    @Provides
    @Singleton
    fun provideBookingsRepository(bookingSource: BookingPersistenceSource): BookingsRepository {
        return BookingsRepository(bookingSource)
    }

    @Provides
    @Singleton
    fun provideStatisticsRepository(statisticsPersistenceSource: StatisticsPersistenceSource): StatisticsRepository {
        return StatisticsRepositoryImpl(statisticsPersistenceSource)
    }

    @Provides
    @Singleton
    fun provideSelectedAvailabilityFiltersRepository(selectedAvailabilityFiltersSource: SelectedAvailabilityFiltersSource): SelectedAvailabilityFiltersRepository {
        return SelectedAvailabilityFiltersRepository(selectedAvailabilityFiltersSource)
    }

    @Provides
    fun provideBookingNotificationService(): BookingNotificationService {
        return BookingNotificationService()
    }

}

@Module
@InstallIn(SingletonComponent::class)
class FireBaseModule {

    @Provides
    @Singleton
    fun provideFireBaseAuth(): FirebaseAuth = FirebaseAuth.getInstance()

    @Provides
    @Singleton
    fun provideFirestore(): FirebaseFirestore = FirebaseFirestore.getInstance()
}

@InstallIn(ActivityComponent::class)
@Module
object ActivityModule {

    @Provides
    fun bindLoginActivity(activity: Activity): LoginPresenter.View {
        return activity as LoginPresenter.View
    }

    @Provides
    fun bindRegisterActivity(activity: Activity): RegisterPresenter.View {
        return activity as RegisterPresenter.View
    }

    @Provides
    fun bindMainActivity(activity: Activity): MainPresenter.View {
        return activity as MainPresenter.View
    }

    @Provides
    fun bindSettingsActivity(activity: Activity): SettingsPresenter.View {
        return activity as SettingsPresenter.View
    }
}

@InstallIn(FragmentComponent::class)
@Module
object FragmentModule {

    @Provides
    @FragmentScoped
    fun bindDashboardFragment(fragment: Fragment): DashboardPresenter.View {
        return fragment as DashboardPresenter.View
    }

    @Provides
    @FragmentScoped
    fun bindAvailabilityFragment(fragment: Fragment): AvailabilityPresenter.View {
        return fragment as AvailabilityPresenter.View
    }

    @Provides
    @FragmentScoped
    fun bindFilterAvailabilities(fragment: Fragment): FilterAvailabilitiesPresenter.View {
        return fragment as FilterAvailabilitiesPresenter.View
    }

    @Provides
    @FragmentScoped
    fun bindLandingBooking(fragment: Fragment): LandingBookingPresenter.View {
        return fragment as LandingBookingPresenter.View
    }


    @Provides
    @FragmentScoped
    fun bindBookingFragment(fragment: Fragment): BookingsPresenter.View {
        return fragment as BookingsPresenter.View
    }
}

@Module
@InstallIn(SingletonComponent::class)
class UseCasesModule {

    @Provides
    @Singleton
    fun provideLogin(usersRepository: UsersRepository): Login = Login(usersRepository)

    @Provides
    @Singleton
    fun provideRegister(usersRepository: UsersRepository): Register = Register(usersRepository)

    @Provides
    @Singleton
    fun provideLogout(usersRepository: UsersRepository): Logout = Logout(usersRepository)

    @Provides
    @Singleton
    fun provideGetUser(usersRepository: UsersRepository): GetUser = GetUser(usersRepository)

    @Provides
    @Singleton
    fun provideGetCompanies(companiesRepository: CompaniesRepository): GetCompanies = GetCompanies(companiesRepository)

    @Provides
    @Singleton
    fun provideGetBuildings(buildingsRepository: BuildingsRepository): GetBuildings = GetBuildings(buildingsRepository)

    @Provides
    @Singleton
    fun provideGetRooms(roomsRepository: RoomsRepository): GetRooms = GetRooms(roomsRepository)

    @Provides
    @Singleton
    fun provideGetRoomsByCompany(roomsRepository: RoomsRepository): GetRoomsByCompany = GetRoomsByCompany(roomsRepository)

    @Provides
    @Singleton
    fun provideGetTables(tablesRepository: TablesRepository): GetTables = GetTables(tablesRepository)

    @Provides
    @Singleton
    fun provideCreateAvailabilityReport(repository: AvailabilityReportsRepository): CreateAvailabilityReport = CreateAvailabilityReport(repository)

    @Provides
    @Singleton
    fun provideGetTablesWithFilters(tablesRepository: TablesRepository): GetTablesWithFilters = GetTablesWithFilters(tablesRepository)

    @Provides
    @Singleton
    fun provideGetAvailableTables(tablesRepository: TablesRepository): GetAvailableTables = GetAvailableTables(tablesRepository)

    @Provides
    @Singleton
    fun provideCreateBooking(bookingsRepository: BookingsRepository): CreateBooking = CreateBooking(bookingsRepository)

    @Provides
    @Singleton
    fun provideEditBooking(bookingsRepository: BookingsRepository): EditBooking = EditBooking(bookingsRepository)

    @Provides
    @Singleton
    fun provideDeleteBooking(bookingsRepository: BookingsRepository): DeleteBooking = DeleteBooking(bookingsRepository)

    @Provides
    @Singleton
    fun provideGetBookings(bookingsRepository: BookingsRepository): GetBookings = GetBookings(bookingsRepository)

    @Provides
    @Singleton
    fun provideGetOccupationByHour(statisticsRepository: StatisticsRepository): GetOccupationByHour = GetOccupationByHour(statisticsRepository)

    @Provides
    @Singleton
    fun provideGetOccupationByRoom(statisticsRepository: StatisticsRepository): GetOccupationByRoom = GetOccupationByRoom(statisticsRepository)
}
