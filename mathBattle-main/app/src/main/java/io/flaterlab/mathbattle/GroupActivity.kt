package io.flaterlab.mathbattle

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import io.flaterlab.mathbattle.databinding.ActivityGroupBinding
import io.flaterlab.mathbattle.engine.GameActivity
import io.flaterlab.mathbattle.models.Data
import io.flaterlab.mathbattle.models.Group
import io.flaterlab.mathbattle.util.FirebaseNodes
import java.util.*

class GroupActivity : AppCompatActivity() {
    lateinit var binding: ActivityGroupBinding

    private var db = FirebaseDatabase.getInstance()

    private lateinit var group: Group

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityGroupBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initialize()
    }

    private fun initialize(){
        val id = intent.getStringExtra("id")
        val data = Data(this)
        id?.let { id_ref ->
            val g = data.findGroup(id_ref)
            g?.let {
                group = g
                binding.groupId.text = group.id
            }
            db.getReference(FirebaseNodes.groupsNode)
                .child(id_ref)
                .addChildEventListener(object: ChildEventListener {
                    override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {}
                    override fun onChildChanged(
                        it: DataSnapshot,
                        previousChildName: String?
                    ) {
                        val date = Date()
                        if (it.key == "time"){
                            val time = it.getValue(Long::class.java)
                            time?.let {
                                if(date.time - time < 60 * 60 * 1000){
                                    setStatusStart(time, date.time)
                                }else{
                                    setStatusEnd()
                                }
                            }
                        }
                    }
                    override fun onChildRemoved(snapshot: DataSnapshot) {}
                    override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {}
                    override fun onCancelled(error: DatabaseError) {}
                })

            db.getReference(FirebaseNodes.groupsNode)
                .child(id_ref)
                .child("time")
                .get().addOnSuccessListener {
                    val date = Date()
                    if (it.exists()){
                        val time = it.getValue(Long::class.java)
                        time?.let {
                            if(date.time - time < 60 * 60 * 1000){
                                setStatusStart(time, date.time)
                            }else{
                                setStatusEnd()
                            }
                        }
                    }
                }


            db.getReference(FirebaseNodes.groupsNode)
                .child(id_ref)
                .child("rating")
                .get().addOnSuccessListener {
                    if (it.exists()){
                        var ratting = ""
                        it.children.forEach { data ->
                            ratting += data.key + ": "+ data.getValue(Long::class.java).toString() + "\n"
                        }
                        binding.results.text = ratting
                    }
                }
        }
    }

    private fun setStatusEnd(){
        binding.buttonStart.text = "Начать раунд"
        binding.buttonStart.setOnClickListener {
            start()
        }
    }

    private fun start(){
        val date = Date()
        db.getReference(FirebaseNodes.groupsNode)
            .child(group.id)
            .child("time")
            .setValue(date.time)
    }

    private fun setStatusStart(time: Long, timeC: Long){
        binding.buttonStart.text = "Присоединиться"
        binding.begining.text = "До конда раунда " + convertTime(((time + (60 * 60 * 1000)) - timeC)/1000)
        binding.buttonStart.setOnClickListener {
            val i = Intent(this, GameActivity::class.java)
            i.putExtra("group", group.id)
            startActivity(i)
        }
    }

    override fun onResume() {
        super.onResume()
        initialize()
    }

    private fun convertTime(n: Long): String{
        val sec = n%60
        if (sec < 10){
            return "${n/60}:0$sec"
        }
        return "${n/60}:$sec"
    }
}