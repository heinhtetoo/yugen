package com.yugen.animeapp.ui.screen.search

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material.icons.rounded.CheckCircle
import androidx.compose.material.icons.rounded.Clear
import androidx.compose.material.icons.rounded.History
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material.icons.rounded.SearchOff
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import com.yugen.animeapp.R
import com.yugen.animeapp.core.utils.USERNAME_CHARACTER_LIMIT
import com.yugen.animeapp.ui.screen.animelist.AnimeListBody

@Composable
fun SearchScreen(
    navigateBack: () -> Unit,
    navigateToAnimeDetails: (Int) -> Unit,
    modifier: Modifier = Modifier,
    searchViewModel: SearchViewModel = hiltViewModel()
) {
    val searchResults = searchViewModel.searchResults.collectAsLazyPagingItems()
    val recentSearches by searchViewModel.recentSearches.collectAsState()

    val focusManager = LocalFocusManager.current

    Column(modifier = Modifier.fillMaxSize()) {
        SearchHeader(
            query = searchViewModel.searchQuery,
            onQueryChanged = searchViewModel::onQueryChanged,
            onSearch = {
                searchViewModel.onSearchTriggered(query = it)
                focusManager.clearFocus()
            },
            selectedType = searchViewModel.selectedType,
            onTypeSelected = searchViewModel::onTypeSelected,
            selectedStatus = searchViewModel.selectedStatus,
            onStatusSelected = searchViewModel::onStatusSelected,
            onClearQuery = { searchViewModel.onQueryChanged("") },
            onNavigateBack = navigateBack
        )

        if (searchViewModel.searchQuery.isBlank()) {
            SearchHistoryList(
                history = recentSearches,
                onItemClick = {
                    searchViewModel.onQueryChanged(it)
                    searchViewModel.onSearchTriggered(it)
                },
                onDeleteClick = searchViewModel::deleteSearchHistory
            )
        } else {
            if (searchResults.itemCount == 0 && searchResults.loadState.refresh !is LoadState.Loading) {
                EmptySearchBody()
            } else if (searchResults.loadState.refresh is LoadState.Loading) {
                LoadingBody()
            } else {
                AnimeListBody(
                    lazyData = searchResults,
                    onAnimeClick = {
                        searchViewModel.onSearchTriggered(searchViewModel.searchQuery)
                        navigateToAnimeDetails(it)
                    }
                )
            }
        }
    }
}

@Composable
fun SearchHeader(
    query: String,
    onQueryChanged: (String) -> Unit,
    onSearch: (String) -> Unit,
    onClearQuery: () -> Unit,
    selectedType: String?,
    onTypeSelected: (String) -> Unit,
    selectedStatus: String?,
    onStatusSelected: (String) -> Unit,
    onNavigateBack: () -> Unit
) {
    Column(
        modifier = Modifier
            .background(MaterialTheme.colorScheme.surface)
            .padding(bottom = dimensionResource(R.dimen.padding_medium))
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(dimensionResource(R.dimen.top_app_bar_height))
                .padding(
                    horizontal = dimensionResource(R.dimen.padding_medium),
                    vertical = dimensionResource(R.dimen.padding_xsmall)
                ),
            horizontalArrangement = Arrangement.Start,
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(
                onClick = onNavigateBack,
                modifier = Modifier.size(dimensionResource(R.dimen.icon_size_normal))
            ) {
                Icon(
                    Icons.AutoMirrored.Rounded.ArrowBack,
                    contentDescription = stringResource(R.string.back_button)
                )
            }
            BasicTextField(
                value = query,
                onValueChange = onQueryChanged,
                singleLine = true,
                textStyle = MaterialTheme.typography.bodyLarge.copy(
                    color = MaterialTheme.colorScheme.onSurface
                ),
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
                keyboardActions = KeyboardActions(onSearch = { onSearch(query) }),
                cursorBrush = SolidColor(MaterialTheme.colorScheme.primary),
                modifier = Modifier
                    .weight(1f)
                    .padding(start = dimensionResource(R.dimen.padding_medium))
                    .height(dimensionResource(R.dimen.top_app_bar_height)),
                decorationBox = { innerTextField ->
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .fillMaxWidth()
                            .border(
                                width = 1.dp,
                                color = MaterialTheme.colorScheme.primary,
                                shape = RoundedCornerShape(dimensionResource(R.dimen.rounded_corner_shape_normal))
                            )
                            .padding(horizontal = dimensionResource(R.dimen.padding_small))
                    ) {
                        Icon(
                            Icons.Rounded.Search,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.onSurfaceVariant
                        )

                        Spacer(modifier = Modifier.width(dimensionResource(R.dimen.padding_small)))

                        Box(modifier = Modifier.weight(1f)) {
                            if (query.isEmpty()) {
                                Text(
                                    text = stringResource(R.string.search_anime),
                                    style = MaterialTheme.typography.bodyLarge,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                            innerTextField()
                        }

                        if (query.isNotEmpty()) {
                            IconButton(onClick = onClearQuery) {
                                Icon(
                                    Icons.Rounded.Clear,
                                    contentDescription = stringResource(R.string.clear),
                                    tint = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }
                    }
                }
            )
        }

        Spacer(Modifier.height(dimensionResource(R.dimen.padding_small)))

        FilterRow(
            listOf("tv" to "TV Series", "movie" to "Movie", "ova" to "OVA", "special" to "Special"),
            selectedType,
            onTypeSelected
        )
        FilterRow(
            listOf("airing" to "Airing", "complete" to "Completed", "upcoming" to "Upcoming"),
            selectedStatus,
            onStatusSelected
        )
    }
}

@Composable
fun FilterRow(
    filters: List<Pair<String, String>>,
    selectedFilter: String?,
    onFilterSelected: (String) -> Unit
) {
    LazyRow(
        contentPadding = PaddingValues(horizontal = dimensionResource(R.dimen.padding_medium)),
        horizontalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.padding_small))
    ) {
        items(filters) { (id, label) ->
            FilterChip(
                selected = selectedFilter == id,
                onClick = { onFilterSelected(id) },
                label = { Text(label) },
                leadingIcon = if (selectedFilter == id) {
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
    }
}

@Composable
fun SearchHistoryList(
    history: List<String>,
    onItemClick: (String) -> Unit,
    onDeleteClick: (String) -> Unit
) {
    if (history.isNotEmpty()) {
        Column(
            modifier = Modifier.padding(
                horizontal = dimensionResource(R.dimen.padding_medium),
                vertical = dimensionResource(R.dimen.padding_small)
            )
        ) {
            Text(
                stringResource(R.string.recent_searches),
                style = MaterialTheme.typography.titleSmall,
                color = MaterialTheme.colorScheme.primary
            )

            Spacer(Modifier.height(dimensionResource(R.dimen.padding_small)))

            LazyColumn {
                items(history) { item ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { onItemClick(item) }
                            .padding(vertical = dimensionResource(R.dimen.padding_small))
                    ) {
                        Icon(Icons.Rounded.History, contentDescription = null, tint = Color.Gray)
                        Spacer(Modifier.width(dimensionResource(R.dimen.padding_medium)))
                        Text(
                            item,
                            modifier = Modifier.weight(1f),
                            style = MaterialTheme.typography.bodyLarge
                        )
                        IconButton(onClick = { onDeleteClick(item) }) {
                            Icon(
                                Icons.Rounded.Clear,
                                contentDescription = stringResource(R.string.delete)
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun EmptySearchBody() {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Icon(
                Icons.Rounded.SearchOff,
                contentDescription = null,
                modifier = Modifier.size(dimensionResource(R.dimen.icon_size_3xlarge)),
                tint = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Spacer(Modifier.height(dimensionResource(R.dimen.padding_medium)))
            Text(
                stringResource(R.string.no_anime_found),
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Composable
fun LoadingBody() {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        CircularProgressIndicator()
    }
}