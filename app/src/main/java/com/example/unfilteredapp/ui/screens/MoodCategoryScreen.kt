package com.example.unfilteredapp.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MoodCategoryScreen(
    onCategorySelected: (String) -> Unit,
    onLogout: () -> Unit
) {
    val categories = listOf(
        MoodCategoryItem("High Energy + Pleasant", "high_energy_pleasant", Color(0xFFFBDA63), Color(0xFFFBB140)),
        MoodCategoryItem("Low Energy + Pleasant", "low_energy_pleasant", Color(0xFF62F95D), Color(0xFF058C00)),
        MoodCategoryItem("Low Energy + Unpleasant", "low_energy_unpleasant", Color(0xFF5D99F9), Color(0xFF2B7CFF)),
        MoodCategoryItem("High Energy + Unpleasant", "high_energy_unpleasant", Color(0xFFF83700), Color(0xFFBF2A00))
    )

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("How do you feel?", fontWeight = FontWeight.Bold) },
                actions = {
                    IconButton(onClick = onLogout) {
                        Icon(Icons.AutoMirrored.Filled.Logout, contentDescription = "Logout")
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(16.dp))

            LazyVerticalGrid(
                columns = GridCells.Fixed(1),
                verticalArrangement = Arrangement.spacedBy(20.dp),
                modifier = Modifier.fillMaxSize()
            ) {
                items(categories) { category ->
                    CategoryCard(category) {
                        onCategorySelected(category.modeType)
                    }
                }
            }
        }
    }
}

data class MoodCategoryItem(
    val title: String,
    val modeType: String,
    val startColor: Color,
    val endColor: Color
)

@Composable
fun CategoryCard(
    category: MoodCategoryItem,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(120.dp)
            .clickable { onClick() },
        shape = RoundedCornerShape(24.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        colors = listOf(category.startColor, category.endColor)
                    )
                ),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = category.title,
                style = MaterialTheme.typography.titleLarge,
                color = Color.White,
                fontWeight = FontWeight.SemiBold,
                textAlign = TextAlign.Center,
                fontSize = 20.sp
            )
        }
    }
}
