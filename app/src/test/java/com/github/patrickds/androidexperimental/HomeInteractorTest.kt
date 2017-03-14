package com.github.patrickds.androidexperimental

import com.github.patrickds.androidexperimental.application.HomeActivityState
import com.github.patrickds.androidexperimental.home.HomeInteractor
import com.github.patrickds.androidexperimental.home.domain.model.RedditPost
import com.github.patrickds.androidexperimental.home.domain.repositories.IRedditPostRepository
import io.reactivex.Observable
import io.reactivex.observers.TestObserver
import org.junit.Before
import org.junit.Test
import org.mockito.BDDMockito.given
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.MockitoAnnotations
import org.mockito.Spy

class HomeInteractorTest {

    @Mock
    lateinit var redditPostRepository: IRedditPostRepository

    @Spy
    lateinit var activityState: HomeActivityState

    lateinit var homeInteractor: HomeInteractor

    @Before
    fun setup() {
        MockitoAnnotations.initMocks(this)
        homeInteractor = HomeInteractor(activityState, redditPostRepository)
    }

    @Test
    fun givenNoSavedStateAndNoPendingRequest_shouldCreateNewRequest() {
        val post = mock(RedditPost::class.java)
        val posts = listOf(post)

        val networkObservable = Observable.just(posts)
        given(redditPostRepository.getAll()).willReturn(networkObservable)

        homeInteractor.loadNews()
                .test()
                .assertValues(posts)

        verify(activityState, times(1)).getNewsSavedState()
        verify(activityState, times(1)).newsPendingRequest
        verify(redditPostRepository, times(1)).getAll()
    }

    @Test
    fun givenNoSavedStateAndPendingRequest_shouldReusePendingRequest() {
        val post = mock(RedditPost::class.java)
        val posts = listOf(post)

        val pendingRequestObservable = Observable.just(posts)
        val networkRequestSubscriber = TestObserver.create<List<RedditPost>>()
        val networkRequestObservable = Observable.just(posts).doOnSubscribe { networkRequestSubscriber.onSubscribe(it) }
        given(activityState.newsPendingRequest).willReturn(pendingRequestObservable)
        given(redditPostRepository.getAll()).willReturn(networkRequestObservable)

        homeInteractor.loadNews()
                .test()
                .assertValues(posts)

        networkRequestSubscriber.assertNotSubscribed()

        verify(activityState, times(1)).getNewsSavedState()
        verify(activityState, times(1)).newsPendingRequest
        verify(redditPostRepository, times(1)).getAll()
    }

    @Test
    fun givenASuccessfulRequest_shouldSaveState() {
        val post = mock(RedditPost::class.java)
        val posts = listOf(post)

        val networkRequestObservable = Observable.just(posts)
        given(redditPostRepository.getAll()).willReturn(networkRequestObservable)

        homeInteractor.loadNews()
                .test()
                .assertComplete()

        verify(activityState, times(1)).saveNewsState(posts)
    }

    @Test
    fun givenASuccessfulRequest_whenCallingAgain_shouldReturnSavedState() {
        val post = mock(RedditPost::class.java)
        val posts = listOf(post)

        val networkRequestObservable = Observable.just(posts)
        given(redditPostRepository.getAll()).willReturn(networkRequestObservable)

        homeInteractor.loadNews()
                .test()
                .assertComplete()

        val networkRequestSubscriber = TestObserver.create<List<RedditPost>>()
        val networkRequestObservable2 = Observable.just(posts).doOnSubscribe(networkRequestSubscriber::onSubscribe)
        given(redditPostRepository.getAll()).willReturn(networkRequestObservable2)

        homeInteractor.loadNews()
                .test()
                .assertValues(posts)
                .assertComplete()

        networkRequestSubscriber.assertNotSubscribed()
    }

    @Test
    fun givenASavedState_whenDataIsOutdated_ShouldReturnSavedStateAndMakeNewRequest(){
        val post = mock(RedditPost::class.java)
        val freshPost = mock(RedditPost::class.java)
        val outdatedPosts = listOf(post)
        val freshPosts = listOf(freshPost)

        homeInteractor.isOutdated = true

        val savedStateObservable = Observable.just(outdatedPosts)
        given(activityState.getNewsSavedState()).willReturn(savedStateObservable)

        val networkRequestObservable = Observable.just(freshPosts)
        given(redditPostRepository.getAll()).willReturn(networkRequestObservable)

        homeInteractor.loadNews()
                .test()
                .assertValues(outdatedPosts, freshPosts)
                .assertComplete()
    }
}