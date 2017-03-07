package com.github.patrickds.androidexperimental.home

import com.github.patrickds.androidexperimental.application.HomeActivityState
import com.github.patrickds.androidexperimental.home.domain.model.RedditPost
import com.github.patrickds.androidexperimental.home.domain.repositories.IRedditPostRepository
import io.reactivex.Observable
import javax.inject.Inject

class HomeInteractor
@Inject constructor(
        val activityState: HomeActivityState,
        val redditPostRepository: IRedditPostRepository) {

    var isOutdated = false

    fun loadNews(): Observable<List<RedditPost>> = when (isOutdated) {
        false -> getCachedOrNetwork()
        true -> getCachedAndNetwork()
    }

    private fun getCachedAndNetwork() =
            Observable.concat(
                    activityState.getNewsSavedState(),
                    loadNewsFromNetwork())

    private fun getCachedOrNetwork() =
            activityState.getNewsSavedState()
                    .switchIfEmpty(loadNewsFromNetwork())

    private fun loadNewsFromNetwork(): Observable<List<RedditPost>> {
        val requestObservable = activityState.newsPendingRequest
                .switchIfEmpty(redditPostRepository.getAll())
                .doOnNext(activityState::saveNewsState)
                .doFinally(activityState::clearNewsPendingRequest)
                .cache()

        activityState.newsPendingRequest = requestObservable

        return requestObservable
    }
}