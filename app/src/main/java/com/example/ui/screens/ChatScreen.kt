package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.DeleteSweep
import androidx.compose.material.icons.filled.Translate
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.FluentoViewModel
import com.example.data.ChatMessage
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatScreen(
    viewModel: FluentoViewModel,
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    val messages by viewModel.textMessages.collectAsState()
    val isTyping by viewModel.isProcessing.collectAsState()
    val profile by viewModel.userProfile.collectAsState()

    var textInput by remember { mutableStateOf("") }
    val listState = rememberLazyListState()
    val coroutineScope = rememberCoroutineScope()

    // Auto-scroll when messages update
    LaunchedEffect(messages.size, isTyping) {
        if (messages.isNotEmpty()) {
            listState.animateScrollToItem(messages.size - 1)
        }
    }

    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(34.dp)
                                .background(Color(0xFF00D2C4).copy(0.12f), CircleShape)
                                .border(1.dp, Color(0xFF00D2C4), CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = (profile?.tutorPersonality ?: "F").take(1),
                                color = Color(0xFF00D2C4),
                                fontWeight = FontWeight.Bold,
                                fontSize = 14.sp
                            )
                        }
                        Spacer(modifier = Modifier.width(12.dp))
                        Column {
                            Text(
                                text = "Coach ${profile?.tutorPersonality?.split(" ")?.get(0) ?: "Tutor"}",
                                fontSize = 15.sp,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onBackground
                            )
                            Text(
                                text = "Text Practice • ${profile?.targetLanguage ?: "Spanish"}",
                                fontSize = 11.sp,
                                color = MaterialTheme.colorScheme.onBackground.copy(0.6f)
                            )
                        }
                    }
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back", tint = MaterialTheme.colorScheme.onBackground)
                    }
                },
                actions = {
                    IconButton(onClick = { viewModel.clearHistory("Text") }) {
                        Icon(
                            imageVector = Icons.Default.DeleteSweep,
                            contentDescription = "Clear Chat history",
                            tint = MaterialTheme.colorScheme.onBackground.copy(0.6f)
                        )
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
        ) {
            // LazyColumn message board
            LazyColumn(
                state = listState,
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .padding(horizontal = 14.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp),
                contentPadding = PaddingValues(top = 12.dp, bottom = 12.dp)
            ) {
                if (messages.isEmpty()) {
                    item {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 48.dp),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(64.dp)
                                    .background(Color.White.copy(0.04f), CircleShape),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Translate,
                                    contentDescription = null,
                                    tint = Color(0xFF00D2C4),
                                    modifier = Modifier.size(28.dp)
                                )
                            }
                            Spacer(modifier = Modifier.height(16.dp))
                            Text(
                                text = "Start Your Dialogue",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "Type any sentence in ${profile?.targetLanguage ?: "Spanish"}.\nOur coach will guide and fix typos instantly!",
                                fontSize = 13.sp,
                                color = Color.White.copy(0.5f),
                                textAlign = TextAlign.Center,
                                lineHeight = 18.sp
                            )
                        }
                    }
                } else {
                    items(messages) { message ->
                        ChatBubbleRow(message = message, profile = profile)
                    }
                }

                if (isTyping) {
                    item {
                        Row(
                            horizontalArrangement = Arrangement.Start,
                            modifier = Modifier.fillMaxWidth().padding(vertical = 6.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .background(Color.White.copy(0.04f), RoundedCornerShape(16.dp))
                                    .padding(horizontal = 16.dp, vertical = 12.dp)
                            ) {
                                Text(
                                    text = "Coach is typing correction...",
                                    color = Color.White.copy(0.6f),
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.Medium
                                )
                            }
                        }
                    }
                }
            }

            // Chat input Row
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color(0xFF131A22))
                    .padding(horizontal = 12.dp, vertical = 12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                OutlinedTextField(
                    value = textInput,
                    onValueChange = { textInput = it },
                    modifier = Modifier
                        .weight(1f)
                        .testTag("chat_input_field"),
                    placeholder = {
                        Text(
                            "Type in ${profile?.targetLanguage ?: "Spanish"}...",
                            color = Color.White.copy(0.4f),
                            fontSize = 14.sp
                        )
                    },
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedTextColor = Color.White,
                        unfocusedTextColor = Color.White,
                        focusedBorderColor = Color(0xFF00D2C4),
                        unfocusedBorderColor = Color.White.copy(0.12f),
                        focusedContainerColor = Color.White.copy(0.02f),
                        unfocusedContainerColor = Color.White.copy(0.02f)
                    ),
                    shape = RoundedCornerShape(24.dp),
                    maxLines = 3
                )

                Spacer(modifier = Modifier.width(10.dp))

                Box(
                    modifier = Modifier
                        .size(46.dp)
                        .background(
                            if (textInput.trim().isNotEmpty()) Color(0xFF00D2C4) else Color.White.copy(0.06f),
                            CircleShape
                        )
                        .clickable(enabled = textInput.trim().isNotEmpty()) {
                            viewModel.sendTextChat(textInput)
                            textInput = ""
                        },
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Default.Send,
                        contentDescription = "Send text",
                        tint = if (textInput.trim().isNotEmpty()) Color.Black else Color.White.copy(0.3f),
                        modifier = Modifier.size(18.dp)
                    )
                }
            }
        }
    }
}

@Composable
fun ChatBubbleRow(
    message: ChatMessage,
    profile: com.example.data.UserProfile?
) {
    val isUser = message.sender == "USER"
    var showAlternatives by remember { mutableStateOf(false) }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        horizontalArrangement = if (isUser) Arrangement.End else Arrangement.Start
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(0.85f),
            horizontalAlignment = if (isUser) Alignment.End else Alignment.Start
        ) {
            // Main Bubble Card
            Box(
                modifier = Modifier
                    .background(
                        if (isUser) Color(0xFF00D2C4).copy(0.15f) else Color.White.copy(0.04f),
                        shape = RoundedCornerShape(
                            topStart = 16.dp,
                            topEnd = 16.dp,
                            bottomStart = if (isUser) 16.dp else 4.dp,
                            bottomEnd = if (isUser) 4.dp else 16.dp
                        )
                    )
                    .border(
                        1.dp,
                        if (isUser) Color(0xFF00D2C4).copy(0.3f) else Color.White.copy(0.06f),
                        shape = RoundedCornerShape(
                            topStart = 16.dp,
                            topEnd = 16.dp,
                            bottomStart = if (isUser) 16.dp else 4.dp,
                            bottomEnd = if (isUser) 4.dp else 16.dp
                        )
                    )
                    .padding(horizontal = 16.dp, vertical = 12.dp)
            ) {
                Column {
                    Text(
                        text = message.text,
                        color = Color.White,
                        fontSize = 15.sp,
                        lineHeight = 20.sp,
                        textDecoration = if (isUser && message.isCorrected) TextDecoration.LineThrough else TextDecoration.None
                    )
                }
            }

            // Inline grammar correction block (if mistake is caught)
            if (isUser && message.isCorrected) {
                Spacer(modifier = Modifier.height(6.dp))
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color(0xFFFF4D6D).copy(0.08f), RoundedCornerShape(12.dp))
                        .border(1.dp, Color(0xFFFF4D6D).copy(0.2f), RoundedCornerShape(12.dp))
                        .padding(12.dp)
                ) {
                    // Correction heading
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(18.dp)
                                .background(Color(0xFF2EC4B6), CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            Text("✓", color = Color.Black, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                        }
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = message.correctedText ?: "",
                            color = Color(0xFF2EC4B6),
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    if (message.explanation != null) {
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = "💡 Coach Explains:",
                            fontSize = 11.sp,
                            color = Color.White.copy(0.5f),
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = message.explanation,
                            color = Color.White.copy(0.80f),
                            fontSize = 12.sp,
                            lineHeight = 16.sp,
                            modifier = Modifier.padding(top = 2.dp)
                        )
                    }

                    // Alternatives dropdown clicker
                    if (!message.alternative1.isNullOrEmpty()) {
                        Spacer(modifier = Modifier.height(8.dp))
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier
                                .clickable { showAlternatives = !showAlternatives }
                                .background(Color.White.copy(0.03f), RoundedCornerShape(6.dp))
                                .padding(horizontal = 8.dp, vertical = 4.dp)
                        ) {
                            Text(
                                text = if (showAlternatives) "Hide Better Alternatives ▲" else "Learn Native Alternatives ▼",
                                color = Color(0xFF00D2C4),
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }

                        if (showAlternatives) {
                            Spacer(modifier = Modifier.height(6.dp))
                            Text(
                                text = "1. ${message.alternative1}",
                                fontSize = 12.sp,
                                color = Color.White,
                                fontWeight = FontWeight.Medium
                            )
                            if (!message.alternative2.isNullOrEmpty()) {
                                Spacer(modifier = Modifier.height(3.dp))
                                Text(
                                    text = "2. ${message.alternative2}",
                                    fontSize = 12.sp,
                                    color = Color.White,
                                    fontWeight = FontWeight.Medium
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
