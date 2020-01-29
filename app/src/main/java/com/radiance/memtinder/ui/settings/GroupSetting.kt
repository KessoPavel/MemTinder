package com.radiance.memtinder.ui.settings

import android.content.Context
import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager

import com.radiance.memtinder.R
import com.radiance.memtinder.memProvider.news.MemProvider
import com.radiance.memtinder.ui.groupAdapter.GroupAdapter
import com.radiance.memtinder.ui.groupAdapter.GroupItem
import kotlinx.android.synthetic.main.fragment_settings.*
import kotlinx.android.synthetic.main.toolbar_settings.*

class GroupSetting : Fragment(), GroupAdapter.GroupItemListener {
    private val adapter: GroupAdapter by lazy {
        GroupAdapter(ArrayList(), this)
    }

    private lateinit var viewModel: GroupSettingViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_settings, container, false)
    }


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(GroupSettingViewModel::class.java)

        viewModel.groups.observe(this, Observer { updateGroups() })
        viewModel.enabledGroup.observe(this, Observer { updateGroups() })

        viewModel.init(context!!.getSharedPreferences(MemProvider.FILE_NAME, Context.MODE_PRIVATE))

        group_recycler.adapter = adapter
        group_recycler.layoutManager = LinearLayoutManager(context)

        updateIsChecked()

        selectAll.setOnClickListener {
            viewModel.enabledAll(selectAll.isChecked)
        }

        (activity as AppCompatActivity).setSupportActionBar(toolbar_setting)
        (activity as AppCompatActivity).supportActionBar?.setDisplayShowHomeEnabled(true)
        (activity as AppCompatActivity).supportActionBar?.setDisplayHomeAsUpEnabled(true)

        toolbar_setting.setNavigationOnClickListener {
            findNavController().popBackStack()
        }
    }

    private fun updateIsChecked() {
        selectAll.isChecked = viewModel.groups.value?.size == viewModel.enabledGroup.value?.size

    }

    private fun updateGroups() {
        val answer = ArrayList<GroupItem>()

        val enabledGroup = viewModel.getEnabledGroup()
        for (group in viewModel.getGroup()) {
            val gi = GroupItem(group, enabledGroup.contains(group))
            answer.add(gi)
        }

        adapter.groups = answer
        adapter.notifyDataSetChanged()
    }

    override fun groupSelected(group: GroupItem) {
        viewModel.enabledGroup(group.group, !group.isSelected)
        updateIsChecked()
    }

}
