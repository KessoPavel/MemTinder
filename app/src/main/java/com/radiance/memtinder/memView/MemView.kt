package com.radiance.memtinder.memView

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AccelerateInterpolator
import android.view.animation.DecelerateInterpolator
import android.widget.ImageView
import androidx.lifecycle.Observer
import com.bumptech.glide.Glide
import com.radiance.memtinder.CardSwipeAdapter

import com.radiance.memtinder.R
import com.radiance.memtinder.getBestResolutionImage
import com.radiance.memtinder.memProvider.MemProvider
import com.radiance.memtinder.photoView.PhotoViewer
import com.radiance.memtinder.vkapi.image.VkImage
import com.radiance.memtinder.vkapi.memes.VkMemes
import com.stfalcon.imageviewer.StfalconImageViewer
import com.yuyakaido.android.cardstackview.*
import kotlinx.android.synthetic.main.mem_view.*
import kotlinx.android.synthetic.main.mem_view.view.*
import kotlinx.android.synthetic.main.mem_view_fragment.*
import java.util.ArrayList

class MemView : Fragment(), CardStackListener, CardSwipeAdapter.ClickListener {

    companion object {
        fun newInstance() = MemView()
    }

    private lateinit var viewModel: MemViewViewModel
    private val manager by lazy { CardStackLayoutManager(context, this) }
    private val adapter by lazy { CardSwipeAdapter(ArrayList(), ArrayList(), this) }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.mem_view_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        initView()

        viewModel = ViewModelProviders.of(this).get(MemViewViewModel::class.java)
        viewModel.init(context!!.getSharedPreferences(MemProvider.FILE_NAME, Context.MODE_PRIVATE))

        viewModel.memesStore.observe(this, Observer { updateAdapter(it) })
        viewModel.groups.observe(this, Observer { adapter.groups = it })

        viewModel.loadMem(10)
    }

    private fun initView() {
        manager.setStackFrom(StackFrom.Right)
        manager.setVisibleCount(4)

        card_stack_view.layoutManager = manager
        card_stack_view.adapter = adapter

        rewind_button.setOnClickListener {
            card_stack_view.rewind()
        }

        like_button.setOnClickListener {
            val setting = SwipeAnimationSetting.Builder()
                .setDirection(Direction.Right)
                .setDuration(Duration.Normal.duration)
                .setInterpolator(AccelerateInterpolator())
                .build()
            manager.setSwipeAnimationSetting(setting)

            card_stack_view.swipe()
        }

        skip_button.setOnClickListener {
            val setting = SwipeAnimationSetting.Builder()
                .setDirection(Direction.Left)
                .setDuration(Duration.Normal.duration)
                .setInterpolator(AccelerateInterpolator())
                .build()
            manager.setSwipeAnimationSetting(setting)
            card_stack_view.swipe()
        }
    }

    private fun updateAdapter(memes: ArrayList<VkMemes>?) {
        memes?.let {
            adapter.memes.addAll(memes)
            adapter.notifyItemInserted(adapter.memes.size - memes.size)
        }
    }

    override fun onCardDisappeared(view: View?, position: Int) {
    }

    override fun onCardDragging(direction: Direction?, ratio: Float) {
    }

    override fun onCardSwiped(direction: Direction?) {
        if ((adapter.memes.size - manager.topPosition) <= 4) {
            viewModel.loadMem(10)
        }
    }

    override fun onCardCanceled() {
    }

    override fun onCardAppeared(view: View?, position: Int) {
    }

    override fun onCardRewound() {
    }


    @SuppressLint("Range")
    override fun onImageClick(mem: VkMemes, imageView: ImageView) {
        StfalconImageViewer.Builder<VkImage>(context, mem.images, ::loadPosterImage)
            .withBackgroundColor(Color.parseColor("#00000000"))
            .withTransitionFrom(imageView)
            .show()
    }

    private fun loadPosterImage(imageView: ImageView, image: VkImage?) {
        imageView.apply {
            Glide
                .with(this)
                .load(image?.getBestResolutionImage())
                .placeholder(R.drawable.rounded_shape)
                .into(imageView)
        }
    }

    override fun onTextClick(text: String) {

    }

    override fun onGroupClick() {
    }

}
