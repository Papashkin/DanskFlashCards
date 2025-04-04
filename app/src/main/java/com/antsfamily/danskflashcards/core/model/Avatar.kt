package com.antsfamily.danskflashcards.core.model

import androidx.annotation.DrawableRes
import com.antsfamily.danskflashcards.R

enum class Avatar {
    PEN,
    DOLLY,
    SPIRIT,
    HEISENBERG,
    CACTUS,
    WOMAN,
    SLOTH,
    MAN,
    ALIEN,
    BEAR,
    AVOCADO,
    COFFEE,
    ;
}

@DrawableRes
fun Avatar.toIconRes(): Int {
    return when(this) {
        Avatar.PEN -> R.drawable.ic_avatar_01
        Avatar.DOLLY -> R.drawable.ic_avatar_02
        Avatar.SPIRIT -> R.drawable.ic_avatar_03
        Avatar.HEISENBERG -> R.drawable.ic_avatar_04
        Avatar.CACTUS -> R.drawable.ic_avatar_05
        Avatar.WOMAN -> R.drawable.ic_avatar_06
        Avatar.SLOTH -> R.drawable.ic_avatar_07
        Avatar.MAN -> R.drawable.ic_avatar_08
        Avatar.ALIEN -> R.drawable.ic_avatar_09
        Avatar.BEAR -> R.drawable.ic_avatar_10
        Avatar.AVOCADO -> R.drawable.ic_avatar_11
        Avatar.COFFEE -> R.drawable.ic_avatar_12
    }
}