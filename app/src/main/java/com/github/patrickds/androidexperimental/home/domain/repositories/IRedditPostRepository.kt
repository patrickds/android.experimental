package com.github.patrickds.androidexperimental.home.domain.repositories

import com.github.patrickds.androidexperimental.home.domain.model.RedditPost
import io.reactivex.Observable

interface IRedditPostRepository {
    fun getAll(): Observable<List<RedditPost>>
}