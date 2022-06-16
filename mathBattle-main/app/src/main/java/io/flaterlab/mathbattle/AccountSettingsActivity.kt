package io.flaterlab.mathbattle

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.google.firebase.firestore.FirebaseFirestore
import io.flaterlab.mathbattle.databinding.ActivityAccountSettingsBinding
import io.flaterlab.mathbattle.models.Data
import io.flaterlab.mathbattle.util.FirebaseNodes

class AccountSettingsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAccountSettingsBinding

    val db = FirebaseFirestore.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAccountSettingsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.backButton.setOnClickListener{
            onBackPressed()
        }
        val d = Data(this)
        val u = d.getUser()

        u?.let { user ->
            binding.email.setText(user.email)
            binding.login.setText(user.login)
            binding.nickname.setText(user.username)
            binding.password.setText(user.password)
            binding.saveButton.setOnClickListener {
                user.email = binding.email.text.toString()
                user.login = binding.login.text.toString()
                user.username = binding.nickname.text.toString()
                user.password = binding.password.text.toString()
                d.saveUser(user)
                binding.progressBar2.visibility = View.VISIBLE
                db.collection(FirebaseNodes.usersNode).whereEqualTo("login", user.login).get().addOnSuccessListener {
                    if(it.documents.size == 0){
                        db.collection(FirebaseNodes.usersNode).document(user.id).set(user).addOnSuccessListener {
                            onBackPressed()
                        }.addOnFailureListener{
                            binding.progressBar2.visibility = View.GONE
                            Toast.makeText(this, "Ошибка при сохранении", Toast.LENGTH_LONG).show()
                        }
                    }else{
                        binding.progressBar2.visibility = View.GONE
                        Toast.makeText(this, "Выбранный вами id занят", Toast.LENGTH_LONG).show()
                    }
                }.addOnFailureListener {
                    binding.progressBar2.visibility = View.GONE
                    Toast.makeText(this, "Ошибка при сохранении", Toast.LENGTH_LONG).show()
                }
            }
        }
    }
}

