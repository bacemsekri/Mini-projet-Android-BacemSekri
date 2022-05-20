package com.mbds.bmp.newsletter.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.StringRes
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.BaseTransientBottomBar
import com.google.android.material.snackbar.Snackbar
import com.mbds.bmp.newsletter.MainActivity
import com.mbds.bmp.newsletter.R
import com.mbds.bmp.newsletter.adapters.ArticleAdapter
import com.mbds.bmp.newsletter.database.ArticleRoomDatabase
import com.mbds.bmp.newsletter.databinding.FragmentArticlesBinding
import com.mbds.bmp.newsletter.listener.ArticlesScrollListener
import com.mbds.bmp.newsletter.model.Article
import com.mbds.bmp.newsletter.services.ArticleService
import com.mbds.bmp.newsletter.services.FavoriteService
import com.mbds.bmp.newsletter.tools.isOnline
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ArticlesFragment : Fragment() {

    private var articleService: ArticleService? = null
    private lateinit var binding: FragmentArticlesBinding
    private val articleAdapter = ArticleAdapter(mutableListOf(), this)
    private val articlesScrollListener = ArticlesScrollListener()
    private lateinit var favoriteService: FavoriteService

    private var page = 1
    private var isLoading = false

    private var errorSnackBar: Snackbar? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            articleService = it.getParcelable(ARG_ARTICLE_SERVICE) as ArticleService?
        }

        activity?.setTitle(articleService?.getTitleId() ?: R.string.results)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentArticlesBinding.inflate(inflater, container, false)

        favoriteService =
            FavoriteService(ArticleRoomDatabase.getDatabase(binding.root.context).articleDao())

        val recyclerView = binding.articlesView

        recyclerView.layoutManager = LinearLayoutManager(view?.context)
        recyclerView.hasFixedSize()
        recyclerView.adapter = articleAdapter

        articlesScrollListener.loadMoreCallback = {
            launchQuery()
        }

        manageToolbar()

        launchQuery()
        return binding.root
    }

    private fun launchQuery() {
        if (!isLoading) {
            isLoading = true
            lifecycleScope.launch {
                binding.articlesView.removeOnScrollListener(articlesScrollListener)
                getData()
            }
        }
    }

    private suspend fun getData() {
        withContext(Dispatchers.IO)
        {
            if (context?.isOnline() == true) {
                val result = articleService?.getArticles(page)
                if (result != null) {
                    result.forEach {
                        it.isFavorite = favoriteService.exist(it)
                    }
                    bindData(result)
                } else {
                    displayError(R.string.request_error)
                }
            } else {
                displayError(R.string.no_internet_error)
            }
        }
    }

    private suspend fun bindData(result: List<Article>) {
        withContext(Dispatchers.Main)
        {
            articleAdapter.dataSet.addAll(result)
            articleAdapter.notifyDataSetChanged()

            if (page < articleService?.getPageNumber() ?: 0) {
                binding.articlesView.addOnScrollListener(articlesScrollListener)
                page++
            }
            isLoading = false
        }
    }

    private suspend fun displayError(@StringRes errorId: Int) {
        withContext(Dispatchers.Main)
        {
            errorSnackBar = Snackbar.make(
                binding.root,
                errorId,
                BaseTransientBottomBar.LENGTH_INDEFINITE
            )
            errorSnackBar?.setAction(R.string.retry) {
                launchQuery()
            }
            errorSnackBar?.show()
            isLoading = false
        }
    }

    fun addFavorite(article: Article) {
        article.isFavorite = true
        lifecycleScope.launch {
            addFav(article)
        }
    }

    fun deleteFavorite(article: Article) {
        article.isFavorite = false
        lifecycleScope.launch {
            delFav(article)
        }
    }

    private suspend fun addFav(article: Article) {
        withContext(Dispatchers.IO)
        {
            favoriteService.add(article)
        }
    }

    private suspend fun delFav(article: Article) {
        withContext(Dispatchers.IO)
        {
            favoriteService.delete(article)
        }
    }

    private fun manageToolbar() {
        if (articleService?.isFavorite != false) {
            val activity = activity as MainActivity
            activity.setFavoriteVisible(false)
        }
    }


    override fun onDestroy() {
        super.onDestroy()
        val activity = activity as MainActivity
        activity.setMenuVisible(true)
        errorSnackBar?.dismiss()
    }


    companion object {
        private const val ARG_ARTICLE_SERVICE = "articleService"

        @JvmStatic
        fun newInstance(articleService: ArticleService) =
            ArticlesFragment().apply {
                arguments = Bundle().apply {
                    putParcelable(ARG_ARTICLE_SERVICE, articleService)
                }
            }
    }
}