package com.example.trio.footballapps.menu.match


import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.provider.CalendarContract
import android.support.design.widget.TabLayout
import android.support.v4.app.ActivityCompat
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.SearchView
import android.util.Log
import android.view.*
import android.widget.AdapterView
import android.widget.ArrayAdapter
import com.example.trio.footballapps.MatchDetailActivity
import com.example.trio.footballapps.R
import com.example.trio.footballapps.adapter.MatchAdapter
import com.example.trio.footballapps.api.APIClient
import com.example.trio.footballapps.model.EventsItem
import com.example.trio.footballapps.model.LeaguesItem
import com.example.trio.footballapps.utils.gone
import com.example.trio.footballapps.utils.invisible
import com.example.trio.footballapps.utils.visible
import kotlinx.android.synthetic.main.layout_main.*
import org.jetbrains.anko.startActivity
import org.jetbrains.anko.support.v4.toast
import java.text.SimpleDateFormat
import java.util.*

class MatchFragment : Fragment(), MatchView {
    private var matches: MutableList<EventsItem> = mutableListOf()
    private var searches: MutableList<EventsItem> = mutableListOf()
    private var leagues: MutableList<LeaguesItem> = mutableListOf()
    private lateinit var presenter: MatchPresenter
    private lateinit var adapter: MatchAdapter
    private var searchView: SearchView? = null

    private var isSearching: Boolean = false
    private var isUpcomingMatch: Boolean = true

    private var MY_PERMISSIONS_REQUEST_READ_CALENDAR = 100

    override fun showLoading() {
        progress_circular?.visible()
    }

    override fun hideLoading() {
        progress_circular?.invisible()
    }

    override fun showLeagues(data: List<LeaguesItem>) {
        if (sp_main != null) {
            leagues.clear()
            leagues.addAll(data)
            val adapter: ArrayAdapter<LeaguesItem> =
                    ArrayAdapter(context!!, android.R.layout.simple_spinner_item, leagues)
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            sp_main.adapter = adapter
            sp_main.visible()
        }
    }

    override fun showMatches(data: List<EventsItem>) {
        if (rv_main != null) {
            matches.clear()
            matches.addAll(data)
            adapter.notifyDataSetChanged()
        }
        sr_main?.isRefreshing = false
    }

    override fun showSearched(data: List<EventsItem>) {
        if (rv_main != null) {
            searches.clear()
            searches.addAll(data)
            adapter.setData(searches)
        }
        sr_main?.isRefreshing = false
    }

    override fun closeSearch() {
        adapter.setData(matches)
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

        val searchMatch = menu?.findItem(R.id.menu_search)?.actionView as SearchView?
        searchView = searchMatch

        searchMatch!!.queryHint = "Enter team name here"

        searchMatch.setOnQueryTextListener(object : android.support.v7.widget.SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(param: String?): Boolean {
                tab_main.gone()
                sp_main.gone()
                presenter.searchMatches(param!!)
                isSearching = true
                return false
            }

            override fun onQueryTextChange(param: String?): Boolean {
                return false
            }

        })

        searchMatch.setOnCloseListener {
            tab_main.visible()
            sp_main.visible()
            closeSearch()
            false
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        sp_main.invisible()
        sp_main.onItemSelectedListener = LeagueItemSelectedListener()

        val apiService = APIClient.getService()

        presenter = MatchPresenter(this, apiService)
        presenter.getAllLeagues()


        adapter = MatchAdapter(context, matches)
        adapter.listener = {
            context?.startActivity<MatchDetailActivity>("event" to it)
        }

        adapter.alarmListener = {
            requestCalendarPermission(it)
        }

        rv_main.adapter = adapter
        rv_main.layoutManager = LinearLayoutManager(context)

        tab_main.addOnTabSelectedListener(MatchTabSelectedListener())

        sr_main.setOnRefreshListener {
            if (!isSearching) {
                val league = sp_main.selectedItem as LeaguesItem
                if (isUpcomingMatch)
                    presenter.getNextMatches(league.idLeague!!)
                else
                    presenter.getLastMatches(league.idLeague!!)
            } else {
                presenter.searchMatches(searchView?.query.toString())
            }
        }
    }

    private fun requestCalendarPermission(match: EventsItem) {
        val permissionArrayList = ArrayList<String>()

        if (ContextCompat.checkSelfPermission(
                        context!!,
                        Manifest.permission.WRITE_CALENDAR
                ) != PackageManager.PERMISSION_GRANTED
        ) {
            permissionArrayList.add(Manifest.permission.WRITE_CALENDAR)
        }

        if (ContextCompat.checkSelfPermission(
                        context!!,
                        Manifest.permission.READ_CALENDAR
                ) != PackageManager.PERMISSION_GRANTED
        ) {
            permissionArrayList.add(Manifest.permission.READ_CALENDAR)
        }

        if (permissionArrayList.size > 0) {
            val permissionArray = arrayOfNulls<String>(permissionArrayList.size)

            for (value in permissionArrayList.withIndex()) {
                permissionArray[value.index] = permissionArrayList[value.index]
            }

            ActivityCompat.requestPermissions(
                    activity!!,
                    permissionArray,
                    MY_PERMISSIONS_REQUEST_READ_CALENDAR
            )
        } else {
            toast("Permission Granted")
            addToCalendar(match)
        }

    }

    private fun addToCalendar(match: EventsItem) {
        val titleEvent = match.strEvent
        val pattern = "yyyy-MM-dd HH:mm:ss"
        val format = SimpleDateFormat(pattern, Locale.ENGLISH)
        val stringTimeEvent = match.getDateStringFromat(pattern)

        val dateTimeEvent = format.parse(stringTimeEvent)
        Log.e("DATE_TO_ADD", dateTimeEvent.toString())
        //convert to long
        val beginTime = dateTimeEvent.time
        //add end-time
        val endTime = beginTime + 3600000
        // intent
        val intentCalendar = Intent(Intent.ACTION_EDIT)
        intentCalendar.type = "vnd.android.cursor.item/event"
        intentCalendar.putExtra(CalendarContract.Events.TITLE, titleEvent)
        intentCalendar.putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME, beginTime)
        intentCalendar.putExtra(CalendarContract.EXTRA_EVENT_END_TIME, endTime)
        startActivity(intentCalendar)
    }


    inner class LeagueItemSelectedListener : AdapterView.OnItemSelectedListener {
        override fun onNothingSelected(p0: AdapterView<*>?) {
        }

        override fun onItemSelected(adapterView: AdapterView<*>?, view: View?, i: Int, l: Long) {
            val league = sp_main.selectedItem as LeaguesItem
            if (isUpcomingMatch)
                presenter.getNextMatches(league.idLeague!!)
            else
                presenter.getLastMatches(league.idLeague!!)
        }
    }

    inner class MatchTabSelectedListener : TabLayout.OnTabSelectedListener {
        override fun onTabReselected(tab: TabLayout.Tab?) {
        }

        override fun onTabUnselected(tab: TabLayout.Tab?) {
        }

        override fun onTabSelected(tab: TabLayout.Tab?) {
            val league = sp_main.selectedItem as LeaguesItem
            if (tab?.position == 0) {
                isUpcomingMatch = true
                presenter.getNextMatches(league.idLeague!!)
            } else {
                isUpcomingMatch = false
                presenter.getLastMatches(league.idLeague!!)
            }
        }
    }
}
