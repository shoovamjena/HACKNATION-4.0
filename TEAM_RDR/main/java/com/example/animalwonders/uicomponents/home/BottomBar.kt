package com.example.animalwonders.uicomponents.home

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.animalwonders.R
import com.example.animalwonders.ui.theme.PrimaryPinkDark

@Composable
fun bottomBarNav(navController: NavController,state: Int){
    var selectedItem by remember { mutableStateOf(state) }


    val items = listOf("Home", "Scan", "Quiz")
    val selectedIcons = listOf(
        ImageVector.vectorResource(id = R.drawable.home_filled),
        ImageVector.vectorResource(id = R.drawable.scan),
        ImageVector.vectorResource(id =R.drawable.quiz_filled)
    )
    val unselectedIcons = listOf(
        ImageVector.vectorResource(id = R.drawable.home_outline),
        ImageVector.vectorResource(id = R.drawable.scan_outline),
        ImageVector.vectorResource(id = R.drawable.quiz_outline),
    )

    NavigationBar(
        modifier = Modifier
            .fillMaxWidth()
            .height(100.dp)
            .clip(RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp)),
        containerColor = PrimaryPinkDark,
        contentColor = PrimaryPinkDark
    ) {
        items.forEachIndexed { index, label ->
            NavigationBarItem(
                icon = {
                    Icon(
                        imageVector = if (selectedItem == index) selectedIcons[index] else unselectedIcons[index],
                        contentDescription = label,
                        modifier = Modifier.size(28.dp)
                    )
                },
                selected = selectedItem == index,
                onClick = {
                    selectedItem = index
                    if (label == "Home") {
                        navController.navigate("home_screen")
                    }
                    if(label == "Quiz"){
                        navController.navigate("quiz_screen")
                    }
                },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = PrimaryPinkDark,
                    unselectedIconColor = Color.White.copy(alpha = 0.85f),
                    indicatorColor = Color.White.copy(alpha = 0.85f)
                ),
            )
        }
    }
}