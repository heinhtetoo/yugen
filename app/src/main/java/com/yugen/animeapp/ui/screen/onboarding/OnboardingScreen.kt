package com.yugen.animeapp.ui.screen.onboarding

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.ShapeDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.yugen.animeapp.R
import com.yugen.animeapp.core.utils.USERNAME_CHARACTER_LIMIT
import com.yugen.animeapp.domain.model.AnimeGenre
import com.yugen.animeapp.domain.model.OnboardingStep
import com.yugen.animeapp.domain.model.ThemePreference

@Composable
fun OnboardingScreen(
    navigateToHome: () -> Unit,
    onboardingViewModel: OnboardingViewModel = hiltViewModel()
) {

    val step = onboardingViewModel.currentStep
    val username by onboardingViewModel.username.collectAsState()
    val selectedTheme by onboardingViewModel.selectedTheme.collectAsState()
    val genreList by onboardingViewModel.genreList.collectAsState()
    val selectedGenreIds by onboardingViewModel.selectedGenreIds.collectAsState()

    Scaffold(
        topBar = {
            StepIndicator(currentStep = step, totalSteps = OnboardingStep.entries.size)
        },
        bottomBar = {
            OnboardingBottomBar(
                isFirstStep = step == OnboardingStep.USERNAME_SETUP,
                isLastStep = step == OnboardingStep.GENRE_SELECTION,
                canContinue = if (step == OnboardingStep.USERNAME_SETUP) username.isNotBlank() else if (step == OnboardingStep.GENRE_SELECTION) selectedGenreIds.isNotEmpty() else true,
                onContinue = {
                    onboardingViewModel.nextStep()
                    if (step == OnboardingStep.GENRE_SELECTION) navigateToHome()
                },
                onBack = {
                    onboardingViewModel.previousStep()
                }
            )
        }
    ) { padding ->
        AnimatedContent(
            targetState = step,
            modifier = Modifier.padding(paddingValues = padding),
            label = "OnboardingTransition"
        ) { targetStep ->
            when (targetStep) {
                OnboardingStep.USERNAME_SETUP -> UsernameStepScreen(
                    username = username,
                    onUsernameChanged = onboardingViewModel::onUsernameChanged
                )

                OnboardingStep.THEME_SELECTION -> ThemeStepScreen(
                    selectedTheme = selectedTheme,
                    onThemeSelected = onboardingViewModel::onThemeSelected
                )

                OnboardingStep.GENRE_SELECTION -> GenreStepScreen(
                    genreList = genreList,
                    selectedGenreIds = selectedGenreIds,
                    onGenreToggled = onboardingViewModel::onGenreToggled
                )
            }
        }
    }
}

@Composable
fun UsernameStepScreen(
    username: String,
    onUsernameChanged: (String) -> Unit
) {
    var tempName by remember(username) { mutableStateOf(username) }
    val focusRequester = remember { FocusRequester() }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(dimensionResource(R.dimen.padding_medium))
    ) {
        Text(
            text = "What should we call you?",
            style = MaterialTheme.typography.headlineMedium
        )

        Spacer(
            modifier = Modifier.height(dimensionResource(R.dimen.padding_small))
        )

        Row(
            verticalAlignment = Alignment.Bottom
        ) {
            Text("Call me", style = MaterialTheme.typography.bodyLarge)

            Spacer(
                modifier = Modifier.width(dimensionResource(R.dimen.padding_small))
            )

            Box(modifier = Modifier.weight(1f)) {
                BasicTextField(
                    value = tempName,
                    onValueChange = {
                        if (it.length <= USERNAME_CHARACTER_LIMIT) {
                            tempName = it
                            onUsernameChanged(tempName)
                        }
                    },
                    maxLines = 2,
                    textStyle = MaterialTheme.typography.headlineMedium.copy(
                        color = MaterialTheme.colorScheme.onSurface,
                        fontWeight = FontWeight.Light,
                        fontStyle = FontStyle.Italic,
                        textDecoration = TextDecoration.Underline
                    ),
                    cursorBrush = SolidColor(MaterialTheme.colorScheme.primary),
                    modifier = Modifier.focusRequester(focusRequester)
                )

                if (tempName.isEmpty()) {
                    Text(
                        text = stringResource(R.string.app_name),
                        style = MaterialTheme.typography.headlineMedium.copy(
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f),
                            fontWeight = FontWeight.Light,
                            fontStyle = FontStyle.Italic,
                            textDecoration = TextDecoration.Underline
                        )
                    )
                }
            }
        }
    }

    LaunchedEffect(Unit) {
        focusRequester.requestFocus()
    }
}

@Composable
fun ThemeStepScreen(
    selectedTheme: ThemePreference,
    onThemeSelected: (ThemePreference) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(dimensionResource(R.dimen.padding_medium))
    ) {
        Text("Choose your Style", style = MaterialTheme.typography.headlineMedium)

        Spacer(
            modifier = Modifier.height(dimensionResource(R.dimen.padding_large))
        )

        ThemeOptionCard(
            title = stringResource(R.string.light_mode),
            isSelected = selectedTheme == ThemePreference.LIGHT,
            onClick = { onThemeSelected(ThemePreference.LIGHT) }
        )

        ThemeOptionCard(
            title = stringResource(R.string.dark_mode),
            isSelected = selectedTheme == ThemePreference.DARK,
            onClick = { onThemeSelected(ThemePreference.DARK) }
        )

        ThemeOptionCard(
            title = stringResource(R.string.system_default),
            isSelected = selectedTheme == ThemePreference.SYSTEM,
            onClick = { onThemeSelected(ThemePreference.SYSTEM) }
        )
    }
}

@Composable
fun GenreStepScreen(
    genreList: List<AnimeGenre>,
    selectedGenreIds: Set<Int>,
    onGenreToggled: (Int, Int) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(dimensionResource(R.dimen.padding_medium))
    ) {
        Text(
            text = "What do you like?",
            style = MaterialTheme.typography.headlineMedium
        )
        Text(
            text = "Pick categories to personalize your feed.",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        Spacer(modifier = Modifier.height(dimensionResource(R.dimen.padding_medium)))

        Column(modifier = Modifier.verticalScroll(rememberScrollState())) {
            FlowRow(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.padding_small)),
                verticalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.padding_small)),
                maxItemsInEachRow = Int.MAX_VALUE
            ) {
                genreList.forEach { genre ->
                    val isSelected = selectedGenreIds.contains(genre.id)

                    GenreChip(
                        name = genre.name,
                        isSelected = isSelected,
                        isEnabled = selectedGenreIds.size < 5,
                        onClick = { onGenreToggled(genre.id, 5) },
                        modifier = Modifier
                            .defaultMinSize(dimensionResource(R.dimen.genre_chip_width_min))
                            .height(dimensionResource(R.dimen.genre_chip_height))
                    )
                }
            }
        }
    }
}

@Composable
fun StepIndicator(
    currentStep: OnboardingStep,
    totalSteps: Int,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(
                top = dimensionResource(R.dimen.padding_medium),
                bottom = dimensionResource(R.dimen.padding_large)
            ),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        repeat(totalSteps) { index ->
            val isSelected = index == currentStep.ordinal

            val width by animateDpAsState(
                targetValue = dimensionResource(if (isSelected) R.dimen.step_indicator_width_selected else R.dimen.step_indicator_width_unselected),
                label = "dot_width"
            )

            val colour by animateColorAsState(
                targetValue = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surfaceVariant,
                label = "dot_colour"
            )

            Box(
                modifier = Modifier
                    .padding(dimensionResource(R.dimen.padding_xsmall))
                    .height(dimensionResource(R.dimen.step_indicator_height))
                    .width(width)
                    .clip(CircleShape)
                    .background(colour)
            )
        }
    }
}

@Composable
fun OnboardingBottomBar(
    isFirstStep: Boolean,
    isLastStep: Boolean,
    canContinue: Boolean,
    onContinue: () -> Unit,
    onBack: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(dimensionResource(R.dimen.padding_large))
            .navigationBarsPadding(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        if (!isFirstStep) {
            TextButton(onClick = onBack) {
                Text(stringResource(R.string.back_button))
            }
        } else {
            Spacer(modifier = Modifier.width(dimensionResource(R.dimen.padding_large)))
        }

        Button(
            onClick = onContinue,
            enabled = canContinue,
            modifier = Modifier.height(dimensionResource(R.dimen.padding_2xlarge))
        ) {
            Text(stringResource(if (isLastStep) R.string.get_started else R.string.continue_button))

            Spacer(modifier = Modifier.width(dimensionResource(R.dimen.padding_small)))

            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                contentDescription = null
            )
        }
    }
}

@Composable
fun ThemeOptionCard(
    title: String,
    isSelected: Boolean,
    onClick: () -> Unit,
    icon: ImageVector? = null
) {
    val borderColour by animateColorAsState(
        targetValue = if (isSelected) MaterialTheme.colorScheme.primary else Color.Transparent,
        label = "border_colour"
    )

    val containerColour by animateColorAsState(
        targetValue = if (isSelected) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.surface,
        label = "container_colour"
    )

    Card(
        onClick = onClick,
        colors = CardDefaults.cardColors(containerColor = containerColour),
        border = BorderStroke(dimensionResource(R.dimen.padding_2xsmall), borderColour),
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = dimensionResource(R.dimen.padding_small))
            .height(dimensionResource(R.dimen.theme_option_card_height))
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = dimensionResource(R.dimen.padding_medium)),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start
        ) {
            RadioButton(
                selected = isSelected,
                onClick = null
            )

            Spacer(modifier = Modifier.width(dimensionResource(R.dimen.padding_medium)))

            if (icon != null) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onSurface
                )
                Spacer(modifier = Modifier.width(dimensionResource(R.dimen.padding_medium)))
            }

            Text(
                title,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
            )
        }
    }
}

@Composable
fun GenreChip(
    name: String,
    isSelected: Boolean,
    isEnabled: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    FilterChip(
        selected = isSelected,
        enabled = isEnabled || isSelected,
        onClick = { if (isEnabled || isSelected) onClick() },
        label = { Text(name) },
        leadingIcon = if (isSelected) {
            { Icon(imageVector = Icons.Filled.CheckCircle, contentDescription = null) }
        } else null,
        shape = ShapeDefaults.Medium,
        modifier = modifier
    )
}

@Preview
@Composable
private fun StepIndicatorPreview() {
    StepIndicator(currentStep = OnboardingStep.THEME_SELECTION, totalSteps = 2)
}

@Preview
@Composable
private fun OnboardingBottomBarPreview() {
    OnboardingBottomBar(false, true, true, {}, {})
}

@Preview(showBackground = true)
@Composable
private fun UsernameStepScreenPreview() {
    UsernameStepScreen("") { }
}

@Preview(showBackground = true)
@Composable
private fun ThemeStepScreenPreview() {
    ThemeStepScreen(ThemePreference.LIGHT) { }
}

@Preview(showBackground = true)
@Composable
private fun GenreStepScreenPreview() {
    GenreStepScreen(
        listOf(
            AnimeGenre(1, "TV", "Yugen", 1),
            AnimeGenre(2, "TV", "Yugen", 2),
            AnimeGenre(3, "TV", "Yugen", 3)
        ),
        setOf(1, 2)
    ) { _, _ -> }
}