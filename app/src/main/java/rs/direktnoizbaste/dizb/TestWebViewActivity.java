package rs.direktnoizbaste.dizb;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.webkit.WebView;
import rs.direktnoizbaste.dizb.app.WebAppInterface;

public class TestWebViewActivity extends AppCompatActivity {
    WebView webView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_web_view);
        webView = (WebView)findViewById(R.id.webView);
        webView.getSettings().setJavaScriptEnabled(true);

        // Enable java script to access android app class
        webView.addJavascriptInterface(new WebAppInterface(this), "Android");

        // load html5 test to demonstrate browser working
        webView.loadUrl("file:///android_asset/www/test.htm");
        //
    }
}
