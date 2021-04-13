package com.example.friend.Data

class Friends {

    val mFriends = arrayOf<BEFriend>(
    )

    fun getAll(): Array<BEFriend> = mFriends

    fun getFriend(index: Int): BEFriend{
        return mFriends[index]
    }


    fun getAllNames(): Array<String>  =  mFriends.map { aFriend -> aFriend.name }.toTypedArray()

}