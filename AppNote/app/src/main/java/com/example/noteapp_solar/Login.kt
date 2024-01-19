package com.example.noteapp_solar

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.noteapp_solar.databinding.ActivityLoginBinding

class Login : AppCompatActivity() {
    lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setUpPassCodeInput()
        val share = getSharedPreferences("PassWord", MODE_PRIVATE)
        binding.btnLogin.setOnClickListener {
            val pass = binding.inputCode1.text.toString() + binding.inputCode2.text.toString() +
                    binding.inputCode3.text.toString() + binding.inputCode4.text.toString()

            if (pass.length != 4) {
                Toast.makeText(this, "Vui lòng nhập mật khẩu gồm 4 số", Toast.LENGTH_SHORT).show()
            } else {
                val isSetPass = share.getBoolean("isSetPassword", false)
                val localPass = if (!isSetPass) "0000" else share.getString("password", "")
                Log.d("pass", "$pass - $localPass: ")
                if (pass == localPass) {
                    //login
                    startActivity(Intent(this, NoteHomeScreen::class.java))
                    finish()
                } else {
                    Toast.makeText(this, "Mật khẩu chưa chính xác", Toast.LENGTH_SHORT).show()
                }
            }

        }

        binding.btnChangePass.setOnClickListener {//change PassWord to Login
            startActivity(Intent(this, ChangePassActivity::class.java))
            finish()
        }
    }

    private fun setUpPassCodeInput() {
        binding.inputCode1.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                if (p0.toString().trim().isNotEmpty()) {
                    binding.inputCode2.requestFocus()
                }
            }

            override fun afterTextChanged(p0: Editable?) {

            }
        })
        binding.inputCode2.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                if (p0.toString().trim().isNotEmpty()) {
                    binding.inputCode3.requestFocus()
                }
            }

            override fun afterTextChanged(p0: Editable?) {
            }

        })
        binding.inputCode3.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                if (p0.toString().trim().isNotEmpty()) {
                    binding.inputCode4.requestFocus()
                }
            }

            override fun afterTextChanged(p0: Editable?) {
            }

        })
    }
}