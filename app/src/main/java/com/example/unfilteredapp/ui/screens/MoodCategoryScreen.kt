package com.example.unfilteredapp.ui.screens

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material.icons.filled.BarChart
import androidx.compose.material.icons.filled.ElectricBolt
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.SentimentDissatisfied
import androidx.compose.material.icons.filled.WaterDrop
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MoodCategoryScreen(
    onCategorySelected: (String) -> Unit,
    onViewAnalytics: () -> Unit,
    onLogout: () -> Unit
) {
    val categories = listOf(
        MoodCategoryItem(
            "High Energy\nPleasant", 
            "high_energy_pleasant", 
            Color(0xFFFBDA63), 
            Color(0xFFFBB140),
            Icons.Default.ElectricBolt
        ),
        MoodCategoryItem(
            "Low Energy\nPleasant", 
            "low_energy_pleasant", 
            Color(0xFF62F95D), 
            Color(0xFF058C00),
            Icons.Default.Favorite
        ),
        MoodCategoryItem(
            "Low Energy\nUnpleasant", 
            "low_energy_unpleasant", 
            Color(0xFF5D99F9), 
            Color(0xFF2B7CFF),
            Icons.Default.WaterDrop
        ),
        MoodCategoryItem(
            "High Energy\nUnpleasant", 
            "high_energy_unpleasant", 
            Color(0xFFF83700), 
            Color(0xFFBF2A00),
            Icons.Default.SentimentDissatisfied
        )
    )

    Scaffold(
        topBar = {
            LargeTopAppBar(
                title = { 
                    Text(
                        "How are you\nfeeling today?", 
                        fontWeight = FontWeight.ExtraBold,
                        lineHeight = 36.sp
                    ) 
                },
                actions = {
                    IconButton(onClick = onLogout) {
                        Surface(
                            shape = RoundedCornerShape(12.dp),
                            color = MaterialTheme.colorScheme.surfaceVariant
                        ) {
                            Icon(
                                Icons.AutoMirrored.Filled.Logout, 
                                contentDescription = "Logout",
                                modifier = Modifier.padding(8.dp),
                                tint = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                },
                colors = TopAppBarDefaults.largeTopAppBarColors(
                    containerColor = Color.Transparent,
                    scrolledContainerColor = MaterialTheme.colorScheme.background
                )
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .background(MaterialTheme.colorScheme.background)
                .padding(horizontal = 20.dp),
        ) {
            Text(
                text = "Select a quadrant to explore your emotions",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(bottom = 24.dp)
            )

            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                modifier = Modifier.weight(1f)
            ) {
                itemsIndexed(categories) { index, category ->
                    var visible by remember { mutableStateOf(false) }
                    LaunchedEffect(Unit) {
                        kotlinx.coroutines.delay(index * 100L)
                        visible = true
                    }
                    
                    AnimatedVisibility(
                        visible = visible,
                        enter = fadeIn() + slideInVertically { it / 2 }
                    ) {
                        CategoryCard(category) {
                            onCategorySelected(category.modeType)
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // New View Analytics Button
            OutlinedButton(
                onClick = onViewAnalytics,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
                    .padding(bottom = 16.dp),
                shape = RoundedCornerShape(16.dp),
                border = androidx.compose.foundation.BorderStroke(
                    1.dp, 
                    MaterialTheme.colorScheme.primary.copy(alpha = 0.3f)
                )
            ) {
                Icon(Icons.Default.BarChart, contentDescription = null)
                Spacer(modifier = Modifier.width(12.dp))
                Text(
                    "View My Analytics",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

data class MoodCategoryItem(
    val title: String,
    val modeType: String,
    val startColor: Color,
    val endColor: Color,
    val icon: ImageVector
)

@Composable
fun CategoryCard(
    category: MoodCategoryItem,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .aspectRatio(0.85f)
            .clip(RoundedCornerShape(32.dp))
            .clickable { onClick() },
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
        colors = CardDefaults.cardColors(containerColor = Color.Transparent)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        colors = listOf(category.startColor, category.endColor)
                    )
                )
                .padding(20.dp)
        ) {
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                Surface(
                    shape = RoundedCornerShape(16.dp),
                    color = Color.White.copy(alpha = 0.2f)
                ) {
                    Icon(
                        imageVector = category.icon,
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.padding(10.dp).size(28.dp)
                    )
                }

                Text(
                    text = category.title,
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Bold,
                        lineHeight = 22.sp
                    ),
                    color = Color.White,
                    fontSize = 18.sp
                )
            }
        }
    }
}
