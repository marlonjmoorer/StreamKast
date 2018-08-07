package com.example.marlonmoorer.streamkast.ui.viewModels

import android.app.Application
import android.app.DownloadManager
import android.arch.lifecycle.AndroidViewModel
import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.Transformations
import android.content.Context
import android.content.SharedPreferences
import android.database.ContentObserver
import android.net.Uri
import android.os.Environment
import android.os.Handler
import android.support.v7.preference.PreferenceManager
import com.example.marlonmoorer.streamkast.*
import com.example.marlonmoorer.streamkast.api.Repository


import com.example.marlonmoorer.streamkast.data.PlaybackHistory
import com.example.marlonmoorer.streamkast.data.SavedEpisode
import com.example.marlonmoorer.streamkast.models.DownloadedEpisodeModel
import com.example.marlonmoorer.streamkast.models.IEpisode
import io.reactivex.subjects.PublishSubject


import org.jetbrains.anko.doAsync
import java.io.File
import javax.inject.Inject


class LibraryViewModel(val app:Application):AndroidViewModel(app){

    @Inject
    lateinit var repository: Repository
    private var downloads:MutableLiveData<List<DownloadedEpisodeModel>>
    private lateinit var preferences:SharedPreferences
    private lateinit var  downloadManager:DownloadManager
    private lateinit var  key_wifi:String
    private lateinit var  key_location:String
    private lateinit  var rxRegex: String

    private val observer=object :ContentObserver(Handler()){
        override fun onChange(selfChange: Boolean, uri: Uri?) {
            super.onChange(selfChange, uri)
            uri?.lastPathSegment?.toLongOrNull()?.let {
                checkStatus(it)
            }
        }
    }



    init {
        downloads= MutableLiveData()
        App.component?.inject(this)
        app.run {
            preferences= PreferenceManager.getDefaultSharedPreferences(this)
            downloadManager=getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
            contentResolver.registerContentObserver(Uri.parse( getString(R.string.downloadUri)),true,observer)
            key_location=getString(R.string.key_storage)
            key_wifi=getString(R.string.key_wifi_only)
            rxRegex=getString(R.string.RxReplaceSpecial)
        }
    }


    val downloadLocation:String
        get() {
          return preferences.getString(key_location,"${Environment.getExternalStorageDirectory()}/podcast")
        }

    val wifiOnly:Boolean
        get() = preferences.getBoolean(key_wifi,true)

    val downloadChangeEvent=PublishSubject.create<DownloadInfo>()



    private fun checkStatus(id: Long):DownloadInfo{
        val query=DownloadManager.Query()
        query. setFilterById(id)
        val info= DownloadInfo(id)
        val cursor= downloadManager.query(query)
        if(cursor.moveToFirst()){
            val bytes=cursor.getInt(DownloadManager.COLUMN_BYTES_DOWNLOADED_SO_FAR).toDouble()
            val total=cursor.getInt(DownloadManager.COLUMN_TOTAL_SIZE_BYTES).toDouble()
            val location= cursor.getString(DownloadManager.COLUMN_LOCAL_URI)
            info.apply{
                status=cursor.getInt(DownloadManager.COLUMN_STATUS)
                progress= ((bytes/total*100)).toInt()
                path=location?:""
            }
            downloadChangeEvent.onNext(info)
        }
        return info
    }



    fun getPlayBackHistory(): LiveData<List<PlaybackHistory>> {
        return repository.history.all
    }

    fun queDownload(episode: IEpisode):Long{

        val uri=Uri.fromFile(File(createPath(episode.guid)))
        val request= DownloadManager.Request(Uri.parse(episode.url)).run {
            setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI or DownloadManager.Request.NETWORK_MOBILE);
            setAllowedOverRoaming(false)
            setTitle(episode.title)
            setVisibleInDownloadsUi(true)
            setDestinationUri(uri)
        }
        val savedEpisode= IEpisode.fromEpisode<SavedEpisode>(episode)
        val downloadId=downloadManager.enqueue(request)
        checkStatus(downloadId)
        savedEpisode.downloadId= downloadId
        savedEpisode.url =uri.toString()
        doAsync {
            repository.savedEpisodes.insert(savedEpisode)
        }
        return downloadId
    }

    private fun createPath(name:String):String{
        return  app.getString(R.string.mp3Format,downloadLocation,name.replace(Regex(rxRegex),""))
    }

    fun getDownloaded(): LiveData<List<DownloadedEpisodeModel>> {
        return Transformations.map(repository.savedEpisodes.all,{savedEpisodes->
           return@map savedEpisodes.map {
                IEpisode.fromEpisode<DownloadedEpisodeModel>(it).apply {
                    val file= File(createPath(guid))
                    size= file.length().toByteSize()
                    downloadId=it.downloadId
                    progress=100
                    status= checkStatus(it.downloadId).status
                }
            }
        })
    }

    fun findPreviousDownload(guid:String):LiveData<Long> {
        return Transformations.map(repository.savedEpisodes.getById(guid),{ep->
           ep?.let{
               checkStatus(ep.downloadId)
               return@map ep.downloadId
           }
        })
    }



    fun removeDownload(id:String){
        doAsync{
            repository.savedEpisodes.removeEpisode(id)
            val file=File(createPath(id))
            if(file.exists()){
                file.delete()
            }
        }
    }

    fun removeHistory(id:String) {
        doAsync{
            repository.history.removeFromHistory(id)
        }
    }

    data class DownloadInfo(var id:Long,var progress:Int=0,var status:Int=0,var path:String="")

    override fun onCleared() {
        super.onCleared()
        getApplication<App>().contentResolver.unregisterContentObserver(observer)
    }
}