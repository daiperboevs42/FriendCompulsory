package com.example.friend.GUI

import android.app.ListActivity
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.*
import com.example.friend.Data.BEFriend
import com.example.friend.Data.FriendRepositoryInDB
import com.example.friend.Data.Friends
import com.example.friend.R

class MainActivity : ListActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        FriendRepositoryInDB.initialize(this)

        listAdapter = SimpleAdapter(
                this,
                asListMap(Friends().getAll()),
                R.layout.cell,
                arrayOf("name", "phone"),
                intArrayOf(R.id.name, R.id.phone)
        )
    }

    /* Input: an array of friend objects
       Return: a list of hashmaps, where each hashmap represent a friend object from input
     */
    private fun asListMap(src: Array<BEFriend>): List<Map<String, String?>> {

        return src.map{ person -> hashMapOf("name" to person.name, "phone" to person.phone) }
    }


    override fun onListItemClick(
            parent: ListView?,
            v: View?, position: Int, id: Long
    ) {
        val intent = Intent(this, DetailsActivity::class.java).also {
            it.putExtra("position", position)
            startActivity(it)
        }
    }
}