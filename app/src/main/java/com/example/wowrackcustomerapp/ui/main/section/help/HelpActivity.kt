package com.example.wowrackcustomerapp.ui.main.section.help

import android.content.Intent
import android.icu.text.SimpleDateFormat
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ReportFragment.Companion.reportFragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.wowrackcustomerapp.R
import com.example.wowrackcustomerapp.databinding.ActivityHelpBinding
import com.example.wowrackcustomerapp.ui.main.MainActivity
import com.example.wowrackcustomerapp.adapter.ChatAdapter
import com.example.wowrackcustomerapp.adapter.CommandAdapter
import com.example.wowrackcustomerapp.data.models.ChatMessage
import com.example.wowrackcustomerapp.data.models.Commands
import com.example.wowrackcustomerapp.ui.ViewModelFactory
import com.example.wowrackcustomerapp.ui.main.MainViewModel
import com.example.wowrackcustomerapp.utils.Constant
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.getValue
import com.google.firebase.database.ktx.database
import com.google.firebase.firestore.DocumentChange
import com.google.firebase.firestore.EventListener
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.ktx.Firebase
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap

class HelpActivity : AppCompatActivity() {
    private lateinit var binding: ActivityHelpBinding
//    private lateinit var chatAdapter: ChatAdapter

    //    private lateinit var chatMessage: List<ChatMessage>
//    private lateinit var chatMessage: ArrayList<ChatMessage>
    private var chatList = ArrayList<ChatMessage>()
    private val listCmd = ArrayList<Commands>()
    private lateinit var database: FirebaseDatabase
    private val viewModel by viewModels<HelpViewModel> {
        ViewModelFactory.getInstance(this)
    }
    private var senderId: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHelpBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupView()
        setListeners()
        readMessage(senderId,Constant.RECEIVER_ID)

    }

    private fun setListeners() {
        binding.imageBack.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

        binding.layoutSend.setOnClickListener {
            // Handle the send button click here
//            sendMessage()
            var message = binding.inputMessage.text.toString()
            if (message.isEmpty()){
                Toast.makeText(applicationContext,"Message can't b empty", Toast.LENGTH_SHORT).show()
                binding.inputMessage.text = null
            }else{
                sendMessage(senderId,Constant.RECEIVER_ID,message)
                binding.inputMessage.text = null
            }
        }

    }

    private fun setupView() {
        viewModel.getSession().observe(this) { user ->
            senderId = user.userId
        }
        Log.d("sender", senderId)
        // Initialize chatMessage with your data
//        chatMessage = mutableListOf(
//            ChatMessage(1, senderId, "CustomerService", "Test!"),
//            ChatMessage(2, "CustomerService", senderId, "test123!")
//            // Add more messages as needed
//        )

//        chatMessage = ArrayList()
//        chatAdapter = ChatAdapter(chatMessage, senderId)
//        binding.chatRecyclerView.adapter = chatAdapter
//        chatList = ArrayList<ChatMessage>()
//        chatAdapter = ChatAdapter(this@HelpActivity, chatList, senderId)

//        binding.chatRecyclerView.adapter = chatAdapter

        binding.commandRecyclerView.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        val listCommandAdapter = CommandAdapter(listCmd)
        binding.commandRecyclerView.adapter = listCommandAdapter
        listCmd.addAll(getCmd())
        database = FirebaseDatabase.getInstance("https://wowrackcustomerapp-default-rtdb.asia-southeast1.firebasedatabase.app")

    }

//    private fun sendMessage() {
//        val message = HashMap<String, Any>()
//        message[Constant.KEY_SENDER_ID] = senderId
//        message[Constant.KEY_RECEIVER_ID] = "CustomerService"
//        message[Constant.KEY_MESSAGE] = binding.inputMessage.text.toString()
//        message[Constant.KEY_TIMESTAMP] = Date()
//        database.collection(Constant.KEY_COLLECTION_CHAT).add(message)
//        binding.inputMessage.text.clear()
//    }

//    private fun getCurrentDateTime(): String {
//        val dateFormat = java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
//        val date = Date()
//        return dateFormat.format(date)
//    }

    private fun getCmd(): ArrayList<Commands> {
        val cmd = resources.getStringArray(R.array.data_command)
        val listCmd = ArrayList<Commands>()
        for (i in cmd.indices) {
            val commands = Commands(cmd[i])
            listCmd.add(commands)
        }
        return listCmd
    }

//    private val eventListener = EventListener<QuerySnapshot> { value, error ->
//        if (error != null) {
//            return@EventListener
//        }
//        if (value != null) {
//            var count: Int = chatMessage.size
//            for (documentChange in value.documentChanges) {
//                if (documentChange.type == DocumentChange.Type.ADDED) {
//                    var message: ChatMessage = ChatMessage()
//                    message.senderId = documentChange.document.getString(Constant.KEY_SENDER_ID)
//                    message.receiverId = documentChange.document.getString(Constant.KEY_RECEIVER_ID)
//                    message.message = documentChange.document.getString(Constant.KEY_MESSAGE)
//                    message.dateObject =
//                        documentChange.document.getDate(Constant.KEY_TIMESTAMP) // Assuming dateObject is defined in message
//                    chatMessage.add(message)
//                }
//            }
//
//            chatMessage.sortWith { obj1, obj2 -> obj1.dateObject!!.compareTo(obj2.dateObject) }
//            if (count == 0) {
//                chatAdapter.notifyDataSetChanged()
//            } else {
//                chatAdapter.notifyItemRangeInserted(chatMessage.size, chatMessage.size)
//                binding.chatRecyclerView.smoothScrollToPosition(chatMessage.size - 1)
//            }
//            binding.chatRecyclerView.visibility = View.VISIBLE
//        }
//    }
//    private fun addChatMessage(chatMessage: ChatMessage) {
//        chatMessage.add(chatMessage)
//        chatAdapter.notifyDataSetChanged()
//        binding.chatRecyclerView.smoothScrollToPosition(chatMessageList.size - 1)
//        binding.chatRecyclerView.visibility = View.VISIBLE
//    }

//    private fun listenMessage() {
//        database.collection(Constant.KEY_COLLECTION_CHAT)
//            .whereEqualTo(Constant.KEY_SENDER_ID, senderId)
//            .whereEqualTo(Constant.KEY_RECEIVER_ID, "CustomerService")
//            .addSnapshotListener(eventListener)
//        database.collection(Constant.KEY_COLLECTION_CHAT)
//            .whereEqualTo(Constant.KEY_SENDER_ID, "CustomerService")
//            .whereEqualTo(Constant.KEY_RECEIVER_ID, senderId)
//            .addSnapshotListener(eventListener)
//    }

//    private fun getReadableDateTime(date: Date): String {
//        return SimpleDateFormat("MMMM dd, yyyy - hh:mm a", Locale.getDefault()).format(date)
//    }

    private fun sendMessage(senderId: String, receiverId: String, message: String) {
//        val database = FirebaseDatabase.getInstance("https://wowrackcustomerapp-default-rtdb.asia-southeast1.firebasedatabase.app")
        var reference = database.reference

        var hashMap: HashMap<String, Any> = HashMap()
        hashMap["senderId"] = senderId
        hashMap["receiverId"] = receiverId
        hashMap["message"] = message
        val chat = ChatMessage(
            senderId = senderId,
            receiverId = receiverId,
            message = message
        )

//        reference.setValue(hashMap)
        reference.child("Chat").push().setValue(hashMap)
        Log.d("send","berhasil")
        chatList.add(chat)

    }

//    private fun readMessage(senderId: String, receiverId: String){
////        val database = Firebase.database
//        var databaseReference: DatabaseReference =
//            FirebaseDatabase.getInstance("https://wowrackcustomerapp-default-rtdb.asia-southeast1.firebasedatabase.app").getReference("Chat")
//
//        databaseReference.addValueEventListener(object : ValueEventListener{
//            override fun onDataChange(snapshot: DataSnapshot) {
//                for (dataSnapShot: DataSnapshot in snapshot.children) {
//                    val chat = dataSnapShot.getValue<ChatMessage>(ChatMessage::class.java)
//                    val chatSenderId = dataSnapShot.getValue(ChatMessage::class.java)!!.senderId
//                    val chatReceiverId = dataSnapShot.getValue(ChatMessage::class.java)!!.receiverId
//                    val chatMessage = dataSnapShot.getValue(ChatMessage::class.java)!!.message
////                    val chat = ChatMessage(
////                        senderId = chatSenderId,
////                        receiverId = chatReceiverId,
////                        message = chatMessage
////                    )
//                    if (chat!!.senderId.equals(senderId) && chat!!.receiverId.equals(receiverId) ||
//                        chat!!.senderId.equals(receiverId) && chat!!.receiverId.equals(senderId)
//                    ) {
//                        chatList.add(chat)
//                    }
//                }
//
//
//            }
//
//            override fun onCancelled(error: DatabaseError) {
//                TODO("Not yet implemented")
//            }
//
//        })
//    }
fun readMessage(senderId: String, receiverId: String) {
    val databaseReference: DatabaseReference =
        FirebaseDatabase.getInstance().getReference("Chat")

    databaseReference.addValueEventListener(object : ValueEventListener {
        override fun onCancelled(error: DatabaseError) {
            TODO("Not yet implemented")
        }

        override fun onDataChange(snapshot: DataSnapshot) {
            chatList.clear()
            for (dataSnapShot: DataSnapshot in snapshot.children) {
                val chat = dataSnapShot.getValue(ChatMessage::class.java)

                if (chat!!.senderId.equals(senderId) && chat!!.receiverId.equals(receiverId) ||
                    chat!!.senderId.equals(receiverId) && chat!!.receiverId.equals(senderId)
                ) {
                    chatList.add(chat)
                }
            }

            val chatAdapter = ChatAdapter(this@HelpActivity, chatList,senderId)

            binding.chatRecyclerView.adapter = chatAdapter
        }
    })
}
}
