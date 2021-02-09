package pl.polsl.piotrgazda

import android.Manifest
import android.Manifest.permission.ACCESS_COARSE_LOCATION
import android.Manifest.permission.ACCESS_FINE_LOCATION
import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.location.LocationManager
import android.net.Uri
import android.os.Bundle
import android.speech.RecognizerIntent
import android.webkit.WebView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import java.util.*


class Prezenty : AppCompatActivity() {

    val presentsList: MutableList<String> = mutableListOf<String>()
    lateinit var pathToPhoto: Uri
    var longitude:Double = 0.0
    var latitude:Double = 0.0
    private lateinit var fusedLocationClient: FusedLocationProviderClient


    @SuppressLint("JavascriptInterface")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        //WebView - kontrolka wyswietlajaca html
        val page = WebView(this)
        //wlaczenie obslugi JS
        page.settings.javaScriptEnabled=true

        //dodanie interfejsu pomiędzy Kotlinem a JS
        //this - obiekt tej klasy dostarcza metody JSInterface, activity - nazwa widoczna w JS
        page.addJavascriptInterface(PrezentyJSInterface(this), "prezenty")

        //zaladowanie zawartosci kontroli WebView - pliki z katalogu assests w projekcie
        page.loadUrl("file:///android_asset/prezenty.html")

        //wstawienie kontrolki WebView jako calej fasady aktywnosci
        setContentView(page)

    }


    override fun onActivityResult(requstCode:Int, resultCode:Int, data:Intent?)
    {
        super.onActivityResult(requstCode, resultCode, data)
        if (resultCode==RESULT_OK)
        {
            if(requstCode == 0) {
                data?.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS)?.let { presentsList.addAll(presentsList.size, it) }
            }
            else if(requstCode == 1){
                actualizeLocation()
                generatePresentList()
            }
        }
    }


    fun actualizeLocation() {

       if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, arrayOf(ACCESS_FINE_LOCATION), 1)
            ActivityCompat.requestPermissions(this, arrayOf(ACCESS_COARSE_LOCATION), 1)
        }

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            val gps =
            getSystemService(LOCATION_SERVICE) as LocationManager

            val location= gps.getLastKnownLocation(LocationManager.NETWORK_PROVIDER)

            longitude = location?.longitude!!
            latitude = location?.latitude!!
        }
    }

    fun generatePresentList(){
        val wishList = presentsList as (ArrayList<String>)
        val intent = Intent(this, Kartka::class.java).apply {
            putExtra("WISH_LIST", wishList)
            putExtra("PHOTO_PATH", pathToPhoto)
            putExtra("LONGITUDE", longitude)
            putExtra("LATITUDE", latitude)
        }
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        startActivity(intent)
    }
}