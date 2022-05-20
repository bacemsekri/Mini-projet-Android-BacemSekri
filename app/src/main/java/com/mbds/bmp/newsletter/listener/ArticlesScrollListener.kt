package com.mbds.bmp.newsletter.listener

import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class ArticlesScrollListener : RecyclerView.OnScrollListener() {

    lateinit var loadMoreCallback: () -> Unit

    override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
        super.onScrolled(recyclerView, dx, dy)

        val layoutManager = recyclerView.layoutManager as LinearLayoutManager
        if ((layoutManager.findLastCompletelyVisibleItemPosition() >= (recyclerView.adapter?.itemCount?.minus(
                3
            )!!))
        ) {
            loadMoreCallback.invoke()
        }
    }
}