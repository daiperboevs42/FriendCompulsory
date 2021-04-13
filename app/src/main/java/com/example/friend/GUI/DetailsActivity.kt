package com.example.friend.GUI

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.friend.Data.FriendRepositoryInDB
import com.example.friend.Data.OUTDATEDFriends
import com.example.friend.R
import androidx.lifecycle.Observer
import com.example.friend.Data.BEFriend

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

        }
    }
}