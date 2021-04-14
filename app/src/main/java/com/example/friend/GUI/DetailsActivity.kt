package com.example.friend.GUI

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.location.LocationManager
import android.os.Build
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.CheckBox
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.friend.Data.FriendRepositoryInDB
import com.example.friend.R
import androidx.lifecycle.Observer
import com.example.friend.Data.BEFriend

class DetailsActivity : AppCompatActivity(){
    private var friendLoaded = false
    private var friendID = 0
    private val permissions = arrayOf(Manifest.permission.ACCESS_FINE_LOCATION,
    Manifest.permission.ACCESS_COARSE_LOCATION)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.details)
        val back: Button = findViewById(R.id.backDetails)
        back.setOnClickListener{
            finish()
        }

        val position = intent.getIntExtra("position", -1)
        if (position >= 0) {
            friendLoaded = true
            friendID = position
            val mRep = FriendRepositoryInDB.get()
            val friendName: TextView = findViewById(R.id.friendName)
            val friendNumber: TextView = findViewById(R.id.friendPhone)
            val friendEmail: TextView = findViewById(R.id.friendEmail)
            val friendWebsite: TextView = findViewById(R.id.friendWebsite)
            val friendBest: CheckBox = findViewById(R.id.bestFriend)
            val saveFriend: Button = findViewById(R.id.saveFriend)
            val deleteFriend: Button = findViewById(R.id.deleteFriend)
            val nameObserver = Observer<BEFriend> { friend ->
                friendName.text = friend.name;
                friendNumber.text = friend.phone;
                friendEmail.text = friend.email;
                friendWebsite.text = friend.website;
                friendBest.isChecked = friend.isFavorite;
            }
            saveFriend.text = "Update Friend"
            deleteFriend.visibility = View.VISIBLE
            mRep.getById(position).observe(this, nameObserver)
        }
    }

    private fun requestPermission(){
        if(!isPermissionGiven()){
            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
                requestPermissions(permissions, 1)
        }
    }

    private fun isPermissionGiven(): Boolean {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            return permissions.all {p -> checkSelfPermission(p) == PackageManager.PERMISSION_GRANTED}
        }
        return true
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu);
        return true;
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id: Int = item.getItemId()
        when (id) {
            R.id.openList -> {
                //go to main activity
                finish()
                true
            }
            R.id.newFriend -> {
                //go to "create new" activity
                Toast.makeText(this, "Already on Create Page", Toast.LENGTH_LONG).show()
                true
            }
        }
        return super.onOptionsItemSelected(item)
    }

     fun onClickSaveFriend(view: View) {
        val mRep = FriendRepositoryInDB.get()
        val friendName: TextView = findViewById(R.id.friendName)
        val friendNumber: TextView = findViewById(R.id.friendPhone)
        val friendEmail: TextView = findViewById(R.id.friendEmail)
        val friendWebsite: TextView = findViewById(R.id.friendWebsite)
        val friendBest: CheckBox = findViewById(R.id.bestFriend)
        val friend = BEFriend(friendID,
            friendName.text.toString(),
            friendNumber.text.toString(),
            friendEmail.text.toString(),
            friendWebsite.text.toString(),
            friendBest.isChecked,
            )

        if (friendLoaded){
            mRep.update(friend)
            Toast.makeText(this, "${friend.name} has been updated", Toast.LENGTH_SHORT).show()
            finish()
        }else{
            mRep.insert(friend)
            Toast.makeText(this, "${friend.name} has been created", Toast.LENGTH_SHORT).show()
            finish()
        }
    }

    fun onClickDeleteFriend(view: View){
        val mRep = FriendRepositoryInDB.get()
        val friendName: TextView = findViewById(R.id.friendName)
        val friendNumber: TextView = findViewById(R.id.friendPhone)
        val friendEmail: TextView = findViewById(R.id.friendEmail)
        val friendWebsite: TextView = findViewById(R.id.friendWebsite)
        val friendBest: CheckBox = findViewById(R.id.bestFriend)
        val friend = BEFriend(friendID,
            friendName.text.toString(),
            friendNumber.text.toString(),
            friendEmail.text.toString(),
            friendWebsite.text.toString(),
            friendBest.isChecked,
        )
        mRep.delete(friend)
        finish()
    }

    @SuppressLint("MissingPermission")
    fun onClickLocation(view: View){
        if (!isPermissionGiven()){
            requestPermission()
            return
        }

        val locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        val location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER)

        if(location != null){
            Toast.makeText(this, "location = ${location.latitude}, ${location.longitude}", Toast.LENGTH_SHORT).show()
        }
        else{
            Toast.makeText(this, "location null", Toast.LENGTH_SHORT).show()
        }
    }
}