package rs.direktnoizbaste.dizb;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.webkit.WebView;

import rs.direktnoizbaste.dizb.app.WebAppInterface;

public class GraphActivity extends AppCompatActivity {
    WebView browser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_line_graph);
        browser = (WebView) findViewById(R.id.webView);
        browser.addJavascriptInterface(new WebAppInterface(this), "Android");
        browser.getSettings().setJavaScriptEnabled(true);
        browser.loadUrl("file:///android_asset/www/graphs.htm");
    }
}
