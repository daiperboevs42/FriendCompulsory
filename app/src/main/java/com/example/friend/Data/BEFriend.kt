package com.example.friend.Data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class BEFriend(
        @PrimaryKey(autoGenerate = true) var id:Int,
        var name: String,
        var phone: String,
        var isFavorite: Boolean){
}