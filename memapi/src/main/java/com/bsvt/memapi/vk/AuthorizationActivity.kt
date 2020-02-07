package com.bsvt.memapi.vk

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.bsvt.memapi.R
import com.vk.api.sdk.VK
import com.vk.api.sdk.VKTokenExpiredHandler
import com.vk.api.sdk.auth.VKAccessToken
import com.vk.api.sdk.auth.VKAuthCallback
import com.vk.api.sdk.auth.VKScope

class AuthorizationActivity : AppCompatActivity() {
    private val callback = object : VKAuthCallback {
        override fun onLogin(token: VKAccessToken) {
            VkApi.authorizationSuccess(true)
            this@AuthorizationActivity.finish()
        }

        override fun onLoginFailed(errorCode: Int) {
            VkApi.authorizationSuccess(false)
            this@AuthorizationActivity.finish()
        }
    }

    private val tokenTracker = object : VKTokenExpiredHandler {
        override fun onTokenExpired() {
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_authorization)

        VK.initialize(this)
        VK.addTokenExpiredHandler(tokenTracker)

        if (!VK.isLoggedIn()) {
            VK.login(
                this,
                arrayListOf(VKScope.WALL, VKScope.PHOTOS, VKScope.GROUPS, VKScope.FRIENDS)
            )
        } else {
            VkApi.authorizationSuccess(true)
            this@AuthorizationActivity.finish()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (data == null || !VK.onActivityResult(requestCode, resultCode, data, callback)) {
            super.onActivityResult(requestCode, resultCode, data)
        }
    }
}
