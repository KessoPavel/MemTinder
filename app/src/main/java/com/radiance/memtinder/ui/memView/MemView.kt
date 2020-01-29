package com.radiance.memtinder.ui.memView

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.*
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.radiance.memtinder.R
import com.radiance.memtinder.getBestResolutionImage
import com.radiance.memtinder.memProvider.news.MemProvider
import com.radiance.memtinder.memProvider.news.Source
import com.radiance.memtinder.toRating
import com.radiance.memtinder.ui.cardAdapter.CardSwipeAdapter
import com.radiance.memtinder.ui.cardAdapter.MemCard
import com.radiance.memtinder.ui.textViewer.TextViewer
import com.radiance.memtinder.vkapi.group.VkGroup
import com.radiance.memtinder.vkapi.id.VkId
import com.radiance.memtinder.vkapi.image.VkImage
import com.radiance.memtinder.vkapi.memes.VkMemes
import com.stfalcon.imageviewer.StfalconImageViewer
import com.yuyakaido.android.cardstackview.CardStackLayoutManager
import com.yuyakaido.android.cardstackview.CardStackListener
import com.yuyakaido.android.cardstackview.Direction
import com.yuyakaido.android.cardstackview.StackFrom
import kotlinx.android.synthetic.main.fragment_memes.*
import kotlinx.android.synthetic.main.toolbar_mem.*

class MemView
    : Fragment(),
    CardStackListener,
    CardSwipeAdapter.ClickListener {

    private lateinit var viewModel: MemViewViewModel

    private lateinit var newsManager: CardStackLayoutManager
    private val newsAdapter: CardSwipeAdapter by lazy {
        CardSwipeAdapter(ArrayList(), this)
    }
    private lateinit var recommendedManager: CardStackLayoutManager
    private val recommendedAdapter: CardSwipeAdapter by lazy {
        CardSwipeAdapter(ArrayList(), this)
    }

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

    override fun onResume() {
        super.onResume()
        initView()
        initViewModel()

        viewModel.requestMem(requestCount, true)
    }

    private fun initView() {
        initNewsView()
        initRecommendedView()

        if (source_switch.isChecked) {
            adapter = recommendedAdapter
            manager = recommendedManager
        } else {
            adapter = newsAdapter
            manager = newsManager
        }

        card_stack_view.layoutManager = manager
        card_stack_view.adapter = adapter

        update_button.setOnClickListener {
            adapter.memes = ArrayList()
            adapter.notifyDataSetChanged()

            viewModel.clear()
            viewModel.requestMem(requestCount, true)
        }

        share_button.setOnClickListener {
            val mem: MemCard = adapter.memes[manager.topPosition]

            var shareString = mem.title
            for (image in mem.mem.images) {
                shareString += "\n${image.getBestResolutionImage()}"
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
            val source: Source
            if (source_switch.isChecked) {
                source = Source.RECOMMENDED
                manager = recommendedManager
                adapter = recommendedAdapter
            } else {
                source = Source.NEWS
                manager = newsManager
                adapter = newsAdapter
            }

            card_stack_view.layoutManager = manager
            card_stack_view.adapter = adapter

            viewModel.setSource(source)
            viewModel.requestMem(requestCount)
        }

        settings.setOnClickListener{
            findNavController().navigate(R.id.action_memView_to_groupSetting)
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
        viewModel = ViewModelProviders.of(this).get(MemViewViewModel::class.java)

        val source = if (source_switch.isChecked) {
            Source.RECOMMENDED
        } else {
            Source.NEWS
        }

        viewModel.init(
            context!!.getSharedPreferences(MemProvider.FILE_NAME, Context.MODE_PRIVATE),
            source
        )
        viewModel.news.observe(this, Observer { addNewMemes(it) })
        viewModel.recommended.observe(this, Observer { addRecommended(it) })
    }

    private fun addNewMemes(memes: ArrayList<VkMemes>?) {
        memes?.let {
            newsAdapter.memes.addAll(memToCard(memes))
            newsAdapter.notifyItemInserted(newsAdapter.memes.size - memes.size)
        }
    }

    private fun addRecommended(memes: ArrayList<VkMemes>?) {
        memes?.let {
            recommendedAdapter.memes.addAll(memToCard(memes))
            recommendedAdapter.notifyItemInserted(recommendedAdapter.memes.size - memes.size)
        }
    }

    override fun onCardDisappeared(view: View?, position: Int) {
    }

    override fun onCardDragging(direction: Direction?, ratio: Float) {
    }

    override fun onCardSwiped(direction: Direction?) {
        val swipedMem = adapter.memes[manager.topPosition]
        val rating = direction?.toRating()

        if (rating != null) {
            viewModel.setRating(swipedMem.mem, rating)
        }

        if ((adapter.memes.size - manager.topPosition) <= visibleCount) {
            viewModel.requestMem(requestCount)
        }
    }

    override fun onCardCanceled() {
    }

    override fun onCardAppeared(view: View?, position: Int) {
    }

    override fun onCardRewound() {
    }


    @SuppressLint("Range")
    override fun onImageClick(mem: MemCard, imageView: ImageView) {
        StfalconImageViewer.Builder<VkImage>(context, mem.mem.images, ::loadPosterImage)
            .withBackgroundColor(Color.BLACK)
            .withTransitionFrom(imageView)
            .show()
    }

    private fun loadPosterImage(imageView: ImageView, image: VkImage?) {
        imageView.apply {
            Glide
                .with(this)
                .load(image?.getBestResolutionImage())
                .placeholder(R.drawable.rounded_shape)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(imageView)
        }
    }

    override fun onTextClick(mem: MemCard) {
        activity?.supportFragmentManager?.let {
            it.beginTransaction()
                .add(R.id.nav_host_fragment, TextViewer.newInstance(mem.title))
                .addToBackStack(null)
                .commit()
        }
    }

    override fun onGroupClick(mem: MemCard) {
    }

    private fun memToCard(memes: List<VkMemes>): List<MemCard> {
        val answer = ArrayList<MemCard>()

        for (mem in memes) {
            val groupName = getGroupName(viewModel.groupList.value, mem.sourceId)
            val image = mem.images[0].getBestResolutionImage()
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

    private fun getGroupName(groupList: List<VkGroup>?, sourceId: VkId): String {
        groupList?.let {
            for (group in it) {
                if (group.id.getGroupId() == sourceId.getGroupId())
                    return group.name
            }
        }

        return ""
    }
}
