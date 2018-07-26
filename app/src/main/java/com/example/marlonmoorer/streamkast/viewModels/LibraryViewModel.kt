package com.example.marlonmoorer.streamkast.viewModels

import android.app.DownloadManager
import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.Transformations
import android.content.Context
import android.content.SharedPreferences
import android.database.ContentObserver
import android.database.Cursor
import android.media.MediaPlayer
import android.net.Uri
import android.os.Environment
import android.os.FileObserver
import android.os.Handler


import com.example.marlonmoorer.streamkast.data.PlaybackHistory
import com.example.marlonmoorer.streamkast.data.SavedEpisode
import com.example.marlonmoorer.streamkast.getInt
import com.example.marlonmoorer.streamkast.models.DownloadedEpisodeModel
import com.example.marlonmoorer.streamkast.models.IEpisode
import com.example.marlonmoorer.streamkast.toByteSize
import org.jetbrains.anko.AnkoAsyncContext


import org.jetbrains.anko.doAsync
import java.io.File
import java.util.*


class LibraryViewModel:BaseViewModel(){


    private var downloads:MutableLiveData<List<DownloadedEpisodeModel>>
    private val models= MutableLiveData<List<DownloadedEpisodeModel>>()

    init {
        downloads= MutableLiveData()
    }

    val prefs
        get() = context.getSharedPreferences(context.packageName,0)

    val downloadLocation:String
        get() {
          return prefs.getString("location","${Environment.getExternalStorageDirectory().absolutePath}/podcast")
        }


    fun getPlayBackHistory(): LiveData<List<PlaybackHistory>> {
        return repository.history.all
    }

    fun queDownload(episode: IEpisode):LiveData<DownloadInfo>{


        val data= DowndLoadLiveData(context)
        val request= DownloadManager.Request(Uri.parse(episode.url)).run {
            setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI or DownloadManager.Request.NETWORK_MOBILE);
            setAllowedOverRoaming(false)
            setTitle(episode.title)
            setVisibleInDownloadsUi(true)
            setDestinationInExternalPublicDir(downloadLocation,"${episode.guid}.mp3")
        }
        val savedEpisode= IEpisode.fromEpisode<SavedEpisode>(episode)
        savedEpisode.downloadId=data.sendRequest(request)
        savedEpisode.url = createPath(episode.guid)
        doAsync {
            repository.savedEpisodes.insert(savedEpisode)
        }
        return  data
    }

    private fun createPath(name:String):String{
        return "${downloadLocation}/${name}.mp3"
    }

    fun getDownloaded(): LiveData<List<SavedEpisode>> {


        return repository.savedEpisodes.all
    }

    fun findPreviousDownload(guid:String): LiveData<DownloadInfo> {
        return Transformations.switchMap(repository.savedEpisodes.getById(guid),{ep->
           ep?.let{
               if(File(ep.url).exists()){
                    return@switchMap DowndLoadLiveData(context).apply {
                           setDownloadId(ep.downloadId)
                    }
               }else{
                   doAsync {
                       repository.savedEpisodes.delete(ep)
                   }
                  return@switchMap null
               }

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
    fun clearDownloads(){
        models.postValue(emptyList())
    }

    data class DownloadInfo(var progress:Int=0,var status:Int=0)


    class DowndLoadLiveData(val context: Context):LiveData<DownloadInfo>(){


        private val downloadManager:DownloadManager
        private var downloadId:Long?=null
        private var query:DownloadManager.Query?=null
        private val model:DownloadInfo



        init {
            downloadManager= context.getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
            model= DownloadInfo()
        }

        fun setDownloadId(id: Long){
            downloadId=id
            query=DownloadManager.Query().apply {
                setFilterById(id)
            }
            checkStatus()
            val requestUri=Uri.parse( "content://downloads/my_downloads/$id" )
            context.contentResolver.registerContentObserver(requestUri, true,observer)

        }

        private fun checkStatus(){
            downloadId?.let {
                val cursor= downloadManager.query(query)
                if(cursor.moveToFirst()){
                    val bytes=cursor.getInt(DownloadManager.COLUMN_BYTES_DOWNLOADED_SO_FAR).toDouble()
                    val total=cursor.getInt(DownloadManager.COLUMN_TOTAL_SIZE_BYTES).toDouble()
                    model.run {
                        status=cursor.getInt(DownloadManager.COLUMN_STATUS)
                        progress= ((bytes/total*100)).toInt()
                    }
                    postValue(model)
                }
                when(model.status){
                    DownloadManager.STATUS_SUCCESSFUL,
                    DownloadManager.STATUS_FAILED-> context.contentResolver.unregisterContentObserver(observer)
                }
            }
        }

        fun sendRequest(request:DownloadManager.Request):Long{
            val id=downloadManager.enqueue(request)
            setDownloadId(id)
            return id
        }


        private val observer=object :ContentObserver(Handler()){
            override fun onChange(selfChange: Boolean, uri: Uri?) {
                super.onChange(selfChange, uri)
                if (uri?.encodedPath?.contains("$downloadId")==true)
                    checkStatus()
            }
        }


    }
}