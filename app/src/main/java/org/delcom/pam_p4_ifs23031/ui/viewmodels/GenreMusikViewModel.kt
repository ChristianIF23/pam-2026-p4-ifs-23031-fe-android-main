package org.delcom.pam_p4_ifs23031.ui.viewmodels

import androidx.annotation.Keep
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import okhttp3.MultipartBody
import org.delcom.pam_p4_ifs23031.network.games.data.ResponseGenreMusikData
import org.delcom.pam_p4_ifs23031.network.games.service.IGenreMusikRepository
import javax.inject.Inject

data class UIStateGenreMusik(
    val genreMusiks: GenreMusiksUIState = GenreMusiksUIState.Loading,
    var genreMusik: GenreMusikUIState = GenreMusikUIState.Loading,
    var genreMusikAction: GenreMusikActionUIState = GenreMusikActionUIState.Loading
)

sealed interface GenreMusiksUIState {
    data class Success(val data: List<ResponseGenreMusikData>) : GenreMusiksUIState
    data class Error(val message: String) : GenreMusiksUIState
    object Loading : GenreMusiksUIState
}

sealed interface GenreMusikUIState {
    data class Success(val data: ResponseGenreMusikData) : GenreMusikUIState
    data class Error(val message: String) : GenreMusikUIState
    object Loading : GenreMusikUIState
}

sealed interface GenreMusikActionUIState {
    data class Success(val message: String) : GenreMusikActionUIState
    data class Error(val message: String) : GenreMusikActionUIState
    object Loading : GenreMusikActionUIState
}

@HiltViewModel
@Keep
class GenreMusikViewModel @Inject constructor(
    private val repository: IGenreMusikRepository,
) : ViewModel() {
    private val _uiState = MutableStateFlow(UIStateGenreMusik())
    val uiState = _uiState.asStateFlow()

    fun getAllGenreMusiks(search: String? = null) {
        _uiState.update { it.copy(genreMusiks = GenreMusiksUIState.Loading) }
        viewModelScope.launch {
            val result = repository.getAllGenreMusiks(search)
            val newState = if (result.status == "success") {
                GenreMusiksUIState.Success(result.data!!.genreMusiks)
            } else {
                GenreMusiksUIState.Error(result.message)
            }
            _uiState.update { it.copy(genreMusiks = newState) }
        }
    }

    fun postGenreMusik(
        nama: String,
        deskripsi: String,
        contohArtis: String,
        asalUsul: String,
        file: MultipartBody.Part
    ) {
        _uiState.update { it.copy(genreMusikAction = GenreMusikActionUIState.Loading) }
        viewModelScope.launch {
            val result = repository.postGenreMusik(
                nama = nama,
                deskripsi = deskripsi,
                contohArtis = contohArtis,
                asalUsul = asalUsul,
                file = file
            )
            val newState = if (result.status == "success") {
                GenreMusikActionUIState.Success(result.data!!.genreMusikId)
            } else {
                GenreMusikActionUIState.Error(result.message)
            }
            _uiState.update { it.copy(genreMusikAction = newState) }
        }
    }

    fun getGenreMusikById(genreMusikId: String) {
        _uiState.update { it.copy(genreMusik = GenreMusikUIState.Loading) }
        viewModelScope.launch {
            val result = repository.getGenreMusikById(genreMusikId)
            val newState = if (result.status == "success") {
                GenreMusikUIState.Success(result.data!!.genreMusik)
            } else {
                GenreMusikUIState.Error(result.message)
            }
            _uiState.update { it.copy(genreMusik = newState) }
        }
    }

    fun putGenreMusik(
        genreMusikId: String,
        nama: String,
        deskripsi: String,
        contohArtis: String,
        asalUsul: String,
        file: MultipartBody.Part?
    ) {
        _uiState.update { it.copy(genreMusikAction = GenreMusikActionUIState.Loading) }
        viewModelScope.launch {
            val result = repository.putGenreMusik(
                genreMusikId = genreMusikId,
                nama = nama,
                deskripsi = deskripsi,
                contohArtis = contohArtis,
                asalUsul = asalUsul,
                file = file
            )
            val newState = if (result.status == "success") {
                GenreMusikActionUIState.Success(result.message)
            } else {
                GenreMusikActionUIState.Error(result.message)
            }
            _uiState.update { it.copy(genreMusikAction = newState) }
        }
    }

    fun deleteGenreMusik(genreMusikId: String) {
        _uiState.update { it.copy(genreMusikAction = GenreMusikActionUIState.Loading) }
        viewModelScope.launch {
            val result = repository.deleteGenreMusik(genreMusikId)
            val newState = if (result.status == "success") {
                GenreMusikActionUIState.Success(result.message)
            } else {
                GenreMusikActionUIState.Error(result.message)
            }
            _uiState.update { it.copy(genreMusikAction = newState) }
        }
    }
}