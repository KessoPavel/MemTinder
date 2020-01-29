package com.radiance.memtinder.ui.memView

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.*
import android.widget.ImageView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.radiance.memtinder.R
import com.radiance.memtinder.getBestResolutionImage
import com.radiance.memtinder.memProvider.news.MemProvider
import com.radiance.memtinder.memProvider.news.Source
import com.radiance.memtinder.toRating
import com.radiance.memtinder.ui.cardAdapter.CardSwipeAdapter
import com.radiance.memtinder.ui.cardAdapter.MemCard
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
    CardSwipeAdapter.ClickListener
{
    private lateinit var viewModel: MemViewViewModel
    private lateinit var newsManager: CardStackLayoutManager
    private val newsAdapter: CardSwipeAdapter by lazy {
        CardSwipeAdapter(ArrayList(), this)
    }
    private val requestCount = 10

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_memes, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        initView()
        initViewModel()

        viewModel.requestMem(requestCount)
    }

    private fun initView() {
        newsManager = CardStackLayoutManager(context, this)
        newsAdapter.memes = ArrayList()
        newsAdapter.notifyDataSetChanged()

        newsManager.setStackFrom(StackFrom.Right)
        newsManager.setVisibleCount(4)

        card_stack_view.layoutManager = newsManager
        card_stack_view.adapter = newsAdapter

        update_button.setOnClickListener {
            newsAdapter.memes = ArrayList()
            newsAdapter.notifyDataSetChanged()

            viewModel.clear()
            viewModel.requestMem(requestCount, true)
        }

        share_button.setOnClickListener {
            val mem = newsAdapter.memes[newsManager.topPosition]

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

        source_switch.setOnClickListener{
            newsAdapter.memes = ArrayList()
            newsAdapter.notifyDataSetChanged()

            val source = if (source_switch.isChecked) {
                Source.RECOMMENDED
            } else {
                Source.NEWS
            }

            viewModel.setSource(source)
            viewModel.requestMem(requestCount)
        }
    }

    private fun initViewModel() {
        viewModel = ViewModelProviders.of(this).get(MemViewViewModel::class.java)

        val source = if (source_switch.isChecked) {
            Source.RECOMMENDED
        } else {
            Source.NEWS
        }

        viewModel.init(context!!.getSharedPreferences(MemProvider.FILE_NAME, Context.MODE_PRIVATE), source)
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
            newsAdapter.memes.addAll(memToCard(memes))
            newsAdapter.notifyItemInserted(newsAdapter.memes.size - memes.size)
        }
    }

    override fun onCardDisappeared(view: View?, position: Int) {
    }

    override fun onCardDragging(direction: Direction?, ratio: Float) {
    }

    override fun onCardSwiped(direction: Direction?) {
        val swipedMem = newsAdapter.memes[newsManager.topPosition]
        val rating = direction?.toRating()

        if (rating != null) {
            viewModel.setRating(swipedMem.mem, rating)
        }

        if ((newsAdapter.memes.size - newsManager.topPosition) <= 4) {
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

    }

    override fun onGroupClick(mem: MemCard) {
    }

    private fun memToCard(memes: List<VkMemes>): List<MemCard> {
        val answer = ArrayList<MemCard>()

        for (mem in memes) {
            val groupName = getGroupName(viewModel.groupList.value, mem.sourceId)
            val image = mem.images[0].getBestResolutionImage()
            val imageCount = if(mem.images.size == 1) "" else mem.images.size.toString()

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
