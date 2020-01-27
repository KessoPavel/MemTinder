package com.radiance.memtinder

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.radiance.memtinder.vkapi.group.VkGroup
import com.radiance.memtinder.vkapi.id.VkId
import com.radiance.memtinder.vkapi.memes.VkMemes
import kotlinx.android.synthetic.main.mem_view.view.*

class CardSwipeAdapter(
    var memes: ArrayList<VkMemes>,
    var groups: List<VkGroup>
) : RecyclerView.Adapter<CardSwipeAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = parent.inflate(R.layout.mem_view)
        return ViewHolder(view)
    }

    override fun getItemCount() = memes.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(memes[position], getGroupName(memes[position].sourceId))
    }

    class ViewHolder(val view: View): RecyclerView.ViewHolder(view) {
        fun bind(mem: VkMemes, groupName: String) {
            view.title.text = mem.title

            val image = mem.images[0]

            Glide
                .with(view)
                .load(image.getBestResolutionImage())
                .placeholder(R.drawable.rounded_shape)
                .into(view.mem)

            view.group_title.text = groupName
        }
    }

    private fun getGroupName(id: VkId): String{
        for (group in groups) {
            if ( group.id.getGroupId() == id.getGroupId())
                return group.name
        }
        return ""
    }
}