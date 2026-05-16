package com.aurabeat.presentation.ui.navigation

import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.MusicNote
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navDeepLink
import com.aurabeat.presentation.ui.component.BottomNavBar
import com.aurabeat.presentation.ui.component.EmptyState
import com.aurabeat.presentation.ui.component.MiniPlayer
import com.aurabeat.presentation.ui.screen.ArtistDetailsScreen
import com.aurabeat.presentation.ui.screen.GeneratedPlaylistScreen
import com.aurabeat.presentation.ui.screen.HomeScreen
import com.aurabeat.presentation.ui.screen.LibraryScreen
import com.aurabeat.presentation.ui.screen.MoodLoadingScreen
import com.aurabeat.presentation.ui.screen.PlayerScreen
import com.aurabeat.presentation.ui.screen.ProfileScreen
import com.aurabeat.presentation.ui.screen.SearchScreen
import com.aurabeat.presentation.viewmodel.HomeViewModel
import com.aurabeat.presentation.viewmodel.LibraryViewModel
import com.aurabeat.presentation.viewmodel.ProfileViewModel
import com.aurabeat.presentation.viewmodel.SearchViewModel
import com.aurabeat.presentation.viewmodel.PlayerViewModel
import com.aurabeat.presentation.viewmodel.ArtistViewModel
import com.aurabeat.presentation.viewmodel.MoodViewModel
import com.aurabeat.core.AppContainer

@Composable
fun AuraBeatNavigationGraph(
    isDarkTheme: Boolean,
    onToggleTheme: () -> Unit,
    appContainer: AppContainer,
    navController: NavHostController = rememberNavController()
) {
    val navigationActions = remember(navController) { NavigationActions(navController) }
    val homeViewModel = remember { HomeViewModel(appContainer.playlistRepository) }
    val searchViewModel = remember { SearchViewModel(appContainer.searchEngine) }
    val libraryViewModel = remember { LibraryViewModel(appContainer.playlistRepository, appContainer.musicRepository) }
    val profileViewModel = remember { ProfileViewModel(appContainer.userRepository) }
    val playerViewModel = remember { PlayerViewModel() }
    val artistViewModel = remember { ArtistViewModel() }
    val moodViewModel = remember { MoodViewModel() }
    val playerState by playerViewModel.uiState.collectAsState()
    val backStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = backStackEntry?.destination?.route
    val showBottomBar = currentRoute in bottomLevelDestinations.map { it.route }
    val showMiniPlayer = showBottomBar && playerState.currentSong != null

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = MaterialTheme.colorScheme.background,
        bottomBar = {
            Column {
                if (showMiniPlayer) {
                    MiniPlayer(
                        song = playerState.currentSong,
                        isPlaying = playerState.isPlaying,
                        onTogglePlay = playerViewModel::togglePlayPause,
                        onExpand = {
                            playerState.currentSong?.let { song ->
                                navigationActions.navigateToPlayer(song.id)
                            }
                        }
                    )
                }
                if (showBottomBar) {
                    BottomNavBar(
                        currentRoute = currentRoute,
                        onNavigate = navigationActions::navigateToBottomDestination
                    )
                }
            }
        }
    ) { innerPadding ->
        AppNavHost(
            navController = navController,
            contentPadding = innerPadding,
            isDarkTheme = isDarkTheme,
            onToggleTheme = onToggleTheme,
            navigationActions = navigationActions,
            homeViewModel = homeViewModel,
            searchViewModel = searchViewModel,
            libraryViewModel = libraryViewModel,
            profileViewModel = profileViewModel,
            playerViewModel = playerViewModel,
            artistViewModel = artistViewModel,
            moodViewModel = moodViewModel
        )
    }
}

@Composable
private fun AppNavHost(
    navController: NavHostController,
    contentPadding: PaddingValues,
    isDarkTheme: Boolean,
    onToggleTheme: () -> Unit,
    navigationActions: NavigationActions,
    homeViewModel: HomeViewModel,
    searchViewModel: SearchViewModel,
    libraryViewModel: LibraryViewModel,
    profileViewModel: ProfileViewModel,
    playerViewModel: PlayerViewModel,
    artistViewModel: ArtistViewModel,
    moodViewModel: MoodViewModel
) {
    NavHost(
        navController = navController,
        startDestination = AppDestination.Home.route,
        enterTransition = {
            slideInHorizontally(initialOffsetX = { it / 5 }) + fadeIn()
        },
        exitTransition = {
            slideOutHorizontally(targetOffsetX = { -it / 5 }) + fadeOut()
        },
        popEnterTransition = {
            slideInHorizontally(initialOffsetX = { -it / 5 }) + fadeIn()
        },
        popExitTransition = {
            slideOutHorizontally(targetOffsetX = { it / 5 }) + fadeOut()
        }
    ) {
        composable(AppDestination.Home.route) {
            HomeScreen(
                contentPadding = contentPadding,
                homeViewModel = homeViewModel,
                onArtistClick = navigationActions::navigateToArtist,
                onGenerateClick = {
                    moodViewModel.startGeneration(it)
                    navigationActions.navigateToMoodLoading()
                }
            )
        }
        composable(AppDestination.Search.route) {
            SearchScreen(
                contentPadding = contentPadding,
                searchViewModel = searchViewModel,
                onSongClick = {
                    playerViewModel.playSong(it)
                    navigationActions.navigateToPlayer(it)
                },
                onArtistClick = navigationActions::navigateToArtist
            )
        }
        composable(AppDestination.Library.route) {
            LibraryScreen(
                contentPadding = contentPadding,
                libraryViewModel = libraryViewModel,
                onSongClick = {
                    playerViewModel.playSong(it)
                    navigationActions.navigateToPlayer(it)
                }
            )
        }
        composable(AppDestination.Profile.route) {
            ProfileScreen(
                contentPadding = contentPadding,
                isDarkTheme = isDarkTheme,
                onToggleTheme = onToggleTheme,
                profileViewModel = profileViewModel
            )
        }

        composable(
            route = AppDestination.PlaylistDetails.route,
            arguments = AppDestination.PlaylistDetails.arguments,
            deepLinks = AppDestination.PlaylistDetails.deepLinks.map { navDeepLink { uriPattern = it } }
        ) { entry ->
            FutureDestinationScreen(
                title = "Playlist Details",
                message = "Playlist ${entry.arguments?.getString("playlistId").orEmpty()} will open here.",
                contentPadding = contentPadding
            )
        }

        composable(
            route = AppDestination.ArtistDetails.route,
            arguments = AppDestination.ArtistDetails.arguments,
            deepLinks = AppDestination.ArtistDetails.deepLinks.map { navDeepLink { uriPattern = it } }
        ) { entry ->
            ArtistDetailsScreen(
                artistId = entry.arguments?.getString("artistId").orEmpty(),
                artistViewModel = artistViewModel,
                contentPadding = contentPadding,
                onBack = navigationActions::navigateBack,
                onSongClick = {
                    playerViewModel.playSong(it)
                    navigationActions.navigateToPlayer(it)
                },
                onAlbumClick = navigationActions::navigateToPlaylist,
                onArtistClick = navigationActions::navigateToArtist
            )
        }

        composable(
            route = AppDestination.Player.route,
            arguments = AppDestination.Player.arguments,
            deepLinks = AppDestination.Player.deepLinks.map { navDeepLink { uriPattern = it } }
        ) { entry ->
            PlayerScreen(
                songId = entry.arguments?.getString("songId").orEmpty(),
                playerViewModel = playerViewModel,
                contentPadding = contentPadding,
                onMinimize = navigationActions::navigateBack,
                onArtistClick = { navigationActions.navigateToArtist(it.toArtistId()) },
                onAlbumClick = navigationActions::navigateToPlaylist
            )
        }

        composable(AppDestination.Settings.route) {
            FutureDestinationScreen(
                title = "Settings",
                message = "App settings will be managed here.",
                contentPadding = contentPadding
            )
        }

        composable(AppDestination.GeneratedPlaylist.route) {
            GeneratedPlaylistScreen(
                moodViewModel = moodViewModel,
                playerViewModel = playerViewModel,
                contentPadding = contentPadding,
                onSongClick = navigationActions::navigateToPlayer
            )
        }

        composable(AppDestination.MoodLoading.route) {
            MoodLoadingScreen(
                moodViewModel = moodViewModel,
                contentPadding = contentPadding,
                onFinished = navigationActions::navigateToGeneratedPlaylist
            )
        }
    }
}

private fun String.toArtistId(): String = lowercase()
    .replace("&", "and")
    .replace(Regex("[^a-z0-9]+"), "-")
    .trim('-')

@Composable
private fun FutureDestinationScreen(
    title: String,
    message: String,
    contentPadding: PaddingValues
) {
    EmptyState(
        icon = Icons.Rounded.MusicNote,
        title = title,
        message = message,
        modifier = Modifier
            .fillMaxSize()
            .padding(contentPadding)
    )
}
