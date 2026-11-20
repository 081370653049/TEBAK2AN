package com.example.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.audio.IndonesianSoundManager
import com.example.data.UserPrefsRepository
import com.example.model.Animal
import com.example.model.AnimalCatalog
import com.example.model.AnimalId
import com.example.model.Difficulty
import com.example.model.GameMode
import com.example.model.Habitat
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

sealed interface AppScreen {
    data object Home : AppScreen
    data object Quiz : AppScreen
    data object Learn : AppScreen
    data object Victory : AppScreen
}

data class QuizQuestion(
    val target: Animal,
    val options: List<Animal>
)

data class QuizState(
    val mode: GameMode = GameMode.SOUND_GUESS,
    val habitat: Habitat = Habitat.ALL,
    val questions: List<QuizQuestion> = emptyList(),
    val currentIndex: Int = 0,
    val wrongAttemptIds: Set<AnimalId> = emptySet(),
    val lastWrongAnimalId: AnimalId? = null,
    val wrongShakeTrigger: Int = 0,
    val isCorrectRevealed: Boolean = false,
    val roundStars: Int = 0,
    val feedbackText: String? = null,
    val isFeedbackPositive: Boolean = true,
    val celebrationTrigger: Int = 0
) {
    val currentQuestion: QuizQuestion?
        get() = questions.getOrNull(currentIndex)

    val totalQuestions: Int
        get() = questions.size
}

data class LearnState(
    val selectedHabitat: Habitat = Habitat.ALL,
    val activeAnimal: Animal? = null,
    val bounceTrigger: Int = 0
)

data class GameUiState(
    val currentScreen: AppScreen = AppScreen.Home,
    val selectedHabitat: Habitat = Habitat.ALL,
    val difficulty: Difficulty = Difficulty.EASY_TWO,
    val isSoundEnabled: Boolean = true,
    val totalStars: Int = 0,
    val quiz: QuizState = QuizState(),
    val learn: LearnState = LearnState()
)

class GameViewModel(application: Application) : AndroidViewModel(application) {

    private val prefsRepository = UserPrefsRepository(application.applicationContext)
    private val soundManager = IndonesianSoundManager(application.applicationContext)

    private val _uiState = MutableStateFlow(GameUiState())
    val uiState: StateFlow<GameUiState> = _uiState.asStateFlow()

    private var autoAdvanceJob: Job? = null
    private var hasSpokenWelcome = false

    init {
        viewModelScope.launch {
            prefsRepository.preferencesFlow.collect { prefs ->
                soundManager.updateSoundEnabled(prefs.isSoundEnabled)
                _uiState.update {
                    it.copy(
                        isSoundEnabled = prefs.isSoundEnabled,
                        difficulty = prefs.difficulty,
                        totalStars = prefs.totalStars
                    )
                }
                if (!hasSpokenWelcome) {
                    hasSpokenWelcome = true
                    if (prefs.isSoundEnabled) {
                        delay(450L)
                        if (_uiState.value.currentScreen is AppScreen.Home) {
                            soundManager.speakWelcome()
                        }
                    }
                }
            }
        }
    }

    fun toggleSound() {
        val newValue = !_uiState.value.isSoundEnabled
        soundManager.updateSoundEnabled(newValue)
        if (newValue) {
            soundManager.playPop()
        }
        viewModelScope.launch {
            prefsRepository.setSoundEnabled(newValue)
        }
    }

    fun selectDifficulty(difficulty: Difficulty) {
        soundManager.playPop()
        viewModelScope.launch {
            prefsRepository.setDifficulty(difficulty)
        }
    }

    fun selectHabitat(habitat: Habitat) {
        soundManager.playPop()
        _uiState.update { it.copy(selectedHabitat = habitat) }
        soundManager.speak("Dunia ${habitat.labelId}!")
    }

    fun startMode(mode: GameMode, habitatOverride: Habitat? = null) {
        autoAdvanceJob?.cancel()
        soundManager.playPop()

        val chosenHabitat = habitatOverride ?: _uiState.value.selectedHabitat

        if (mode == GameMode.LEARN_ANIMALS) {
            _uiState.update {
                it.copy(
                    currentScreen = AppScreen.Learn,
                    selectedHabitat = chosenHabitat,
                    learn = LearnState(selectedHabitat = chosenHabitat)
                )
            }
            soundManager.speak("Ayo kenali hewan-hewan lucu! Sentuh gambarnya ya!")
            return
        }

        val questions = buildQuestions(
            habitat = chosenHabitat,
            optionCount = _uiState.value.difficulty.optionCount
        )

        _uiState.update {
            it.copy(
                currentScreen = AppScreen.Quiz,
                selectedHabitat = chosenHabitat,
                quiz = QuizState(
                    mode = mode,
                    habitat = chosenHabitat,
                    questions = questions,
                    currentIndex = 0
                )
            )
        }

        questions.firstOrNull()?.let { firstQuestion ->
            speakQuestionPrompt(mode, firstQuestion.target)
        }
    }

    fun replayCurrentQuestion() {
        val quiz = _uiState.value.quiz
        val target = quiz.currentQuestion?.target ?: return
        soundManager.playPop()
        speakQuestionPrompt(quiz.mode, target)
    }

    fun onAnswerSelected(selected: Animal) {
        val state = _uiState.value
        val quiz = state.quiz
        val currentQ = quiz.currentQuestion ?: return

        // Ignore taps if already celebrating correct answer
        if (quiz.isCorrectRevealed) return

        if (selected.id == currentQ.target.id) {
            val newStars = quiz.roundStars + 1
            _uiState.update {
                it.copy(
                    quiz = it.quiz.copy(
                        isCorrectRevealed = true,
                        roundStars = newStars,
                        feedbackText = "Horeee! Benar! Itu ${selected.name} (${selected.soundDisplay})",
                        isFeedbackPositive = true,
                        celebrationTrigger = it.quiz.celebrationTrigger + 1
                    )
                )
            }
            soundManager.speakCorrectCelebration(selected)
            viewModelScope.launch {
                prefsRepository.addStars(1)
            }

            // Gentle automatic advance after celebration, while also offering instant "Lanjut!" tap
            autoAdvanceJob?.cancel()
            autoAdvanceJob = viewModelScope.launch {
                delay(3200L)
                if (_uiState.value.currentScreen is AppScreen.Quiz &&
                    _uiState.value.quiz.isCorrectRevealed
                ) {
                    advanceToNextQuestion()
                }
            }
        } else {
            _uiState.update {
                it.copy(
                    quiz = it.quiz.copy(
                        wrongAttemptIds = it.quiz.wrongAttemptIds + selected.id,
                        lastWrongAnimalId = selected.id,
                        wrongShakeTrigger = it.quiz.wrongShakeTrigger + 1,
                        feedbackText = "Itu ${selected.name}! Yuk coba lagi ya",
                        isFeedbackPositive = false
                    )
                )
            }
            soundManager.speakGentleTryAgain(selected)
        }
    }

    fun advanceToNextQuestion() {
        autoAdvanceJob?.cancel()
        val quiz = _uiState.value.quiz
        val nextIndex = quiz.currentIndex + 1

        if (nextIndex >= quiz.questions.size) {
            _uiState.update { it.copy(currentScreen = AppScreen.Victory) }
            soundManager.speakVictory(quiz.roundStars)
        } else {
            val nextQ = quiz.questions[nextIndex]
            _uiState.update {
                it.copy(
                    quiz = it.quiz.copy(
                        currentIndex = nextIndex,
                        wrongAttemptIds = emptySet(),
                        lastWrongAnimalId = null,
                        isCorrectRevealed = false,
                        feedbackText = null
                    )
                )
            }
            speakQuestionPrompt(quiz.mode, nextQ.target)
        }
    }

    fun playAgain() {
        val quiz = _uiState.value.quiz
        startMode(quiz.mode, quiz.habitat)
    }

    fun onLearnFilterSelected(habitat: Habitat) {
        soundManager.playPop()
        _uiState.update {
            it.copy(
                learn = it.learn.copy(
                    selectedHabitat = habitat,
                    activeAnimal = null
                )
            )
        }
    }

    fun onLearnAnimalTapped(animal: Animal) {
        _uiState.update {
            it.copy(
                learn = it.learn.copy(
                    activeAnimal = animal,
                    bounceTrigger = it.learn.bounceTrigger + 1
                )
            )
        }
        soundManager.speakAnimalLearn(animal)
    }

    fun navigateHome() {
        autoAdvanceJob?.cancel()
        soundManager.stopSpeaking()
        soundManager.playPop()
        _uiState.update {
            it.copy(currentScreen = AppScreen.Home)
        }
    }

    private fun speakQuestionPrompt(mode: GameMode, animal: Animal) {
        when (mode) {
            GameMode.SOUND_GUESS -> soundManager.speakSoundQuestion(animal)
            GameMode.SHADOW_GUESS -> soundManager.speakShadowQuestion(animal)
            GameMode.LEARN_ANIMALS -> soundManager.speakAnimalLearn(animal)
        }
    }

    private fun buildQuestions(habitat: Habitat, optionCount: Int): List<QuizQuestion> {
        val primaryPool = AnimalCatalog.animalsByHabitat(habitat).shuffled()
        val roundLength = if (habitat == Habitat.ALL) 5 else primaryPool.size.coerceAtMost(4)
        val targets = primaryPool.take(roundLength)

        val distractorUniverse = if (primaryPool.size >= optionCount) {
            primaryPool
        } else {
            AnimalCatalog.allAnimals
        }

        return targets.map { target ->
            val distractors = distractorUniverse
                .filter { it.id != target.id }
                .shuffled()
                .take((optionCount - 1).coerceAtLeast(1))
            val options = (distractors + target).shuffled()
            QuizQuestion(target = target, options = options)
        }
    }

    override fun onCleared() {
        super.onCleared()
        autoAdvanceJob?.cancel()
        soundManager.release()
    }
}
