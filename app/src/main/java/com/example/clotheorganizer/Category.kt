package com.example.clotheorganizer

class Category(
    var categoryID: Int? = null,      // PK
    var categoryName: String? = null  // Tops, Accessories, etc
) {
    fun getCategoryID(): Int? = categoryID
    fun setCategoryID(id: Int) {
        categoryID = id
    }

    fun getCategoryName(): String? = categoryName
    fun setCategoryName(name: String) {
        categoryName = name
    }
}
