package com.radiance.memtinder

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.radiance.memtinder.memProvider.IMemProvider
import com.radiance.memtinder.memProvider.MemProvider
import com.radiance.memtinder.vkapi.api.GroupAnswer
import com.radiance.memtinder.vkapi.api.IVkApi
import com.radiance.memtinder.vkapi.api.MemesAnswer
import com.radiance.memtinder.vkapi.api.VkApi
import com.radiance.memtinder.vkapi.group.VkGroup
import com.radiance.memtinder.vkapi.memes.VkMemes
import com.vk.api.sdk.VK
import com.yuyakaido.android.cardstackview.*
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity :
    AppCompatActivity(),
    IMemProvider.MemListener,
    IMemProvider.UpdateGroupListener,
    IVkApi.AuthorizationListener,
    CardStackListener{
    private lateinit var memProvider: IMemProvider
    private val manager by lazy { CardStackLayoutManager(this, this) }
    private val adapter by lazy { CardSwipeAdapter(ArrayList()) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        VkApi.addAuthorizationListener(this)
        VkApi.authorization(this)

        memProvider = MemProvider(getSharedPreferences(MemProvider.FILE_NAME, Context.MODE_PRIVATE))
        memProvider.addMemListener(this)
        memProvider.addUpdateListener(this)

        manager.setStackFrom(StackFrom.Top)
        manager.setVisibleCount(3)

        card_stack_view.layoutManager = manager
        card_stack_view.adapter = adapter
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (data == null || !VK.onActivityResult(requestCode, resultCode, data, VkApi.callback)) {
            super.onActivityResult(requestCode, resultCode, data)
        }
    }

    override fun receiveMemes(memes: List<VkMemes>) {
        runOnUiThread {
            adapter.memes = ArrayList(memes)
            adapter.notifyDataSetChanged()
        }
    }

    override fun groupLoaded() {
        val groups = memProvider.getGroups()

        memProvider.clearEnabled()
        memProvider.enableMemFromGroup(groups[0], true)
        memProvider.enableMemFromGroup(groups[13], true)
        memProvider.enableMemFromGroup(groups[5], true)

        //memProvider.enabledAll()
        memProvider.requestMemes(50)
    }

    override fun isAuthorized(boolean: Boolean) {
        memProvider.load()
    }

    override fun onCardDisappeared(view: View?, position: Int) {
    }

    override fun onCardDragging(direction: Direction?, ratio: Float) {
    }

    override fun onCardSwiped(direction: Direction?) {

    }

    override fun onCardCanceled() {

    }

    override fun onCardAppeared(view: View?, position: Int) {
    }

    override fun onCardRewound() {

    }
}