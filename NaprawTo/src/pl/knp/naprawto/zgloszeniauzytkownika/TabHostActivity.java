package pl.knp.naprawto.zgloszeniauzytkownika;

import pl.knp.naprawto.R;
import android.app.TabActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TabHost;

public class TabHostActivity extends TabActivity {
	
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    setContentView(R.layout.tabhost);
	       	    
	    addPunkty("PUNKTY", SpisPunktow.class);
	    addMapa("MAPA", MapaActivity.class);
	}
	
	private void addPunkty(String labelId, Class<?> c) {
		TabHost tabHost = getTabHost();
		Intent intent = new Intent(this, c);
		TabHost.TabSpec spec = tabHost.newTabSpec("tab" + labelId);

		View tabIndicator = LayoutInflater.from(this).inflate(R.layout.tabs, getTabWidget(), false);
		ImageButton title = (ImageButton) tabIndicator.findViewById(R.id.title);
		title.setImageResource(R.drawable.p);

		spec.setIndicator("",getResources().getDrawable(R.drawable.p));
		spec.setContent(intent);
		tabHost.addTab(spec);
	}
	
	private void addMapa(String labelId, Class<?> c) {
		TabHost tabHost = getTabHost();
		Intent intent = new Intent(this, c);
		TabHost.TabSpec spec = tabHost.newTabSpec("tab" + labelId);

		View tabIndicator = LayoutInflater.from(this).inflate(R.layout.tabs, getTabWidget(), false);
		ImageButton title = (ImageButton) tabIndicator.findViewById(R.id.title);
		title.setImageResource(R.drawable.m);

		spec.setIndicator("",getResources().getDrawable(R.drawable.m));
		spec.setContent(intent);
		tabHost.addTab(spec);
	}

}
