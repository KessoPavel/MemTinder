package com.radiance.memtinder.ui.textViewer

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.radiance.memtinder.R
import kotlinx.android.synthetic.main.fragment_text_viewer.*

class TextViewer : Fragment() {
    companion object {
        fun newInstance(text: String): TextViewer {
            val fragment = TextViewer()
            fragment.text = text
            return fragment
        }
    }

    private var text = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_text_viewer, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        textView.text = text
        close.setOnClickListener{
            activity?.supportFragmentManager?.let {
                it.beginTransaction().remove(this).commit()
            }
        }
        text_view_main.setOnClickListener{

        }
    }

}
