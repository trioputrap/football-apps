package com.example.trio.footballapps

import android.database.sqlite.SQLiteConstraintException
import android.os.Bundle
import android.support.design.widget.TabLayout
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import com.bumptech.glide.Glide
import com.example.trio.footballapps.adapter.PlayerAdapter
import com.example.trio.footballapps.api.APIClient
import com.example.trio.footballapps.database.database
import com.example.trio.footballapps.model.PlayerItem
import com.example.trio.footballapps.model.TeamsItem
import com.example.trio.footballapps.utils.gone
import com.example.trio.footballapps.utils.invisible
import com.example.trio.footballapps.utils.visible
import kotlinx.android.synthetic.main.activity_team_detail.*
import org.jetbrains.anko.db.classParser
import org.jetbrains.anko.db.delete
import org.jetbrains.anko.db.insert
import org.jetbrains.anko.db.select
import org.jetbrains.anko.design.snackbar
import org.jetbrains.anko.startActivity

class TeamDetailActivity : AppCompatActivity(), TeamDetailView {
    val TAG = this::class.java.simpleName
    private var team: TeamsItem? = null
    private var players: MutableList<PlayerItem> = mutableListOf()
    private lateinit var presenter: TeamDetailPresenter
    private lateinit var adapter: PlayerAdapter

    private var isFavorite: Boolean = false
    private var menu: Menu? = null

    override fun showLoading() {
        progress_circular?.visible()
    }

    override fun hideLoading() {
        progress_circular?.invisible()
    }

    override fun showPlayers(data: List<PlayerItem>) {
        if (list_player != null) {
            players.clear()
            players.addAll(data)
            adapter.notifyDataSetChanged()
        }
    }


    private fun favoriteState() {
        database.use {
            val result = select(TeamsItem.TABLE_NAME)
                    .whereArgs(
                            "(" + TeamsItem.ID_TEAM + "= {id})",
                            "id" to team!!.idTeam.toString()
                    )
            val favorite = result.parseList(classParser<TeamsItem>())
            if (!favorite.isEmpty()) isFavorite = true
        }

        Log.e("ID TABEL", team?.idTeam.toString())
    }

    private fun setFavorite() {
        if (isFavorite)
            menu?.getItem(0)?.icon = ContextCompat.getDrawable(this, R.drawable.ic_favorited)
        else
            menu?.getItem(0)?.icon = ContextCompat.getDrawable(this, R.drawable.ic_favorite)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_team_detail)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        this.team = intent.getParcelableExtra("team")

        supportActionBar?.title = team!!.strTeam

        showDetail(this.team!!)

        val apiService = APIClient.getService()
        presenter = TeamDetailPresenter(this, apiService)

        presenter.getPlayers(this.team!!.idTeam!!)


        adapter = PlayerAdapter(this, players)
        adapter.listener = {
            this.startActivity<PlayerDetailActivity>("player" to it)
        }

        list_player.adapter = adapter
        list_player.layoutManager = LinearLayoutManager(this)

        tab_main.addOnTabSelectedListener(TeamTabSelectedListener())
        list_player.invisible()

        favoriteState()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                finish()
                true
            }
            R.id.favorite -> {
                if (isFavorite)
                    removeFromFavorite(team!!)
                else
                    addToFavorite(team!!)

                isFavorite = !isFavorite
                setFavorite()

                true
            }

            else -> super.onOptionsItemSelected(item)
        }
    }


    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.favorite, menu)
        this.menu = menu
        setFavorite()
        return true
    }

    fun showDetail(team: TeamsItem) {
        name.text = team.strTeam
        Glide.with(this).load(team.strTeamBadge).into(badge)
        year.text = team.intFormedYear
        stadium.text = team.strStadium
        desc.text = team.strDescriptionEN
    }

    private fun addToFavorite(team: TeamsItem) {
        try {
            database.use {
                insert(
                        TeamsItem.TABLE_NAME,
                        TeamsItem.ID_TEAM to team.idTeam,
                        TeamsItem.TEAM_NAME to team.strTeam,
                        TeamsItem.FORMED_YEAR to team.intFormedYear,
                        TeamsItem.DESCRIPTION to team.strDescriptionEN,
                        TeamsItem.STADIUM to team.strStadium,
                        TeamsItem.BADGE to team.strTeamBadge
                )
            }
            parent_layout.snackbar(R.string.added_to_favorite).show()
        } catch (e: SQLiteConstraintException) {
            parent_layout.snackbar(e.localizedMessage).show()
        }
    }

    private fun removeFromFavorite(team: TeamsItem) {
        try {
            database.use {
                delete(
                        TeamsItem.TABLE_NAME, "(" + TeamsItem.ID_TEAM + " = {id})",
                        "id" to team.idTeam.toString()
                )
            }
            parent_layout.snackbar(R.string.removed_from_favorite).show()
        } catch (e: SQLiteConstraintException) {
            parent_layout.snackbar(e.localizedMessage).show()
        }
    }

    inner class TeamTabSelectedListener : TabLayout.OnTabSelectedListener {
        override fun onTabReselected(tab: TabLayout.Tab?) {
        }

        override fun onTabUnselected(tab: TabLayout.Tab?) {
        }

        override fun onTabSelected(tab: TabLayout.Tab?) {
            if (tab?.position == 0) {
                desc.visible()
                list_player.gone()
            } else {
                desc.gone()
                list_player.visible()
            }
        }
    }


}
