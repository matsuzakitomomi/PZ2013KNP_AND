package pl.knp.informacje;

import pl.knp.naprawto.R;
import android.app.Activity;
import android.os.Bundle;
import android.webkit.WebView;

public class OnasActivity extends Activity {
	
	WebView webView;
	String text;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.onas);
		
		webView = (WebView)findViewById(R.id.webView);
		
		webView.setBackgroundColor(0);

		
		
		text =  "<?xml version=\"1.0\" encoding=\"UTF-8\" ?>" +
				"<html><style>" + 
				"html, body { font-family:Arial; font-size:27;background-color:transparent);>}" +
				"td.naglowek {font-size:27;color:white;}"+
				"td.tekst {font-size:17;color:white}"+
				"</style>" +
				"<body>" +
				"<center><table style=\"background-color:black;\">" +	 
				"<tr><td class=\"naglowek\">Aplikacjê stworzyli:</td></tr>"+
				"<tr><td class=\"tekst\"><a href=mailto:\"zxcv1991_39@o2.pl\" style=\"color:white\">Norbert Szyd³owski (implementacja)</a></td></tr>"+
				"<tr><td class=\"tekst\"><a href=mailto:\"pyto091@wp.pl\" style=\"color:white\">Bartosz Pytka (grafika)</td></tr>"+

				"<tr><td class=\"naglowek\"><br></td></tr>"+
				"<tr><td class=\"naglowek\">Inne osoby wspó³pracuj¹ce:</td></tr>"+
				"<tr><td class=\"tekst\"><a href=mailto:\"krypru91@gmial.coml\" style=\"color:white\">Krzysztof pruszyñski</td></tr>"+
				"<tr><td class=\"tekst\"><a href=mailto:\"dunajkarol@gmail.com\" style=\"color:white\">Karold Dunajski</td></tr>"+
				"</table></center></body>"+
				"</html>";
		
		webView.loadData(text, "text/html; charset=UTF-8" ,null);
		webView.setScrollBarStyle(WebView.SCROLLBARS_OUTSIDE_OVERLAY);	
	}

}
