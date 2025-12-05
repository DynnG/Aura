package com.example.clotheorganizer

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class AuraDBHelper(context: Context) :
    SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    override fun onCreate(db: SQLiteDatabase) {

        // USERS TABLE
        db.execSQL(
            "CREATE TABLE IF NOT EXISTS $TABLE_USERS (" +
                    "userID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "userName TEXT NOT NULL, " +
                    "password TEXT NOT NULL, " +
                    "email TEXT NOT NULL, " +
                    "fullname TEXT NOT NULL)"
        )

        // CATEGORY TABLE
        db.execSQL(
            "CREATE TABLE IF NOT EXISTS $TABLE_CATEGORY (" +
                    "categoryID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "categoryName TEXT)"
        )

        // SUBCATEGORY TABLE
        db.execSQL(
            "CREATE TABLE IF NOT EXISTS $TABLE_SUBCATEGORY (" +
                    "typeID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "categoryID INTEGER, " +
                    "typeName TEXT, " +
                    "FOREIGN KEY(categoryID) REFERENCES $TABLE_CATEGORY(categoryID))"
        )

        // STATUS TABLE
        db.execSQL(
            "CREATE TABLE IF NOT EXISTS $TABLE_STATUS (" +
                    "statusID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "status TEXT, " +
                    "lastUpdated LONG)"
        )

        // CLOTHES TABLE
        db.execSQL(
            "CREATE TABLE IF NOT EXISTS $TABLE_CLOTHES (" +
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
            "CREATE TABLE IF NOT EXISTS $TABLE_OUTFIT (" +
                    "outfitID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "clothesID INTEGER, " +
                    "userID INTEGER, " +
                    "rand_top INTEGER, " +
                    "rand_bottom INTEGER, " +
                    "date LONG, " +
                    "FOREIGN KEY(userID) REFERENCES $TABLE_USERS(userID))"
        )

        // OUTFIT ITEM TABLE
        db.execSQL(
            "CREATE TABLE IF NOT EXISTS $TABLE_OUTFIT_ITEM (" +
                    "outfit_item_ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "outfitID INTEGER, " +
                    "clothesID INTEGER, " +
                    "FOREIGN KEY(outfitID) REFERENCES $TABLE_OUTFIT(outfitID), " +
                    "FOREIGN KEY(clothesID) REFERENCES $TABLE_CLOTHES(clothesID))"
        )

        // --- PRE-POPULATE DATA ---
        val cursor = db.rawQuery("SELECT count(*) FROM $TABLE_CATEGORY", null)
        cursor.moveToFirst()
        val count = cursor.getInt(0)
        cursor.close()
        
        if (count == 0) {
             // Categories
            db.execSQL("INSERT INTO $TABLE_CATEGORY (categoryName) VALUES ('Tops')") // 1
            db.execSQL("INSERT INTO $TABLE_CATEGORY (categoryName) VALUES ('Bottoms')") // 2
            db.execSQL("INSERT INTO $TABLE_CATEGORY (categoryName) VALUES ('Accessories')") // 3

            // SubCategories (Types)
            // Tops (1)
            db.execSQL("INSERT INTO $TABLE_SUBCATEGORY (categoryID, typeName) VALUES (1, 'T-shirt')")
            db.execSQL("INSERT INTO $TABLE_SUBCATEGORY (categoryID, typeName) VALUES (1, 'Polo')")
            db.execSQL("INSERT INTO $TABLE_SUBCATEGORY (categoryID, typeName) VALUES (1, 'Sleeveless')")

            // Bottoms (2)
            db.execSQL("INSERT INTO $TABLE_SUBCATEGORY (categoryID, typeName) VALUES (2, 'Trousers')")
            db.execSQL("INSERT INTO $TABLE_SUBCATEGORY (categoryID, typeName) VALUES (2, 'Jeans')")
            db.execSQL("INSERT INTO $TABLE_SUBCATEGORY (categoryID, typeName) VALUES (2, 'Sweatpants')")

            // Accessories (3)
            db.execSQL("INSERT INTO $TABLE_SUBCATEGORY (categoryID, typeName) VALUES (3, 'Bags')")
            db.execSQL("INSERT INTO $TABLE_SUBCATEGORY (categoryID, typeName) VALUES (3, 'Necklace')")
            db.execSQL("INSERT INTO $TABLE_SUBCATEGORY (categoryID, typeName) VALUES (3, 'Extra')")
            db.execSQL("INSERT INTO $TABLE_SUBCATEGORY (categoryID, typeName) VALUES (3, 'Bracelets')")
            
            // Status
            db.execSQL("INSERT INTO $TABLE_STATUS (status, lastUpdated) VALUES ('Clean', ${System.currentTimeMillis()})") // 1
            db.execSQL("INSERT INTO $TABLE_STATUS (status, lastUpdated) VALUES ('Laundry', ${System.currentTimeMillis()})") // 2
        }
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_OUTFIT_ITEM")
        db.execSQL("DROP TABLE IF EXISTS $TABLE_OUTFIT")
        db.execSQL("DROP TABLE IF EXISTS $TABLE_CLOTHES")
        db.execSQL("DROP TABLE IF EXISTS $TABLE_STATUS")
        db.execSQL("DROP TABLE IF EXISTS $TABLE_SUBCATEGORY")
        db.execSQL("DROP TABLE IF EXISTS $TABLE_CATEGORY")
        // db.execSQL("DROP TABLE IF EXISTS $TABLE_USERS") // User data preserved during upgrades
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

    fun getClothesForUser(userId: Int): List<Clothes> {
        val clothesList = ArrayList<Clothes>()
        val selectQuery = "SELECT * FROM $TABLE_CLOTHES WHERE userID = ? ORDER BY clothesID DESC"
        val db = this.readableDatabase
        val cursor = db.rawQuery(selectQuery, arrayOf(userId.toString()))
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
    
    // Kept for backward compatibility if needed, but getClothesForUser should be used
    fun getAllClothes(): List<Clothes> {
        return getClothesForUser(1) // Default to 1 or empty? Better to return all or fail.
    }

    fun getClothesByTypeAndUser(typeName: String, userId: Int): List<Clothes> {
        val clothesList = ArrayList<Clothes>()
        val selectQuery = "SELECT c.* FROM $TABLE_CLOTHES c INNER JOIN $TABLE_SUBCATEGORY s ON c.typeID = s.typeID WHERE s.typeName = ? AND c.userID = ? ORDER BY c.clothesID DESC"
        val db = this.readableDatabase
        val cursor = db.rawQuery(selectQuery, arrayOf(typeName, userId.toString()))
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
    
    fun getClothesByCategoryAndUser(categoryName: String, userId: Int): List<Clothes> {
        val clothesList = ArrayList<Clothes>()
        val selectQuery = "SELECT c.* FROM $TABLE_CLOTHES c INNER JOIN $TABLE_CATEGORY cat ON c.categoryID = cat.categoryID WHERE cat.categoryName = ? AND c.userID = ? ORDER BY c.clothesID DESC"
        val db = this.readableDatabase
        val cursor = db.rawQuery(selectQuery, arrayOf(categoryName, userId.toString()))
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
    
    fun getClothesByCategory(categoryName: String): List<Clothes> {
        return getClothesByCategoryAndUser(categoryName, 1)
    }
    
    fun getClothesByType(typeName: String): List<Clothes> {
         // Deprecated or default usage
         return getClothesByTypeAndUser(typeName, 1)
    }

    fun getTypeId(typeName: String): Int? {
        val db = this.readableDatabase
        val cursor = db.rawQuery("SELECT typeID FROM $TABLE_SUBCATEGORY WHERE typeName = ?", arrayOf(typeName))
        var typeId: Int? = null
        if (cursor.moveToFirst()) {
            typeId = cursor.getInt(cursor.getColumnIndexOrThrow("typeID"))
        }
        cursor.close()
        db.close()
        return typeId
    }
    
    fun getAllOutfitsForUser(userId: Int): List<Outfit> {
        val outfitMap = LinkedHashMap<String, Outfit>()
        val selectQuery = "SELECT * FROM $TABLE_OUTFIT WHERE userID = ? ORDER BY date DESC"
        val db = this.readableDatabase
        val cursor = db.rawQuery(selectQuery, arrayOf(userId.toString()))
        
        val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        
        if (cursor.moveToFirst()) {
            do {
                val id = cursor.getInt(cursor.getColumnIndexOrThrow("outfitID"))
                val dateLong = cursor.getLong(cursor.getColumnIndexOrThrow("date"))
                val dateString = sdf.format(Date(dateLong))
                
                val items = getOutfitItems(id)
                
                if (outfitMap.containsKey(dateString)) {
                    val existing = outfitMap[dateString]!!
                    val newItems = ArrayList(existing.items)
                    newItems.addAll(items)
                    existing.items = newItems
                } else {
                    val outfit = Outfit()
                    outfit.outfitID = id
                    outfit.date = dateLong
                    outfit.items = items
                    outfitMap[dateString] = outfit
                }
            } while (cursor.moveToNext())
        }
        cursor.close()
        return ArrayList(outfitMap.values)
    }
    
    fun getAllOutfits(): List<Outfit> {
        return getAllOutfitsForUser(1)
    }

    private fun getOutfitItems(outfitID: Int): List<Clothes> {
        val items = ArrayList<Clothes>()
        val query = "SELECT c.* FROM $TABLE_CLOTHES c INNER JOIN $TABLE_OUTFIT_ITEM oi ON c.clothesID = oi.clothesID WHERE oi.outfitID = ?"
        val db = this.readableDatabase
        val cursor = db.rawQuery(query, arrayOf(outfitID.toString()))
        if (cursor.moveToFirst()) {
             do {
                val cloth = Clothes()
                cloth.clothesID = cursor.getInt(cursor.getColumnIndexOrThrow("clothesID"))
                cloth.name = cursor.getString(cursor.getColumnIndexOrThrow("name"))
                cloth.image_path = cursor.getString(cursor.getColumnIndexOrThrow("image_path"))
                items.add(cloth)
            } while (cursor.moveToNext())
        }
        cursor.close()
        return items
    }

    fun logOutfit(items: List<Clothes>, userId: Int): Long {
        val db = this.writableDatabase
        val values = ContentValues()
        values.put("date", System.currentTimeMillis())
        values.put("userID", userId)
        
        val outfitID = db.insert(TABLE_OUTFIT, null, values)
        
        for (item in items) {
            val itemValues = ContentValues()
            itemValues.put("outfitID", outfitID)
            itemValues.put("clothesID", item.clothesID)
            db.insert(TABLE_OUTFIT_ITEM, null, itemValues)
            
            updateClothStatus(item.clothesID!!, 2)
        }
        db.close()
        return outfitID
    }
    
    // Overload for backward compatibility but defaults to 1?
    fun logOutfit(items: List<Clothes>): Long {
        return logOutfit(items, 1)
    }

    companion object {
        private const val DATABASE_NAME = "clotheorganizer.db"
        private const val DATABASE_VERSION = 7 // Incremented version

        const val TABLE_USERS = "Users"
        const val TABLE_CATEGORY = "Category"
        const val TABLE_SUBCATEGORY = "SubCategory"
        const val TABLE_STATUS = "Status"
        const val TABLE_CLOTHES = "Clothes"
        const val TABLE_OUTFIT = "Outfit"
        const val TABLE_OUTFIT_ITEM = "Outfit_Item"
    }
}
