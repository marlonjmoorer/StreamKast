package com.example.marlonmoorer.streamkast

import android.databinding.BaseObservable
import android.databinding.Bindable
import android.graphics.Bitmap
import java.time.Duration

class MediaPlayerModel:BaseObservable() {

    private  var elapsed=0
    private  var duration=0
    private  var title=""
    private  var author=""
    private  var image:Bitmap?=null


    var Elapsed
        @Bindable get()=elapsed
        set(value){
            elapsed=value
            notifyPropertyChanged(BR.elapsed)
            notifyPropertyChanged(BR.currentTime)
        }
    var Title
        @Bindable get()=title
        set(value){
            title=value
            notifyPropertyChanged(BR.title)
        }

    var Duration
        @Bindable get()=duration
        set(value){
            duration=value
            notifyPropertyChanged(BR.duration)
            notifyPropertyChanged(BR.durationTime)
        }
    var Author
        @Bindable get()=author
        set(value){
            author=value
            notifyPropertyChanged(BR.author)
        }
    var Image
        @Bindable get()=image
        set(value){
            image=value
            notifyPropertyChanged(BR.image)
        }

    val currentTime
        @Bindable get() = elapsed.toTime()
    val durationTime
        @Bindable get() = duration.toTime()
}