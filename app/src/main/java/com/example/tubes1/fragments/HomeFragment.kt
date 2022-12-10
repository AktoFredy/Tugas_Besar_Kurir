package com.example.tubes1.fragments

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.denzcoskun.imageslider.constants.ScaleTypes
import com.denzcoskun.imageslider.models.SlideModel
import com.example.tubes1.Kiriman.KirimanCRUDActivity
import com.example.tubes1.Penerima.PenerimaCRUDActivity
import com.example.tubes1.R
import com.example.tubes1.databinding.FragmentHomeBinding

class HomeFragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
       // return inflater.inflate(R.layout.fragment_home, container, false)
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_home, container, false)
        return binding.root
    }

    companion object {
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val imgList = ArrayList<SlideModel>()

        imgList.add(SlideModel("https://d1csarkz8obe9u.cloudfront.net/posterpreviews/food-deal-offer-video-ad-design-template-7860739098a2ec5f0e4bbd802c6b9308_screen.jpg"))
        imgList.add(SlideModel("https://d1csarkz8obe9u.cloudfront.net/posterpreviews/autumn-sale-video-advert-promo-store-retail-shop-foliage-design-template-53c791b59014032859e61cddf0a8c965_screen.jpg"))
        imgList.add(SlideModel("https://d1csarkz8obe9u.cloudfront.net/posterpreviews/milkshake-%26-smoothie-promo-design-template-76b7c7397ceeec615336a3493579cdef_screen.jpg"))
        imgList.add(SlideModel("https://d1csarkz8obe9u.cloudfront.net/posterpreviews/super-weekend-discount-sale-promo-special-ad-design-template-8534c64327a8a8a2a5e7ad15f12a340b_screen.jpg"))
        imgList.add(SlideModel("https://d1csarkz8obe9u.cloudfront.net/posterpreviews/pizza-banner-design-template-3f9ce3fbcc33ae4264b2067e7228e45f_screen.jpg"))

        binding.imgPromoSlider.setImageList(imgList, ScaleTypes.FIT)

        binding.barangList.setOnClickListener {
            startActivity(Intent(requireActivity(), KirimanCRUDActivity::class.java))
        }

        binding.penerimaList.setOnClickListener {
            startActivity(Intent(requireActivity(), PenerimaCRUDActivity::class.java))
        }
    }
}