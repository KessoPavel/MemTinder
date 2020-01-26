package com.radiance.memtinder

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.radiance.memtinder.memProvider.IMemProvider
import com.radiance.memtinder.memProvider.MemProvider
import com.radiance.memtinder.vkapi.api.GroupAnswer
import com.radiance.memtinder.vkapi.api.IVkApi
import com.radiance.memtinder.vkapi.api.MemesAnswer
import com.radiance.memtinder.vkapi.api.VkApi
import com.radiance.memtinder.vkapi.group.VkGroup
import com.radiance.memtinder.vkapi.memes.VkMemes
import com.vk.api.sdk.VK
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(), IMemProvider.MemListener, IMemProvider.UpdateGroupListener, IVkApi.AuthorizationListener {
    private lateinit var memProvider: IMemProvider

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        VkApi.addAuthorizationListener(this)
        VkApi.authorization(this)

        memProvider = MemProvider(getSharedPreferences(MemProvider.FILE_NAME, Context.MODE_PRIVATE))
        memProvider.addMemListener(this)
        memProvider.addUpdateListener(this)

        get_memes.setOnClickListener{
            memProvider.requestMemes(10)
        }
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (data == null || !VK.onActivityResult(requestCode, resultCode, data, VkApi.callback)) {
            super.onActivityResult(requestCode, resultCode, data)
        }
    }

    override fun receiveMemes(memes: List<VkMemes>) {

    }

    override fun groupLoaded() {
        val groups = memProvider.getGroups()

        memProvider.enableMemFromGroup(groups[0], true)
        memProvider.enableMemFromGroup(groups[4], true)
        memProvider.enableMemFromGroup(groups[5], true)

        memProvider.requestMemes(10)
    }

    override fun isAuthorized(boolean: Boolean) {
        memProvider.load()
    }
}