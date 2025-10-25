package com.yugen.anime

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import com.yugen.anime.core.ui.theme.YugenTheme
import com.yugen.anime.ui.navigation.YugenNavHost

@Composable
fun MyApp() {
    val navController = rememberNavController()
    YugenTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            YugenNavHost(navController)
        }
    }
}