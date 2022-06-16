package io.flaterlab.mathbattle.ui.dashboard


import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import io.flaterlab.mathbattle.adapters.UserAdapter
import io.flaterlab.mathbattle.databinding.FragmentDashboardBinding
import io.flaterlab.mathbattle.models.User
import io.flaterlab.mathbattle.util.FirebaseNodes

class DashboardFragment : Fragment() {

    private lateinit var dashboardViewModel: DashboardViewModel

    private lateinit var binding: FragmentDashboardBinding

    private lateinit var mAdapter: UserAdapter
    private lateinit var recyclerView: RecyclerView

    private val db = FirebaseFirestore.getInstance()

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?

    ): View {

        dashboardViewModel =
                ViewModelProvider(requireActivity()).get(DashboardViewModel::class.java)
        binding = FragmentDashboardBinding.inflate(layoutInflater)

        mAdapter = UserAdapter(arrayListOf())

        recyclerView = binding.recycler.apply {
            adapter = mAdapter
            layoutManager = LinearLayoutManager(requireContext())
        }

        if(dashboardViewModel.users.size > 0){
            mAdapter.update(dashboardViewModel.users)
            return binding.root
        }

        db.collection(FirebaseNodes.usersNode)
                .orderBy("score", Query.Direction.DESCENDING)
                .limit(100).get()
                .addOnCanceledListener {

                }.addOnSuccessListener { qas ->
                    val list = arrayListOf<User>()
                    qas.documents.forEach {
                        val u = User(
                                it["id"] as String,
                                it["username"] as String,
                                it["login"] as String,
                                "",
                                (it["score"] as Long).toInt(),
                                level = (it["level"] as Long).toInt()
                        )
                        list.add(u)
                    }
                    Log.d("dfasf", list.toString())
                    dashboardViewModel.users = list
                    mAdapter.update(list)
                }.addOnFailureListener {

                }

        return binding.root
    }
}