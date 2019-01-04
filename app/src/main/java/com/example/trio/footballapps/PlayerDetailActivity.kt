package com.example.trio.footballapps

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.MenuItem
import com.bumptech.glide.Glide
import com.example.trio.footballapps.model.PlayerItem
import kotlinx.android.synthetic.main.activity_player_detail.*

class PlayerDetailActivity : AppCompatActivity() {
    val TAG = this::class.java.simpleName
    private var player: PlayerItem? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_player_detail)

        supportActionBar?.title = "Player Detail"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        this.player = intent.getParcelableExtra<PlayerItem>("player")
        Log.wtf(TAG, this.player!!.strPlayer)

        showDetail(this.player!!)
    }

    fun showDetail(player: PlayerItem) {
        name.text = player.strPlayer
        Glide.with(this).load(player.strThumb).into(profile)
        position.text = player.strPosition
        if (player.strWeight!!.equals("0") or player.strWeight.equals("")) weight.text = "-" else weight.text = player.strWeight
        height.text = player.strHeight!!.split(" (")[0]
        desc.text = player.strDescriptionEN
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                finish()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}
