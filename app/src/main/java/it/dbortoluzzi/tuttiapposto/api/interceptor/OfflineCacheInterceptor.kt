package it.dbortoluzzi.tuttiapposto.api.interceptor

import android.util.Log
import it.dbortoluzzi.tuttiapposto.annotation.Cacheable
import it.dbortoluzzi.tuttiapposto.di.App
import it.dbortoluzzi.tuttiapposto.ui.util.Constants
import okhttp3.CacheControl
import okhttp3.Interceptor
import okhttp3.Response
import retrofit2.Invocation
import java.util.concurrent.TimeUnit

open class OfflineCacheInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        var request = chain.request()
        val invocation: Invocation? = request.tag(Invocation::class.java)

        if (invocation != null) {
            val annotation: Cacheable? =
                    invocation.method().getAnnotation(Cacheable::class.java)

            /* check if this request has the [Cacheable] annotation */
            if (annotation != null &&
                    annotation.annotationClass.simpleName.equals("Cacheable") &&
                    !App.isNetworkConnected()
            ) {
                // prevent caching when network is on. For that we use the "networkInterceptor"
                Log.d(TAG, "cache interceptor: called.")
                val cacheControl = CacheControl.Builder()
                        .maxStale(7, TimeUnit.DAYS)
                        .build()

                request = request.newBuilder()
                        .removeHeader(Constants.HEADER_PRAGMA)
                        .removeHeader(Constants.HEADER_CACHE_CONTROL)
                        .cacheControl(cacheControl)
                        .build()
            } else {
                Log.d(TAG, "cache interceptor: not called.")
            }
        }
        return chain.proceed(request)
    }

    companion object {
        val TAG = "OfflineCacheInterceptor"
    }
}