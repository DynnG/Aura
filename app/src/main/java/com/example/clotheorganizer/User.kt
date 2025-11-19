package com.example.clotheorganizer

class User(
    var userID: Int? = null,
    var userName: String? = null,
    var password: String? = null,
    var email: String? = null,
    var fullname: String? = null
) {
    fun getUserID(): Int? = userID
    fun setUserID(id: Int) {
        userID = id
    }

    fun getUserName(): String? = userName
    fun setUserName(name: String) {
        userName = name
    }

    fun getPassword(): String? = password
    fun setPassword(pass: String) {
        password = pass
    }

    fun getEmail(): String? = email
    fun setEmail(mail: String) {
        email = mail
    }

    fun getFullname(): String? = fullname
    fun setFullname(name: String) {
        fullname = name
    }
}
