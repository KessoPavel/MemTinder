package com.radiance.memtinder

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.bsvt.login.Login
import kotlinx.coroutines.ExperimentalCoroutinesApi


class MainActivity :
    AppCompatActivity(){

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    @ExperimentalCoroutinesApi
    override fun onStart() {
        super.onStart()

       val account = Login.currentAccount(this)
       if (account == null) {
           Login.login(this)
       } else {
           val memIntent = Intent(this, MemActivity::class.java)
           startActivity(memIntent)
       }

      //  val api = VkMemApi()
      //  if (!api.isRegistered()) {
      //      GlobalScope.launch {
      //          api.toRegister(this@MainActivity)
      //      }
      //  } else {
      //      val flow = api.startMemFlow(10, true)
      //      GlobalScope.launch {
      //          flow.take(10).collect { mem ->
      //              Log.i("MEM_TITLE", "mem " + mem.postId)
      //          }
      //      }
      //      GlobalScope.launch {
      //          flow.take(10).collect { mem ->
      //              Log.i("MEM_TITLE", "mem2 " + mem.postId)
      //          }
      //      }
      //      GlobalScope.launch {
      //          flow.collect {
      //              mem ->
      //              Log.i("MEM_TITLE", " " +mem.postId)
//
      //          }
      //      }
    }
}