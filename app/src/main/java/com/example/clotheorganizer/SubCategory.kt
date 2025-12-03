package com.example.clotheorganizer

class SubCategory(
    var typeID: Int? = null,      // PK
    var categoryID: Int? = null,  // FK
    var typeName: String? = null  // Tshirts, Dress, etc
)