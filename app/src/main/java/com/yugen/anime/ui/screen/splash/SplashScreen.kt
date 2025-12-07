package com.yugen.anime.ui.screen.splash

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.yugen.anime.R
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(
    navigateToOnboarding: () -> Unit,
    navigateToHome: () -> Unit,
    splashViewModel: SplashViewModel = hiltViewModel()
) {

    val isOnboardingCompleted by splashViewModel.isOnboardingCompleted.collectAsState()

    LaunchedEffect(Unit) {
        delay(2_000)
        if (isOnboardingCompleted) navigateToHome() else navigateToOnboarding()
    }

    AppLogo()
}

@Composable
fun AppLogo(modifier: Modifier = Modifier) {
    Row(
        modifier = Modifier
            .fillMaxSize()
            .padding(dimensionResource(R.dimen.padding_large)),
        horizontalArrangement = Arrangement.SpaceAround,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            painter = painterResource(R.drawable.ic_launcher_foreground),
            contentDescription = stringResource(R.string.app_name),
            modifier = Modifier
                .size(dimensionResource(R.dimen.icon_size_splash))
        )

        Text(
            stringResource(R.string.app_name),
            style = MaterialTheme.typography.headlineSmall,
            color = MaterialTheme.colorScheme.onBackground,
            modifier = Modifier.weight(1f)
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun AppLogoPreview() {
    AppLogo()
}