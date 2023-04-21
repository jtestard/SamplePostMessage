package com.example.samplepostmessage

import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.webkit.JavascriptInterface
import android.webkit.WebMessage
import android.webkit.WebView
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import java.time.Instant


class MainActivity : AppCompatActivity() {

    private lateinit var myButton: Button


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        myButton = findViewById(R.id.button)

        val myWebView: WebView = findViewById(R.id.webview)

        myWebView.settings.javaScriptEnabled = true

        myWebView.addJavascriptInterface(this, "Android")
        myWebView.loadUrl("https://www.google.com")

        /*
        <input type="button" value="Say hello" onClick="showAndroidToast('Hello Android!')" />
        <script type="text/javascript">
            function showAndroidToast(toast) {
                Android.showToast(toast);
            }
            window.addEventListener(
              "message",
              (event) => {
                Android.showToast(event.data)
              },
              false
            );
        </script>
         */
        val html =
            "<input type=\"button\" value=\"Say hello\" onClick=\"showAndroidToast('Hello Android!')\" />\n" +
                    "        <script type=\"text/javascript\">\n" +
                    "            function showAndroidToast(toast) {\n" +
                    "                Android.showToast(toast);\n" +
                    "            }\n" +
                    "            window.addEventListener(\n" +
                    "              \"message\",\n" +
                    "              (event) => {\n" +
                    "                Android.updateButton(event.data)\n" +
                    "              },\n" +
                    "              false\n" +
                    "            );\n" +
                    "        </script>"

        myWebView.loadDataWithBaseURL(
            "file:///android_res/drawable/",
            html,
            "text/html",
            "UTF-8",
            null
        )


        myButton.setOnClickListener {
            myWebView.postWebMessage(WebMessage("${Instant.now().nano}"), Uri.EMPTY)
        }
    }

    /** Show a toast from the web page  */
    @JavascriptInterface
    fun showToast(toast: String) {
        Toast.makeText(this, toast, Toast.LENGTH_SHORT).show()
    }

    /** Show a toast from the web page  */
    @JavascriptInterface
    fun updateButton(message: String) {
        val diff = Instant.now().nano - Integer.parseInt(message)
        myButton.text = "${diff.div(1000000f)}"
        Log.w("MainActivity", "${myButton.text}")
    }
}


