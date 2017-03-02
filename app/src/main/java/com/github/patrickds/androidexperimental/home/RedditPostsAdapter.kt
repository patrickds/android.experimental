package com.github.patrickds.androidexperimental.home

import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.github.patrickds.androidexperimental.R
import com.github.patrickds.androidexperimental.application.inflate
import com.github.patrickds.androidexperimental.home.domain.model.RedditPost
import com.jakewharton.rxbinding2.view.RxView
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject
import kotlinx.android.synthetic.main.view_news_list_item.view.*

class RedditPostsAdapter(var items: List<RedditPost>) :
        RecyclerView.Adapter<RedditPostsAdapter.NewsViewHolder>() {

    private val clickSubject = PublishSubject.create<RedditPost>()
    fun observeListItemClicks(): Observable<RedditPost> = clickSubject

    private val voteUpSubject = PublishSubject.create<RedditPost>()
    fun observeVoteUp(): Observable<RedditPost> = voteUpSubject

    private val voteDownSubject = PublishSubject.create<RedditPost>()
    fun observeVoteDown(): Observable<RedditPost> = voteDownSubject

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NewsViewHolder {
        val view = parent.inflate(R.layout.view_news_list_item)
        val holder = NewsViewHolder(view)

        holder.postClicks.map(items::get).subscribe(clickSubject::onNext)
        holder.voteUpClicks.map(items::get).subscribe(voteUpSubject::onNext)
        holder.voteDownClicks.map(items::get).subscribe(voteDownSubject::onNext)

        return holder
    }

    override fun onBindViewHolder(holder: NewsViewHolder, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount() = items.size

    fun updateItem(item: RedditPost) {
        notifyDataSetChanged()
    }

    fun replaceData(items: List<RedditPost>) {
        this.items = items
        notifyDataSetChanged()
    }

    inner class NewsViewHolder(view: View) :
            RecyclerView.ViewHolder(view) {

        val postTitle: TextView by lazy { view.post_title }
        val postCategory: TextView by lazy { view.post_category }
        val postVotes: TextView by lazy { view.post_votes }
        val voteUp: ImageView by lazy { view.post_vote_up }
        val voteDown: ImageView by lazy { view.post_vote_down }
        val postImage: ImageView by lazy { view.post_image }
        val postComments: TextView by lazy { view.post_comments }
        val savePost: TextView by lazy { view.post_save }
        val hidePost: TextView by lazy { view.post_hide }
        val sharePost: TextView by lazy { view.post_share }

        val postClicks = RxView.clicks(view).map { this.adapterPosition }!!
        val voteUpClicks = RxView.clicks(voteUp).map { this.adapterPosition }!!
        val voteDownClicks = RxView.clicks(voteDown).map { this.adapterPosition }!!

        fun bind(item: RedditPost) {
            postTitle.text = item.title
            postCategory.text = item.category
            postVotes.text = item.votes.toString()
            postComments.text = "${item.comments} comments"
        }
    }
}