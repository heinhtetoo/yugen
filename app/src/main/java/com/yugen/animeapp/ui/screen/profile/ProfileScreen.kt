package com.yugen.animeapp.ui.screen.profile

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.yugen.animeapp.domain.model.ThemePreference

@Composable
fun ProfileScreen(
    modifier: Modifier = Modifier,
    profileViewModel: ProfileViewModel = hiltViewModel()
) {

    val theme by profileViewModel.theme.collectAsState()

    Column {
        Text(text = "Theme Preference")

        ThemePreference.entries.forEach { themePreference ->
            Row(
                Modifier
                    .fillMaxWidth()
                    .clickable { profileViewModel.updateTheme(themePreference) }
            ) {
                RadioButton(
                    selected = theme == themePreference,
                    onClick = { profileViewModel.updateTheme(themePreference) }
                )
                Text(themePreference.name)
            }
        }
    }
}