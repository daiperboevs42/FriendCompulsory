package com.example.friend.Data

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity
data class BEFriend(
        @PrimaryKey(autoGenerate = true) var id:Int,
        var name: String,
        var phone: String,
        var address: String,
        var email: String,
        var website: String,
        var photoPath: String?,
        var isFavorite: Boolean){
}