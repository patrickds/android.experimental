package com.github.patrickds.androidexperimental.application

import com.github.patrickds.androidexperimental.application.injection.ApplicationScope
import com.github.patrickds.androidexperimental.application.injection.RepositoryModule
import com.github.patrickds.androidexperimental.home.domain.repositories.IRedditPostRepository
import dagger.Component

@ApplicationScope
@Component(modules = arrayOf(RepositoryModule::class))
interface ApplicationComponent {
    fun newsRepository(): IRedditPostRepository
}