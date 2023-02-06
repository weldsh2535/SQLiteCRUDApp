package com.weldsh2535.sqlitecruddemo.Model

import kotlin.random.Random

data class Student(
    var id: Int = getAutoId(),
    var name: String = "",
    var email: String = ""
) {
    companion object {
        fun getAutoId(): Int {
            val random = Random.nextInt(100)
            return random
        }
    }
}
