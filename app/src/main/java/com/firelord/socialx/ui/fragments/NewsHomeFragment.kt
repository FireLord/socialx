package com.firelord.socialx.ui.fragments

import android.os.Bundle
import android.util.Log
import android.view.*
import android.view.Menu
import androidx.fragment.app.Fragment
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.firelord.socialx.R
import com.firelord.socialx.adapters.NewsAdapter
import com.firelord.socialx.databinding.FragmentNewsHomeBinding
import com.firelord.socialx.ui.MainActivity
import com.firelord.socialx.ui.NewsViewModel
import com.firelord.socialx.util.Resource
import com.google.firebase.auth.FirebaseAuth

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
        setHasOptionsMenu(true)
        // init firebase auth
        firebaseAuth = FirebaseAuth.getInstance()

        viewModel = (activity as MainActivity).viewModel
        setupRecyclerView()

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
        return binding.root
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
    override fun onCreateOptionsMenu(menu: Menu,inflater: MenuInflater) {
        inflater.inflate(R.menu.logout, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.logout -> {
                firebaseAuth.signOut()
                view?.findNavController()?.navigate(R.id.action_newsHomeFragment_to_homeFragment)
            }
        }
        return true
    }
}