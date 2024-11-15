package ru.practicum.android.diploma.data.dto

data class VacanciesRequest(
    val text: String,
    val currency: String,
    val size: Int,
    val page: Int,
    val locale: String = "RU",
    val host: String = "hh.ru"
)
