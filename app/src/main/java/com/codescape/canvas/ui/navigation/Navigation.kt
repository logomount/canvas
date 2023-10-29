package com.codescape.canvas.ui.navigation

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ChevronLeft
import androidx.compose.material3.Button
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.codescape.canvas.ui.animations.PathAnimation
import com.codescape.canvas.ui.animations.Spinner
import com.codescape.canvas.ui.blendmodes.DrawWithContent
import com.codescape.canvas.ui.blendmodes.PieChart
import com.codescape.canvas.ui.blendmodes.PieSlice
import com.codescape.canvas.ui.rendereffects.BlurEffectComponent
import com.codescape.canvas.ui.shaders.SHADER_1
import com.codescape.canvas.ui.shaders.ShaderBrushGradient
import com.codescape.canvas.ui.shaders.ShaderComponent
import com.codescape.canvas.ui.text.TextWithShadow

sealed class Screen(
    val route: String,
    val title: String,
    val showBackButton: Boolean = true
) {
    data object Menu: Screen(
        route = "menu",
        title = "Menu",
        showBackButton = false
    )
    data object PathAnimation: Screen(
        route = "path_animation",
        title = "Path Animation"
    )
    data object Transformations: Screen(
        route = "transformations",
        title = "Transformations"
    )
    data object BlendModes: Screen(
        route = "blend_modes",
        title = "Blend Modes"
    )
    data object DrawWithContent: Screen(
        route = "draw_with_content",
        title = "Draw With Content"
    )
    data object TextWithShadow: Screen(
        route = "text_with_shadow",
        title = "Text With Shadow"
    )
    data object BlurEffect: Screen(
        route = "blur_effect",
        title = "Blur Effect"
    )
    data object ShaderBrush: Screen(
        route = "shader_brush",
        title = "Shader Brush"
    )
    data object Shader: Screen(
        route = "shader",
        title = "Shader"
    )
}

val allScreens = listOf(
    Screen.PathAnimation,
    Screen.Transformations,
    Screen.BlendModes,
    Screen.DrawWithContent,
    Screen.TextWithShadow,
    Screen.BlurEffect,
    Screen.ShaderBrush,
    Screen.Shader
)

@RequiresApi(Build.VERSION_CODES.TIRAMISU)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Navigation() {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val route = remember(navBackStackEntry) { navBackStackEntry?.destination?.route }
    val currentScreen = remember(route) {
        when (route) {
            Screen.BlurEffect.route -> Screen.BlurEffect
            Screen.BlendModes.route -> Screen.BlendModes
            Screen.DrawWithContent.route -> Screen.DrawWithContent
            Screen.Menu.route -> Screen.Menu
            Screen.PathAnimation.route -> Screen.PathAnimation
            Screen.Shader.route -> Screen.Shader
            Screen.ShaderBrush.route -> Screen.ShaderBrush
            Screen.Transformations.route -> Screen.Transformations
            Screen.TextWithShadow.route -> Screen.TextWithShadow
            else -> null
        }
    }
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    currentScreen?.let {
                        Text(text = currentScreen.title)
                    }
                },
                navigationIcon = {
                    if (currentScreen?.showBackButton == true) {
                        IconButton(onClick = { navController.navigateUp() }) {
                            Icon(
                                imageVector = Icons.Rounded.ChevronLeft,
                                contentDescription = "Chevron Left"
                            )
                        }
                    }
                }
            )
        },
        modifier = Modifier.fillMaxSize()
    ) { padding ->
        NavHost(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
            navController = navController,
            startDestination = Screen.Menu.route,
        ) {
            composable(route = Screen.Menu.route) {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(24.dp),
                    verticalArrangement = Arrangement.spacedBy(
                        space = 16.dp,
                        alignment = Alignment.CenterVertically
                    )
                ) {
                    items(
                        items = allScreens,
                        key = { screen -> screen.route }
                    ) { screen ->
                        Button(
                            modifier = Modifier.fillMaxWidth(),
                            onClick = { navController.navigate(route = screen.route) }
                        ) {
                            Text(text = screen.title)
                        }
                    }
                }
            }
            composable(route = Screen.PathAnimation.route) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    PathAnimation()
                }
            }
            composable(route = Screen.Transformations.route) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Spinner(
                        modifier = Modifier.size(120.dp),
                        color = Color.Black,
                        sectionLength = 12.dp,
                        sectionWidth = 12.dp
                    )
                }
            }
            composable(route = Screen.BlendModes.route) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    val slices = remember {
                        listOf(
                            PieSlice(
                                color = Color.Red,
                                size = 0.5f,
                                name = "first"
                            ),
                            PieSlice(
                                color = Color.Blue,
                                size = 0.3f,
                                name = "two"
                            ),
                            PieSlice(
                                color = Color.Green,
                                size = 0.2f,
                                name = "three"
                            )
                        )
                    }
                    PieChart(
                        modifier = Modifier
                            .aspectRatio(1f)
                            .padding(24.dp),
                        slices = slices,
                        strokeWidth = 64.dp
                    )
                }
            }
            composable(route = Screen.DrawWithContent.route) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    DrawWithContent()
                }
            }
            composable(route = Screen.TextWithShadow.route) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    TextWithShadow(text = "Text")
                }
            }
            composable(route = Screen.BlurEffect.route) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    BlurEffectComponent()
                }
            }
            composable(route = Screen.ShaderBrush.route) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    ShaderBrushGradient(text = "Click Me")
                }
            }
            composable(route = Screen.Shader.route) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    ShaderComponent(shaderScript = SHADER_1)
                }
            }
        }
    }
}