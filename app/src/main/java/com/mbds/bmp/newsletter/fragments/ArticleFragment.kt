package com.mbds.bmp.newsletter.fragments

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.format.DateUtils
import android.view.LayoutInflater
import android.view.Menu
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.mbds.bmp.newsletter.MainActivity
import com.mbds.bmp.newsletter.R
import com.mbds.bmp.newsletter.database.ArticleRoomDatabase
import com.mbds.bmp.newsletter.databinding.FragmentArticleBinding
import com.mbds.bmp.newsletter.model.Article
import com.mbds.bmp.newsletter.services.FavoriteService
import com.mbds.bmp.newsletter.tools.getCleanContent
import com.mbds.bmp.newsletter.tools.setImageFromUrl
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ArticleFragment : Fragment() {
    private var article: Article? = null
    private lateinit var binding: FragmentArticleBinding
    private lateinit var favoriteService: FavoriteService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            article = it.getSerializable(ARG_ARTICLE) as Article
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentArticleBinding.inflate(inflater, container, false)
        favoriteService =
            FavoriteService(ArticleRoomDatabase.getDatabase(binding.root.context).articleDao())

        setData()

        val activity = activity as MainActivity
        activity.setMenuVisible(true)
        if (article?.isFavorite != false) {
            activity.changeFavoriteIcon(R.drawable.ic_delete_favorite)
        } else {
            activity.changeFavoriteIcon(R.drawable.ic_add_favorite)
        }
        activity.onFavoriteClick = { menu ->
            updateFavoriteStatus(menu)
        }

        return binding.root
    }

    private fun setData() {
        if (article?.author.isNullOrBlank()) {
            //Hide author data
            binding.author.visibility = View.INVISIBLE
            binding.byAuthor.visibility = View.INVISIBLE
            binding.authorDateSeparator.visibility = View.INVISIBLE

            //Patch centering
            val dateLayoutParams = binding.date.layoutParams as ConstraintLayout.LayoutParams
            dateLayoutParams.startToStart = ConstraintLayout.LayoutParams.PARENT_ID
            binding.date.requestLayout()
        } else {
            binding.author.text = article?.author
        }
        binding.content.text = article?.getCleanContent()
        binding.description.text = article?.description

        article?.publishedAt?.let {
            binding.date.text = DateUtils.formatDateTime(
                this.context, it.time,
                DateUtils.FORMAT_SHOW_TIME or DateUtils.FORMAT_SHOW_DATE or DateUtils.FORMAT_SHOW_YEAR or DateUtils.FORMAT_SHOW_WEEKDAY or DateUtils.FORMAT_ABBREV_ALL
            )
        } ?: run {
            //Hide date data
            binding.date.visibility = View.INVISIBLE
            binding.authorDateSeparator.visibility = View.INVISIBLE

            //Patch centering
            val authorLayoutParams = binding.author.layoutParams as ConstraintLayout.LayoutParams
            authorLayoutParams.endToEnd = ConstraintLayout.LayoutParams.PARENT_ID
            binding.author.requestLayout()
        }

        binding.source.text = article?.source?.name
        binding.title.text = article?.title
        binding.articleLink.setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(article?.url))
            startActivity(intent)
        }
        binding.image.setImageFromUrl(
            article?.urlToImage ?: "",
            R.drawable.placeholder_large,
            binding.root
        )
    }

    private fun updateFavoriteStatus(menu: Menu): Boolean {
        val activity = activity as MainActivity
        if (article != null) {
            if (article!!.isFavorite) {
                article!!.isFavorite = false
                activity.changeFavoriteIcon(R.drawable.ic_add_favorite)
                lifecycleScope.launch {
                    delFav(article!!)
                }
            } else {
                article!!.isFavorite = true
                activity.changeFavoriteIcon(R.drawable.ic_delete_favorite)
                lifecycleScope.launch {
                    addFav(article!!)
                }
            }
            return true
        }
        return false
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

    companion object {

        val ARG_ARTICLE = "Article"

        @JvmStatic
        fun newInstance(article: Article) =
            ArticleFragment().apply {
                arguments = Bundle().apply {
                    putSerializable(ARG_ARTICLE, article)
                }
            }
    }


    override fun onDestroy() {
        val activity = activity as MainActivity
        activity.changeFavoriteIcon(R.drawable.ic_favorite_white)
        activity.onFavoriteClick = null
        super.onDestroy()
    }
}
