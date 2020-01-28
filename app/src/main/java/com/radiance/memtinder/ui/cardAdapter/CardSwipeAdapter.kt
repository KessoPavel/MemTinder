package com.radiance.memtinder.ui.cardAdapter

import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.radiance.memtinder.R
import com.radiance.memtinder.inflate
import kotlinx.android.synthetic.main.mem_view.view.*
import kotlinx.android.synthetic.main.mem_view.view.group_title
import kotlinx.android.synthetic.main.mem_view.view.title

class CardSwipeAdapter(
    var memes: ArrayList<MemCard>,
    private val listener: ClickListener
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
        return ViewHolder(
            view,
            listener
        )
    }

    override fun getItemCount() = memes.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(memes[position])
    }

    override fun getItemViewType(position: Int): Int {
        return singleImage
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