package com.example.animalwonders.screen.loginscreen

import android.widget.Toast
import androidx.annotation.DrawableRes
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.scrollBy
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.ime
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.compose.animation.core.*
import androidx.compose.ui.graphics.TileMode
import com.example.animalwonders.R
import com.example.animalwonders.ui.theme.DarkTextColor
import com.example.animalwonders.ui.theme.PrimaryPink
import com.example.animalwonders.ui.theme.PrimaryPinkBlended
import com.example.animalwonders.ui.theme.PrimaryPinkDark
import com.example.animalwonders.ui.theme.PrimaryPinkLight
import com.example.animalwonders.uicomponents.welcome.welcomeButton
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.launch

@Composable
fun LoginScreen(
    modifier: Modifier = Modifier,
    navController : NavController
){
    val scrollState = rememberScrollState()
    val coroutineScope = rememberCoroutineScope()
    val keyboardHeight = WindowInsets.ime.getBottom(LocalDensity.current)

    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    val auth = FirebaseAuth.getInstance()
    val firestore = FirebaseFirestore.getInstance()
    val context = LocalContext.current

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

    LaunchedEffect(keyboardHeight) {
        coroutineScope.launch{
            scrollState.scrollBy(keyboardHeight.toFloat())
        }
    }

    Column (
        modifier = modifier
            .fillMaxSize()
            .background(animatedBrush)
            .systemBarsPadding()
            .verticalScroll(scrollState)
            .imePadding(),
        horizontalAlignment = Alignment.CenterHorizontally
    ){
        Image(
            painter = painterResource(R.drawable.login_animal),
            contentDescription = null,
            modifier = Modifier.size(250.dp)
        )
        Message( title = "Welcome Back", subtitle = "Please, Log In.")
        Spacer(
            modifier = modifier.height(10.dp)
        )
        inputField(
            leadingIconRes = R.drawable.email,
            placeholderText = "Your Email",
            inputValue = email,
            onInputChange = { email = it },
            modifier = modifier.padding(horizontal = 24.dp)
        )
        Spacer(
            modifier = modifier.height(10.dp)
        )
        inputField(
            leadingIconRes = R.drawable.key,
            placeholderText = "Your Password",
            inputValue = password,
            onInputChange = { password = it },
            visualTransformation = PasswordVisualTransformation(),
            modifier = modifier.padding(horizontal = 24.dp)
        )
        Spacer(
                modifier = modifier.height(24.dp)
                )
        welcomeButton(
            text = "LOGIN",
            isNavigationArrowVisible = true,
            onClicked = {
                if (email.isNotEmpty() && password.isNotEmpty()) {
                    auth.signInWithEmailAndPassword(email, password)
                        .addOnCompleteListener { task ->
                            if (task.isSuccessful) {
                                val userId = auth.currentUser?.uid
                                userId?.let {
                                    firestore.collection("users").document(it)
                                        .get()
                                        .addOnSuccessListener { document ->
                                            if (document.exists()) {
                                                Toast.makeText(context, "Login successful!", Toast.LENGTH_SHORT).show()
                                                navController.navigate("home_screen")
                                            } else {
                                                Toast.makeText(context, "User profile not found.", Toast.LENGTH_SHORT).show()
                                            }
                                        }
                                        .addOnFailureListener { e ->
                                            Toast.makeText(context, "Error fetching user profile: ${e.message}", Toast.LENGTH_SHORT).show()
                                        }
                                }
                            } else {
                                Toast.makeText(context, "Login failed: ${task.exception?.message}", Toast.LENGTH_SHORT).show()
                            }
                        }
                } else {
                    Toast.makeText(context, "Please fill in all fields.", Toast.LENGTH_SHORT).show()
                }
            },
            colors = ButtonDefaults.buttonColors(
                containerColor = PrimaryPinkDark,
                contentColor = Color.White
            ),
            shadowColor = PrimaryPinkDark,
            modifier = Modifier.padding(horizontal = 24.dp)
        )
        Separator(
            modifier = Modifier.fillMaxWidth()
                .padding(horizontal = 40.dp)
                .height(48.dp)
        )
        welcomeButton(
            text = "SIGN UP",
            isNavigationArrowVisible = false,
            onClicked = { navController.navigate("signup_screen") },
            colors = ButtonDefaults.buttonColors(
                containerColor = PrimaryPinkLight,
                contentColor = Color.White
            ),
            shadowColor = PrimaryPinkDark,
            modifier = Modifier.padding(horizontal = 24.dp)
        )
    }
}

@Composable
private fun Message(
    modifier: Modifier = Modifier,
    title: String,
    subtitle: String
){
    Column (
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ){
        Text(
            text = title,
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.bodyLarge,
            color = Color.White,
            fontWeight = FontWeight.Medium
        )
        Text(
            text = subtitle,
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.headlineMedium,
            color = Color.White,
            fontWeight = FontWeight.Black
        )

    }
}

@Composable
private fun inputField(
    modifier: Modifier = Modifier,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    @DrawableRes leadingIconRes: Int,
    placeholderText: String,
    inputValue: String,
    onInputChange: (String) -> Unit
){
    TextField(
        modifier = modifier
            .fillMaxWidth()
            .height(62.dp),
        value = inputValue,
        onValueChange = onInputChange,
        visualTransformation = visualTransformation,
        singleLine = true,
        shape = RoundedCornerShape(percent = 50),
        colors = TextFieldDefaults.colors(
            focusedIndicatorColor = Color.Transparent,
            disabledIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent,
            errorIndicatorColor = Color.Transparent,
            focusedTextColor = DarkTextColor,
            unfocusedTextColor = DarkTextColor,
            unfocusedPlaceholderColor = DarkTextColor,
            focusedPlaceholderColor = DarkTextColor.copy(alpha = 0.5f),
            focusedLeadingIconColor = DarkTextColor,
            unfocusedLeadingIconColor = DarkTextColor,
            focusedContainerColor = Color.White,
            unfocusedContainerColor = Color.White.copy(alpha = 0.7f),
        ),
        textStyle = MaterialTheme.typography.bodyLarge.copy(
            fontWeight = FontWeight.Medium
        ),
        leadingIcon = {
            Icon(
                painter = painterResource(leadingIconRes),
                contentDescription = null,
                modifier = Modifier.size(24.dp))
        },
        placeholder = {
            Text(text = placeholderText)
        }
    )
}

@Composable
private fun Separator(
    modifier: Modifier = Modifier
){
    Row (
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ){
        DashedLine(
            modifier = Modifier.weight(weight = 1f),
            color = Color.White
        )
        Text(
            text = "or",
            style = MaterialTheme.typography.labelMedium,
            color = Color.White
        )
        DashedLine(
            modifier = Modifier.weight(weight = 1f),
            color = Color.White
        )
    }
}

@Composable
fun DashedLine(
    modifier: Modifier = Modifier,
    color: Color
){
    Canvas(modifier = modifier) {
        drawLine(
            color = color,
            start = Offset(0f,0f),
            end = Offset(size.width,0f),
            pathEffect = PathEffect.dashPathEffect(floatArrayOf(8f,8f),0f),
            cap = StrokeCap.Round,
            strokeWidth = 2.dp.toPx()
        )
    }
}