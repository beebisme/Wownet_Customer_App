package com.example.wowrackcustomerapp.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.wowrackcustomerapp.R
import com.example.wowrackcustomerapp.data.models.ChatMessage
import com.example.wowrackcustomerapp.databinding.ItemContainerReceivedMessageBinding
import com.example.wowrackcustomerapp.databinding.ItemContainerSentMessageBinding

//
//class ChatAdapter(private val chatMessage: List<ChatMessage>,private val senderId: String) :
//    RecyclerView.Adapter<RecyclerView.ViewHolder>() {
//
//    val VIEW_TYPE_SENT = 1
//    val VIEW_TYPE_RECEIVED = 2
//
//
//    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
////        TODO("Not yet implemented")
//        if (viewType == VIEW_TYPE_SENT){
//            return SendMessageViewHolder(
//                ItemContainerSentMessageBinding.inflate(
//                    LayoutInflater.from(parent.context),
//                    parent,
//                    false
//                )
//            )
//        }else{
//            return ReceivedMessageViewHolder(
//                ItemContainerReceivedMessageBinding.inflate(
//                    LayoutInflater.from(parent.context),
//                    parent,
//                    false
//                )
//            )
//        }
//    }
//
//    override fun getItemCount(): Int {
//        return chatMessage.size
//    }
//
//    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
//        if (getItemViewType(position) == VIEW_TYPE_SENT){
//            (holder as SendMessageViewHolder).setData(chatMessage[position])
//        }else{
//            (holder as ReceivedMessageViewHolder).setData(chatMessage[position])
//        }
//    }
//
//    override fun getItemViewType(position: Int): Int {
//        if (chatMessage.get(position).senderId.equals(senderId)){
//            return VIEW_TYPE_SENT
//        }else{
//            return VIEW_TYPE_RECEIVED
//        }
//
//    }
//    inner class SendMessageViewHolder(private val binding: ItemContainerSentMessageBinding)
//        : RecyclerView.ViewHolder(binding.root){
//            fun setData(chatMessage: ChatMessage){
//                binding.textMessage.text = chatMessage.message
////                binding.dateMessage.text = chatMessage.dateTime
//            }
//        }
//
//    inner class ReceivedMessageViewHolder(private val binding: ItemContainerReceivedMessageBinding)
//        : RecyclerView.ViewHolder(binding.root){
//        fun setData(chatMessage: ChatMessage){
//            binding.textReceivedMessage.text = chatMessage.message
////            binding.dateMessage.text = chatMessage.dateTime
//        }
//    }
//
//    companion object {
//        private val DiffCallback = object : DiffUtil.ItemCallback<ChatMessage>() {
//            override fun areItemsTheSame(oldItem: ChatMessage, newItem: ChatMessage): Boolean {
//                return oldItem.id == newItem.id
//            }
//
//            override fun areContentsTheSame(oldItem: ChatMessage, newItem: ChatMessage): Boolean {
//                return oldItem == newItem
//            }
//        }
//    }
//
//}

//class ChatAdapter(
//    private val context: Context,
//    private val chatList: ArrayList<ChatMessage>,
//    private val senderId: String
//) :
//    RecyclerView.Adapter<RecyclerView.ViewHolder>() {
//    private val VIEW_TYPE_SENT = 1
//    private val VIEW_TYPE_RECEIVED = 2
//
//    inner class SendMessageViewHolder(private val binding: ItemContainerSentMessageBinding)
//        : RecyclerView.ViewHolder(binding.root){
//            fun setData(chatMessage: ChatMessage){
//                binding.textMessage.text = chatMessage.message
//                binding.dateMessage.text = chatMessage.dateObject.toString()
//            }
//        }
//
//    inner class ReceivedMessageViewHolder(private val binding: ItemContainerReceivedMessageBinding)
//        : RecyclerView.ViewHolder(binding.root){
//        fun setData(chatMessage: ChatMessage){
//            binding.textReceivedMessage.text = chatMessage.message
//            binding.dateMessage.text = chatMessage.dateObject.toString()
//        }
//    }
//
//    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
//        if (viewType == VIEW_TYPE_SENT) {
//            return SendMessageViewHolder(
//                ItemContainerSentMessageBinding.inflate(
//                    LayoutInflater.from(parent.context),
//                    parent,
//                    false
//                )
//            )
////            val view = LayoutInflater.from(parent.context)
////                .inflate(R.layout.item_container_sent_message, parent, false)
////            return ViewHolder(view)
//        } else {
//            return ReceivedMessageViewHolder(
//                ItemContainerReceivedMessageBinding.inflate(
//                    LayoutInflater.from(parent.context),
//                    parent,
//                    false
//                )
//            )
////            val view = LayoutInflater.from(parent.context)
////                .inflate(R.layout.item_container_received_message, parent, false)
////            return ViewHolder(view)
//        }
//    }
//
//    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
//        if (getItemViewType(position) == VIEW_TYPE_SENT){
//            (holder as SendMessageViewHolder).setData(chatList[position])
//        }else{
//            (holder as ReceivedMessageViewHolder).setData(chatList[position])
//        }
//    }
//
//
//    override fun getItemCount(): Int {
//        return chatList.size
//    }
//
//    override fun getItemViewType(position: Int): Int {
//        if (chatList[position].senderId.equals(senderId)) {
//            return VIEW_TYPE_SENT
//        } else {
//            return VIEW_TYPE_RECEIVED
//        }
//    }
//
//}

class ChatAdapter(private val context: Context, private val chatList: ArrayList<ChatMessage>, private val senderId : String) :
    RecyclerView.Adapter<ChatAdapter.ViewHolder>() {

    private val MESSAGE_TYPE_LEFT = 0
    private val MESSAGE_TYPE_RIGHT = 1

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        if (viewType == MESSAGE_TYPE_RIGHT) {
            val view =
                LayoutInflater.from(parent.context).inflate(R.layout.item_container_sent_message, parent, false)
            return ViewHolder(view)
        } else {
            val view =
                LayoutInflater.from(parent.context).inflate(R.layout.item_container_received_message, parent, false)
            return ViewHolder(view)
        }

    }

    override fun getItemCount(): Int {
        return chatList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val chat = chatList[position]
        holder.txtSentMessage.text = chat.message
        //Glide.with(context).load(user.profileImage).placeholder(R.drawable.profile_image).into(holder.imgUser)

    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        val txtSentMessage: TextView = view.findViewById(R.id.textMessage)
        val txtReceivedMessage: TextView = view.findViewById(R.id.textReceivedMessage)
    }

    override fun getItemViewType(position: Int): Int {
//        firebaseUser = FirebaseAuth.getInstance().currentUser
        if (chatList[position].senderId == senderId) {
            return MESSAGE_TYPE_RIGHT
        } else {
            return MESSAGE_TYPE_LEFT
        }

    }
}