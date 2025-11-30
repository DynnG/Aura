package com.example.clotheorganizer

class Outfit_Item(
    var outfitID: Int?,        // PK, FK sa OUTFITS
    var clothesID: Int?,       // PK, FK sa CLOTHES
    var outfit_item_ID: Int?   // unique ID for this record
)
