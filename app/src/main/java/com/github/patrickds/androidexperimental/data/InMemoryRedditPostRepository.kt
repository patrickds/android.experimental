package com.github.patrickds.androidexperimental.data

import com.github.patrickds.androidexperimental.home.domain.model.RedditPost
import com.github.patrickds.androidexperimental.home.domain.repositories.IRedditPostRepository
import io.reactivex.Observable
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class InMemoryRedditPostRepository
@Inject constructor() : IRedditPostRepository {

    override fun getAll() = Observable.create<List<RedditPost>> { emitter ->

        val news = listOf(
                RedditPost("Title 1", "r/All", "Content 1", "Author 1", 200, 4),
                RedditPost("This corn plane at the airport", "r/mildlyinteresting", "Content 2", "Author 2", 647, 3),
                RedditPost("Title 3", "r/All", "Content 3", "Author 3", 300, 5),
                RedditPost("Title 4", "r/All", "Content 4", "Author 4", 300, 3),
                RedditPost("Title 5", "r/All", "Content 5", "Author 5", 300, 2),
                RedditPost("Title 6", "r/All", "Content 6", "Author 6", 300, 4),
                RedditPost("Title 7", "r/All", "Content 7", "Author 7", 300, 1),
                RedditPost("Title 8", "r/All", "Content 8", "Author 8", 300, 5),
                RedditPost("Title 9", "r/All", "Content 9", "Author 9", 300, 2)
        )

        emitter.onNext(news)
        emitter.onComplete()
    }
            .delay(3, TimeUnit.SECONDS)
}