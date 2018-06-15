package com.example.marlonmoorer.streamkast.listeners

import com.example.marlonmoorer.streamkast.api.models.MediaItem

interface OnSubscribeListener {
    fun toggleSubscription(podcast:MediaItem)
}