package com.example.friend.Data

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.sql.Date
import java.sql.Time
import java.time.LocalDate
import java.util.*

@Entity
data class BEFriend(
        @PrimaryKey(autoGenerate = true) var id:Int,
        var name: String,
        var phone: String,
        var email: String,
        var website: String,
       // var birthday: Long,
        var isFavorite: Boolean){
}