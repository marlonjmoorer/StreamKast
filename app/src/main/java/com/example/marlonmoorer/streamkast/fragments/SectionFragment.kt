package com.example.marlonmoorer.streamkast.fragments

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.GridLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.example.marlonmoorer.streamkast.R
import com.example.marlonmoorer.streamkast.adapters.PodcastListAdapter
import com.example.marlonmoorer.streamkast.adapters.SectionAdapter
import com.example.marlonmoorer.streamkast.api.models.MediaGenre
import com.example.marlonmoorer.streamkast.viewModels.BrowseViewModel
import kotlinx.android.synthetic.main.fragment_browse.view.*
import kotlinx.android.synthetic.main.fragment_section.view.*

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val KEY = "SECTION_NAME"


class SectionFragment : Fragment() {

    private var genreId: String? = null
    private var listener: OnFragmentInteractionListener? = null
    lateinit var viewModel: BrowseViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            genreId= it.getString(KEY)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view=inflater.inflate(R.layout.fragment_section, container, false)
        view.section_title.text=MediaGenre.parse(genreId!!)?.displayname
        viewModel.getSection(genreId!!,20)?.observe(this, Observer {podcast->
             podcast?.let {
                var adapter=  SectionAdapter(it)
                 view.section_items.layoutManager= GridLayoutManager(activity,2)
                 view.section_items.adapter=adapter
                 view.section_items.setNestedScrollingEnabled(false);
             }

        })
        return view
    }



    override fun onAttach(context: Context) {
        super.onAttach(context)
        viewModel = ViewModelProviders.of(activity!!).get(BrowseViewModel::class.java!!)
    }

    override fun onDetach() {
        super.onDetach()
        listener = null
    }

    interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        fun onFragmentInteraction(uri: Uri)
    }

    companion object {
        @JvmStatic
        fun newInstance(genreId:String) =
                SectionFragment().apply {
                    arguments = Bundle().apply {
                        putString(KEY,genreId)
                    }
                }
    }
}
