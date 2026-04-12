package com.example.unfilteredapp.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.unfilteredapp.data.model.MoodData
import com.example.unfilteredapp.data.model.MoodSubType

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MoodSubSelectionScreen(
    modeType: String,
    onMoodSelected: (String) -> Unit,
    onBack: () -> Unit
) {
    val moods = remember(modeType) {
        MoodData.moods.filter { it.modeType == modeType }
    }
    
    val displayTitle = remember(modeType) {
        modeType.replace("_", " ").uppercase()
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { 
                    Text(
                        displayTitle, 
                        fontWeight = FontWeight.ExtraBold, 
                        fontSize = 14.sp,
                        letterSpacing = 2.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    ) 
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.background)
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .background(MaterialTheme.colorScheme.background)
        ) {
            // Editorial Header
            Column(modifier = Modifier.padding(24.dp)) {
                Text(
                    "Identify your state.",
                    fontSize = 40.sp,
                    fontWeight = FontWeight.Black,
                    lineHeight = 44.sp,
                    letterSpacing = (-2).sp,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    "Select the precise emotion that defines your current frequency in the $displayTitle quadrant.",
                    fontSize = 14.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    lineHeight = 20.sp
                )
            }

            // Staggered Bento-style Grid
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(20.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(moods) { mood ->
                    MuseumMoodCard(
                        moodItem = mood,
                        onClick = { onMoodSelected("$modeType:${mood.subTitle}") }
                    )
                }
            }
        }
    }
}

@Composable
fun MuseumMoodCard(
    moodItem: MoodSubType,
    onClick: () -> Unit
) {
    val accentColor = try {
        Color(android.graphics.Color.parseColor(moodItem.lColor))
    } catch (e: Exception) {
        MaterialTheme.colorScheme.primary
    }

    // Flat Museum Design: High contrast, No shadows, Rounded geometric shapes
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .aspectRatio(1f) // Square grid for a modern look
            .clip(RoundedCornerShape(32.dp))
            .background(accentColor)
            .clickable { onClick() }
            .padding(20.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            // Subtle indicator
            Surface(
                modifier = Modifier.size(8.dp),
                shape = RoundedCornerShape(2.dp),
                color = Color.White.copy(alpha = 0.5f)
            ) {}

            Column {
                Text(
                    text = moodItem.subTitle,
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.ExtraBold,
                    color = Color.White,
                    lineHeight = 22.sp
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = moodItem.description.take(30) + if(moodItem.description.length > 30) "..." else "",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color.White.copy(alpha = 0.8f),
                    lineHeight = 14.sp
                )
            }
        }
    }
}
