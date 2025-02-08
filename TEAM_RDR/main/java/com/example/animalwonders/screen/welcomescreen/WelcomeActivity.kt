package com.example.animalwonders.screen.welcomescreen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.compose.animation.core.*
import androidx.compose.runtime.getValue
import androidx.compose.ui.graphics.TileMode
import androidx.compose.ui.platform.LocalDensity
import com.example.animalwonders.R
import com.example.animalwonders.ui.theme.DarkTextColor
import com.example.animalwonders.ui.theme.PrimaryPinkBlended
import com.example.animalwonders.ui.theme.PrimaryYellow
import com.example.animalwonders.ui.theme.PrimaryYellowDark
import com.example.animalwonders.ui.theme.PrimaryYellowLight
import com.example.animalwonders.uicomponents.welcome.welcomeButton

@Composable
fun welcomeScreen(
    modifier: Modifier = Modifier,
    navController : NavController
){
    // Animated gradient setup
    // Animated gradient setup
    val gradientOffset by animateFloatAsState(
        targetValue = 1.5f,
        animationSpec = tween(
            durationMillis = 4000,
            easing = LinearEasing),
        label = "",
    )

    val gradientColors = listOf(PrimaryPinkBlended, PrimaryYellowLight,PrimaryYellow)
    val animatedBrush = Brush.verticalGradient(
        colors = gradientColors,
        startY = 0f,
        endY = with(LocalDensity.current) { 1000.dp.toPx() * gradientOffset },
        tileMode = TileMode.Clamp
    )

    Column (
        modifier = modifier
            .fillMaxSize()
            .background(animatedBrush)
            .systemBarsPadding(),
        horizontalAlignment = Alignment.CenterHorizontally
    ){
        Spacer(
            modifier = Modifier.height(58.dp)
        )
        Image(painter = painterResource(R.drawable.welcome_animal), contentDescription = "Welcome Animal")
        Spacer(
            modifier = Modifier.height(58.dp)
        )
        Text(text= "Welcome to Animal Fun World!",
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Black,
            color = DarkTextColor)
        Spacer(
                modifier = Modifier.height(18.dp)
        )
        Text(
            text= "Meet, play, and learn with\namazing 3D animal friends!",
            modifier = Modifier.padding(horizontal = 24.dp),
            style = MaterialTheme.typography.bodyLarge,
            textAlign = TextAlign.Center,
            color = DarkTextColor)
        Spacer(
            modifier = Modifier.weight(weight = 1f)
        )
        welcomeButton(
            text = "Let's Get Started!!!",
            isNavigationArrowVisible = true,
            onClicked = { navController.navigate("signup_screen") },
            colors = ButtonDefaults.buttonColors(
                containerColor = PrimaryYellowDark,
                contentColor = DarkTextColor
            ),
            shadowColor = PrimaryYellowDark,
        )
    }
}
