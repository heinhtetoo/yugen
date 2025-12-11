package com.yugen.animeapp.ui.screen.home

import com.yugen.animeapp.domain.model.DefaultHomeSectionType

sealed interface HomeSectionConfig {
    val genreId: Int

    data class Default(
        override val genreId: Int,
        val titleRes: Int,
        val type: DefaultHomeSectionType
    ) : HomeSectionConfig

    data class UserGenre(
        override val genreId: Int,
        val title: String
    ) : HomeSectionConfig
}