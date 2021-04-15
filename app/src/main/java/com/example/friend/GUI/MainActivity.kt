package com.example.friend.GUI

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
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
        //creates and inflates the top menu
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu);
        return true;
    }
        //handles when the menu buttons are being pressed
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id: Int = item.getItemId()
        when (id) {
            R.id.openList -> {
                //go to main activity
                Toast.makeText(this, "Already on List Page", Toast.LENGTH_LONG).show()
                true
            }
            R.id.newFriend -> {
                //go to "create new" activity
                val intent = Intent(this, DetailsActivity::class.java).also {
                    startActivity(it)
                }
                true
            }
        }
        return super.onOptionsItemSelected(item)
    }
        //inserts test data
    //NOTE! Start with the method uncommented, but make sure you dont open the mainActivity again before removing it! it'll keep making the same data each time!
    private fun insertTestData() {
        val mRep = FriendRepositoryInDB.get()
        mRep.insert(BEFriend(0,"Rip", "01040705", "West Philadelphia","stranger@email.com","github.com/stranger",null, true))
        mRep.insert(BEFriend(0,"Rap", "01010101", "That Street 420","stranger@email.com","github.com/stranger", null,false))
        mRep.insert(BEFriend(0,"Rup", "10101010", "The Other Street 62","stranger@email.com","github.com/stranger", null,false))
    }

    //sets up the listView with all friends from the DB with Observers
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

    //opens detailView when pressing a specific friend
    private fun onClickFriend(pos: Int) {
        val id = cache!![pos].id
        val friendObserver = Observer<BEFriend> { friend ->
            if  (friend != null)
            {
                val intent = Intent(this, DetailsActivity::class.java).also {
                    it.putExtra("position", id)
                    startActivity(it)
                }
            }
        }
        val mRep = FriendRepositoryInDB.get()
        mRep.getById(id).observeOnce(this, friendObserver)
    }

    //clears the entire DB of Friend entities
    fun onClickClear(view: View) {
        val mRep = FriendRepositoryInDB.get()
        mRep.clear()
    }

}