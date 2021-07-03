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
import it.dbortoluzzi.tuttiapposto.ui.presenters.*
import it.dbortoluzzi.tuttiapposto.ui.util.Constants
import it.dbortoluzzi.usecases.*
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class AppModule {

    /* Retrofit */
    @Provides
    fun provideBaseUrl() = Constants.BASE_URL

    @Singleton
    @Provides
    fun provideOkHttpClient() = if (BuildConfig.DEBUG) {
        val loggingInterceptor = HttpLoggingInterceptor()
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY)
        OkHttpClient.Builder()
                .addInterceptor(loggingInterceptor)
                .build()
    } else {
        OkHttpClient
                .Builder()
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
    fun locationPersistenceSource(): LocationPersistenceSource = InMemoryLocationPersistenceSource()


    @Provides
    @Singleton
    fun deviceLocationSource(): DeviceLocationSource = FakeLocationSource()

    @Provides
    @Singleton
    fun authenticationSource(firebaseAuth: FirebaseAuth): AuthenticationSource = FirebaseAuthenticationSource(firebaseAuth)

    @Provides
    @Singleton
    fun companySource(firebaseFirestore: FirebaseFirestore): CompanyPersistenceSource = FirebaseCompanySource(firebaseFirestore)

    @Provides
    @Singleton
    fun androidTableSource(apiHelper: ApiHelper): TablePersistenceSource = AndroidTableSource(apiHelper)

    /*Repositories*/
    @Provides
    @Singleton
    fun locationsRepository(persistence: LocationPersistenceSource, deviceLocationSource: DeviceLocationSource): LocationsRepository {
        return LocationsRepository(persistence, deviceLocationSource)
    }

    @Provides
    @Singleton
    fun usersRepository(authenticationSource: AuthenticationSource): UsersRepository {
        return UsersRepository(authenticationSource)
    }

    @Provides
    @Singleton
    fun companiesRepository(companyPersistenceSource: CompanyPersistenceSource): CompaniesRepository {
        return CompaniesRepository(companyPersistenceSource)
    }

    @Provides
    @Singleton
    fun tableRepository(tablePersistenceSource: TablePersistenceSource): TablesRepository {
        return TablesRepository(tablePersistenceSource)
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
    fun bindLocationFragment(fragment: Fragment): LocationPresenter.View {
        return fragment as LocationPresenter.View
    }
}

@Module
@InstallIn(SingletonComponent::class)
class UseCasesModule {

    @Provides
    @Singleton
    fun getLocations(locationsRepository: LocationsRepository): GetLocations = GetLocations(locationsRepository)

    @Provides
    @Singleton
    fun requestNewLocation(locationsRepository: LocationsRepository): RequestNewLocation = RequestNewLocation(locationsRepository)

    @Provides
    @Singleton
    fun login(usersRepository: UsersRepository): Login = Login(usersRepository)

    @Provides
    @Singleton
    fun register(usersRepository: UsersRepository): Register = Register(usersRepository)

    @Provides
    @Singleton
    fun logout(usersRepository: UsersRepository): Logout = Logout(usersRepository)

    @Provides
    @Singleton
    fun getUser(usersRepository: UsersRepository): GetUser = GetUser(usersRepository)

    @Provides
    @Singleton
    fun getCompanies(companiesRepository: CompaniesRepository): GetCompanies = GetCompanies(companiesRepository)

    @Provides
    @Singleton
    fun getAvailableTables(tablesRepository: TablesRepository): GetAvailableTables = GetAvailableTables(tablesRepository)
}
