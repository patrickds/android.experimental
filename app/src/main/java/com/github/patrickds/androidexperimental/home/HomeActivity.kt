package com.github.patrickds.androidexperimental.home

import android.app.ProgressDialog
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import com.github.patrickds.androidexperimental.R
import com.github.patrickds.androidexperimental.application.AndroidExperimentalApplication
import com.github.patrickds.androidexperimental.application.showLongSnack
import com.github.patrickds.androidexperimental.home.domain.model.RedditPost
import com.github.patrickds.androidexperimental.home.injection.DaggerHomeComponent
import com.github.patrickds.androidexperimental.home.injection.HomeModule
import kotlinx.android.synthetic.main.activity_home.*
import javax.inject.Inject

class HomeActivity : AppCompatActivity(), HomeContract.View {

    val newsList: RecyclerView by lazy { news_list }
    val progressDialog by lazy { ProgressDialog(this) }
    val adapter = RedditPostsAdapter(emptyList())

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
    override fun listItemClicks() = adapter.observeListItemClicks()
    override fun voteUpClicks() = adapter.observeVoteUp()
    override fun voteDownClicks() = adapter.observeVoteDown()

    override fun startDetailsActivity(post: RedditPost) {
    }

    override fun showNews(news: List<RedditPost>) {
        adapter.replaceData(news)
    }

    override fun showLoading() {
        progressDialog.show()
    }

    override fun hideLoading() {
        progressDialog.hide()
    }
}
