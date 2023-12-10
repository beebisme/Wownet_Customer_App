package com.example.wowrackcustomerapp.ui.main.section.help

import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.wowrackcustomerapp.R
import com.example.wowrackcustomerapp.databinding.ActivityHelpBinding
import com.example.wowrackcustomerapp.adapter.ChatAdapter
import com.example.wowrackcustomerapp.adapter.CommandAdapter
import com.example.wowrackcustomerapp.data.models.ChatMessage
import com.example.wowrackcustomerapp.data.models.Commands
import com.example.wowrackcustomerapp.ui.ViewModelFactory
import com.example.wowrackcustomerapp.utils.Constant
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlin.collections.ArrayList
import kotlin.collections.HashMap

class HelpActivity : AppCompatActivity() {
    private lateinit var binding: ActivityHelpBinding
    private lateinit var chatAdapter: ChatAdapter

    //    private lateinit var chatMessage: List<ChatMessage>
    private lateinit var chatMessage: MutableList<ChatMessage>
    private lateinit var listCmd: MutableList<Commands>
    private lateinit var database: FirebaseDatabase
    private val viewModel by viewModels<HelpViewModel> {
        ViewModelFactory.getInstance(this)
    }
    private lateinit var senderId: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHelpBinding.inflate(layoutInflater)
        setContentView(binding.root)
        senderId = intent.getStringExtra("senderId").toString()
        Log.d("senderOncreate", senderId)
        setupView()
        setListeners()
        readMessage(senderId, Constant.RECEIVER_ID)
        getCommandsFromDB()

    }

    private fun setListeners() {
        binding.imageBack.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

        binding.layoutSend.setOnClickListener {
            // Handle the send button click here
//            sendMessage()
            val message = binding.inputMessage.text.toString()
            if (message.isEmpty()) {
                Toast.makeText(applicationContext, "Message can't b empty", Toast.LENGTH_SHORT)
                    .show()
                binding.inputMessage.text = null
            } else {
                sendMessage(senderId, Constant.RECEIVER_ID, message)
                binding.inputMessage.text = null
            }
        }

    }

    private fun setupView() {
        viewModel.getSession().observe(this) { user ->
            senderId = user.userId
            Log.d("senderGetSession", senderId)
        }
        Log.d("sender", senderId)
        // Initialize chatMessage with your data
        chatMessage = mutableListOf(
//            ChatMessage(1, senderId, "CustomerService", "Test!"),
//            ChatMessage(2, "CustomerService", senderId, "test123!")
            // Add more messages as needed
        )
        listCmd = mutableListOf()

        binding.commandRecyclerView.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        val listCommandAdapter = CommandAdapter(listCmd)
        binding.commandRecyclerView.adapter = listCommandAdapter
//        getCommandsFromDB()
//        listCmd.addAll(get)
        listCommandAdapter.setCommandClickListener(object : CommandAdapter.CommandClickListener {
            @Suppress("DEPRECATION")
            override fun onCommandClick(command: Commands) {
                // Handle the command click here, you can use the command value to send a message
                val message = command.cmd
//                sendMessage(senderId, Constant.RECEIVER_ID, message)
//                sendMessage(Constant.RECEIVER_ID, senderId, message)
                sendMessage(senderId, Constant.RECEIVER_ID, message)
                val delayMillis = 2000L
                Handler().postDelayed({
                    sendMessage(Constant.RECEIVER_ID, senderId, message)
                }, delayMillis)
            }
        })
        database =
            FirebaseDatabase.getInstance("https://wowrackcustomerapp-default-rtdb.asia-southeast1.firebasedatabase.app")
    }

//    private fun getCmd(): ArrayList<Commands> {
//        val cmd = resources.getStringArray(R.array.data_command)
//        val listCmd = ArrayList<Commands>()
//        for (i in cmd.indices) {
//            val commands = Commands(cmd[i], cmd[i])
//            listCmd.add(commands)
//        }
//        return listCmd
//    }

    private fun sendMessage(senderId: String, receiverId: String, message: String) {
        val reference = database.reference

        val hashMap: HashMap<String, Any> = HashMap()
        hashMap["senderId"] = senderId
        hashMap["receiverId"] = receiverId
        hashMap["message"] = message
        val chat = ChatMessage(
            senderId = senderId,
            receiverId = receiverId,
            message = message
        )

        chatMessage = mutableListOf(
            ChatMessage(
                senderId = senderId,
                receiverId = receiverId,
                message = message
            )
        )

        reference.child("Chat").push().setValue(hashMap)
        Log.d("send", "berhasil")
        chatMessage.add(chat)
        Log.d("chatList", chatMessage.toString())

    }

    private fun readMessage(senderId: String, receiverId: String) {
        val databaseReference: DatabaseReference =
            FirebaseDatabase.getInstance(" https://wowrackcustomerapp-default-rtdb.asia-southeast1.firebasedatabase.app")
                .getReference("Chat")

        databaseReference.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(error: DatabaseError) {
                Log.w("HelpDb", "Failed to read value.", error.toException())
            }

            override fun onDataChange(snapshot: DataSnapshot) {
                chatMessage.clear()
                for (dataSnapShot: DataSnapshot in snapshot.children) {
                    val chatData = dataSnapShot.value as HashMap<*, *>
                    val chat = ChatMessage(
                        senderId = chatData["senderId"].toString(),
                        receiverId = chatData["receiverId"].toString(),
                        message = chatData["message"].toString()
                    )
                    Log.d("chat", chat.toString())
                    val ceksender = chat.senderId == senderId ||
                            chat.receiverId == senderId
                    Log.d("ceksender", ceksender.toString())
                    Log.d("senderid", chat.senderId.toString())
                    Log.d("sender", senderId)
                    if (chat.senderId == senderId && chat.receiverId.equals(receiverId) ||
                        chat.receiverId == senderId && chat.receiverId.equals(senderId)
                    ) {
                        chatMessage.add(chat)
                    }
                }
                chatAdapter = ChatAdapter(chatMessage, senderId)
                binding.chatRecyclerView.adapter = chatAdapter
                binding.chatRecyclerView.scrollToPosition(chatMessage.size - 1)
            }
        })
    }

    private fun getCommandsFromDB() {
        val databaseReference: DatabaseReference =
            FirebaseDatabase.getInstance("https://wowrackcustomerapp-default-rtdb.asia-southeast1.firebasedatabase.app")
                .getReference("Commands")

//        val listCmd = ArrayList<Commands>()

        databaseReference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                listCmd.clear() // Clear the list to avoid duplicates on data change
                for (dataSnapShot: DataSnapshot in snapshot.children) {
                    val commandsData = dataSnapShot.value as HashMap<*, *>
                    Log.d("cdata", commandsData.toString())
                    val cmd = Commands(commandsData["cmd"].toString(), commandsData["response"].toString())
                    Log.d("cmd", cmd.toString())
                    listCmd.add(cmd)
                }
                // Now listCmd should contain all the commands
                // You can update your UI or perform any other action here
                // For example, update the adapter if needed
                Log.d("list",listCmd.toString())
                val listCommandAdapter = CommandAdapter(listCmd)
                binding.commandRecyclerView.adapter = listCommandAdapter
                listCommandAdapter.setCommandClickListener(object :
                    CommandAdapter.CommandClickListener {
                    override fun onCommandClick(command: Commands) {
                        val message = command.cmd
                        val response = command.response
                        sendMessage(senderId, Constant.RECEIVER_ID, message)
                        val delayMillis = 2000L
                        Handler().postDelayed({
                            sendMessage(Constant.RECEIVER_ID, senderId, response)
                        }, delayMillis)
                    }
                })
            }

            override fun onCancelled(error: DatabaseError) {
                Log.w("HelpDb", "Failed to read value.", error.toException())
            }
        })
    }
}