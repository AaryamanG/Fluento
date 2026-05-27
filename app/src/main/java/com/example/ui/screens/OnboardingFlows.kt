package com.example.ui.screens

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.FluentoViewModel
import kotlinx.coroutines.delay

@Composable
fun WelcomeScreen(
    onStartOnboarding: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(
                        Color(0xFF0A0E17),
                        Color(0xFF131E31)
                    )
                )
            )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp)
                .navigationBarsPadding()
                .statusBarsPadding(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            // Header Logo Area
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.padding(top = 40.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(90.dp)
                        .background(
                            Brush.sweepGradient(
                                colors = listOf(Color(0xFF00D2C4), Color(0xFF3A86FF), Color(0xFF00D2C4))
                            ),
                            shape = RoundedCornerShape(26.dp)
                        )
                        .border(1.5.dp, Color.White.copy(0.2f), RoundedCornerShape(26.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.RecordVoiceOver,
                        contentDescription = "Fluento logo",
                        tint = Color.White,
                        modifier = Modifier.size(48.dp)
                    )
                }

                Spacer(modifier = Modifier.height(20.dp))

                Text(
                    text = "Fluento",
                    fontSize = 36.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = Color.White,
                    letterSpacing = 1.sp
                )

                Text(
                    text = "YOUR PRIVATE AI LANGUAGE TUTOR",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF00D2C4),
                    letterSpacing = 2.sp
                )
            }

            // Interactive Pitch Cards
            Column(
                verticalArrangement = Arrangement.spacedBy(16.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                PitchCard(
                    imageVector = Icons.Default.SupportAgent,
                    title = "Available 24/7 on Call",
                    description = "Practice conversations anytime, anywhere with zero social anxiety. Unlimited speaking runtime."
                )

                PitchCard(
                    imageVector = Icons.Default.CheckCircle,
                    title = "Instant Grammar Correction",
                    description = "Our smart coach analyzes your vocal submissions or texts in real-time, pointing out exact mistakes."
                )

                PitchCard(
                    imageVector = Icons.Default.Timeline,
                    title = "Personalized Weekly Curriculum",
                    description = "No static vocabulary decks. A tailored study path matching your travel, career, or dating goals."
                )
            }

            // CTA Button Area
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.fillMaxWidth()
            ) {
                Button(
                    onClick = onStartOnboarding,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp)
                        .testTag("get_started_button"),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF00D2C4)),
                    shape = RoundedCornerShape(16.dp),
                    elevation = ButtonDefaults.buttonElevation(defaultElevation = 8.dp)
                ) {
                    Text(
                        text = "Start Free Onboarding",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Black
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    text = "Free practice included • No credit card required",
                    fontSize = 12.sp,
                    color = Color.White.copy(0.5f)
                )
            }
        }
    }
}

@Composable
fun PitchCard(
    imageVector: androidx.compose.ui.graphics.vector.ImageVector,
    title: String,
    description: String
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White.copy(0.04f), RoundedCornerShape(18.dp))
            .border(1.dp, Color.White.copy(0.06f), RoundedCornerShape(18.dp))
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(46.dp)
                .background(Color(0xFF00D2C4).copy(0.12f), RoundedCornerShape(12.dp)),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = imageVector,
                contentDescription = null,
                tint = Color(0xFF00D2C4),
                modifier = Modifier.size(24.dp)
            )
        }

        Spacer(modifier = Modifier.width(16.dp))

        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = title,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = description,
                fontSize = 13.sp,
                color = Color.White.copy(0.7f),
                lineHeight = 18.sp
            )
        }
    }
}


@Composable
fun OnboardingScreen(
    viewModel: FluentoViewModel,
    onOnboardingFinished: () -> Unit,
    modifier: Modifier = Modifier
) {
    var step by remember { mutableStateOf(1) }

    // Question answers
    var targetLanguage by remember { mutableStateOf("Spanish") }
    var proficiencyLevel by remember { mutableStateOf("Beginner (A1)") }
    var nativeLanguage by remember { mutableStateOf("English") }
    var studyReason by remember { mutableStateOf("Travel & Leisure") }
    var learningStyle by remember { mutableStateOf("Conversational Practice") }
    var dailyTime by remember { mutableStateOf("15 mins/day") }
    var tutorPersonality by remember { mutableStateOf("Friendly & Motivating") }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp)
                .navigationBarsPadding()
                .statusBarsPadding(),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            // Steps indicator
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(
                    onClick = { if (step > 1) step-- },
                    enabled = step > 1
                ) {
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = "Back Step",
                        tint = if (step > 1) Color.White else Color.White.copy(0.2f)
                    )
                }

                Text(
                    text = "Step $step of 7",
                    color = Color(0xFF00D2C4),
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp
                )

                Text(
                    text = "${(step * 100 / 7)}%",
                    color = Color.White.copy(0.5f),
                    fontSize = 13.sp
                )
            }

            // Form container
            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(vertical = 24.dp),
                verticalArrangement = Arrangement.Center
            ) {
                when (step) {
                    1 -> OnboardingQuestion(
                        title = "What is your target language?",
                        description = "We will optimize your voice tutor modules for this choice.",
                        options = listOf("Spanish", "French", "Japanese", "German", "Italian"),
                        selectedValue = targetLanguage,
                        onSelected = { targetLanguage = it }
                    )
                    2 -> OnboardingQuestion(
                        title = "What is your current level?",
                        description = "We'll suggest vocabulary and grammar that fits your grade.",
                        options = listOf("Beginner (A1)", "Elementary (A2)", "Intermediate (B1)", "Advanced (C2)"),
                        selectedValue = proficiencyLevel,
                        onSelected = { proficiencyLevel = it }
                    )
                    3 -> OnboardingQuestion(
                        title = "What is your native speaking tongue?",
                        description = "Helps our tutor highlight natural phonetic differences.",
                        options = listOf("English", "French", "Portuguese", "Mandarin", "Spanish"),
                        selectedValue = nativeLanguage,
                        onSelected = { nativeLanguage = it }
                    )
                    4 -> OnboardingQuestion(
                        title = "Why do you want to learn?",
                        description = "Lessons will center on terms relevant to your purpose.",
                        options = listOf("Travel & Leisure", "Job & Career Growth", "Exams & Study", "Dating & Relationships", "General Fluency"),
                        selectedValue = studyReason,
                        onSelected = { studyReason = it }
                    )
                    5 -> OnboardingQuestion(
                        title = "Specify preferred study styles:",
                        description = "Optimize lesson pacing and drill formats.",
                        options = listOf("Conversational Practice", "Flashcards & Vocab", "Strict Grammar Drills", "Bite-Sized Scenarios"),
                        selectedValue = learningStyle,
                        onSelected = { learningStyle = it }
                    )
                    6 -> OnboardingQuestion(
                        title = "Weekly study commitment:",
                        description = "Consistency triggers streaks!",
                        options = listOf("5 mins/day - Relaxed", "15 mins/day - Standard", "30 mins/day - Serious", "60 mins/day - Intense"),
                        selectedValue = dailyTime,
                        onSelected = { dailyTime = it }
                    )
                    7 -> OnboardingQuestion(
                        title = "Select your AI Tutor profile:",
                        description = "Sets the voice response and conversational tone triggers.",
                        options = listOf("Friendly & Motivating", "Professional & Strict", "Fun & Energetic", "Modern & Casual"),
                        selectedValue = tutorPersonality,
                        onSelected = { tutorPersonality = it }
                    )
                }
            }

            // CTAs
            Button(
                onClick = {
                    if (step < 7) {
                        step++
                    } else {
                        // All steps finished, trigger studies setup
                        viewModel.completeOnboarding(
                            targetLanguage, nativeLanguage, proficiencyLevel,
                            studyReason, learningStyle, dailyTime, tutorPersonality
                        )
                        onOnboardingFinished()
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
                    .testTag("onboarding_continue"),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF00D2C4)),
                shape = RoundedCornerShape(16.dp)
            ) {
                Text(
                    text = if (step == 7) "Create My Coach Plan" else "Continue",
                    color = Color.Black,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

@Composable
fun OnboardingQuestion(
    title: String,
    description: String,
    options: List<String>,
    selectedValue: String,
    onSelected: (String) -> Unit
) {
    Column {
        Text(
            text = title,
            fontSize = 24.sp,
            fontWeight = FontWeight.ExtraBold,
            color = Color.White
        )
        
        Spacer(modifier = Modifier.height(6.dp))
        
        Text(
            text = description,
            fontSize = 14.sp,
            color = Color.White.copy(0.6f)
        )
        
        Spacer(modifier = Modifier.height(28.dp))

        options.forEach { option ->
            val isSelected = option == selectedValue
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 6.dp)
                    .background(
                        if (isSelected) Color(0xFF00D2C4).copy(0.08f) else Color.White.copy(0.03f),
                        RoundedCornerShape(14.dp)
                    )
                    .border(
                        1.5.dp,
                        if (isSelected) Color(0xFF00D2C4) else Color.White.copy(0.08f),
                        RoundedCornerShape(14.dp)
                    )
                    .clickable { onSelected(option) }
                    .padding(18.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = option,
                    color = if (isSelected) Color(0xFF00D2C4) else Color.White,
                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                    fontSize = 16.sp
                )
                
                if (isSelected) {
                    Icon(
                        imageVector = Icons.Default.Check,
                        contentDescription = "Selected",
                        tint = Color(0xFF00D2C4),
                        modifier = Modifier.size(20.dp)
                    )
                }
            }
        }
    }
}


@Composable
fun RoadmapPreviewScreen(
    viewModel: FluentoViewModel,
    onEnterDashboard: () -> Unit,
    modifier: Modifier = Modifier
) {
    var loadingStep by remember { mutableStateOf(1) }
    val profile by viewModel.userProfile.collectAsState()

    LaunchedEffect(Unit) {
        delay(1200)
        loadingStep = 2
        delay(1200)
        loadingStep = 3
        delay(1200)
        loadingStep = 4
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFF0A0E17))
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp)
                .navigationBarsPadding()
                .statusBarsPadding(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.padding(top = 40.dp)
            ) {
                Text(
                    text = "Building Your Coach",
                    fontSize = 28.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = Color.White
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Personalizing language models for your level...",
                    fontSize = 14.sp,
                    color = Color.White.copy(0.5f),
                    textAlign = TextAlign.Center
                )
            }

            // Visual Processing Area
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                ProcessingStep(
                    stepNum = 1,
                    text = "Mapping ${profile?.targetLanguage ?: "Target"} Conversational Scenarios",
                    status = when {
                        loadingStep > 1 -> "COMPLETE"
                        loadingStep == 1 -> "ACTIVE"
                        else -> "PENDING"
                    }
                )

                ProcessingStep(
                    stepNum = 2,
                    text = "Booting '${profile?.tutorPersonality ?: "Friendly"}' Voice Engine",
                    status = when {
                        loadingStep > 2 -> "COMPLETE"
                        loadingStep == 2 -> "ACTIVE"
                        else -> "PENDING"
                    }
                )

                ProcessingStep(
                    stepNum = 3,
                    text = "Structuring 30-Day Speakings Plan",
                    status = when {
                        loadingStep > 3 -> "COMPLETE"
                        loadingStep == 3 -> "ACTIVE"
                        else -> "PENDING"
                    }
                )

                ProcessingStep(
                    stepNum = 4,
                    text = "Pre-populating Grammar Checkers",
                    status = when {
                        loadingStep > 4 -> "COMPLETE"
                        loadingStep == 4 -> "ACTIVE"
                        else -> "PENDING"
                    }
                )
            }

            // Completed CTA
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.fillMaxWidth()
            ) {
                if (loadingStep >= 4) {
                    Button(
                        onClick = onEnterDashboard,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(56.dp)
                            .testTag("enter_coachy_button")
                            .animateContentSize(),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF00D2C4)),
                        shape = RoundedCornerShape(16.dp)
                    ) {
                        Text(
                            text = "Launch My Coach",
                            fontWeight = FontWeight.Bold,
                            color = Color.Black,
                            fontSize = 16.sp
                        )
                    }
                } else {
                    CircularProgressIndicator(
                        color = Color(0xFF00D2C4),
                        modifier = Modifier.size(40.dp)
                    )
                }
                Spacer(modifier = Modifier.height(40.dp))
            }
        }
    }
}

@Composable
fun ProcessingStep(
    stepNum: Int,
    text: String,
    status: String
) {
    val isComplete = status == "COMPLETE"
    val isActive = status == "ACTIVE"

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                if (isActive) Color(0xFF00D2C4).copy(0.04f) else Color.White.copy(0.02f),
                RoundedCornerShape(16.dp)
            )
            .border(
                1.dp,
                if (isActive) Color(0xFF00D2C4).copy(0.4f) else Color.White.copy(0.04f),
                RoundedCornerShape(16.dp)
            )
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier
                    .size(28.dp)
                    .background(
                        when {
                            isComplete -> Color(0xFF2EC4B6).copy(0.15f)
                            isActive -> Color(0xFF00D2C4).copy(0.15f)
                            else -> Color.White.copy(0.05f)
                        },
                        shape = RoundedCornerShape(8.dp)
                    ),
                contentAlignment = Alignment.Center
            ) {
                if (isComplete) {
                    Icon(
                        imageVector = Icons.Default.CheckCircle,
                        contentDescription = "Done",
                        tint = Color(0xFF2EC4B6),
                        modifier = Modifier.size(16.dp)
                    )
                } else {
                    Text(
                        text = stepNum.toString(),
                        color = if (isActive) Color(0xFF00D2C4) else Color.White.copy(0.4f),
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
            Spacer(modifier = Modifier.width(16.dp))

            Text(
                text = text,
                fontSize = 14.sp,
                fontWeight = if (isActive) FontWeight.Bold else FontWeight.Normal,
                color = if (isComplete) Color.White.copy(0.6f) else Color.White
            )
        }

        Text(
            text = status,
            fontSize = 11.sp,
            fontWeight = FontWeight.ExtraBold,
            color = when (status) {
                "COMPLETE" -> Color(0xFF2EC4B6)
                "ACTIVE" -> Color(0xFF00D2C4)
                else -> Color.White.copy(0.3f)
            }
        )
    }
}
