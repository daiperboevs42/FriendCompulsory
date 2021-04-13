package com.example.friend.GUI

import android.os.Bundle
import android.view.View
import android.widget.*
import com.example.friend.Data.BEFriend
import com.example.friend.Data.FriendRepositoryInDB
import androidx.lifecycle.Observer
import androidx.appcompat.app.AppCompatActivity
import com.example.friend.Data.observeOnce
import com.example.friend.R

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        FriendRepositoryInDB.initialize(this)

        //insertTestData()
        setupDataObserver()
    }

    private fun insertTestData() {
        val mRep = FriendRepositoryInDB.get()
        mRep.insert(BEFriend(0,"Rip", "01040705",true))
        mRep.insert(BEFriend(0,"Rap", "01010101", false))
        mRep.insert(BEFriend(0,"Rup", "10101010", false))
    }
    var cache: List<BEFriend>? = null;

    private fun setupDataObserver() {
        val lvNames = findViewById<ListView>(R.id.lvNames)
        val mRep = FriendRepositoryInDB.get()
        val nameObserver = Observer<List<BEFriend>>{ friends ->
            cache = friends;
            val asStrings = friends.map { f -> "${f.id}, ${f.name}"}
            val adapter: ListAdapter = ArrayAdapter(
                this,
                            android.R.layout.simple_list_item_1,
                            asStrings.toTypedArray()
            )
        lvNames.adapter = adapter
        }
        mRep.getAll().observe(this, nameObserver)
        lvNames.onItemClickListener = AdapterView.OnItemClickListener {_,_,pos,_ -> onClickFriend(pos)}
    }

    private fun onClickFriend(pos: Int) {
        val id = cache!![pos].id
        val friendObserver = Observer<BEFriend> { friend ->
            if  (friend != null)
            {
                Toast.makeText(this, "You have selected ${friend.name} ", Toast.LENGTH_SHORT).show()
            }
        }
        val mRep = FriendRepositoryInDB.get()
        mRep.getById(id).observeOnce(this, friendObserver)
    }

    fun onClickClear(view: View) {
        val mRep = FriendRepositoryInDB.get()
        mRep.clear()
    }



    /*private fun asListMap(src: Array<BEFriend>): List<Map<String, String?>> {

        return src.map{ person -> hashMapOf("name" to person.name, "phone" to person.phone) }
    }*/


    /*override fun onListItemClick(
            parent: ListView?,
            v: View?, position: Int, id: Long
    ) {
        val intent = Intent(this, DetailsActivity::class.java).also {
            it.putExtra("position", position)
            startActivity(it)
        }
    }*/
}