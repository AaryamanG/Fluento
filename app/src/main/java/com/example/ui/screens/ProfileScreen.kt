package com.example.ui.screens

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.widget.Toast
import androidx.compose.animation.*
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.FluentoViewModel
import com.example.data.UserProfile
import com.example.service.SystemPrompts

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    viewModel: FluentoViewModel,
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    val profileState by viewModel.userProfile.collectAsState()
    val profile = profileState ?: UserProfile()
    val scrollState = rememberScrollState()
    val context = LocalContext.current

    val isDark = isSystemInDarkTheme()
    val bgColor = if (isDark) Color(0xFF232023) else Color(0xFFFFFFFF)
    val borderColor = if (isDark) Color(0xFF3D3A3D) else Color(0xFFEADDFF)
    val labelColor = if (isDark) Color.White.copy(0.5f) else Color(0xFF49454F)
    val normalTextColor = if (isDark) Color.White else Color(0xFF1D1B1E)

    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "My Coach Cockpit",
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
                .verticalScroll(scrollState)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(18.dp)
        ) {
            
            // PROFILE AVATAR BLOCK
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(bgColor, RoundedCornerShape(18.dp))
                    .border(1.dp, borderColor, RoundedCornerShape(18.dp))
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(56.dp)
                        .background(Color(0xFF00D2C4).copy(0.12f), CircleShape)
                        .border(1.dp, Color(0xFF00D2C4), CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(Icons.Default.Person, null, tint = Color(0xFF00D2C4), modifier = Modifier.size(28.dp))
                }

                Spacer(modifier = Modifier.width(16.dp))

                Column {
                    Text(text = "Language Learner", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = normalTextColor)
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(modifier = Modifier.size(6.dp).background(Color(0xFF2EC4B6), CircleShape))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "${profile.proficiencyLevel} • Native: ${profile.nativeLanguage}",
                            fontSize = 12.sp,
                            color = labelColor
                        )
                    }
                }
            }

            // COACH PREFERENCES BLOCK
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text(
                    text = "COACH SPECIFICATIONS",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = if (isDark) Color.White.copy(0.6f) else Color(0xFF6750A4),
                    letterSpacing = 1.2.sp
                )

                PreferenceCard(icon = Icons.Default.Language, label = "Target Language", value = profile.targetLanguage)
                PreferenceCard(icon = Icons.Default.Timeline, label = "Reason for Study", value = profile.studyReason)
                PreferenceCard(icon = Icons.Default.Psychology, label = "Preferred Style", value = profile.learningStyle)
                PreferenceCard(icon = Icons.Default.Shield, label = "Tutor Persona", value = profile.tutorPersonality)
            }

            // MEMBERSHIP BILLING BOX
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text(
                    text = "BILLING DETAILS",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = if (isDark) Color.White.copy(0.6f) else Color(0xFF6750A4),
                    letterSpacing = 1.2.sp
                )

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(bgColor, RoundedCornerShape(14.dp))
                        .border(1.dp, borderColor, RoundedCornerShape(14.dp))
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = if (profile.isPremium) "Fluento Premium active" else "Fluento Basic Plan",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            color = normalTextColor
                        )
                        Text(
                            text = if (profile.isPremium) "Renewal scheduled for June 2026" else "Upgrade on Call to unlock oral drills",
                            fontSize = 11.sp,
                            color = labelColor
                        )
                    }

                    Switch(
                        checked = profile.isPremium,
                        onCheckedChange = { viewModel.togglePremium(it) },
                        colors = SwitchDefaults.colors(checkedThumbColor = Color(0xFF00D2C4))
                    )
                }
            }

            // DYNAMIC DEVELOPER ENGINE AREA: Raw AI System Prompts Exploration drawer
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text(
                    text = "AI ENGINE SYSTEM PROMPTS EXPLORER",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = if (isDark) Color(0xFF00D2C4) else Color(0xFF6750A4),
                    letterSpacing = 1.2.sp
                )
                Text(
                    text = "Review and directly copy the underlying, production-proven system prompts configuring Fluento's Gemini coach models.",
                    fontSize = 11.sp,
                    color = labelColor,
                    lineHeight = 14.sp
                )

                Spacer(modifier = Modifier.height(4.dp))

                PromptExplorerCard(
                    title = "onboarding-to-study-plan generator boost",
                    promptText = SystemPrompts.ONBOARDING_TO_STUDY_PLAN_GENERATOR,
                    context = context
                )

                PromptExplorerCard(
                    title = "real-time grammatical correction parser",
                    promptText = SystemPrompts.REAL_TIME_CORRECTION,
                    context = context
                )

                PromptExplorerCard(
                    title = "pronunciation coach feedback details",
                    promptText = SystemPrompts.PRONUNCIATION_FEEDBACK,
                    context = context
                )

                PromptExplorerCard(
                    title = "daily lesson generator compiler",
                    promptText = SystemPrompts.DAILY_LESSON_GENERATOR,
                    context = context
                )

                PromptExplorerCard(
                    title = "weekly summary report cards",
                    promptText = SystemPrompts.WEEKLY_SUMMARY,
                    context = context
                )
            }
        }
    }
}

@Composable
fun PreferenceCard(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    label: String,
    value: String
) {
    val isDark = isSystemInDarkTheme()
    val bgColor = if (isDark) Color(0xFF232023) else Color(0xFFFFFFFF)
    val borderColor = if (isDark) Color(0xFF3D3A3D) else Color(0xFFEADDFF)
    val labelColor = if (isDark) Color.White.copy(0.5f) else Color(0xFF49454F)
    val normalTextColor = if (isDark) Color.White else Color(0xFF1D1B1E)

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(bgColor, RoundedCornerShape(12.dp))
            .border(1.dp, borderColor, RoundedCornerShape(12.dp))
            .padding(12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(imageVector = icon, contentDescription = null, tint = Color(0xFF00D2C4), modifier = Modifier.size(18.dp))
        Spacer(modifier = Modifier.width(12.dp))
        Column {
            Text(text = label, fontSize = 10.sp, color = labelColor)
            Text(text = value, fontSize = 13.sp, color = normalTextColor, fontWeight = FontWeight.SemiBold)
        }
    }
}

@Composable
fun PromptExplorerCard(
    title: String,
    promptText: String,
    context: Context
) {
    var isExpanded by remember { mutableStateOf(false) }
    val isDark = isSystemInDarkTheme()
    val bgColor = if (isDark) Color(0xFF232023) else Color(0xFFFFFFFF)
    val borderColor = if (isDark) Color(0xFF3D3A3D) else Color(0xFFEADDFF)
    val normalTextColor = if (isDark) Color.White else Color(0xFF1D1B1E)

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(bgColor, RoundedCornerShape(14.dp))
            .border(1.dp, borderColor, RoundedCornerShape(14.dp))
            .clickable { isExpanded = !isExpanded }
            .padding(14.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.Terminal, null, tint = Color(0xFF00D2C4), modifier = Modifier.size(16.dp))
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = title.uppercase(),
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = normalTextColor
                )
            }

            Icon(
                imageVector = if (isExpanded) Icons.Default.ExpandLess else Icons.Default.ExpandMore,
                contentDescription = null,
                tint = normalTextColor.copy(0.5f),
                modifier = Modifier.size(16.dp)
            )
        }

        if (isExpanded) {
            Spacer(modifier = Modifier.height(10.dp))
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.Black.copy(0.4f), RoundedCornerShape(8.dp))
                    .padding(8.dp)
            ) {
                Text(
                    text = promptText.trim(),
                    fontSize = 11.sp,
                    fontFamily = FontFamily.Monospace,
                    color = Color(0xFF2EC4B6),
                    lineHeight = 15.sp
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            Button(
                onClick = {
                    val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                    val clip = ClipData.newPlainText("Fluento Prompt", promptText)
                    clipboard.setPrimaryClip(clip)
                    Toast.makeText(context, "System Prompt Copied!", Toast.LENGTH_SHORT).show()
                },
                modifier = Modifier.fillMaxWidth().testTag("copy_prompt_test_tag"),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF00D2C4).copy(0.12f)),
                shape = RoundedCornerShape(8.dp)
            ) {
                Icon(Icons.Default.ContentCopy, null, tint = Color(0xFF00D2C4), modifier = Modifier.size(14.dp))
                Spacer(modifier = Modifier.width(6.dp))
                Text("Copy System Prompt", color = Color(0xFF00D2C4), fontSize = 11.sp, fontWeight = FontWeight.Bold)
            }
        }
    }
}
