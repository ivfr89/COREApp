package com.developer.ivan.coreapp.ui.main.fragments

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.SearchView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.NavigationUI.setupActionBarWithNavController
import com.developer.ivan.coreapp.R
import com.developer.ivan.coreapp.core.EventObserver
import com.developer.ivan.coreapp.core.writeTextFlow
import com.developer.ivan.coreapp.databinding.FragmentDocumentListBinding
import com.developer.ivan.coreapp.ui.adapters.ArticleListAdapter
import com.developer.ivan.coreapp.ui.main.MainActivity
import com.developer.ivan.coreapp.ui.main.MainViewModel
import com.developer.ivan.coreapp.ui.main.MainViewModel.NavigationEvent
import com.developer.ivan.coreapp.ui.main.UIArticle
import com.developer.ivan.coreapp.ui.main.ViewModelFactory
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@ExperimentalCoroutinesApi
@FlowPreview
class DocumentListFragment : Fragment() {

    private lateinit var menu: Menu

    @Inject
    lateinit var mViewModel: MainViewModel

    @Inject
    lateinit var documentViewModelFactory: ViewModelFactory
    /*@Inject
    lateinit var documentViewModelFactory: ViewModelFactory


    private val mViewModel
            by navGraphViewModels<MainViewModel>(R.id.main_nav_xml) { documentViewModelFactory }*/

    private lateinit var binding: FragmentDocumentListBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        setHasOptionsMenu(true)


    }


    override fun onAttach(context: Context) {
        super.onAttach(context)
        (activity as MainActivity).documentsSubComponent.inject(this)

//        mViewModel = (activity as MainActivity).mViewModel
//        mViewModel = ViewModelProvider(this,documentViewModelFactory)[MainViewModel::class.java]
//        mViewModel = (activity as MainActivity).documentsSubComponent.viewModel

        Log.i("VIEWMODEL",mViewModel.toString())

    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_document_list, container, false)
        binding.lifecycleOwner = activity
        binding.viewModel = mViewModel


        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        configureToolbar()
        configureUI()
        configureEvents()


    }

    private fun configureToolbar() {
        (activity as AppCompatActivity).setSupportActionBar(binding.toolbar)

        setupActionBarWithNavController(
            activity as AppCompatActivity,
            findNavController()
        )
    }

    private fun configureEvents() {

        mViewModel.navigation.observe(viewLifecycleOwner, EventObserver { event ->

            when (event) {
                is NavigationEvent.ToDetail -> {

                    val extras = FragmentNavigatorExtras(event.title to event.arg.id.toString())
                    val action =
                        DocumentListFragmentDirections.actionDocumentListFragmentToDocumentDetailFragment(
                            event.arg
                        )

                    findNavController().navigate(action,extras)
                }
            }
        })

    }


    private fun configureUI() {
        with(binding) {
            rcvDocumentList.adapter = ArticleListAdapter(::onClickOnItem)
        }
    }

    private fun onClickOnItem(uiArticle: UIArticle, title: TextView) {
        mViewModel.navigateTo(NavigationEvent.ToDetail(uiArticle,title))
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.search_menu, menu)
        this.menu = menu

        val mySearchView = menu.findItem(R.id.searchView)

        val actionView = mySearchView.actionView as SearchView

        actionView
            .writeTextFlow()
            .debounce(600)
            .onEach { resultString ->

                if(!resultString.isNullOrEmpty())
                    mViewModel.searchArticles(resultString, false)

            }.launchIn(viewLifecycleOwner.lifecycleScope)

    }

}