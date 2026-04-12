package com.example.unfilteredapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.*
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.*
import androidx.navigation.toRoute
import com.example.unfilteredapp.data.repository.AuthRepository
import com.example.unfilteredapp.ui.screens.*
import com.example.unfilteredapp.ui.theme.UnfilteredAppTheme
import com.example.unfilteredapp.viewmodel.AuthState
import com.example.unfilteredapp.viewmodel.AuthViewModel
import com.example.unfilteredapp.viewmodel.MoodAnalyticsViewModel
import com.example.unfilteredapp.data.repository.MoodRepository
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import com.example.unfilteredapp.data.repository.JournalRepository
import com.example.unfilteredapp.viewmodel.JournalViewModel
import com.example.unfilteredapp.viewmodel.ChatViewModel
import com.example.unfilteredapp.data.repository.ChatRepository
import androidx.compose.foundation.shape.RoundedCornerShape
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.serialization.Serializable

// Consolidated ViewModels to resolve reference issues

class MoodViewModel : ViewModel() {
    private val _selectedMood = MutableStateFlow<String?>(null)
    val selectedMood: StateFlow<String?> = _selectedMood
    fun selectMood(moodTag: String) {
        _selectedMood.value = moodTag
    }
}

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            UnfilteredAppTheme {
                MainContainer()
            }
        }
    }
}

@Serializable sealed interface Screen {
    @Serializable data object Login : Screen
    @Serializable data object Signup : Screen
    @Serializable data object Journal : Screen
    @Serializable data object Music : Screen
    @Serializable data object MoodCategory : Screen
    @Serializable data class MoodSubSelection(val modeType: String) : Screen
    @Serializable data object MoodSummary : Screen
    @Serializable data object Rooms : Screen
    @Serializable data class Chat(val roomId: Int, val roomName: String, val moodTag: String, val roomDescription: String? = null) : Screen
    @Serializable data object Detox : Screen
    @Serializable data object Analytics : Screen
}

sealed class BottomNavScreen(val route: Screen, val icon: ImageVector, val label: String) {
    object Journal : BottomNavScreen(Screen.Journal, Icons.Default.AutoStories, "Journal")
    object Music : BottomNavScreen(Screen.Music, Icons.Default.MusicNote, "Music")
    object Mood : BottomNavScreen(Screen.MoodCategory, Icons.Default.Face, "Mood")
    object Rooms : BottomNavScreen(Screen.Rooms, Icons.Default.Forum, "Rooms")
    object Detox : BottomNavScreen(Screen.Detox, Icons.Default.Psychology, "Detox")
}

@Composable
fun MainContainer() {
    val navController = rememberNavController()
    val context = LocalContext.current
    val repository = remember { AuthRepository(context) }
    val authViewModel: AuthViewModel = viewModel(
        factory = object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                @Suppress("UNCHECKED_CAST")
                return AuthViewModel(repository) as T
            }
        }
    )

    val analyticsViewModel: MoodAnalyticsViewModel = viewModel(
        factory = object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                @Suppress("UNCHECKED_CAST")
                return MoodAnalyticsViewModel(MoodRepository(context)) as T
            }
        }
    )

    val journalViewModel: JournalViewModel = viewModel(
        factory = object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                @Suppress("UNCHECKED_CAST")
                return JournalViewModel(JournalRepository(context)) as T
            }
        }
    )

    val chatViewModel: ChatViewModel = viewModel(
        factory = object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                @Suppress("UNCHECKED_CAST")
                return ChatViewModel(com.example.unfilteredapp.data.repository.ChatRepository(context)) as T
            }
        }
    )

    val authState by authViewModel.authState.collectAsState()

    LaunchedEffect(authState) {
        if (authState is AuthState.Unauthenticated) {
            navController.navigate(Screen.Login) {
                popUpTo(0) { inclusive = true }
            }
            authViewModel.resetState()
        }
    }

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    val routeName = currentDestination?.route ?: ""
    val showNavigation = routeName.isNotEmpty() && 
                        !routeName.contains("Login") && 
                        !routeName.contains("Signup") &&
                        !routeName.contains("Chat")

    Scaffold(
        bottomBar = {
            AnimatedVisibility(
                visible = showNavigation,
                enter = slideInVertically { it },
                exit = slideOutVertically { it }
            ) {
                CustomBottomAppBar(navController)
            }
        }
    ) { paddingValues ->
        Box(modifier = Modifier.padding(paddingValues)) {
            AppNavigation(navController, authViewModel, analyticsViewModel, journalViewModel, chatViewModel)
        }
    }
}

@Composable
fun CustomBottomAppBar(navController: androidx.navigation.NavHostController) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination
    
    val items = listOf(
        BottomNavScreen.Journal,
        BottomNavScreen.Music,
        BottomNavScreen.Mood,
        BottomNavScreen.Rooms,
        BottomNavScreen.Detox
    )

    NavigationBar(
        containerColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.95f),
        tonalElevation = 0.dp,
        modifier = Modifier
            .height(84.dp)
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .clip(RoundedCornerShape(32.dp)),
        windowInsets = WindowInsets(0)
    ) {
        items.forEach { item ->
            val isSelected = currentDestination?.hierarchy?.any { 
                it.route?.contains(item.route::class.simpleName ?: "") == true 
            } == true

            NavigationBarItem(
                selected = isSelected,
                onClick = {
                    navController.navigate(item.route) {
                        popUpTo(navController.graph.findStartDestination().id) { saveState = true }
                        launchSingleTop = true
                        restoreState = true
                    }
                },
                icon = { 
                    Icon(
                        item.icon, 
                        contentDescription = item.label,
                        modifier = Modifier.size(26.dp)
                    ) 
                },
                label = { 
                    Text(
                        item.label, 
                        fontSize = 11.sp, 
                        fontWeight = if (isSelected) FontWeight.ExtraBold else FontWeight.Medium,
                        letterSpacing = 0.5.sp
                    ) 
                },
                colors = NavigationBarItemDefaults.colors(
                    indicatorColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.1f),
                    selectedIconColor = MaterialTheme.colorScheme.primary,
                    selectedTextColor = MaterialTheme.colorScheme.primary,
                    unselectedIconColor = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f),
                    unselectedTextColor = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f)
                )
            )
        }
    }
}

@Composable
fun AppNavigation(
    navController: androidx.navigation.NavHostController,
    authViewModel: AuthViewModel,
    analyticsViewModel: MoodAnalyticsViewModel,
    journalViewModel: JournalViewModel,
    chatViewModel: ChatViewModel
) {
    val moodViewModel: MoodViewModel = viewModel()
    
    val startDestination = if (authViewModel.isLoggedIn()) Screen.MoodCategory else Screen.Login

    NavHost(
        navController = navController,
        startDestination = startDestination,
        enterTransition = { fadeIn(tween(400)) },
        exitTransition = { fadeOut(tween(400)) }
    ) {
        composable<Screen.Login> {
            LoginScreen(
                viewModel = authViewModel,
                onNavigateToSignup = { navController.navigate(Screen.Signup) },
                onLoginSuccess = { 
                    navController.navigate(Screen.MoodCategory) {
                        popUpTo(Screen.Login) { inclusive = true }
                    }
                }
            )
        }
        composable<Screen.Signup> {
            SignupScreen(
                viewModel = authViewModel,
                onNavigateToLogin = { navController.navigate(Screen.Login) },
                onSignupSuccess = {
                    navController.navigate(Screen.Login) {
                        popUpTo(Screen.Signup) { inclusive = true }
                    }
                }
            )
        }
        composable<Screen.MoodCategory> {
            MoodCategoryScreen(
                onCategorySelected = { modeType ->
                    navController.navigate(Screen.MoodSubSelection(modeType))
                },
                onViewAnalytics = { navController.navigate(Screen.Analytics) },
                onLogout = { authViewModel.logout() }
            )
        }
        composable<Screen.MoodSubSelection> { backStackEntry ->
            val subSelection: Screen.MoodSubSelection = backStackEntry.toRoute()
            MoodSubSelectionScreen(
                modeType = subSelection.modeType,
                onMoodSelected = { moodTag ->
                    moodViewModel.selectMood(moodTag)
                    
                    // Call the API to store the mood in the backend
                    val parts = moodTag.split(":")
                    if (parts.size == 2) {
                        analyticsViewModel.logMood(parts[0], parts[1])
                    }
                    
                    navController.navigate(Screen.MoodSummary)
                },
                onBack = { navController.popBackStack() }
            )
        }
        composable<Screen.MoodSummary> {
            MoodSummaryScreen(onBack = { 
                navController.navigate(Screen.MoodCategory) {
                    popUpTo(Screen.MoodCategory) { inclusive = true }
                }
            }, viewModel = moodViewModel)
        }
        composable<Screen.Journal> {
            JournalScreen(onBack = { navController.popBackStack() }, viewModel = journalViewModel)
        }
        composable<Screen.Music> { PlaceholderScreen("Music") }
        composable<Screen.Rooms> { 
            RoomsScreen(viewModel = chatViewModel, onRoomClick = { room ->
                navController.navigate(Screen.Chat(room.id, room.name, room.mood_tag, room.description))
            }) 
        }
        composable<Screen.Chat> { backStackEntry ->
            val chatRoute: Screen.Chat = backStackEntry.toRoute()
            
            ChatScreen(
                room = com.example.unfilteredapp.data.model.Room(
                    chatRoute.roomId, 
                    chatRoute.roomName, 
                    chatRoute.moodTag, 
                    chatRoute.roomDescription
                ),
                authViewModel = authViewModel,
                viewModel = chatViewModel,
                onBack = { navController.popBackStack() }
            )
        }
        composable<Screen.Detox> { PlaceholderScreen("Detox") }
        composable<Screen.Analytics> { 
            AnalyticsScreen(
                viewModel = analyticsViewModel,
                onBack = { navController.popBackStack() }
            ) 
        }
    }
}

@Composable
fun PlaceholderScreen(name: String) {
    Box(
        modifier = Modifier.fillMaxSize().background(MaterialTheme.colorScheme.background), 
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Icon(
                Icons.Default.HourglassEmpty, 
                contentDescription = null, 
                modifier = Modifier.size(64.dp),
                tint = MaterialTheme.colorScheme.primary.copy(alpha = 0.3f)
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "$name Screen", 
                style = MaterialTheme.typography.headlineMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}
