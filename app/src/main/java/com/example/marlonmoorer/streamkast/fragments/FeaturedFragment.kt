package com.example.marlonmoorer.streamkast.fragments

import android.arch.lifecycle.Observer
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.marlonmoorer.streamkast.R
import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import com.example.marlonmoorer.streamkast.adapters.SectionListAdapter
import com.example.marlonmoorer.streamkast.adapters.SectionModel
import com.example.marlonmoorer.streamkast.api.models.MediaGenre
import com.example.marlonmoorer.streamkast.viewModels.DetailViewModel
import com.example.marlonmoorer.streamkast.viewModels.SectionViewModel
import kotlinx.android.synthetic.main.fragment_featured.*


/**
 * Created by marlonmoorer on 3/21/18.
 */
class FeaturedFragment : Fragment(){




    lateinit var adapter: SectionListAdapter
    lateinit var viewModel: SectionViewModel
    lateinit var detailModel: DetailViewModel
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater?.inflate(R.layout.fragment_featured,
                container, false)
    }



    override fun onAttach(context: Context?) {
        super.onAttach(context)
        viewModel = ViewModelProviders.of(activity!!).get(SectionViewModel::class.java!!)
        detailModel = ViewModelProviders.of(activity!!).get(DetailViewModel::class.java!!)
        adapter= SectionListAdapter(model = viewModel)
        viewModel.apply {
//            getSections(SectionViewModel.FEATURED)?.observe(this@FeaturedFragment, Observer{ podcast->
//                podcast?.let {
//                    adapter.prependSection(SectionModel(it,title = SectionViewModel.FEATURED) )
//                }
//            })
            MediaGenre.values().forEach { genre->
                getSections(genre.id)?.observe(this@FeaturedFragment, Observer{ podcast->
                    podcast?.let {
                        adapter.addSection(SectionModel(it,genre) )
                    }
                })
            }
            isLoading.observe(this@FeaturedFragment, Observer { loading->
                if(loading!!){
                    loadFragment(ListDialogFragment())
                }
            })
            selectedPodcast.observe(this@FeaturedFragment, Observer { podcast->
                loadFragment(DetailFragment())
                podcast?.let { detailModel.selectShow(it) }
            })
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onStart() {
        super.onStart()
        contentList.layoutManager= LinearLayoutManager(activity) as RecyclerView.LayoutManager?
        contentList.adapter=adapter

    }

    fun loadFragment(fragment: Fragment){
        fragmentManager?.let{
            it.beginTransaction()
                    .setCustomAnimations(
                            R.anim.enter_right,
                            R.anim.exit_right,
                            R.anim.enter_right,
                            R.anim.exit_right)
                    //.remove(it.findFragmentByTag("over"))
                    .replace(R.id.main,fragment,"over")
                    .addToBackStack("over")
                    .commit()
        }
    }


}