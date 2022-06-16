package io.flaterlab.mathbattle

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import io.flaterlab.mathbattle.databinding.ActivityCenterBinding
import io.flaterlab.mathbattle.engine.GameActivity

class CenterActivity : AppCompatActivity() {
    private lateinit var binding: ActivityCenterBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCenterBinding.inflate(layoutInflater)

        val view = binding.root
        setContentView(view)

        binding.playAnonimously.setOnClickListener {
            startActivity(Intent(this, GameActivity::class.java))
        }
    }
}