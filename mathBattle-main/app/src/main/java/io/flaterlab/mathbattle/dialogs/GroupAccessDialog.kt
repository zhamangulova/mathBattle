package io.flaterlab.mathbattle.dialogs

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import android.widget.Toast
import io.flaterlab.mathbattle.databinding.DialogGameOverBinding
import io.flaterlab.mathbattle.databinding.DialogGroupPasswordBinding

class GroupAccessDialog(c: Context,
                        val password: String,
                        val next: View.OnClickListener): Dialog(c) {

    lateinit var binding: DialogGroupPasswordBinding
    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DialogGroupPasswordBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.button.setOnClickListener {
            val pass = binding.password.text.toString()
            if (pass == password){
                next.onClick(it)
                dismiss()
            }else{
                Toast.makeText(context, "не верный пароль", Toast.LENGTH_LONG).show()
            }
        }
        binding.cancel.setOnClickListener {
            dismiss()
        }
    }
}