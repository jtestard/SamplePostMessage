package com.example.samplepostmessage

import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.webkit.JavascriptInterface
import android.webkit.WebMessage
import android.webkit.WebView
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val myButton: Button = findViewById(R.id.button)

        val myWebView: WebView = findViewById(R.id.webview)

        myWebView.settings.javaScriptEnabled = true

        myWebView.addJavascriptInterface(WebAppInterface(this), "Android")
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
                    "                Android.showToast(event.data)\n" +
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
            myWebView.postWebMessage(WebMessage("Coucou !"), Uri.EMPTY)
        }
    }

}

class WebAppInterface(
    private val mContext: Context
) {

    /** Show a toast from the web page  */
    @JavascriptInterface
    fun showToast(toast: String) {
        Toast.makeText(mContext, toast, Toast.LENGTH_SHORT).show()
    }
}

