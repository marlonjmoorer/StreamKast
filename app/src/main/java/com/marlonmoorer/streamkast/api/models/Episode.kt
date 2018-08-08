package com.marlonmoorer.streamkast.api.models

import android.graphics.Bitmap
import com.marlonmoorer.streamkast.models.IEpisode
import java.util.*

class  Episode:IEpisode{
    override  var title=""
    override  var guid=""
    override var thumbnail=""
    override var duration: Int?=0
    override var description=""
    override var url=""
    var length=""
    override var author=""
}
