package io.flaterlab.mathbattle.ui.account

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import io.flaterlab.mathbattle.AccountSettingsActivity
import io.flaterlab.mathbattle.R
import io.flaterlab.mathbattle.databinding.FragmentAccountBinding
import io.flaterlab.mathbattle.engine.GameActivity
import io.flaterlab.mathbattle.models.Data

class AccountFragment : Fragment() {

    private lateinit var accountViewModel: AccountViewModel

    private lateinit var binding: FragmentAccountBinding

    @SuppressLint("SetTextI18n")
    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View {
        accountViewModel =
                ViewModelProvider(this).get(AccountViewModel::class.java)
        binding = FragmentAccountBinding.inflate(layoutInflater)
        val root = binding.root

        binding.accSettings.setOnClickListener{
            startActivity(Intent(context, AccountSettingsActivity::class.java))
        }

        val user = Data(requireContext()).getUser()

        binding.playAnonimously.setOnClickListener {
            startActivity(Intent(requireContext(), GameActivity::class.java))
        }

        user?.let {
            binding.login.text = "@" + user.login
            binding.username.text = user.username
            binding.games.text = user.games.toString()
            binding.record.text = user.record.toString()
            binding.wins.text = user.wins.toString()
            binding.scores.text = user.score.toString()
            binding.level.text = user.level.toString()
            binding.progressBar.max = user.levelMax
            binding.progressBar.progress = user.levelPoints
            binding.max.text = user.levelMax.toString()
            binding.progress.text = user.levelPoints.toString()
        }

        return root
    }


}