package com.radiance.memtinder

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.radiance.memtinder.vkapi.memes.VkMemes
import kotlinx.android.synthetic.main.mem_view.view.*

class CardSwipeAdapter(
    var memes: ArrayList<VkMemes>
) : RecyclerView.Adapter<CardSwipeAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = parent.inflate(R.layout.mem_view)
        return ViewHolder(view)
    }

    override fun getItemCount() = memes.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(memes[position])
    }

    class ViewHolder(val view: View): RecyclerView.ViewHolder(view) {
        fun bind(mem: VkMemes) {
            view.title.text = mem.title

            val image = mem.images[0]

            Glide
                .with(view)
                .load(image.getBestResolutionImage())
                .centerCrop()
                .placeholder(R.drawable.mem)
                .into(view.mem)
        }
    }
}