package com.example.trio.footballapps.menu.favorite


import android.os.Bundle
import android.support.design.widget.TabLayout
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.trio.footballapps.MatchDetailActivity
import com.example.trio.footballapps.R
import com.example.trio.footballapps.TeamDetailActivity
import com.example.trio.footballapps.adapter.MatchAdapter
import com.example.trio.footballapps.adapter.TeamAdapter
import com.example.trio.footballapps.model.EventsItem
import com.example.trio.footballapps.model.TeamsItem
import com.example.trio.footballapps.utils.gone
import com.example.trio.footballapps.utils.invisible
import com.example.trio.footballapps.utils.visible
import kotlinx.android.synthetic.main.layout_main.*
import org.jetbrains.anko.startActivity

class FavoriteFragment : Fragment(), FavoriteView {

    private var matches: MutableList<EventsItem> = mutableListOf()
    private var teams: MutableList<TeamsItem> = mutableListOf()
    private lateinit var matchAdapter: MatchAdapter
    private lateinit var teamAdapter: TeamAdapter
    private lateinit var presenter: FavoritePresenter

    override fun showLoading() {
        progress_circular?.visible()
    }

    override fun hideLoading() {
        progress_circular?.invisible()
    }

    override fun showMatches(data: List<EventsItem>) {
        if (rv_main != null) {
            matches.clear()
            matches.addAll(data)
            matchAdapter.notifyDataSetChanged()
        }
        sr_main?.isRefreshing = false
    }

    override fun showTeams(data: List<TeamsItem>) {
        if (rv_main != null) {
            teams.clear()
            teams.addAll(data)
            teamAdapter.notifyDataSetChanged()
        }
        sr_main?.isRefreshing = false
    }

    fun displayData(tab_position: Int?) {
        if (tab_position == 0) {
            rv_main.adapter = matchAdapter
            presenter.getMatches()
        } else {
            rv_main.adapter = teamAdapter
            presenter.getTeams()
        }
    }

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.layout_main, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        sp_main.gone()
        tab_main.getTabAt(0)?.text = "MATCH"
        tab_main.getTabAt(1)?.text = "TEAM"

        presenter = FavoritePresenter(this, context!!)

        matchAdapter = MatchAdapter(context, matches)
        matchAdapter.listener = {
            context?.startActivity<MatchDetailActivity>("event" to it)
        }

        teamAdapter = TeamAdapter(context!!, teams)
        teamAdapter.listener = {
            context?.startActivity<TeamDetailActivity>("team" to it)
        }

        rv_main.adapter = matchAdapter
        rv_main.layoutManager = LinearLayoutManager(context)

        tab_main.addOnTabSelectedListener(FavoriteTabSelectedListener())
        presenter.getMatches()


        sr_main.setOnRefreshListener {
            displayData(tab_main.selectedTabPosition)
        }
    }

    override fun onResume() {
        super.onResume()
        displayData(tab_main.selectedTabPosition)
    }


    inner class FavoriteTabSelectedListener : TabLayout.OnTabSelectedListener {
        override fun onTabReselected(tab: TabLayout.Tab?) {
        }

        override fun onTabUnselected(tab: TabLayout.Tab?) {
        }

        override fun onTabSelected(tab: TabLayout.Tab?) {
            displayData(tab?.position)
        }
    }

}
