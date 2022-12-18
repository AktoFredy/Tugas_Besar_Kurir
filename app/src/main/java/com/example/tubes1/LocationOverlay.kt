package com.example.tubes1

import android.content.Context
import android.graphics.Point
import android.graphics.drawable.Drawable
import org.osmdroid.api.IMapView
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.overlay.ItemizedOverlay
import org.osmdroid.views.overlay.OverlayItem


class LocationOverlay(context: Context) : ItemizedOverlay<OverlayItem>(context, context.getDrawable(R.drawable.ic_location_on)) {
    private val items = mutableListOf<OverlayItem>()

    init {
        // Add marker to list of items
        val drawable = context.getDrawable(R.drawable.ic_location_on)
        val marker = OverlayItem("Current location", "", GeoPoint(0.0, 0.0))
        marker.setMarker(drawable)
        items.add(marker)

        // Populate the overlay with the items
        populate()
    }

    override fun onSnapToItem(x: Int, y: Int, snapPoint: Point?, mapView: IMapView?): Boolean {
        return true
    }

    override fun createItem(index: Int): OverlayItem {
        return items[index]
    }

    override fun size(): Int {
        return items.size
    }
}