package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.FluentoViewModel
import com.example.data.UserProfile

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DashboardScreen(
    viewModel: FluentoViewModel,
    onNavigateToChat: () -> Unit,
    onNavigateToVoice: () -> Unit,
    onNavigateToRoadmap: () -> Unit,
    onNavigateToReviews: () -> Unit,
    onNavigateToPaywall: () -> Unit,
    modifier: Modifier = Modifier
) {
    val profileState by viewModel.userProfile.collectAsState()
    val allLessonsState by viewModel.allLessons.collectAsState()
    val weakItemsState by viewModel.weakItems.collectAsState()

    val profile = profileState ?: UserProfile()
    val scrollState = rememberScrollState()

    // Find the next active incomplete lesson
    val todayLesson = allLessonsState.firstOrNull { !it.isCompleted } ?: allLessonsState.firstOrNull()

    val isDarkTheme = isSystemInDarkTheme()
    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = {
                    Row(
                        modifier = Modifier.fillMaxWidth().padding(end = 16.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(
                                text = "Fluento AI",
                                fontSize = 24.sp,
                                fontWeight = FontWeight.ExtraBold,
                                color = MaterialTheme.colorScheme.onBackground
                            )
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Box(
                                    modifier = Modifier
                                        .size(8.dp)
                                        .background(Color(0xFF2EC4B6), CircleShape)
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = "Coach active: ${profile.tutorPersonality.split(" ")[0]}",
                                    fontSize = 11.sp,
                                    color = MaterialTheme.colorScheme.onBackground.copy(0.6f)
                                )
                            }
                        }

                        // Premium Tag or Initials Box from Bento theme
                        val initials = "JD"
                        Box(
                            modifier = Modifier
                                .size(36.dp)
                                .background(Color(0xFFEADDFF), CircleShape)
                                .border(1.dp, Color(0xFFD0BCFF), CircleShape)
                                .clickable { onNavigateToPaywall() },
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = initials,
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF21005D)
                            )
                        }
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
                .verticalScroll(scrollState)
                .padding(horizontal = 16.dp, vertical = 8.dp),
            verticalArrangement = Arrangement.spacedBy(18.dp)
        ) {
            // Stats Row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                StatPanel(
                    modifier = Modifier.weight(1f),
                    icon = Icons.Default.LocalFireDepartment,
                    iconColor = Color(0xFFFF9F1C),
                    value = "${profile.streak} Days",
                    label = "Active Streak"
                )
                StatPanel(
                    modifier = Modifier.weight(1f),
                    icon = Icons.Default.OfflineBolt,
                    iconColor = Color(0xFF3A86FF),
                    value = "${profile.xp} XP",
                    label = "Total Points"
                )
                StatPanel(
                    modifier = Modifier.weight(1f),
                    icon = Icons.Default.GraphicEq,
                    iconColor = Color(0xFF00D2C4),
                    value = "${profile.speakingMinutes} mins",
                    label = "Speaking Coaching"
                )
            }

            // Today's Lesson Call Card
            if (todayLesson != null) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            Brush.linearGradient(
                                listOf(Color(0xFF6750A4), Color(0xFF4F378B))
                            ),
                            RoundedCornerShape(24.dp)
                        )
                        .padding(20.dp)
                ) {
                    Column {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Box(
                                modifier = Modifier
                                    .background(Color.White.copy(0.2f), RoundedCornerShape(12.dp))
                                    .padding(horizontal = 10.dp, vertical = 4.dp)
                            ) {
                                Text(
                                    text = "CURRENT LEVEL: " + profile.proficiencyLevel.uppercase(),
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White
                                )
                            }
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text("🔥", fontSize = 16.sp)
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(
                                    text = "${profile.streak}",
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        Text(
                            text = profile.targetLanguage + " Mastery",
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = "Focus: " + todayLesson.title,
                            fontSize = 12.sp,
                            color = Color.White.copy(0.8f)
                        )

                        Spacer(modifier = Modifier.height(14.dp))

                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(6.dp)
                                .background(Color.White.copy(0.2f), CircleShape)
                        ) {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth(0.65f)
                                    .height(6.dp)
                                    .background(Color.White, CircleShape)
                            )
                        }
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "65% Daily goal achieved",
                            fontSize = 10.sp,
                            color = Color.White.copy(0.7f)
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            Button(
                                onClick = {
                                    viewModel.selectLesson(todayLesson)
                                    onNavigateToRoadmap()
                                },
                                modifier = Modifier.weight(1.5f).height(46.dp).testTag("start_lesson_cta"),
                                colors = ButtonDefaults.buttonColors(containerColor = Color.White),
                                shape = RoundedCornerShape(12.dp)
                            ) {
                                Icon(Icons.Default.PlayArrow, null, tint = Color(0xFF6750A4), modifier = Modifier.size(18.dp))
                                Spacer(modifier = Modifier.width(6.dp))
                                Text("Start Daily Lesson", fontWeight = FontWeight.Bold, color = Color(0xFF6750A4), fontSize = 13.sp)
                            }

                            Button(
                                onClick = onNavigateToRoadmap,
                                modifier = Modifier.weight(1.5f).height(46.dp),
                                colors = ButtonDefaults.buttonColors(containerColor = Color.White.copy(0.15f)),
                                shape = RoundedCornerShape(12.dp)
                            ) {
                                Text("Roadmap", color = Color.White, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                }
            }

            // Central Navigation Grid
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                val isDark = isSystemInDarkTheme()
                Text(
                    text = "STUDY MODULES",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = if (isDark) Color.White.copy(0.6f) else Color(0xFF6750A4),
                    letterSpacing = 1.2.sp
                )

                // Row 1
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    // Chat module bento purple card
                    val chatBg = if (isDark) Color(0xFF282330) else Color(0xFFE8DEF8)
                    val chatContent = if (isDark) Color(0xFFE8DEF8) else Color(0xFF21005D)
                    val chatSub = if (isDark) Color(0xFFE8DEF8).copy(0.7f) else Color(0xFF49454F)
                    val chatIconBg = if (isDark) Color(0xFFD0BCFF) else Color(0xFF21005D)
                    val chatIcon = if (isDark) Color.Black else Color.White

                    FeatureGridCard(
                        modifier = Modifier.weight(1f),
                        icon = Icons.Default.Chat,
                        title = "AI Chat Practice",
                        description = "Chat 24/7 with corrections in real-time.",
                        backgroundColor = chatBg,
                        contentColor = chatContent,
                        subTextColor = chatSub,
                        iconBgColor = chatIconBg,
                        iconColor = chatIcon,
                        badgeText = "FREE",
                        badgeColor = Color(0xFF2EC4B6),
                        onClick = onNavigateToChat
                    )

                    // Voice coach warm pink card
                    val voiceBg = if (isDark) Color(0xFF332025) else Color(0xFFFFD8E4)
                    val voiceContent = if (isDark) Color(0xFFFFD8E4) else Color(0xFF3B0014)
                    val voiceSub = if (isDark) Color(0xFFFFD8E4).copy(0.7f) else Color(0xFF49454F)
                    val voiceIconBg = if (isDark) Color(0xFFFF8FA3) else Color(0xFF4F378B)
                    val voiceIcon = if (isDark) Color.Black else Color.White

                    FeatureGridCard(
                        modifier = Modifier.weight(1f),
                        icon = Icons.Default.Call,
                        title = "Voice Call Coach",
                        description = "Vocalize fluently on dynamic call mockups.",
                        backgroundColor = voiceBg,
                        contentColor = voiceContent,
                        subTextColor = voiceSub,
                        iconBgColor = voiceIconBg,
                        iconColor = voiceIcon,
                        badgeText = "POPULAR",
                        badgeColor = Color(0xFF3A86FF),
                        onClick = onNavigateToVoice
                    )
                }

                // Row 2
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    // Roadmap warm bento card
                    val mapBg = if (isDark) Color(0xFF2F251E) else Color(0xFFFFEAD2)
                    val mapContent = if (isDark) Color(0xFFFFD0B0) else Color(0xFF5D2500)
                    val mapSub = if (isDark) Color(0xFFFFD0B0).copy(0.7f) else Color(0xFF49454F)
                    val mapIconBg = if (isDark) Color(0xFFFF9F1C) else Color(0xFF6750A4)
                    val mapIcon = if (isDark) Color.Black else Color.White

                    FeatureGridCard(
                        modifier = Modifier.weight(1f),
                        icon = Icons.Default.Map,
                        title = "Personal Roadmap",
                        description = "Track your complete 30-day timeline progress.",
                        backgroundColor = mapBg,
                        contentColor = mapContent,
                        subTextColor = mapSub,
                        iconBgColor = mapIconBg,
                        iconColor = mapIcon,
                        onClick = onNavigateToRoadmap
                    )

                    // Review light blue card
                    val reviewBg = if (isDark) Color(0xFF1E2638) else Color(0xFFD3E3FD)
                    val reviewContent = if (isDark) Color(0xFFD3E3FD) else Color(0xFF041E49)
                    val reviewSub = if (isDark) Color(0xFFD3E3FD).copy(0.7f) else Color(0xFF49454F)
                    val reviewIconBg = if (isDark) Color(0xFFB1C9F8) else Color(0xFF041E49)
                    val reviewIcon = if (isDark) Color.Black else Color.White

                    val weakSize = weakItemsState.size
                    FeatureGridCard(
                        modifier = Modifier.weight(1f),
                        icon = Icons.Default.Rule,
                        title = "Review Mistakes",
                        description = "Practice weak terms using repetition loops.",
                        backgroundColor = reviewBg,
                        contentColor = reviewContent,
                        subTextColor = reviewSub,
                        iconBgColor = reviewIconBg,
                        iconColor = reviewIcon,
                        badgeText = if (weakSize > 0) "$weakSize Items" else null,
                        badgeColor = if (weakSize > 0) Color(0xFFFF4D6D) else null,
                        onClick = onNavigateToReviews
                    )
                }
            }

            // Pro Promos
            if (!profile.isPremium) {
                val isDark = isSystemInDarkTheme()
                val promoBg = if (isDark) Color(0xFF2E1919) else Color(0xFF1D1B1E)
                val promoBorderColor = if (isDark) Color(0xFFFF9F1C).copy(0.3f) else Color(0xFFEADDFF)
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(promoBg, RoundedCornerShape(24.dp))
                        .border(1.dp, promoBorderColor, RoundedCornerShape(24.dp))
                        .clickable { onNavigateToPaywall() }
                        .padding(16.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(Icons.Default.Star, null, tint = Color(0xFFFF9F1C), modifier = Modifier.size(16.dp))
                                Spacer(modifier = Modifier.width(6.dp))
                                Text("Fluento Elite Access", fontWeight = FontWeight.Bold, color = Color.White, fontSize = 15.sp)
                            }
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "Unlock unlimited speaking calls, customized phonetic drills, and no daily practice caps.",
                                fontSize = 12.sp,
                                color = Color.White.copy(0.7f),
                                lineHeight = 16.sp
                            )
                        }
                        
                        Box(
                            modifier = Modifier
                                .size(34.dp)
                                .background(Color(0xFFFF9F1C), CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(Icons.Default.ChevronRight, null, tint = Color.Black)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun StatPanel(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    iconColor: Color,
    value: String,
    label: String,
    modifier: Modifier = Modifier
) {
    val isDark = isSystemInDarkTheme()
    val bgColor = if (isDark) Color(0xFF232023) else Color(0xFFFFFFFF)
    val borderColor = if (isDark) Color(0xFF3D3A3D) else Color(0xFFEADDFF)
    val textColor = if (isDark) Color.White else Color(0xFF1D1B1E)
    val subTextColor = if (isDark) Color.White.copy(0.6f) else Color(0xFF49454F)

    Column(
        modifier = modifier
            .background(bgColor, RoundedCornerShape(16.dp))
            .border(1.dp, borderColor, RoundedCornerShape(16.dp))
            .padding(12.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(imageVector = icon, contentDescription = null, tint = iconColor, modifier = Modifier.size(22.dp))
        Spacer(modifier = Modifier.height(6.dp))
        Text(text = value, fontSize = 15.sp, fontWeight = FontWeight.ExtraBold, color = textColor)
        Spacer(modifier = Modifier.height(2.dp))
        Text(text = label, fontSize = 9.sp, color = subTextColor, textAlign = TextAlign.Center)
    }
}

@Composable
fun FeatureGridCard(
    modifier: Modifier = Modifier,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    title: String,
    description: String,
    backgroundColor: Color,
    contentColor: Color,
    subTextColor: Color,
    iconBgColor: Color,
    iconColor: Color,
    badgeText: String? = null,
    badgeColor: Color? = null,
    onClick: () -> Unit
) {
    Box(
        modifier = modifier
            .background(backgroundColor, RoundedCornerShape(24.dp))
            .clickable { onClick() }
            .padding(16.dp)
            .height(130.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                Box(
                    modifier = Modifier
                        .size(36.dp)
                        .background(iconBgColor, RoundedCornerShape(12.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(imageVector = icon, contentDescription = null, tint = iconColor, modifier = Modifier.size(18.dp))
                }

                if (badgeText != null && badgeColor != null) {
                    Text(
                        text = badgeText,
                        fontSize = 8.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = Color.Black,
                        modifier = Modifier
                            .background(badgeColor, RoundedCornerShape(6.dp))
                            .padding(horizontal = 6.dp, vertical = 2.dp)
                    )
                }
            }

            Column {
                Text(
                    text = title,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = contentColor
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = description,
                    fontSize = 11.sp,
                    color = subTextColor,
                    lineHeight = 14.sp
                )
            }
        }
    }
}
