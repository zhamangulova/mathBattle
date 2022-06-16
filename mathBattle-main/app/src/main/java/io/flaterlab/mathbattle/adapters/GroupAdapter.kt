package io.flaterlab.mathbattle.adapters;

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import io.flaterlab.mathbattle.BaseActivity
import io.flaterlab.mathbattle.GroupActivity
import io.flaterlab.mathbattle.databinding.ItemGroupBinding
import io.flaterlab.mathbattle.dialogs.GroupAccessDialog
import io.flaterlab.mathbattle.models.Data
import io.flaterlab.mathbattle.models.Group


class GroupAdapter(private var myDataset: ArrayList<Group>, val data: Data)
    : RecyclerView.Adapter<GroupAdapter.MyViewHolder>() {

    lateinit var binding: ItemGroupBinding

    class MyViewHolder(val item: ItemGroupBinding, val context: Context) : RecyclerView.ViewHolder(item.root)

    override fun onCreateViewHolder(parent: ViewGroup,
                                    viewType: Int): MyViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val b = ItemGroupBinding.inflate(inflater, parent, false)

        return MyViewHolder(b, parent.context)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val group = myDataset[position]
        holder.item.nameOfGroup.text = group.id
        holder.item.root.setOnClickListener {
            val g = data.findGroup(group.id)
            g?.let {
                val i = Intent(holder.context, GroupActivity::class.java)
                i.putExtra("id", group.id)
                holder.context.startActivity(i)
                return@setOnClickListener
            }
            GroupAccessDialog(holder.context, group.password){
                data.addGroup(group)
                val i = Intent(holder.context, GroupActivity::class.java)
                i.putExtra("id", group.id)
                holder.context.startActivity(i)
            }.show()
        }
    }

    override fun getItemCount() = myDataset.size

    fun update(list : ArrayList<Group>){
        myDataset = list
        notifyDataSetChanged()
    }
}