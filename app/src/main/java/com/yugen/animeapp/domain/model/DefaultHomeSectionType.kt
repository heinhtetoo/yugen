package com.yugen.animeapp.domain.model

import com.yugen.animeapp.R
import com.yugen.animeapp.core.utils.AWARD_WINNING_ANIME_GENRE_ID
import com.yugen.animeapp.core.utils.TOP_AIRING_ANIME_CUSTOM_GENRE_ID
import com.yugen.animeapp.core.utils.TOP_AIRING_ANIME_FILTER_ENUM
import com.yugen.animeapp.core.utils.TOP_UPCOMING_ANIME_CUSTOM_GENRE_ID
import com.yugen.animeapp.core.utils.TOP_UPCOMING_ANIME_FILTER_ENUM

enum class DefaultHomeSectionType(
    val filterEnumString: String?,
    val genreId: Int,
    val titleRes: Int
) {
    TOP_AIRING(
        filterEnumString = FilterEnum.TOP_AIRING.value,
        genreId = TOP_AIRING_ANIME_CUSTOM_GENRE_ID,
        titleRes = R.string.top_airing_anime
    ),
    TOP_UPCOMING(
        filterEnumString = FilterEnum.TOP_UPCOMING.value,
        genreId = TOP_UPCOMING_ANIME_CUSTOM_GENRE_ID,
        titleRes = R.string.top_upcoming_anime
    ),
    AWARD_WINNING(
        filterEnumString = null,
        genreId = AWARD_WINNING_ANIME_GENRE_ID,
        titleRes = R.string.award_winning_anime
    )
}

enum class FilterEnum(val value: String) {
    TOP_AIRING(TOP_AIRING_ANIME_FILTER_ENUM),
    TOP_UPCOMING(TOP_UPCOMING_ANIME_FILTER_ENUM)
}