package com.radiance.memtinder

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.radiance.memtinder.vkapi.api.IVkApi
import com.radiance.memtinder.vkapi.api.VkApi
import com.vk.api.sdk.VK
import com.vk.api.sdk.utils.VKUtils.getCertificateFingerprint


class MainActivity :
    AppCompatActivity(),
    IVkApi.AuthorizationListener{

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        VkApi.addAuthorizationListener(this)
        VkApi.authorization(this)
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (data == null || !VK.onActivityResult(requestCode, resultCode, data, VkApi.callback)) {
            super.onActivityResult(requestCode, resultCode, data)
        }
    }

    override fun isAuthorized(boolean: Boolean) {

    }
}