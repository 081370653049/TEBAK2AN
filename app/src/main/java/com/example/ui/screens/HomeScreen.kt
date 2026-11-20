package com.example.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.AutoAwesome
import androidx.compose.material.icons.rounded.CheckCircle
import androidx.compose.material.icons.rounded.MusicNote
import androidx.compose.material.icons.rounded.Pets
import androidx.compose.material.icons.rounded.VolumeOff
import androidx.compose.material.icons.rounded.VolumeUp
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.example.R
import com.example.model.AnimalId
import com.example.model.Difficulty
import com.example.model.GameMode
import com.example.model.Habitat
import com.example.ui.components.AnimalArtwork
import com.example.ui.components.AnimatedStorybookBackground
import com.example.ui.components.GoldenStarPill
import com.example.ui.components.ToyBlockCard
import com.example.ui.theme.CocoaInk
import com.example.ui.theme.CocoaInkMuted
import com.example.ui.theme.CoralBerry
import com.example.ui.theme.CoralBerryDark
import com.example.ui.theme.GrassGreen
import com.example.ui.theme.GrassGreenDark
import com.example.ui.theme.LavenderPop
import com.example.ui.theme.LavenderPopDark
import com.example.ui.theme.MeadowMint
import com.example.ui.theme.MeadowMintDark
import com.example.ui.theme.SplashBlue
import com.example.ui.theme.SplashBlueDark
import com.example.ui.theme.SunburstYellow
import com.example.ui.theme.SunburstYellowDark
import com.example.ui.theme.WarmWhite
import com.example.viewmodel.GameUiState

@Composable
fun HomeScreen(
    uiState: GameUiState,
    onToggleSound: () -> Unit,
    onSelectDifficulty: (Difficulty) -> Unit,
    onSelectHabitat: (Habitat) -> Unit,
    onStartMode: (GameMode) -> Unit
) {
    AnimatedStorybookBackground {
        BoxWithConstraints(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding()
                .navigationBarsPadding(),
            contentAlignment = Alignment.TopCenter
        ) {
            val isWide = maxWidth >= 600.dp

            Column(
                modifier = Modifier
                    .widthIn(max = 760.dp)
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(horizontal = 20.dp, vertical = 14.dp),
                verticalArrangement = Arrangement.spacedBy(18.dp)
            ) {
                // Top Bar: App Title, Stars, and Giant Sound Toggle
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = stringResource(R.string.app_name),
                            style = MaterialTheme.typography.displayMedium,
                            color = CocoaInk
                        )
                        Text(
                            text = stringResource(R.string.app_subtitle),
                            style = MaterialTheme.typography.bodyMedium,
                            color = CocoaInkMuted,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }

                    Row(
                        horizontalArrangement = Arrangement.spacedBy(10.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        GoldenStarPill(
                            stars = uiState.totalStars,
                            label = uiState.totalStars.toString(),
                            modifier = Modifier.testTag("total_stars_badge")
                        )

                        SoundToggleButton(
                            isSoundEnabled = uiState.isSoundEnabled,
                            onClick = onToggleSound,
                            modifier = Modifier.testTag("sound_toggle_button")
                        )
                    }
                }

                // Joyful Hero Storybook Banner
                Surface(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(if (isWide) 188.dp else 158.dp),
                    shape = RoundedCornerShape(28.dp),
                    border = BorderStroke(3.dp, Color.White),
                    shadowElevation = 6.dp
                ) {
                    Box(modifier = Modifier.fillMaxSize()) {
                        Image(
                            painter = painterResource(id = R.drawable.img_hero_safari),
                            contentDescription = stringResource(R.string.hero_banner_desc),
                            contentScale = ContentScale.Crop,
                            modifier = Modifier.fillMaxSize()
                        )
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .background(
                                    Brush.horizontalGradient(
                                        colors = listOf(
                                            Color(0xB82D1E17),
                                            Color(0x552D1E17),
                                            Color.Transparent
                                        )
                                    )
                                )
                        )
                        Row(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(horizontal = 20.dp, vertical = 16.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Column(
                                modifier = Modifier.weight(1f),
                                verticalArrangement = Arrangement.spacedBy(6.dp)
                            ) {
                                Surface(
                                    shape = RoundedCornerShape(50),
                                    color = SunburstYellow
                                ) {
                                    Text(
                                        text = uiState.selectedHabitat.labelId,
                                        style = MaterialTheme.typography.labelMedium,
                                        color = CocoaInk,
                                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp)
                                    )
                                }
                                Text(
                                    text = "Halo Adik Pintar!",
                                    style = MaterialTheme.typography.headlineLarge,
                                    color = Color.White
                                )
                                Text(
                                    text = "Ayo tebak suara & bayangan hewan lucu!",
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = Color.White.copy(alpha = 0.94f)
                                )
                            }

                            AnimalArtwork(
                                animalId = AnimalId.LION,
                                modifier = Modifier.size(92.dp),
                                isCelebrating = false
                            )
                        }
                    }
                }

                // Play Mode Cards Section
                Text(
                    text = stringResource(R.string.mode_section_title),
                    style = MaterialTheme.typography.headlineMedium,
                    color = CocoaInk
                )

                // Primary Giant Play Card: Tebak Suara
                PlayModeBannerCard(
                    title = stringResource(R.string.mode_sound_title),
                    subtitle = stringResource(R.string.mode_sound_desc),
                    icon = Icons.Rounded.MusicNote,
                    animalPreview = AnimalId.CAT,
                    isSilhouettePreview = false,
                    bgColor = CoralBerry,
                    shadowColor = CoralBerryDark,
                    onClick = { onStartMode(GameMode.SOUND_GUESS) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(114.dp)
                        .testTag("mode_sound_card")
                )

                // Secondary Play Cards: Tebak Bayangan & Kenal Hewan
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    CompactModeCard(
                        title = stringResource(R.string.mode_shadow_title),
                        subtitle = stringResource(R.string.mode_shadow_desc),
                        icon = Icons.Rounded.AutoAwesome,
                        animalPreview = AnimalId.RABBIT,
                        isSilhouettePreview = true,
                        bgColor = LavenderPop,
                        shadowColor = LavenderPopDark,
                        onClick = { onStartMode(GameMode.SHADOW_GUESS) },
                        modifier = Modifier
                            .weight(1f)
                            .height(172.dp)
                            .testTag("mode_shadow_card")
                    )

                    CompactModeCard(
                        title = stringResource(R.string.mode_learn_title),
                        subtitle = stringResource(R.string.mode_learn_desc),
                        icon = Icons.Rounded.Pets,
                        animalPreview = AnimalId.DUCK,
                        isSilhouettePreview = false,
                        bgColor = MeadowMint,
                        shadowColor = MeadowMintDark,
                        onClick = { onStartMode(GameMode.LEARN_ANIMALS) },
                        modifier = Modifier
                            .weight(1f)
                            .height(172.dp)
                            .testTag("mode_learn_card")
                    )
                }

                // Difficulty Selector (2 choices for ages 2-3, 4 choices for ages 3-5)
                Surface(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(24.dp),
                    color = WarmWhite.copy(alpha = 0.92f),
                    border = BorderStroke(2.dp, Color(0xFFECDCC0))
                ) {
                    Column(
                        modifier = Modifier.padding(14.dp),
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Text(
                            text = stringResource(R.string.difficulty_label),
                            style = MaterialTheme.typography.titleMedium,
                            color = CocoaInk
                        )
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            DifficultyPill(
                                label = stringResource(R.string.difficulty_easy),
                                selected = uiState.difficulty == Difficulty.EASY_TWO,
                                activeColor = GrassGreen,
                                activeShadow = GrassGreenDark,
                                onClick = { onSelectDifficulty(Difficulty.EASY_TWO) },
                                modifier = Modifier
                                    .weight(1f)
                                    .height(52.dp)
                                    .testTag("difficulty_easy")
                            )
                            DifficultyPill(
                                label = stringResource(R.string.difficulty_fun),
                                selected = uiState.difficulty == Difficulty.FUN_FOUR,
                                activeColor = SplashBlue,
                                activeShadow = SplashBlueDark,
                                onClick = { onSelectDifficulty(Difficulty.FUN_FOUR) },
                                modifier = Modifier
                                    .weight(1f)
                                    .height(52.dp)
                                    .testTag("difficulty_fun")
                            )
                        }
                    }
                }

                // Choose Animal World Section
                Text(
                    text = stringResource(R.string.world_section_title),
                    style = MaterialTheme.typography.headlineMedium,
                    color = CocoaInk
                )

                Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        HabitatWorldCard(
                            habitat = Habitat.ALL,
                            selected = uiState.selectedHabitat == Habitat.ALL,
                            onClick = { onSelectHabitat(Habitat.ALL) },
                            modifier = Modifier
                                .weight(1f)
                                .height(112.dp)
                                .testTag("world_all_card")
                        )
                        HabitatWorldCard(
                            habitat = Habitat.FARM,
                            selected = uiState.selectedHabitat == Habitat.FARM,
                            onClick = { onSelectHabitat(Habitat.FARM) },
                            modifier = Modifier
                                .weight(1f)
                                .height(112.dp)
                                .testTag("world_farm_card")
                        )
                    }
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        HabitatWorldCard(
                            habitat = Habitat.JUNGLE,
                            selected = uiState.selectedHabitat == Habitat.JUNGLE,
                            onClick = { onSelectHabitat(Habitat.JUNGLE) },
                            modifier = Modifier
                                .weight(1f)
                                .height(112.dp)
                                .testTag("world_jungle_card")
                        )
                        HabitatWorldCard(
                            habitat = Habitat.WATER,
                            selected = uiState.selectedHabitat == Habitat.WATER,
                            onClick = { onSelectHabitat(Habitat.WATER) },
                            modifier = Modifier
                                .weight(1f)
                                .height(112.dp)
                                .testTag("world_water_card")
                        )
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))
            }
        }
    }
}

@Composable
fun SoundToggleButton(
    isSoundEnabled: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    ToyBlockCard(
        onClick = onClick,
        backgroundColor = if (isSoundEnabled) SplashBlue else Color(0xFFB5A99B),
        shadowColor = if (isSoundEnabled) SplashBlueDark else Color(0xFF8B8073),
        cornerRadius = 18.dp,
        lipHeight = 4.dp,
        modifier = modifier.size(50.dp)
    ) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Icon(
                imageVector = if (isSoundEnabled) Icons.Rounded.VolumeUp else Icons.Rounded.VolumeOff,
                contentDescription = stringResource(
                    if (isSoundEnabled) R.string.sound_on_desc else R.string.sound_off_desc
                ),
                tint = Color.White,
                modifier = Modifier.size(26.dp)
            )
        }
    }
}

@Composable
private fun PlayModeBannerCard(
    title: String,
    subtitle: String,
    icon: ImageVector,
    animalPreview: AnimalId,
    isSilhouettePreview: Boolean,
    bgColor: Color,
    shadowColor: Color,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    ToyBlockCard(
        onClick = onClick,
        backgroundColor = bgColor,
        shadowColor = shadowColor,
        cornerRadius = 28.dp,
        lipHeight = 7.dp,
        modifier = modifier
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 20.dp, vertical = 14.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                modifier = Modifier.weight(1f),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(54.dp)
                        .clip(CircleShape)
                        .background(Color.White.copy(alpha = 0.24f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = icon,
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.size(32.dp)
                    )
                }
                Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
                    Text(
                        text = title,
                        style = MaterialTheme.typography.headlineMedium,
                        color = Color.White,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = subtitle,
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color.White.copy(alpha = 0.92f),
                        maxLines = 2
                    )
                }
            }

            Spacer(modifier = Modifier.width(8.dp))

            Box(
                modifier = Modifier
                    .size(76.dp)
                    .clip(CircleShape)
                    .background(Color.White.copy(alpha = 0.92f)),
                contentAlignment = Alignment.Center
            ) {
                AnimalArtwork(
                    animalId = animalPreview,
                    isSilhouette = isSilhouettePreview,
                    modifier = Modifier
                        .size(66.dp)
                        .padding(4.dp)
                )
            }
        }
    }
}

@Composable
private fun CompactModeCard(
    title: String,
    subtitle: String,
    icon: ImageVector,
    animalPreview: AnimalId,
    isSilhouettePreview: Boolean,
    bgColor: Color,
    shadowColor: Color,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    ToyBlockCard(
        onClick = onClick,
        backgroundColor = bgColor,
        shadowColor = shadowColor,
        cornerRadius = 26.dp,
        lipHeight = 7.dp,
        modifier = modifier
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(14.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Box(
                modifier = Modifier
                    .size(74.dp)
                    .clip(CircleShape)
                    .background(Color.White.copy(alpha = 0.92f)),
                contentAlignment = Alignment.Center
            ) {
                AnimalArtwork(
                    animalId = animalPreview,
                    isSilhouette = isSilhouettePreview,
                    modifier = Modifier
                        .size(64.dp)
                        .padding(3.dp)
                )
            }

            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Icon(
                        imageVector = icon,
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.size(18.dp)
                    )
                    Text(
                        text = title,
                        style = MaterialTheme.typography.titleLarge,
                        color = Color.White,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
                Text(
                    text = subtitle,
                    style = MaterialTheme.typography.labelMedium,
                    color = Color.White.copy(alpha = 0.90f),
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
    }
}

@Composable
private fun DifficultyPill(
    label: String,
    selected: Boolean,
    activeColor: Color,
    activeShadow: Color,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    ToyBlockCard(
        onClick = onClick,
        backgroundColor = if (selected) activeColor else Color(0xFFF3ECE0),
        shadowColor = if (selected) activeShadow else Color(0xFFD8CCB8),
        cornerRadius = 18.dp,
        lipHeight = 5.dp,
        modifier = modifier
    ) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text(
                text = label,
                style = MaterialTheme.typography.labelLarge,
                color = if (selected) Color.White else CocoaInkMuted
            )
        }
    }
}

@Composable
private fun HabitatWorldCard(
    habitat: Habitat,
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    ToyBlockCard(
        onClick = onClick,
        backgroundColor = habitat.accentColor,
        shadowColor = habitat.shadowColor,
        cornerRadius = 22.dp,
        lipHeight = 5.dp,
        borderColor = if (selected) SunburstYellow else null,
        modifier = modifier
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            habitat.bannerRes?.let { resId ->
                Image(
                    painter = painterResource(id = resId),
                    contentDescription = habitat.labelId,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )
            }
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(
                                Color(0x26000000),
                                Color(0xB8241913)
                            )
                        )
                    )
            )
            if (selected) {
                Box(
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(8.dp)
                        .size(26.dp)
                        .clip(CircleShape)
                        .background(SunburstYellow)
                        .border(2.dp, Color.White, CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Rounded.CheckCircle,
                        contentDescription = null,
                        tint = CocoaInk,
                        modifier = Modifier.size(18.dp)
                    )
                }
            }
            Text(
                text = habitat.labelId,
                style = MaterialTheme.typography.titleMedium,
                color = Color.White,
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .padding(PaddingValues(horizontal = 12.dp, vertical = 10.dp))
            )
        }
    }
}
