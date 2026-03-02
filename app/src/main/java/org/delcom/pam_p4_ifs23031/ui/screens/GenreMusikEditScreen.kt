package org.delcom.pam_p4_ifs23031.ui.screens

import android.content.Context
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Save
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import okhttp3.MultipartBody
import org.delcom.pam_p4_ifs23031.R
import org.delcom.pam_p4_ifs23031.helper.AlertHelper
import org.delcom.pam_p4_ifs23031.helper.AlertState
import org.delcom.pam_p4_ifs23031.helper.AlertType
import org.delcom.pam_p4_ifs23031.helper.ConstHelper
import org.delcom.pam_p4_ifs23031.helper.RouteHelper
import org.delcom.pam_p4_ifs23031.helper.SuspendHelper
import org.delcom.pam_p4_ifs23031.helper.SuspendHelper.SnackBarType
import org.delcom.pam_p4_ifs23031.helper.ToolsHelper
import org.delcom.pam_p4_ifs23031.helper.ToolsHelper.uriToMultipart
import org.delcom.pam_p4_ifs23031.network.games.data.ResponseGenreMusikData
import org.delcom.pam_p4_ifs23031.ui.components.BottomNavComponent
import org.delcom.pam_p4_ifs23031.ui.components.LoadingUI
import org.delcom.pam_p4_ifs23031.ui.components.TopAppBarComponent
import org.delcom.pam_p4_ifs23031.ui.viewmodels.GenreMusikActionUIState
import org.delcom.pam_p4_ifs23031.ui.viewmodels.GenreMusikUIState
import org.delcom.pam_p4_ifs23031.ui.viewmodels.GenreMusikViewModel

@Composable
fun GenreMusikEditScreen(
    navController: NavHostController,
    snackbarHost: SnackbarHostState,
    genreMusikViewModel: GenreMusikViewModel,
    genreMusikId: String
) {
    val uiStateGenreMusik by genreMusikViewModel.uiState.collectAsState()
    var isLoading by remember { mutableStateOf(false) }
    var genreMusik by remember { mutableStateOf<ResponseGenreMusikData?>(null) }

    LaunchedEffect(Unit) {
        isLoading = true
        uiStateGenreMusik.genreMusikAction = GenreMusikActionUIState.Loading
        uiStateGenreMusik.genreMusik = GenreMusikUIState.Loading
        genreMusikViewModel.getGenreMusikById(genreMusikId)
    }

    LaunchedEffect(uiStateGenreMusik.genreMusik) {
        if (uiStateGenreMusik.genreMusik !is GenreMusikUIState.Loading) {
            if (uiStateGenreMusik.genreMusik is GenreMusikUIState.Success) {
                genreMusik = (uiStateGenreMusik.genreMusik as GenreMusikUIState.Success).data
                isLoading = false
            } else {
                RouteHelper.back(navController)
                isLoading = false
            }
        }
    }

    fun onSave(
        context: Context,
        nama: String,
        deskripsi: String,
        contohArtis: String,
        asalUsul: String,
        file: Uri? = null
    ) {
        isLoading = true

        var filePart: MultipartBody.Part? = null
        if (file != null && file.scheme == "content") {
            filePart = uriToMultipart(context, file, "file")
        }

        genreMusikViewModel.putGenreMusik(
            genreMusikId = genreMusikId,
            nama = nama,
            deskripsi = deskripsi,
            contohArtis = contohArtis,
            asalUsul = asalUsul,
            file = filePart,
        )
    }

    LaunchedEffect(uiStateGenreMusik.genreMusikAction) {
        when (val state = uiStateGenreMusik.genreMusikAction) {
            is GenreMusikActionUIState.Success -> {
                SuspendHelper.showSnackBar(
                    snackbarHost = snackbarHost,
                    type = SnackBarType.SUCCESS,
                    message = state.message
                )
                RouteHelper.to(
                    navController = navController,
                    destination = ConstHelper.RouteNames.GenreMusikDetail.path
                        .replace("{genreMusikId}", genreMusikId),
                    popUpTo = ConstHelper.RouteNames.GenreMusikDetail.path
                        .replace("{genreMusikId}", genreMusikId),
                    removeBackStack = true
                )
                isLoading = false
            }

            is GenreMusikActionUIState.Error -> {
                SuspendHelper.showSnackBar(
                    snackbarHost = snackbarHost,
                    type = SnackBarType.ERROR,
                    message = state.message
                )
                isLoading = false
            }

            else -> {}
        }
    }

    if (isLoading || genreMusik == null) {
        LoadingUI()
        return
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.background)
    ) {
        TopAppBarComponent(
            navController = navController,
            title = "Ubah Data",
            showBackButton = true,
        )
        Box(
            modifier = Modifier
                .weight(1f)
        ) {
            GenreMusikEditUI(
                genreMusik = genreMusik!!,
                onSave = ::onSave
            )
        }
        BottomNavComponent(navController = navController)
    }
}

@Composable
fun GenreMusikEditUI(
    genreMusik: ResponseGenreMusikData,
    onSave: (
        Context,
        String,
        String,
        String,
        String,
        Uri?
    ) -> Unit
) {
    val alertState = remember { mutableStateOf(AlertState()) }

    var dataFile by remember { mutableStateOf<Uri?>(null) }
    var dataNama by remember { mutableStateOf(genreMusik.nama) }
    var dataDeskripsi by remember { mutableStateOf(genreMusik.deskripsi) }
    var dataContohArtis by remember { mutableStateOf(genreMusik.contohArtis) }
    var dataAsalUsul by remember { mutableStateOf(genreMusik.asalUsul) }
    val context = LocalContext.current

    val focusManager = LocalFocusManager.current

    val imagePicker = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia()
    ) { uri: Uri? ->
        dataFile = uri
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxWidth()
        ) {
            Box(
                modifier = Modifier
                    .size(150.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(MaterialTheme.colorScheme.primaryContainer)
                    .clickable {
                        imagePicker.launch(
                            PickVisualMediaRequest(
                                ActivityResultContracts.PickVisualMedia.ImageOnly
                            )
                        )
                    },
                contentAlignment = Alignment.Center
            ) {
                if (dataFile != null) {
                    AsyncImage(
                        model = dataFile,
                        placeholder = painterResource(R.drawable.img_placeholder),
                        error = painterResource(R.drawable.img_placeholder),
                        contentDescription = "Pratinjau Gambar",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxSize()
                    )
                } else {
                    AsyncImage(
                        model = ToolsHelper.getGenreMusikImageUrl(genreMusik.pathGambar),
                        placeholder = painterResource(R.drawable.img_placeholder),
                        error = painterResource(R.drawable.img_placeholder),
                        contentDescription = "Pratinjau Gambar",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxSize()
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Tap untuk mengganti gambar",
                style = MaterialTheme.typography.bodySmall
            )
        }

        OutlinedTextField(
            value = dataNama,
            onValueChange = { dataNama = it },
            colors = OutlinedTextFieldDefaults.colors(
                focusedTextColor = MaterialTheme.colorScheme.onPrimaryContainer,
                unfocusedTextColor = MaterialTheme.colorScheme.onPrimaryContainer,
                focusedBorderColor = MaterialTheme.colorScheme.primaryContainer,
                cursorColor = MaterialTheme.colorScheme.primaryContainer,
                unfocusedBorderColor = MaterialTheme.colorScheme.onPrimaryContainer,
            ),
            label = {
                Text(
                    text = "Nama",
                    color = MaterialTheme.colorScheme.onPrimaryContainer
                )
            },
            modifier = Modifier
                .fillMaxWidth(),
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Text,
                imeAction = ImeAction.Done
            ),
            keyboardActions = KeyboardActions(
                onDone = { focusManager.clearFocus() }
            ),
        )

        OutlinedTextField(
            value = dataDeskripsi,
            onValueChange = { dataDeskripsi = it },
            colors = OutlinedTextFieldDefaults.colors(
                focusedTextColor = MaterialTheme.colorScheme.onPrimaryContainer,
                unfocusedTextColor = MaterialTheme.colorScheme.onPrimaryContainer,
                focusedBorderColor = MaterialTheme.colorScheme.primaryContainer,
                cursorColor = MaterialTheme.colorScheme.primaryContainer,
                unfocusedBorderColor = MaterialTheme.colorScheme.onPrimaryContainer,
            ),
            label = {
                Text(
                    text = "Deskripsi",
                    color = MaterialTheme.colorScheme.onPrimaryContainer
                )
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(120.dp),
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Text,
                imeAction = ImeAction.Done
            ),
            keyboardActions = KeyboardActions(
                onDone = { focusManager.clearFocus() }
            ),
            maxLines = 5,
            minLines = 3
        )

        OutlinedTextField(
            value = dataContohArtis,
            onValueChange = { dataContohArtis = it },
            colors = OutlinedTextFieldDefaults.colors(
                focusedTextColor = MaterialTheme.colorScheme.onPrimaryContainer,
                unfocusedTextColor = MaterialTheme.colorScheme.onPrimaryContainer,
                focusedBorderColor = MaterialTheme.colorScheme.primaryContainer,
                cursorColor = MaterialTheme.colorScheme.primaryContainer,
                unfocusedBorderColor = MaterialTheme.colorScheme.onPrimaryContainer,
            ),
            label = {
                Text(
                    text = "Contoh Artis",
                    color = MaterialTheme.colorScheme.onPrimaryContainer
                )
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(120.dp),
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Text,
                imeAction = ImeAction.Done
            ),
            keyboardActions = KeyboardActions(
                onDone = { focusManager.clearFocus() }
            ),
            maxLines = 5,
            minLines = 3
        )

        OutlinedTextField(
            value = dataAsalUsul,
            onValueChange = { dataAsalUsul = it },
            colors = OutlinedTextFieldDefaults.colors(
                focusedTextColor = MaterialTheme.colorScheme.onPrimaryContainer,
                unfocusedTextColor = MaterialTheme.colorScheme.onPrimaryContainer,
                focusedBorderColor = MaterialTheme.colorScheme.primaryContainer,
                cursorColor = MaterialTheme.colorScheme.primaryContainer,
                unfocusedBorderColor = MaterialTheme.colorScheme.onPrimaryContainer,
            ),
            label = {
                Text(
                    text = "Asal Usul",
                    color = MaterialTheme.colorScheme.onPrimaryContainer
                )
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(120.dp),
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Text,
                imeAction = ImeAction.Done
            ),
            keyboardActions = KeyboardActions(
                onDone = {
                    focusManager.clearFocus()
                }
            ),
            maxLines = 5,
            minLines = 3
        )

        Spacer(modifier = Modifier.height(64.dp))
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
    ) {
        FloatingActionButton(
            onClick = {
                if (dataNama.isEmpty()) {
                    AlertHelper.show(
                        alertState,
                        AlertType.ERROR,
                        "Nama tidak boleh kosong!"
                    )
                    return@FloatingActionButton
                }

                if (dataDeskripsi.isEmpty()) {
                    AlertHelper.show(
                        alertState,
                        AlertType.ERROR,
                        "Deskripsi tidak boleh kosong!"
                    )
                    return@FloatingActionButton
                }

                if (dataContohArtis.isEmpty()) {
                    AlertHelper.show(
                        alertState,
                        AlertType.ERROR,
                        "Contoh Artis tidak boleh kosong!"
                    )
                    return@FloatingActionButton
                }

                if (dataAsalUsul.isEmpty()) {
                    AlertHelper.show(
                        alertState,
                        AlertType.ERROR,
                        "Asal Usul tidak boleh kosong!"
                    )
                    return@FloatingActionButton
                }

                onSave(
                    context,
                    dataNama,
                    dataDeskripsi,
                    dataContohArtis,
                    dataAsalUsul,
                    dataFile
                )
            },
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(16.dp),
            containerColor = MaterialTheme.colorScheme.primary,
            contentColor = MaterialTheme.colorScheme.onPrimary
        ) {
            Icon(
                imageVector = Icons.Default.Save,
                contentDescription = "Simpan Data"
            )
        }
    }

    if (alertState.value.isVisible) {
        AlertDialog(
            onDismissRequest = {
                AlertHelper.dismiss(alertState)
            },
            title = {
                Text(alertState.value.type.title)
            },
            text = {
                Text(alertState.value.message)
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        AlertHelper.dismiss(alertState)
                    }
                ) {
                    Text("OK")
                }
            }
        )
    }
}
