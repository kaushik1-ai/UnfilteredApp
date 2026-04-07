package com.example.unfilteredapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
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
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.serialization.Serializable

// Consolidated ViewModels to resolve reference issues
class JournalViewModel : ViewModel() {
    private val _entries = MutableStateFlow<List<String>>(emptyList())
    val entries: StateFlow<List<String>> = _entries
    fun addEntry(entry: String) {
        if (entry.isNotBlank()) {
            _entries.value = listOf(entry) + _entries.value
        }
    }
}

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
    @Serializable data object Detox : Screen
}

sealed class BottomNavScreen(val route: Screen, val icon: ImageVector, val label: String) {
    object Journal : BottomNavScreen(Screen.Journal, Icons.Default.Book, "Journal")
    object Music : BottomNavScreen(Screen.Music, Icons.Default.MusicNote, "Music")
    object Mood : BottomNavScreen(Screen.MoodCategory, Icons.Default.Favorite, "Mood")
    object Rooms : BottomNavScreen(Screen.Rooms, Icons.Default.Chat, "Rooms")
    object Detox : BottomNavScreen(Screen.Detox, Icons.Default.SelfImprovement, "Detox")
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

    val authState by authViewModel.authState.collectAsState()

    LaunchedEffect(authState) {
        if (authState is AuthState.LoggedOut) {
            navController.navigate(Screen.Login) {
                popUpTo(0) { inclusive = true }
            }
            authViewModel.resetState()
        }
    }

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    // Simple string-based route check
    val routeName = currentDestination?.route ?: ""
    val showNavigation = routeName.isNotEmpty() && 
                        !routeName.contains("Login") && 
                        !routeName.contains("Signup")

    Scaffold(
        bottomBar = {
            if (showNavigation) {
                CustomBottomAppBar(navController)
            }
        }
    ) { paddingValues ->
        Box(modifier = Modifier.padding(paddingValues)) {
            AppNavigation(navController, authViewModel)
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
        containerColor = MaterialTheme.colorScheme.surface,
        tonalElevation = 8.dp
    ) {
        items.forEach { item ->
            NavigationBarItem(
                selected = currentDestination?.hierarchy?.any { 
                    it.route?.contains(item.route::class.simpleName ?: "") == true 
                } == true,
                onClick = {
                    navController.navigate(item.route) {
                        popUpTo(navController.graph.findStartDestination().id) { saveState = true }
                        launchSingleTop = true
                        restoreState = true
                    }
                },
                icon = { Icon(item.icon, contentDescription = item.label) },
                label = { Text(item.label, fontSize = 10.sp) }
            )
        }
    }
}

@Composable
fun AppNavigation(
    navController: androidx.navigation.NavHostController,
    authViewModel: AuthViewModel
) {
    val journalViewModel: JournalViewModel = viewModel()
    val moodViewModel: MoodViewModel = viewModel()
    
    val startDestination = if (authViewModel.isLoggedIn()) Screen.MoodCategory else Screen.Login

    NavHost(
        navController = navController,
        startDestination = startDestination
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
                onLogout = { authViewModel.logout() }
            )
        }
        composable<Screen.MoodSubSelection> { backStackEntry ->
            val subSelection: Screen.MoodSubSelection = backStackEntry.toRoute()
            MoodSubSelectionScreen(
                modeType = subSelection.modeType,
                onMoodSelected = { moodTag ->
                    moodViewModel.selectMood(moodTag)
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
        composable<Screen.Rooms> { RoomsScreen(onBack = { navController.popBackStack() }) }
        composable<Screen.Detox> { PlaceholderScreen("Detox") }
    }
}

@Composable
fun PlaceholderScreen(name: String) {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Text(text = "$name Screen", style = MaterialTheme.typography.headlineMedium)
    }
}

@Composable
fun MoodSummaryScreen(
    onBack: () -> Unit,
    viewModel: MoodViewModel
) {
    val selectedMood by viewModel.selectedMood.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(text = "Mood Recorded", style = MaterialTheme.typography.headlineSmall)
        Spacer(modifier = Modifier.height(24.dp))
        selectedMood?.let {
            Text(text = "You are feeling: $it", style = MaterialTheme.typography.bodyLarge)
        }
        Spacer(modifier = Modifier.height(32.dp))
        Button(onClick = { onBack() }, modifier = Modifier.fillMaxWidth()) {
            Text("Finish")
        }
    }
}

@Composable
fun JournalScreen(
    onBack: () -> Unit,
    viewModel: JournalViewModel
) {
    val entries by viewModel.entries.collectAsState()
    var text by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            text = "Journal",
            style = MaterialTheme.typography.headlineSmall,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        OutlinedTextField(
            value = text,
            onValueChange = { text = it },
            modifier = Modifier
                .fillMaxWidth()
                .height(150.dp),
            placeholder = { Text("Write your thoughts here...") }
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                viewModel.addEntry(text)
                text = ""
            },
            modifier = Modifier.fillMaxWidth(),
            enabled = text.isNotBlank()
        ) {
            Text("Save Entry")
        }

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = "Previous Entries",
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        LazyColumn(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(entries) { entry ->
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surfaceVariant
                    )
                ) {
                    Text(
                        text = entry,
                        modifier = Modifier.padding(16.dp),
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(onClick = onBack, modifier = Modifier.fillMaxWidth()) {
            Text("Back")
        }
    }
}

@Composable
fun RoomsScreen(onBack: () -> Unit) {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(text = "Rooms Screen", style = MaterialTheme.typography.headlineMedium)
        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = onBack) {
            Text("Back")
        }
    }
}
