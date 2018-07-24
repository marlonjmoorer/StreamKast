package com.example.marlonmoorer.streamkast.viewModels

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.os.Environment
import com.example.marlonmoorer.streamkast.data.PlaybackHistory
import com.example.marlonmoorer.streamkast.data.SavedEpisode
import com.example.marlonmoorer.streamkast.models.DownloadedEpisodeModel
import com.example.marlonmoorer.streamkast.models.IEpisode
import com.example.marlonmoorer.streamkast.toByteSize
import com.tonyodev.fetch2.*
import com.tonyodev.fetch2core.DownloadBlock
import org.jetbrains.anko.doAsync


class LibraryViewModel:BaseViewModel(){


    private var downloads:MutableLiveData<List<Download>>
    private val models= MutableLiveData<List<DownloadedEpisodeModel>>()
    init {
        downloads= MutableLiveData()
    }

    val downloadLocation:String
        get() {
          return  "${Environment.getExternalStorageDirectory()}/podcast"
        }

    fun getDownloads():LiveData<List<DownloadedEpisodeModel>>{
        fetch.getDownloads{results->
          doAsync {
              val eps= mutableListOf<DownloadedEpisodeModel>()
              results.filter { download ->download.tag!==null}.forEach{dl->
                  val ep=repository.savedEpisodes.getById(dl.tag!!)
                  ep?.let {
                      eps.add(IEpisode.fromEpisode<DownloadedEpisodeModel>(ep).apply {
                          url=dl.file
                          size=dl.total.toByteSize()
                          downloadId=dl.id
                      })
                  }
              }
              models.postValue(eps)
          }
        }
        return models
    }

    fun getPlayBackHistory(): LiveData<List<PlaybackHistory>> {
        return repository.history.all
    }

    fun queDownload(episode: IEpisode):DownloadData{
        val request=Request(episode.url,"${downloadLocation}/${episode.guid.trim()}.mp3").apply {
            networkType=NetworkType.ALL
            tag=episode.guid
        }
        fetch.enqueue(request)
        val entry=DownloadData(request.id)
        fetch.addListener(entry,true)
        val se:SavedEpisode=IEpisode.fromEpisode(episode)
        doAsync {
            repository.savedEpisodes.insert(se.apply {
                downloadId=request.id
            })
        }
        return entry
    }
    fun  findExistingDownload(guid: String):LiveData<Download>{
        val downloadData=DownloadData()
        fetch.getDownloads {
            it.find {d->d.tag==guid}?.let { download->
                downloadData.setDownLoad(download)
                if(download.status==Status.DOWNLOADING){
                    fetch.addListener(downloadData)
                }
            }
        }
        return  downloadData
    }

    fun removeDownload(id:Int){
        fetch.remove(id)

    }
    fun clearDownloads(){
        fetch.deleteAll()
        models.postValue(emptyList())
    }


    class DownloadData(private var id:Int=-1):LiveData<Download>(),FetchListener{


        fun setDownLoad(value:Download){
            this.id=value.id
            postValue(value)
        }

        override fun onAdded(download: Download) {
        }

        override fun onCancelled(download: Download) {

        }

        override fun onCompleted(download: Download) {
            if(download.id==id) this.postValue(download)
        }

        override fun onDeleted(download: Download) {
        }

        override fun onDownloadBlockUpdated(download: Download, downloadBlock: DownloadBlock, totalBlocks: Int) {

        }

        override fun onError(download: Download) {
            if(download.id==id)
                this.postValue(download)
        }


        override fun onPaused(download: Download) {
            if(download.id==id)
                this.postValue(download)
        }

        override fun onProgress(download: Download, etaInMilliSeconds: Long, downloadedBytesPerSecond: Long) {
            if(download.id==id)
                this.postValue(download)
        }

        override fun onQueued(download: Download, waitingOnNetwork: Boolean) {
        }

        override fun onRemoved(download: Download) {
        }

        override fun onResumed(download: Download) {
        }
    }
}