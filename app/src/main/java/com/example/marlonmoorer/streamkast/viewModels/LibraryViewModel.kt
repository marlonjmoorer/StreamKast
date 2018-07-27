package com.example.marlonmoorer.streamkast.viewModels

import android.app.Application
import android.app.DownloadManager
import android.arch.lifecycle.AndroidViewModel
import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.Transformations
import android.content.ContentResolver
import android.content.Context
import android.content.SharedPreferences
import android.content.pm.ApplicationInfo
import android.database.ContentObserver
import android.database.Cursor
import android.media.MediaPlayer
import android.net.Uri
import android.os.Environment
import android.os.FileObserver
import android.os.Handler
import android.provider.MediaStore
import com.example.marlonmoorer.streamkast.App
import com.example.marlonmoorer.streamkast.api.Repository


import com.example.marlonmoorer.streamkast.data.PlaybackHistory
import com.example.marlonmoorer.streamkast.data.SavedEpisode
import com.example.marlonmoorer.streamkast.getInt
import com.example.marlonmoorer.streamkast.getString
import com.example.marlonmoorer.streamkast.models.DownloadedEpisodeModel
import com.example.marlonmoorer.streamkast.models.IEpisode
import com.example.marlonmoorer.streamkast.toByteSize
import io.reactivex.subjects.PublishSubject
import org.jetbrains.anko.AnkoAsyncContext


import org.jetbrains.anko.doAsync
import java.io.File
import java.util.*
import javax.inject.Inject


class LibraryViewModel(app:Application):AndroidViewModel(app){

    @Inject
    lateinit var repository: Repository
    private var downloads:MutableLiveData<List<DownloadedEpisodeModel>>
    private lateinit var preferences:SharedPreferences
    private lateinit var  downloadManager:DownloadManager
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
            preferences=getSharedPreferences(packageName,0)
            downloadManager=getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
            contentResolver.registerContentObserver(Uri.parse( "content://downloads/my_downloads/"),true,observer)
        }
    }


    val downloadLocation:String
        get() {
          return preferences.getString("location","${Environment.getExternalStorageDirectory()}/podcast")
        }


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

        val request= DownloadManager.Request(Uri.parse(episode.url)).run {
            setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI or DownloadManager.Request.NETWORK_MOBILE);
            setAllowedOverRoaming(false)
            setTitle(episode.title)
            setVisibleInDownloadsUi(true)
            setDestinationInExternalPublicDir("/podcast","${episode.guid.replace("[^a-zA-Z0-9]+","")}.mp3")
        }
        val savedEpisode= IEpisode.fromEpisode<SavedEpisode>(episode)
        val downloadId=downloadManager.enqueue(request)
        val meta= checkStatus(downloadId)
        savedEpisode.downloadId= downloadId
        savedEpisode.url =meta.path
        doAsync {
            repository.savedEpisodes.insert(savedEpisode)
        }
        return downloadId
    }

    private fun createPath(name:String):String{
        return "${downloadLocation}/${name.replace("[^a-zA-Z0-9]+","")}.mp3"
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

    data class DownloadInfo(var id:Long,var progress:Int=0,var status:Int=0,var path:String="")

}