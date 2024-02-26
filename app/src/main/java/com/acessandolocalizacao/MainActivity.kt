package com.acessandolocalizacao

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import java.text.SimpleDateFormat
import java.util.Date

@SuppressLint("ServiceCast")
class MainActivity : AppCompatActivity() {

    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var locationManager: LocationManager
    private var lastLocationUpdateTime: Long = 0

    private var atualizacaoDeLocalizacaoEnabled = false

    lateinit var txtLatitude: TextView
    lateinit var txtLongitude: TextView
    lateinit var botao: Button
    lateinit var txtDataAtualizacao: TextView
    private var locationListener: LocationListener? = null



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        txtLongitude = findViewById(R.id.idtxtLongitudeValor)
        txtLatitude = findViewById(R.id.idtxtlatitudeValor)
        txtDataAtualizacao = findViewById(R.id.idtxtDataAtualizacao)
        botao = findViewById(R.id.idButton)
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager

        permissaoLocalizacaoConcedida()
        /*botao.setOnClickListener{
            if(locationListener == null){
                startLocationListener()
                botao.text = "Parar atualizacoes"
            }else{
                stopGetLocationUpdate()
                botao.text = "Iniciar atualizacoes"
            }
        }*/
        botao.setOnClickListener{getLocation()}
    }

    private fun permissaoLocalizacaoConcedida() {
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED &&
            ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ),
                123
            )
        }
    }

    /*private fun startLocationListener(){
        locationListener = object : LocationListener {
            override fun onLocationChanged(location: Location) {
                val latitude = location.latitude.toString()
                val longitude = location.longitude.toString()
                txtLongitude.text = longitude
                txtLatitude.text = latitude
                val dateFormat = SimpleDateFormat("dd/MM/yyyy HH:mm:ss")
                val ultimaAtualizacao = dateFormat.format(Date(lastLocationUpdateTime))
                txtDataAtualizacao.text = ultimaAtualizacao
                Toast.makeText(
                    applicationContext,
                    "Latitude: $latitude, Longitude: $longitude",
                    Toast.LENGTH_SHORT
                ).show()
                lastLocationUpdateTime = System.currentTimeMillis()
            }

            override fun onStatusChanged(provider: String?, status: Int, extras: Bundle?) {}

            override fun onProviderEnabled(provider: String) {}

            override fun onProviderDisabled(provider: String) {}
        }

        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            locationManager.requestLocationUpdates(
                LocationManager.GPS_PROVIDER,
                5000,
                0f,
                locationListener!!
            )
        }

    }
    private fun stopGetLocationUpdate(){
        locationListener?.let { locationManager.removeUpdates(it) }
        locationListener = null
    }*/

    private fun getLocation() {
        val hasGps = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
        val hasNetwork = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)

        val locationListener: LocationListener = object : LocationListener {
            override fun onLocationChanged(location: Location) {

                val latitude = location.latitude.toString()
                val longitude = location.longitude.toString()


                txtLongitude.setText(longitude)
                txtLatitude.setText(latitude)


                val dateFormat = SimpleDateFormat("dd/MM/yyy HH:mm:ss")
                val ultimaAtualizacao = dateFormat.format(Date(lastLocationUpdateTime)).toString()

                Toast.makeText(
                    applicationContext,
                    "Latitude: $latitude, Longitude: $longitude",
                    Toast.LENGTH_SHORT
                ).show()
                lastLocationUpdateTime = System.currentTimeMillis()

                txtDataAtualizacao.setText(ultimaAtualizacao)
            }

            override fun onStatusChanged(provider: String?, status: Int, extras: Bundle?) {}

            override fun onProviderEnabled(provider: String) {}

            override fun onProviderDisabled(provider: String) {}
        }



        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            if (hasGps) {
                locationManager.requestLocationUpdates(
                    LocationManager.GPS_PROVIDER,
                    5000,
                    0f,
                    locationListener
                )
            }
            if (hasNetwork) {
                locationManager.requestLocationUpdates(
                    LocationManager.NETWORK_PROVIDER,
                    5000,
                    0f,
                    locationListener
                )
            }


            val lastKnownLocation: Location? =
                locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER)
            if (lastKnownLocation != null) {
                val latitude = lastKnownLocation.latitude
                val longitude = lastKnownLocation.longitude

                Toast.makeText(
                    applicationContext,
                    "Ultima Latitude: $latitude, Longitude: $longitude conhecida",
                    Toast.LENGTH_SHORT
                ).show()
            }
            else{
                Toast.makeText(
                    applicationContext,
                    "Última localização GPS não disponível",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }


}
