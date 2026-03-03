package org.delcom.pam_p4_ifs23031.network.games.service

import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.logging.HttpLoggingInterceptor
import org.delcom.pam_p4_ifs23031.BuildConfig
import org.delcom.pam_p4_ifs23031.helper.ToolsHelper
import retrofit2.Retrofit
import java.util.concurrent.TimeUnit

class GenreMusikAppContainer : IGenreMusikAppContainer {
    private val loggingInterceptor = HttpLoggingInterceptor().apply {
        level = if (BuildConfig.DEBUG) {
            HttpLoggingInterceptor.Level.BODY
        } else {
            HttpLoggingInterceptor.Level.NONE
        }
    }

    private val json = Json {
        ignoreUnknownKeys = true
        coerceInputValues = true
    }

    private val okHttpClient = ToolsHelper.getUnsafeOkHttpClient().newBuilder().apply {
        if (BuildConfig.DEBUG) {
            addInterceptor(loggingInterceptor)
        }

        // Tingkatkan timeout menjadi 60 detik untuk upload gambar
        connectTimeout(60, TimeUnit.SECONDS)
        readTimeout(60, TimeUnit.SECONDS)
        writeTimeout(60, TimeUnit.SECONDS)
    }.build()

    private val retrofit = Retrofit.Builder()
        .baseUrl(BuildConfig.BASE_URL_GENREMUSIK)
        .addConverterFactory(json.asConverterFactory("application/json".toMediaType()))
        .client(okHttpClient)
        .build()

    private val retrofitService: GenreMusikApiService by lazy {
        retrofit.create(GenreMusikApiService::class.java)
    }

    override val genreMusikRepository: IGenreMusikRepository by lazy {
        GenreMusikRepository(retrofitService)
    }
}
