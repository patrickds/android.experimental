package com.github.patrickds.androidexperimental.home

import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class HomePresenter
@Inject constructor(
        val view: HomeContract.View,
        val interactor: HomeInteractor) :
        HomeContract.Presenter {

    private val subscriptions = CompositeDisposable()

    override fun start() {
        subscriptions.add(loadNews())
        subscriptions.add(postClick())
        subscriptions.add(voteUpClick())
        subscriptions.add(voteDownClick())
    }

    override fun stop() {
        subscriptions.clear()
    }

    private fun loadNews() =
            interactor.loadNews()
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .doOnSubscribe { view.showLoading() }
                    .doFinally(view::hideLoading)
                    .subscribe(view::showNews, view::showError)

    private fun postClick() =
            view.listItemClicks()
                    .subscribe(view::startDetailsActivity)

    private fun voteUpClick() =
            view.voteUpClicks()
                    .doOnNext { it.votes++ }
                    .subscribe(view::updateItem)

    private fun voteDownClick() =
            view.voteDownClicks()
                    .doOnNext { it.votes-- }
                    .subscribe(view::updateItem)
}