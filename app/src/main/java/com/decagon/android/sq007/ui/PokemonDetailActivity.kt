package com.decagon.android.sq007.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import com.bumptech.glide.Glide
import com.decagon.android.sq007.model.Common
import com.byoyedele.pokemoon.Pokemon
import com.decagon.android.sq007.model.PokemonApi
import com.decagon.android.sq007.R
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*

/* declear all the views id attached to this activity */
lateinit var lnDetailLinearLayout:LinearLayout
lateinit var tvDetailPokmonNewName:TextView
lateinit var ivDetailPokemonImage:ImageView
lateinit var tvDetailPokemonHeight:TextView
lateinit var tvDetailPokemonWeight:TextView
lateinit var tvDetailPokemonAbilitiesList:TextView
lateinit var tvDetailPokemonBaseExperience:TextView
lateinit var tvDetailPokemonFormList:TextView
lateinit var tvDetailPokemonGameIndicesList:TextView
lateinit var tvDetailPokemonHeld_items_list:TextView
lateinit var tvDetailPokemonMoves_list:TextView
lateinit var tvDetailPokemonOrder:TextView
lateinit var tvDetailPokemonSpecies:TextView
lateinit var tvDetailPokemonStats:TextView
lateinit var tvDetailPokemontypes:TextView


class PokemonDetailActivity : AppCompatActivity() {
    private lateinit var pokeyName: String
    private lateinit var pokeyUrl: String
    private var pokeyId: Int = 0
    private lateinit var service: PokemonApi
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pokemon_detail)

        // Get the values of the keys that were sent on click from Recycler Adapter.
        val bundle = intent
        pokeyName = ""
        pokeyUrl = ""
        pokeyId = 0

        if (bundle != null) {
            pokeyName = bundle.extras?.getString("NAME").toString().toUpperCase(Locale.ROOT)    // Get the value of the name and convert it to Uppercase.
            pokeyUrl = bundle.extras?.getString("URL").toString()                               // Get the value of the URL and set as PokeyURL
            pokeyId = bundle.extras?.getInt("ID") ?: 0                                          // Get the value of ID
        }

        ivDetailPokemonImage = findViewById(R.id.ivDetailPokemonImage)
        tvDetailPokmonNewName = findViewById<TextView>(R.id.tvDetailPokmonNewName)
        tvDetailPokmonNewName.text = pokeyName
        Glide.with(this).load(pokeyUrl).into(ivDetailPokemonImage)


        service = Common.retrofitService

        service.getPokey(pokeyId).enqueue(object : Callback<Pokemon> {
            override fun onFailure(call: Call<Pokemon>, error: Throwable) {
                Toast.makeText(this@PokemonDetailActivity, "$error", Toast.LENGTH_LONG).show()
            }

            // OnResponse of the Data, set them to the appropriate Textviews already set on the activity_pokey_detail XML file

            override fun onResponse(call: Call<Pokemon>, response: Response<Pokemon>) {

                tvDetailPokemonHeight = findViewById(R.id.tvDetailPokemonHeight)
                tvDetailPokemonWeight = findViewById(R.id.tvDetailPokemonWeight)
                tvDetailPokemonAbilitiesList = findViewById(R.id.tvDetailPokemonAbilitiesList)
                tvDetailPokemonBaseExperience = findViewById(R.id.tvDetailPokemonBaseExperience)
                tvDetailPokemonFormList = findViewById(R.id.tvDetailPokemonFormList)
                tvDetailPokemonGameIndicesList = findViewById(R.id.tvDetailPokemonGameIndicesList)
                tvDetailPokemonHeld_items_list = findViewById(R.id.tvDetailPokemonHeld_items_list)
                tvDetailPokemonMoves_list = findViewById(R.id.tvDetailPokemonMoves_list)
                tvDetailPokemonOrder = findViewById(R.id.tvDetailPokemonOrder)
                tvDetailPokemonSpecies = findViewById(R.id.tvDetailPokemonSpecies)
                tvDetailPokemonStats = findViewById(R.id.tvDetailPokemonStats)
                tvDetailPokemontypes = findViewById(R.id.tvDetailPokemontypes)

                if (response.body() != null) {
                    tvDetailPokemonHeight.text = """H: ${response.body()?.height.toString()}m"""
                    tvDetailPokemonWeight.text = "W: ${response.body()?.weight.toString()}kg"
                    tvDetailPokemonAbilitiesList.text = "Abilities: ${response.body()?.abilities?.joinToString { it.ability.name }}"
                    tvDetailPokemonFormList.text = "Forms: ${response.body()?.forms?.joinToString { it.name }}"
                    tvDetailPokemonBaseExperience.text = "Base Experience: ${response.body()?.baseExperience.toString()}"
                    tvDetailPokemonGameIndicesList.text = "Game Indices: ${response.body()?.gameIndices?.joinToString { it.gameIndex.toString() }}"
                    tvDetailPokemonHeld_items_list.text = "Held Items: ${response.body()?.heldItems?.joinToString { it.item.name }}"
                    tvDetailPokemonMoves_list.text = "Moves: ${response.body()?.moves?.joinToString { it.move.name }}"
                    tvDetailPokemonOrder.text = "Order: ${response.body()?.order.toString()}"
                    tvDetailPokemonSpecies.text = "Species: ${response.body()?.species?.name.toString()}"
                    tvDetailPokemonStats.text = "Stats: ${response.body()?.stats?.joinToString { it.stat.name }}"
                    tvDetailPokemontypes.text = "Types: ${response.body()?.types?.joinToString { it.type.name }}"
                } else {
                    Toast.makeText(this@PokemonDetailActivity, "Oga, Your Village People Don Dey Work : ${response.body()}", Toast.LENGTH_LONG).show()
                    //On failure, display failure message
                }
            }
        })
    }
}