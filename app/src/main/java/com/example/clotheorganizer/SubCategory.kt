package com.example.clotheorganizer

class SubCategory(
    var typeID: Int? = null,      // PK
    var categoryID: Int? = null,  // FK
    var typeName: String? = null  // Tshirts, Dress, etc
) {
    fun getTypeID(): Int? = typeID
    fun setTypeID(id: Int) {
        typeID = id
    }

    fun getCategoryID(): Int? = categoryID
    fun setCategoryID(id: Int) {
        categoryID = id
    }

    fun getTypeName(): String? = typeName
    fun setTypeName(name: String) {
        typeName = name
    }
}
