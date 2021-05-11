package com.decagon.android.sq007.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.byoyedele.pokemoon.Result
import com.decagon.android.sq007.R
import com.decagon.android.sq007.ui.PokemonDetailActivity
import java.util.*

class RecyclerAdapter(val context: Context, private val pokemon: List<Result>) :
    RecyclerView.Adapter<RecyclerAdapter.MyViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.pokemon_recycler_item, parent, false)
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val pokey = pokemon[position]
        holder.setData(pokey, position)
    }

    override fun getItemCount(): Int {
        return pokemon.size
    }

    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvRecyclerItemPokeyName = itemView.findViewById<TextView>(R.id.tvRecyclerItemPokeyName)
        val ivRecyclerItemBgImg = itemView.findViewById<ImageView>(R.id.ivRecyclerItemBgImg)
        val ivRecyclerItemBgImgPokeyImg = itemView.findViewById<ImageView>(R.id.ivRecyclerItemBgImgPokeyImg)

        var currentPokey: Result? = null
        var currentPos = 0
        var currentUrl = ""
        var currentId = 0
//
        // The onClickListener is set on the init here in the MyViewHolder inner class
        // This is used to launch a new activity which goes along with the necessary information.

        init {
            itemView.setOnClickListener {
//               Toast.makeText(context, "${currentPokey?.name} and ${currentPokey?.url}", Toast.LENGTH_LONG).show()
                val intent = Intent(itemView.context, PokemonDetailActivity::class.java).apply {
                    putExtra("NAME", currentPokey?.name)
                    putExtra("URL", currentUrl)
                    putExtra("ID", currentId)
                }
                itemView.context.startActivity(intent)
            }
        }

        fun setData(pokey: Result?, position: Int) {
            tvRecyclerItemPokeyName.text = pokey!!.name.toUpperCase(Locale.ROOT)
            val pokeyUrl = pokey.url
            val imgUrl = getPokeyId(pokeyUrl)
            this.currentUrl = imgUrl
            this.currentPokey = pokey
            this.currentPos = position
            this.currentId = pokeyUrl.substring(34, pokeyUrl.length - 1).toInt()

            Glide.with(context).load(imgUrl).into(ivRecyclerItemBgImgPokeyImg)
            Glide.with(context).load(imgUrl).into(ivRecyclerItemBgImg)
        }
    }

    private fun getPokeyId(item: String): String {
        val id = item.substring(34, item.length - 1).toInt()
        return "https://pokeres.bastionbot.org/images/pokemon/$id.png"
    }
}
