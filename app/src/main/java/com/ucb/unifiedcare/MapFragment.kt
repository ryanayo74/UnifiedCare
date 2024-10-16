package com.ucb.unifiedcare

import ModelClass.NominatimResult
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import com.ucb.unifiedcare.unifiedcare.therapist.NominatimRetrofit
import org.osmdroid.api.IMapController
import org.osmdroid.views.MapView
import org.osmdroid.config.Configuration
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.overlay.Marker
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class MapFragment : Fragment() {
    private lateinit var mapView: MapView
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_map, container, false)
        mapView = view.findViewById(R.id.map)
        // Setup the map
        mapSetup()
        return view
    }
    private fun mapSetup(){
        //osmdroid config
        Configuration.getInstance().userAgentValue = requireActivity().packageName

        //setup mapview
        mapView.setTileSource(TileSourceFactory.MAPNIK)
        mapView.setMultiTouchControls(true)

        //setup map controller
        val mapController: IMapController = mapView.controller
        mapController.setZoom(15.0)
        val startPoint = GeoPoint(51.505, -0.09)
        mapController.setCenter(startPoint)
    }
    override fun onResume() {
        super.onResume()
        mapView.onResume()
    }

    override fun onPause() {
        super.onPause()
        mapView.onPause()
    }
    public fun geocodeAddress(address: String, callback: (Double?, Double?) -> Unit) {
        NominatimRetrofit.nominatimApi.geocodeAddress(address).enqueue(object :
            Callback<List<NominatimResult>?> {
            override fun onResponse(
                call: Call<List<NominatimResult>?>,
                response: Response<List<NominatimResult>?>
            ) {
                if (response.isSuccessful && response.body() != null) {
                    val results = response.body()
                    if (results != null && results.isNotEmpty()) {
                        val latitude = results[0].latitude.toDouble()
                        val longitude = results[0].longitude.toDouble()
                        // Add marker on the map with the returned coordinates
                        addMarker(latitude, longitude)
                        // Invoke the callback with the coordinates
                        callback(latitude, longitude)
                    } else {
                        Log.e("Geocoding", "Address not found.")
                        callback(null, null) // Invoke callback with null if address not found
                    }
                } else {
                    Log.e("Geocoding", "Error: ${response.message()}")
                    callback(null, null) // Handle error and invoke callback with null
                }
            }

            override fun onFailure(call: Call<List<NominatimResult>?>, t: Throwable) {
                Log.e("Geocoding", "Failure: ${t.message}")
                callback(null, null) // Handle failure and invoke callback with null
            }
        })
    }


    private fun addMarker(latitude: Double, longitude: Double) {
        val geoPoint = GeoPoint(latitude, longitude)
        val marker = Marker(mapView)
        marker.position = geoPoint
        marker.title = "Dynamic Location"
        mapView.overlays.add(marker)
        mapView.controller.setCenter(geoPoint)
        marker.showInfoWindow()
    }
}