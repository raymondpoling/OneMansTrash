package org.mousehole.americanairline.onemanstrash.view

import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import org.mousehole.americanairline.onemanstrash.R
import org.mousehole.americanairline.onemanstrash.model.Offer
import org.mousehole.americanairline.onemanstrash.utils.Constants.OFFER
import org.mousehole.americanairline.onemanstrash.utils.DebugLogger.debug
import org.mousehole.americanairline.onemanstrash.viewmodel.WatchViewModel

class ShowSingleProductFragment : Fragment() {

    private val watchViewModel = WatchViewModel

    private lateinit var productDisplayImageView : ImageView
    private lateinit var productNameTextView : TextView
    private lateinit var productDescriptionTextView : TextView
    private lateinit var productSellerTextView: TextView
    private lateinit var productExpirationDateTextView : TextView

    private lateinit var bidButton : Button
    private lateinit var watchButton : Button
    private lateinit var closeButton : Button

    override fun onCreateView(inflater: android.view.LayoutInflater, container: android.view.ViewGroup?, savedInstanceState: android.os.Bundle?): android.view.View? {
        return inflater.inflate(R.layout.show_single_product_fragment_layout,
                container,
                false)
    }

    override fun onViewCreated(view: android.view.View, savedInstanceState: android.os.Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // bind ui
        productDisplayImageView = view.findViewById(R.id.product_imageview)
        productNameTextView = view.findViewById(R.id.product_name_textview)
        productDescriptionTextView = view.findViewById(R.id.product_description_textview)
        productSellerTextView = view.findViewById(R.id.seller_textview)
        productExpirationDateTextView = view.findViewById(R.id.expiration_date_textview)

        // set values in ui
        val offer : Offer = (arguments?.getSerializable(OFFER) ?: Offer()) as Offer
        productNameTextView.text = offer.name
        productDescriptionTextView.text = offer.description
        productSellerTextView.text = offer.email
        productExpirationDateTextView.text = offer.expiration

        Glide.with(requireContext()).load(offer.url).into(productDisplayImageView)

        bidButton = view.findViewById(R.id.bid_button)
        val sortedMap = offer.bidOn.toSortedMap()
        val lastKey : String? = if(sortedMap.size > 0) sortedMap.lastKey() else null
        val lastItem = lastKey?.let{ offer.bidOn[lastKey] }
        bidButton.isEnabled = lastItem != watchViewModel.getUid()
        bidButton.setOnClickListener {
            watchViewModel.bid(offer)
            bidButton.isEnabled = false
        }

        watchButton = view.findViewById(R.id.watch_button)
        watchButtonSetText(offer)
        watchButton.setOnClickListener {
            val id = watchViewModel.getUid()
            debug("clicking! $id ${offer.watchers}")
            if(offer.watchers.containsKey(id)) {
                watchViewModel.deleteWatch(offer)
                offer.watchers.remove(id)
                watchButtonSetText(offer)
            } else {
                watchViewModel.setWatch(offer)
                offer.watchers[id] = true
                watchButtonSetText(offer)
            }
        }

        closeButton = view.findViewById(R.id.close_button)
        closeButton.setOnClickListener {
            requireFragmentManager()
                    .popBackStack()
        }
    }
private fun watchButtonSetText(offer:Offer) {
    if(offer.watchers.containsKey(watchViewModel.getUid())) {
        watchButton.text = getString(R.string.unwatch)
    } else {
        watchButton.text = getString(R.string.watch)
    }
}

}