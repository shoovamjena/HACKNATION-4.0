package com.example.animalwonders.uicomponents.home


import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun moduleButton(
    modifier: Modifier = Modifier,
    text : String,
    onclicked: ()->Unit,
    color : ButtonColors,
    shadowColor: Color,
    borderColor: Color
){
        OutlinedButton(
            modifier = modifier
                .widthIn(160.dp)
                .height(42.dp)
                .padding(horizontal = 15.dp)
                .shadow(
                    elevation = 24.dp,
                    shape = RoundedCornerShape(percent = 50),
                    spotColor = shadowColor
                ),
            onClick = onclicked,
            colors = color,
            border = BorderStroke(2.dp,borderColor)) {
            Text(
                text = text,
                style = MaterialTheme.typography.bodyLarge,
                fontSize = 14.sp)
        }

    }
