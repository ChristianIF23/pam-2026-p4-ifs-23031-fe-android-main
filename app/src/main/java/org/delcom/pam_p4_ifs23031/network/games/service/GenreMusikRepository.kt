package org.delcom.pam_p4_ifs23031.network.games.service

import okhttp3.MultipartBody
import org.delcom.pam_p4_ifs23031.helper.SuspendHelper
import org.delcom.pam_p4_ifs23031.helper.ToolsHelper.toRequestBodyText
import org.delcom.pam_p4_ifs23031.network.data.ResponseMessage
import org.delcom.pam_p4_ifs23031.network.games.data.ResponseGenreMusik
import org.delcom.pam_p4_ifs23031.network.games.data.ResponseGenreMusikAdd
import org.delcom.pam_p4_ifs23031.network.games.data.ResponseGenreMusiks

class GenreMusikRepository(private val genreMusikApiService: GenreMusikApiService) : IGenreMusikRepository {

    override suspend fun getAllGenreMusiks(search: String?): ResponseMessage<ResponseGenreMusiks?> {
        return SuspendHelper.safeApiCall { genreMusikApiService.getAllGenreMusiks(search) }
    }

    override suspend fun postGenreMusik(
        nama: String,
        deskripsi: String,
        contohArtis: String,
        asalUsul: String,
        file: MultipartBody.Part
    ): ResponseMessage<ResponseGenreMusikAdd?> {
        return SuspendHelper.safeApiCall {
            genreMusikApiService.postGenreMusik(
                nama = nama.toRequestBodyText(),
                deskripsi = deskripsi.toRequestBodyText(),
                contohArtis = contohArtis.toRequestBodyText(),
                asalUsul = asalUsul.toRequestBodyText(),
                file = file
            )
        }
    }

    override suspend fun getGenreMusikById(genreMusikId: String): ResponseMessage<ResponseGenreMusik?> {
        return SuspendHelper.safeApiCall { genreMusikApiService.getGenreMusikById(genreMusikId) }
    }

    override suspend fun putGenreMusik(
        genreMusikId: String,
        nama: String,
        deskripsi: String,
        contohArtis: String,
        asalUsul: String,
        file: MultipartBody.Part?
    ): ResponseMessage<String?> {
        return SuspendHelper.safeApiCall {
            genreMusikApiService.putGenreMusik(
                genreMusikId = genreMusikId,
                nama = nama.toRequestBodyText(),
                deskripsi = deskripsi.toRequestBodyText(),
                contohArtis = contohArtis.toRequestBodyText(),
                asalUsul = asalUsul.toRequestBodyText(),
                file = file
            )
        }
    }

    override suspend fun deleteGenreMusik(genreMusikId: String): ResponseMessage<String?> {
        return SuspendHelper.safeApiCall { genreMusikApiService.deleteGenreMusik(genreMusikId) }
    }
}
