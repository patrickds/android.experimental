package com.github.patrickds.androidexperimental.home.domain.repositories

import com.github.patrickds.androidexperimental.home.domain.model.RedditPost
import io.reactivex.Single

interface IRedditPostRepository {
    fun getAll(): Single<List<RedditPost>>
}