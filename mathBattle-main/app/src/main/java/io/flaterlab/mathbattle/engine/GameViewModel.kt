package io.flaterlab.mathbattle.engine

import androidx.lifecycle.ViewModel

class GameViewModel: ViewModel() {
    var level = 0
    var problem: Problem? = null

    var time = 60
    var reward = 5.0

    var score = 0.0

    var gen = Generator()

    var scores: HashMap<String, Long> = HashMap()

    fun init() {
        level = 0
        level ++
        problem = gen.getProblem(level)
    }

    fun restart(){
        score = 0.0
        reward = 5.0
        time = 60
    }

    fun tick(){
        time -= 1
    }

    fun tack(){
        time -= 5
    }

    fun next(){
        level++
        problem = gen.getProblem(level)
    }
}