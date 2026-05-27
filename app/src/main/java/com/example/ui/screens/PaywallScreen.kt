package com.example.ui.screens

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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.FluentoViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun PaywallScreen(
    viewModel: FluentoViewModel,
    modifier: Modifier = Modifier,
    onBack: () -> Unit = {}
) {
    var selectedPlan by remember { mutableStateOf("annual") } // "monthly" or "annual"
    var isCheckingOut by remember { mutableStateOf(false) }
    var isSuccess by remember { mutableStateOf(false) }

    val coroutineScope = rememberCoroutineScope()
    val scrollState = rememberScrollState()

    val isDark = isSystemInDarkTheme()
    val bgColor = MaterialTheme.colorScheme.background
    val textColor = MaterialTheme.colorScheme.onBackground
    val cardBg = if (isDark) Color(0xFF232023) else Color(0xFFFFFFFF)
    val cardBorderColor = if (isDark) Color(0xFF3D3A3D) else Color(0xFFEADDFF)
    val primaryAccent = if (isDark) Color(0xFF00D2C4) else Color(0xFF6750A4)
    val onPrimaryAccent = if (isDark) Color.Black else Color.White
    val secondaryText = textColor.copy(0.7f)

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(bgColor)
    ) {
        if (!isSuccess) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(scrollState)
                    .padding(24.dp)
                    .navigationBarsPadding()
                    .statusBarsPadding()
            ) {
                // Top Exit Row
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(
                        onClick = onBack,
                        modifier = Modifier.background(textColor.copy(0.08f), CircleShape)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "Close paywall",
                            tint = textColor
                        )
                    }

                    Text(
                        text = "TRY FREE FOR 3 DAYS",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = primaryAccent,
                        letterSpacing = 1.5.sp,
                        modifier = Modifier
                            .background(primaryAccent.copy(0.12f), RoundedCornerShape(20.dp))
                            .padding(horizontal = 14.dp, vertical = 6.dp)
                    )
                }

                Spacer(modifier = Modifier.height(28.dp))

                // Heading
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "Fluento Pro",
                        fontSize = 34.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = textColor,
                        textAlign = TextAlign.Center
                    )

                    Spacer(modifier = Modifier.height(6.dp))

                    Text(
                        text = "Break through speaking barriers with your custom AI Coach",
                        fontSize = 15.sp,
                        color = secondaryText,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.padding(horizontal = 16.dp)
                    )
                }

                Spacer(modifier = Modifier.height(28.dp))

                // High Weight Value Checklist
                Column(
                    verticalArrangement = Arrangement.spacedBy(14.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    PremiumFeatureRow(
                        title = "24/7 Voice Dialogues Tutoring",
                        desc = "Initiate instant audio phone call simulations. Total oral training."
                    )
                    PremiumFeatureRow(
                        title = "Full Roleplay Scenarios",
                        desc = "Cafe, Airport, Hotel checkout, Jobs, Dating & Shopping modules unlocked."
                    )
                    PremiumFeatureRow(
                        title = "Personalized Grammar AI",
                        desc = "Deep real-time feedback with corrections card and custom phonetic rules."
                    )
                    PremiumFeatureRow(
                        title = "Zero Daily Speaking Caps",
                        desc = "Accelerate speech milestones rapidly without credit restriction limits."
                    )
                }

                Spacer(modifier = Modifier.height(30.dp))

                // Price selection billing cards
                Column(
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    // Annual Card
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(
                                if (selectedPlan == "annual") primaryAccent.copy(0.12f) else cardBg,
                                RoundedCornerShape(18.dp)
                            )
                            .border(
                                2.dp,
                                if (selectedPlan == "annual") primaryAccent else cardBorderColor,
                                RoundedCornerShape(18.dp)
                            )
                            .clickable { selectedPlan = "annual" }
                            .padding(20.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(
                                    text = "Annual Plan",
                                    fontSize = 18.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = textColor
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = "SAVE 60%",
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.ExtraBold,
                                    color = Color.White,
                                    modifier = Modifier
                                        .background(Color(0xFFFF9F1C), RoundedCornerShape(6.dp))
                                        .padding(horizontal = 6.dp, vertical = 2.dp)
                                )
                            }
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "Only $5.83 / month ($69.99 total)",
                                fontSize = 13.sp,
                                color = secondaryText
                            )
                        }

                        RadioButton(
                            selected = selectedPlan == "annual",
                            onClick = { selectedPlan = "annual" },
                            colors = RadioButtonDefaults.colors(selectedColor = primaryAccent)
                        )
                    }

                    // Monthly Card
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(
                                if (selectedPlan == "monthly") primaryAccent.copy(0.12f) else cardBg,
                                RoundedCornerShape(18.dp)
                            )
                            .border(
                                2.dp,
                                if (selectedPlan == "monthly") primaryAccent else cardBorderColor,
                                RoundedCornerShape(18.dp)
                            )
                            .clickable { selectedPlan = "monthly" }
                            .padding(20.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(
                                text = "Monthly Plan",
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold,
                                color = textColor
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "$14.99 / month • Cancel anytime",
                                fontSize = 13.sp,
                                color = secondaryText
                            )
                        }

                        RadioButton(
                            selected = selectedPlan == "monthly",
                            onClick = { selectedPlan = "monthly" },
                            colors = RadioButtonDefaults.colors(selectedColor = primaryAccent)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(28.dp))

                // Social Reviews
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(cardBg, RoundedCornerShape(18.dp))
                        .border(1.dp, cardBorderColor, RoundedCornerShape(18.dp))
                        .padding(16.dp)
                ) {
                    Column {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.Star,
                                contentDescription = null,
                                tint = Color(0xFFFF9F1C),
                                modifier = Modifier.size(16.dp)
                            )
                            Icon(
                                imageVector = Icons.Default.Star,
                                contentDescription = null,
                                tint = Color(0xFFFF9F1C),
                                modifier = Modifier.size(16.dp)
                            )
                            Icon(
                                imageVector = Icons.Default.Star,
                                contentDescription = null,
                                tint = Color(0xFFFF9F1C),
                                modifier = Modifier.size(16.dp)
                            )
                            Icon(
                                imageVector = Icons.Default.Star,
                                contentDescription = null,
                                tint = Color(0xFFFF9F1C),
                                modifier = Modifier.size(16.dp)
                            )
                            Icon(
                                imageVector = Icons.Default.Star,
                                contentDescription = null,
                                tint = Color(0xFFFF9F1C),
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "LingoApp reviews",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = secondaryText
                            )
                        }
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = "\"Having a speaking partner available at 2:00 AM completely cured my pronunciation block. I actually look forward to studying Spanish now!\"",
                            fontSize = 12.sp,
                            color = textColor.copy(0.8f),
                            lineHeight = 16.sp
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "— Maria K., Intermediate Spanish Student",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = primaryAccent
                        )
                    }
                }

                Spacer(modifier = Modifier.height(30.dp))

                // Big CTA Pay Trigger
                Button(
                    onClick = {
                        isCheckingOut = true
                        coroutineScope.launch {
                            delay(2000) // Simulate secure validation txn
                            viewModel.togglePremium(true)
                            isCheckingOut = false
                            isSuccess = true
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp)
                        .testTag("subscribe_button"),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = primaryAccent,
                        contentColor = onPrimaryAccent
                    ),
                    shape = RoundedCornerShape(16.dp),
                    enabled = !isCheckingOut
                ) {
                    if (isCheckingOut) {
                        CircularProgressIndicator(color = onPrimaryAccent, modifier = Modifier.size(24.dp))
                        Spacer(modifier = Modifier.width(12.dp))
                        Text(
                            text = "Securing Checkout Verification...",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = onPrimaryAccent
                        )
                    } else {
                        val text = if (selectedPlan == "annual") "Start 3-Day Free Trial, then $69.99/yr" else "Secure Upgrade Now - $14.99/mo"
                        Text(
                            text = text,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = onPrimaryAccent
                        )
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                Text(
                    text = "Safe & Secure Checkout • Cancel anytime in Settings",
                    fontSize = 11.sp,
                    color = secondaryText.copy(0.6f),
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(20.dp))
            }
        } else {
            // SUCCESS CONTAINER
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(24.dp)
                    .navigationBarsPadding()
                    .statusBarsPadding(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Box(
                    modifier = Modifier
                        .size(100.dp)
                        .background(Color(0xFF2EC4B6).copy(0.15f), CircleShape)
                        .border(2.dp, Color(0xFF2EC4B6), CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.WorkspacePremium,
                        contentDescription = "Success",
                        tint = Color(0xFF2EC4B6),
                        modifier = Modifier.size(54.dp)
                    )
                }

                Spacer(modifier = Modifier.height(30.dp))

                Text(
                    text = "Welcome to Fluento Pro!",
                    fontSize = 28.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = textColor,
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    text = "Your elite AI Language Coaching plan has successfully initialized. All speaking boundaries have been unlocked!",
                    fontSize = 15.sp,
                    color = secondaryText,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(horizontal = 16.dp)
                )

                Spacer(modifier = Modifier.height(40.dp))

                Button(
                    onClick = {
                        isSuccess = false
                        onBack()
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp)
                        .testTag("dismiss_success_paywall"),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF2EC4B6),
                        contentColor = Color.Black
                    ),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Text(
                        text = "Enter Elite Practice",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Black
                    )
                }
            }
        }
    }
}

@Composable
fun PremiumFeatureRow(
    title: String,
    desc: String
) {
    val isDark = isSystemInDarkTheme()
    val titleColor = MaterialTheme.colorScheme.onBackground
    val descColor = titleColor.copy(0.6f)
    val iconTint = if (isDark) Color(0xFF00D2C4) else Color(0xFF6750A4)
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.Top
    ) {
        Icon(
            imageVector = Icons.Default.CheckCircle,
            contentDescription = null,
            tint = iconTint,
            modifier = Modifier
                .size(20.dp)
                .padding(top = 2.dp)
        )

        Spacer(modifier = Modifier.width(14.dp))

        Column {
            Text(
                text = title,
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold,
                color = titleColor
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = desc,
                fontSize = 13.sp,
                color = descColor,
                lineHeight = 16.sp
            )
        }
    }
}
