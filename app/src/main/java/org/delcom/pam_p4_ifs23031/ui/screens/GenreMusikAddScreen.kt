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
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import org.delcom.pam_p4_ifs23031.R
import org.delcom.pam_p4_ifs23031.helper.AlertHelper
import org.delcom.pam_p4_ifs23031.helper.AlertState
import org.delcom.pam_p4_ifs23031.helper.AlertType
import org.delcom.pam_p4_ifs23031.helper.ConstHelper
import org.delcom.pam_p4_ifs23031.helper.RouteHelper
import org.delcom.pam_p4_ifs23031.helper.SuspendHelper
import org.delcom.pam_p4_ifs23031.helper.SuspendHelper.SnackBarType
import org.delcom.pam_p4_ifs23031.helper.ToolsHelper.uriToMultipart
import org.delcom.pam_p4_ifs23031.network.games.data.ResponseGenreMusikData
import org.delcom.pam_p4_ifs23031.ui.components.BottomNavComponent
import org.delcom.pam_p4_ifs23031.ui.components.LoadingUI
import org.delcom.pam_p4_ifs23031.ui.components.TopAppBarComponent
import org.delcom.pam_p4_ifs23031.ui.theme.DelcomTheme
import org.delcom.pam_p4_ifs23031.ui.viewmodels.GenreMusikActionUIState
import org.delcom.pam_p4_ifs23031.ui.viewmodels.GenreMusikViewModel

@Composable
fun GenreMusikAddScreen(
    navController: NavHostController,
    snackbarHost: SnackbarHostState,
    genreMusikViewModel: GenreMusikViewModel
) {
    val uiStateGenreMusik by genreMusikViewModel.uiState.collectAsState()

    var isLoading by remember { mutableStateOf(false) }
    var tmpGenreMusik by remember { mutableStateOf<ResponseGenreMusikData?>(null) }

    // Reset action state when entering screen
    LaunchedEffect(Unit) {
        // We don't want to set it to Loading immediately, but to something neutral
        // or just ensure it's not a stale Success state.
        // If your ViewModel doesn't have a 'Neutral' or 'Idle' state, we just handle it in the collector.
    }

    fun onSave(
        context: Context,
        nama: String,
        deskripsi: String,
        contohArtis: String,
        asalUsul: String,
        file: Uri
    ) {
        isLoading = true

        val filePart = uriToMultipart(context, file, "file")

        genreMusikViewModel.postGenreMusik(
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
                isLoading = false
                SuspendHelper.showSnackBar(
                    snackbarHost = snackbarHost,
                    type = SnackBarType.SUCCESS,
                    message = "Berhasil menambahkan genre musik"
                )
                // Pindah ke halaman daftar dan refresh data
                navController.navigate(ConstHelper.RouteNames.GenreMusik.path) {
                    popUpTo(ConstHelper.RouteNames.GenreMusik.path) { inclusive = true }
                }
            }
            is GenreMusikActionUIState.Error -> {
                isLoading = false
                SuspendHelper.showSnackBar(
                    snackbarHost = snackbarHost,
                    type = SnackBarType.ERROR,
                    message = state.message
                )
            }
            else -> {}
        }
    }

    if (isLoading) {
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
            title = "Tambah Data",
            showBackButton = true,
        )
        Box(
            modifier = Modifier
                .weight(1f)
        ) {
            GenreMusikAddUI(
                tmpGenreMusik = tmpGenreMusik,
                onSave = ::onSave
            )
        }
        BottomNavComponent(navController = navController)
    }
}

@Composable
fun GenreMusikAddUI(
    tmpGenreMusik: ResponseGenreMusikData?,
    onSave: (
        Context,
        String,
        String,
        String,
        String,
        Uri
    ) -> Unit
) {
    val alertState = remember { mutableStateOf(AlertState()) }

    var dataFile by remember { mutableStateOf<Uri?>(null) }
    var dataNama by remember { mutableStateOf(tmpGenreMusik?.nama ?: "") }
    var dataDeskripsi by remember { mutableStateOf(tmpGenreMusik?.deskripsi ?: "") }
    var dataContohArtis by remember { mutableStateOf(tmpGenreMusik?.contohArtis ?: "") }
    var dataAsalUsul by remember { mutableStateOf(tmpGenreMusik?.asalUsul ?: "") }
    val context = LocalContext.current

    val focusManager = LocalFocusManager.current

    val deskripsiFocus = remember { FocusRequester() }
    val contohArtisFocus = remember { FocusRequester() }
    val asalUsulFocus = remember { FocusRequester() }

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
                        contentDescription = "Pratinjau Gambar",
                        placeholder = painterResource(R.drawable.img_placeholder),
                        error = painterResource(R.drawable.img_placeholder),
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxSize()
                    )
                } else {
                    Text(
                        text = "Pilih Gambar",
                        color = MaterialTheme.colorScheme.onPrimaryContainer
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
                imeAction = ImeAction.Next
            ),
            keyboardActions = KeyboardActions(
                onNext = { deskripsiFocus.requestFocus() }
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
                .height(120.dp)
                .focusRequester(deskripsiFocus),
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Text,
                imeAction = ImeAction.Next
            ),
            keyboardActions = KeyboardActions(
                onNext = { contohArtisFocus.requestFocus() }
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
                .height(120.dp)
                .focusRequester(contohArtisFocus),
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Text,
                imeAction = ImeAction.Next
            ),
            keyboardActions = KeyboardActions(
                onNext = { asalUsulFocus.requestFocus() }
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
                .height(120.dp)
                .focusRequester(asalUsulFocus),
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
                if (dataFile != null) {

                    if(dataNama.isEmpty()) {
                        AlertHelper.show(
                            alertState,
                            AlertType.ERROR,
                            "Nama tidak boleh kosong!"
                        )
                        return@FloatingActionButton
                    }

                    if(dataDeskripsi.isEmpty()) {
                        AlertHelper.show(
                            alertState,
                            AlertType.ERROR,
                            "Deskripsi tidak boleh kosong!"
                        )
                        return@FloatingActionButton
                    }

                    if(dataContohArtis.isEmpty()) {
                        AlertHelper.show(
                            alertState,
                            AlertType.ERROR,
                            "Contoh Artis tidak boleh kosong!"
                        )
                        return@FloatingActionButton
                    }

                    if(dataAsalUsul.isEmpty()) {
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
                        dataFile!!
                    )
                } else {
                    AlertHelper.show(
                        alertState,
                        AlertType.ERROR,
                        "Gambar tidak boleh kosong!"
                    )
                    return@FloatingActionButton
                }

            },
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(16.dp)
            ,
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

@Preview(showBackground = true, name = "Light Mode")
@Composable
fun PreviewGenreMusikAddUI() {
    DelcomTheme {
        GenreMusikAddUI(
            tmpGenreMusik = null,
            onSave = { _, _, _, _, _, _ -> }
        )
    }
}
