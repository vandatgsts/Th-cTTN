package com.pnt.paypaldemo

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.pnt.paypaldemo.databinding.ActivityCardsBinding

class CardsActivity : AppCompatActivity() {
    private lateinit var binding: ActivityCardsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityCardsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar!!.hide()

        binding.btnMore1.background = null
        binding.btnMore2.background = null
        binding.btnBack.setOnClickListener {
            startActivity(Intent(this, PayActivity::class.java))
        }
    }
}