package pl.polsl.piotrgazda

import android.annotation.SuppressLint
import android.app.Activity
import android.net.Uri
import android.os.Bundle
import android.webkit.WebView
import kotlin.collections.ArrayList

class Kartka : Activity() {

    var listOfPresents = mutableListOf<String>()

    lateinit var photoPath:Uri

    lateinit var longitude:String

    lateinit var latitude:String

    @SuppressLint("JavascriptInterface")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val newWishes = intent.extras
        if (newWishes != null) {
            listOfPresents.addAll(listOfPresents.size, newWishes.get("WISH_LIST") as ArrayList<String>)

            photoPath = newWishes.get("PHOTO_PATH") as Uri

            longitude = (newWishes.get("LONGITUDE") as Double).toString()
            latitude = (newWishes.get("LATITUDE") as Double).toString()
        }

        //WebView - kontrolka wyswietlajaca html
        val page = WebView(this)
        //wlaczenie obslugi JS
        page.settings.javaScriptEnabled=true

        //dodanie interfejsu pomiędzy Kotlinem a JS
        //this - obiekt tej klasy dostarcza metody JSInterface, activity - nazwa widoczna w JS
        page.addJavascriptInterface(KartkaJSInterface(this), "kartka")

        //zaladowanie zawartosci kontroli WebView - pliki z katalogu assests w projekcie
        page.loadUrl("file:///android_asset/kartka.html")

        //wstawienie kontrolki WebView jako calej fasady aktywnosci
        setContentView(page)
    }

}