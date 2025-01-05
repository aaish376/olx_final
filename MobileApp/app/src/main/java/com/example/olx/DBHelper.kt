package com.example.olx


import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DBHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_NAME = "UserDB"
        private const val DATABASE_VERSION = 1

        private const val TABLE_USER = "User"
        private const val COLUMN_ID = "id"
        private const val COLUMN_EMAIL = "email"
        private const val COLUMN_IS_LOGGED_IN = "isLoggedIn"
    }

    override fun onCreate(db: SQLiteDatabase) {
        val createTable = """
            CREATE TABLE $TABLE_USER (
                $COLUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT,
                $COLUMN_EMAIL TEXT,
                $COLUMN_IS_LOGGED_IN INTEGER
            )
        """
        db.execSQL(createTable)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_USER")
        onCreate(db)
    }

    fun saveUser(id:Int, email: String, isLoggedIn: Boolean) {
        val db = writableDatabase
        db.delete(TABLE_USER, null, null) // Clear existing user data
        val values = ContentValues().apply {
            put(COLUMN_ID, id)
            put(COLUMN_EMAIL, email)
            put(COLUMN_IS_LOGGED_IN, if (isLoggedIn) 1 else 0)
        }
        db.insert(TABLE_USER, null, values)
        db.close()
    }

    fun getUser(): Pair<String?, Boolean> {
        val db = readableDatabase
        val cursor = db.query(
            TABLE_USER,
            arrayOf( COLUMN_EMAIL, COLUMN_IS_LOGGED_IN),
            null,
            null,
            null,
            null,
            null
        )

        var userEmail: String? = null
        var isLoggedIn = false

        if (cursor.moveToFirst()) {
            userEmail=cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_EMAIL))

            isLoggedIn = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_IS_LOGGED_IN)) == 1
        }
        cursor.close()
        db.close()
        return Pair( userEmail,isLoggedIn)
    }

    fun getUserId(): Int {
        val db = readableDatabase
        val cursor = db.query(
            TABLE_USER,
            arrayOf(COLUMN_ID), // Select only the ID column
            null,
            null,
            null,
            null,
            null
        )

        var userId: Int = 0
        if (cursor.moveToFirst()) {
            userId = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID))
        }

        cursor.close()
        db.close()
        return userId
    }


    fun clearUser() {
        val db = writableDatabase
        db.delete(TABLE_USER, null, null)
        db.close()
    }
}
