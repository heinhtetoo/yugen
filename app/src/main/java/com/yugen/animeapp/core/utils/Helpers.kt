package com.yugen.animeapp.core.utils

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.AddCircleOutline
import androidx.compose.material.icons.rounded.CheckCircle
import androidx.compose.material.icons.rounded.DateRange
import androidx.compose.material.icons.rounded.PlayCircleFilled
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import com.yugen.animeapp.R
import com.yugen.animeapp.domain.model.WatchStatus

fun getAvatarDrawableRes(animeCount: Int): Int = when {
    animeCount >= AVATAR_THRESHOLD_LVL3 -> R.drawable.avatar_lvl3
    animeCount >= AVATAR_THRESHOLD_LVL2 -> R.drawable.avatar_lvl2
    animeCount >= AVATAR_THRESHOLD_LVL1 -> R.drawable.avatar_lvl1
    else -> R.drawable.ic_yugen_greyscale_24
}

fun getAvatarStringRes(animeCount: Int): Int = when {
    animeCount >= AVATAR_THRESHOLD_LVL3 -> R.string.avatar_lvl3
    animeCount >= AVATAR_THRESHOLD_LVL2 -> R.string.avatar_lvl2
    animeCount >= AVATAR_THRESHOLD_LVL1 -> R.string.avatar_lvl1
    else -> R.string.avatar_unknown
}

fun getWatchStatusLabelRes(status: WatchStatus?): Int = when (status) {
    WatchStatus.PLAN_TO_WATCH -> R.string.planned_to_watch
    WatchStatus.WATCHING -> R.string.watching
    WatchStatus.COMPLETED -> R.string.completed
    null -> R.string.add_to_library
}

fun getWatchStatusIcon(status: WatchStatus?): ImageVector = when (status) {
    WatchStatus.PLAN_TO_WATCH -> Icons.Rounded.DateRange
    WatchStatus.WATCHING -> Icons.Rounded.PlayCircleFilled
    WatchStatus.COMPLETED -> Icons.Rounded.CheckCircle
    null -> Icons.Rounded.AddCircleOutline
}

fun getWatchStatusColour(status: WatchStatus?): Color = when (status) {
    WatchStatus.PLAN_TO_WATCH -> Color(0xFFFFC107)
    WatchStatus.WATCHING -> Color(0xFF4CAF50)
    WatchStatus.COMPLETED -> Color(0xFF2196F3)
    null -> Color.Transparent
}
