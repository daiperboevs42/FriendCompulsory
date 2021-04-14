package com.example.friend.GUI

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.LocationManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.*
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
            val callFriend: ImageButton = findViewById(R.id.callButton)
            val messageFriend: ImageButton = findViewById(R.id.messageButton)
            val emailButton: ImageButton = findViewById(R.id.emailButton)
            val websiteButton: ImageButton = findViewById(R.id.websiteButton)
            val nameObserver = Observer<BEFriend> { friend ->
                friendName.text = friend.name;
                friendNumber.text = friend.phone;
                friendEmail.text = friend.email;
                friendWebsite.text = friend.website;
                friendBest.isChecked = friend.isFavorite;
            }
            saveFriend.text = "Update Friend"
            deleteFriend.visibility = View.VISIBLE
            callFriend.visibility = View.VISIBLE
            messageFriend.visibility = View.VISIBLE
            emailButton.visibility = View.VISIBLE
            websiteButton.visibility = View.VISIBLE
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

    fun onClickCall(view: View){
        val friendNumber: TextView = findViewById(R.id.friendPhone)
        val intent = Intent(Intent.ACTION_DIAL)
        intent.data = Uri.parse("tel:${friendNumber.text}")
        startActivity(intent)
    }

    fun onClickEmail(view: View) {
        val friendEmail: TextView = findViewById(R.id.friendEmail)
        val emailIntent = Intent(Intent.ACTION_SEND)
        emailIntent.type="plain/text"
        val receivers = arrayOf("${friendEmail.text}")
        emailIntent.putExtra(Intent.EXTRA_EMAIL, receivers)
        emailIntent.putExtra(Intent.EXTRA_TEXT,
            "Hey, this email is sent to you by the app I just created")
        startActivity(emailIntent)
    }

    fun onClickWebsite(view: View){
        val friendWebsite: TextView = findViewById(R.id.friendWebsite)
        var url="${friendWebsite.text}"
        if (!url.startsWith("http://") && !url.startsWith("https://"))
            url = "http://" + url;
        val i = Intent(Intent.ACTION_VIEW)
        i.data =Uri.parse(url)
        startActivity(i)
    }

    fun onClickMessage(view: View){
        val friendNumber: TextView = findViewById(R.id.friendPhone)
        val sendIntent = Intent(Intent.ACTION_VIEW)
        sendIntent.data=Uri.parse("sms:${friendNumber.text}")
        sendIntent.putExtra("sms_body", "This is magic!")
        startActivity(sendIntent)
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