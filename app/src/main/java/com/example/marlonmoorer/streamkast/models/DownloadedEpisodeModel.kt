package com.example.marlonmoorer.streamkast.models

import android.app.DownloadManager
import android.databinding.BaseObservable
import android.databinding.Bindable
import android.databinding.BindingAdapter
import android.widget.TextView
import com.example.marlonmoorer.streamkast.toDateString
import java.util.*

class DownloadedEpisodeModel: BaseObservable(), IEpisode {
    override var title =""
    override var guid = ""
    override var thumbnail = ""
    override var duration: Int? = 0
    override var description = ""
    override var url=""
    override var author = ""
    var size=""
    var downloadId:Long?=null

    @Bindable
    var status:Int=DownloadManager.STATUS_RUNNING
    @Bindable
    var progress: Int = 0


}