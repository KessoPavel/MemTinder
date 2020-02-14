package com.radiance.memtinder

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.bsvt.login.Login


class MainActivity :
    AppCompatActivity(){

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    override fun onStart() {
        super.onStart()

        val account = Login.currentAccount(this)
        if (account == null) {
            Login.login(this)
        } else {
            val memIntent = Intent(this, MemActivity::class.java)
            startActivity(memIntent)
        }
    }
}