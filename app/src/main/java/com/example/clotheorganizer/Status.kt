package com.example.clotheorganizer

class Status(
    var statusID: Int? = null,   // PK
    var status: String? = null,  // Clean or Laundry
    var lastUpdated: Long? = null // timestamp
)
