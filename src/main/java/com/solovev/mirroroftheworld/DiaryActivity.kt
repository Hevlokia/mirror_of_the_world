package com.solovev.mirroroftheworld

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import com.example.mirroroftheworld.R
import com.solovev.mirroroftheworld.binding.MainActivity
import com.solovev.mirroroftheworld.ui.main.DiaryFragment
import java.util.*

private const val TAG = "DiaryActivity"

class DiaryActivity : AppCompatActivity(),
    DiaryListFragment.Callbacks {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.diary_activity)
        if (savedInstanceState == null) {
            val fragment = DiaryListFragment.newInstance()
            supportFragmentManager
                .beginTransaction()
                .add(R.id.fragment_container, fragment)
                .commit()
            
        }

    }
    override fun onCrimeSelected(crimeId: UUID) {
        val fragment = DiaryFragment.newInstance(crimeId)
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.fragment_container, fragment)
            .addToBackStack(null)
            .commit()
    }
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return super.onCreateOptionsMenu(menu)

    }
    override fun onPrepareOptionsMenu(menu: Menu?): Boolean {
        menu?.getItem(0)?.isVisible = false

        return super.onPrepareOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {


        when(item.itemId){

            R.id.sign_out -> finish()

            R.id.chat -> {val i = Intent(this, MainActivity::class.java)
                startActivity(i)

            }

        }
        return super.onOptionsItemSelected(item)
    }

}