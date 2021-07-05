package it.dbortoluzzi.tuttiapposto.api.interceptor

import android.util.Log
import it.dbortoluzzi.tuttiapposto.ui.util.Constants.HEADER_CACHE_CONTROL
import it.dbortoluzzi.tuttiapposto.ui.util.Constants.HEADER_PRAGMA
import okhttp3.CacheControl
import okhttp3.Interceptor
import okhttp3.Response
import java.util.concurrent.TimeUnit

class NetworkInterceptor: Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        Log.d(TAG,"network interceptor: called.")

        val response = chain.proceed(chain.request())

        val cacheControl = CacheControl.Builder()
                .maxAge(20, TimeUnit.SECONDS)
                .build()

        return response.newBuilder()
                .removeHeader(HEADER_PRAGMA)
                .removeHeader(HEADER_CACHE_CONTROL)
                .header(HEADER_CACHE_CONTROL, cacheControl.toString())
                .build()    }

    companion object {
        val TAG = "NetworkInterceptor"
    }
}