package pl.polsl.piotrgazda

import android.content.ActivityNotFoundException
import android.content.Intent
import android.os.Environment
import android.provider.MediaStore
import android.speech.RecognizerIntent
import android.webkit.JavascriptInterface
import android.widget.Toast
import androidx.core.app.ActivityCompat.startActivityForResult
import androidx.core.content.FileProvider
import java.io.File

class PrezentyJSInterface(val myPrezenty: Prezenty) {

    @JavascriptInterface
    fun addPresent(){
        val wywolanie = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH)
        wywolanie.putExtra(
                RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM
        )
        wywolanie.putExtra(RecognizerIntent.EXTRA_PROMPT, "Powiedz cos do mnie...")


        try {
            startActivityForResult(myPrezenty, wywolanie, 0,null)
        } catch (wyjatek: ActivityNotFoundException) {
            Toast.makeText(
                    myPrezenty, "Nie slucham Ciebie!",
                    Toast.LENGTH_SHORT
            ).show()
        }
    }

    @JavascriptInterface
    fun makePhoto(){
        val plik = File((myPrezenty
                .getExternalFilesDir(
                        Environment.DIRECTORY_PICTURES)
                ?.path ?: "") + "/zdjecie.jpg")
        myPrezenty.pathToPhoto = FileProvider.getUriForFile(myPrezenty, myPrezenty.packageName.toString() + ".provider", plik)
        val przejscie = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        przejscie.putExtra(MediaStore.EXTRA_OUTPUT, myPrezenty.pathToPhoto)
        przejscie.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        try {
            startActivityForResult(myPrezenty, przejscie,1, null)
        }catch (e: ActivityNotFoundException) {
            // display error state to the user
        }
    }
}