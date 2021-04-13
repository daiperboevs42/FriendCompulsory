package com.example.friend.Data

import androidx.lifecycle.LiveData
import androidx.room.*
import com.google.android.material.appbar.AppBarLayout

@Dao
interface FriendDao {

    @Query("SELECT * from BEFriend order by id")
    fun getAll(): LiveData<List<BEFriend>>

    @Query("SELECT name from BEFriend order by name")
    fun getAllNames(): LiveData<List<String>>

    @Query("SELECT * from BEFriend where id = (:id)")
    fun getById(id: Int): LiveData<BEFriend>

    @Insert
    fun insert(f: BEFriend)

    @Update
    fun update(f: BEFriend)

    @Delete
    fun delete(f: BEFriend)

    @Query("DELETE from BEFriend")
    fun deleteAll()
}