package org.mousehole.americanairline.onemanstrash.view.adapter

import android.content.res.Resources
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.res.ResourcesCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import org.mousehole.americanairline.onemanstrash.R
import org.mousehole.americanairline.onemanstrash.model.Offer
import org.mousehole.americanairline.onemanstrash.utils.DebugLogger.debug
import org.mousehole.americanairline.onemanstrash.viewmodel.ListingViewModel

class ShowProductAdapter(private var offers:List<Offer>, private val delegate: SelectDelegate) : RecyclerView.Adapter<ShowProductAdapter.ShowProductViewHolder>() {

    private val listingViewModel = ListingViewModel

    interface SelectDelegate {
        fun showFrame(offer:Offer)
    }

    class ShowProductViewHolder(itemView: View, val resources: Resources) : RecyclerView.ViewHolder(itemView) {
        val productImageView : ImageView = itemView.findViewById(R.id.product_imageview)
        val itemName : TextView = itemView.findViewById(R.id.product_name_textview)
        val constraintLayout : ConstraintLayout = itemView.findViewById(R.id.background_layout)
        val numberBids : TextView = itemView.findViewById(R.id.number_bids)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ShowProductViewHolder {
        val itemView : View = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_for_sale_list_item, parent, false)
        return ShowProductViewHolder(itemView, parent.resources)
    }

    override fun onBindViewHolder(holder: ShowProductViewHolder, position: Int) {
        val product = offers[position]
        val id = listingViewModel.getUid()
        val watcher = product.watchers
        debug("UId: $id \n watchers? $watcher \n  ${watcher.contains(id)}")
        val sortedMap = product.bidOn.toSortedMap()
        when {
            product.seller == id ->
                holder.constraintLayout
                    .background = ResourcesCompat.getDrawable(holder.resources,
                    R.drawable.product_seller_list_item, null)
            sortedMap.size > 0 && sortedMap[sortedMap.lastKey()] == id ->
                holder.constraintLayout
                        .background = ResourcesCompat.getDrawable(holder.resources,
                        R.drawable.winning_bidder_list_item, null)
            product.bidOn.contains(id) ->
                holder.constraintLayout
                    .background = ResourcesCompat.getDrawable(holder.resources,
                        R.drawable.product_bidon_list_item, null)
            product.watchers.containsKey(id) -> {
                holder.constraintLayout
                        .background = ResourcesCompat.getDrawable(holder.resources,
                        R.drawable.watcher_list_item, null)
            }

            else ->
                holder.constraintLayout
                    .background = ResourcesCompat.getDrawable(holder.resources,
                        R.drawable.product_list_item, null)
        }
        Glide.with(holder.itemView).load(product.url).into(holder.productImageView)
        holder.numberBids.text = product.bidOn.size.toString()
        holder.itemName.text = product.name
        holder.constraintLayout.setOnClickListener {
            debug("What is product watchers? ${product.watchers}")
            delegate.showFrame(product)
        }
    }

    override fun getItemCount(): Int =
        offers.size

    fun setProducts(offers:List<Offer>) {
        this.offers = offers
        notifyDataSetChanged()
    }
}