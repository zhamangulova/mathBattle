package io.flaterlab.mathbattle

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.google.firebase.database.FirebaseDatabase
import io.flaterlab.mathbattle.databinding.ActivityAddGroupBinding
import io.flaterlab.mathbattle.models.Data
import io.flaterlab.mathbattle.models.Group
import io.flaterlab.mathbattle.util.FirebaseNodes
import io.flaterlab.mathbattle.util.hideKeyboard
import java.util.*

class AddGroupActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAddGroupBinding
    val db = FirebaseDatabase.getInstance()
    lateinit var data: Data
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddGroupBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.backButton.setOnClickListener {
            onBackPressed()
        }
        data = Data(this)
        var u = ""
        data.getUser()?.let {
            u = it.id
        }
        binding.saveButton.setOnClickListener {
            hideKeyboard(this)
            binding.progressBar3.visibility = View.VISIBLE
            val g = Group(
                binding.login.text.toString(),
                "",
                binding.password.text.toString(),
                u
            )
            if(isValid(g.id)){
                if(isValid(g.password)){
                    db.getReference(FirebaseNodes.groupsNode).child(g.id).get().addOnSuccessListener {
                        if (it.exists()){
                            binding.progressBar3.visibility = View.GONE
                            Toast.makeText(this, "придумайте другой id", Toast.LENGTH_LONG).show()
                        }else{
                            db.getReference(FirebaseNodes.groupsNode)
                                .child(g.id)
                                .child("data")
                                .setValue(g).addOnSuccessListener {
                                    db.getReference(FirebaseNodes.groupsNode).child(g.id).child("time").setValue(
                                        1
                                    ).addOnSuccessListener {
                                        binding.progressBar3.visibility = View.GONE
                                        data.addGroup(g)
                                        onBackPressed()
                                    }
                            }.addOnFailureListener {
                                    binding.progressBar3.visibility = View.GONE
                                    Toast.makeText(this, "ошибка при загрузке", Toast.LENGTH_LONG).show()
                            }
                        }
                    }.addOnFailureListener {
                        binding.progressBar3.visibility = View.GONE
                        Toast.makeText(this, "ошибка при загрузке 1", Toast.LENGTH_LONG).show()
                    }
                }else{
                    binding.progressBar3.visibility = View.GONE
                    Toast.makeText(this, "пароль может сожержать только буквы латинского алфавита и цифры", Toast.LENGTH_LONG).show()
                }
            }else{
                binding.progressBar3.visibility = View.GONE
                Toast.makeText(this, "id может сожержать только буквы латинского алфавита и цифры", Toast.LENGTH_LONG).show()
            }
        }
    }
    fun isValid(g: String): Boolean{
        if(g.length < 2){
            return false
        }
        return g.filter { it in 'A'..'Z' || it in 'a'..'z' || it in '0'..'9'}.length == g.length
    }


}