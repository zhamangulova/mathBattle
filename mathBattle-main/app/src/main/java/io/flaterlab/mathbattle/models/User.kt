package io.flaterlab.mathbattle.models

data class User(
    var id: String,
    var username: String,
    var login: String,
    var password: String,
    var score: Int = 0,
    var email: String = "",
    var games: Int = 0,
    var record: Int = 0,
    var wins: Int = 0,
    var level: Int = 1,
    var levelPoints: Int = 0,
    var levelMax: Int = 300
)
