package com.yugen.animeapp.domain.model

enum class LibraryFilter(val label: String) {
    FAVOURITE("Favourites"),
    PLANNED_TO_WATCH("Plan to Watch"),
    WATCHING("Watching"),
    COMPLETED("Completed");

    fun toWatchStatus(): WatchStatus? {
        return when (this) {
            FAVOURITE -> null
            PLANNED_TO_WATCH -> WatchStatus.PLAN_TO_WATCH
            WATCHING -> WatchStatus.WATCHING
            COMPLETED -> WatchStatus.COMPLETED
        }
    }
}