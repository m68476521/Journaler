package com.m68476521.mike.journaler.provider

import android.content.ContentProvider
import android.content.ContentResolver
import android.content.ContentValues
import android.content.UriMatcher
import android.database.Cursor
import android.net.Uri

class JournalerProvider : ContentProvider() {
    private val authority = "com.journaler.provider"
    private val dataTypeNote = "note"
    private val dataTypeTodo = "note"

    companion object {
        private val matcher = UriMatcher(UriMatcher.NO_MATCH)
        private val NOTE_ALL = 1
        private val NOTE_ITEM = 2
        private val TODO_ALL = 3
        private val TODO_ITEM = 4
    }

    init {
        matcher.addURI(authority, dataTypeNote, NOTE_ALL)
        matcher.addURI(authority, "$dataTypeNote/#", NOTE_ITEM)
        matcher.addURI(authority, dataTypeTodo, TODO_ALL)
        matcher.addURI(authority, "$dataTypeTodo/#", TODO_ITEM)
    }

    override fun onCreate(): Boolean = true

    override fun insert(p0: Uri, p1: ContentValues?): Uri? {
        throw NotImplementedError("Not implemented")
    }

    override fun update(p0: Uri, p1: ContentValues?, p2: String?, p3: Array<String>?): Int {
        throw NotImplementedError("Not implemented")
    }

    override fun delete(p0: Uri, p1: String?, p2: Array<String>?): Int {
        throw NotImplementedError("Not implemented")
    }

    override fun query(p0: Uri, p1: Array<String>?, p2: String?, p3: Array<String>?, p4: String?): Cursor? {
        throw NotImplementedError("Not implemented")
    }

    override fun getType(p0: Uri): String = when (matcher.match(p0)) {
        NOTE_ALL -> {
            "${ContentResolver.CURSOR_DIR_BASE_TYPE}/vnd.com.journaler.note.items"
        }
        NOTE_ITEM -> {
            "${ContentResolver.CURSOR_DIR_BASE_TYPE}/vnd.com.journaler.note.item"
        }
        TODO_ALL -> {
            "${ContentResolver.CURSOR_DIR_BASE_TYPE}/vnd.com.journaler.todo.items"
        }
        TODO_ITEM -> {
            "${ContentResolver.CURSOR_DIR_BASE_TYPE}/vnd.com.journaler.todo.item"
        }
        else -> throw IllegalAccessException("Unsupported Uri [ $p0 ]")
    }
}