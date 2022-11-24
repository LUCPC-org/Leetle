package org.lucpc.utils

data class Problem(val name : String, var description: String, val link: String, val difficulty: Difficulty)

enum class Difficulty(val tagName : String) {
    EASY("easy"),
    MEDIUM("medium"),
    HARD("hard")
}