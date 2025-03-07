package com.antsfamily.danskflashcards.ui.game.model

import com.antsfamily.danskflashcards.core.util.COUNTDOWN_TIME_SEC
import com.antsfamily.danskflashcards.core.util.toTimeFormat

data class TimerModel(
    val remainTime: Long = COUNTDOWN_TIME_SEC,
    val progress: Float = 1f,
) {

    val remainTimeString: String
        get() = this.remainTime.toTimeFormat()

    val isLastResort: Boolean
        get() = remainTime <= 5
}