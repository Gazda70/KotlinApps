package pl.polsl.piotrgazda

import android.webkit.JavascriptInterface

class KartkaJSInterface(val myKartka:Kartka) {

    @JavascriptInterface
    fun getMyGifts():String{
        var toReturn = ""

        for (newString in myKartka.listOfPresents){
            toReturn += newString
            toReturn += "<br>"
        }
        return toReturn
    }

    @JavascriptInterface
    fun getMyPhoto():String{
        return myKartka.photoPath.toString()
    }

    @JavascriptInterface
    fun getMyLocation():String{
        return "Długość geograficzna: " + myKartka.longitude + "<br>" + "Szerokość geograficzna: " + myKartka.latitude + "<br>"
    }
}