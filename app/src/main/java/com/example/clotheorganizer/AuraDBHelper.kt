package com.example.clotheorganizer

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class AuraDBHelper(context: Context) :
    SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    override fun onCreate(db: SQLiteDatabase) {

        // USERS TABLE
        db.execSQL(
            "CREATE TABLE $TABLE_USERS (" +
                    "userID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "userName TEXT NOT NULL, " +
                    "password TEXT NOT NULL, " +
                    "email TEXT NOT NULL, " +
                    "fullname TEXT NOT NULL)"
        )

        // CATEGORY TABLE
        db.execSQL(
            "CREATE TABLE $TABLE_CATEGORY (" +
                    "categoryID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "categoryName TEXT)"
        )

        // SUBCATEGORY TABLE
        db.execSQL(
            "CREATE TABLE $TABLE_SUBCATEGORY (" +
                    "typeID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "categoryID INTEGER, " +
                    "typeName TEXT, " +
                    "FOREIGN KEY(categoryID) REFERENCES $TABLE_CATEGORY(categoryID))"
        )

        // STATUS TABLE
        db.execSQL(
            "CREATE TABLE $TABLE_STATUS (" +
                    "statusID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "status TEXT, " +
                    "lastUpdated LONG)"
        )

        // CLOTHES TABLE
        db.execSQL(
            "CREATE TABLE $TABLE_CLOTHES (" +
                    "clothesID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "userID INTEGER, " +
                    "categoryID INTEGER, " +
                    "statusID INTEGER, " +
                    "typeID INTEGER, " +
                    "name TEXT, " +
                    "image_path TEXT, " +
                    "date_added TEXT, " +
                    "usage_count INTEGER, " +
                    "FOREIGN KEY(userID) REFERENCES $TABLE_USERS(userID), " +
                    "FOREIGN KEY(categoryID) REFERENCES $TABLE_CATEGORY(categoryID), " +
                    "FOREIGN KEY(statusID) REFERENCES $TABLE_STATUS(statusID), " +
                    "FOREIGN KEY(typeID) REFERENCES $TABLE_SUBCATEGORY(typeID))"
        )

        // OUTFIT TABLE
        db.execSQL(
            "CREATE TABLE $TABLE_OUTFIT (" +
                    "outfitID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "clothesID INTEGER, " +
                    "userID INTEGER, " +
                    "rand_top INTEGER, " +
                    "rand_bottom INTEGER, " +
                    "FOREIGN KEY(userID) REFERENCES $TABLE_USERS(userID))"
        )

        // OUTFIT ITEM TABLE
        db.execSQL(
            "CREATE TABLE $TABLE_OUTFIT_ITEM (" +
                    "outfit_item_ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "outfitID INTEGER, " +
                    "clothesID INTEGER, " +
                    "FOREIGN KEY(outfitID) REFERENCES $TABLE_OUTFIT(outfitID), " +
                    "FOREIGN KEY(clothesID) REFERENCES $TABLE_CLOTHES(clothesID))"
        )
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_OUTFIT_ITEM")
        db.execSQL("DROP TABLE IF EXISTS $TABLE_OUTFIT")
        db.execSQL("DROP TABLE IF EXISTS $TABLE_CLOTHES")
        db.execSQL("DROP TABLE IF EXISTS $TABLE_STATUS")
        db.execSQL("DROP TABLE IF EXISTS $TABLE_SUBCATEGORY")
        db.execSQL("DROP TABLE IF EXISTS $TABLE_CATEGORY")
        db.execSQL("DROP TABLE IF EXISTS $TABLE_USERS")
        onCreate(db)
    }

    fun addUser(user: User): Boolean {
        val db = this.writableDatabase
        val values = ContentValues()
        values.put("userName", user.userName)
        values.put("password", user.password)
        values.put("email", user.email)
        values.put("fullname", user.fullname)

        val result = db.insert(TABLE_USERS, null, values)
        db.close()
        return result != -1L
    }

    fun checkUser(username: String, pass: String): User? {
        val db = this.readableDatabase
        val cursor = db.rawQuery("SELECT * FROM $TABLE_USERS WHERE userName = ? AND password = ?", arrayOf(username, pass))
        var user: User? = null
        if (cursor.moveToFirst()) {
            val userId = cursor.getInt(cursor.getColumnIndexOrThrow("userID"))
            val uName = cursor.getString(cursor.getColumnIndexOrThrow("userName"))
            val pWord = cursor.getString(cursor.getColumnIndexOrThrow("password"))
            val email = cursor.getString(cursor.getColumnIndexOrThrow("email"))
            val fullname = cursor.getString(cursor.getColumnIndexOrThrow("fullname"))
            user = User(userId, uName, pWord, email, fullname)
        }
        cursor.close()
        db.close()
        return user
    }

    fun addCloth(cloth: Clothes): Long {
        val db = this.writableDatabase
        val values = ContentValues()
        values.put("userID", cloth.userID)
        values.put("categoryID", cloth.categoryID)
        values.put("statusID", cloth.statusID)
        values.put("typeID", cloth.typeID)
        values.put("name", cloth.name)
        values.put("image_path", cloth.image_path)
        values.put("date_added", cloth.date_added)
        values.put("usage_count", cloth.usage_count)

        val id = db.insert(TABLE_CLOTHES, null, values)
        db.close()
        return id
    }

    fun updateClothStatus(clothId: Int, statusId: Int): Int {
        val db = this.writableDatabase
        val values = ContentValues()
        values.put("statusID", statusId)
        val result = db.update(TABLE_CLOTHES, values, "clothesID = ?", arrayOf(clothId.toString()))
        db.close()
        return result
    }

    fun getAllClothes(): List<Clothes> {
        val clothesList = ArrayList<Clothes>()
        val selectQuery = "SELECT * FROM $TABLE_CLOTHES ORDER BY clothesID DESC"
        val db = this.readableDatabase
        val cursor = db.rawQuery(selectQuery, null)
        if (cursor.moveToFirst()) {
            do {
                val cloth = Clothes()
                cloth.clothesID = cursor.getInt(cursor.getColumnIndexOrThrow("clothesID"))
                cloth.userID = cursor.getInt(cursor.getColumnIndexOrThrow("userID"))
                cloth.categoryID = cursor.getInt(cursor.getColumnIndexOrThrow("categoryID"))
                cloth.statusID = cursor.getInt(cursor.getColumnIndexOrThrow("statusID"))
                cloth.typeID = cursor.getInt(cursor.getColumnIndexOrThrow("typeID"))
                cloth.name = cursor.getString(cursor.getColumnIndexOrThrow("name"))
                cloth.image_path = cursor.getString(cursor.getColumnIndexOrThrow("image_path"))
                cloth.date_added = cursor.getString(cursor.getColumnIndexOrThrow("date_added"))
                cloth.usage_count = cursor.getInt(cursor.getColumnIndexOrThrow("usage_count"))
                clothesList.add(cloth)
            } while (cursor.moveToNext())
        }
        cursor.close()
        return clothesList
    }

    companion object {
        private const val DATABASE_NAME = "clotheorganizer.db"
        private const val DATABASE_VERSION = 2

        const val TABLE_USERS = "Users"
        const val TABLE_CATEGORY = "Category"
        const val TABLE_SUBCATEGORY = "SubCategory"
        const val TABLE_STATUS = "Status"
        const val TABLE_CLOTHES = "Clothes"
        const val TABLE_OUTFIT = "Outfit"
        const val TABLE_OUTFIT_ITEM = "Outfit_Item"
    }
}
