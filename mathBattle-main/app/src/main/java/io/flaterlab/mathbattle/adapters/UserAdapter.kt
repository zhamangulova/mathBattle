package io.flaterlab.mathbattle.adapters;

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import io.flaterlab.mathbattle.databinding.ItemUserBinding
import io.flaterlab.mathbattle.models.User

class UserAdapter(private var myDataset: ArrayList<User>)
    : RecyclerView.Adapter<UserAdapter.MyViewHolder>() {

    lateinit var binding: ItemUserBinding

    class MyViewHolder(val itemUserBinding: ItemUserBinding)
        : RecyclerView.ViewHolder(itemUserBinding.root) {
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        binding = ItemUserBinding.inflate(inflater, parent, false)
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val user = myDataset[position]
        holder.itemUserBinding.username.text =  (position + 1).toString() +" : "+ user.username
        holder.itemUserBinding.level.text = user.level.toString()
        holder.itemUserBinding.login.text = "@" + user.login
        holder.itemUserBinding.score.text = user.score.toString()
    }

    override fun getItemCount() = myDataset.size

    fun update(list: ArrayList<User>) {
        myDataset = list
        notifyDataSetChanged()
    }
}