package io.flaterlab.mathbattle.engine

class Functions {
    companion object{
        fun getString(len: Int): String{
            val alp = "qwertyuiopasdfghjklzxcvbnmQWERTYUIOPASDFGHJKLZXCVBNM1234567890"
            var res = ""
            (0..len).forEach {
                res += alp.random()
            }
            return res
        }
    }
}

