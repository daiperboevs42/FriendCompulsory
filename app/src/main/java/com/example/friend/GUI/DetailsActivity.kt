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
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.friend.Data.FriendRepositoryInDB
import com.example.friend.R
import androidx.lifecycle.Observer
import com.example.friend.Data.BEFriend

class DetailsActivity : AppCompatActivity(){
    private var friendLoaded = false;
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
            val mRep = FriendRepositoryInDB.get()
            val friendName: TextView = findViewById(R.id.friendName)
            val friendNumber: TextView = findViewById(R.id.friendPhone)
            val nameObserver = Observer<BEFriend> { friend ->
                friendName.text = friend.name;
                friendNumber.text = friend.phone;

            }
            mRep.getById(position).observe(this, nameObserver)
        }
        else
        {
            //add textAreas and make sure they're blank here
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