package com.example.model

import androidx.annotation.DrawableRes
import androidx.compose.ui.graphics.Color
import com.example.R
import com.example.ui.theme.BubblegumPink
import com.example.ui.theme.BubblegumPinkDark
import com.example.ui.theme.CardCream
import com.example.ui.theme.CardLavender
import com.example.ui.theme.CardLemon
import com.example.ui.theme.CardMint
import com.example.ui.theme.CardPeach
import com.example.ui.theme.CardRose
import com.example.ui.theme.CardSky
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
import com.example.ui.theme.TangerineGlow
import com.example.ui.theme.TangerineGlowDark

enum class Habitat(
    val labelId: String,
    val accentColor: Color,
    val shadowColor: Color,
    @DrawableRes val bannerRes: Int?
) {
    ALL("Semua Hewan", CoralBerry, CoralBerryDark, R.drawable.img_hero_safari),
    FARM("Peternakan", TangerineGlow, TangerineGlowDark, R.drawable.img_pack_peternakan),
    JUNGLE("Hutan Rimba", GrassGreen, GrassGreenDark, R.drawable.img_pack_hutan),
    WATER("Dunia Air", SplashBlue, SplashBlueDark, R.drawable.img_pack_laut)
}

enum class GameMode {
    SOUND_GUESS,
    SHADOW_GUESS,
    LEARN_ANIMALS
}

enum class Difficulty(val optionCount: Int, val label: String) {
    EASY_TWO(2, "Mudah (2)"),
    FUN_FOUR(4, "Seru (4)")
}

enum class AnimalId {
    CAT,
    DOG,
    COW,
    CHICKEN,
    LION,
    ELEPHANT,
    MONKEY,
    RABBIT,
    DUCK,
    FROG,
    FISH,
    WHALE
}

data class Animal(
    val id: AnimalId,
    val name: String,
    val soundDisplay: String,
    val spokenSound: String,
    val shadowClue: String,
    val funFact: String,
    val habitat: Habitat,
    val cardBackground: Color,
    val accentColor: Color,
    val borderColor: Color
)

object AnimalCatalog {
    val allAnimals: List<Animal> = listOf(
        Animal(
            id = AnimalId.CAT,
            name = "Kucing",
            soundDisplay = "Meong! Meong!",
            spokenSound = "Meong, meong!",
            shadowClue = "Aku punya kumis lucu dan suka minum susu!",
            funFact = "Kucing suka bermain bola benang dan tidur siang!",
            habitat = Habitat.FARM,
            cardBackground = CardPeach,
            accentColor = TangerineGlow,
            borderColor = TangerineGlowDark
        ),
        Animal(
            id = AnimalId.DOG,
            name = "Anjing",
            soundDisplay = "Guk! Guk!",
            spokenSound = "Guk, guk, guk!",
            shadowClue = "Aku sahabat setia dan suka mengibas ekor!",
            funFact = "Anjing sangat ceria dan suka bermain lempar bola!",
            habitat = Habitat.FARM,
            cardBackground = CardLemon,
            accentColor = SunburstYellow,
            borderColor = SunburstYellowDark
        ),
        Animal(
            id = AnimalId.COW,
            name = "Sapi",
            soundDisplay = "Mooo! Mooo!",
            spokenSound = "Mooo, mooo!",
            shadowClue = "Tubuhku belang hitam putih dan aku menghasilkan susu!",
            funFact = "Sapi suka makan rumput hijau segar di padang rumput!",
            habitat = Habitat.FARM,
            cardBackground = CardCream,
            accentColor = CoralBerry,
            borderColor = CoralBerryDark
        ),
        Animal(
            id = AnimalId.CHICKEN,
            name = "Ayam",
            soundDisplay = "Kukuruyuk!",
            spokenSound = "Kukuruyuk! Petok, petok!",
            shadowClue = "Aku punya jengger merah dan membangunkan pagi!",
            funFact = "Ayam berkokok setiap pagi saat matahari terbit!",
            habitat = Habitat.FARM,
            cardBackground = CardRose,
            accentColor = BubblegumPink,
            borderColor = BubblegumPinkDark
        ),
        Animal(
            id = AnimalId.LION,
            name = "Singa",
            soundDisplay = "Auum! Roaaar!",
            spokenSound = "Auum! Roaaar!",
            shadowClue = "Aku punya surai lebat seperti matahari dan raja hutan!",
            funFact = "Singa adalah raja hutan yang gagah dan pemberani!",
            habitat = Habitat.JUNGLE,
            cardBackground = CardPeach,
            accentColor = TangerineGlow,
            borderColor = TangerineGlowDark
        ),
        Animal(
            id = AnimalId.ELEPHANT,
            name = "Gajah",
            soundDisplay = "Preeet! Truuut!",
            spokenSound = "Preeet! Truuut!",
            shadowClue = "Hidungku belalai panjang dan telingaku sangat lebar!",
            funFact = "Gajah suka menyemprotkan air dengan belalainya yang panjang!",
            habitat = Habitat.JUNGLE,
            cardBackground = CardLavender,
            accentColor = LavenderPop,
            borderColor = LavenderPopDark
        ),
        Animal(
            id = AnimalId.MONKEY,
            name = "Monyet",
            soundDisplay = "Uu! Aa! Uu! Aa!",
            spokenSound = "Uu aa, uu aa!",
            shadowClue = "Aku pandai memanjat pohon dan suka buah pisang!",
            funFact = "Monyet sangat lincah berayun dari dahan ke dahan!",
            habitat = Habitat.JUNGLE,
            cardBackground = CardLemon,
            accentColor = GrassGreen,
            borderColor = GrassGreenDark
        ),
        Animal(
            id = AnimalId.RABBIT,
            name = "Kelinci",
            soundDisplay = "Hop! Hop!",
            spokenSound = "Hop, hop, hop!",
            shadowClue = "Telingaku panjang sekali dan aku suka makan wortel!",
            funFact = "Kelinci melompat-lompat lucu dan bulunya sangat lembut!",
            habitat = Habitat.JUNGLE,
            cardBackground = CardRose,
            accentColor = BubblegumPink,
            borderColor = BubblegumPinkDark
        ),
        Animal(
            id = AnimalId.DUCK,
            name = "Bebek",
            soundDisplay = "Kwek! Kwek!",
            spokenSound = "Kwek, kwek, kwek!",
            shadowClue = "Warnaku kuning cerah dan aku suka berenang di kolam!",
            funFact = "Bebek berjalan goyang-goyang dan pintar berenang!",
            habitat = Habitat.WATER,
            cardBackground = CardLemon,
            accentColor = SunburstYellow,
            borderColor = SunburstYellowDark
        ),
        Animal(
            id = AnimalId.FROG,
            name = "Katak",
            soundDisplay = "Krok! Krok!",
            spokenSound = "Krok, krok! Kung kong!",
            shadowClue = "Warnaku hijau dan aku suka melompat di daun teratai!",
            funFact = "Katak suka bernyanyi riang saat hujan turun!",
            habitat = Habitat.WATER,
            cardBackground = CardMint,
            accentColor = GrassGreen,
            borderColor = GrassGreenDark
        ),
        Animal(
            id = AnimalId.FISH,
            name = "Ikan",
            soundDisplay = "Blub! Blub!",
            spokenSound = "Blub, blub, blub!",
            shadowClue = "Aku punya sirip berwarna cerah dan berenang di air!",
            funFact = "Ikan bernapas di dalam air dan membuat gelembung lucu!",
            habitat = Habitat.WATER,
            cardBackground = CardSky,
            accentColor = MeadowMint,
            borderColor = MeadowMintDark
        ),
        Animal(
            id = AnimalId.WHALE,
            name = "Paus",
            soundDisplay = "Wuuush! Byuur!",
            spokenSound = "Wuuush! Byuur!",
            shadowClue = "Aku sangat besar di lautan dan bisa menyemburkan air!",
            funFact = "Paus adalah teman raksasa yang ramah di lautan biru!",
            habitat = Habitat.WATER,
            cardBackground = CardSky,
            accentColor = SplashBlue,
            borderColor = SplashBlueDark
        )
    )

    fun animalsByHabitat(habitat: Habitat): List<Animal> {
        return if (habitat == Habitat.ALL) {
            allAnimals
        } else {
            allAnimals.filter { it.habitat == habitat }
        }
    }
}
