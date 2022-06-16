package io.flaterlab.mathbattle.engine

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.FirebaseFirestore
import com.squareup.okhttp.Dispatcher
import io.flaterlab.mathbattle.databinding.ActivityGameBinding
import io.flaterlab.mathbattle.dialogs.GameOverDialog
import io.flaterlab.mathbattle.models.Data
import io.flaterlab.mathbattle.models.Level
import io.flaterlab.mathbattle.models.User
import io.flaterlab.mathbattle.util.FirebaseNodes
import java.math.BigDecimal
import java.math.RoundingMode

class GameActivity : AppCompatActivity() {

    val db = FirebaseFirestore.getInstance()
    val readTimeDb = FirebaseDatabase.getInstance()

    private lateinit var binding: ActivityGameBinding
    private lateinit var viewModel: GameViewModel

    private var timer: Boolean = true

    private lateinit var data: Data

    private var isContest = false
    private var groupID = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityGameBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        // check if group game
        if (intent.hasExtra("group")){
            val groupId= intent.getStringExtra("group")
            if (groupId != null) {
                initContest(groupId)
                groupID = groupId
            }
        }
        // endCheck

        data = Data(this)
        binding.editText3.setOnClickListener {
            onBackPressed()
        }

        viewModel = ViewModelProvider(this, GameViewModelFactory()).get(GameViewModel::class.java)
        viewModel.init()

        startTimer()

        viewModel.problem?.let { show(it) }
        showScore()

        binding.button1.setOnClickListener {
            click(0)
        }
        binding.button2.setOnClickListener {
            click(1)
        }
        binding.button3.setOnClickListener {
            click(2)
        }
        binding.button4.setOnClickListener {
            click(3)
        }

    }

    override fun onBackPressed() {
        AlertDialog.Builder(this)
                .setTitle("Выход из игры")
                .setMessage("Вы действительно хотите завершить игру?")
                .setPositiveButton("да") { dialog, _ ->
                    dialog.dismiss()
                    super.onBackPressed()
                }
                .setNegativeButton("нет") { dialog, _ ->
                    dialog.dismiss()
                }
                .show()
    }

    fun setTime(){
        viewModel.tick()
        binding.time.text = convertTime(viewModel.time)
        if(viewModel.time <= 0){
            gameStop()
        }
    }

    fun updateReward(){
        if(viewModel.reward > -4){
            viewModel.reward -= 0.04
        }
        binding.reward.text = "${BigDecimal(viewModel.reward)
            .setScale(2, RoundingMode.HALF_EVEN)}"
    }

    fun show(problem: Problem){
        Log.d("fdasf", viewModel.problem.toString())

        binding.problemTitle.text = problem.problemHeader

        val f = problem.problemAnswers[problem.problemSolutionIndex]

        problem.problemAnswers.forEachIndexed { i, it->
            if(it == f && i != problem.problemSolutionIndex){
                problem.problemAnswers[i] = (it.toInt() + 1).toString()
            }
        }

        binding.button1.text = trimTail(problem.problemAnswers[0])
        binding.button2.text = trimTail(problem.problemAnswers[1])
        binding.button3.text = trimTail(problem.problemAnswers[2])
        binding.button4.text = trimTail(problem.problemAnswers[3])

    }

    private fun trimTail(string: String): String{
        if(viewModel.problem?.answerType == AnswerType.DOUBLE){
            if(string.length < 7){
                return string
            }
            return string.subSequence(0, 6).toString()
        }
        return string
    }

    private fun convertTime(n: Int): String{
        val sec = n%60
        if (sec < 10){
            return "${n/60}:0$sec"
        }
        return "${n/60}:$sec"
    }

    private fun click(n: Int){
        viewModel.problem?.let { p ->
            if(n == p.problemSolutionIndex){
                viewModel.next()
                viewModel.problem?.let { show(it) }
                viewModel.score += viewModel.reward
                viewModel.reward = 5.0
                viewModel.time += 5
                showScore()
            } else {
                viewModel.reward = 5.0
                viewModel.tack()
                viewModel.next()
                viewModel.score -= 2
                viewModel.problem?.let { show(it) }
                showScore()
            }
        }
        if(viewModel.time <= 0){
            gameStop()
        }
    }

    private fun gameStop(){
        GameOverDialog(this,
                viewModel.score.toInt().toString(),
                {
                    restart()
                },
                {
                    super.onBackPressed()
                }
                ).show()
        saveProgress()
        viewModel.init()
        viewModel.restart()
        timer = false
    }

    private fun saveProgress() {
        val user = data.getUser()
        user?.let {
            it.score += viewModel.score.toInt()
            it.games += 1
            if(viewModel.score > it.record){
                it.record = viewModel.score.toInt()
            }
            val l = calculateLevel(it.score)
            it.level = l.num
            it.levelMax = l.max
            it.levelPoints  = l.progress
            data.saveUser(it)
            db.collection(FirebaseNodes.usersNode).document(it.id).set(it)

            if (isContest){
                saveContestResult(it)

            }
        }
    }

    private fun restart(){
        timer = true
        startTimer()
    }

    fun startTimer(){
        Thread{
            var c = 0
            while (timer){
                Thread.sleep(100)
                c++
                if(c == 10){
                    runOnUiThread {
                        setTime()
                    }
                    c = 0
                }
                runOnUiThread {
                    updateReward()
                }
            }
        }.start()
    }

    private fun showScore(){
        val sc = (viewModel.score * 100).toInt()/100.0
        binding.score.text = sc.toString()
    }

    private fun calculateLevel(score: Int): Level {
        val l = Level(1,0,0)
        l.max = 200
        l.progress = score
        while (true){
            if(l.progress < l.max){
                break
            }
            l.num++
            l.progress -= l.max
            l.max = (l.max * 1.2).toInt()
        }
        return l
    }

    private fun initContest(groupId: String){
        // add phone to rating
        isContest = true
        binding.scoreboard.visibility = View.VISIBLE
        readTimeDb.getReference(FirebaseNodes.groupsNode).child(groupId).child("rating").addChildEventListener(object: ChildEventListener{
            override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                updateScoreboard(snapshot.key as String, snapshot.value as Long)
            }
            override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {
                updateScoreboard(snapshot.key as String, snapshot.value as Long)
            }
            override fun onChildRemoved(snapshot: DataSnapshot) {}
            override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {}
            override fun onCancelled(error: DatabaseError) {}
        })
        readTimeDb.getReference(FirebaseNodes.groupsNode)
            .child(groupID)
            .child("rating").get().addOnSuccessListener {
                it.children.forEach { data ->
                    updateScoreboard(data.key as String, data.value as Long)
                }
            }
    }

    private fun saveContestResult(user: User){
        readTimeDb.getReference(FirebaseNodes.groupsNode)
            .child(groupID)
            .child("rating")
            .child(user.username)
            .setValue(viewModel.score.toInt())
    }

    private fun updateScoreboard(key: String, value: Long){
        var text = "Рейтинг: \n"

        viewModel.scores[key] = value
        val result = viewModel.scores.toList().sortedBy { (_, value) ->
              1000000 -value
        }.toMap()
        var index = 1
        for ((k: String, v: Long) in result) {
            text += "$index) $k: $v\n"
            index++
            if(index > 10){
                continue
            }
        }
        Log.d("my_tag", "update score board")
        binding.scoreboard.text = text
    }

    override fun onPause() {
        super.onPause()
        timer = false
    }

    override fun onDestroy() {
        super.onDestroy()
        timer = false;
    }
}