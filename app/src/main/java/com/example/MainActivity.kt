package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.ui.screens.*
import com.example.ui.theme.MyApplicationTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyApplicationTheme {
                MainAppHost()
            }
        }
    }
}

// ROUTE DEFINITIONS
object Routes {
    const val WELCOME = "welcome"
    const val ONBOARDING = "onboarding"
    const val ROADMAP_PREVIEW = "roadmap_preview"
    
    const val DASHBOARD = "dashboard"
    const val CHAT = "chat"
    const val VOICE = "voice"
    const val ROADMAP = "roadmap"
    const val LESSON_DETAIL = "lesson_detail"
    const val REVIEWS = "reviews"
    const val PROFILE = "profile"
    const val PAYWALL = "paywall"
}

@Composable
fun MainAppHost() {
    val viewModel: FluentoViewModel = viewModel()
    val profileState by viewModel.userProfile.collectAsState()

    if (profileState == null) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background),
            contentAlignment = androidx.compose.ui.Alignment.Center
        ) {
            CircularProgressIndicator(color = Color(0xFF00D2C4))
        }
    } else {
        val navController = rememberNavController()
        val currentBackStackEntry by navController.currentBackStackEntryAsState()
        val currentRoute = currentBackStackEntry?.destination?.route

        // Determine start destination ONCE on initial loading and never change it
        val startDestination = remember {
            val completed = profileState?.completedOnboarding ?: false
            if (completed) Routes.DASHBOARD else Routes.WELCOME
        }

        // Determine if we should show bottom navigation bar
        val shouldShowBottomBar = currentRoute in listOf(
            Routes.DASHBOARD,
            Routes.ROADMAP,
            Routes.REVIEWS,
            Routes.PROFILE
        )

        Scaffold(
            modifier = Modifier.fillMaxSize(),
            bottomBar = {
                if (shouldShowBottomBar) {
                    val isDark = isSystemInDarkTheme()
                    val navBgColor = if (isDark) Color(0xFF232023) else Color(0xFFF3EDF7)
                    val navBorderColor = if (isDark) Color(0xFF3D3A3D) else Color(0xFFCAC4D0)
                    val navActiveColor = if (isDark) Color(0xFFD0BCFF) else Color(0xFF6750A4)
                    val navActivePillColor = if (isDark) Color(0xFF4F378B) else Color(0xFFEADDFF)
                    val navInactiveColor = if (isDark) Color(0xFFB0ACB2).copy(0.7f) else Color(0xFF49454F).copy(0.7f)

                    Column {
                        HorizontalDivider(color = navBorderColor, thickness = 1.dp)
                        NavigationBar(
                            containerColor = navBgColor,
                            tonalElevation = 0.dp,
                            modifier = Modifier.height(64.dp)
                        ) {
                            NavigationBarItem(
                                selected = currentRoute == Routes.DASHBOARD,
                                onClick = {
                                    if (currentRoute != Routes.DASHBOARD) {
                                        navController.navigate(Routes.DASHBOARD) {
                                            popUpTo(Routes.DASHBOARD) { inclusive = false }
                                        }
                                    }
                                },
                                icon = { Icon(Icons.Default.Dashboard, contentDescription = "Dashboard") },
                                label = { Text("Cockpit", fontSize = 10.sp, fontWeight = FontWeight.Medium) },
                                colors = NavigationBarItemDefaults.colors(
                                    selectedIconColor = navActiveColor,
                                    selectedTextColor = navActiveColor,
                                    unselectedIconColor = navInactiveColor,
                                    unselectedTextColor = navInactiveColor,
                                    indicatorColor = navActivePillColor
                                )
                            )

                            NavigationBarItem(
                                selected = currentRoute == Routes.ROADMAP,
                                onClick = {
                                    if (currentRoute != Routes.ROADMAP) {
                                        navController.navigate(Routes.ROADMAP) {
                                            popUpTo(Routes.DASHBOARD)
                                        }
                                    }
                                },
                                icon = { Icon(Icons.Default.Map, contentDescription = "Roadmap") },
                                label = { Text("Roadmap", fontSize = 10.sp, fontWeight = FontWeight.Medium) },
                                colors = NavigationBarItemDefaults.colors(
                                    selectedIconColor = navActiveColor,
                                    selectedTextColor = navActiveColor,
                                    unselectedIconColor = navInactiveColor,
                                    unselectedTextColor = navInactiveColor,
                                    indicatorColor = navActivePillColor
                                )
                            )

                            NavigationBarItem(
                                selected = currentRoute == Routes.REVIEWS,
                                onClick = {
                                    if (currentRoute != Routes.REVIEWS) {
                                        navController.navigate(Routes.REVIEWS) {
                                            popUpTo(Routes.DASHBOARD)
                                        }
                                    }
                                },
                                icon = { Icon(Icons.Default.Rule, contentDescription = "Review mistakes") },
                                label = { Text("Reviews", fontSize = 10.sp, fontWeight = FontWeight.Medium) },
                                colors = NavigationBarItemDefaults.colors(
                                    selectedIconColor = navActiveColor,
                                    selectedTextColor = navActiveColor,
                                    unselectedIconColor = navInactiveColor,
                                    unselectedTextColor = navInactiveColor,
                                    indicatorColor = navActivePillColor
                                )
                            )

                            NavigationBarItem(
                                selected = currentRoute == Routes.PROFILE,
                                onClick = {
                                    if (currentRoute != Routes.PROFILE) {
                                        navController.navigate(Routes.PROFILE) {
                                            popUpTo(Routes.DASHBOARD)
                                        }
                                    }
                                },
                                icon = { Icon(Icons.Default.Person, contentDescription = "My Profile") },
                                label = { Text("Profile", fontSize = 10.sp, fontWeight = FontWeight.Medium) },
                                colors = NavigationBarItemDefaults.colors(
                                    selectedIconColor = navActiveColor,
                                    selectedTextColor = navActiveColor,
                                    unselectedIconColor = navInactiveColor,
                                    unselectedTextColor = navInactiveColor,
                                    indicatorColor = navActivePillColor
                                )
                            )
                        }
                    }
                }
            }
        ) { innerPadding ->
            NavHost(
                navController = navController,
                startDestination = startDestination,
                modifier = Modifier.padding(innerPadding).background(MaterialTheme.colorScheme.background)
            ) {
                // WELCOME HERO SCREEN
                composable(Routes.WELCOME) {
                    WelcomeScreen(
                        onStartOnboarding = { navController.navigate(Routes.ONBOARDING) }
                    )
                }

                // ONBOARDING MULTI-STEP FLOW
                composable(Routes.ONBOARDING) {
                    OnboardingScreen(
                        viewModel = viewModel,
                        onOnboardingFinished = { navController.navigate(Routes.ROADMAP_PREVIEW) }
                    )
                }

                // STUDY ROADMAP BUILD LOADING PREVIEW
                composable(Routes.ROADMAP_PREVIEW) {
                    RoadmapPreviewScreen(
                        viewModel = viewModel,
                        onEnterDashboard = {
                            navController.navigate(Routes.DASHBOARD) {
                                popUpTo(Routes.WELCOME) { inclusive = true }
                            }
                        }
                    )
                }

                // PRIMARY COCKPIT DASHBOARD
                composable(Routes.DASHBOARD) {
                    DashboardScreen(
                        viewModel = viewModel,
                        onNavigateToChat = { navController.navigate(Routes.CHAT) },
                        onNavigateToVoice = { navController.navigate(Routes.VOICE) },
                        onNavigateToRoadmap = { navController.navigate(Routes.ROADMAP) },
                        onNavigateToReviews = { navController.navigate(Routes.REVIEWS) },
                        onNavigateToPaywall = { navController.navigate(Routes.PAYWALL) }
                    )
                }

                // SMART CHAT TUTOR SCREEN
                composable(Routes.CHAT) {
                    ChatScreen(
                        viewModel = viewModel,
                        onBack = { navController.popBackStack() }
                    )
                }

                // CALL VOICE PRACTICE SEGMENT
                composable(Routes.VOICE) {
                    VoiceScreen(
                        viewModel = viewModel,
                        onBack = { navController.popBackStack() }
                    )
                }

                // TIMELINE PATH ROADMAPS
                composable(Routes.ROADMAP) {
                    RoadmapScreen(
                        viewModel = viewModel,
                        onNavigateToLessonDetail = { navController.navigate(Routes.LESSON_DETAIL) },
                        onBack = { navController.navigate(Routes.DASHBOARD) }
                    )
                }

                // DETAILED LESSON TERMINAL
                composable(Routes.LESSON_DETAIL) {
                    LessonDetailScreen(
                        viewModel = viewModel,
                        onBack = { navController.popBackStack() }
                    )
                }

                // MISTAKE SPACED REPETITION REVIEW
                composable(Routes.REVIEWS) {
                    ReviewScreen(
                        viewModel = viewModel,
                        onBack = { navController.navigate(Routes.DASHBOARD) }
                    )
                }

                // MY PROFILE COCKPIT
                composable(Routes.PROFILE) {
                    ProfileScreen(
                        viewModel = viewModel,
                        onBack = { navController.navigate(Routes.DASHBOARD) }
                    )
                }

                // PREMIUM ELITE PAYWALL
                composable(Routes.PAYWALL) {
                    PaywallScreen(
                        viewModel = viewModel,
                        onBack = { navController.popBackStack() }
                    )
                }
            }
        }
    }
}
