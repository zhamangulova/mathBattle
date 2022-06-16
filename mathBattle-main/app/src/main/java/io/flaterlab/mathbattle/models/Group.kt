package io.flaterlab.mathbattle.models

data class Group(
    var id: String,
    var name: String,
    var password: String,
    var adminId: String = "",
) {
    constructor() : this("", "", "") {

    }
}