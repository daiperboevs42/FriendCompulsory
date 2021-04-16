package com.example.friend.GUI

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.location.LocationManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.FileProvider
import com.example.friend.Data.FriendRepositoryInDB
import com.example.friend.R
import androidx.lifecycle.Observer
import com.example.friend.Data.BEFriend
import java.io.File
import android.Manifest.permission.CAMERA
import android.Manifest.permission.WRITE_EXTERNAL_STORAGE
import android.Manifest.permission_group.CAMERA
import androidx.annotation.RequiresApi
import java.sql.Time
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.*

class DetailsActivity : AppCompatActivity(){
    private var friendLoaded = false
    private var friendID = 0
    val TAG = "xyz"
    private val PERMISSION_REQUEST_CODE = 1
    val CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE_BY_FILE = 101
    var mFile: File? = null

    private val permissions = arrayOf(Manifest.permission.ACCESS_FINE_LOCATION,
    Manifest.permission.ACCESS_COARSE_LOCATION)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.details)
        checkPermissions()
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

     @RequiresApi(Build.VERSION_CODES.O)
     fun onClickSaveFriend(view: View) {
        val mRep = FriendRepositoryInDB.get()
        val friendName: TextView = findViewById(R.id.friendName)
        val friendNumber: TextView = findViewById(R.id.friendPhone)
        val friendEmail: TextView = findViewById(R.id.friendEmail)
        val friendWebsite: TextView = findViewById(R.id.friendWebsite)
        val friendBest: CheckBox = findViewById(R.id.bestFriend)
        // val date = LocalDate.parse(findViewById(R.id.birthday))
        // val zoneId = ZoneId.systemDefault() // or: ZoneId.of("Europe/Oslo");
        // val epoch = date.atStartOfDay(zoneId).toEpochSecond()
        val friend = BEFriend(friendID,
            friendName.text.toString(),
            friendNumber.text.toString(),
            friendEmail.text.toString(),
            friendWebsite.text.toString(),
             //   epoch,
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

    @RequiresApi(Build.VERSION_CODES.O)
    fun onClickDeleteFriend(view: View){
        val mRep = FriendRepositoryInDB.get()
        val friendName: TextView = findViewById(R.id.friendName)
        val friendNumber: TextView = findViewById(R.id.friendPhone)
        val friendEmail: TextView = findViewById(R.id.friendEmail)
        val friendWebsite: TextView = findViewById(R.id.friendWebsite)
        val friendBest: CheckBox = findViewById(R.id.bestFriend)
       // val date = LocalDate.parse(findViewById(R.id.birthday))
      //  val zoneId = ZoneId.systemDefault() // or: ZoneId.of("Europe/Oslo");
      //  val epoch = date.atStartOfDay(zoneId).toEpochSecond()
        val friend = BEFriend(friendID,
            friendName.text.toString(),
            friendNumber.text.toString(),
            friendEmail.text.toString(),
            friendWebsite.text.toString(),
              //  epoch,
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
    //Take a Photo save as file
    fun onTakeAsFile(view: View) {
        mFile = getOutputMediaFile("Camera01") // create a file to save the image

        if (mFile == null) {
            Toast.makeText(this, "Could not create file...", Toast.LENGTH_LONG).show()
            return
        }
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)

        val Friend = "com.example.friend"
        intent.putExtra(MediaStore.EXTRA_OUTPUT, FileProvider.getUriForFile(
                this,
                "${Friend}.provider",  //use your app signature + ".provider"
                mFile!!))

        if (intent.resolveActivity(packageManager) != null) {
            startActivityForResult(intent, CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE_BY_FILE)
        } else Log.d(TAG, "camera app could NOT be started")

    }
    //Checks if the app has the required permissions, and prompts the user with the ones missing.
    private fun checkPermissions() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) return
        val permissions = mutableListOf<String>()
        if ( ! isGranted(Manifest.permission.WRITE_EXTERNAL_STORAGE) ) permissions.add(Manifest.permission.WRITE_EXTERNAL_STORAGE)
        if ( ! isGranted(Manifest.permission.CAMERA) ) permissions.add(Manifest.permission.CAMERA)
        if (permissions.size > 0)
            ActivityCompat.requestPermissions(this, permissions.toTypedArray(), PERMISSION_REQUEST_CODE)
    }
    private fun isGranted(permission: String): Boolean =
            ActivityCompat.checkSelfPermission(this, permission) == PackageManager.PERMISSION_GRANTED
    // return a new file with a timestamp name in a folder named [folder] in
    // the external directory for pictures.
    // Return null if the file cannot be created
    private fun getOutputMediaFile(folder: String): File? {
        // in an emulated device you can see the external files in /sdcard/Android/data/<your app>.
        val mediaStorageDir = File(getExternalFilesDir(Environment.DIRECTORY_PICTURES), folder)
        // Create the storage directory if it does not exist
        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                Log.d(TAG, "failed to create directory")
                return null
            }
        }
        // Create a media file name
        val timeStamp: String = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        val postfix = "jpg"
        val prefix = "IMG"
        return File(mediaStorageDir.path +
                File.separator + prefix +
                "_" + timeStamp + "." + postfix)
    }
    //The camera intent activity opening the phone camera
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        val mImage = findViewById<ImageView>(R.id.imgView)
        val tvImageInfo = findViewById<TextView>(R.id.tvImageInfo)
        when (requestCode) {

            CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE_BY_FILE ->
                if (resultCode == RESULT_OK)
                    showImageFromFile(mImage, tvImageInfo, mFile!!)
                else handleOther(resultCode)
        }
    }
    private fun handleOther(resultCode: Int) {
        if (resultCode == RESULT_CANCELED)
            Toast.makeText(this, "Canceled...", Toast.LENGTH_LONG).show()
        else Toast.makeText(this, "Picture NOT taken - unknown error...", Toast.LENGTH_LONG).show()
    }

    // show the image allocated in [f] in imageview [img]. Show meta data in [txt] = END Result set in app
    private fun showImageFromFile(img: ImageView, txt: TextView, f: File) {
        img.setImageURI(Uri.fromFile(f))
        img.setBackgroundColor(Color.BLACK)
        //mImage.setRotation(90);
        txt.text = "File at:" + f.absolutePath + " - size = " + f.length()

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