package com.github.patrickds.androidexperimental.home

import com.github.patrickds.androidexperimental.home.domain.model.RedditPost
import io.reactivex.Observable

class HomeContract {
    interface View{
        fun showNews(news: List<RedditPost>)
        fun showError(message: String)
        fun showError(throwable: Throwable)
        fun showLoading()
        fun hideLoading()
        fun updateItem(item: RedditPost)
        fun listItemClicks(): Observable<RedditPost>
        fun voteUpClicks(): Observable<RedditPost>
        fun voteDownClicks(): Observable<RedditPost>
        fun startDetailsActivity(post: RedditPost)
    }

    interface Presenter{
        fun start()
        fun stop()
    }
}