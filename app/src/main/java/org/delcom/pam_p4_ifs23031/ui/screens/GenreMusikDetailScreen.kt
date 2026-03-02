package org.delcom.pam_p4_ifs23031.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import org.delcom.pam_p4_ifs23031.R
import org.delcom.pam_p4_ifs23031.helper.ConstHelper
import org.delcom.pam_p4_ifs23031.helper.RouteHelper
import org.delcom.pam_p4_ifs23031.helper.SuspendHelper
import org.delcom.pam_p4_ifs23031.helper.SuspendHelper.SnackBarType
import org.delcom.pam_p4_ifs23031.helper.ToolsHelper
import org.delcom.pam_p4_ifs23031.network.games.data.ResponseGenreMusikData
import org.delcom.pam_p4_ifs23031.ui.components.BottomDialog
import org.delcom.pam_p4_ifs23031.ui.components.BottomDialogType
import org.delcom.pam_p4_ifs23031.ui.components.BottomNavComponent
import org.delcom.pam_p4_ifs23031.ui.components.LoadingUI
import org.delcom.pam_p4_ifs23031.ui.components.TopAppBarComponent
import org.delcom.pam_p4_ifs23031.ui.components.TopAppBarMenuItem
import org.delcom.pam_p4_ifs23031.ui.theme.DelcomTheme
import org.delcom.pam_p4_ifs23031.ui.viewmodels.GenreMusikActionUIState
import org.delcom.pam_p4_ifs23031.ui.viewmodels.GenreMusikUIState
import org.delcom.pam_p4_ifs23031.ui.viewmodels.GenreMusikViewModel

@Composable
fun GenreMusikDetailScreen(
    navController: NavHostController,
    snackbarHost: SnackbarHostState,
    genreMusikViewModel: GenreMusikViewModel,
    genreMusikId: String
) {
    val uiStateGenreMusik by genreMusikViewModel.uiState.collectAsState()
    var isLoading by remember { mutableStateOf(false) }
    var isConfirmDelete by remember { mutableStateOf(false) }

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
            }
        }
    }

    fun onDelete() {
        isLoading = true
        genreMusikViewModel.deleteGenreMusik(genreMusikId)
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
                    navController,
                    ConstHelper.RouteNames.GenreMusik.path,
                    true
                )
                uiStateGenreMusik.genreMusik = GenreMusikUIState.Loading
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

    val detailMenuItems = listOf(
        TopAppBarMenuItem(
            text = "Ubah Data",
            icon = Icons.Filled.Edit,
            route = null,
            onClick = {
                RouteHelper.to(
                    navController,
                    ConstHelper.RouteNames.GenreMusikEdit.path
                        .replace("{genreMusikId}", genreMusik!!.id),
                )
            }
        ),
        TopAppBarMenuItem(
            text = "Hapus Data",
            icon = Icons.Filled.Delete,
            route = null,
            onClick = {
                isConfirmDelete = true
            }
        ),
    )

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.background)
    )
    {
        TopAppBarComponent(
            navController = navController,
            title = genreMusik!!.nama,
            showBackButton = true,
            customMenuItems = detailMenuItems
        )
        Box(
            modifier = Modifier
                .weight(1f)
        ) {
            GenreMusikDetailUI(
                genreMusik = genreMusik!!
            )
            BottomDialog(
                type = BottomDialogType.ERROR,
                show = isConfirmDelete,
                onDismiss = { isConfirmDelete = false },
                title = "Konfirmasi Hapus Data",
                message = "Apakah Anda yakin ingin menghapus data ini?",
                confirmText = "Ya, Hapus",
                onConfirm = {
                    onDelete()
                },
                cancelText = "Batal",
                destructiveAction = true
            )
        }
        BottomNavComponent(navController = navController)
    }
}

@Composable
fun GenreMusikDetailUI(
    genreMusik: ResponseGenreMusikData
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState())
    )
    {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(vertical = 16.dp)
        )
        {
            AsyncImage(
                model = ToolsHelper.getGenreMusikImageUrl(genreMusik.pathGambar),
                contentDescription = genreMusik.nama,
                placeholder = painterResource(R.drawable.img_placeholder),
                error = painterResource(R.drawable.img_placeholder),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(250.dp),
                contentScale = ContentScale.Fit
            )

            Text(
                text = genreMusik.nama,
                style = MaterialTheme.typography.headlineLarge,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )
        }

        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 24.dp),
            shape = MaterialTheme.shapes.medium,
            elevation = CardDefaults.cardElevation(4.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        )
        {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Text(
                    text = "Deskripsi",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.SemiBold
                )
                HorizontalDivider(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 4.dp)
                )
                Text(
                    text = genreMusik.deskripsi,
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.padding(top = 4.dp)
                )
            }

        }

        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 24.dp),
            shape = MaterialTheme.shapes.medium,
            elevation = CardDefaults.cardElevation(4.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        )
        {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Text(
                    text = "Contoh Artis",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.SemiBold
                )
                HorizontalDivider(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 4.dp)
                )
                Text(
                    text = genreMusik.contohArtis,
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.padding(top = 4.dp)
                )
            }

        }

        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 24.dp),
            shape = MaterialTheme.shapes.medium,
            elevation = CardDefaults.cardElevation(4.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        )
        {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Text(
                    text = "Asal Usul",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.SemiBold
                )
                HorizontalDivider(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 4.dp)
                )
                Text(
                    text = genreMusik.asalUsul,
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.padding(top = 4.dp)
                )
            }

        }
    }
}

@Preview(showBackground = true, name = "Light Mode")
@Composable
fun PreviewGenreMusikDetailUI() {
    DelcomTheme {
//        GenreMusikDetailUI(
//            genreMusik = DummyData.getGenreMusiksData()[0]
//        )
    }
}
