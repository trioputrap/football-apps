package com.example.trio.footballapps

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.example.trio.footballapps.menu.favorite.FavoriteFragment
import com.example.trio.footballapps.menu.match.MatchFragment
import com.example.trio.footballapps.menu.team.TeamFragment
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        supportActionBar?.title = "Football Apps"

        bottom_navigation.setOnNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.menu_matches -> {
                    loadMatchFragment(savedInstanceState)
                }

                R.id.menu_teams -> {
                    loadTeamFragment(savedInstanceState)
                }

                R.id.menu_favourites -> {
                    loadFavoriteFragment(savedInstanceState)
                }
            }
            true
        }

        bottom_navigation.selectedItemId = R.id.menu_matches
    }

    private fun loadMatchFragment(savedInstanceState: Bundle?) {
        if (savedInstanceState == null) {
            supportFragmentManager
                    .beginTransaction()
                    .replace(R.id.main_container,
                            MatchFragment(), MatchFragment::class.java.simpleName)
                    .commit()
        }
    }

    private fun loadTeamFragment(savedInstanceState: Bundle?) {
        if (savedInstanceState == null) {
            supportFragmentManager
                    .beginTransaction()
                    .replace(R.id.main_container,
                            TeamFragment(), TeamFragment::class.java.simpleName)
                    .commit()
        }
    }

    private fun loadFavoriteFragment(savedInstanceState: Bundle?) {
        if (savedInstanceState == null) {
            supportFragmentManager
                    .beginTransaction()
                    .replace(R.id.main_container,
                            FavoriteFragment(), FavoriteFragment::class.java.simpleName)
                    .commit()
        }
    }
}
