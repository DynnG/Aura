package com.example.clotheorganizer

class Outfit(
    var clothesID: Int? = null,
    var userID: Int? = null,
    var outfitID: Int? = null,
    var rand_top: Int? = null,
    var rand_bottom: Int? = null
) {
    fun getClothesID(): Int? = clothesID
    fun setClothesID(id: Int) {
        clothesID = id
    }

    fun getUserID(): Int? = userID
    fun setUserID(id: Int) {
        userID = id
    }

    fun getOutfitID(): Int? = outfitID
    fun setOutfitID(id: Int) {
        outfitID = id
    }

    fun getRandTop(): Int? = rand_top
    fun setRandTop(id: Int) {
        rand_top = id
    }

    fun getRandBottom(): Int? = rand_bottom
    fun setRandBottom(id: Int) {
        rand_bottom = id
    }
}
