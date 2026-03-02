package org.delcom.pam_p4_ifs23031.network.games.service

import okhttp3.logging.HttpLoggingInterceptor
import org.delcom.pam_p4_ifs23031.BuildConfig
import org.delcom.pam_p4_ifs23031.helper.ToolsHelper
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

class GenreMusikAppContainer : IGenreMusikAppContainer {

    // 1. Logging Interceptor untuk memantau request/response di Logcat
    private val loggingInterceptor = HttpLoggingInterceptor().apply {
        level = if (BuildConfig.DEBUG) {
            HttpLoggingInterceptor.Level.BODY
        } else {
            HttpLoggingInterceptor.Level.NONE
        }
    }

    /**
     * 2. OkHttpClient Konfigurasi
     * Kita menggunakan .newBuilder() dari getUnsafeOkHttpClient()
     * supaya settingan bypass SSL (Trust All Certs) tetap terbawa.
     */
    private val okHttpClient = ToolsHelper.getUnsafeOkHttpClient().newBuilder().apply {
        // Menambahkan Logging Interceptor jika dalam mode Debug
        if (BuildConfig.DEBUG) {
            addInterceptor(loggingInterceptor)
        }

        // Pengaturan Timeout
        connectTimeout(30, TimeUnit.SECONDS)
        readTimeout(30, TimeUnit.SECONDS)
        writeTimeout(30, TimeUnit.SECONDS)
    }.build()

    // 3. Konfigurasi Retrofit
    private val retrofit = Retrofit.Builder()
        .baseUrl(BuildConfig.BASE_URL_GENREMUSIK)
        .addConverterFactory(GsonConverterFactory.create())
        .client(okHttpClient) // Menggunakan client yang sudah di-bypass SSL-nya
        .build()

    // 4. Inisialisasi API Service secara Lazy
    private val retrofitService: GenreMusikApiService by lazy {
        retrofit.create(GenreMusikApiService::class.java)
    }

    // 5. Implementasi Repository
    override val genreMusikRepository: IGenreMusikRepository by lazy {
        GenreMusikRepository(retrofitService)
    }
}