package com.example.marlonmoorer.streamkast.ui.fragments

import android.app.DownloadManager
import android.arch.lifecycle.Observer
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.support.annotation.RequiresApi
import android.support.design.widget.BottomSheetDialogFragment
import android.support.v7.preference.PreferenceManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.marlonmoorer.streamkast.R

import com.example.marlonmoorer.streamkast.createViewModel
import com.example.marlonmoorer.streamkast.databinding.FragmentEpisodeBinding

import com.example.marlonmoorer.streamkast.models.IEpisode
import com.example.marlonmoorer.streamkast.setIcon
import com.example.marlonmoorer.streamkast.ui.activities.FragmentEvenListener
import com.example.marlonmoorer.streamkast.ui.viewModels.LibraryViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import org.jetbrains.anko.cancelButton
import org.jetbrains.anko.okButton
import org.jetbrains.anko.support.v4.alert
import org.jetbrains.anko.yesButton


class EpisodeFragment: BottomSheetDialogFragment() {


    lateinit var episode: IEpisode
    lateinit var binding:FragmentEpisodeBinding
    private lateinit var libraryViewModel: LibraryViewModel
    private  var listener: FragmentEvenListener?=null
    private  var downloadId:Long?=null

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        listener = context as FragmentEvenListener
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            episode=it.getSerializable(KEY) as IEpisode
        }

    }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentEpisodeBinding.inflate(inflater)
        binding.episode=episode
        binding.actionButtonPlay.setOnClickListener {
            listener?.playEpisode(episode)
        }
        binding.actionButtonDownload.setOnClickListener {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M&& !checkPermission()) {
              return@setOnClickListener
            }
            if (libraryViewModel.wifiOnly){
                alert(getString(R.string.wifi_only_warning_message)){
                    yesButton {
                        startDownload()
                    }
                    cancelButton {
                    }
                }.show()

            }else{
                startDownload()
            }
        }
        return  binding.root
    }

    private fun startDownload(){
        libraryViewModel.queDownload(episode)
        this.dismiss()
    }

    @RequiresApi(Build.VERSION_CODES.M)
    private fun checkPermission():Boolean{
         return activity?.checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)== PackageManager.PERMISSION_GRANTED
    }


    private var disposeable: Disposable?=null

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        libraryViewModel=createViewModel()
        libraryViewModel.findPreviousDownload(episode.guid).observe(this, Observer {id->
            downloadId=id
        })
        disposeable=libraryViewModel.downloadChangeEvent
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .filter {info->info.id==downloadId }
                .subscribe{
                    when(it?.status){
                        DownloadManager.STATUS_PENDING->{
                            binding.actionButtonDownload.apply{
                                text = getString(R.string.pending_text)
                                setIcon(0)
                                isEnabled=false
                            }
                        }
                        DownloadManager.STATUS_RUNNING->{
                            binding.actionButtonDownload.apply{
                                text = getString(R.string.downloading_format,it.progress.toFloat())
                                setIcon(0)
                                isEnabled=false
                            }
                        }
                        DownloadManager.STATUS_SUCCESSFUL->{
                            binding.actionButtonDownload.apply {
                                text=getString(R.string.downloaded_text)
                                setIcon(R.drawable.icons8_check_mark_symbol)
                                isEnabled=false
                            }
                            disposeable?.dispose()
                        }
                    }
                }
    }
    companion object {
        private const val KEY = "EPISODE"
        @JvmStatic
        fun newInstance(episode: IEpisode) =
               EpisodeFragment().apply {
                    arguments = Bundle().apply {
                        putSerializable(KEY,episode)
                    }
                }
    }

}