package com.example.tubes1.fragments

import android.graphics.Color
import android.os.Bundle
import android.preference.PreferenceManager
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.graphics.toColor
import com.example.tubes1.CustomInfoWindow
import com.example.tubes1.ModelMain
import com.example.tubes1.R
import com.example.tubes1.databinding.FragmentGudangBinding
import org.json.JSONException
import org.json.JSONObject
import org.osmdroid.config.Configuration
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.CustomZoomButtonsController
import org.osmdroid.views.MapController
import org.osmdroid.views.overlay.Marker
import org.osmdroid.views.overlay.OverlayItem
import java.io.IOException
import java.nio.charset.StandardCharsets

class GudangFragment : Fragment() {
    var modelMainList: MutableList<ModelMain> = ArrayList()
    lateinit var mapController: MapController
    lateinit var overlayItem: ArrayList<OverlayItem>

    //view binding
    private var _binding: FragmentGudangBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {

        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        // inflater.inflate(R.layout.fragment_gudang, container, false)
        _binding = FragmentGudangBinding.inflate(inflater, container, false)
        val view = binding.root

        // main oncreate
        startApp()

        return view
    }

    private fun startApp(){
        Configuration.getInstance().load(requireActivity(), PreferenceManager.getDefaultSharedPreferences(requireContext()))

        val geoPoint = GeoPoint(-7.78165, 110.414497)
        binding.mapView.setMultiTouchControls(true)
        binding.mapView.controller.animateTo(geoPoint)
        binding.mapView.setTileSource(TileSourceFactory.DEFAULT_TILE_SOURCE)
        binding.mapView.zoomController.setVisibility(CustomZoomButtonsController.Visibility.NEVER)

        mapController = binding.mapView.controller as MapController
        mapController.setCenter(geoPoint)
        mapController.zoomTo(8)

        getLocationMarker()
    }

    private fun getLocationMarker(){
        try {
            val stream = requireContext().getAssets().open("maps_gudang.json")
            val size = stream.available()
            val buffer = ByteArray(size)
            stream.read(buffer)
            stream.close()
            val strContent = String(buffer, StandardCharsets.UTF_8)
            try {
                val jsonObject = JSONObject(strContent)
                val jsonArrayResult = jsonObject.getJSONArray("results")
                for (i in 0 until jsonArrayResult.length()){
                    val jsonObjectResult = jsonArrayResult.getJSONObject(i)
                    val modelMain = ModelMain()
                    modelMain.strName = jsonObjectResult.getString("name")
                    modelMain.strVicinity = jsonObjectResult.getString("vicinity")

                    //get lat long
                    val jsonObjectGeo = jsonObjectResult.getJSONObject("geometry")
                    val jsonObjectLoc = jsonObjectGeo.getJSONObject("location")
                    modelMain.latloc = jsonObjectLoc.getDouble("lat")
                    modelMain.longloc = jsonObjectLoc.getDouble("lng")
                    modelMainList.add(modelMain)
                }
                initMarker(modelMainList)
            }catch (e: JSONException){
                e.printStackTrace()
            }
        }catch (ignored: IOException){
            Toast.makeText(
                requireContext(),
                "Oops, ada yang tidak beres. Coba ulangi beberapa saat lagi.",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    private fun initMarker(modelList: List<ModelMain>){
        for (i in modelList.indices){
            overlayItem = ArrayList()
            overlayItem.add(
                OverlayItem(
                    modelList[i].strName, modelList[i].strVicinity, GeoPoint(
                        modelList[i].latloc, modelList[i].longloc
                    )
                )
            )
            val info = ModelMain()
            info.strName = modelList[i].strName
            info.strVicinity = modelList[i].strVicinity

            val marker = Marker(binding.mapView)
            marker.icon = resources.getDrawable(R.drawable.ic_location_on)
            marker.icon.setTint(Color.RED)
            marker.position = GeoPoint(modelList[i].latloc, modelList[i].longloc)
            marker.relatedObject = info
            marker.infoWindow = CustomInfoWindow(binding.mapView)
            marker.setOnMarkerClickListener{ item, arg1 ->
                item.showInfoWindow()
                true
            }

            binding.mapView.overlays.add(marker)
            binding.mapView.invalidate()
        }
    }

    override fun onResume() {
        super.onResume()
        Configuration.getInstance().load(requireActivity(), PreferenceManager.getDefaultSharedPreferences(requireContext()))
        if(binding.mapView != null){
            binding.mapView.onResume()
        }
    }

    override fun onPause() {
        super.onPause()
        Configuration.getInstance().load(requireActivity(), PreferenceManager.getDefaultSharedPreferences(requireContext()))
        if (binding.mapView != null){
            binding.mapView.onPause()
        }
    }

}
