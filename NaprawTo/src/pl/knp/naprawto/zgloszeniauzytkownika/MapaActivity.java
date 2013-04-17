package pl.knp.naprawto.zgloszeniauzytkownika;

import java.util.List;

import pl.knp.naprawto.R;
import pl.knp.naprawto.zglaszanie.ObjectItemizedOverlay;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;
import com.google.android.maps.MyLocationOverlay;
import com.google.android.maps.Overlay;
import com.google.android.maps.OverlayItem;

public class MapaActivity extends MapActivity implements View.OnClickListener{

	MapView mapView;
	List<Overlay> mapOverlays;
	ObjectItemizedOverlay itemizedOverlay;
	MapController mapController;
	MyLocationOverlay me;
	
	Button typ;
	Button ja;
	Button centrum;
	
	Drawable drawable;
	int ile = 0;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.mapa);
		
		mapView = (MapView)findViewById(R.id.map);
		typ = (Button)findViewById(R.id.typ);
		ja = (Button)findViewById(R.id.ja);
		centrum = (Button)findViewById(R.id.centrum);
		
		typ.setOnClickListener(this);
		ja.setOnClickListener(this);
		centrum.setOnClickListener(this);
		
		drawable = this.getResources().getDrawable(R.drawable.androidmarker);
		mapOverlays = mapView.getOverlays();
		mapController = mapView.getController();
			
		me=new MyLocationOverlay(this, mapView);
		mapOverlays.add(me);
		mapView.postInvalidate();
		
		zmienNapisTypMapy(mapView.isSatellite());
	}
	
	public void wycentruj()
	{
		int minLat = Integer.MAX_VALUE;
		int maxLat = Integer.MIN_VALUE;
		int minLon = Integer.MAX_VALUE;
		int maxLon = Integer.MIN_VALUE;
		
		for (UsterkaListaMapa item : SpisPunktow.areas) 
		{ 

		      int lat = item.latitude;
		      int lon = item.longitude;

		      maxLat = Math.max(lat, maxLat);
		      minLat = Math.min(lat, minLat);
		      maxLon = Math.max(lon, maxLon);
		      minLon = Math.min(lon, minLon);
		      
		     
		 }
		
		double fitFactor = 1.5;
		int latSpanE6 = (int)(Math.abs(maxLat - minLat) * fitFactor);
		int lonSpanE6 = (int)(Math.abs(maxLon - minLon) * fitFactor);
		mapController.zoomToSpan(latSpanE6, lonSpanE6);

		mapController.animateTo(new GeoPoint( (maxLat + minLat)/2, 
				(maxLon + minLon)/2 )); 
	}
	
	@Override
	protected boolean isRouteDisplayed() {
		// TODO Auto-generated method stub
		return false;
	}
	
	@Override
	protected void onResume() {
		me.enableMyLocation();
		if(SpisPunktow.areas.size()!=ile)
		{
			dodajNoweMarkery();
		}
		wycentruj();		
		super.onResume();
	}

	@Override
	protected void onPause() {
		me.disableMyLocation();
		super.onPause();
	}
	
	public void zmienNapisTypMapy(boolean value)
	{
		if(value)
		{
			typ.setText("Podstawowa");
		}
		else
		{
			typ.setText("Satelita");
		}
	}
	
	public void dodajNoweMarkery()
	{
			ile=SpisPunktow.areas.size();
			Log.i("tu", Integer.toString(ile));
			itemizedOverlay = new ObjectItemizedOverlay(drawable, this,true);
			for (UsterkaListaMapa usterka : SpisPunktow.areas) {	
				itemizedOverlay.addOverlay(new OverlayItem(new GeoPoint(usterka.latitude, usterka.longitude), usterka.title, usterka.description));
			}
			itemizedOverlay.populateNow();	
			mapOverlays.clear();
			mapOverlays.add(me);
			mapView.postInvalidate();
			mapOverlays.add(itemizedOverlay);
	}

	
	public void idzDoMojejLokalizacji()
	{
		if(me.getMyLocation()!=null)
		{
			mapController.animateTo(me.getMyLocation());
		}
		else
		{
			Toast.makeText(getApplicationContext(), "Nie mo¿emy ustaliæ twojej lokalizacji", Toast.LENGTH_SHORT).show();
		}
	}
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.typ:
			mapView.setSatellite(!mapView.isSatellite());
			zmienNapisTypMapy(mapView.isSatellite());
			break;		
		case R.id.ja:
			idzDoMojejLokalizacji();
			break;
		case R.id.centrum:
			wycentruj();
			break;
		default:
			break;
		}
		
	}
	
}
