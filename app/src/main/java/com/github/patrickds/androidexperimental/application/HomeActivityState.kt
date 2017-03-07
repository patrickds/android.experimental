package com.github.patrickds.androidexperimental.application

import com.github.patrickds.androidexperimental.home.domain.model.RedditPost
import io.reactivex.Observable
import javax.inject.Inject

class HomeActivityState
@Inject constructor() {

    var newsPendingRequest: Observable<List<RedditPost>> =
            Observable.empty()

    var savedState = emptyList<RedditPost>()
    fun getNewsSavedState(): Observable<List<RedditPost>> =
            if (savedState.isEmpty())
                Observable.empty()
            else
                Observable.just(savedState)

    fun clearNewsPendingRequest() {
        newsPendingRequest = Observable.empty()
    }

    fun saveNewsState(state: List<RedditPost>) {
        savedState = state
    }
}