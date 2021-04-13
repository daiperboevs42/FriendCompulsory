package com.example.friend.Data

import android.annotation.SuppressLint
import android.content.Context
import androidx.lifecycle.LiveData
import androidx.room.Room
import java.lang.IllegalStateException
import java.util.concurrent.Executors

class FriendRepositoryInDB private constructor(private val context: Context) {

    private val database: FriendDatabase = Room.databaseBuilder(context.applicationContext,
    FriendDatabase::class.java,
    "friend-database").build()

    private val friendDao = database.friendDao()

    fun getAll(): LiveData<List<BEFriend>> = friendDao.getAll()

    fun getAllNames(): LiveData<List<String>> = friendDao.getAllNames()

    fun getById(id: Int) = friendDao.getById(id)

    private val executor = Executors.newSingleThreadExecutor()

    fun insert(f: BEFriend){
        executor.execute { friendDao.insert(f) }
    }

    fun udate(f: BEFriend){
        executor.execute{ friendDao.update(f)}
    }

    fun delete(f: BEFriend){
        executor.execute{ friendDao.delete(f)}
    }
    fun clear() {
        executor.execute{ friendDao.deleteAll()}
    }

    companion object{
        @SuppressLint("StaticFieldLeak")
        private var Instance: FriendRepositoryInDB? = null

        fun initialize(context: Context){
            if(Instance == null)
                Instance = FriendRepositoryInDB(context)
        }

        fun get(): FriendRepositoryInDB {
            if (Instance  != null) return Instance!!
            throw IllegalStateException("Friend repo not initialized")
        }
    }
}