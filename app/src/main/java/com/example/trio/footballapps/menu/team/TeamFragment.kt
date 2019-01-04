package com.example.trio.footballapps.menu.team


import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.SearchView
import android.view.*
import android.widget.AdapterView
import android.widget.ArrayAdapter
import com.example.trio.footballapps.R
import com.example.trio.footballapps.TeamDetailActivity
import com.example.trio.footballapps.adapter.TeamAdapter
import com.example.trio.footballapps.api.APIClient
import com.example.trio.footballapps.model.LeaguesItem
import com.example.trio.footballapps.model.TeamsItem
import com.example.trio.footballapps.utils.gone
import com.example.trio.footballapps.utils.invisible
import com.example.trio.footballapps.utils.visible
import kotlinx.android.synthetic.main.layout_main.*
import org.jetbrains.anko.startActivity

class TeamFragment : Fragment(), TeamView {
    private var teams: MutableList<TeamsItem> = mutableListOf()
    private var leagues: MutableList<LeaguesItem> = mutableListOf()
    private lateinit var presenter: TeamPresenter
    private lateinit var adapter: TeamAdapter
    private var searchView: SearchView? = null

    private var isSearching: Boolean = false

    override fun showLoading() {
        progress_circular?.visible()
    }

    override fun hideLoading() {
        progress_circular?.invisible()
    }


    override fun showLeagues(data: List<LeaguesItem>) {
        if (sp_main != null) {
            leagues.clear()
            leagues.addAll(data.filter { league -> league.strSport == "Soccer" })

            val adapter: ArrayAdapter<LeaguesItem> = ArrayAdapter(context!!, android.R.layout.simple_spinner_item, leagues)
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

            sp_main.adapter = adapter
            sp_main.visible()

            presenter.getTeams(leagues.get(0).idLeague!!)
        }
    }

    override fun showTeams(data: List<TeamsItem>) {
        if (rv_main != null) {
            teams.clear()
            teams.addAll(data)
            adapter.setData(teams)
        }
        sr_main?.isRefreshing = false
    }

    override fun showSearched(data: List<TeamsItem>) {
        adapter.setData(data)
        isSearching = true
        sr_main?.isRefreshing = false
    }

    override fun closeSearch() {
        adapter.setData(teams)
        isSearching = false
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.layout_main, container, false)
    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        inflater?.inflate(R.menu.search, menu)
        super.onCreateOptionsMenu(menu, inflater)

        val searchMatch = menu?.findItem(R.id.menu_search)?.actionView as android.support.v7.widget.SearchView?
        searchView = searchMatch

        searchMatch!!.queryHint = "Enter team name here"

        searchMatch.setOnQueryTextListener(object : android.support.v7.widget.SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(param: String?): Boolean {
                sp_main.gone()
                presenter.searchTeams(param!!)
                return false
            }

            override fun onQueryTextChange(param: String?): Boolean {
                return false
            }

        })

        searchMatch.setOnCloseListener {
            sp_main.visible()
            closeSearch()
            false
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        tab_main.gone()

        sp_main.invisible()
        sp_main.onItemSelectedListener = LeagueItemSelectedListener()

        val apiService = APIClient.getService()

        presenter = TeamPresenter(this, apiService)
        presenter.getAllLeagues()


        adapter = TeamAdapter(context!!, teams)
        adapter.listener = {
            context?.startActivity<TeamDetailActivity>("team" to it)
        }

        rv_main.adapter = adapter
        rv_main.layoutManager = LinearLayoutManager(context)

        sr_main.setOnRefreshListener {
            if (!isSearching) {
                val league = sp_main.selectedItem as LeaguesItem
                presenter.getTeams(league.idLeague!!)
            } else {
                presenter.searchTeams(searchView?.query.toString())
            }
        }
    }

    inner class LeagueItemSelectedListener : AdapterView.OnItemSelectedListener {
        override fun onNothingSelected(adapterView: AdapterView<*>?) {
        }

        override fun onItemSelected(adapterView: AdapterView<*>?, view: View?, i: Int, l: Long) {
            val item: LeaguesItem = adapterView?.selectedItem as LeaguesItem
            presenter.getTeams(item.idLeague!!)
        }
    }
}
