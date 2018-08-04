package com.example.marlonmoorer.streamkast.ui.activities

import android.arch.lifecycle.Observer
import android.content.Intent
import android.content.IntentFilter
import android.net.ConnectivityManager
import android.os.Bundle
import android.support.design.widget.BottomNavigationView
import android.support.design.widget.BottomSheetBehavior
import android.support.design.widget.Snackbar
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.support.v7.preference.PreferenceManager
import android.view.Menu
import android.view.MenuItem

import com.example.marlonmoorer.streamkast.ui.fragments.*
import com.example.marlonmoorer.streamkast.ui.viewModels.SubscriptionViewModel
import kotlinx.android.synthetic.main.activity_main.*
import android.view.View
import android.view.ViewGroup
import com.example.marlonmoorer.streamkast.*
import com.example.marlonmoorer.streamkast.api.models.MediaGenre
import com.example.marlonmoorer.streamkast.api.models.Podcast

import com.example.marlonmoorer.streamkast.models.EpisodeModel
import com.example.marlonmoorer.streamkast.models.IEpisode
import com.example.marlonmoorer.streamkast.ui.viewModels.MediaPlayerViewModel
import org.jetbrains.anko.contentView
import org.jetbrains.anko.longToast


class MainActivity : AppCompatActivity(),FragmentEvenListener,BottomNavigationView.OnNavigationItemSelectedListener, ConnectivityReceiverListener {



    private var subscriptionViewModel:SubscriptionViewModel?=null
    private var mediaViewModel:MediaPlayerViewModel?=null
    private val targetId:Int= R.id.main
    private lateinit var searchFragment:SearchFragment
    private lateinit var mediaPlayerFragment:MediaPlayerFragment
    private lateinit var episodeFragment: EpisodeFragment
    private  var isBound=false



    private var behavior: BottomSheetBehavior<View>?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        Utils.AppExceptionHandler(this)

        registerReceiver(ConnectivityReceiver(this),
                IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION))


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
        nav.selectedItemId= R.id.menu_browse
        searchFragment=SearchFragment()
        episodeFragment=EpisodeFragment()
        mediaPlayerFragment=supportFragmentManager.findFragmentById(R.id.mini_player) as MediaPlayerFragment


        val historyData= mediaViewModel?.restoreFromHistory()

        historyData?.observe(this, Observer { ep->
            ep?.let {
                val media:EpisodeModel=IEpisode.fromEpisode(ep)
                prepareMedia(media)
                historyData.removeObservers(this)
            }
        })

        PreferenceManager.setDefaultValues(this, R.xml.preferences, false);
    }



    override fun onNewIntent(intent: Intent?) {
        if(intent?.action== MediaService.MEDIAPLAYER){
            behavior?.run{
                state=BottomSheetBehavior.STATE_EXPANDED
                isHideable=false
            }
        }
    }


    override fun onNavigationItemSelected(item: MenuItem): Boolean {

        val fragment = when(item.itemId){
            R.id.menu_browse -> BrowseFragment()
            R.id.menu_library ->LibraryFragment()
            R.id.menu_subs ->SubscriptionFragment()
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
            R.id.action_settings -> {
                val intent = Intent(this, SettingsActivity::class.java)
                startActivity(intent)
               return true
            }
            R.id.action_open_search ->{
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

    override fun viewEpisode(episode: IEpisode) {

        episodeFragment=EpisodeFragment.newInstance(episode)
        episodeFragment.show(supportFragmentManager,"")
    }

    override fun selectGenre(genre: MediaGenre) {
        val sectionFragment = SectionFragment.newInstance(genre.id)
        addFragment(targetId,sectionFragment,true)
    }

    override fun toggleSubscription(podcast: Podcast) {
        subscriptionViewModel?.toggleSubscription(podcast)?.observe(this, Observer { subbed->
              subbed?.let {
                  val messageId=if(it) R.string.message_subscribe else R.string.message_unsubscribe
                  val message=resources.getString(messageId)
                  Snackbar.make(contentView!!,message, Snackbar.LENGTH_SHORT).show()
              }
        })
    }

    override fun playEpisode(episode: IEpisode) {
        episodeFragment.takeIf { it.isVisible }?.dismiss()
        val media:EpisodeModel= IEpisode.fromEpisode<EpisodeModel>(episode).apply{
            autoPlay = true
        }
        prepareMedia(media)
    }

    private  fun prepareMedia(media:EpisodeModel){
        if(!isBound) {
            val intent =Intent(application, MediaService::class.java).apply {
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

    override fun onConnectionChanged(isConnected: Boolean) {
        val message=if(isConnected) "Connected" else "Disconected"
        longToast(message)
    }

    override fun onBackPressed() {
        if(behavior!!.state==BottomSheetBehavior.STATE_EXPANDED){
            behavior!!.state=BottomSheetBehavior.STATE_COLLAPSED
        }else{
            super.onBackPressed()
        }
    }

}
interface FragmentEvenListener{
    fun viewEpisode(episode: IEpisode)

    fun playEpisode(episode: IEpisode)
    fun selectGenre(genre: MediaGenre)

    fun viewPodcast(podcastId:String)

    fun toggleSubscription(podcast:Podcast)
}




