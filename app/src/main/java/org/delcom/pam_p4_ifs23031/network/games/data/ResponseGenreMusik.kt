package org.delcom.pam_p4_ifs23031.network.games.data

import kotlinx.serialization.Serializable
import java.util.UUID

@Serializable
data class ResponseGenreMusiks (
    val genreMusiks: List<ResponseGenreMusikData>
)

@Serializable
data class ResponseGenreMusik (
    val genreMusik: ResponseGenreMusikData
)

@Serializable
data class ResponseGenreMusikAdd (
    val genreMusikId: String
)

@Serializable
data class ResponseGenreMusikData(
    var id : String = UUID.randomUUID().toString(),
    var nama: String,
    var pathGambar: String,
    var deskripsi: String,
    var contohArtis: String,
    var asalUsul: String,
)