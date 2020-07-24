package com.developer.ivan.coreapp.ui.main.fragments

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.navigation.ui.NavigationUI
import androidx.transition.TransitionInflater
import com.developer.ivan.coreapp.R
import com.developer.ivan.coreapp.core.EventObserver
import com.developer.ivan.coreapp.core.registerReceiver
import com.developer.ivan.coreapp.databinding.FragmentDocumentDetailBinding
import com.developer.ivan.coreapp.ui.main.MainActivity
import com.developer.ivan.coreapp.ui.main.MainViewModel
import com.developer.ivan.coreapp.ui.main.ViewModelFactory
import com.developer.ivan.coreapp.ui.pdfviewer.PDFViewerActivity
import com.google.android.material.appbar.AppBarLayout
import kotlinx.coroutines.ExperimentalCoroutinesApi
import javax.inject.Inject

@ExperimentalCoroutinesApi
class DocumentDetailFragment : Fragment() {

    private lateinit var binding: FragmentDocumentDetailBinding

    @Inject
    lateinit var mViewModel: MainViewModel

    @Inject
    lateinit var documentViewModelFactory: ViewModelFactory
/*
    private val mViewModel
            by navGraphViewModels<MainViewModel>(R.id.main_nav_xml) { documentViewModelFactory }*/

    private val args: DocumentDetailFragmentArgs by navArgs()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sharedElementEnterTransition = TransitionInflater.from(context).inflateTransition(android.R.transition.move)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        (activity as MainActivity).documentsSubComponent.inject(this)
//        mViewModel = (activity as MainActivity).documentsSubComponent.viewModel
//        mViewModel = (activity as MainActivity).mViewModel
//        mViewModel = ViewModelProvider(this,documentViewModelFactory)[MainViewModel::class.java]

//        Log.i("VIEWMODEL",mViewModel.toString())

        Log.i("VIEWMODEL",mViewModel.toString())

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_document_detail, container, false)
        binding.viewmodel = mViewModel
        binding.lifecycleOwner = activity
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        configureToolbar()
        configureAppBarScroll()
        configureListeners()
        configureObservers()

        mViewModel.getArticle(args.uiarticle.id)

    }

    override fun onDestroy() {
        super.onDestroy()

    }

    private fun configureToolbar() {
        binding.toolbar.apply {
            transitionName = args.uiarticle.id.toString()
            title = args.uiarticle.title
        }

        (activity as AppCompatActivity).setSupportActionBar(binding.toolbar)

        NavigationUI.setupActionBarWithNavController(
            activity as AppCompatActivity,
            findNavController()
        )


    }

    private fun configureListeners(){

        binding.btnDownload.setOnClickListener {
            mViewModel.download()
        }
    }

    private fun configureObservers(){
        mViewModel.navigation.observe(viewLifecycleOwner,EventObserver{navEvent->

            when(navEvent){
                is MainViewModel.NavigationEvent.ToPDFView -> startActivity(Intent(context,PDFViewerActivity::class.java).apply {
                    putExtra(PDFViewerActivity.ARG_PDF_URI,navEvent.uri)
                    flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_NO_HISTORY
                })
            }

        })
    }

    private fun configureAppBarScroll() {

        binding.appBarLayout.addOnOffsetChangedListener(AppBarLayout.OnOffsetChangedListener { appBarLayout, verticalOffset ->
            val seekPosition = -verticalOffset / appBarLayout.totalScrollRange.toFloat()
            binding.motionLayout.progress = seekPosition
            binding.motionBody.progress = seekPosition
        })
    }
}