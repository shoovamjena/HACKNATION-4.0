package com.example.animalwonders.screen.animaldetailscreen

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import com.example.animalwonders.R
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.TileMode
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.animalwonders.screen.loginscreen.DashedLine
import com.example.animalwonders.ui.theme.PrimaryPink
import com.example.animalwonders.ui.theme.PrimaryPinkBlended
import com.example.animalwonders.ui.theme.PrimaryPinkDark
import com.example.animalwonders.ui.theme.PrimaryVioletDark
import com.example.animalwonders.uicomponents.home.bottomBarNav
import com.example.animalwonders.viewmodel.AnimalViewModel

@Composable
fun AnimalDetailScreen(animalId: Int, animalViewModel: AnimalViewModel, navController: NavController) {
    // Observe the selected animal by ID
    animalViewModel.fetchAnimalById(animalId)
    val animal by animalViewModel.selectedAnimal.observeAsState(initial = null)
    animal?.let {
        val imageScale = remember { Animatable(1f) }

        val gradientOffset by animateFloatAsState(
            targetValue = 1f,
            animationSpec = tween(durationMillis = 4000, easing = LinearEasing), label = "",
        )

        val gradientColors = listOf(PrimaryPinkBlended, PrimaryPink)
        val animatedBrush = Brush.verticalGradient(
            colors = gradientColors,
            startY = 0f,
            endY = with(LocalDensity.current) { 1000.dp.toPx() * gradientOffset },
            tileMode = TileMode.Clamp
        )

        // Animate image scaling effect
        LaunchedEffect(Unit) {
            imageScale.animateTo(
                targetValue = 1.2f,
                animationSpec = tween(
                    durationMillis = 600,
                    easing = FastOutSlowInEasing
                )
            )
        }
        Scaffold(
            modifier = Modifier.fillMaxSize(),
            bottomBar = {
                bottomBarNav(navController,0)
            }

        ) { paddingValues ->

            Column(modifier = Modifier
                .fillMaxSize()
                .background(animatedBrush)
            ) {
                Image(
                    painter = painterResource(id = it.imageResId),
                    contentDescription = null,
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(bottomStart = 50.dp, bottomEnd = 50.dp))
                        .graphicsLayer(
                            scaleX = imageScale.value,
                            scaleY = imageScale.value
                        )
                )
                Spacer(modifier = Modifier.height(6.dp))
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .fillMaxHeight()
                        .weight(0.5f)
                        .padding(top = 14.dp, bottom = 90.dp)
                        .padding(horizontal = 8.dp)
                        .clip(RoundedCornerShape(24.dp))
                        .background(Color.White.copy(alpha = 0.5f))
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 10.dp, start = 10.dp, end = 10.dp)
                            .shadow(
                                elevation = 20.dp,
                                shape = RoundedCornerShape(14.dp),
                                ambientColor = Color.Gray,
                                spotColor = Color.Black
                            )
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(150.dp)
                                .background(Color.White)
                                .alpha(0.7f)
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(top = 24.dp)
                                    .padding(horizontal = 10.dp),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                // Column for name and food (aligned vertically)
                                Column(
                                    verticalArrangement = Arrangement.SpaceEvenly
                                ) {
                                    InfoWithIcon(R.drawable.animal, it.name)
                                    Spacer(modifier = Modifier.height(28.dp))
                                    InfoWithIcon(R.drawable.food, it.food)
                                }

                                // Column for habitat and color (aligned vertically)
                                Column(
                                    verticalArrangement = Arrangement.SpaceEvenly
                                ) {
                                    InfoWithIcon(R.drawable.habitat, it.category)
                                    Spacer(modifier = Modifier.height(28.dp))
                                    InfoWithIcon(R.drawable.color, it.color)
                                }
                            }
                        }
                    }
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 190.dp)
                            .padding(horizontal = 14.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "DESCRIPTION",
                            modifier = Modifier
                                .fillMaxWidth(),
                            style = MaterialTheme.typography.headlineLarge,
                            textAlign = TextAlign.Center,
                            color = PrimaryVioletDark
                        )

                        Text(
                            text = it.description,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 6.dp),
                            style = MaterialTheme.typography.bodyLarge,
                            textAlign = TextAlign.Center,
                            fontSize = 24.sp,
                            color = PrimaryPinkDark
                        )
                        // Button at the bottom
                        Button(
                            onClick = { navController.navigate("ar_screen/${animal!!.name}")},
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 80.dp)
                                .align(Alignment.Start)
                        ) {
                            Text(text = "Interact")
                        }
                    }
                    }
                }

            }
        }
    }

@Composable
fun InfoWithIcon(icon: Int, text: String) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.padding(end = 8.dp)
    ) {
        Icon(
            painter = painterResource(icon),
            contentDescription = null,
            tint = Color.Black,
            modifier = Modifier.size(24.dp)
        )
        Spacer(modifier = Modifier.width(14.dp))
        Text(text,
            style = MaterialTheme.typography.headlineSmall,
            color = PrimaryPinkDark,
            fontSize = 20.sp
            )
    }
}
