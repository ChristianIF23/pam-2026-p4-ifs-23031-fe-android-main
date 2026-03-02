package org.delcom.pam_p4_ifs23031.network.games.service

import okhttp3.MultipartBody
import org.delcom.pam_p4_ifs23031.network.data.ResponseMessage
import org.delcom.pam_p4_ifs23031.network.games.data.ResponseGenreMusik
import org.delcom.pam_p4_ifs23031.network.games.data.ResponseGenreMusikAdd
import org.delcom.pam_p4_ifs23031.network.games.data.ResponseGenreMusiks

interface IGenreMusikRepository {
    suspend fun getAllGenreMusiks(search: String? = null): ResponseMessage<ResponseGenreMusiks?>

    // Semua input teks diubah menjadi String
    suspend fun postGenreMusik(
        nama: String,
        deskripsi: String,
        contohArtis: String,
        asalUsul: String,
        file: MultipartBody.Part
    ): ResponseMessage<ResponseGenreMusikAdd?>

    suspend fun getGenreMusikById(genreMusikId: String): ResponseMessage<ResponseGenreMusik?>

    // Semua input teks diubah menjadi String
    suspend fun putGenreMusik(
        genreMusikId: String,
        nama: String,
        deskripsi: String,
        contohArtis: String,
        asalUsul: String,
        file: MultipartBody.Part? = null
    ): ResponseMessage<String?>

    suspend fun deleteGenreMusik(genreMusikId: String): ResponseMessage<String?>
}