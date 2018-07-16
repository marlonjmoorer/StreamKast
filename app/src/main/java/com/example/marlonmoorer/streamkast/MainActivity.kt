package com.example.marlonmoorer.streamkast

import android.arch.lifecycle.Observer
import android.content.Intent
import android.os.Bundle
import android.support.design.widget.BottomNavigationView
import android.support.design.widget.BottomSheetBehavior
import android.support.design.widget.Snackbar
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem

import com.example.marlonmoorer.streamkast.fragments.*
import com.example.marlonmoorer.streamkast.viewModels.DetailViewModel
import com.example.marlonmoorer.streamkast.viewModels.SubscriptionViewModel
import kotlinx.android.synthetic.main.activity_main.*
import android.view.View
import android.view.ViewGroup
import com.example.marlonmoorer.streamkast.api.models.MediaGenre
import com.example.marlonmoorer.streamkast.api.models.Podcast
import com.example.marlonmoorer.streamkast.api.models.rss.Episode
import com.example.marlonmoorer.streamkast.listeners.IEpisodeListener
import com.example.marlonmoorer.streamkast.listeners.IGenreListener
import com.example.marlonmoorer.streamkast.listeners.IPodcastListener
import com.example.marlonmoorer.streamkast.models.EpisodeModel
import com.example.marlonmoorer.streamkast.viewModels.MediaPlayerViewModel
import org.jetbrains.anko.contentView
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.intentFor


class MainActivity : AppCompatActivity(),IPodcastListener,IEpisodeListener,IGenreListener,BottomNavigationView.OnNavigationItemSelectedListener{



    private var detailViewModel: DetailViewModel?=null
    private var subscriptionViewModel:SubscriptionViewModel?=null
    private var mediaViewModel:MediaPlayerViewModel?=null
    private val targetId:Int=R.id.main
    private lateinit var searchFragment:SearchFragment
    private lateinit var mediaPlayerFragment:MediaPlayerFragment
    private lateinit var episodeFragment: EpisodeFragment
    private  var isBound=false

    companion object {
        val PLAY_MEDIA="PLAY_MEDIA"
    }


    private var behavior: BottomSheetBehavior<View>?=null



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        detailViewModel = createViewModel()
        subscriptionViewModel= createViewModel()
        mediaViewModel=createViewModel()

        behavior=BottomSheetBehavior.from(findViewById<View>(R.id.bottom_sheet))?.apply {
            isHideable=true
            state=BottomSheetBehavior.STATE_HIDDEN

            setBottomSheetCallback(object :BottomSheetBehavior.BottomSheetCallback(){
                override fun onSlide(bottomSheet: View, slideOffset: Float) {
                    nav.animate()
                            .translationY(slideOffset*nav.height)
                            .setDuration(0)
                    mediaPlayerFragment.fadeMiniPlayer(slideOffset)
                }
                override fun onStateChanged(bottomSheet: View, newState: Int) {

                }
            })
        }

        nav.setOnNavigationItemSelectedListener(this)
        nav.selectedItemId=R.id.menu_browse
        searchFragment=SearchFragment()
        episodeFragment= EpisodeFragment()
        mediaPlayerFragment=supportFragmentManager.findFragmentById(R.id.mini_player) as MediaPlayerFragment


        val historyData= mediaViewModel?.restoreFromHistory()

        historyData?.observe(this, Observer { ep->
            ep?.let {
                val media=EpisodeModel(
                        guid = ep.guid,
                        title=ep.title,
                        author=ep.author,
                        thumbnail=ep.thumbnail,
                        url=ep.url,
                        description=ep.description)
                prepareMedia(media)
                historyData.removeObservers(this)
            }
        })
    }



    override fun onNewIntent(intent: Intent?) {
        if(intent?.action==MediaService.MEDIAPLAYER){
            behavior?.run{
                state=BottomSheetBehavior.STATE_EXPANDED
                isHideable=false
            }
        }
    }


    override fun onNavigationItemSelected(item: MenuItem): Boolean {

        val fragment = when(item.itemId){
            R.id.menu_browse-> BrowseFragment()
            R.id.menu_library->LibraryFragment()
            R.id.menu_subs->SubscriptionFragment()
            else-> Fragment()
        }
        replaceFragment(targetId,fragment)
        return true
    }


    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_settings -> true
            R.id.action_open_search->{
                this.addFragment(targetId,searchFragment)
                return  true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        unbindService(mediaViewModel)
    }

    private fun updateMargin () {
        val params=main.getLayoutParams()
        if (params is ViewGroup.MarginLayoutParams) {
            params.setMargins(params.leftMargin, params.topMargin, params.rightMargin, behavior!!.peekHeight)
            main.requestLayout()
        }
    }

    override fun viewPodcast(podcastId: String) {

        val fragment = DetailFragment.newInstance(podcastId)
        addFragment(targetId,fragment,true)
    }

    override fun viewEpisode(episode: Episode) {
        detailViewModel?.setEpisode(episode)
        this.addFragment(targetId,episodeFragment)
        //episodeFragment.show(supportFragmentManager,"")
    }

    override fun selectGenre(genre: MediaGenre) {
        val sectionFragment = SectionFragment.newInstance(genre.id)
        addFragment(targetId,sectionFragment,true)
    }

    override fun toggleSubscription(podcast: Podcast) {
        subscriptionViewModel?.toggleSubscription(podcast.collectionId)?.observe(this, Observer {subbed->
              subbed?.let {
                  podcast.subscribed=subbed
                  val messageId=if(it) R.string.message_subscribe else R.string.message_unsubscribe
                  val message=resources.getString(messageId)
                  Snackbar.make(contentView!!,message, Snackbar.LENGTH_SHORT).show()
                  podcast.notifyPropertyChanged(BR.subscribed)
              }
        })
    }

    override fun playEpisode(episode: Episode) {

        episodeFragment.takeIf { it.isVisible }?.dismiss()

        val media=EpisodeModel(
                guid = episode.guid,
                title=episode.title,
                author=episode.author,
                thumbnail=episode.thumbnail,
                url=episode.mediaUrl,
                description=episode.description,
                autoPlay = true)

        prepareMedia(media)

    }

    private  fun prepareMedia(media:EpisodeModel){
        if(!isBound) {
            val intent =Intent(application,MediaService::class.java).apply {
                putExtra(MediaService.MEDIA,media)
            }
            application.startService(intent)
            bindService(intent,mediaViewModel,AppCompatActivity.BIND_AUTO_CREATE)
            isBound=true
        }else{
            val broadcastIntent = Intent(MediaService.PLAY_AUDIO).apply{
                putExtra(MediaService.MEDIA,media)
            }
            application.sendBroadcast(broadcastIntent)
        }
        behavior?.run{
            state=BottomSheetBehavior.STATE_COLLAPSED
            isHideable=false
        }
        updateMargin()

    }

    override fun onBackPressed() {
        if(behavior!!.state==BottomSheetBehavior.STATE_EXPANDED){
            behavior!!.state=BottomSheetBehavior.STATE_COLLAPSED
        }else{
            super.onBackPressed()
        }
    }

}





