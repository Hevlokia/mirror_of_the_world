package com.solovev.mirroroftheworld.binding

import android.content.Context
import android.content.Intent
import android.graphics.drawable.BitmapDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.solovev.mirroroftheworld.DiaryActivity
import com.example.mirroroftheworld.R
import com.example.mirroroftheworld.databinding.ActivityMainBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.squareup.picasso.Picasso
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding
    lateinit var auth:FirebaseAuth
    lateinit var adapter: UserAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        auth = Firebase.auth
        setUpActionBar()
        val database = Firebase.database
        val myRef = database.getReference("message")


        binding.buttonSend.setOnClickListener {
            myRef.child(myRef.push().key ?:"LOL").setValue(User(auth.currentUser?.displayName,binding.edMessag.text.toString()))
            binding.edMessag.text = null
            val view = this.currentFocus
            if (view != null) {
                val hideMy = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                hideMy.hideSoftInputFromWindow(view.windowToken, 0)
            } else {

                window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN)
            }

        }


        binding.edMessag.setOnClickListener {
            binding.edMessag.text = null
        }


        CoroutineScope(Dispatchers.IO).launch {
            initRecyclerView()
            onChangeListener(myRef)
        }



    }
    private fun initRecyclerView() = with(binding){
        adapter = UserAdapter()
        recyclerView.layoutManager = LinearLayoutManager( this@MainActivity)
        recyclerView.adapter = adapter

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return super.onCreateOptionsMenu(menu)

    }

    override fun onPrepareOptionsMenu(menu: Menu?): Boolean {
        menu?.getItem(1)?.isVisible = false

        return super.onPrepareOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {


        when(item.itemId){

            R.id.sign_out -> finish()

            R.id.diary ->{val i = Intent(this, DiaryActivity::class.java)
            startActivity(i)
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun onChangeListener(dRef :DatabaseReference ){
        dRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
               val list = ArrayList<User>()
                for (s in snapshot.children){
                    val user = s.getValue(User::class.java)
                    if (user!=null)list.add(user)

                }
                adapter.submitList(list)
            }

            override fun onCancelled(error: DatabaseError) {

            }

        })
    }

    private fun setUpActionBar(){
        val actionBar = supportActionBar
        Thread{
            val bitMap = Picasso.get().load(auth.currentUser?.photoUrl).get()
            val drawableIcon = BitmapDrawable(resources,bitMap)
            runOnUiThread{
                actionBar?.setDisplayHomeAsUpEnabled(true)
                actionBar?.setHomeAsUpIndicator(drawableIcon)
                actionBar?.title = auth.currentUser?.displayName
            }

        }.start()

    }



}