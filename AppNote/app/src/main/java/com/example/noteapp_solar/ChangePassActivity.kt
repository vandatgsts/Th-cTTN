package com.example.noteapp_solar

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.noteapp_solar.databinding.ActivityChangePassBinding

class ChangePassActivity : AppCompatActivity() {
    lateinit var binding: ActivityChangePassBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChangePassBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val share = getSharedPreferences("PassWord", MODE_PRIVATE)
        val edit = share.edit()
        setUpPassCodeInput()
        binding.btnConfirm.setOnClickListener {
            val pass = binding.input1.text.toString() + binding.input2.text.toString() +
                    binding.input3.text.toString() + binding.input4.text.toString()

            if (pass.length != 4) {
                Toast.makeText(this, "Vui lòng nhập mật khẩu gồm 4 số", Toast.LENGTH_SHORT).show()
            } else {
                val isSetpass = share.getBoolean("isSetPassword", false)
                if (!isSetpass) {//don't set pass => defaul password = 0000
                    if (pass != "0000") {
                        Toast.makeText(this, "Mật khẩu mặc định là 0000", Toast.LENGTH_SHORT).show()
                    } else {//default pass
                        setPass(edit)
                    }
                } else { //compare localPass
                    val localPass = share.getString("password", "")
                    if (pass == localPass) {
                        setPass(edit)
                    } else {
                        Toast.makeText(this, "Mật khẩu hiện tại chưa chính xác", Toast.LENGTH_SHORT)
                            .show()
                    }
                }

            }
        }

        binding.btnBackToLogin.setOnClickListener {
            startActivity(Intent(this, Login::class.java))
            finish()
        }
    }

    private fun setPass(edit: SharedPreferences.Editor) {
        val newPass = binding.confirmCode1.text.toString() + binding.confirmCode2.text.toString() +
                binding.confirmCode3.text.toString() + binding.confirmCode4.text.toString()
        if (newPass.length != 4) {
            Toast.makeText(
                this,
                "Vui lòng nhập mật khẩu gồm 4 số",
                Toast.LENGTH_SHORT
            ).show()
        } else {//set newPass
            edit.putString("password", newPass)
            edit.putBoolean("isSetPassword", true)
            edit.apply()
            Toast.makeText(this, "Mật khẩu hiện tại: $newPass", Toast.LENGTH_SHORT)
                .show()
            startActivity(Intent(this, Login::class.java))
            finish()
        }
    }

    private fun setUpPassCodeInput() {
        binding.confirmCode1.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                if (p0.toString().trim().isNotEmpty()) {
                    binding.confirmCode2.requestFocus()
                }
            }

            override fun afterTextChanged(p0: Editable?) {

            }
        })

        binding.confirmCode2.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                if (p0.toString().trim().isNotEmpty()) {
                    binding.confirmCode3.requestFocus()
                }
            }

            override fun afterTextChanged(p0: Editable?) {
            }

        })

        binding.confirmCode3.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                if (p0.toString().trim().isNotEmpty()) {
                    binding.confirmCode4.requestFocus()
                }
            }

            override fun afterTextChanged(p0: Editable?) {
            }

        })

        binding.confirmCode4.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                if (p0.toString().trim().isNotEmpty()) {
                    binding.input1.requestFocus()
                }
            }

            override fun afterTextChanged(p0: Editable?) {
            }

        })

        binding.input1.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                if (p0.toString().trim().isNotEmpty()) {
                    binding.input2.requestFocus()
                }
            }

            override fun afterTextChanged(p0: Editable?) {
            }

        })

        binding.input2.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                if (p0.toString().trim().isNotEmpty()) {
                    binding.input3.requestFocus()
                }
            }

            override fun afterTextChanged(p0: Editable?) {
            }

        })

        binding.input3.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                if (p0.toString().trim().isNotEmpty()) {
                    binding.input4.requestFocus()
                }
            }

            override fun afterTextChanged(p0: Editable?) {
            }

        })
    }
}