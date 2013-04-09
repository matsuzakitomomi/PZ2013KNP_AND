package pl.knp.naprawto.zgloszeniauzytkownika;

import java.util.ArrayList;
import java.util.List;

import pl.knp.naprawto.R;
import pl.knp.naprawto.zglaszanie.ObjectItemizedOverlay;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;
import com.google.android.maps.Overlay;
import com.google.android.maps.OverlayItem;

public class MapaActivity extends MapActivity{

	MapView mapView;
	List<Overlay> mapOverlays;
	ObjectItemizedOverlay itemizedOverlay;
	MapController mapController;
	
	Drawable drawable;
	ArrayList<UsterkaListaMapa> areas = SpisPunktow.areas;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.mapa);
		
		mapView = (MapView)findViewById(R.id.map);
		
		drawable = this.getResources().getDrawable(R.drawable.androidmarker);
		itemizedOverlay = new ObjectItemizedOverlay(drawable, this);
		mapOverlays = mapView.getOverlays();
		mapController = mapView.getController();
		
		for (UsterkaListaMapa usterka : areas) {
			
			itemizedOverlay.addOverlay(new OverlayItem(new GeoPoint(usterka.latitude, usterka.longitude), usterka.title, usterka.description));
			
		}
		
		itemizedOverlay.populateNow();
		mapOverlays.add(itemizedOverlay);
		
		wycentruj();
		
	}
	
	public void wycentruj()
	{
		int minLat = Integer.MAX_VALUE;
		int maxLat = Integer.MIN_VALUE;
		int minLon = Integer.MAX_VALUE;
		int maxLon = Integer.MIN_VALUE;
		
		for (UsterkaListaMapa item : areas) 
		{ 

		      int lat = item.latitude;
		      int lon = item.longitude;

		      maxLat = Math.max(lat, maxLat);
		      minLat = Math.min(lat, minLat);
		      maxLon = Math.max(lon, maxLon);
		      minLon = Math.min(lon, minLon);
		      
		     
		 }
		
		double fitFactor = 1.5;
		mapController.zoomToSpan((int) (Math.abs(maxLat - minLat) * fitFactor), (int)(Math.abs(maxLon - minLon) * fitFactor));

		mapController.animateTo(new GeoPoint( (maxLat + minLat)/2, 
				(maxLon + minLon)/2 )); 
	}
	
	@Override
	protected boolean isRouteDisplayed() {
		// TODO Auto-generated method stub
		return false;
	}

}
