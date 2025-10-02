package app.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import app.ui.detailPage.CharacterDetailsScreen
import app.ui.mainPage.CharactersScreen
import app.ui.searchPage.CharacterSearchScreen
import com.example.appakk.R
import com.example.appakk.ui.theme.AppAkkTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            AppAkkTheme {
                val navController =
                    rememberNavController()
                NavHost(
                    navController = navController,
                    startDestination = resources.getString(R.string.main)
                ) {
                    composable(
                        resources.getString(R.string.main)
                    ) {
                        CharactersScreen(navController)
                    }
                    composable(
                        route = "detail/{characterId}",
                        arguments = listOf(navArgument(resources.getString(R.string.characterId)) {
                            type = NavType.IntType
                        })
                    ) { backStackEntry ->
                        val id =
                            backStackEntry.arguments?.getInt(resources.getString(R.string.characterId))
                        requireNotNull(
                            value = id,
                            lazyMessage = { R.string.id_can_not_be_null })
                        CharacterDetailsScreen(id, navController)
                    }
                    composable(
                        route = resources.getString(R.string.search)
                    ) {
                        CharacterSearchScreen(navController)
                    }
                }
            }
        }
    }
}