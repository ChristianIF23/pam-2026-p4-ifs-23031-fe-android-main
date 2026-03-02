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
import okhttp3.RequestBody
import org.delcom.pam_p4_ifs23031.network.plants.data.ResponsePlantData
import org.delcom.pam_p4_ifs23031.network.plants.data.ResponseProfile
import org.delcom.pam_p4_ifs23031.network.plants.service.IPlantRepository
import javax.inject.Inject

sealed interface PlantProfileUIState {
    data class Success(val data: ResponseProfile) : PlantProfileUIState
    data class Error(val message: String) : PlantProfileUIState
    object Loading : PlantProfileUIState
}

// Don't forget to update your data class to use the new name:
data class UIStatePlant(
    val profile: PlantProfileUIState = PlantProfileUIState.Loading,
    val plants: PlantsUIState = PlantsUIState.Loading,
    var plant: PlantUIState = PlantUIState.Loading,
    var plantAction: PlantActionUIState = PlantActionUIState.Loading
)

sealed interface PlantsUIState {
    data class Success(val data: List<ResponsePlantData>) : PlantsUIState
    data class Error(val message: String) : PlantsUIState
    object Loading : PlantsUIState
}

sealed interface PlantUIState {
    data class Success(val data: ResponsePlantData) : PlantUIState
    data class Error(val message: String) : PlantUIState
    object Loading : PlantUIState
}

sealed interface PlantActionUIState {
    data class Success(val message: String) : PlantActionUIState
    data class Error(val message: String) : PlantActionUIState
    object Loading : PlantActionUIState
}

@HiltViewModel
@Keep
class PlantViewModel @Inject constructor(
    private val repository: IPlantRepository,
) : ViewModel() {
    private val _uiState = MutableStateFlow(UIStatePlant())
    val uiState = _uiState.asStateFlow()

    fun getProfile() {
        _uiState.update { it.copy(profile = PlantProfileUIState.Loading) }
        viewModelScope.launch {
            val result = repository.getProfile()
            val newState = if (result.status == "success") {
                PlantProfileUIState.Success(result.data!!)
            } else {
                PlantProfileUIState.Error(result.message)
            }
            _uiState.update { it.copy(profile = newState) }
        }
    }

    fun getAllPlants(search: String? = null) {
        _uiState.update { it.copy(plants = PlantsUIState.Loading) }
        viewModelScope.launch {
            val result = repository.getAllPlants(search)
            val newState = if (result.status == "success") {
                PlantsUIState.Success(result.data!!.plants)
            } else {
                PlantsUIState.Error(result.message)
            }
            _uiState.update { it.copy(plants = newState) }
        }
    }

    fun postPlant(
        nama: RequestBody,
        deskripsi: RequestBody,
        manfaat: RequestBody,
        efekSamping: RequestBody,
        file: MultipartBody.Part
    ) {
        _uiState.update { it.copy(plantAction = PlantActionUIState.Loading) }
        viewModelScope.launch {
            val result = repository.postPlant(
                nama = nama,
                deskripsi = deskripsi,
                manfaat = manfaat,
                efekSamping = efekSamping,
                file = file
            )
            val newState = if (result.status == "success") {
                PlantActionUIState.Success(result.data!!.plantId)
            } else {
                PlantActionUIState.Error(result.message)
            }
            _uiState.update { it.copy(plantAction = newState) }
        }
    }

    fun getPlantById(plantId: String) {
        _uiState.update { it.copy(plant = PlantUIState.Loading) }
        viewModelScope.launch {
            val result = repository.getPlantById(plantId)
            val newState = if (result.status == "success") {
                PlantUIState.Success(result.data!!.plant)
            } else {
                PlantUIState.Error(result.message)
            }
            _uiState.update { it.copy(plant = newState) }
        }
    }

    fun putPlant(
        plantId: String,
        nama: RequestBody,
        deskripsi: RequestBody,
        manfaat: RequestBody,
        efekSamping: RequestBody,
        file: MultipartBody.Part?
    ) {
        _uiState.update { it.copy(plantAction = PlantActionUIState.Loading) }
        viewModelScope.launch {
            val result = repository.putPlant(
                plantId = plantId,
                nama = nama,
                deskripsi = deskripsi,
                manfaat = manfaat,
                efekSamping = efekSamping,
                file = file
            )
            val newState = if (result.status == "success") {
                PlantActionUIState.Success(result.message)
            } else {
                PlantActionUIState.Error(result.message)
            }
            _uiState.update { it.copy(plantAction = newState) }
        }
    }

    fun deletePlant(plantId: String) {
        _uiState.update { it.copy(plantAction = PlantActionUIState.Loading) }
        viewModelScope.launch {
            val result = repository.deletePlant(
                plantId = plantId
            )
            val newState = if (result.status == "success") {
                PlantActionUIState.Success(result.message)
            } else {
                PlantActionUIState.Error(result.message)
            }
            _uiState.update { it.copy(plantAction = newState) }
        }
    }
}