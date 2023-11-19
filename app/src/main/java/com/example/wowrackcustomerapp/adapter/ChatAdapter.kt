package com.example.wowrackcustomerapp.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.wowrackcustomerapp.data.models.ChatMessage
import com.example.wowrackcustomerapp.databinding.ItemContainerReceivedMessageBinding
import com.example.wowrackcustomerapp.databinding.ItemContainerSentMessageBinding

class ChatAdapter(private val chatMessage: List<ChatMessage>,private val senderId: String) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    val VIEW_TYPE_SENT = 1
    val VIEW_TYPE_RECEIVED = 2


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
//        TODO("Not yet implemented")
        if (viewType == VIEW_TYPE_SENT){
            return SendMessageViewHolder(
                ItemContainerSentMessageBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
            )
        }else{
            return ReceivedMessageViewHolder(
                ItemContainerReceivedMessageBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
            )
        }
    }

    override fun getItemCount(): Int {
        return chatMessage.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (getItemViewType(position) == VIEW_TYPE_SENT){
            (holder as SendMessageViewHolder).setData(chatMessage[position])
        }else{
            (holder as ReceivedMessageViewHolder).setData(chatMessage[position])
        }
    }

    override fun getItemViewType(position: Int): Int {
        if (chatMessage.get(position).senderId.equals(senderId)){
            return VIEW_TYPE_SENT
        }else{
            return VIEW_TYPE_RECEIVED
        }

    }
    inner class SendMessageViewHolder(private val binding: ItemContainerSentMessageBinding)
        : RecyclerView.ViewHolder(binding.root){
            fun setData(chatMessage: ChatMessage){
                binding.textMessage.text = chatMessage.message
//                binding.dateMessage.text = chatMessage.dateTime
            }
        }

    inner class ReceivedMessageViewHolder(private val binding: ItemContainerReceivedMessageBinding)
        : RecyclerView.ViewHolder(binding.root){
        fun setData(chatMessage: ChatMessage){
            binding.textReceivedMessage.text = chatMessage.message
//            binding.dateMessage.text = chatMessage.dateTime
        }
    }

    companion object {
        private val DiffCallback = object : DiffUtil.ItemCallback<ChatMessage>() {
            override fun areItemsTheSame(oldItem: ChatMessage, newItem: ChatMessage): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: ChatMessage, newItem: ChatMessage): Boolean {
                return oldItem == newItem
            }
        }
    }

}