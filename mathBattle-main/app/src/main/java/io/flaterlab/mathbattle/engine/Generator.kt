package io.flaterlab.mathbattle.engine

import java.math.BigDecimal
import java.math.RoundingMode
import kotlin.math.round

class Generator {

    fun getProblem(level: Int): Problem {
        var  problem = Problem("", 0.toDouble(), arrayListOf(), AnswerType.INT, 0)

        if (level < 10){
            problem = modifyProblem(problem, 0)
        }

        if (level in 10..29){
            problem = modifyProblem(problem, 1)
        }

        if (level in 30..99){
            problem = modifyProblem(problem, 2)
        }

        if (level > 100){
            problem = modifyProblem(problem, 3)
        }

        return problem
    }

    private fun genNum(count: Int): Int{
        var res = 0
        for (i in 0..count){
            res += (0..9).random() * (Math.round(Math.pow(10.toDouble(), i.toDouble())).toInt())
        }
        return res
    }

    private fun modifyProblem(p: Problem, numsCount: Int): Problem{
        val sign = arrayListOf("+", "-", "/", "x").random()
        val num1 = genNum(numsCount)
        var num2 = genNum(numsCount)


        when(sign){
            "+" -> {
                p.answerType = AnswerType.INT
                p.problemSolution = num1 + num2.toDouble()
            }
            "-" -> {
                p.answerType = AnswerType.INT
                p.problemSolution = num1 - num2.toDouble()
            }
            "/" -> {
                if(num2 == 0){
                    num2 = 1
                }
                p.answerType = AnswerType.DOUBLE
                p.problemSolution = num1.toDouble() / num2
            }
            "x" -> {
                p.answerType = AnswerType.INT
                p.problemSolution = num1 * num2.toDouble()
            }
        }
        p.problemHeader = "$num1 $sign $num2"

        return generateAnswers(p)
    }

    private fun generateAnswers(p: Problem): Problem{
        val arr = arrayListOf("", "", "", "")
        if(p.answerType == AnswerType.INT){
            arr.forEachIndexed { index, _ ->
                when ((0..1).random()){
                    0 -> {
                        arr[index] = (p.problemSolution + (0..9).random()).toInt().toString()
                    }
                    1->{
                        arr[index] = (p.problemSolution - (0..9).random()).toInt().toString()
                    }
                }
            }
            if(arr.indexOf(p.problemSolution.toInt().toString()) != -1){
                arr[arr.indexOf(p.problemSolution.toInt().toString())] = (p.problemSolution + 1.0).toString()
            }
            p.problemSolutionIndex = (0..3).random()
            arr[p.problemSolutionIndex] = p.problemSolution.toInt().toString()
            p.problemAnswers = arr
            return p
        }else{
            arr.forEachIndexed { index, _ ->
                val g =(2..9).random() / (1..9).random().toDouble()
                when ((0..1).random()){
                    0 -> {
                        val decimal = (p.problemSolution + g * 100).toInt()/100.0
                        arr[index] = decimal.toString()
                    }
                    1->{
                        val decimal = (p.problemSolution - g * 100).toInt()/100.0
                        arr[index] = decimal.toString()
                    }
                }
            }
            p.problemSolutionIndex = (0..3).random()
            if(arr.indexOf(p.problemSolution.toString()) != -1){
                arr[arr.indexOf(p.problemSolution.toString())] = (p.problemSolution + 1.1).toString()
            }
            arr[p.problemSolutionIndex] = p.problemSolution.toString()
            p.problemAnswers = arr
            return p
        }
    }

}