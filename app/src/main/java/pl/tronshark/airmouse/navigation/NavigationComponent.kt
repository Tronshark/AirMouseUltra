package pl.tronshark.airmouse.navigation

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarDefaults
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import dev.chrisbanes.haze.HazeState
import dev.chrisbanes.haze.hazeEffect
import dev.chrisbanes.haze.hazeSource
import dev.chrisbanes.haze.materials.ExperimentalHazeMaterialsApi
import dev.chrisbanes.haze.rememberHazeState
import pl.tronshark.airmouse.core.common.types.ManualPageType
import pl.tronshark.airmouse.core.ui.theme.AirMouseTheme
import pl.tronshark.airmouse.core.ui.theme.hazeUltraThinSurface
import pl.tronshark.airmouse.screen.home.HomeScreen
import pl.tronshark.airmouse.screen.settings.SettingsScreen

@OptIn(ExperimentalHazeMaterialsApi::class)
@Composable
internal fun AirMouseContent() {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentScreen = rememberScreen(navBackStackEntry?.destination?.route)

    AirMouseScaffold(
        navController = navController,
        currentScreen = currentScreen,
        showManualPageClicked = { _ -> }
    )
}

@Composable
private fun AirMouseScaffold(
    navController: NavHostController,
    currentScreen: Screen,
    modifier: Modifier = Modifier,
    showManualPageClicked: (ManualPageType) -> Unit,
) {
    val navigationHazeState = rememberHazeState()

    Scaffold(
        modifier = modifier.fillMaxSize(),
        bottomBar = {
            AirMouseBottomBar(
                navController = navController,
                currentScreen = currentScreen,
                modifier = Modifier.hazeEffect(
                    state = navigationHazeState,
                    style = hazeUltraThinSurface()
                )
            )
        },
        content = { paddingValues ->
            AirMouseNavigation(
                navController = navController,
                paddingValues = paddingValues,
                navigationHazeState = navigationHazeState,
                showManualPageClicked = showManualPageClicked
            )
        }
    )
}

@Composable
private fun AirMouseNavigation(
    navController: NavHostController,
    paddingValues: PaddingValues,
    navigationHazeState: HazeState,
    showManualPageClicked: (ManualPageType) -> Unit,
) = NavHost(
    navController = navController,
    startDestination = Screen.Home,
    enterTransition = AnimatedContentTransitionScope<NavBackStackEntry>::enterAnimation,
    exitTransition = { ExitTransition.None },
    popEnterTransition = { EnterTransition.None },
    popExitTransition = AnimatedContentTransitionScope<NavBackStackEntry>::exitAnimation
) {
    composable<Screen.Home> {
        HomeScreen(
            paddingValues = paddingValues,
            navigationHazeState = navigationHazeState,
            showManualPageClicked = showManualPageClicked
        )
    }
    composable<Screen.Settings> {
        SettingsScreen(
            paddingValues = paddingValues,
            navigationHazeState = navigationHazeState
        )
    }
}

@Composable
private fun AirMouseBottomBar(
    navController: NavHostController,
    currentScreen: Screen,
    modifier: Modifier = Modifier
) = NavigationBar(
    windowInsets = NavigationBarDefaults.windowInsets,
    containerColor = Color.Transparent,
    modifier = modifier
) {
    Screen.order.forEach { screen ->
        NavigationBarItem(
            selected = screen == currentScreen,
            onClick = { navController.navigateTo(screen) },
            icon = { NavIcon(screen = screen) },
            label = { Text(text = stringResource(screen.titleResId)) }
        )
    }
}

@Composable
private fun NavIcon(screen: Screen) = Icon(
    painter = painterResource(screen.iconResId),
    contentDescription = stringResource(screen.titleResId)
)

@Composable
private fun rememberScreen(currentRoute: String?) = remember(currentRoute) {
    Screen.findFromRoute(currentRoute)
}

private fun NavController.navigateTo(screen: Any) = navigate(screen) {
    launchSingleTop = true
    popUpTo(graph.findStartDestination().id) { saveState = true }
    restoreState = true
}

@Composable
@Preview
private fun AirMousePreview() {
    AirMouseTheme {
        AirMouseContent()
    }
}
