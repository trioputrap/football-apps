package com.example.trio.footballapps.menu.match

import com.example.trio.footballapps.api.APIService
import com.example.trio.footballapps.model.EventsItem
import com.example.trio.footballapps.model.MatchResponse
import io.reactivex.Observable
import io.reactivex.schedulers.Schedulers
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.Mockito.`when`
import org.mockito.MockitoAnnotations

class MatchPresenterTest {
    private var matchs = mutableListOf<EventsItem>()
    private lateinit var match : MatchResponse
    private lateinit var matchResponse: Observable<MatchResponse>

    @Mock
    private
    lateinit var view: MatchView

    @Mock
    private
    lateinit var apiService: APIService

    private lateinit var presenter: MatchPresenter

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)
        presenter = MatchPresenter(view, apiService)
    }

    @Test
    fun getMatch() {
        match = MatchResponse(matchs)
        val id = "4328"
        matchResponse = Observable.just(match)

        `when`(apiService.getNextMatches(id)).thenReturn(matchResponse)

        presenter.getNextMatches(id, Schedulers.trampoline())
        Mockito.verify(view).showLoading()
        Mockito.verify(view).showMatches(match.events!!)
        Mockito.verify(view).hideLoading()
    }
}