package com.example.aiventure.profile

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Switch
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.aiventure.R
import com.example.aiventure.chatbot.ChatBotActivity
import com.example.aiventure.data.datastore.UserDatastore
import com.example.aiventure.splashscreen.SplashScreen
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch


class ProfileFragment : Fragment() {
    private lateinit var userDatastore: UserDatastore

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_profile, container, false)
    }

    private lateinit var tvLogout: TextView
    private lateinit var tvName: TextView
    private lateinit var tvReservation: TextView
    private lateinit var tvAIMate: TextView
    private lateinit var switchDarkMode: Switch

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        userDatastore = UserDatastore(requireContext())

        switchDarkMode = view.findViewById(R.id.switchDarkMode)
        tvLogout = view.findViewById(R.id.tvLogout)
        tvName = view.findViewById(R.id.tvName)
        tvReservation = view.findViewById(R.id.tvReservation)
        tvAIMate = view.findViewById(R.id.tvAIMate)

        tvAIMate.setOnClickListener {
            val intent = Intent(requireContext(), ChatBotActivity::class.java)
            startActivity(intent)
        }

        lifecycleScope.launch {
            tvName.text = userDatastore.name.firstOrNull().orEmpty()
            switchDarkMode.isChecked = userDatastore.darkModeFlow.firstOrNull() == true
        }

        tvLogout.setOnClickListener {
            val builder = AlertDialog.Builder(requireContext())
            builder.setTitle("Confirm Logout")
                .setMessage("Are you sure you want to log out?")
                .setPositiveButton("Yes") { _, _ ->
                    lifecycleScope.launch {
                        userDatastore.clearToken()
                        val intent = Intent(requireContext(), SplashScreen::class.java)
                        intent.addFlags(
                            Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                        )
                        startActivity(intent)
                    }
                }
                .setNegativeButton("No") { dialog, _ ->
                    dialog.dismiss()
                }
                .show()
        }

        tvReservation.setOnClickListener {
            findNavController().navigate(R.id.navigation_booking)
        }

        switchDarkMode.setOnCheckedChangeListener { _, isChecked ->
            viewLifecycleOwner.lifecycleScope.launch {
                userDatastore.setDarkMode(isChecked)
                applyDarkMode(isChecked)
            }
        }
    }

    private fun applyDarkMode(isDarkMode: Boolean) {
        val mode = if (isDarkMode) {
            AppCompatDelegate.MODE_NIGHT_YES
        } else {
            AppCompatDelegate.MODE_NIGHT_NO
        }
        AppCompatDelegate.setDefaultNightMode(mode)
    }
}