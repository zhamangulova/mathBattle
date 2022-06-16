package io.flaterlab.mathbattle.dialogs

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import io.flaterlab.mathbattle.databinding.DialogGameOverBinding

class GameOverDialog(context: Context,
                     val score: String,
                     val restartListener: View.OnClickListener,
                     val end: View.OnClickListener): Dialog(context) {

    lateinit var binding: DialogGameOverBinding
    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DialogGameOverBinding.inflate(layoutInflater)
        setContentView(binding.root)
        window?.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT)

        if(score.toInt() > 0){
            binding.score.text = "+$score"
        }else{
            binding.score.text = score
        }
        binding.exit.setOnClickListener {
            dismiss()
            end.onClick(it)
        }
        binding.restart.setOnClickListener{
            dismiss()
            restartListener.onClick(it)
        }
    }
}