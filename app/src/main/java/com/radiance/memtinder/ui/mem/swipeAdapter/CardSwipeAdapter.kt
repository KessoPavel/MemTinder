package com.radiance.memtinder.ui.mem.swipeAdapter

import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.radiance.memtinder.R
import com.radiance.memtinder.inflate
import kotlinx.android.synthetic.main.item_mem.view.*


class CardSwipeAdapter(
    var memes: ArrayList<MemCard>,
    private val listener: ClickListener
) : RecyclerView.Adapter<CardSwipeAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = parent.inflate(R.layout.item_mem)
        return ViewHolder(
            view,
            listener
        )
    }

    override fun getItemCount() = memes.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(memes[position])
    }

    class ViewHolder(
        private val view: View,
        private val listener: ClickListener) :
        RecyclerView.ViewHolder(view) {

        fun bind(mem: MemCard) {
            view.title.text = mem.title
            view.group_title.text = mem.groupName
            Glide.with(view)
                .load(mem.url)
                .placeholder(R.drawable.rounded_shape)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(view.mem)
            view.images_count.text = mem.imagesCount

            view.title.setOnClickListener {
                listener.onTextClick(mem)
            }

            view.mem.setOnClickListener {
                listener.onImageClick(mem, view.mem)
            }
        }
    }

    interface ClickListener {
        fun onImageClick(mem: MemCard, imageView: ImageView)
        fun onTextClick(mem: MemCard)
        fun onGroupClick(mem: MemCard)
    }
}