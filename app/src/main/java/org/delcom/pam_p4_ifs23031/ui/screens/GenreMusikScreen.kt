package org.delcom.pam_p4_ifs23031.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import org.delcom.pam_p4_ifs23031.R
import org.delcom.pam_p4_ifs23031.helper.ConstHelper
import org.delcom.pam_p4_ifs23031.helper.RouteHelper
import org.delcom.pam_p4_ifs23031.helper.ToolsHelper
import org.delcom.pam_p4_ifs23031.network.games.data.ResponseGenreMusikData
import org.delcom.pam_p4_ifs23031.ui.components.BottomNavComponent
import org.delcom.pam_p4_ifs23031.ui.components.LoadingUI
import org.delcom.pam_p4_ifs23031.ui.components.TopAppBarComponent
import org.delcom.pam_p4_ifs23031.ui.viewmodels.GenreMusikViewModel
import org.delcom.pam_p4_ifs23031.ui.viewmodels.GenreMusiksUIState

@Composable
fun GenreMusikScreen(
    navController: NavHostController,
    genreMusikViewModel: GenreMusikViewModel
) {
    val uiStateGenreMusik by genreMusikViewModel.uiState.collectAsState()
    val lifecycleOwner = LocalLifecycleOwner.current

    var isLoading by remember { mutableStateOf(false) }
    var searchQuery by remember {
        mutableStateOf(TextFieldValue(""))
    }

    var genreMusiks by remember { mutableStateOf<List<ResponseGenreMusikData>>(emptyList()) }

    fun fetchGenreMusiksData(){
        isLoading = true
        genreMusikViewModel.getAllGenreMusiks(searchQuery.text)
    }

    LaunchedEffect(lifecycleOwner) {
        lifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
            fetchGenreMusiksData()
        }
    }

    LaunchedEffect(uiStateGenreMusik.genreMusiks){
        if(uiStateGenreMusik.genreMusiks !is GenreMusiksUIState.Loading){
            isLoading = false

            genreMusiks = if(uiStateGenreMusik.genreMusiks is GenreMusiksUIState.Success) {
                (uiStateGenreMusik.genreMusiks as GenreMusiksUIState.Success).data
            }else{
                emptyList()
            }
        }
    }

    if(isLoading){
        LoadingUI()
        return
    }

    fun onOpen(genreMusikId: String) {
        RouteHelper.to(
            navController = navController,
            destination = "genre-musik/${genreMusikId}" 
        )
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.background)
    ) {
        TopAppBarComponent(
            navController = navController,
            title = "Genre Musik", showBackButton = false, 
            withSearch = true,
            searchQuery = searchQuery,
            onSearchQueryChange = { query ->
                searchQuery = query
            },
            onSearchAction = {
                fetchGenreMusiksData()
            }
        )
        Box(
            modifier = Modifier
                .weight(1f)
        ) {
            GenreMusikUI(
                genreMusiks = genreMusiks,
                onOpen = ::onOpen
            )

            Box(
                modifier = Modifier
                    .fillMaxSize()
            )
            {
                FloatingActionButton(
                    onClick = {
                        RouteHelper.to(
                            navController,
                            ConstHelper.RouteNames.GenreMusikAdd.path
                        )
                    },
                    modifier = Modifier
                        .align(Alignment.BottomEnd)
                        .padding(16.dp)
                    ,
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = MaterialTheme.colorScheme.onPrimary
                ) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = "Tambah Genre Musik"
                    )
                }
            }
        }
        BottomNavComponent(navController = navController)
    }
}

@Composable
fun GenreMusikUI(
    genreMusiks: List<ResponseGenreMusikData>,
    onOpen: (String) -> Unit
) {
    if(genreMusiks.isEmpty()){
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(32.dp),
                shape = MaterialTheme.shapes.medium,
                elevation = CardDefaults.cardElevation(4.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
            ) {
                Text(
                    text = "Tidak ada data!",
                    style = MaterialTheme.typography.bodyMedium,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                )
            }
        }
    } else {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            items(genreMusiks) { genreMusik ->
                GenreMusikItemUI(
                    genreMusik,
                    onOpen
                )
            }
        }
    }
}

@Composable
fun GenreMusikItemUI(
    genreMusik: ResponseGenreMusikData,
    onOpen: (String) -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clickable {
                onOpen(genreMusik.id)
            },
        shape = MaterialTheme.shapes.medium,
        elevation = CardDefaults.cardElevation(4.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp)
        ) {
            AsyncImage(
                // PERBAIKAN: Gunakan genreMusik.id alih-alih pathGambar jika menggunakan endpoint /image
                model = ToolsHelper.getGenreMusikImageUrl(genreMusik.id),
                contentDescription = genreMusik.nama,
                placeholder = painterResource(R.drawable.img_placeholder),
                error = painterResource(R.drawable.img_placeholder),
                modifier = Modifier
                    .size(70.dp)
                    .clip(MaterialTheme.shapes.medium),
                contentScale = ContentScale.Crop
            )

            Spacer(modifier = Modifier.width(12.dp))

            Column(
                modifier = Modifier
                    .weight(1f)
            ) {
                Text(
                    text = genreMusik.nama,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = genreMusik.deskripsi,
                    style = MaterialTheme.typography.bodyMedium,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
    }
}
