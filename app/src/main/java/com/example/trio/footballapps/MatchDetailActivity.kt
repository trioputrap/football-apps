package com.example.trio.footballapps

import android.database.sqlite.SQLiteConstraintException
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import com.bumptech.glide.Glide
import com.example.trio.footballapps.api.APIClient
import com.example.trio.footballapps.database.database
import com.example.trio.footballapps.model.EventsItem
import com.example.trio.footballapps.model.TeamsItem
import com.example.trio.footballapps.utils.invisible
import com.example.trio.footballapps.utils.toStringNull
import com.example.trio.footballapps.utils.visible
import kotlinx.android.synthetic.main.activity_match_detail.*
import org.jetbrains.anko.db.classParser
import org.jetbrains.anko.db.delete
import org.jetbrains.anko.db.insert
import org.jetbrains.anko.db.select
import org.jetbrains.anko.design.snackbar

class MatchDetailActivity : AppCompatActivity(), MatchDetailView {
    val TAG = this::class.java.simpleName
    private lateinit var presenter: MatchDetailPresenter

    private var isFavorite: Boolean = false
    private var menu: Menu? = null

    private var match: EventsItem? = null

    override fun showHomeBadgeLoading() {
        home_progress_bar?.visible()
    }

    override fun hideHomeBadgeLoading() {
        home_progress_bar?.invisible()
    }

    override fun showAwayBadgeLoading() {
        away_progress_bar?.visible()
    }

    override fun hideAwayBadgeLoading() {
        away_progress_bar?.invisible()
    }

    override fun showHomeTeam(data: TeamsItem) {
        Glide.with(applicationContext).load(data.strTeamBadge).into(home_badge)
    }

    override fun showAwayTeam(data: TeamsItem) {
        Glide.with(applicationContext).load(data.strTeamBadge).into(away_badge)
    }

    private fun favoriteState() {
        database.use {
            val result = select(EventsItem.TABLE_NAME)
                    .whereArgs(
                            "(" + EventsItem.ID_EVENT + "= {id})",
                            "id" to match!!.idEvent.toString()
                    )
            val favorite = result.parseList(classParser<EventsItem>())
            if (!favorite.isEmpty()) isFavorite = true
        }

        Log.e("ID TABEL", match?.idEvent.toString())
    }

    private fun setFavorite() {
        if (isFavorite)
            menu?.getItem(0)?.icon = ContextCompat.getDrawable(this, R.drawable.ic_favorited)
        else
            menu?.getItem(0)?.icon = ContextCompat.getDrawable(this, R.drawable.ic_favorite)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_match_detail)

        val apiService = APIClient.getService()
        presenter = MatchDetailPresenter(this, apiService)


        this.match = intent.getParcelableExtra("event")

        supportActionBar?.title = match!!.strEvent
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        Log.wtf(TAG, this.match!!.strAwayTeam)

        showDetail(this.match!!)
        favoriteState()
    }


    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.favorite, menu)
        this.menu = menu
        setFavorite()
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                finish()
                true
            }
            R.id.favorite -> {
                if (isFavorite)
                    removeFromFavorite(match!!)
                else
                    addToFavorite(match!!)

                isFavorite = !isFavorite
                setFavorite()
                true
            }

            else -> super.onOptionsItemSelected(item)
        }
    }


    fun showDetail(match: EventsItem) {
        presenter.getHomeTeam(match.idHomeTeam!!)
        presenter.getAwayTeam(match.idAwayTeam!!)

        match_date.text = match.getDateStringFromat("E, d MMM yyyy \nHH:mm")
        home_name.text = match.strHomeTeam
        away_name.text = match.strAwayTeam
        home_score.text = match.intHomeScore?.toStringNull()
        away_score.text = match.intAwayScore?.toStringNull()
        home_goals.text = match.strHomeGoalDetails?.toStringNull()
        away_goals.text = match.strAwayGoalDetails?.toStringNull()
        home_shots.text = match.intHomeShots?.toStringNull()
        away_shots.text = match.intAwayShots?.toStringNull()
        home_gk.text = match.strHomeLineupGoalkeeper?.toStringNull()
        away_gk.text = match.strAwayLineupGoalkeeper?.toStringNull()
        home_def.text = match.strHomeLineupDefense?.toStringNull()
        away_def.text = match.strAwayLineupDefense?.toStringNull()
        home_mid.text = match.strHomeLineupMidfield?.toStringNull()
        away_mid.text = match.strAwayLineupMidfield?.toStringNull()
        home_fwd.text = match.strHomeLineupForward?.toStringNull()
        away_fwd.text = match.strAwayLineupForward?.toStringNull()
        home_subs.text = match.strHomeLineupSubstitutes?.toStringNull()
        away_subs.text = match.strAwayLineupSubstitutes?.toStringNull()
    }

    private fun addToFavorite(match: EventsItem) {
        try {
            database.use {
                insert(
                        EventsItem.TABLE_NAME,
                        EventsItem.ID_EVENT to match.idEvent,
                        EventsItem.DATE_EVENT to match.dateEvent,
                        EventsItem.TIME_EVENT to match.strTime,
                        EventsItem.ID_HOME_TEAM to match.idHomeTeam,
                        EventsItem.ID_AWAY_TEAM to match.idAwayTeam,
                        EventsItem.HOME_TEAM_NAME to match.strHomeTeam,
                        EventsItem.AWAY_TEAM_NAME to match.strAwayTeam,
                        EventsItem.HOME_SCORE to match.intHomeScore,
                        EventsItem.AWAY_SCORE to match.intAwayScore,
                        EventsItem.HOME_SHOTS to match.intHomeShots,
                        EventsItem.AWAY_SHOTS to match.intAwayShots,
                        EventsItem.HOME_GOAL_DETAILS to match.strHomeGoalDetails,
                        EventsItem.AWAY_GOAL_DETAILS to match.strAwayGoalDetails,
                        EventsItem.HOME_GOAL_KEEPER to match.strHomeLineupGoalkeeper,
                        EventsItem.AWAY_GOAL_KEEPER to match.strAwayLineupGoalkeeper,
                        EventsItem.HOME_DEFENSE to match.strHomeLineupDefense,
                        EventsItem.AWAY_DEFENSE to match.strAwayLineupDefense,
                        EventsItem.HOME_MID_FIELD to match.strHomeLineupMidfield,
                        EventsItem.AWAY_MID_FIELD to match.strAwayLineupMidfield,
                        EventsItem.HOME_FORWARD to match.strHomeLineupForward,
                        EventsItem.AWAY_FORWARD to match.strAwayLineupForward,
                        EventsItem.HOME_SUBSTITUTES to match.strHomeLineupSubstitutes,
                        EventsItem.AWAY_SUBSTITUTES to match.strAwayLineupSubstitutes
                )
            }
            scrollView.snackbar(R.string.added_to_favorite).show()
        } catch (e: SQLiteConstraintException) {
            scrollView.snackbar(e.localizedMessage).show()
        }
    }

    private fun removeFromFavorite(match: EventsItem) {
        try {
            database.use {
                delete(
                        EventsItem.TABLE_NAME, "(" + EventsItem.ID_EVENT + " = {id})",
                        "id" to match.idEvent.toString()
                )
            }
            scrollView.snackbar(R.string.removed_from_favorite).show()
        } catch (e: SQLiteConstraintException) {
            scrollView.snackbar(e.localizedMessage).show()
        }
    }
}
