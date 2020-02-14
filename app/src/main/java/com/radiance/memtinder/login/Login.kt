package com.radiance.memtinder.login

import android.app.Activity
import android.content.Intent
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount

object Login {
    var account: GoogleSignInAccount? = null

    fun currentAccount(activity: Activity): GoogleSignInAccount? {
        account = GoogleSignIn.getLastSignedInAccount(activity.applicationContext)
        return account
    }

    fun login(activity: Activity) {
        account = GoogleSignIn.getLastSignedInAccount(activity.applicationContext)

        if (account == null) {
            val intent = Intent(activity, LoginActivity::class.java)
            activity.startActivity(intent)
        }
    }
}