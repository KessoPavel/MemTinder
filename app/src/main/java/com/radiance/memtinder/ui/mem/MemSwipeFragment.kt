package com.radiance.memtinder.ui.mem

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.radiance.domain.entity.Id
import com.radiance.domain.entity.Image
import com.radiance.domain.entity.Mem
import com.radiance.domain.entity.Source
import com.radiance.memtinder.R
import com.radiance.memtinder.ui.mem.swipeAdapter.CardSwipeAdapter
import com.radiance.memtinder.ui.mem.swipeAdapter.MemCard
import com.radiance.memtinder.ui.text.MemText
import com.stfalcon.imageviewer.StfalconImageViewer
import com.yuyakaido.android.cardstackview.CardStackLayoutManager
import com.yuyakaido.android.cardstackview.CardStackListener
import com.yuyakaido.android.cardstackview.Direction
import com.yuyakaido.android.cardstackview.StackFrom
import kotlinx.android.synthetic.main.fragment_memes.*
import kotlinx.android.synthetic.main.toolbar_mem.*
import kotlinx.coroutines.*

class MemSwipeFragment : Fragment(),
    CardStackListener,
    CardSwipeAdapter.ClickListener {
    private val scope = CoroutineScope(Dispatchers.IO)

    private lateinit var viewModel: MemSwipeFragmentViewModel

    private lateinit var newsManager: CardStackLayoutManager
    private val newsAdapter: CardSwipeAdapter by lazy {
        CardSwipeAdapter(ArrayList(), this)
    }
    private lateinit var recommendedManager: CardStackLayoutManager
    private val recommendedAdapter: CardSwipeAdapter by lazy {
        CardSwipeAdapter(ArrayList(), this)
    }

    private lateinit var sourceType: SourceType
    private lateinit var manager: CardStackLayoutManager
    private lateinit var adapter: CardSwipeAdapter

    private val requestCount = 10
    private val visibleCount = 4

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_memes, container, false)
    }

    @ExperimentalCoroutinesApi
    override fun onResume() {
        super.onResume()

        initView()
        initViewModel()
    }

    private fun initView() {
        initNewsView()
        initRecommendedView()

        if (source_switch.isChecked) {
            sourceType = SourceType.RECOMMENDED
            adapter = recommendedAdapter
            manager = recommendedManager
        } else {
            sourceType = SourceType.NEWS
            adapter = newsAdapter
            manager = newsManager
        }

        card_stack_view.layoutManager = manager
        card_stack_view.adapter = adapter

        update_button.setOnClickListener {
            adapter.memes = ArrayList()
            adapter.notifyDataSetChanged()

            scope.launch {
                viewModel.requestMem(requestCount, true, sourceType)
            }
        }

        share_button.setOnClickListener {
            val mem: MemCard = adapter.memes[manager.topPosition]

            var shareString = mem.title
            for (image in mem.mem.images) {
                //shareString += "\n${image.getBestResolutionImage()}"
            }

            val sharingIntent = Intent(Intent.ACTION_SEND)
            sharingIntent.type = "text/plain"
            sharingIntent.putExtra(Intent.EXTRA_TEXT, shareString)
            startActivity(
                Intent.createChooser(
                    sharingIntent,
                    null
                )
            )
        }

        rewind_button.setOnClickListener {
            card_stack_view.rewind()
        }

        source_switch.setOnClickListener {
            if (source_switch.isChecked) {
                sourceType = SourceType.RECOMMENDED
                manager = recommendedManager
                adapter = recommendedAdapter
            } else {
                sourceType = SourceType.NEWS
                manager = newsManager
                adapter = newsAdapter
            }

            card_stack_view.layoutManager = manager
            card_stack_view.adapter = adapter

            scope.launch {
                viewModel.requestMem(requestCount, false, sourceType)
            }
        }
    }

    private fun initRecommendedView() {
        recommendedManager = CardStackLayoutManager(context, this)
        recommendedManager.setStackFrom(StackFrom.Right)
        recommendedManager.setVisibleCount(visibleCount)
    }

    private fun initNewsView() {
        newsManager = CardStackLayoutManager(context, this)
        newsAdapter.memes = ArrayList()
        newsAdapter.notifyDataSetChanged()

        newsManager.setStackFrom(StackFrom.Right)
        newsManager.setVisibleCount(visibleCount)
    }


    private fun initViewModel() {
        viewModel = ViewModelProviders.of(this).get(MemSwipeFragmentViewModel::class.java)

        viewModel.newsfeed.observe(this, Observer { addNewMemes(it) })
        viewModel.recommended.observe(this, Observer { addRecommended(it) })
        viewModel.sourcesList.observe(this, Observer { addSources(it) })
        viewModel.enabledSourceList.observe(this, Observer { })

        scope.launch {
                viewModel.login(activity!!)
        }
    }

    private fun addNewMemes(memes: ArrayList<Mem>?) {
        memes?.let {
            newsAdapter.memes.addAll(memToCard(viewModel.sourcesList.value, memes))
            newsAdapter.notifyItemInserted(newsAdapter.memes.size - memes.size)
        }
    }

    private fun addRecommended(memes: ArrayList<Mem>?) {
        memes?.let {
            recommendedAdapter.memes.addAll(memToCard(viewModel.sourcesList.value, memes))
            recommendedAdapter.notifyItemInserted(recommendedAdapter.memes.size - memes.size)
        }
    }

    private fun addSubscription(groups: List<Source>?) {

    }

    private fun addSources(groups: List<Source>?) {
        groups?.let {
            for (mem in recommendedAdapter.memes) {
                if (mem.groupName == "") {
                    mem.groupName = getGroupName(viewModel.sourcesList.value, mem.mem.sourceId)
                    if (mem.groupName != "") {
                        recommendedAdapter.notifyItemChanged(recommendedAdapter.memes.indexOf(mem))
                    }
                }
            }
        }
    }


    private fun memToCard(groupList: List<Source>?, memes: List<Mem>): List<MemCard> {
        val answer = ArrayList<MemCard>()

        for (mem in memes) {
            val groupName = getGroupName(groupList, mem.sourceId)
            val image = mem.images[0].getImageLink(mem.images[0].getHighResolution())!!
            val imageCount = if (mem.images.size == 1) "" else mem.images.size.toString()

            val memCard = MemCard(
                image,
                mem.title,
                groupName,
                imageCount,
                mem
            )

            answer.add(memCard)
        }

        return answer
    }

    private fun getGroupName(groupList: List<Source>?, sourceId: Id): String {
        groupList?.let {
            try {
                for (group in it) {
                    if (group.id.toLong() == sourceId.toLong())
                        return group.name
                }

            } catch (e: ConcurrentModificationException) {

            }
        }

        return ""
    }


    //region CardSwipe
    override fun onCardDisappeared(view: View?, position: Int) {
    }

    override fun onCardDragging(direction: Direction?, ratio: Float) {
    }

    @ExperimentalCoroutinesApi
    override fun onCardSwiped(direction: Direction?) {
        val swipedMem = adapter.memes[manager.topPosition]

        val rating = when (direction) {
            Direction.Right -> "like"
            Direction.Left -> "dislike"
            else -> "unknown"
        }

        viewModel.setRating(swipedMem.mem, rating)

        if ((adapter.memes.size - manager.topPosition) <= visibleCount) {
            scope.launch {
                viewModel.requestMem(requestCount, false, sourceType)
            }
        }
    }

    override fun onCardCanceled() {
    }

    override fun onCardAppeared(view: View?, position: Int) {
    }

    override fun onCardRewound() {
    }
    //endregion

    //region CardClick
    @SuppressLint("Range")
    override fun onImageClick(mem: MemCard, imageView: ImageView) {
        StfalconImageViewer.Builder<Image>(context, mem.mem.images, ::loadPosterImage)
            .withBackgroundColor(Color.BLACK)
            .withTransitionFrom(imageView)
            .show()
    }

    private fun loadPosterImage(imageView: ImageView, image: Image?) {
        imageView.apply {
            Glide
                .with(this)
                .load(image?.getImageLink(image.getHighResolution()))
                .placeholder(R.drawable.rounded_shape)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(imageView)
        }
    }

    override fun onTextClick(mem: MemCard) {
        activity?.supportFragmentManager?.beginTransaction()
            ?.add(R.id.nav_host_fragment, MemText.newInstance(mem.title))
            ?.addToBackStack(null)?.commit()
    }

    override fun onGroupClick(mem: MemCard) {
    }
    //endregion
}