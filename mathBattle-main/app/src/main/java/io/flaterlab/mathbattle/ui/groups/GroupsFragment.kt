package io.flaterlab.mathbattle.ui.groups

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.FirebaseDatabase
import io.flaterlab.mathbattle.AddGroupActivity
import io.flaterlab.mathbattle.adapters.GroupAdapter
import io.flaterlab.mathbattle.databinding.FragmentHomeBinding
import io.flaterlab.mathbattle.models.Data
import io.flaterlab.mathbattle.models.Group
import io.flaterlab.mathbattle.util.FirebaseNodes

class GroupsFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView

    private lateinit var viewAdapter: GroupAdapter

    private lateinit var groupsViewModel: GroupsViewModel

    private lateinit var binding: FragmentHomeBinding
    private val db = FirebaseDatabase.getInstance()

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View {

        groupsViewModel = ViewModelProvider(this).get(GroupsViewModel::class.java)
        binding = FragmentHomeBinding.inflate(layoutInflater)

        binding.addGroup.setOnClickListener {
            startActivity(Intent(requireContext(), AddGroupActivity::class.java))
        }

        val data = Data(requireContext())
        viewAdapter = GroupAdapter(arrayListOf(), data)

        recyclerView = binding.groups.apply {
            adapter = viewAdapter
            layoutManager = LinearLayoutManager(requireContext())
        }

        viewAdapter.update(data.getGroups())
        if(viewAdapter.itemCount == 0){
            binding.noGroup.visibility = View.VISIBLE
        }
        binding.search.setOnClickListener { _ ->
            binding.noGroup.visibility = View.GONE
            binding.progressBar5.visibility = View.VISIBLE
            viewAdapter.update(arrayListOf())
            db.getReference(FirebaseNodes.groupsNode)
                .child(binding.searchBar.text.toString())
                .child("data")
                .get().addOnSuccessListener { data ->
                    binding.progressBar5.visibility = View.GONE
                if (data.exists()){
                    val g = data.getValue(Group::class.java)
                    g?.let {
                        viewAdapter.update(arrayListOf(it))
                    }
                }else{
                    binding.noGroup.visibility = View.VISIBLE
                    binding.noGroup.text  = "не найдено"
                }
            }.addOnFailureListener {
                    binding.progressBar5.visibility = View.GONE
                }
        }

        return binding.root
    }
}