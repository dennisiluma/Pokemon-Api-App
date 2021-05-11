package com.decagon.android.sq007.ui

import android.content.Context
import android.net.ConnectivityManager
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.decagon.android.sq007.R
import com.decagon.android.sq007.adapter.RecyclerAdapter
import com.decagon.android.sq007.model.Common
import com.decagon.android.sq007.model.PokeAll
import com.decagon.android.sq007.model.PokemonApi
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() {
    /* Some late decleration */
    private lateinit var service: PokemonApi
    lateinit var mAdapter: RecyclerAdapter
    lateinit var recyclerView: RecyclerView

    /*Declare All the Views Id */
    lateinit var etSetCount: EditText
    lateinit var btSetCount: Button
    lateinit var btReload: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        checkConnectivity()
        etSetCount = findViewById(R.id.etSetCount)
        etSetCount.setText("100")
        var limit: Int = etSetCount.text.toString().toInt()
        service = Common.retrofitService
        recyclerView = findViewById(R.id.recyclerView)
        recyclerView.setHasFixedSize(true)
        recyclerView.layoutManager = LinearLayoutManager(this)

        launchService(limit)
        btSetCount = findViewById(R.id.btSetCount)
        btSetCount.setOnClickListener {
            limit = btSetCount.text.toString().toInt()
            launchService(limit)
        }

        btReload = findViewById(R.id.btReload)
        btReload.setOnClickListener {
            recreate()
        }
    }

    private fun launchService(limit: Int) {
        service.get(limit).enqueue(object : Callback<PokeAll> {
            override fun onFailure(call: Call<PokeAll>, error: Throwable) {
                Toast.makeText(this@MainActivity, "$error", Toast.LENGTH_LONG).show()
            }

            override fun onResponse(call: Call<PokeAll>, response: Response<PokeAll>) {
                mAdapter = RecyclerAdapter(baseContext, response.body()?.results!!)
                mAdapter.notifyDataSetChanged()
                recyclerView.adapter = mAdapter
            }
        })
    }

    private fun checkConnectivity() {

        btReload = findViewById(R.id.btReload)
        val cm = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetwork = cm.activeNetworkInfo
        val isConnected = activeNetwork != null && activeNetwork.isConnectedOrConnecting

        if (!isConnected) {
            Toast.makeText(this, "Switch On Your network and Refresh", Toast.LENGTH_LONG).show()
            btReload.visibility = View.VISIBLE
        } else btReload.visibility = View.GONE
    }
}
