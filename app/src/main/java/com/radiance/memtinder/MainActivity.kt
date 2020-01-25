package com.radiance.memtinder

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.radiance.memtinder.vkapi.api.GroupAnswer
import com.radiance.memtinder.vkapi.api.IVkApi
import com.radiance.memtinder.vkapi.api.MemesAnswer
import com.radiance.memtinder.vkapi.api.VkApi
import com.radiance.memtinder.vkapi.group.VkGroup
import com.vk.api.sdk.VK
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(), IVkApi.GroupListener, IVkApi.MemesListener, IVkApi.RecommendedMemesListener {
    override fun receiveMemes(answer: MemesAnswer) {
        var str = ""

        answer.memes.forEach{
            str += it.title + "\n"
        }

        runOnUiThread{
            groups.text = str
        }
    }

    private var groupsList: List<VkGroup>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        VkApi.authorization(this)
        VkApi.addGroupListener(this)
        VkApi.addRecommendedMemesListener(this)

        get_groups.setOnClickListener{
            VkApi.requestGroups()
        }

        get_memes.setOnClickListener{
            VkApi.requestRecommendedMemes(15, "")
        }
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (data == null || !VK.onActivityResult(requestCode, resultCode, data, VkApi.callback)) {
            super.onActivityResult(requestCode, resultCode, data)
        }
    }

    override fun receiveGroup(answer: GroupAnswer) {
        var str = ""

        groupsList = answer.groups

        answer.groups.forEach{
            str += it.name + "\n"
        }

        runOnUiThread{
            groups.text = str
        }
    }
}