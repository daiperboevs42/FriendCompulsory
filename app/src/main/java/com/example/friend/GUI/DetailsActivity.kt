package com.example.friend.GUI

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.friend.Data.Friends
import com.example.friend.R

class DetailsActivity : AppCompatActivity(){
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.details)
        val back: Button = findViewById(R.id.backDetails)
        back.setOnClickListener{
            finish()
        }

        val position = intent.getIntExtra("position", -1)
        if (position >= 0) {
            val friend = Friends().getFriend(position)
            val friendName: TextView = findViewById(R.id.friendName)
            friendName.text = friend.name
            val friendNumber: TextView = findViewById(R.id.friendPhone)
            friendNumber.text = friend.phone
        }
        else
        {
            val toast = Toast.makeText(applicationContext,"Could not load Friend, sorry", Toast.LENGTH_LONG)
            toast.show()
        }
    }
}