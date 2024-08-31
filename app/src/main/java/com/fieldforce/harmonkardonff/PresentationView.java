package com.fieldforce.harmonkardonff;

import android.content.Intent;
import android.os.Bundle;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import app.core.base.InnosolsActivity;
import es.voghdev.pdfviewpager.library.PDFViewPager;
import es.voghdev.pdfviewpager.library.RemotePDFViewPager;
import es.voghdev.pdfviewpager.library.remote.DownloadFile;

public class PresentationView extends InnosolsActivity implements DownloadFile.Listener {
	private WebView webvw;
	String docurl = "";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_presenttnvw);
		initialise();
	}

	private void initialise() {
		webvw = (WebView) findViewById(R.id.web);
		Intent i = getIntent();
		if (i != null && i.getStringExtra("documenturl") != null) {
			docurl = i.getStringExtra("documenturl");
			setpropertyforwebvw();

		}

	}

	private void setpropertyforwebvw() {
		String url = "https://docs.google.com/gview?embedded=true&url=";
		String loadurl = url + docurl;
		webvw.getProgress();

		webvw.loadUrl(loadurl);

		WebSettings webSettings = webvw.getSettings();

		webSettings.setJavaScriptEnabled(true);
		webSettings.setCacheMode(WebSettings.LOAD_NO_CACHE);

		webvw.setWebViewClient(new MyBrowser());

	}

	@Override
	public void onSuccess(String url, String destinationPath) {

	}

	@Override
	public void onFailure(Exception e) {

	}

	@Override
	public void onProgressUpdate(int progress, int total) {

	}

	@Override
	protected void onDestroy() {
		super.onDestroy();

	}

	class MyBrowser extends WebViewClient {
		@Override
		public boolean shouldOverrideUrlLoading(WebView view, String url) {
			// TODO Auto-generated method stub
			view.loadUrl(url);
			return super.shouldOverrideUrlLoading(view, url);
		}

	}

	@Override
	public void RegisterTableInfoForLocalDB() {
		// TODO Auto-generated method stub

	}

}
