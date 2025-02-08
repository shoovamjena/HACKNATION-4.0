package com.example.animalwonders.uicomponents.home

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.example.animalwonders.ui.theme.PrimaryPinkDark
import java.util.Calendar

@Composable
fun TimeBasedGreeting (){
    val currentTime = Calendar.getInstance().get(Calendar.HOUR_OF_DAY)
    val contentColor = PrimaryPinkDark
    val greetingMessage = when{
        currentTime in 4..11 -> "Good Morning!!"
        currentTime in 12..15 -> "Good Afternoon!!"
        else -> "Good Evening!!"
    }

    Text(
        text = greetingMessage,
        color = contentColor,
        style = MaterialTheme.typography.headlineMedium,
        fontWeight = FontWeight.ExtraBold,
        modifier = Modifier.alpha(0.6f)
    )

}