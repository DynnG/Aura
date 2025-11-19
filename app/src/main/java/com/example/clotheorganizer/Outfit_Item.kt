package com.example.clotheorganizer

class Outfit_Item(
    var outfitID: Int?,        // PK, FK sa OUTFITS
    var clothesID: Int?,       // PK, FK sa CLOTHES
    var outfit_item_ID: Int?   // unique ID for this record
) {
    fun getOutfitID(): Int? = outfitID
    fun setOutfitID(id: Int) {
        outfitID = id
    }

    fun getClothesID(): Int? = clothesID
    fun setClothesID(id: Int) {
        clothesID = id
    }

    fun getOutfitItemID(): Int? = outfit_item_ID
    fun setOutfitItemID(id: Int) {
        outfit_item_ID = id
    }
}
