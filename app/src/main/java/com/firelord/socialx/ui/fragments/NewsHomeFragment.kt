package com.firelord.socialx.ui.fragments

import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.core.widget.addTextChangedListener
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.firelord.socialx.R
import com.firelord.socialx.adapters.NewsAdapter
import com.firelord.socialx.databinding.FragmentNewsHomeBinding
import com.firelord.socialx.ui.MainActivity
import com.firelord.socialx.ui.NewsViewModel
import com.firelord.socialx.util.Constants.Companion.SEARCH_TIME_DELAY
import com.firelord.socialx.util.Resource
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.Job
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class NewsHomeFragment : Fragment() {

    private lateinit var binding: FragmentNewsHomeBinding

    lateinit var viewModel: NewsViewModel

    lateinit var newsAdapter: NewsAdapter

    val TAG = "AllNewsFrag"
    // init firebase auth
    private lateinit var firebaseAuth: FirebaseAuth

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_news_home, container, false)

        // Toolbar
        binding.tbNewsHome.inflateMenu(R.menu.logout)
        setHasOptionsMenu(true)

        // init firebase auth
        firebaseAuth = FirebaseAuth.getInstance()

        binding.tbNewsHome.setOnMenuItemClickListener{
            when(it.itemId){
                R.id.logout -> {
                    firebaseAuth.signOut()
                    view?.findNavController()?.navigate(R.id.action_newsHomeFragment_to_homeFragment)
                    true
                }
                else -> false
            }
        }

        viewModel = (activity as MainActivity).viewModel
        setupRecyclerView()

        var job: Job? = null
        binding.etSearchNews.addTextChangedListener{ editable ->
            job?.cancel()
            job = MainScope().launch {
                delay(SEARCH_TIME_DELAY)
                editable?.let {
                    if(editable.toString().isNotEmpty()) {
                        viewModel.searchNews(editable.toString())
                    }
                    else{
                        // when user has clearned search query show the
                        // default news list in rv
                        allNews()
                    }
                }
            }
        }

        // Home showing news
        allNews()

        // search showing news
        viewModel.searchNews.observe(viewLifecycleOwner, Observer { response ->
            when(response){
                is Resource.Success -> {
                    hideProgressBar()
                    response.data?.let { newsResponse ->
                        newsAdapter.differ.submitList(newsResponse.articles)
                    }
                }
                is Resource.Error -> {
                    hideProgressBar()
                    response.message?.let { message ->
                        Log.e(TAG, "An error occured: $message")

                    }
                }
                is Resource.Loading -> {
                    showProgressBar()
                }
            }
        })

        // the default return
        return binding.root
    }

    // Home showing news
    private fun allNews(){
        viewModel.allNews.observe(viewLifecycleOwner, Observer { response ->
            when(response){
                is Resource.Success -> {
                    hideProgressBar()
                    response.data?.let { newsResponse ->
                        newsAdapter.differ.submitList(newsResponse.articles)
                    }
                }
                is Resource.Error -> {
                    hideProgressBar()
                    response.message?.let { message ->
                        Log.e(TAG, "An error occured: $message")

                    }
                }
                is Resource.Loading -> {
                    showProgressBar()
                }
            }

        })
    }

    private fun hideProgressBar(){
        binding.progressBar.visibility = View.INVISIBLE
    }

    private fun showProgressBar(){
        binding.progressBar.visibility = View.VISIBLE
    }

    private fun setupRecyclerView(){
        newsAdapter = NewsAdapter()
        binding.rvAllNews.apply {
            adapter = newsAdapter
            layoutManager = LinearLayoutManager(activity)
        }
    }
}