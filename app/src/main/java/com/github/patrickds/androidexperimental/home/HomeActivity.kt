package com.github.patrickds.androidexperimental.home

import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import com.github.patrickds.androidexperimental.R
import com.github.patrickds.androidexperimental.application.AndroidExperimentalApplication
import com.github.patrickds.androidexperimental.application.showLongSnack
import com.github.patrickds.androidexperimental.autoadapter.AutoAdapter
import com.github.patrickds.androidexperimental.details.DetailsActivity
import com.github.patrickds.androidexperimental.home.domain.model.RedditPost
import com.github.patrickds.androidexperimental.home.injection.DaggerHomeComponent
import com.github.patrickds.androidexperimental.home.injection.HomeModule
import kotlinx.android.synthetic.main.activity_home.*
import kotlinx.android.synthetic.main.view_news_list_item.view.*
import javax.inject.Inject

class HomeActivity : AppCompatActivity(), HomeContract.View {

    val newsList: RecyclerView by lazy { news_list }
    val progressDialog by lazy { ProgressDialog(this) }

    val adapter = AutoAdapter
            .Builder<RedditPost>()
            .layout(R.layout.view_news_list_item)
            .clicks(R.id.post_vote_down)
            .clicks(R.id.post_vote_up)
            .clicks(R.id.post_list_item)
            .itemBinder(RedditPostBinder())
            .build()

    class RedditPostBinder : AutoAdapter.IItemBinder<RedditPost> {
        override fun bind(view: View, item: RedditPost) {
            view.post_votes.text = item.votes.toString()
            view.post_title.text = item.title
        }
    }

    @Inject
    lateinit var presenter: HomeContract.Presenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        DaggerHomeComponent.builder()
                .homeModule(HomeModule(this))
                .applicationComponent(AndroidExperimentalApplication.get(this).component)
                .build()
                .inject(this)

        newsList.layoutManager = LinearLayoutManager(this)
        newsList.adapter = adapter
    }

    override fun onResume() {
        super.onResume()
        presenter.start()
    }

    override fun onStop() {
        presenter.stop()
        super.onStop()
    }

    override fun showError(message: String) {
        this.showLongSnack(message)
    }

    override fun showError(throwable: Throwable) {
        showError(throwable.message.orEmpty())
    }

    override fun updateItem(item: RedditPost) = adapter.updateItem(item)
    override fun listItemClicks() = adapter.clicks(R.id.post_list_item)
    override fun voteUpClicks() = adapter.clicks(R.id.post_vote_up)
    override fun voteDownClicks() = adapter.clicks(R.id.post_vote_down)

    override fun startDetailsActivity(post: RedditPost) {
        startActivity(Intent(this, DetailsActivity::class.java).putExtra("title", post.title))
    }

    override fun showNews(news: List<RedditPost>) {
        adapter.swapData(news)
    }

    override fun showLoading() {
        progressDialog.show()
    }

    override fun hideLoading() {
        progressDialog.hide()
    }
}
