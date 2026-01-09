package com.biligo.ui.screen.main

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.biligo.ui.screen.home.HomeScreen
import com.biligo.ui.screen.login.LoginScreen
import com.biligo.ui.screen.profile.ProfileScreen
import com.biligo.ui.screen.video.VideoPlayerScreen
import com.biligo.ui.theme.BiliGoTheme
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber

/**
 * 主活动
 */
@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Timber.d("MainActivity created")
        
        setContent {
            BiliGoTheme {
                val navController = rememberNavController()
                MainScreen(navController = navController)
            }
        }
    }
}

/**
 * 主屏幕导航
 */
@Composable
fun MainScreen(navController: NavHostController) {
    var selectedTab by remember { mutableStateOf(0) }
    
    Scaffold(
        backgroundColor = Color(0xFF29303A),
        bottomBar = {
            MainBottomNavigation(
                selectedTab = selectedTab,
                onTabSelected = { index ->
                    selectedTab = index
                    when (index) {
                        0 -> navController.navigate("home")
                        1 -> navController.navigate("discover")
                        2 -> navController.navigate("profile")
                    }
                }
            )
        }
    ) { paddingValues ->
        NavHost(
            navController = navController,
            startDestination = "login",
            modifier = Modifier.padding(paddingValues)
        ) {
            // 登录页面
            composable("login") {
                LoginScreen(
                    onLoginSuccess = {
                        navController.navigate("home") {
                            popUpTo("login") { inclusive = true }
                        }
                    },
                    onSkipLogin = {
                        navController.navigate("home") {
                            popUpTo("login") { inclusive = true }
                        }
                    }
                )
            }
            
            // 首页
            composable("home") {
                HomeScreen(
                    onVideoClick = { video ->
                        navController.navigate("video/${video.bvid}")
                    }
                )
            }
            
            // 发现页面
            composable("discover") {
                // TODO: 实现发现页面
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp)
                ) {
                    Text(
                        text = "发现页面",
                        color = Color.White,
                        fontSize = 24.sp
                    )
                }
            }
            
            // 个人中心
            composable("profile") {
                ProfileScreen()
            }
            
            // 视频播放页面
            composable("video/{bvid}") { backStackEntry ->
                val bvid = backStackEntry.arguments?.getString("bvid") ?: ""
                VideoPlayerScreen(
                    bvid = bvid,
                    onBack = {
                        navController.popBackStack()
                    }
                )
            }
        }
    }
}

/**
 * 底部导航栏
 */
@Composable
fun MainBottomNavigation(
    selectedTab: Int,
    onTabSelected: (Int) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(80.dp)
            .background(Color(0xFF1A1A1A)),
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.CenterVertically
    ) {
        NavigationItem(
            icon = "🏠",
            label = "首页",
            isSelected = selectedTab == 0,
            onClick = { onTabSelected(0) }
        )
        NavigationItem(
            icon = "🔍",
            label = "发现",
            isSelected = selectedTab == 1,
            onClick = { onTabSelected(1) }
        )
        NavigationItem(
            icon = "👤",
            label = "我的",
            isSelected = selectedTab == 2,
            onClick = { onTabSelected(2) }
        )
    }
}

@Composable
fun NavigationItem(
    icon: String,
    label: String,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .clickable { onClick() }
            .padding(horizontal = 32.dp, vertical = 12.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = icon,
            fontSize = 24.sp,
            color = if (isSelected) Color(0xFFFB7299) else Color.White.copy(alpha = 0.6f)
        )
        Text(
            text = label,
            fontSize = 14.sp,
            color = if (isSelected) Color(0xFFFB7299) else Color.White.copy(alpha = 0.6f),
            modifier = Modifier.padding(top = 4.dp)
        )
    }
}