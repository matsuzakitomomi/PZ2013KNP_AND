package pl.knp.naprawto.zgloszeniauzytkownika;

import java.util.ArrayList;
import java.util.List;

import pl.knp.naprawto.R;
import pl.knp.naprawto.zglaszanie.ObjectItemizedOverlay;
import pl.knp.zgloszenia.SpisPunktowWszystkich;
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
	
	LiniaOverlay linia;
	List<GeoPoint> points;
	
	Button typ;
	Button ja;
	Button centrum;
	
	Drawable drawable;
	int ile = 0;
	
	Bundle extras;
	
	String olsztyn="53.8289849927508 20.4848504807726 53.8285127511943 20.4810017862769 53.8253082029491 20.4800740188962 53.8248886969936 20.4760977795749 53.8209620812445 20.477330839793 53.8204078814541 20.4716323685726 53.8130873824012 20.4581839171068 53.8212309880838 20.4473737619172 53.8243053947415 20.4470263511761 53.8281751877625 20.4358467220912 53.8274846873593 20.4310724820843 53.8290161970336 20.4273159864459 53.8193883240853 20.4226470180213 53.8194698550259 20.4169287600833 53.8216813236548 20.4173478818427 53.8210145529841 20.4132207643741 53.818695121595 20.4107718481205 53.8176969069466 20.4061961212064 53.8187801769703 20.4052414331879 53.8157597910023 20.4022446137667 53.8123141427299 20.4006340447907 53.8090948287434 20.4027575152737 53.8091046066272 20.3873926826777 53.8017794362162 20.3834247461242 53.7997702399248 20.3902339517531 53.7981901901018 20.3887846804995 53.795955456729 20.3945594655535 53.7890939075671 20.3829439402994 53.7809654694574 20.3664895383372 53.7795646108329 20.3673351871672 53.7799249565957 20.3739419147754 53.7780999189458 20.3790381431812 53.781344386333 20.3778893894559 53.778196940609 20.3979056697301 53.7685155139484 20.4009634261082 53.7606170931675 20.4079487216937 53.7571186802106 20.4140280383518 53.75214424907 20.4261688897273 53.7545372961138 20.4281689560557 53.7552908062614 20.4349470708344 53.744490996854 20.4433162833986 53.7413221387959 20.450567167244 53.7432650203344 20.4519698888407 53.7426772601437 20.4599135394471 53.7390182497172 20.46208720516 53.7389491513038 20.4690009260734 53.7355531453915 20.4691360255816 53.7357299058921 20.4717523068005 53.7338835092411 20.4718425630284 53.7338187784565 20.4800525872369 53.7241252248749 20.4751863437099 53.7312773913417 20.4964505396254 53.7329229720993 20.5101765623196 53.7343980231098 20.5260845197976 53.7420620405994 20.5235929125834 53.747505360233 20.5472813301383 53.7507424262609 20.5396007236623 53.7584973152305 20.5364706645204 53.762216615285 20.5291308960789 53.7638965546885 20.5288480382847 53.7691071393095 20.5332981461671 53.7699562090345 20.5382832336659 53.7657379682098 20.5503787083628 53.7671956906804 20.5529782125306 53.7708880481995 20.5536608070234 53.7734614605013 20.5481906526832 53.7738540830596 20.5512454414177 53.7749586236529 20.5510450412236 53.7744982367374 20.5559174331813 53.7820134136484 20.5591006283905 53.7856723587343 20.5567154323296 53.7852977774319 20.5607236621722 53.7853596714766 20.5666449689087 53.7965197154047 20.5596676387517 53.7983346577335 20.5619508796236 53.8024733489208 20.558670503494 53.8033732102666 20.5614096454617 53.8042011101837 20.5550897395608 53.806538963797 20.5529563279632 53.8102230329001 20.5392682248713 53.8096126204403 20.5321934275053 53.8132099481952 20.5350425580123 53.813588755552 20.5395094292028 53.8160775412938 20.5376031722892 53.8145777792689 20.5271756691239 53.8165068467208 20.5236476296411 53.8155621763799 20.5186034947095 53.8178186389913 20.5153976970453 53.8167887738224 20.5115526465874 53.8199917784397 20.5089103931216 53.8176149918153 20.5043535213903 53.8217498799524 20.5024668297859 53.8224210076254 20.4920022338399 53.8243723283904 20.49207420608 53.8266789607079 20.4884535296459 53.8264136720984 20.4860603987772 53.8289849927508 20.4848504807726";
	
	List<Drawable> lista_markerow;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.mapa);
		
		mapView = (MapView)findViewById(R.id.map);
		typ = (Button)findViewById(R.id.typ);
		ja = (Button)findViewById(R.id.ja);
		centrum = (Button)findViewById(R.id.centrum);
		
		lista_markerow=getMarker();
		
		typ.setOnClickListener(this);
		ja.setOnClickListener(this);
		centrum.setOnClickListener(this);
		
		drawable = this.getResources().getDrawable(R.drawable.androidmarker);
		mapOverlays = mapView.getOverlays();
		mapController = mapView.getController();
			
		me=new MyLocationOverlay(this, mapView);
		
		points = getLine(olsztyn);
		linia = new LiniaOverlay(points,false);
		
		mapOverlays.add(me);
		mapView.postInvalidate();
		
		zmienNapisTypMapy(mapView.isSatellite());
		
		extras = getIntent().getExtras();
	}
	
	public void wycentruj()
	{
		if(ile!=0)
		{
			int minLat = Integer.MAX_VALUE;
			int maxLat = Integer.MIN_VALUE;
			int minLon = Integer.MAX_VALUE;
			int maxLon = Integer.MIN_VALUE;
			
			for (UsterkaListaMapa item : extras.getBoolean("uzytkownik")?SpisPunktow.areas:SpisPunktowWszystkich.areas)
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
		else
		{
			mapOverlays.clear();
			mapOverlays.add(linia);
			mapOverlays.add(me);
			
			mapView.postInvalidate();
			
			mapController.setZoom(12);
			mapController.animateTo(new GeoPoint(53743070,20457079)); 
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
	
	@Override
	protected boolean isRouteDisplayed() {
		// TODO Auto-generated method stub
		return false;
	}
	
	@Override
	protected void onResume() {
		me.enableMyLocation();
		if(extras.getBoolean("uzytkownik"))
		{
			if(SpisPunktow.areas.size()!=ile)
			{
				dodajNoweMarkery();
			}
		}
		else
		{
			if(SpisPunktowWszystkich.areas.size()!=ile)
			{
				dodajNoweMarkery();
				wycentruj();
			}
		}
				
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
			ile=extras.getBoolean("uzytkownik")?SpisPunktow.areas.size():SpisPunktowWszystkich.areas.size();
			Log.i("tu", Integer.toString(ile));
			itemizedOverlay = new ObjectItemizedOverlay(drawable, this,true,extras.getBoolean("uzytkownik"));
			OverlayItem item=null;
			for (UsterkaListaMapa usterka : extras.getBoolean("uzytkownik")?SpisPunktow.areas:SpisPunktowWszystkich.areas) {	
				item = new OverlayItem(new GeoPoint(usterka.latitude, usterka.longitude), usterka.title, usterka.description);
				item.setMarker(lista_markerow.get(usterka.typ));
				itemizedOverlay.addOverlay(item);
			}
			
			itemizedOverlay.populateNow();	
			mapOverlays.clear();
			mapOverlays.add(linia);
			mapOverlays.add(me);
			
			mapView.postInvalidate();
			mapOverlays.add(itemizedOverlay);
	}
	
	public List<Drawable> getMarker()
	{
		List<Drawable> lista = new ArrayList<Drawable>();
		
		Drawable d;
		
		d = getResources().getDrawable(R.drawable.pogotowie);
		d.setBounds(0, -d.getIntrinsicHeight(), d.getIntrinsicWidth(), 0);
		
		lista.add(d);
		
		d = getResources().getDrawable(R.drawable.drogowcy);
		d.setBounds(0, -d.getIntrinsicHeight(), d.getIntrinsicWidth(), 0);

		lista.add(d);
		
		d = getResources().getDrawable(R.drawable.policja);
		d.setBounds(0, -d.getIntrinsicHeight(), d.getIntrinsicWidth(), 0);
		
		lista.add(d);
		
		d = getResources().getDrawable(R.drawable.inne);
		d.setBounds(0, -d.getIntrinsicHeight(), d.getIntrinsicWidth(), 0);
		
		lista.add(d);
		
		

		return lista;
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
	
	public ArrayList<GeoPoint> getLine(String line)
	{
		ArrayList<GeoPoint> punkty = new ArrayList<GeoPoint>();
		String seperator = " ";  
		String [] tempText = line.split(seperator);
		
		for (int i=0;i<tempText.length;i=i+2)
		{
			punkty.add(new GeoPoint(getIntFromString(tempText[i]),getIntFromString(tempText[i+1])));
		}
		return punkty;
	}
	
	private int getIntFromString(String var)
	{
		Double d=Double.valueOf(var);
		d*=1E6;
		return d.intValue();
	}
	
}
