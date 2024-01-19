package com.example.noteapp_solar

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.noteapp_solar.databinding.ActivityAddNoteBinding

class AddNoteActivity : AppCompatActivity() {
    private lateinit var sqliteHelper: NoteDatabase
    private lateinit var binding: ActivityAddNoteBinding

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddNoteBinding.inflate(layoutInflater)
        setContentView(binding.root)

        sqliteHelper = NoteDatabase(this)
        binding.btnAdd.setOnClickListener {
            addNote()
        }
        val day = intent.getIntExtra("dayNote", 1).toString()
        val month = intent.getIntExtra("monthNote", 1)
        val year = intent.getIntExtra("yearNote", 2021)
        binding.inputTimeNote.setText("$day/$month/$year")
    }

    private fun addNote() {
        val title = binding.inputTitleNote.text.toString()
        val timeNote = binding.inputTimeNote.text.toString()
        val contentNote = binding.inputContentNote.text.toString()
        if (title.isEmpty() || timeNote.isEmpty() || contentNote.isEmpty()) {
            Toast.makeText(
                this,
                "Bạn phải nhập đủ thông tin 3 trường", Toast.LENGTH_SHORT
            ).show()
        } else {
            val status = sqliteHelper.insertNote(Note(title, timeNote, contentNote))
            if (status > -1) {
                Toast.makeText(this, "Thêm thành công", Toast.LENGTH_SHORT).show()
                clearEditext()
            } else {
                Toast.makeText(this, "Không thêm được", Toast.LENGTH_SHORT).show()
            }
            val intent = Intent(this, NoteHomeScreen::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
            finish()
        }

    }

    private fun clearEditext() {
        binding.inputTitleNote.setText("")
        binding.inputTimeNote.setText("")
        binding.inputContentNote.setText("")
        binding.inputTitleNote.requestFocus()
    }
}