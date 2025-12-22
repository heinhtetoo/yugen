package com.yugen.animeapp.ui.screen.animedetails

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material.icons.automirrored.rounded.TrendingUp
import androidx.compose.material.icons.rounded.AddCircleOutline
import androidx.compose.material.icons.rounded.CheckCircle
import androidx.compose.material.icons.rounded.DateRange
import androidx.compose.material.icons.rounded.Favorite
import androidx.compose.material.icons.rounded.FavoriteBorder
import androidx.compose.material.icons.rounded.PlayCircleFilled
import androidx.compose.material.icons.rounded.PlayCircleOutline
import androidx.compose.material.icons.rounded.RemoveCircle
import androidx.compose.material.icons.rounded.RemoveCircleOutline
import androidx.compose.material.icons.rounded.Star
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.yugen.animeapp.R
import com.yugen.animeapp.core.utils.getWatchStatusIcon
import com.yugen.animeapp.core.utils.getWatchStatusLabelRes
import com.yugen.animeapp.domain.model.Anime
import com.yugen.animeapp.domain.model.AnimeDetails
import com.yugen.animeapp.domain.model.WatchStatus
import com.yugen.animeapp.ui.screen.home.AnimeItem

@Composable
fun AnimeDetailsScreen(
    navigateBack: () -> Unit,
    navigateToAnimeDetails: (Int) -> Unit,
    modifier: Modifier = Modifier,
    animeDetailsViewModel: AnimeDetailsViewModel = hiltViewModel()
) {
    val animeDetailsUiState by animeDetailsViewModel.uiState.collectAsState()
    val recommendations by animeDetailsViewModel.recommendations.collectAsState()

    Scaffold(
        topBar = {
            Icon(
                Icons.AutoMirrored.Rounded.ArrowBack,
                contentDescription = stringResource(R.string.back_button),
                modifier = Modifier
                    .padding(dimensionResource(R.dimen.padding_medium))
                    .clickable(true) { navigateBack() }
            )
        }
    ) { paddingValues ->
        AnimeDetailsBody(
            animeDetailsUiState = animeDetailsUiState,
            recommendations = recommendations,
            onFavouriteClick = animeDetailsViewModel::toggleFavourite,
            onWatchStatusChange = animeDetailsViewModel::updateWatchStatus,
            onRemoveFromLibraryClick = animeDetailsViewModel::removeAnimeFromLibrary,
            onRecommendationClick = navigateToAnimeDetails,
            paddingValues = paddingValues
        )

    }
}

@Composable
fun AnimeDetailsBody(
    animeDetailsUiState: AnimeDetailsUiState,
    recommendations: List<Anime>,
    onFavouriteClick: () -> Unit,
    onWatchStatusChange: (WatchStatus) -> Unit,
    onRemoveFromLibraryClick: () -> Unit,
    onRecommendationClick: (Int) -> Unit,
    paddingValues: PaddingValues,
    modifier: Modifier = Modifier
) {

    when (animeDetailsUiState) {
        is AnimeDetailsUiState.Idle -> Box(
            Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text(stringResource(R.string.idle))
        }

        is AnimeDetailsUiState.Loading -> Box(
            Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator()
        }

        is AnimeDetailsUiState.Error -> ErrorBody(
            animeDetailsUiState.message,
            animeDetailsUiState.details
        )

        is AnimeDetailsUiState.Success ->
            Column(
                modifier = modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(bottom = paddingValues.calculateBottomPadding())
            ) {
                AnimeDetails(
                    animeDetails = animeDetailsUiState.animeDetails,
                    recommendations = recommendations,
                    isFavourite = animeDetailsUiState.isFavourite,
                    watchStatus = animeDetailsUiState.watchStatus,
                    onFavouriteClick = onFavouriteClick,
                    onWatchStatusChange = onWatchStatusChange,
                    onRemoveFromLibraryClick = onRemoveFromLibraryClick,
                    onRecommendationClick = onRecommendationClick,
                    Modifier.fillMaxSize()
                )
            }
    }
}

@Composable
fun AnimeDetails(
    animeDetails: AnimeDetails,
    recommendations: List<Anime>,
    isFavourite: Boolean,
    watchStatus: WatchStatus?,
    onFavouriteClick: () -> Unit,
    onWatchStatusChange: (WatchStatus) -> Unit,
    onRemoveFromLibraryClick: () -> Unit,
    onRecommendationClick: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    var synopsisExpanded by remember { mutableStateOf(false) }

    Box(modifier = Modifier.height(dimensionResource(R.dimen.anime_backdrop_container_height))) {
        AsyncImage(
            model = ImageRequest.Builder(LocalContext.current)
                .data(animeDetails.images?.jpg?.largeImageUrl)
                .crossfade(true)
                .build(),
            placeholder = painterResource(R.drawable.ic_image_placeholder),
            error = painterResource(R.drawable.ic_image_placeholder),
            contentDescription = stringResource(R.string.anime_backdrop),
            contentScale = ContentScale.FillWidth,
            modifier = Modifier
                .fillMaxWidth()
                .height(dimensionResource(R.dimen.anime_backdrop_height))
        )

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(dimensionResource(R.dimen.anime_backdrop_height))
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            MaterialTheme.colorScheme.background,
                            MaterialTheme.colorScheme.background.copy(alpha = 0.5f),
                            Color.Transparent,
                            Color.Transparent,
                            Color.Transparent,
                            Color.Transparent,
                            Color.Transparent,
                            MaterialTheme.colorScheme.background.copy(alpha = 0.5f),
                            MaterialTheme.colorScheme.background
                        )
                    )
                )
        )

        Column(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(horizontal = dimensionResource(R.dimen.padding_medium)),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
//            Card(
//                elevation = CardDefaults.cardElevation(),
//                modifier = Modifier
//                    .width(dimensionResource(R.dimen.anime_width_large))
//                    .height(dimensionResource(R.dimen.anime_height_large))
//            ) {
//                AsyncImage(
//                    model = ImageRequest.Builder(LocalContext.current)
//                        .data(animeDetails.images?.jpg?.imageUrl)
//                        .crossfade(true)
//                        .build(),
//                    placeholder = painterResource(R.drawable.ic_image_placeholder),
//                    error = painterResource(R.drawable.ic_image_placeholder),
//                    contentDescription = stringResource(R.string.anime_poster),
//                    contentScale = ContentScale.Crop,
//                    modifier = Modifier.fillMaxSize()
//                )
//            }

            Column(
                modifier = Modifier
                    .padding(top = dimensionResource(R.dimen.padding_large))
                    .fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = animeDetails.title ?: "Unknown",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
                animeDetails.titleJapanese?.let {
                    Spacer(Modifier.height(dimensionResource(R.dimen.padding_xsmall)))
                    Text(
                        it,
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.secondary
                    )
                }
                Row {
                    Spacer(Modifier.height(dimensionResource(R.dimen.padding_xsmall)))
                    animeDetails.status?.let {
                        Text(
                            it,
                            style = MaterialTheme.typography.labelMedium,
                            color = MaterialTheme.colorScheme.secondary
                        )
                    }
                    animeDetails.status?.let {
                        animeDetails.airedFromYear?.let {
                            Text(
                                stringResource(R.string.dot_separator),
                                style = MaterialTheme.typography.labelMedium,
                                color = MaterialTheme.colorScheme.secondary,
                                modifier = Modifier.padding(horizontal = dimensionResource(R.dimen.padding_small))
                            )
                        }
                    }
                    animeDetails.airedFromYear?.let {
                        Text(
                            if (animeDetails.airedToYear != null && animeDetails.airedToYear != it) {
                                "$it - ${animeDetails.airedToYear}"
                            } else if (animeDetails.airedToYear == null && animeDetails.airing) {
                                "$it - Present"
                            } else "$it",
                            style = MaterialTheme.typography.labelMedium,
                            color = MaterialTheme.colorScheme.secondary
                        )
                    }
                }
            }
        }
    }

    Column(
        modifier = modifier.padding(
            horizontal = dimensionResource(R.dimen.padding_medium),
            vertical = dimensionResource(R.dimen.padding_small)
        ),
        horizontalAlignment = Alignment.Start,
        verticalArrangement = Arrangement.Top
    ) {
        Spacer(Modifier.height(dimensionResource(R.dimen.padding_large)))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.padding_small))
        ) {
            ActionButton(
                onClick = onFavouriteClick,
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight(),
                isSelected = isFavourite,
                selectedIcon = Icons.Rounded.Favorite,
                unselectedIcon = Icons.Rounded.FavoriteBorder,
                selectedTextRes = R.string.remove_from_favourite,
                unselectedTextRes = R.string.add_to_favourite
            )
            WatchStatusButton(
                currentStatus = watchStatus,
                onStatusSelected = onWatchStatusChange,
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight(),
                isHighlighted = watchStatus != null
            )
            watchStatus?.let {
                ActionButton(
                    onClick = onRemoveFromLibraryClick,
                    modifier = Modifier
                        .weight(0.75f)
                        .fillMaxHeight(),
                    isSelected = true,
                    selectedIcon = Icons.Rounded.RemoveCircle,
                    unselectedIcon = Icons.Rounded.RemoveCircleOutline,
                    selectedColor = MaterialTheme.colorScheme.error,
                    unselectedColor = MaterialTheme.colorScheme.error,
                    selectedTextRes = R.string.remove_from_library,
                    unselectedTextRes = R.string.add_to_library
                )
            }
        }

        Spacer(modifier = Modifier.height(dimensionResource(R.dimen.padding_xlarge)))

        Column {
            SectionTitle(stringResource(R.string.synopsis))

            animeDetails.synopsis?.let {
                Text(
                    it,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    maxLines = if (synopsisExpanded) Int.MAX_VALUE else 5,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.clickable { synopsisExpanded = !synopsisExpanded }
                )
                Spacer(Modifier.height(dimensionResource(R.dimen.padding_medium)))
            }

            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                InfoItem(
                    animeDetails.scoredBy?.let { stringResource(R.string.scored_by, it) }
                        ?: stringResource(R.string.score),
                    animeDetails.score?.toString() ?: stringResource(R.string.na),
                    Icons.Rounded.Star,
                    Modifier.weight(1f)
                )
                InfoItem(
                    stringResource(R.string.episodes),
                    animeDetails.episodes?.toString() ?: stringResource(R.string.na),
                    Icons.Rounded.PlayCircleOutline,
                    Modifier.weight(1f)
                )
                InfoItem(
                    stringResource(R.string.rank),
                    animeDetails.rank?.toString() ?: stringResource(R.string.na),
                    Icons.AutoMirrored.Rounded.TrendingUp,
                    Modifier.weight(1f)
                )
                InfoItem(
                    stringResource(R.string.favourites),
                    animeDetails.favourites?.toString() ?: stringResource(R.string.na),
                    Icons.Rounded.Favorite,
                    Modifier.weight(1f)
                )
            }

        }
    }

    Column(
        modifier = modifier.padding(
            vertical = dimensionResource(R.dimen.padding_small)
        ),
        horizontalAlignment = Alignment.Start,
        verticalArrangement = Arrangement.Top
    ) {
        Spacer(Modifier.height(dimensionResource(R.dimen.padding_large)))

        if (recommendations.isNotEmpty()) {
            SectionTitle(
                stringResource(R.string.recommendations),
                Modifier.padding(horizontal = dimensionResource(R.dimen.padding_medium))
            )

            LazyRow(
                contentPadding = PaddingValues(horizontal = dimensionResource(R.dimen.padding_medium)),
                horizontalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.padding_medium)),
                modifier = Modifier.padding(vertical = dimensionResource(R.dimen.padding_xsmall))
            ) {
                items(recommendations) { anime ->
                    RecommendedAnimeItem(
                        anime = anime,
                        onAnimeClick = { onRecommendationClick(anime.id) },
                        modifier = Modifier
                            .size(
                                dimensionResource(R.dimen.anime_width_small),
                                dimensionResource(R.dimen.anime_height_small)
                            )
                    )
                }
            }

            Spacer(Modifier.height(dimensionResource(R.dimen.padding_xlarge)))
        }
    }
}

@Composable
fun SectionTitle(title: String, modifier: Modifier = Modifier) {
    Text(
        text = title,
        style = MaterialTheme.typography.titleMedium,
        fontWeight = FontWeight.Bold,
        modifier = modifier.padding(bottom = dimensionResource(R.dimen.padding_small))
    )
}

@Composable
fun InfoItem(label: String, value: String, icon: ImageVector, modifier: Modifier = Modifier) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
    ) {
        Icon(imageVector = icon, contentDescription = null)
        Text(value, fontWeight = FontWeight.Bold)
        Text(
            label,
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.secondary,
            textAlign = TextAlign.Center
        )
    }
}

@Composable
fun ActionButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    isSelected: Boolean,
    selectedIcon: ImageVector,
    unselectedIcon: ImageVector,
    selectedColor: Color = MaterialTheme.colorScheme.primary,
    unselectedColor: Color = MaterialTheme.colorScheme.background,
    selectedTextRes: Int,
    unselectedTextRes: Int
) {
    Box(modifier = modifier) {
        OutlinedButton(
            onClick = onClick,
            modifier = Modifier.fillMaxSize(),
            shape = RoundedCornerShape(dimensionResource(R.dimen.rounded_corner_shape_normal)),
            border = BorderStroke(1.dp, selectedColor),
            colors = ButtonDefaults.outlinedButtonColors(
                containerColor = if (isSelected) selectedColor.copy(alpha = 0.1f)
                else unselectedColor.copy(alpha = 0.1f),
                contentColor = selectedColor
            )
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Icon(
                    imageVector = if (isSelected) selectedIcon else unselectedIcon,
                    contentDescription = null,
                    tint = selectedColor
                )
                Spacer(Modifier.width(dimensionResource(R.dimen.padding_small)))
                Text(
                    stringResource(if (isSelected) selectedTextRes else unselectedTextRes),
                    style = MaterialTheme.typography.labelSmall,
                    maxLines = 1,
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}

@Composable
fun WatchStatusButton(
    currentStatus: WatchStatus?,
    onStatusSelected: (WatchStatus) -> Unit,
    modifier: Modifier = Modifier,
    isHighlighted: Boolean,
    highlightColor: Color = MaterialTheme.colorScheme.primary,
    baseColor: Color = MaterialTheme.colorScheme.background
) {

    var expanded by remember { mutableStateOf(false) }

    Box(modifier = modifier) {
        OutlinedButton(
            onClick = { expanded = true },
            modifier = Modifier.fillMaxSize(),
            shape = RoundedCornerShape(dimensionResource(R.dimen.rounded_corner_shape_normal)),
            border = BorderStroke(1.dp, highlightColor),
            colors = ButtonDefaults.outlinedButtonColors(
                containerColor = if (isHighlighted) highlightColor.copy(alpha = 0.1f)
                else baseColor.copy(alpha = 0.1f),
                contentColor = highlightColor
            )
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Icon(
                    imageVector = getWatchStatusIcon(currentStatus),
                    contentDescription = null,
                    tint = highlightColor
                )
                Spacer(Modifier.width(dimensionResource(R.dimen.padding_small)))
                Text(
                    stringResource(getWatchStatusLabelRes(currentStatus)),
                    style = MaterialTheme.typography.labelSmall,
                    maxLines = 1,
                    textAlign = TextAlign.Center
                )
            }

            DropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {
                WatchStatus.entries.forEach { status ->
                    DropdownMenuItem(
                        text = { Text(stringResource(getWatchStatusLabelRes(status))) },
                        onClick = {
                            onStatusSelected(status)
                            expanded = false
                        },
                        leadingIcon = {
                            Icon(
                                imageVector = getWatchStatusIcon(status),
                                contentDescription = null
                            )
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun RecommendedAnimeItem(
    anime: Anime,
    onAnimeClick: (Anime) -> Unit,
    modifier: Modifier = Modifier
) {

    Card(
        modifier = modifier.clickable { onAnimeClick(anime) },
        elevation = CardDefaults.cardElevation()
    ) {
        AsyncImage(
            model = ImageRequest.Builder(LocalContext.current)
                .data(anime.images?.jpg?.imageUrl)
                .crossfade(true)
                .build(),
            placeholder = painterResource(R.drawable.ic_image_placeholder),
            error = painterResource(R.drawable.ic_image_placeholder),
            contentDescription = stringResource(R.string.anime_poster),
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )
    }
}

@Composable
fun ErrorBody(
    message: String,
    details: String?,
    modifier: Modifier = Modifier
) {

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(dimensionResource(R.dimen.padding_medium)),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(message, fontWeight = FontWeight.Bold)
        details?.let { Text(it, maxLines = 2) }
    }
}

@Preview
@Composable
private fun AnimeDetailsScreenPreview() {
    Scaffold(
        topBar = {
            Icon(
                Icons.AutoMirrored.Rounded.ArrowBack,
                contentDescription = stringResource(R.string.back_button),
                modifier = Modifier.padding(dimensionResource(R.dimen.padding_medium))
            )
        }
    ) { paddingValues ->
        AnimeDetailsBody(
            animeDetailsUiState = AnimeDetailsUiState.Success(
                AnimeDetails(
                    id = 0, title = "Anime Details",
                    images = null,
                    titleEnglish = "", titleJapanese = "Japanese Title",
                    type = "TV", episodes = 3,
                    status = "Currently Airing", airing = true,
                    airedFromYear = 2024, airedToYear = 2025,
                    rating = "PG-13", score = 4.5,
                    scoredBy = 3259, rank = 3,
                    favourites = 492, synopsis = "Synopsis"
                ),
                isFavourite = true,
                watchStatus = WatchStatus.PLAN_TO_WATCH
            ),
            recommendations = listOf(
                Anime(id = 1, title = "Anime 1", images = null, status = "", synopsis = ""),
                Anime(id = 2, title = "Anime 2", images = null, status = "", synopsis = ""),
                Anime(id = 3, title = "Anime 3", images = null, status = "", synopsis = ""),
                Anime(id = 4, title = "Anime 4", images = null, status = "", synopsis = "")
            ),
            onFavouriteClick = {},
            onWatchStatusChange = {},
            onRemoveFromLibraryClick = {},
            onRecommendationClick = {},
            paddingValues = paddingValues
        )
    }
}

@Preview(showBackground = true)
@Composable
fun AnimeDetailsPreview(modifier: Modifier = Modifier) {
    AnimeDetails(
        AnimeDetails(
            id = 0, title = "Anime Details",
            images = null,
            titleEnglish = "", titleJapanese = "Japanese Title",
            type = "TV", episodes = 3,
            status = "Currently Airing", airing = true,
            airedFromYear = 2024, airedToYear = 2025,
            rating = "PG-13", score = 4.5,
            scoredBy = 3259, rank = 3,
            favourites = 492, synopsis = "Synopsis"
        ),
        recommendations = listOf(
            Anime(id = 1, title = "Anime 1", images = null, status = "", synopsis = ""),
            Anime(id = 2, title = "Anime 2", images = null, status = "", synopsis = "")
        ),
        isFavourite = true,
        watchStatus = null,
        onFavouriteClick = {},
        onWatchStatusChange = {},
        onRemoveFromLibraryClick = {},
        onRecommendationClick = {},
        modifier
            .height(640.dp)
            .width(320.dp)
    )
}