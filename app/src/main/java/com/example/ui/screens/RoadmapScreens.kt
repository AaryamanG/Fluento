package com.example.ui.screens

import androidx.compose.animation.*
import androidx.compose.animation.core.animateFloatAsState
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
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.FluentoViewModel
import com.example.data.DailyLesson
import com.example.service.GeminiService
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RoadmapScreen(
    viewModel: FluentoViewModel,
    onNavigateToLessonDetail: () -> Unit,
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    val lessons by viewModel.allLessons.collectAsState()
    val profile by viewModel.userProfile.collectAsState()

    val isDark = isSystemInDarkTheme()
    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "My Conversational Roadmap",
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
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp),
            contentPadding = PaddingValues(top = 12.dp, bottom = 24.dp)
        ) {
            item {
                Column(modifier = Modifier.padding(bottom = 12.dp)) {
                    Text(
                        text = "30-DAY SPEECH TIMELINE",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = if (isDark) Color(0xFF2EC4B6) else Color(0xFF6750A4),
                        letterSpacing = 1.5.sp
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Succeed on your daily communication drills to unlock advanced business & relationship modules.",
                        fontSize = 13.sp,
                        color = MaterialTheme.colorScheme.onBackground.copy(0.6f),
                        lineHeight = 18.sp
                    )
                }
            }

            items(lessons) { lesson ->
                RoadmapCard(
                    lesson = lesson,
                    onClick = {
                        viewModel.selectLesson(lesson)
                        onNavigateToLessonDetail()
                    }
                )
            }
        }
    }
}

@Composable
fun RoadmapCard(
    lesson: DailyLesson,
    onClick: () -> Unit
) {
    val isCompleted = lesson.isCompleted
    val isDark = isSystemInDarkTheme()
    val bgColor = if (isDark) Color(0xFF232023) else Color(0xFFE8DEF8).copy(0.4f)
    val borderColor = if (isDark) Color(0xFF3D3A3D) else Color(0xFFEADDFF)
    val titleColor = if (isDark) Color.White else Color(0xFF21005D)
    val descriptionColor = if (isDark) Color.White.copy(0.6f) else Color(0xFF49454F)

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(bgColor, RoundedCornerShape(18.dp))
            .border(
                1.5.dp,
                if (isCompleted) Color(0xFF2EC4B6).copy(0.3f) else borderColor,
                RoundedCornerShape(18.dp)
            )
            .clickable { onClick() }
            .padding(18.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Daily milestone badge
        Box(
            modifier = Modifier
                .size(46.dp)
                .background(
                    if (isCompleted) Color(0xFF2EC4B6).copy(0.12f) else Color.White.copy(0.05f),
                    shape = RoundedCornerShape(12.dp)
                ),
            contentAlignment = Alignment.Center
        ) {
            if (isCompleted) {
                Icon(
                    imageVector = Icons.Default.Check,
                    contentDescription = "Lesson Completed",
                    tint = Color(0xFF2EC4B6),
                    modifier = Modifier.size(24.dp)
                )
            } else {
                Text(
                    text = "${lesson.day}",
                    color = titleColor,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp
                )
            }
        }

        Spacer(modifier = Modifier.width(16.dp))

        Column(modifier = Modifier.weight(1f)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = lesson.category.uppercase(),
                    fontSize = 9.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = if (isDark) Color(0xFF2EC4B6) else Color(0xFF6750A4),
                    letterSpacing = 1.sp
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = lesson.difficulty,
                    fontSize = 9.sp,
                    color = descriptionColor.copy(0.7f),
                    fontWeight = FontWeight.Medium
                )
            }

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = lesson.title,
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold,
                color = titleColor
            )

            Spacer(modifier = Modifier.height(2.dp))

            Text(
                text = lesson.description,
                fontSize = 12.sp,
                color = descriptionColor,
                lineHeight = 16.sp
            )
        }

        Icon(
            imageVector = Icons.Default.ChevronRight,
            contentDescription = null,
            tint = titleColor.copy(0.3f)
        )
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LessonDetailScreen(
    viewModel: FluentoViewModel,
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    val activeLesson by viewModel.currentLesson.collectAsState()
    val isEvaluating by viewModel.isProcessing.collectAsState()
    val profile by viewModel.userProfile.collectAsState()

    var activeVocabIndex by remember { mutableStateOf(0) }
    var practiceInput by remember { mutableStateOf("") }
    
    var showExplanationState by remember { mutableStateOf(false) }
    var inlineCorrectionText by remember { mutableStateOf<String?>(null) }
    var inlineCorrectionAdvice by remember { mutableStateOf<String?>(null) }
    var isVerifiedCorrect by remember { mutableStateOf(false) }
    
    var evaluatedStatus by remember { mutableStateOf(false) } // true once validated successfully!
    val coroutineScope = rememberCoroutineScope()

    val lesson = activeLesson ?: return

    val vocabList = remember(lesson.vocabulary) {
        lesson.vocabulary.split(",").map { it.trim() }
    }

    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "Day ${lesson.day}: Mission Engine",
                        color = MaterialTheme.colorScheme.onBackground,
                        fontSize = 16.sp,
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
                .imePadding()
                .background(MaterialTheme.colorScheme.background)
                .padding(16.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            if (!evaluatedStatus) {
                Column(verticalArrangement = Arrangement.spacedBy(16.dp), modifier = Modifier.weight(1f)) {
                    
                    // CARD 1: Core Vocabulary Interactive Flasher
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(Color.White.copy(0.03f), RoundedCornerShape(16.dp))
                            .border(1.dp, Color.White.copy(0.05f), RoundedCornerShape(16.dp))
                            .padding(16.dp)
                    ) {
                        Text(
                            text = "interactive flashcards drill".uppercase(),
                            fontSize = 9.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF00D2C4),
                            letterSpacing = 1.2.sp
                        )

                        Spacer(modifier = Modifier.height(14.dp))

                        if (vocabList.isNotEmpty()) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Box(
                                    modifier = Modifier
                                        .weight(1f)
                                        .background(
                                            Brush.verticalGradient(
                                                listOf(Color(0xFF1E283B), Color(0xFF161E2E))
                                            ),
                                            RoundedCornerShape(12.dp)
                                        )
                                        .padding(20.dp),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = vocabList[activeVocabIndex],
                                        fontSize = 20.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = Color.White,
                                        textAlign = TextAlign.Center
                                    )
                                }

                                Spacer(modifier = Modifier.width(12.dp))

                                IconButton(
                                    onClick = {
                                        activeVocabIndex = (activeVocabIndex + 1) % vocabList.size
                                    },
                                    modifier = Modifier.background(Color(0xFF00D2C4), CircleShape)
                                ) {
                                    Icon(Icons.Default.ArrowForward, "Next vocab card", tint = Color.Black)
                                }
                            }
                            
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = "Card ${activeVocabIndex + 1} of ${vocabList.size} • Tap arrow to toggle terms",
                                fontSize = 11.sp,
                                color = Color.White.copy(0.4f),
                                modifier = Modifier.fillMaxWidth(),
                                textAlign = TextAlign.Center
                            )
                        }
                    }

                    // CARD 2: Grammar Coach Tip Box
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(Color.White.copy(0.02f), RoundedCornerShape(16.dp))
                            .border(1.dp, Color.White.copy(0.04f), RoundedCornerShape(16.dp))
                            .padding(16.dp)
                    ) {
                        Column {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(Icons.Default.Lightbulb, null, tint = Color(0xFFFF9F1C), modifier = Modifier.size(16.dp))
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = "Grammar Coach Tip",
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White
                                )
                            }
                            Spacer(modifier = Modifier.height(6.dp))
                            Text(
                                text = lesson.grammarTip,
                                color = Color.White.copy(0.7f),
                                fontSize = 12.sp,
                                lineHeight = 16.sp
                            )
                        }
                    }

                    // CARD 3: Active Conversational Roleplay Console
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(Color.White.copy(0.03f), RoundedCornerShape(18.dp))
                            .border(1.2.dp, Color(0xFF00D2C4).copy(0.2f), RoundedCornerShape(18.dp))
                            .padding(16.dp)
                    ) {
                        Text(
                            text = "Interactive Roleplay Scenario".uppercase(),
                            fontSize = 9.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF00D2C4),
                            letterSpacing = 1.2.sp
                        )

                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "Starter: \"${lesson.conversationStarter}\"",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White,
                            lineHeight = 17.sp
                        )

                        Spacer(modifier = Modifier.height(10.dp))
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(Color.White.copy(0.04f), RoundedCornerShape(10.dp))
                                .padding(10.dp)
                        ) {
                            Column {
                                Text(
                                    text = "Your Goal:",
                                    fontSize = 11.sp,
                                    color = Color.White.copy(0.5f),
                                    fontWeight = FontWeight.Bold
                                )
                                Text(
                                    text = lesson.practicePrompt,
                                    fontSize = 12.sp,
                                    color = Color.White.copy(0.85f),
                                    lineHeight = 16.sp
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        // Interaction response input
                        OutlinedTextField(
                            value = practiceInput,
                            onValueChange = { practiceInput = it },
                            modifier = Modifier.fillMaxWidth().testTag("lesson_practice_input"),
                            placeholder = {
                                Text(
                                    "Reply in ${profile?.targetLanguage}...",
                                    color = Color.White.copy(0.4f),
                                    fontSize = 13.sp
                                )
                            },
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedTextColor = Color.White,
                                unfocusedTextColor = Color.White,
                                focusedBorderColor = Color(0xFF00D2C4)
                            ),
                            shape = RoundedCornerShape(10.dp),
                            maxLines = 2
                        )
                    }

                    // Render evaluation error block if caught
                    if (inlineCorrectionText != null) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(Color(0xFFFF4D6D).copy(0.06f), RoundedCornerShape(12.dp))
                                .border(1.dp, Color(0xFFFF4D6D).copy(0.15f), RoundedCornerShape(12.dp))
                                .padding(12.dp)
                        ) {
                            Text(
                                text = "Correction Hint: \"$inlineCorrectionText\"",
                                fontSize = 12.sp,
                                color = Color(0xFF2EC4B6),
                                fontWeight = FontWeight.Bold
                            )
                            if (inlineCorrectionAdvice != null) {
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = inlineCorrectionAdvice!!,
                                    fontSize = 11.sp,
                                    color = Color.White.copy(0.7f),
                                    lineHeight = 14.sp
                                )
                            }
                        }
                    }
                }

                // CTA Validation Trigger
                Button(
                    onClick = {
                        if (practiceInput.trim().isEmpty()) return@Button
                        
                        // Set progress, validate grammar
                        coroutineScope.launch {
                            showExplanationState = true
                            val profileInfo = profile ?: com.example.data.UserProfile()
                            val correction = GeminiService.getCorrection(practiceInput, profileInfo.targetLanguage)
                            
                            isVerifiedCorrect = correction.isCorrect
                            if (!correction.isCorrect) {
                                inlineCorrectionText = correction.correctedText
                                inlineCorrectionAdvice = correction.explanation
                                
                                // Save mistake
                                viewModel.sendTextChat(practiceInput)
                            } else {
                                inlineCorrectionText = null
                                inlineCorrectionAdvice = null
                            }
                            
                            // Let's complete the lesson successfully after showing details
                            viewModel.completeLesson(lesson)
                            evaluatedStatus = true
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(52.dp)
                        .testTag("verify_lesson_button"),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF00D2C4))
                ) {
                    Text(
                        "Submit & Complete Mission",
                        color = Color.Black,
                        fontWeight = FontWeight.Bold,
                        fontSize = 15.sp
                    )
                }
            } else {
                // SCREEN 4: SPLENDID LESSON SUCCESS CONSOLE
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Box(
                        modifier = Modifier
                            .size(100.dp)
                            .background(Color(0xFF2EC4B6).copy(0.12f), CircleShape)
                            .border(2.dp, Color(0xFF2EC4B6), CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.EmojiEvents,
                            contentDescription = "Lesson success trophy",
                            tint = Color(0xFF2EC4B6),
                            modifier = Modifier.size(50.dp)
                        )
                    }

                    Spacer(modifier = Modifier.height(30.dp))

                    Text(
                        text = "Day ${lesson.day} Completed!",
                        fontSize = 28.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = Color.White,
                        textAlign = TextAlign.Center
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    Text(
                        text = "Outstanding! You completed your daily mission: '${lesson.title}'.",
                        fontSize = 15.sp,
                        color = Color.White.copy(0.7f),
                        textAlign = TextAlign.Center
                    )

                    Spacer(modifier = Modifier.height(24.dp))

                    Row(
                        modifier = Modifier
                            .background(Color.White.copy(0.04f), RoundedCornerShape(12.dp))
                            .padding(horizontal = 24.dp, vertical = 12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(Icons.Default.Add, null, tint = Color(0xFFFF9F1C), modifier = Modifier.size(16.dp))
                        Text(
                            text = "35 XP Points Earned • +5 Mins",
                            color = Color(0xFFFF9F1C),
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp
                        )
                    }

                    Spacer(modifier = Modifier.height(40.dp))

                    Button(
                        onClick = {
                            evaluatedStatus = false
                            practiceInput = ""
                            onBack()
                        },
                        modifier = Modifier.fillMaxWidth().height(52.dp).testTag("dismiss_success_lesson"),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2EC4B6))
                    ) {
                        Text(
                            "Return to Roadmap",
                            color = Color.Black,
                            fontWeight = FontWeight.Bold,
                            fontSize = 15.sp
                        )
                    }
                }
            }
        }
    }
}
