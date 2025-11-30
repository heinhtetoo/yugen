package com.yugen.anime.ui.screen.search

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import com.yugen.anime.R
import com.yugen.anime.domain.model.AnimeCategory
import com.yugen.anime.ui.screen.animelist.AnimeListBody

@Composable
fun SearchScreen(
    navigateToAnimeDetails: (Int, AnimeCategory) -> Unit,
    modifier: Modifier = Modifier,
    searchViewModel: SearchViewModel = hiltViewModel()
) {

    val query by searchViewModel.query.collectAsState()

    val searchResults = searchViewModel.searchResults.collectAsLazyPagingItems()

    Column(modifier = Modifier.fillMaxSize()) {
        OutlinedTextField(
            value = query,
            onValueChange = { searchViewModel.onQueryChanged(it) },
            modifier = Modifier.fillMaxWidth().padding(dimensionResource(R.dimen.padding_medium)),
            placeholder = { Text(stringResource(R.string.search_anime)) },
            leadingIcon = { Icon(Icons.Filled.Search, contentDescription = null) },
            singleLine = true
        )

        if (searchResults.itemCount == 0 && query.isNotEmpty() && searchResults.loadState.refresh !is LoadState.Loading) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text(text = stringResource(R.string.no_anime_found))
            }
        } else {
            AnimeListBody(
                lazyData = searchResults,
                onAnimeClick = { navigateToAnimeDetails(it, AnimeCategory.NONE) },
                modifier = modifier
            )
        }
    }
}