package com.radiance.memtinder.ui.groupAdapter

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.radiance.memtinder.R
import com.radiance.memtinder.getBestResolutionImage
import com.radiance.memtinder.inflate
import kotlinx.android.synthetic.main.item_group.view.*

class GroupAdapter(var groups: ArrayList<GroupItem>, private val listener: GroupItemListener) : RecyclerView.Adapter<GroupAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = parent.inflate(R.layout.item_group)
        return ViewHolder(view, listener)
    }

    override fun getItemCount() = groups.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(groups[position])
    }

    class ViewHolder(private val view: View, private val listener: GroupItemListener): RecyclerView.ViewHolder(view) {

        fun bind(group: GroupItem) {
            view.group_name.text = group.group.name
            Glide.with(view)
                .load(group.group.avatar.getBestResolutionImage())
                .placeholder(R.drawable.rounded_shape)
                .apply(RequestOptions.circleCropTransform())
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(view.icon)
            view.selected.isChecked = group.isSelected

            view.selected.setOnClickListener{
                listener.groupSelected(group)
            }
        }
    }

    interface GroupItemListener {
        fun groupSelected(group: GroupItem)
    }
}