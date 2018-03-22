package com.example.marlonmoorer.streamkast

import android.widget.ImageView
import com.bumptech.glide.Glide

/**
 * Created by marlonmoorer on 3/21/18.
 */
fun ImageView.load(url:String){
    Glide.with(context)
            .load(url)
            .into(this);
}