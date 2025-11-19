package com.example.clotheorganizer

class Status(
    var statusID: Int? = null,   // PK
    var status: String? = null,  // Clean or Laundry
    var lastUpdated: Long? = null // timestamp
) {
    fun getStatusID(): Int? = statusID
    fun setStatusID(id: Int) {
        statusID = id
    }

    fun getStatus(): String? = status
    fun setStatus(s: String) {
        status = s
    }

    fun getLastUpdated(): Long? = lastUpdated
    fun setLastUpdated(timestamp: Long) {
        lastUpdated = timestamp
    }
}
