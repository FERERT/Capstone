package com.course.translateapp.languages

fun getStrings(): List<Map<String, String>>{

    val eng = mapOf(
        "title" to "Tests",
        "subtitle" to "Trying"
    )

    val esp = mapOf(
        "title" to "Pruebas",
        "subtitle" to "Intentando"
    )

    return listOf(
        eng,
        esp
    )
}