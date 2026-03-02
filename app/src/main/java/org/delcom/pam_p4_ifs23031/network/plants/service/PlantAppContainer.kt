package org.delcom.pam_p4_ifs23031.network.plants.service

import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import org.delcom.pam_p4_ifs23031.BuildConfig
import org.delcom.pam_p4_ifs23031.helper.ToolsHelper
import java.util.concurrent.TimeUnit

class PlantAppContainer : IPlantAppContainer {

    private val loggingInterceptor = HttpLoggingInterceptor().apply {
        level = if (BuildConfig.DEBUG) {
            HttpLoggingInterceptor.Level.BODY
        } else {
            HttpLoggingInterceptor.Level.NONE
        }
    }

    // Gunakan builder dari ToolsHelper agar bypass SSL aktif
    private val okHttpClient = ToolsHelper.getUnsafeOkHttpClient().newBuilder().apply {
        if (BuildConfig.DEBUG) {
            addInterceptor(loggingInterceptor)
        }

        connectTimeout(30, TimeUnit.SECONDS)
        readTimeout(30, TimeUnit.SECONDS)
        writeTimeout(30, TimeUnit.SECONDS)
    }.build()

    private val retrofit = Retrofit.Builder()
        .baseUrl(BuildConfig.BASE_URL_GENREMUSIK) // Sudah benar menggunakan BASE_URL_GAME
        .addConverterFactory(GsonConverterFactory.create())
        .client(okHttpClient)
        .build()

    private val retrofitService: PlantApiService by lazy {
        retrofit.create(PlantApiService::class.java)
    }

    override val plantRepository: IPlantRepository by lazy {
        PlantRepository(retrofitService)
    }
}