package com.course.translateapp.translator

data class TranslateState(

    val textToTranslate: String = "",
    val translateText: String = "",
    val isDownloading: Boolean = false

)
