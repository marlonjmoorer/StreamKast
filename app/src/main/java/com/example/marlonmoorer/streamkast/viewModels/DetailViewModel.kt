package com.example .marlonmoorer.streamkast.viewModels

import android.arch.lifecycle.*
import com.example.marlonmoorer.streamkast.App
import com.example.marlonmoorer.streamkast.api.models.Podcast
import com.example.marlonmoorer.streamkast.api.models.rss.*


import com.example.marlonmoorer.streamkast.data.Subscription
import org.jetbrains.anko.doAsync


/**
 * Created by marlonmoorer on 3/22/18.
 */
class DetailViewModel:BaseViewModel() {

    lateinit var podcastDetails:LiveData<Channel>
    lateinit  var episodes:LiveData<List<Episode>>
    lateinit  var subscribed:LiveData<Boolean>

    private lateinit var podcast:LiveData<Podcast>
    private val selectedEpisode=MutableLiveData<Episode>()
    private var podcastId=""


    fun loadPodcast(id: String){
        podcastId=id
        podcast=repository.getPodcastById(podcastId)
        podcastDetails=Transformations.switchMap(podcast,{p->
            return@switchMap repository.parseFeed(p.feedUrl!!)
        })
        episodes=Transformations.map(podcastDetails,{channel-> channel.episodes })
        subscribed=Transformations.switchMap(podcast,{p->repository.isSubscribed(p.collectionId)})
    }


    fun getPodcast()=podcast

    fun getCurrentEpisode():LiveData<Episode> = selectedEpisode
    fun setEpisode(episode: Episode)=selectedEpisode.postValue(episode)


    val isSubbed
        get() = subscribed.value==true
    val title
        get() = podcastDetails.value?.title


    fun toggleSubscription(){
        doAsync {
            if(isSubbed){
                repository.unsubscribe(podcastId)
            }else{
                podcastDetails.value?.let{
                    repository.subscribe(Subscription().also{sub->
                        sub.title=it.title
                        sub.thumbnail=it.thumbnail
                        sub.podcastId= podcastId.toInt()
                    })
                }
            }
        }
    }
    class Factory(val id:String): ViewModelProvider.NewInstanceFactory() {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            val vm=DetailViewModel()
            //App.component?.inject(vm as BaseViewModel)
            return  vm as T
        }
    }
}