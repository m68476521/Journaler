package com.m68476521.mike.journaler.provider

import android.content.*
import android.database.Cursor
import android.database.SQLException
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteQueryBuilder
import android.net.Uri
import android.text.TextUtils
import com.m68476521.mike.journaler.database.DbHelper

class JournalerProvider : ContentProvider() {
    private val version = 1
    private val name = "journaler"
    private val db: SQLiteDatabase by lazy {
        DbHelper(name, version).writableDatabase
    }

    companion object {
        private val dataTypeNote = "note"
        private val dataTypeNotes = "notes"
        private val dataTypeTodo = "todo"
        private val dataTypeTodos = "todos"
        val AUTHORITY = "com.journaler.provider"
        val URL_NOTE = "content://$AUTHORITY/$dataTypeNote"
        val URL_TODO = "content://$AUTHORITY/$dataTypeTodo"
        val URL_NOTES = "content://$AUTHORITY/$dataTypeNotes"
        val URL_TODOS = "content://$AUTHORITY/$dataTypeTodos"
        private val matcher = UriMatcher(UriMatcher.NO_MATCH)
        private val NOTE_ALL = 1
        private val NOTE_ITEM = 2
        private val TODO_ALL = 3
        private val TODO_ITEM = 4
    }

    init {
        matcher.addURI(AUTHORITY, dataTypeNote, NOTE_ALL)
        matcher.addURI(AUTHORITY, "$dataTypeNotes/#", NOTE_ITEM)
        matcher.addURI(AUTHORITY, dataTypeTodo, TODO_ALL)
        matcher.addURI(AUTHORITY, "$dataTypeTodos/#", TODO_ITEM)
    }

    override fun onCreate() = true

    override fun insert(uri: Uri, values: ContentValues?): Uri? {
        uri?.let {
            values?.let {
                db.beginTransaction()
                val (url, table) = getParameters(uri)
                if (!TextUtils.isEmpty(table)) {
                    val inserted = db.insert(table, null, values)
                    val success = inserted > 0
                    if (success) {
                        db.setTransactionSuccessful()
                    }
                    db.endTransaction()
                    if (success) {
                        val resultIUrl = ContentUris.withAppendedId(Uri.parse(url), inserted)
                        context.contentResolver.notifyChange(resultIUrl, null)
                        return resultIUrl
                    }
                } else {
                    throw SQLException("Inserted failed, no table for uri : [ $uri ]")
                }
            }
        }
        throw SQLException("Insert failed : $uri ")
    }

    override fun update(uri: Uri, values: ContentValues?, where: String?, whereArgs: Array<String>?): Int {
        uri?.let {
            values?.let {
                db.beginTransaction()
                val (_, table) = getParameters(uri)
                if (!TextUtils.isEmpty(table)) {
                    val updated = db.update(table, values, where, whereArgs)
                    val success = updated > 0
                    if (success) {
                        db.setTransactionSuccessful()
                    }
                    db.endTransaction()
                    if (success) {
                        context.contentResolver.notifyChange(uri, null)
                        return updated
                    }
                } else {
                    throw SQLException("Updated failed, no table for uri: $uri")
                }
            }
        }
        throw SQLException("Updated failed, no table for uri: $uri ")
    }

    override fun delete(uri: Uri, selection: String?, selectionArgs: Array<String>?): Int {
        uri?.let {
            db.beginTransaction()
            val (_, table) = getParameters(uri)
            if (!TextUtils.isEmpty(table)) {
                val count = db.delete(table, selection, selectionArgs)
                val success = count > 0
                if (success) {
                    db.setTransactionSuccessful()
                }
                db.endTransaction()
                if (success) {
                    context.contentResolver.notifyChange(uri, null)
                    return count
                }
            } else {
                throw SQLException("Delete failed, no table for $uri")
            }
        }
        throw SQLException("Delete failed: $uri")
    }

    override fun query(uri: Uri, projection: Array<String>?, selection: String?, selectionArgs: Array<String>?,
                       sortOrder: String?): Cursor? {
        uri?.let {
            val stb = SQLiteQueryBuilder()
            val (_, table) = getParameters(uri)
            stb.tables = table
            stb.setProjectionMap(mutableMapOf<String, String>())
            val cursor = stb.query(db, projection, selection, selectionArgs, null, null, null)
            cursor.setNotificationUri(context.contentResolver, uri)
            return cursor
        }
        throw SQLException("Query failed: $uri ")

    }

    override fun getType(p0: Uri): String = when (matcher.match(p0)) {
        NOTE_ALL -> {
            "${ContentResolver.CURSOR_DIR_BASE_TYPE}/vnd.com.journaler.note.items"
        }
        NOTE_ITEM -> {
            "${ContentResolver.CURSOR_ITEM_BASE_TYPE}/vnd.com.journaler.note.item"
        }
        TODO_ALL -> {
            "${ContentResolver.CURSOR_DIR_BASE_TYPE}/vnd.com.journaler.todo.items"
        }
        TODO_ITEM -> {
            "${ContentResolver.CURSOR_ITEM_BASE_TYPE}/vnd.com.journaler.todo.item"
        }
        else -> throw IllegalAccessException("Unsupported Uri [ $p0 ]")
    }

    private fun getParameters(uri: Uri): Pair<String, String> {
        if (uri.toString().startsWith(URL_NOTE)) {
            return Pair(URL_NOTE, DbHelper.TABLE_NOTES)
        }
        if (uri.toString().startsWith(URL_NOTES)) {
            return Pair(URL_NOTES, DbHelper.TABLE_NOTES)
        }
        if (uri.toString().startsWith(URL_TODO)) {
            return Pair(URL_TODO, DbHelper.TABLE_TODOS)
        }
        if (uri.toString().startsWith(URL_TODOS)) {
            return Pair(URL_TODOS, DbHelper.TABLE_TODOS)
        }
        return Pair("", "")
    }
}