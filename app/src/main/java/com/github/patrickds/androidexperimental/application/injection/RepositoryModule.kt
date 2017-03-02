package com.github.patrickds.androidexperimental.application.injection

import com.github.patrickds.androidexperimental.data.InMemoryRedditPostRepository
import com.github.patrickds.androidexperimental.home.domain.repositories.IRedditPostRepository
import dagger.Module
import dagger.Provides

@Module
class RepositoryModule {

    @Provides
    @ApplicationScope
    fun newsRepository(repository: InMemoryRedditPostRepository): IRedditPostRepository {
        return repository
    }
}