package com.example.quizapp.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.quizapp.models.Questions
import com.example.quizapp.models.questions

class QuizViewModel : ViewModel() {
    var simdikiSoruIndeksi by mutableStateOf(0)
    var dogruSayisi by mutableStateOf(0)
    var YarismaBittiMi by mutableStateOf(false)
    val simdikiSoru: Questions
        get() =
            questions[simdikiSoruIndeksi]

    fun cevabiGonder(cevap: String) {
        if (cevap == simdikiSoru.correctAnswer) {
            dogruSayisi++
        }
        else{
            YarismaBittiMi=true
        }
        if (simdikiSoruIndeksi < questions.size - 1) {
            simdikiSoruIndeksi++
        } else {
            YarismaBittiMi = true
        }
    }
}