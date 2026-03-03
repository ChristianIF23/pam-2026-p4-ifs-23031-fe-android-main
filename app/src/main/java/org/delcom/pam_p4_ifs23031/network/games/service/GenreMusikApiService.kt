package org.delcom.pam_p4_ifs23031.network.games.service

import okhttp3.MultipartBody
import okhttp3.RequestBody
import org.delcom.pam_p4_ifs23031.network.data.ResponseMessage
import org.delcom.pam_p4_ifs23031.network.games.data.ResponseGenreMusik
import org.delcom.pam_p4_ifs23031.network.games.data.ResponseGenreMusikAdd
import org.delcom.pam_p4_ifs23031.network.games.data.ResponseGenreMusiks
import retrofit2.http.*

interface GenreMusikApiService {
    @GET("genreMusik")
    suspend fun getAllGenreMusiks(
        @Query("search") search: String? = null
    ): ResponseMessage<ResponseGenreMusiks?>

    @Multipart
    @POST("genreMusik")
    suspend fun postGenreMusik(
        @Part("nama") nama: RequestBody,
        @Part("deskripsi") deskripsi: RequestBody,
        @Part("contohArtis") contohArtis: RequestBody,
        @Part("asalUsul") asalUsul: RequestBody,
        @Part file: MultipartBody.Part
    ): ResponseMessage<ResponseGenreMusikAdd?>

    @GET("genreMusik/{id}")
    suspend fun getGenreMusikById(
        @Path("id") genreMusikId: String
    ): ResponseMessage<ResponseGenreMusik?>

    @Multipart
    @PUT("genreMusik/{id}")
    suspend fun putGenreMusik(
        @Path("id") genreMusikId: String,
        @Part("nama") nama: RequestBody,
        @Part("deskripsi") deskripsi: RequestBody,
        @Part("contohArtis") contohArtis: RequestBody,
        @Part("asalUsul") asalUsul: RequestBody,
        @Part file: MultipartBody.Part? = null
    ): ResponseMessage<String?>

    @DELETE("genreMusik/{id}")
    suspend fun deleteGenreMusik(
        @Path("id") genreMusikId: String
    ): ResponseMessage<String?>
}
