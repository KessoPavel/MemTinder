package com.radiance.memtinder

import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.radiance.memtinder.vkapi.group.VkGroup
import com.radiance.memtinder.vkapi.id.VkId
import com.radiance.memtinder.vkapi.memes.VkMemes
import kotlinx.android.synthetic.main.mem_view.view.*
import kotlinx.android.synthetic.main.mem_view.view.group_title
import kotlinx.android.synthetic.main.mem_view.view.title

class CardSwipeAdapter(
    var memes: ArrayList<VkMemes>,
    var groups: List<VkGroup>,
    val listener: ClickListener
) : RecyclerView.Adapter<CardSwipeAdapter.ViewHolder>() {

    private val multiImage = 0
    private val singleImage = 1

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val r = if (viewType == multiImage) {
            R.layout.multi_picture_mem_view
        } else {
            R.layout.mem_view
        }
        val view = parent.inflate(r)
        return ViewHolder(view, listener)
    }

    override fun getItemCount() = memes.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(memes[position], getGroupName(memes[position].sourceId))
    }

    override fun getItemViewType(position: Int): Int {
        return singleImage
//        if (memes[position].images.size > 1)
//            return MULTI_MEM
//        else
//            return SINGLE_MEM
    }

    class ViewHolder(val view: View, private val listener: ClickListener) :
        RecyclerView.ViewHolder(view) {
        fun bind(mem: VkMemes, groupName: String) {
            view.title.text = mem.title
            val image = mem.images[0]
            view.group_title.text = groupName
            view.title.setOnClickListener {
                listener.onTextClick(mem.title)
            }

            Glide
                .with(view)
                .load(image.getBestResolutionImage())
                .placeholder(R.drawable.rounded_shape)
                .into(view.mem)

            view.mem.setOnClickListener {
                listener.onImageClick(mem, view.mem)
            }

        }
    }

    private fun getGroupName(id: VkId): String {
        for (group in groups) {
            if (group.id.getGroupId() == id.getGroupId())
                return group.name
        }
        return ""
    }

    interface ClickListener {
        fun onImageClick(mem: VkMemes, imageView: ImageView)
        fun onTextClick(text: String)
        fun onGroupClick()
    }
}