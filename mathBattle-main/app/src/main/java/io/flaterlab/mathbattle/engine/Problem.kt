package io.flaterlab.mathbattle.engine

data class Problem(
    var problemHeader: String,
    var problemSolution: Double,
    var problemAnswers: ArrayList<String>,
    var answerType: AnswerType,
    var problemSolutionIndex: Int
)

enum class AnswerType {
    DOUBLE,
    INT
}
