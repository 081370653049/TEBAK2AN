package com.example.ui.screens

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Home
import androidx.compose.material.icons.rounded.MusicNote
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.R
import com.example.model.Animal
import com.example.model.AnimalCatalog
import com.example.model.Habitat
import com.example.ui.components.AnimalArtwork
import com.example.ui.components.AnimatedStorybookBackground
import com.example.ui.components.PulsingSpeakerButton
import com.example.ui.components.ToyBlockCard
import com.example.ui.theme.CocoaInk
import com.example.ui.theme.CocoaInkMuted
import com.example.ui.theme.MeadowMint
import com.example.ui.theme.MeadowMintDark
import com.example.ui.theme.SunburstYellow
import com.example.ui.theme.SunburstYellowDark
import com.example.ui.theme.WarmWhite
import com.example.viewmodel.GameUiState

@Composable
fun LearnScreen(
    uiState: GameUiState,
    onBackHome: () -> Unit,
    onToggleSound: () -> Unit,
    onFilterHabitat: (Habitat) -> Unit,
    onAnimalTapped: (Animal) -> Unit
) {
    BackHandler(onBack = onBackHome)

    val learn = uiState.learn
    val animals = AnimalCatalog.animalsByHabitat(learn.selectedHabitat)

    AnimatedStorybookBackground {
        BoxWithConstraints(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding()
                .navigationBarsPadding(),
            contentAlignment = Alignment.TopCenter
        ) {
            val columns = if (maxWidth >= 600.dp) 3 else 2

            Column(
                modifier = Modifier
                    .widthIn(max = 820.dp)
                    .fillMaxSize()
                    .padding(horizontal = 18.dp, vertical = 12.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                // Top Bar
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    ToyBlockCard(
                        onClick = onBackHome,
                        backgroundColor = SunburstYellow,
                        shadowColor = SunburstYellowDark,
                        cornerRadius = 18.dp,
                        lipHeight = 4.dp,
                        modifier = Modifier
                            .size(50.dp)
                            .testTag("learn_back_button")
                    ) {
                        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                            Icon(
                                imageVector = Icons.Rounded.Home,
                                contentDescription = stringResource(R.string.back_button_desc),
                                tint = CocoaInk,
                                modifier = Modifier.size(28.dp)
                            )
                        }
                    }

                    Text(
                        text = stringResource(R.string.mode_learn_title),
                        style = MaterialTheme.typography.headlineLarge,
                        color = CocoaInk
                    )

                    SoundToggleButton(
                        isSoundEnabled = uiState.isSoundEnabled,
                        onClick = onToggleSound,
                        modifier = Modifier.testTag("learn_sound_toggle")
                    )
                }

                // Active Animal Talking Banner
                Surface(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(26.dp),
                    color = WarmWhite,
                    border = BorderStroke(
                        2.5.dp,
                        learn.activeAnimal?.accentColor ?: MeadowMint
                    ),
                    shadowElevation = 4.dp
                ) {
                    AnimatedContent(
                        targetState = learn.activeAnimal,
                        label = "learn_spotlight"
                    ) { active ->
                        if (active == null) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 18.dp, vertical = 16.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(12.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Rounded.MusicNote,
                                    contentDescription = null,
                                    tint = MeadowMint,
                                    modifier = Modifier.size(34.dp)
                                )
                                Text(
                                    text = stringResource(R.string.learn_header_subtitle),
                                    style = MaterialTheme.typography.titleMedium,
                                    color = CocoaInk
                                )
                            }
                        } else {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 16.dp, vertical = 12.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(12.dp)
                            ) {
                                PulsingSpeakerButton(
                                    onClick = { onAnimalTapped(active) },
                                    contentDescription = active.name,
                                    buttonSize = 56.dp,
                                    containerColor = active.accentColor,
                                    shadowColor = active.borderColor
                                )
                                Column(
                                    modifier = Modifier.weight(1f),
                                    verticalArrangement = Arrangement.spacedBy(2.dp)
                                ) {
                                    Text(
                                        text = "${active.name} — \"${active.soundDisplay}\"",
                                        style = MaterialTheme.typography.titleLarge,
                                        color = CocoaInk
                                    )
                                    Text(
                                        text = active.funFact,
                                        style = MaterialTheme.typography.bodyMedium,
                                        color = CocoaInkMuted
                                    )
                                }
                            }
                        }
                    }
                }

                // Habitat Filter Chips
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .horizontalScroll(rememberScrollState()),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Habitat.entries.forEach { habitat ->
                        val isSelected = learn.selectedHabitat == habitat
                        ToyBlockCard(
                            onClick = { onFilterHabitat(habitat) },
                            backgroundColor = if (isSelected) habitat.accentColor else WarmWhite,
                            shadowColor = if (isSelected) habitat.shadowColor else Color(0xFFD9CDB8),
                            cornerRadius = 18.dp,
                            lipHeight = 4.dp,
                            modifier = Modifier
                                .height(46.dp)
                                .testTag("learn_filter_${habitat.name.lowercase()}")
                        ) {
                            Box(
                                modifier = Modifier.padding(horizontal = 16.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = habitat.labelId,
                                    style = MaterialTheme.typography.labelLarge,
                                    color = if (isSelected) Color.White else CocoaInkMuted
                                )
                            }
                        }
                    }
                }

                // Interactive Animal Grid
                LazyVerticalGrid(
                    columns = GridCells.Fixed(columns),
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    contentPadding = PaddingValues(bottom = 18.dp),
                    horizontalArrangement = Arrangement.spacedBy(14.dp),
                    verticalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    items(animals, key = { it.id.name }) { animal ->
                        val isActive = learn.activeAnimal?.id == animal.id
                        ToyBlockCard(
                            onClick = { onAnimalTapped(animal) },
                            backgroundColor = animal.cardBackground,
                            shadowColor = if (isActive) MeadowMintDark else animal.borderColor,
                            borderColor = if (isActive) MeadowMint else Color.White,
                            cornerRadius = 26.dp,
                            lipHeight = 6.dp,
                            modifier = Modifier
                                .fillMaxWidth()
                                .aspectRatio(0.95f)
                                .testTag("learn_animal_${animal.id.name.lowercase()}")
                        ) {
                            Column(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .padding(12.dp),
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.SpaceBetween
                            ) {
                                AnimalArtwork(
                                    animalId = animal.id,
                                    isCelebrating = isActive,
                                    isActiveBounce = isActive,
                                    modifier = Modifier
                                        .weight(1f)
                                        .fillMaxWidth()
                                        .padding(4.dp)
                                )
                                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                    Surface(
                                        shape = RoundedCornerShape(14.dp),
                                        color = Color.White.copy(alpha = 0.90f)
                                    ) {
                                        Text(
                                            text = animal.name,
                                            style = MaterialTheme.typography.titleLarge,
                                            color = CocoaInk,
                                            textAlign = TextAlign.Center,
                                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 2.dp)
                                        )
                                    }
                                    Text(
                                        text = animal.soundDisplay,
                                        style = MaterialTheme.typography.labelMedium,
                                        color = CocoaInkMuted,
                                        textAlign = TextAlign.Center
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
