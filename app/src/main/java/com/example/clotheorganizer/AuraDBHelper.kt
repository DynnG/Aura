package com.example.clotheorganizer

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
                    "userName TEXT, " +
                    "password TEXT, " +
                    "email TEXT, " +
                    "fullname TEXT)"
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

    companion object {
        private const val DATABASE_NAME = "clotheorganizer.db"
        private const val DATABASE_VERSION = 1

        const val TABLE_USERS = "Users"
        const val TABLE_CATEGORY = "Category"
        const val TABLE_SUBCATEGORY = "SubCategory"
        const val TABLE_STATUS = "Status"
        const val TABLE_CLOTHES = "Clothes"
        const val TABLE_OUTFIT = "Outfit"
        const val TABLE_OUTFIT_ITEM = "Outfit_Item"
    }
}
