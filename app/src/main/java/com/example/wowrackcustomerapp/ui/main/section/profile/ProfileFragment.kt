package com.example.wowrackcustomerapp.ui.main.section.profile

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.example.wowrackcustomerapp.databinding.FragmentProfileBinding
import com.example.wowrackcustomerapp.ui.ViewModelFactory

class ProfileFragment : Fragment() {

    companion object {
        fun newInstance() = ProfileFragment()
    }

    private val viewModel by viewModels<ProfileViewModel>{
        ViewModelFactory.getInstance(this.requireContext().applicationContext)
    }
    private lateinit var binding: FragmentProfileBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentProfileBinding.inflate(inflater,container,false)
//        return inflater.inflate(R.layout.fragment_profile, container, false)
        binding.buttonLogout.setOnClickListener{
            viewModel.logout()
        }
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
//        viewModel = ViewModelProvider(this)[MainViewModel::class.java]
        // TODO: Use the ViewModel
    }

}