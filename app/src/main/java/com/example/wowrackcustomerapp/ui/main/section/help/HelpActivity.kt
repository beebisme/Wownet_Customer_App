package com.example.wowrackcustomerapp.ui.main.section.help

import android.content.Intent
import android.icu.text.SimpleDateFormat
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.wowrackcustomerapp.databinding.ActivityHelpBinding
import com.example.wowrackcustomerapp.ui.main.MainActivity
import com.example.wowrackcustomerapp.adapter.ChatAdapter
import com.example.wowrackcustomerapp.data.models.ChatMessage
import java.util.*

class HelpActivity : AppCompatActivity() {
    private lateinit var binding: ActivityHelpBinding
    private lateinit var chatAdapter: ChatAdapter
    private lateinit var chatMessage: List<ChatMessage>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHelpBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setListeners()
        setupView()
    }

    private fun setListeners() {
        binding.imageBack.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }

        binding.layoutSend.setOnClickListener {
            // Handle the send button click here
            sendMessage()
        }
    }

    private fun setupView() {
        // Initialize chatMessage with your data
        chatMessage = mutableListOf(
            ChatMessage(1, "senderId", "received","Test!" ),
            ChatMessage(2, "otherSenderId", "received","test123!")
            // Add more messages as needed
        )

        chatAdapter = ChatAdapter(chatMessage, "senderId")
        binding.chatRecyclerView.adapter = chatAdapter
    }

    private fun sendMessage() {
        // Get the message from the input field
        val message = binding.inputMessage.text.toString()

        // Check if the message is not empty
        if (message.isNotEmpty()) {
            // Create a new ChatMessage and add it to the list
            val newMessage = ChatMessage(3, "senderId", "received",message)
            chatMessage.toMutableList().add(newMessage)

            // Notify the adapter about the new message
            chatAdapter.notifyDataSetChanged()

            // Clear the input field
            binding.inputMessage.text.clear()

            // Optionally, you can scroll to the last item in the RecyclerView
            binding.chatRecyclerView.smoothScrollToPosition(chatAdapter.itemCount - 1)
        }
    }
    private fun getCurrentDateTime(): String {
        val dateFormat = java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
        val date = Date()
        return dateFormat.format(date)
    }
}
