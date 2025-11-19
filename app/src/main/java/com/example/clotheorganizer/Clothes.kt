package com.example.clotheorganizer

class Clothes(
    var clothesID: Int? = null,
    var userID: Int? = null,
    var categoryID: Int? = null,
    var statusID: Int? = null,
    var typeID: Int? = null,
    var name: String? = null,
    var image_path: String? = null,
    var date_added: String? = null,
    var usage_count: Int? = null
) {
    fun getClothesID(): Int? = clothesID
    fun setClothesID(id: Int) {
        clothesID = id
    }

    fun getUserID(): Int? = userID
    fun setUserID(id: Int) {
        userID = id
    }

    fun getCategoryID(): Int? = categoryID
    fun setCategoryID(id: Int) {
        categoryID = id
    }

    fun getStatusID(): Int? = statusID
    fun setStatusID(id: Int) {
        statusID = id
    }

    fun getTypeID(): Int? = typeID
    fun setTypeID(id: Int) {
        typeID = id
    }

    fun getName(): String? = name
    fun setName(n: String) {
        name = n
    }

    fun getImagePath(): String? = image_path
    fun setImagePath(path: String) {
        image_path = path
    }

    fun getDateAdded(): String? = date_added
    fun setDateAdded(date: String) {
        date_added = date
    }

    fun getUsageCount(): Int? = usage_count
    fun setUsageCount(count: Int) {
        usage_count = count
    }
}
