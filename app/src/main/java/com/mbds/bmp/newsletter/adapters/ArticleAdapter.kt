package com.mbds.bmp.newsletter.adapters

import android.text.format.DateUtils
import android.text.format.DateUtils.FORMAT_ABBREV_RELATIVE
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CompoundButton
import androidx.recyclerview.widget.RecyclerView
import com.mbds.bmp.newsletter.MainActivity
import com.mbds.bmp.newsletter.R
import com.mbds.bmp.newsletter.database.ArticleRoomDatabase
import com.mbds.bmp.newsletter.databinding.ItemArticleBinding
import com.mbds.bmp.newsletter.fragments.ArticleFragment
import com.mbds.bmp.newsletter.fragments.ArticlesFragment
import com.mbds.bmp.newsletter.model.Article
import com.mbds.bmp.newsletter.services.FavoriteService
import com.mbds.bmp.newsletter.tools.setImageFromUrl
import java.util.*

class ArticleAdapter(
    val dataSet: MutableList<Article>,
    private val articlesFragment: ArticlesFragment
) :
    RecyclerView.Adapter<ArticleAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val rootView = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_article, parent, false)

        return ViewHolder(rootView, articlesFragment)
    }

    override fun onBindViewHolder(holder: ArticleAdapter.ViewHolder, position: Int) {
        holder.bind(dataSet[position])
    }

    override fun getItemCount() = dataSet.size

    class ViewHolder(private val root: View, private val articlesFragment: ArticlesFragment) :
        RecyclerView.ViewHolder(root) {

        internal val binding = ItemArticleBinding.bind(root)
        internal val favoriteService =
            FavoriteService(ArticleRoomDatabase.getDatabase(root.context).articleDao())


        fun bind(item: Article) {
            binding.articleTitle.text = item.title
            binding.articleDescription.text = item.description
            binding.articleSource.text = item.source.name
            binding.articleDate.text = DateUtils.getRelativeTimeSpanString(
                item.publishedAt.time,
                Date().time,
                DateUtils.MINUTE_IN_MILLIS,
                FORMAT_ABBREV_RELATIVE
            )
            binding.articleImage.setImageFromUrl(
                item.urlToImage ?: "",
                R.drawable.placeholder_large,
                root
            )

            binding.articleCard.setOnClickListener {
                val mainActivity = (root.context as MainActivity)
                mainActivity.changeFragment(ArticleFragment.newInstance(item))
            }
            binding.article = item
            binding.articleFavorite.setOnCheckedChangeListener { button: CompoundButton, bool: Boolean ->
                if (bool) articlesFragment.addFavorite(item) else articlesFragment.deleteFavorite(
                    item
                )
            }
        }
    }
}