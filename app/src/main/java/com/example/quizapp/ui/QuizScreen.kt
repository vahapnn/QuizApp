package com.example.quizapp.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.BasicText
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.quizapp.models.Questions
import com.example.quizapp.models.questions
import com.example.quizapp.viewmodel.QuizViewModel
import kotlinx.coroutines.delay
import kotlin.random.Random

@Composable
fun QuizScreen(quizViewModel: QuizViewModel) {
    val simdikiSoru = quizViewModel.simdikiSoru
    val dogruSayisi = quizViewModel.dogruSayisi
    val yarismaBittiMi = quizViewModel.YarismaBittiMi
    var fiftyPercentUsed by remember {
        mutableStateOf(false)
    }
    var showAudienceResult by remember {
        mutableStateOf(false)
    }
    var telefonJokerUsed by remember {
        mutableStateOf(false)
    }
    var telefonJoker by remember {
        mutableStateOf("")
    }
    var audienceVotes by remember {
        mutableStateOf(listOf(0, 0, 0, 0))
    }
    if (yarismaBittiMi) {
        ResultScreen(dogruSayisi = dogruSayisi, toplamSoru = questions.size)
    } else {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = simdikiSoru.questionText,
                Modifier
                    .padding(5.dp)
                    .background(color = Color(6, 20, 100, 255), shape = CircleShape),
                style = TextStyle(
                    fontSize = 32.sp, color = Color.White
                ), textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(16.dp))

            Spacer(modifier = Modifier.height(16.dp))
            simdikiSoru.options.forEach { option ->
                Button(
                    onClick = {
                        quizViewModel.cevabiGonder(option)
                        //showAudienceResult = false
                        //telefonJokerUsed=false
                    }, colors = ButtonDefaults
                        .buttonColors(containerColor = Color(115, 55, 219, 255)),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(
                            vertical = 4.dp
                        )
                ) {
                    Text(text = option, style = TextStyle(fontSize = 20.sp))
                }
            }
            Spacer(modifier = Modifier.padding(6.dp))
            Text(
                text = "Jokerler", modifier = Modifier.padding(16.dp),
                style = TextStyle(fontSize = 20.sp)
            )
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Button(
                    onClick = {
                        if (!fiftyPercentUsed) {
                            fiftyPercentUsed = true
                            simdikiSoru.options = applyFiftyPercentLifeLine(simdikiSoru)
                        }
                    },
                    colors = ButtonDefaults
                        .buttonColors(containerColor = Color(115, 55, 219, 255)),
                    enabled = !fiftyPercentUsed,
                ) {
                    Text(text = "Yarı yarıya joker")
                }
                Button(
                    onClick = {
                        if (!showAudienceResult) {
                            showAudienceResult = true
                            //audienceVotes = generateAudienceVotes()
                        }
                        audienceVotes = generateAudienceVotes()
                        //showAudienceResult = true
                    }, colors = ButtonDefaults
                        .buttonColors(containerColor = Color(115, 55, 219, 255)),
                    enabled = !showAudienceResult
                ) {
                    Text(text = "Seyirciye sorunuz")
                }

                if (showAudienceResult) {
                    AudienceResult(audienceVotes, simdikiSoru.options)
                    LaunchedEffect(Unit) {
                        delay(5000)
                        showAudienceResult=false
                    }
                    //AudienceResult(audienceVotes, simdikiSoru.options)
                }
                Button(
                    onClick = {
                        if (!telefonJokerUsed) {
                            telefonJokerUsed = true
                            telefonJoker = getExpertAdvice(simdikiSoru.correctAnswer).toString()
                        }
                        // telefonJoker = getExpertAdvice(simdikiSoru.correctAnswer)

                    }, enabled = !telefonJokerUsed,
                    colors = ButtonDefaults
                        .buttonColors(containerColor = Color(115, 55, 219, 255))
                ) {
                    Text(text = "Telefon joker")
                    Spacer(modifier = Modifier.padding(5.dp))
                }
                if (telefonJokerUsed) {
                    Text(text = telefonJoker)
                    LaunchedEffect(Unit) {
                        delay(5000)
                        telefonJokerUsed=false
                    }
                }
            }

        }
    }
}

fun getExpertAdvice(correctAnswer: String): String {
    return "Bence sorunun doğru cevabı " + correctAnswer
}

fun applyFiftyPercentLifeLine(questions: Questions): List<String> {
    val incorrectOptions = questions.options.filter {
        it !=
                questions.correctAnswer
    }
    val removedOptions = incorrectOptions.shuffled().take(2)
    return questions.options.filter {
        it == questions.correctAnswer || it !in removedOptions
    }
}

@Composable
fun AudienceResult(votes: List<Int>, options: List<String>) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center,
        modifier = Modifier
            .height(300.dp)
            .width(400.dp)
    ) {
        votes.forEachIndexed { index, vote ->
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
                modifier = Modifier.padding(vertical = 8.dp)
            ) {
                BasicText(options[index])
                Spacer(modifier = Modifier.width(8.dp))
                Box(
                    modifier = Modifier
                        .height((vote * 3).dp)
                        .width(20.dp)
                        .background(Color.Blue)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text("$vote%")
            }
        }
    }
}

fun generateAudienceVotes(): List<Int> {
    val votes = List(4) { Random.nextInt(10, 40) }
    val sum = votes.sum()
    return votes.map { it * 100 / sum }
}

@Composable
fun ResultScreen(dogruSayisi: Int, toplamSoru: Int) {
    var odulMiktari by remember {
        mutableStateOf(1)
    }
    odulMiktari = dogruSayisi * 100000
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(text = "Yarisma bitti!", style = TextStyle(fontSize = 24.sp))
        Text(
            text = "Doğru sayisi: $dogruSayisi", style = TextStyle(
                fontSize = 20.sp,
            )
        )
        for (i in 1500000 downTo 50000 step 100000) {
            if (odulMiktari == i) {
                Text(
                    text = odulMiktari.toString() +" tl",
                    Modifier
                        .padding(6.dp)
                        .width(500.dp)
                        .height(35.dp)
                        .background(color = Color(244, 67, 54, 255), shape = CircleShape),
                    style = TextStyle(fontSize = 20.sp, color = Color.White),
                    textAlign = TextAlign.Center,
                )
            } else {
                Text(
                    text = i.toString() +" tl",
                    Modifier
                        .padding(6.dp)
                        .width(500.dp)
                        .height(35.dp)
                        .background(color = Color(69, 15, 163, 255), shape = CircleShape),
                    style = TextStyle(fontSize = 20.sp, color = Color.White),
                    textAlign = TextAlign.Center,
                )
            }
        }
    }
}
