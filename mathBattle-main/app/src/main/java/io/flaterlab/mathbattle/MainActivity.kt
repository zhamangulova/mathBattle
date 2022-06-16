package io.flaterlab.mathbattle

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import io.flaterlab.mathbattle.databinding.ActivityMainBinding
import io.flaterlab.mathbattle.engine.Functions
import io.flaterlab.mathbattle.models.Data
import io.flaterlab.mathbattle.models.User

class MainActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth

    private lateinit var binding: ActivityMainBinding

    val db = FirebaseFirestore.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        val data = Data(this)
        auth = FirebaseAuth.getInstance()

        auth.signInAnonymously()
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    val user = auth.currentUser
                }
            }
        val user = data.getUser()
        user?.let {
            binding.editText.setText(it.username)
            if(it.id != ""){
                startActivity(Intent(this, BaseActivity::class.java))
                Thread{
                    Thread.sleep(1000)
                    finish()
                }.start()
            }
        }

        binding.button.visibility = View.VISIBLE

        binding.button.setOnClickListener {
            var name = binding.editText.text.toString()
            if(name == ""){
                name = "Аноним"
            }
            binding.button.visibility = View.GONE
            val login = Functions.getString(7)
            val password = Functions.getString((4..7).random())
            val u = User("", name,  login, password)
            db.collection("users").add(u).addOnCanceledListener {
                data.saveUser(u)
            }.addOnSuccessListener {
                u.id = it.id
                data.saveUser(u)
                startActivity(Intent(this, BaseActivity::class.java))
                Thread{
                    Thread.sleep(1000)
                    finish()
                }.start()
            }
        }
    }
}