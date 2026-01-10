package com.yugen.animeapp.ui.screen.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Check
import androidx.compose.material.icons.rounded.CheckCircle
import androidx.compose.material.icons.rounded.Edit
import androidx.compose.material.icons.rounded.Person2
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
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
import com.yugen.animeapp.core.utils.USERNAME_CHARACTER_LIMIT
import com.yugen.animeapp.core.utils.getAvatarDrawableRes
import com.yugen.animeapp.core.utils.getAvatarStringRes
import com.yugen.animeapp.core.utils.getWatchStatusColour
import com.yugen.animeapp.core.utils.getWatchStatusIcon
import com.yugen.animeapp.core.utils.getWatchStatusLabelRes
import com.yugen.animeapp.data.local.model.GenreStat
import com.yugen.animeapp.data.mapper.toAnime
import com.yugen.animeapp.domain.model.ThemePreference
import com.yugen.animeapp.domain.model.WatchStatus

@Composable
fun ProfileScreen(
    modifier: Modifier = Modifier,
    profileViewModel: ProfileViewModel = hiltViewModel()
) {

    val theme by profileViewModel.theme.collectAsState()
    val profileUiState by profileViewModel.uiState.collectAsState()

    ProfileBody(
        profileUiState = profileUiState,
        currentTheme = theme,
        onUsernameChanged = profileViewModel::updateUsername,
        onThemeSelected = profileViewModel::updateTheme
    )
}

@Composable
fun ProfileBody(
    profileUiState: ProfileUiState,
    currentTheme: ThemePreference,
    onUsernameChanged: (String) -> Unit,
    onThemeSelected: (ThemePreference) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        when (profileUiState) {
            is ProfileUiState.Loading -> Box(Modifier.fillMaxSize(), Alignment.Center) {
                CircularProgressIndicator()
            }

            is ProfileUiState.Error -> ErrorBody(profileUiState.message, profileUiState.details)
            is ProfileUiState.Success -> ProfileContent(
                username = profileUiState.username,
                totalAnime = profileUiState.totalAnime,
                completedCount = profileUiState.completedCount,
                watchingCount = profileUiState.watchingCount,
                plannedCount = profileUiState.plannedCount,
                topGenres = profileUiState.topGenres,
                currentTheme = currentTheme,
                onUsernameChanged = onUsernameChanged,
                onThemeSelected = onThemeSelected
            )

            else -> {}
        }
    }
}

@Composable
fun ProfileContent(
    username: String,
    totalAnime: Int,
    completedCount: Int,
    watchingCount: Int,
    plannedCount: Int,
    topGenres: List<GenreStat>,
    currentTheme: ThemePreference,
    onUsernameChanged: (String) -> Unit,
    onThemeSelected: (ThemePreference) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {
        ProfileHeader(
            username = username,
            totalAnime = totalAnime,
            onUsernameChanged = onUsernameChanged
        )

        Spacer(Modifier.height(dimensionResource(R.dimen.padding_large)))

        StatsSection(
            completed = completedCount,
            watching = watchingCount,
            planned = plannedCount
        )

        Spacer(Modifier.height(dimensionResource(R.dimen.padding_large)))

        if (topGenres.isNotEmpty()) {
            GenreDistributionSection(genres = topGenres)
            Spacer(Modifier.height(dimensionResource(R.dimen.padding_large)))
        }

        SettingsSection(
            currentTheme = currentTheme,
            onThemeSelected = onThemeSelected
        )

        Spacer(Modifier.height(dimensionResource(R.dimen.padding_2xlarge)))
    }
}

@Composable
fun ProfileHeader(username: String, totalAnime: Int, onUsernameChanged: (String) -> Unit) {
    var isEditing by remember { mutableStateOf(false) }
    var tempName by remember(username) { mutableStateOf(username) }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = dimensionResource(R.dimen.padding_large)),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Surface(
            modifier = Modifier.size(dimensionResource(R.dimen.profile_image)),
            shape = CircleShape,
            color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
        ) {
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(getAvatarDrawableRes(animeCount = totalAnime))
                    .crossfade(true)
                    .build(),
                placeholder = painterResource(R.drawable.ic_yugen_greyscale_24),
                error = painterResource(R.drawable.ic_yugen_greyscale_24),
                contentDescription = stringResource(R.string.avatar),
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )
        }

        Spacer(Modifier.height(dimensionResource(R.dimen.padding_medium)))

        if (isEditing) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Spacer(Modifier.width(dimensionResource(R.dimen.padding_3xlarge)))

                BasicTextField(
                    value = tempName,
                    onValueChange = { if (it.length <= USERNAME_CHARACTER_LIMIT) tempName = it },
                    singleLine = true,
                    textStyle = MaterialTheme.typography.headlineSmall.copy(
                        color = MaterialTheme.colorScheme.onSurface,
                        textAlign = TextAlign.Center
                    ),
                    cursorBrush = SolidColor(MaterialTheme.colorScheme.primary),
                    modifier = Modifier.weight(1f),
                    decorationBox = { innerTextField ->
                        Box(
                            modifier = Modifier
                                .border(
                                    width = 1.dp,
                                    color = MaterialTheme.colorScheme.primary,
                                    shape = RoundedCornerShape(dimensionResource(R.dimen.rounded_corner_shape_normal))
                                )
                                .padding(
                                    horizontal = dimensionResource(R.dimen.padding_xsmall),
                                    vertical = dimensionResource(R.dimen.padding_2xsmall)
                                ),
                            contentAlignment = Alignment.Center
                        ) {
                            innerTextField()
                        }
                    }
                )

                Spacer(Modifier.width(dimensionResource(R.dimen.padding_small)))

                IconButton(
                    onClick = {
                        if (tempName.isNotBlank()) {
                            onUsernameChanged(tempName)
                            isEditing = false
                        }
                    },
                    colors = IconButtonDefaults.iconButtonColors(
                        containerColor = MaterialTheme.colorScheme.primary
                    )
                ) {
                    Icon(
                        Icons.Rounded.Check, contentDescription = stringResource(R.string.save),
                        modifier = Modifier.size(dimensionResource(R.dimen.icon_size_small)),
                        tint = MaterialTheme.colorScheme.onPrimary
                    )
                }
            }
        } else {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier.clickable {
                    tempName = username
                    isEditing = true
                }) {
                Spacer(Modifier.width(dimensionResource(R.dimen.padding_xlarge)))

                Text(
                    text = username,
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold
                )

                Spacer(Modifier.width(dimensionResource(R.dimen.padding_small)))

                Icon(
                    imageVector = Icons.Rounded.Edit,
                    contentDescription = stringResource(R.string.edit_name),
                    modifier = Modifier.size(dimensionResource(R.dimen.icon_size_small)),
                    tint = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }

        Text(
            text = stringResource(getAvatarStringRes(animeCount = totalAnime)),
            style = MaterialTheme.typography.labelLarge,
            color = MaterialTheme.colorScheme.primary
        )
    }
}

@Composable
fun StatsSection(completed: Int, watching: Int, planned: Int) {
    Column(modifier = Modifier.padding(dimensionResource(R.dimen.padding_medium))) {
        Text(
            "Statistics",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold
        )
        Spacer(Modifier.height(dimensionResource(R.dimen.padding_small)))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.padding_small))
        ) {
            StatCard(
                label = stringResource(getWatchStatusLabelRes(WatchStatus.COMPLETED)),
                value = completed.toString(),
                icon = getWatchStatusIcon(WatchStatus.COMPLETED),
                color = getWatchStatusColour(WatchStatus.COMPLETED),
                modifier = Modifier.weight(1f)
            )
            StatCard(
                label = stringResource(getWatchStatusLabelRes(WatchStatus.WATCHING)),
                value = watching.toString(),
                icon = getWatchStatusIcon(WatchStatus.WATCHING),
                color = getWatchStatusColour(WatchStatus.WATCHING),
                modifier = Modifier.weight(1f)
            )
            StatCard(
                label = stringResource(getWatchStatusLabelRes(WatchStatus.PLAN_TO_WATCH)),
                value = planned.toString(),
                icon = getWatchStatusIcon(WatchStatus.PLAN_TO_WATCH),
                color = getWatchStatusColour(WatchStatus.PLAN_TO_WATCH),
                modifier = Modifier.weight(1f)
            )
        }
    }
}

@Composable
fun StatCard(label: String, value: String, icon: ImageVector, color: Color, modifier: Modifier) {
    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
    ) {
        Column(
            modifier = Modifier
                .padding(dimensionResource(R.dimen.padding_medium))
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(imageVector = icon, contentDescription = null, tint = color)
            Spacer(modifier = Modifier.height(dimensionResource(R.dimen.padding_small)))
            Text(
                text = value,
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = label,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Composable
fun GenreDistributionSection(genres: List<GenreStat>) {
    Column(modifier = Modifier.padding(horizontal = dimensionResource(R.dimen.padding_medium))) {
        Text(
            "Top Genres",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(dimensionResource(R.dimen.padding_small)))

        val maxCount = genres.maxOfOrNull { it.count }?.toFloat() ?: 1f

        genres.forEach { genre ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = dimensionResource(R.dimen.padding_xsmall)),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = genre.genreName,
                    maxLines = 3,
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth(),
                    style = MaterialTheme.typography.bodyMedium
                )

                Box(
                    modifier = Modifier
                        .padding(start = dimensionResource(R.dimen.padding_medium))
                        .weight(3f)
                        .height(dimensionResource(R.dimen.padding_small))
                        .clip(RoundedCornerShape(dimensionResource(R.dimen.rounded_corner_shape_xsmall)))
                        .background(MaterialTheme.colorScheme.surfaceVariant)
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth(fraction = (genre.count / maxCount))
                            .fillMaxHeight()
                            .background(MaterialTheme.colorScheme.primary)
                    )
                }

                Text(
                    text = "${genre.count}",
                    textAlign = TextAlign.End,
                    modifier = Modifier
                        .padding(start = dimensionResource(R.dimen.padding_small))
                        .defaultMinSize(dimensionResource(R.dimen.padding_xlarge)),
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }
    }
}

@Composable
fun SettingsSection(currentTheme: ThemePreference?, onThemeSelected: (ThemePreference) -> Unit) {
    Column(modifier = Modifier.padding(horizontal = dimensionResource(R.dimen.padding_medium))) {
        Text(
            "Appearance",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(dimensionResource(R.dimen.padding_small)))

        Card(modifier = Modifier.fillMaxWidth()) {
            Column(modifier = Modifier.padding(dimensionResource(R.dimen.padding_medium))) {
                Text("App Theme", style = MaterialTheme.typography.bodyMedium)
                Spacer(modifier = Modifier.height(dimensionResource(R.dimen.padding_small)))

                Row(horizontalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.padding_small))) {
                    ThemeChip(
                        selected = currentTheme == ThemePreference.LIGHT,
                        label = stringResource(R.string.light_mode),
                        onClick = { onThemeSelected(ThemePreference.LIGHT) }
                    )
                    ThemeChip(
                        selected = currentTheme == ThemePreference.DARK,
                        label = stringResource(R.string.dark_mode),
                        onClick = { onThemeSelected(ThemePreference.DARK) }
                    )
                    ThemeChip(
                        selected = currentTheme == ThemePreference.SYSTEM,
                        label = stringResource(R.string.system_default),
                        onClick = { onThemeSelected(ThemePreference.SYSTEM) }
                    )
                }
            }
        }


    }
}

@Composable
fun ThemeChip(
    selected: Boolean,
    label: String,
    onClick: () -> Unit
) {
    FilterChip(
        selected = selected,
        onClick = onClick,
        label = {
            Text(
                label,
                style = MaterialTheme.typography.bodySmall,
                fontWeight = if (selected) FontWeight.SemiBold else FontWeight.Normal,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        },
        leadingIcon = if (selected) {
            {
                Icon(
                    Icons.Rounded.CheckCircle,
                    contentDescription = null,
                    Modifier.size(dimensionResource(R.dimen.icon_size_xsmall))
                )
            }
        } else null
    )
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
        details?.let { Text(it, maxLines = 3) }
    }
}

@Preview(showBackground = true)
@Composable
private fun ProfileBodyPreview() {
    ProfileBody(
        profileUiState = ProfileUiState.Success(
            "Yugen User",
            54,
            30,
            4,
            20,
            listOf(GenreStat("Action", 20), GenreStat("Comedy", 1))
        ),
        currentTheme = ThemePreference.SYSTEM,
        onUsernameChanged = {},
        onThemeSelected = {}
    )
}