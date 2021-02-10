package org.mousehole.americanairline.onemanstrash.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import org.mousehole.americanairline.onemanstrash.R
import org.mousehole.americanairline.onemanstrash.model.Offer
import org.mousehole.americanairline.onemanstrash.utils.Constants.OFFER
import org.mousehole.americanairline.onemanstrash.view.adapter.ShowProductAdapter
import org.mousehole.americanairline.onemanstrash.viewmodel.ListingViewModel

class ShowListedItemsFragment : Fragment(), ShowProductAdapter.SelectDelegate {

    private val listingViewModel = ListingViewModel

    private lateinit var offerDisplayFrameLayout : FrameLayout
    private lateinit var recyclerView : RecyclerView
    private val adapter = ShowProductAdapter(listOf(), this)

    private val viewProductFragment = ShowSingleProductFragment()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.show_products_fragment_layout, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        offerDisplayFrameLayout = view.findViewById(R.id.offer_display_framelayout)

        recyclerView = view.findViewById(R.id.show_products_recyclerview)
        recyclerView.adapter = adapter

              listingViewModel.getData().observe(viewLifecycleOwner, {
                    adapter.setProducts(it)
                })

        }
    override fun showFrame(offer: Offer) {
        val bundle = Bundle()
        bundle.putSerializable(OFFER, offer)
        viewProductFragment.arguments = bundle
        requireFragmentManager()
            .beginTransaction()
            .add(R.id.offer_display_framelayout, viewProductFragment)
            .addToBackStack(viewProductFragment.tag)
            .commit()
    }
}