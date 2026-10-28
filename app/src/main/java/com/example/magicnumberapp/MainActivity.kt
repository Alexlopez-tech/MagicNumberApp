package com.example.magicnumberapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.magicnumberapp.data.model.Difficulty
import com.example.magicnumberapp.ui.screens.AddRiddleScreen
import com.example.magicnumberapp.ui.screens.GameScreen
import com.example.magicnumberapp.ui.screens.HistoryScreen
import com.example.magicnumberapp.ui.screens.MenuScreen
import com.example.magicnumberapp.ui.screens.ResultScreen
import com.example.magicnumberapp.ui.theme.MagicNumberAppTheme
import com.example.magicnumberapp.ui.viewmodel.AddRiddleViewModel
import com.example.magicnumberapp.ui.viewmodel.GameViewModel
import com.example.magicnumberapp.ui.viewmodel.HistoryViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MagicNumberAppTheme {
                AdivinaloApp()
            }
        }
    }
}

private object Routes {
    const val MENU = "menu"
    const val GAME = "game/{difficulty}"
    const val RESULT = "result"
    const val ADD_RIDDLE = "add_riddle"
    const val HISTORY = "history"

    fun game(difficulty: Difficulty): String = "game/${difficulty.name}"
}

@Composable
private fun AdivinaloApp() {
    val context = LocalContext.current
    val app = context.applicationContext as AdivinaloApplication

    val gameViewModel: GameViewModel = viewModel(
        factory = GameViewModel.factory(app.repository)
    )
    val addRiddleViewModel: AddRiddleViewModel = viewModel(
        factory = AddRiddleViewModel.factory(app.repository)
    )
    val historyViewModel: HistoryViewModel = viewModel(
        factory = HistoryViewModel.factory(app.repository)
    )

    val gameState by gameViewModel.uiState.collectAsStateWithLifecycle()
    val addRiddleState by addRiddleViewModel.uiState.collectAsStateWithLifecycle()
    val historyState by historyViewModel.uiState.collectAsStateWithLifecycle()

    val navController = rememberNavController()

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        NavHost(
            navController = navController,
            startDestination = Routes.MENU
        ) {
        composable(Routes.MENU) {
            MenuScreen(
                onStartGame = { difficulty ->
                    navController.navigate(Routes.game(difficulty))
                },
                onAddRiddle = { navController.navigate(Routes.ADD_RIDDLE) },
                onShowHistory = { navController.navigate(Routes.HISTORY) }
            )
        }

        composable(
            route = Routes.GAME,
            arguments = listOf(
                navArgument("difficulty") {
                    type = NavType.StringType
                    defaultValue = Difficulty.MEDIUM.name
                }
            )
        ) { backStackEntry ->
            val difficulty = runCatching {
                Difficulty.valueOf(
                    backStackEntry.arguments?.getString("difficulty")
                        ?: Difficulty.MEDIUM.name
                )
            }.getOrDefault(Difficulty.MEDIUM)

            LaunchedEffect(difficulty) {
                gameViewModel.startGame(difficulty)
            }

            GameScreen(
                state = gameState,
                onSelectOption = gameViewModel::selectOption,
                onConfirm = gameViewModel::confirmAnswer,
                onNext = gameViewModel::nextRiddle,
                onRevealHint = gameViewModel::revealHint,
                onFinish = {
                    gameViewModel.finishGame()
                    navController.navigate(Routes.RESULT) {
                        popUpTo(Routes.GAME) { inclusive = true }
                    }
                },
                onExit = { navController.popBackStack() }
            )
        }

        composable(Routes.RESULT) {
            ResultScreen(
                result = gameState.result,
                hintsUsed = gameState.hintsUsed,
                onPlayAgain = {
                    navController.navigate(Routes.game(gameState.difficulty)) {
                        popUpTo(Routes.MENU) { inclusive = false }
                        launchSingleTop = true
                    }
                },
                onMenu = {
                    navController.popBackStack(Routes.MENU, inclusive = false)
                }
            )
        }

        composable(Routes.ADD_RIDDLE) {
            AddRiddleScreen(
                state = addRiddleState,
                onQuestionChange = addRiddleViewModel::updateQuestion,
                onHintChange = addRiddleViewModel::updateHint,
                onOptionChange = addRiddleViewModel::updateOption,
                onCorrectOptionChange = addRiddleViewModel::selectCorrectOption,
                onDifficultyChange = addRiddleViewModel::updateDifficulty,
                onSave = addRiddleViewModel::save,
                onBack = { navController.popBackStack() },
                onSaved = { navController.popBackStack() }
            )
        }

        composable(Routes.HISTORY) {
            HistoryScreen(
                state = historyState,
                onBack = { navController.popBackStack() }
            )
        }
    }
    }
}
