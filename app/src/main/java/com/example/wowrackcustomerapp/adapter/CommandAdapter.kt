package com.example.wowrackcustomerapp.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.wowrackcustomerapp.data.models.Commands
import com.example.wowrackcustomerapp.databinding.ItemContainerCommandMessageBinding

class CommandAdapter(private val listCommand: ArrayList<Commands>) :
    RecyclerView.Adapter<CommandAdapter.ListViewHolder>() {
    private var commandClickListener: CommandClickListener? = null

    fun setCommandClickListener(listener: CommandClickListener) {
        this.commandClickListener = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val binding = ItemContainerCommandMessageBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ListViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return listCommand.size
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        val currentCmd = listCommand[position]
        holder.bind(currentCmd)

        holder.itemView.setOnClickListener {
            commandClickListener?.onCommandClick(currentCmd)
        }
    }

    class ListViewHolder(private val binding: ItemContainerCommandMessageBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(cmd: Commands) {
            binding.textCommands.text = cmd.cmd
        }
    }

    interface CommandClickListener {
        fun onCommandClick(command: Commands)
    }
}
