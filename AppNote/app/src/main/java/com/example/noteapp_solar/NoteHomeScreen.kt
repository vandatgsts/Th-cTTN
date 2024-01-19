package com.example.noteapp_solar

import adapter.NoteAdapter
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.SearchView.OnQueryTextListener
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.noteapp_solar.databinding.ActivityNoteHomeScreenBinding
import java.io.*
import java.lang.Exception
import java.time.LocalDateTime

class NoteHomeScreen : AppCompatActivity() {
    var noteList: MutableList<Note> = ArrayList()
    var searchNoteList: MutableList<Note> = ArrayList()
    lateinit var noteDatabase: NoteDatabase
    lateinit var noteAdapter: NoteAdapter
    private val fileName = "notes.csv"

    private lateinit var binding: ActivityNoteHomeScreenBinding

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNoteHomeScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //initData
        //  initDataList()
        noteDatabase = NoteDatabase(this)
        fetchAllNoteFromDatabase()
        noteAdapter = NoteAdapter(noteList)
        noteAdapter.setOnClickItem {
            Toast.makeText(this, it.title, Toast.LENGTH_SHORT).show()
            val intent = Intent(this, DetailNoteActivity::class.java)
            intent.putExtra("title", it.title)
            intent.putExtra("timeNote", it.timeNote)
            intent.putExtra("content", it.content)
            startActivity(intent)
        }
//        searchNoteList = noteList
        binding.rcvNotes.adapter = noteAdapter
        binding.rcvNotes.layoutManager = LinearLayoutManager(this)
        binding.rcvNotes.addItemDecoration(DividerItemDecoration(this, DividerItemDecoration.VERTICAL))
        binding.searchBar.setOnQueryTextListener(object : OnQueryTextListener,
            androidx.appcompat.widget.SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                binding.searchBar.clearFocus()
                //click submit string search
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                if (newText != null) {
                    searchNoteList = searchNote(newText)
                    sortNoteList(searchNoteList)
                    noteAdapter.noteList = searchNoteList
                    noteAdapter.notifyDataSetChanged()
                }
                return true
            }
        })

        binding.btnAddNote.setOnClickListener {
            val intent = Intent(this, AddNoteActivity::class.java)
            val now = LocalDateTime.now()
            intent.putExtra("dayNote", now.dayOfMonth)
            intent.putExtra("monthNote", now.monthValue)
            intent.putExtra("yearNote", now.year)
            startActivity(intent)
        }
        val itemTouchHelper: ItemTouchHelper.SimpleCallback = object :
            ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                Toast.makeText(
                    applicationContext,
                    "Đã xóa ghi chú \" ${noteList[viewHolder.adapterPosition].title}\"",
                    Toast.LENGTH_LONG
                ).show()
                noteDatabase.deleteNote(noteList[viewHolder.adapterPosition])
                noteList.removeAt(viewHolder.adapterPosition)
                noteAdapter.notifyDataSetChanged()
            }
        }
        ItemTouchHelper(itemTouchHelper).attachToRecyclerView(binding.rcvNotes)
    }

    private fun fetchAllNoteFromDatabase() {
        noteList = noteDatabase.getAllNote()
        sortNoteList(noteList)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.option_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.read_from_file -> {//read note from file csv
                readFromFile()
                noteAdapter.notifyDataSetChanged()

            }
            R.id.write_to_file -> {//write noteList to file csv
                writeToFile()
            }
            R.id.view_calendar -> {//intent to Calendar
                startActivity(Intent(this, MainActivity::class.java))
            }
            R.id.delete_all_notes -> {
                deleteAllNotes()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun deleteAllNotes() {
        noteList.clear()
        noteAdapter.notifyDataSetChanged()
        //clear from database
        noteDatabase.deleteAllNote()
        Toast.makeText(this, "Đã xóa hết các ghi chú", Toast.LENGTH_SHORT).show()
    }

    private fun writeToFile() {
        var stringToFile = ""
        for (item in noteList) {
            stringToFile += item.title + ","
            stringToFile += item.timeNote + ","
            stringToFile += "\"" + item.content + "\"" + "\n"
        }
        try {
            //write stringToFile to fileName
            val writer = FileOutputStream(File(filesDir, fileName))
            writer.write(stringToFile.toByteArray())
            writer.close()
            Toast.makeText(this, "Ghi vào file thành công!", Toast.LENGTH_SHORT)
                .show()
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    private fun readFromFile() {
        noteList.clear()
        val input = FileInputStream(File(filesDir, fileName))
        val inputReader = InputStreamReader(input)
        val reader = BufferedReader(inputReader)
        var line = ""
        try {
            while (reader.readLine().also { line = it } != null) {
                //split by ","

                val tokens = line.split(",")
                //Read data
                val title = tokens[0]
                val timeNote = tokens[1]
                val content = line.substring(line.indexOf("\"")+1,line.length - 1)
                noteList.add(Note(title, timeNote, content))
            }
        } catch (e: Exception) {
        }
        sortNoteList(noteList)
        for (item in noteList) {
            noteDatabase.insertNote(item)
        }
    }

    private fun sortNoteList(list: MutableList<Note>) {
        for (i in 0 until list.size - 2) {
            for (j in i until list.size) {
                val timeNote1 = list[i].timeNote.split("/")
                val timeNote2 = list[j].timeNote.split("/")
                if (timeNote2[2] > timeNote1[2]) {//year
                    list[i] = list[j].also { list[j] = list[i] }
                } else if (timeNote2[1] > timeNote1[1]) {//month
                    list[i] = list[j].also { list[j] = list[i] }
                } else if (timeNote2[0] > timeNote1[0]) {//day
                    list[i] = list[j].also { list[j] = list[i] }
                }
            }
        }
    }

    private fun searchNote(s: String): MutableList<Note> =
        noteList.filter { note ->
            note.content.lowercase().contains(s.lowercase())
        } as MutableList<Note>
}