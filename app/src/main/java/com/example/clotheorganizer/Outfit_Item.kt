package com.example.clotheorganizer

class Outfit_Item(
    var outfitID: Int? = null,        // PK, FK sa OUTFITS
    var clothesID: Int? = null,       // PK, FK sa CLOTHES
    var outfit_item_ID: Int? = null   // unique ID for this record
)
