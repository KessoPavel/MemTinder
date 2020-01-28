package com.radiance.memtinder.ui.settings

import android.content.Context
import android.os.Build
import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager

import com.radiance.memtinder.R
import com.radiance.memtinder.memProvider.ProviderDispatcher
import com.radiance.memtinder.memProvider.news.IMemProvider
import com.radiance.memtinder.memProvider.news.MemProvider
import com.radiance.memtinder.ui.groupAdapter.GroupAdapter
import com.radiance.memtinder.ui.groupAdapter.GroupItem
import kotlinx.android.synthetic.main.group_setting_fragment.*

class GroupSetting : Fragment(), GroupAdapter.GroupItemListener {
    private val adapter: GroupAdapter by lazy {
        GroupAdapter(ArrayList(), this)
    }

    private lateinit var viewModel: GroupSettingViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.group_setting_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(GroupSettingViewModel::class.java)

        viewModel.groups.observe(this, Observer { updateGroups() })
        viewModel.enabledGroup.observe(this, Observer { updateGroups() })

        viewModel.init(context!!.getSharedPreferences(MemProvider.FILE_NAME, Context.MODE_PRIVATE))

        group_recycler.adapter = adapter
        group_recycler.layoutManager = LinearLayoutManager(context)

        selectAll.setOnClickListener{
            viewModel.enabledAll(selectAll.isChecked)
        }
    }

    private fun updateGroups() {
        val answer = ArrayList<GroupItem>()

        val enabledGroup = viewModel.getEnabledGroup()
        for(group in viewModel.getGroup()) {
            val gi = GroupItem(group, enabledGroup.contains(group))
            answer.add(gi)
        }

        adapter.groups = answer
        adapter.notifyDataSetChanged()
    }

    override fun groupSelected(group: GroupItem) {
        viewModel.enabledGroup(group.group, !group.isSelected)
    }

}
