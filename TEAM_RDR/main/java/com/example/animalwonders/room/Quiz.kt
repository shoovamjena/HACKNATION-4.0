package com.example.animalwonders.room

import android.content.Intent
import android.net.Uri
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.TileMode
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.room.*
import com.example.animalwonders.ui.theme.PrimaryPink
import com.example.animalwonders.ui.theme.PrimaryPinkBlended
import com.example.animalwonders.uicomponents.home.bottomBarNav
import kotlinx.coroutines.launch

@Entity
data class QuizQuestion(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val questionText: String,
    val optionA: String,
    val optionB: String,
    val optionC: String,
    val optionD: String,
    val correctAnswer: String
)

@Dao
interface QuizDao {
    @Query("SELECT * FROM QuizQuestion")
    suspend fun getAllQuestions(): List<QuizQuestion>

    @Insert
    suspend fun insertQuestions(questions: List<QuizQuestion>)
}

@Database(entities = [QuizQuestion::class], version = 1)
abstract class QuizDatabase : RoomDatabase() {
    abstract fun quizDao(): QuizDao
}

class QuizViewModel(private val quizDao: QuizDao) : ViewModel() {
    var questions by mutableStateOf(listOf<QuizQuestion>())
    var currentQuestionIndex by mutableStateOf(0)
    var score by mutableStateOf(0)
    var showResult by mutableStateOf(false)

    private val coroutineScope = viewModelScope

    init {
        loadQuestions()
    }

    private fun loadQuestions() {
        coroutineScope.launch {
            val fetchedQuestions = quizDao.getAllQuestions().shuffled()
            questions = fetchedQuestions.ifEmpty {
                // Insert default questions if none exist
                val defaultQuestions = listOf(
                    QuizQuestion(0, "What is the largest land animal?", "Elephant", "Lion", "Giraffe", "Hippo", "Elephant"),
                    QuizQuestion(0, "Which animal is known as the King of the Jungle?", "Tiger", "Lion", "Leopard", "Cheetah", "Lion"),
                    QuizQuestion(0, "What do lions primarily eat?", "Bamboo", "Grass", "Meat", "Leaves", "Meat"),
                    QuizQuestion(0, "Which animal is known for its loyalty to human?", "Dog", "Cat", "Lion", "Crow", "Peacock"),
                    QuizQuestion(0, "What is the domestic animal?", "Cheetah", "Lion", "Dog", "Leopard", "Cheetah")
                )
                quizDao.insertQuestions(defaultQuestions)
                defaultQuestions
            }
        }
    }

    fun submitAnswer(selectedAnswer: String) {
        if (questions[currentQuestionIndex].correctAnswer == selectedAnswer) {
            score++
        }
        if (currentQuestionIndex < questions.size - 1) {
            currentQuestionIndex++
        } else {
            showResult = true
        }
    }
}

class QuizViewModelFactory(private val quizDao: QuizDao) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(QuizViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return QuizViewModel(quizDao) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}

@Composable
fun QuizScreen(quizDao: QuizDao,navController: NavController) {
    val viewModel: QuizViewModel = viewModel(
        factory = QuizViewModelFactory(quizDao)
    )
    val questions = viewModel.questions
    val currentIndex = viewModel.currentQuestionIndex
    val showResult = viewModel.showResult

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

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        bottomBar = {
            bottomBarNav(navController,2)
        }

    ) { paddingValues ->
        Column(
            modifier = Modifier.fillMaxSize().background(animatedBrush)
        ){ }
    }
    if (showResult) {
        ResultScreen(score = viewModel.score, totalQuestions = questions.size)
    } else if (questions.isNotEmpty()) {
        val currentQuestion = questions[currentIndex]
        QuestionCard(
            question = currentQuestion,
            onAnswerSelected = viewModel::submitAnswer
        )
    }
}


@Composable
fun ResultScreen(score: Int, totalQuestions: Int) {
    val context = LocalContext.current
    val url = "https://www.chatbase.co/chatbot-iframe/LmaNDUQhWZPpLZ1ZCqrTI"
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "Your Score: $score / $totalQuestions", style = MaterialTheme.typography.headlineMedium)
        Spacer(modifier = Modifier.height(28.dp))
        Button(
            onClick = {
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                context.startActivity(intent)
            },
            modifier = Modifier.padding(16.dp)
        ) {
            Text(text = "Click for more")
        }
    }
}

@Composable
fun QuestionCard(question: QuizQuestion, onAnswerSelected: (String) -> Unit) {
    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = question.questionText, style = MaterialTheme.typography.headlineLarge)
        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = { onAnswerSelected(question.optionA) }) { Text(question.optionA) }
        Spacer(modifier = Modifier.height(8.dp))
        Button(onClick = { onAnswerSelected(question.optionB) }) { Text(question.optionB) }
        Spacer(modifier = Modifier.height(8.dp))
        Button(onClick = { onAnswerSelected(question.optionC) }) { Text(question.optionC) }
        Spacer(modifier = Modifier.height(8.dp))
        Button(onClick = { onAnswerSelected(question.optionD) }) { Text(question.optionD) }

    }
}
