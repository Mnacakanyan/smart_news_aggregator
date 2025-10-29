package com.news.smartnewsaggregator.data.remote.interceptor

import kotlinx.coroutines.delay
import okhttp3.Interceptor
import okhttp3.Response
import java.io.IOException

class RetryInterceptor(private val maxRetries: Int = 3) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        var request = chain.request()
        var response: Response? = null
        var exception: IOException? = null

        var tryCount = 0
        while (tryCount < maxRetries && (response == null || !response.isSuccessful)) {
            try {
                response = chain.proceed(request)
            } catch (e: IOException) {
                exception = e
            }

            if (response != null && response.isSuccessful) {
                return response
            }

            tryCount++

            if (tryCount < maxRetries) {
                val waitTime = (1000 * Math.pow(2.0, tryCount.toDouble())).toLong()
                try {
                    Thread.sleep(waitTime)
                } catch (e: InterruptedException) {
                    Thread.currentThread().interrupt()
                }
            }
        }

        if (response != null) {
            return response
        }

        throw exception!!
    }
}
