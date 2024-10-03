package com.example.quizapp.models

data class Questions(
    val questionText: String,
    var options: List<String>,
    val correctAnswer: String
)

val questions = listOf(
    Questions(
        "Aşağıdakilerden hangisi Fransa'nın başkenntidir?",
        listOf("Berlin", "Madrid", "Roma", "Paris"), "Paris"
    ),
    Questions(
        "Aşağıdakilerden hangisi kızıl gezegen olarak bilinir?",
        listOf("Dünya", "Neptün", "Mars", "Plüton"), "Mars"
    ),
    Questions(
        "Aşağıdaki yazarlardan hangisi hamleti yazmıştır?",
        listOf("Charles Dickens", "Mark Twain", "Jane Austin ", "William Shekspeare"),
        "William Shekspeare"
    )
)
