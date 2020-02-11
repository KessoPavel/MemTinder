package com.radiance.memtinder.ui.group

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.radiance.memtinder.R
import com.radiance.memtinder.ui.group.adapter.SourceAdapter
import com.radiance.memtinder.ui.group.adapter.SourceItem
import kotlinx.android.synthetic.main.fragment_settings.*
import kotlinx.android.synthetic.main.toolbar_settings.*

class SourceSettingsFragment: Fragment(), SourceAdapter.GroupItemListener {
    private val viewModel: SourceSettingsViewModel by lazy {
        ViewModelProviders.of(this).get(SourceSettingsViewModel::class.java)
    }

    private val adapter: SourceAdapter by lazy {
        SourceAdapter(ArrayList(), this)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_settings, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        group_recycler.adapter = adapter
        group_recycler.layoutManager = LinearLayoutManager(context)

        viewModel.sources.observe(this, Observer { updateSource() })
        viewModel.enabledSources.observe(this, Observer { updateSource() })

        viewModel.init(activity!!)

        updateIsChecked()

        selectAll.setOnClickListener {
            viewModel.enableAll(selectAll.isChecked)
        }

        (activity as AppCompatActivity).setSupportActionBar(toolbar_setting)
        (activity as AppCompatActivity).supportActionBar?.setDisplayShowHomeEnabled(true)
        (activity as AppCompatActivity).supportActionBar?.setDisplayHomeAsUpEnabled(true)

        toolbar_setting.setNavigationOnClickListener {
            findNavController().popBackStack()
        }
    }

    private fun updateIsChecked() {
        selectAll.isChecked = viewModel.sources.value?.size == viewModel.enabledSources.value?.size

    }

    private fun updateSource() {
        val answer = ArrayList<SourceItem>()

        viewModel.enabledSources.value?.let { enabledSource ->
            viewModel.sources.value?.let { sources ->
                for (group in sources) {
                    val gi = SourceItem(group, enabledSource.contains(group))
                    answer.add(gi)
                }

            }
        }

        adapter.sources = answer
        adapter.notifyDataSetChanged()
    }

    override fun groupSelected(group: SourceItem) {
        viewModel.enableSource(group.group, !group.isSelected)
        updateIsChecked()
    }
}