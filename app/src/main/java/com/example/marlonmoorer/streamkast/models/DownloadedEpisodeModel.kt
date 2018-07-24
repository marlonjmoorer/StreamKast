package com.example.marlonmoorer.streamkast.models

import android.databinding.BaseObservable

class DownloadedEpisodeModel: BaseObservable(), IEpisode {
    override var title =""
    override var guid = ""
    override var thumbnail = ""
    override var duration: Int? = 0
    override var description = ""
    override var url=""
    override var author = ""
    var size=""
    var downloadId:Int?=null
}