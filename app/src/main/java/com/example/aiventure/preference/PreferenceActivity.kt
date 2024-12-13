package com.example.aiventure.preference

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.FrameLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import com.example.aiventure.MainActivity
import com.example.aiventure.R
import com.example.aiventure.data.datastore.UserDatastore
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch

class PreferenceActivity : AppCompatActivity() {

    private lateinit var flFirst: FrameLayout
    private lateinit var flSecond: FrameLayout
    private lateinit var flThird: FrameLayout
    private lateinit var flFourth: FrameLayout
    private lateinit var flFifth: FrameLayout
    private lateinit var flFirstChecked: FrameLayout
    private lateinit var flSecondChecked: FrameLayout
    private lateinit var flThirdChecked: FrameLayout
    private lateinit var flFourthChecked: FrameLayout
    private lateinit var flFifthChecked: FrameLayout
    private lateinit var btnSkip: Button
    private lateinit var btnNext: Button

    private lateinit var userDatastore: UserDatastore

    private var checkedIndex: Int = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fragment_preference)


        userDatastore = UserDatastore(this)

        lifecycleScope.launch {
            if (userDatastore.clusterKey.firstOrNull() != -1) {
                val intent = Intent(this@PreferenceActivity, MainActivity::class.java)
                startActivity(intent)
                finish()
            }
        }

        flFirst = findViewById(R.id.fl_first)
        flFirst = findViewById(R.id.fl_first)
        flSecond = findViewById(R.id.fl_second)
        flThird = findViewById(R.id.fl_third)
        flFourth = findViewById(R.id.fl_fourth)
        flFifth = findViewById(R.id.fl_fifth)

        btnSkip = findViewById(R.id.btnSkip)
        btnNext = findViewById(R.id.btnNext)

        flFirstChecked = findViewById(R.id.fl_first_checked)
        flSecondChecked = findViewById(R.id.fl_second_checked)
        flThirdChecked = findViewById(R.id.fl_third_checked)
        flFourthChecked = findViewById(R.id.fl_fourth_checked)
        flFifthChecked = findViewById(R.id.fl_fifth_checked)

        flFirst.setOnClickListener {
            flFirstChecked.isVisible = true
            flSecondChecked.isVisible = false
            flThirdChecked.isVisible = false
            flFourthChecked.isVisible = false
            flFifthChecked.isVisible = false
            checkedIndex = 1
        }

        flSecond.setOnClickListener {
            flFirstChecked.isVisible = false
            flSecondChecked.isVisible = true
            flThirdChecked.isVisible = false
            flFourthChecked.isVisible = false
            flFifthChecked.isVisible = false
            checkedIndex = 2
        }

        flThird.setOnClickListener {
            flFirstChecked.isVisible = false
            flSecondChecked.isVisible = false
            flThirdChecked.isVisible = true
            flFourthChecked.isVisible = false
            flFifthChecked.isVisible = false
            checkedIndex = 3
        }

        flFourth.setOnClickListener {
            flFirstChecked.isVisible = false
            flSecondChecked.isVisible = false
            flThirdChecked.isVisible = false
            flFourthChecked.isVisible = true
            flFifthChecked.isVisible = false
            checkedIndex = 4
        }

        flFifth.setOnClickListener {
            flFirstChecked.isVisible = false
            flSecondChecked.isVisible = false
            flThirdChecked.isVisible = false
            flFourthChecked.isVisible = false
            flFifthChecked.isVisible = true
            checkedIndex = 5
        }

        btnSkip.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }

        btnNext.setOnClickListener {
            if (checkedIndex == -1) {
                Toast.makeText(this, "Please choose first", Toast.LENGTH_SHORT).show()
            } else {
                lifecycleScope.launch {
                    userDatastore.setCluster(checkedIndex)
                    val intent = Intent(this@PreferenceActivity, MainActivity::class.java)
                    startActivity(intent)
                    finish()
                }
            }
        }
    }
}