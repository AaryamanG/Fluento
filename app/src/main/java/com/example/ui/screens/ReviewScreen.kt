package com.example.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.FluentoViewModel
import com.example.data.WeakItem

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReviewScreen(
    viewModel: FluentoViewModel,
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    val weakItemsState by viewModel.weakItems.collectAsState()
    val profile by viewModel.userProfile.collectAsState()

    val isDark = isSystemInDarkTheme()
    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "Spaced Repetition Review",
                        color = MaterialTheme.colorScheme.onBackground,
                        fontSize = 17.sp,
                        fontWeight = FontWeight.Bold
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back", tint = MaterialTheme.colorScheme.onBackground)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.background)
            )
        },
        containerColor = MaterialTheme.colorScheme.background
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 16.dp)
        ) {
            
            if (weakItemsState.isEmpty()) {
                // EXCELLENT EMPTY STATE INTERFACE
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(bottom = 60.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Box(
                        modifier = Modifier
                            .size(80.dp)
                            .background(Color(0xFF2EC4B6).copy(0.12f), CircleShape)
                            .border(1.5.dp, Color(0xFF2EC4B6), CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Spellcheck,
                            contentDescription = "No mistakes scheduled logo",
                            tint = Color(0xFF2EC4B6),
                            modifier = Modifier.size(36.dp)
                        )
                    }

                    Spacer(modifier = Modifier.height(24.dp))

                    Text(
                        text = "Your Spelling & Pronunciation is Clean!",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onBackground,
                        textAlign = TextAlign.Center
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = "Any grammatical slips caught while training\nwith Coach ${profile?.tutorPersonality?.split(" ")?.get(0) ?: "Tutor"} will manifest here automatically. Keep practicing!",
                        fontSize = 13.sp,
                        color = MaterialTheme.colorScheme.onBackground.copy(0.6f),
                        textAlign = TextAlign.Center,
                        lineHeight = 18.sp
                    )
                }
            } else {
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(14.dp),
                    contentPadding = PaddingValues(top = 10.dp, bottom = 24.dp),
                    modifier = Modifier.fillMaxSize()
                ) {
                    item {
                        Column {
                            Text(
                                text = "SCHEDULED REVIEWS: ${weakItemsState.size} SLIPS",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFFFF4D6D),
                                letterSpacing = 1.2.sp
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "Our spaced repetition loop schedules weak words daily. Recite the solution sentence out loud and tap Mastered to clear it.",
                                fontSize = 12.sp,
                                color = MaterialTheme.colorScheme.onBackground.copy(0.6f),
                                lineHeight = 16.sp
                            )
                            Spacer(modifier = Modifier.height(14.dp))
                        }
                    }

                    items(weakItemsState) { item ->
                        ReviewSlipCard(
                            item = item,
                            onMastered = { viewModel.practiceReviewItem(item) }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun ReviewSlipCard(
    item: WeakItem,
    onMastered: () -> Unit
) {
    val isDark = isSystemInDarkTheme()
    val bgColor = if (isDark) Color(0xFF232023) else Color(0xFFFFFFFF)
    val borderColor = if (isDark) Color(0xFF3D3A3D) else Color(0xFFEADDFF)
    val labelColor = if (isDark) Color.White.copy(0.5f) else Color(0xFF49454F)
    val normalTextColor = if (isDark) Color.White else Color(0xFF1D1B1E)

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(bgColor, RoundedCornerShape(24.dp))
            .border(1.dp, borderColor, RoundedCornerShape(24.dp))
            .padding(16.dp)
    ) {
        Column {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .background(
                            if (item.category == "Grammar") Color(0xFFFF4D6D).copy(0.12f) else Color(0xFF3A86FF).copy(0.12f),
                            RoundedCornerShape(6.dp)
                        )
                        .padding(horizontal = 8.dp, vertical = 3.dp)
                ) {
                    Text(
                        text = item.category.uppercase(),
                        fontSize = 8.sp,
                        fontWeight = FontWeight.Bold,
                        color = if (item.category == "Grammar") Color(0xFFFF4D6D) else Color(0xFF3A86FF)
                    )
                }

                Text(
                    text = "Review count: ${item.reviewCount}",
                    fontSize = 10.sp,
                    color = labelColor
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Fault statement
            Text(
                text = "Your original slip:",
                fontSize = 11.sp,
                color = labelColor,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = item.originalSentence,
                fontSize = 14.sp,
                color = Color(0xFFFF4D6D),
                modifier = Modifier.padding(top = 2.dp)
            )

            Spacer(modifier = Modifier.height(10.dp))

            // Corrected Statement
            Text(
                text = "Corrected Coach Version:",
                fontSize = 11.sp,
                color = labelColor,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = item.correctedSentence,
                fontSize = 14.sp,
                color = Color(0xFF2EC4B6),
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(top = 2.dp)
            )

            Spacer(modifier = Modifier.height(10.dp))

            // Explanation Advice segment
            Text(
                text = "Tutor Explains:",
                fontSize = 11.sp,
                color = labelColor,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = item.explanation,
                fontSize = 12.sp,
                color = normalTextColor.copy(0.8f),
                lineHeight = 16.sp,
                modifier = Modifier.padding(top = 2.dp)
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Master verification CTA
            Button(
                onClick = onMastered,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(40.dp)
                    .testTag("resolve_mistake_button"),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2EC4B6).copy(0.12f)),
                shape = RoundedCornerShape(8.dp),
                border = BorderStroke(1.dp, Color(0xFF2EC4B6).copy(0.4f))
            ) {
                Icon(Icons.Default.Check, null, tint = Color(0xFF2EC4B6), modifier = Modifier.size(16.dp))
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = "Mastered Rule (+15 XP)",
                    color = Color(0xFF2EC4B6),
                    fontWeight = FontWeight.Bold,
                    fontSize = 12.sp
                )
            }
        }
    }
}
