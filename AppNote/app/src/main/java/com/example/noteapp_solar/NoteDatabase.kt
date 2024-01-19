package com.example.noteapp_solar

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import java.lang.Exception

class NoteDatabase(var context: Context) :
    SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_NAME = "note.db"
        private const val DATABASE_VERSION = 1
        private const val TBL_NOTE = "tbl_note"
        private const val TITLE = "title"
        private const val TIME_NOTE = "time_note"
        private const val CONTENT_NOTE = "content_note"
    }

    override fun onCreate(db: SQLiteDatabase?) {
        val createTblNote = ("Create table " + TBL_NOTE + "(" + TITLE + " TEXT,"
                + TIME_NOTE + " TEXT,"
                + CONTENT_NOTE + " TEXT)")
        db?.execSQL(createTblNote)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db!!.execSQL("DROP TABLE IF EXISTS $TBL_NOTE")
        onCreate(db)
    }

    fun deleteAllNote() {
        val db = this.writableDatabase
        db.execSQL("delete from $TBL_NOTE")
        db.close()
    }

    fun insertNote(note: Note): Long {
        val db = this.writableDatabase
        val contentValue = ContentValues()
        contentValue.put(TITLE, note.title)
        contentValue.put(TIME_NOTE, note.timeNote)
        contentValue.put(CONTENT_NOTE, note.content)
        val success = db.insert(TBL_NOTE, null, contentValue)
        db.close()
        return success
    }

    fun updateNote(note: Note, newNote: Note): Int {
        val db = this.writableDatabase
        val contentValue = ContentValues()
        contentValue.put(TITLE, newNote.title)
        contentValue.put(TIME_NOTE, newNote.timeNote)
        contentValue.put(CONTENT_NOTE, newNote.content)
        val success = db.update(
            TBL_NOTE, contentValue, "$TITLE = ? and $TIME_NOTE = ? and $CONTENT_NOTE = ?",
            arrayOf(note.title, note.timeNote, note.content)
        )
        db.close()
        return success
    }

    fun deleteNote(note: Note): Int {
        val db = this.writableDatabase
        val success = db.delete(
            TBL_NOTE, "$TITLE = ? and $TIME_NOTE = ? and $CONTENT_NOTE = ?",
            arrayOf(note.title, note.timeNote, note.content)
        )
        db.close()
        return success
    }


    @SuppressLint("Recycle", "Range")
    fun getAllNote(): MutableList<Note> {
        val noteList: MutableList<Note> = ArrayList()
        val selectQuery = "select * from $TBL_NOTE"
        val db = this.readableDatabase
        val cursor: Cursor?
        try {
            cursor = db.rawQuery(selectQuery, null)

        } catch (e: Exception) {
            e.printStackTrace()
            return ArrayList()
        }
        var title: String
        var timeNote: String
        var contentNote: String
        if (cursor.moveToFirst()) {
            do {
                title = cursor.getString(cursor.getColumnIndex(TITLE))
                timeNote = cursor.getString(cursor.getColumnIndex(TIME_NOTE))
                contentNote = cursor.getString(cursor.getColumnIndex(CONTENT_NOTE))
                noteList.add(Note(title, timeNote, contentNote))
            } while (cursor.moveToNext())
        }
        return noteList
    }
}

