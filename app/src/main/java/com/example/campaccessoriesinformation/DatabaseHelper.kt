package com.example.campaccessoriesinformation

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DatabaseHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_NAME = "IteamsDB"
        private const val DATABASE_VERSION = 2 // Increment version for database upgrade
        private const val TABLE_ITEAMS = "Iteam"
        private const val COLUMN_ID = "id"
        private const val COLUMN_NAME = "name"
        private const val COLUMN_TYPE = "type"
        private const val COLUMN_DESCRIPTION = "description"
        private const val COLUMN_PRICE_PER_DAY = "pricePerDay"
        private const val COLUMN_IMAGE_PATH = "selectedImagePath"
        // Add these at the beginning of DatabaseHelper class
        private val TABLE_USERS = "users"
        private val COLUMN_USER_ID = "user_id"
        private val COLUMN_USER_NAME = "user_name"
        private val COLUMN_USER_MOBILE = "user_mobile"
        private val COLUMN_USER_PASSWORD = "user_password"
        private val COLUMN_USER_ROLE = "user_role"

    }

    override fun onCreate(db: SQLiteDatabase?) {
        val CREATE_ITEAM_TABLE = ("CREATE TABLE $TABLE_ITEAMS ("
                + COLUMN_ID + " INTEGER PRIMARY KEY,"
                + COLUMN_NAME + " TEXT,"
                + COLUMN_TYPE + " TEXT,"
                + COLUMN_DESCRIPTION + " TEXT,"
                + COLUMN_PRICE_PER_DAY + " REAL,"
                + COLUMN_IMAGE_PATH + " TEXT" + ")")
        db?.execSQL(CREATE_ITEAM_TABLE)

        val CREATE_USER_TABLE = ("CREATE TABLE $TABLE_USERS ("
                + COLUMN_USER_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + COLUMN_USER_NAME + " TEXT,"
                + COLUMN_USER_MOBILE + " TEXT UNIQUE,"
                + COLUMN_USER_PASSWORD + " TEXT,"
                + COLUMN_USER_ROLE + " TEXT)")
        db?.execSQL(CREATE_USER_TABLE)

        // Insert admin user
        val insertAdmin = "INSERT INTO $TABLE_USERS ($COLUMN_USER_NAME, $COLUMN_USER_MOBILE, $COLUMN_USER_PASSWORD, $COLUMN_USER_ROLE) VALUES ('Harshana', '0779084597', '12345', 'admin')"
        db?.execSQL(insertAdmin)
    }


    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        if (oldVersion < 2) {
            db?.execSQL("ALTER TABLE $TABLE_ITEAMS ADD COLUMN $COLUMN_DESCRIPTION TEXT")
            db?.execSQL("ALTER TABLE $TABLE_ITEAMS ADD COLUMN $COLUMN_PRICE_PER_DAY REAL")
        }
    }

    fun addIteam(name: String, type: String, description: String, pricePerDay: Double,selectedImagePath: String) {
        val db = this.writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_NAME, name)
            put(COLUMN_TYPE, type)
            put(COLUMN_DESCRIPTION, description)
            put(COLUMN_PRICE_PER_DAY, pricePerDay)
            put(COLUMN_IMAGE_PATH, selectedImagePath)
        }
        db.insert(TABLE_ITEAMS, null, values)
        db.close()
    }

    fun updateIteam(id: Int, name: String, type: String, description: String, pricePerDay: Double,imagePath: String) {
        val db = this.writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_NAME, name)
            put(COLUMN_TYPE, type)
            put(COLUMN_DESCRIPTION, description)
            put(COLUMN_PRICE_PER_DAY, pricePerDay)
            put(COLUMN_IMAGE_PATH, imagePath)
        }
        db.update(TABLE_ITEAMS, values, "$COLUMN_ID = ?", arrayOf(id.toString()))
        db.close()
    }

    fun deleteIteam(id: Int) {
        val db = this.writableDatabase
        db.delete(TABLE_ITEAMS, "$COLUMN_ID = ?", arrayOf(id.toString()))
        db.close()
    }

    fun getAllIteam(): List<Iteam> {
        val iteamList = ArrayList<Iteam>()
        val selectQuery = "SELECT * FROM $TABLE_ITEAMS"
        val db = this.readableDatabase
        val cursor = db.rawQuery(selectQuery, null)
        if (cursor.moveToFirst()) {
            do {
                val iteam = Iteam(
                    cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID)),
                    cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_NAME)),
                    cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_TYPE)),
                    cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_DESCRIPTION)),
                    cursor.getDouble(cursor.getColumnIndexOrThrow(COLUMN_PRICE_PER_DAY)),
                    cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_IMAGE_PATH))
                )
                iteamList.add(iteam)
            } while (cursor.moveToNext())
        }
        cursor.close()
        db.close()
        return iteamList
    }
    fun insertUser(name: String, mobile: String, password: String, role: String): Long {
        val db = this.writableDatabase
        val values = ContentValues()
        values.put(COLUMN_USER_NAME, name)
        values.put(COLUMN_USER_MOBILE, mobile)
        values.put(COLUMN_USER_PASSWORD, password)
        values.put(COLUMN_USER_ROLE, role)
        return db.insert(TABLE_USERS, null, values)
    }

    fun getUser(mobile: String, password: String): User? {
        val db = this.readableDatabase
        val cursor = db.query(
            TABLE_USERS,
            arrayOf(COLUMN_USER_ID, COLUMN_USER_NAME, COLUMN_USER_MOBILE, COLUMN_USER_PASSWORD, COLUMN_USER_ROLE),
            "$COLUMN_USER_MOBILE = ? AND $COLUMN_USER_PASSWORD = ?",
            arrayOf(mobile, password),
            null, null, null
        )
        return if (cursor != null && cursor.moveToFirst()) {
            val user = User(
                cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_USER_ID)),
                cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_USER_NAME)),
                cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_USER_MOBILE)),
                cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_USER_PASSWORD)),
                cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_USER_ROLE))
            )
            cursor.close()
            user
        } else {
            cursor?.close()
            null
        }
    }

}
