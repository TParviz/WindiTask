package tj.winditask.data.remote

import androidx.databinding.ktx.BuildConfig
import com.google.gson.GsonBuilder
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import tj.winditask.di.BASE_URL
import java.util.concurrent.TimeUnit

class ServiceGenerator {

    companion object {

        private const val CONNECTION_TIMEOUT = 15L

        fun <T> generate(serviceClass: Class<T>): T {
            val logging = HttpLoggingInterceptor()
            logging.level =
                if (BuildConfig.DEBUG) HttpLoggingInterceptor.Level.BODY
                else HttpLoggingInterceptor.Level.NONE
            val okHttpClientBuilder = OkHttpClient().newBuilder()
            okHttpClientBuilder.addInterceptor(logging)
            okHttpClientBuilder.connectTimeout(CONNECTION_TIMEOUT, TimeUnit.SECONDS)
            okHttpClientBuilder.readTimeout(CONNECTION_TIMEOUT, TimeUnit.SECONDS)
            val okHttpClient = okHttpClientBuilder.build()

            val retrofit = Retrofit.Builder().baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(GsonBuilder().create()))
                .addCallAdapterFactory(CoroutineCallAdapterFactory())
                .client(okHttpClient)
                .build()

            return retrofit.create(serviceClass)
        }
    }
}