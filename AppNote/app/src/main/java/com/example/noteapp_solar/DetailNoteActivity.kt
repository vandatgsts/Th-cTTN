package com.example.noteapp_solar

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.noteapp_solar.databinding.ActivityNoteDetailBinding

class DetailNoteActivity : AppCompatActivity() {
    lateinit var binding: ActivityNoteDetailBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityNoteDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val oldTitle = intent.getStringExtra("title") ?: ""
        val oldTimeNote = intent.getStringExtra("timeNote") ?: ""
        val oldContent = intent.getStringExtra("content") ?: ""
        val oldNote = Note(oldTitle, oldTimeNote, oldContent)
        binding.inputDetailTitle.setText(oldTitle)
        binding.inputDetailTimeNote.setText(oldTimeNote)
        binding.inputDetailContentNote.setText(oldContent)
        binding.btnUpdateNote.setOnClickListener {
            val newTitle = binding.inputDetailTitle.text.toString()
            val newTimeNote = binding.inputDetailTimeNote.text.toString()
            val newContent = binding.inputDetailContentNote.text.toString()
            val newNote = Note(newTitle, newTimeNote, newContent)

            NoteDatabase(this).updateNote(oldNote, newNote)

            startActivity(Intent(this, NoteHomeScreen::class.java))
            finish()
        }

        binding.btnDeleteNote.setOnClickListener {
            NoteDatabase(this).deleteNote(oldNote)

            startActivity(Intent(this, NoteHomeScreen::class.java))
            finish()
        }
    }
}