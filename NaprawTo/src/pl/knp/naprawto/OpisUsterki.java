package pl.knp.naprawto;

import pl.knp.naprawto.zglaszanie.ObjectItemizedOverlay;
import pl.knp.naprawto.zgloszeniauzytkownika.UsterkaListaMapa;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.widget.TextView;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapView;
import com.google.android.maps.OverlayItem;

public class OpisUsterki extends MapActivity {

	Bundle extras;
	UsterkaListaMapa usterka;
	TextView tytul;
	TextView opis;
	TextView data;
	TextView typ;
	
	MapView mapa;
	ObjectItemizedOverlay itemizedOverlay;
	Drawable defaultMarker;
	
	GeoPoint point;
	OverlayItem overlayItem;
	
	static String [] items = new String[]{"wypadek","kradzie¿","infrastruktura","inne"};
	
	@Override
	protected void onCreate(Bundle icicle) {
			super.onCreate(icicle);
			setContentView(R.layout.opis_usterki);
			
			extras = getIntent().getExtras();
			usterka = (UsterkaListaMapa)extras.get("item");
			
			tytul = (TextView)findViewById(R.id.tytul);
			opis = (TextView)findViewById(R.id.opis);
			data = (TextView)findViewById(R.id.data);
			mapa = (MapView)findViewById(R.id.map);
			typ = (TextView)findViewById(R.id.typ);
			
			tytul.setText(usterka.title);
			opis.setText(usterka.description);
			data.setText(usterka.data);
			
			defaultMarker = this.getResources().getDrawable(R.drawable.androidmarker);
			itemizedOverlay = new ObjectItemizedOverlay(defaultMarker, this);
			
			point = new GeoPoint(usterka.latitude, usterka.longitude);
			
			overlayItem = new OverlayItem(point, null, null);
			overlayItem.setMarker(getMarker(usterka.typ));
			
			itemizedOverlay.addOverlay(overlayItem);
			itemizedOverlay.populateNow();
			
			mapa.getOverlays().add(itemizedOverlay);
			mapa.postInvalidate();
			
			typ.setText(typ.getText().toString()+items[usterka.typ]);
			
			wycentruj();
			
	}
	
	@Override
	protected boolean isRouteDisplayed() {
		// TODO Auto-generated method stub
		return false;
	}
	
	public void wycentruj()
	{	
		int latspanE6 = 5000;
		int lonspanE6 = 5000;
		
		mapa.getController().zoomToSpan(latspanE6, lonspanE6);
		mapa.getController().animateTo(point); 
	}
	
	public Drawable getMarker(int typ)
	{
		Drawable d=null;
		
		switch (typ) {
		case 0:
			d = getResources().getDrawable(R.drawable.pogotowie);
			break;
		case 1:
			d = getResources().getDrawable(R.drawable.drogowcy);
			break;
		case 2:
			d = getResources().getDrawable(R.drawable.policja);
			break;
		case 3:
			d = getResources().getDrawable(R.drawable.inne);
			break;

		default:
			break;
		}
		
		d.setBounds(0, -d.getIntrinsicHeight(), d.getIntrinsicWidth(), 0);
		
		return d;
	}

}
