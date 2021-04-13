package com.example.friend.Data

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [BEFriend::class], version=1)
abstract class FriendDatabase : RoomDatabase() {

    abstract fun friendDao(): FriendDao
}