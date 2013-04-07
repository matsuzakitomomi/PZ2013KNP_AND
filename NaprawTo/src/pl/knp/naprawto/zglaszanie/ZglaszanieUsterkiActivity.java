package pl.knp.naprawto.zglaszanie;

import java.util.List;

import pl.knp.naprawto.R;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;
import com.google.android.maps.MyLocationOverlay;
import com.google.android.maps.Overlay;
import com.google.android.maps.OverlayItem;

public class ZglaszanieUsterkiActivity extends MapActivity implements View.OnClickListener{
	
	Context context;
	
	MapView mapView;
	ScrollView hsv;
	Dialog dialog;
	
	Button typmapy;
	Button mojalokalizacja;
	
	ImageButton zglaszanieusterkitytulplus;
	ImageButton zglaszanieusterkiopisplus;
	ImageButton zglaszanieusterkitypplus;
	ImageButton zglaszanieusterkizdjecieplus;
	
	TextView zglaszanie_tytul;
	TextView zglaszanie_opis;
	TextView zglaszanie_typ;
	TextView zglaszanie_zdjecie;
	
	String tytul;
	String opis;
	String typ;
	String selectedImagePath;
	
	MyLocationOverlay me;
	MapController mapController;
	GeoPoint geoPoint; 
	List<Overlay> mapOverlays;
	Drawable drawable;
	ObjectItemizedOverlay itemizedoverlay;
	OverlayItem punkt;
	
	CheckBox wyborreczny;
	
	
	
	static String [] items = new String[]{"wypadek","kradzie¿","infrastruktura","inne"};
	static String [] opcje = new String[]{"aparat","galeria"};
	
	private static final int SELECT_PICTURE = 1;
	private static final int SELECT_APARAT = 2;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);	
		setContentView(R.layout.zglaszanie_usterki);
		
		mapView = (MapView)findViewById(R.id.mapview);
		hsv = (ScrollView)findViewById(R.id.hsv);
		context=this;
		
		me = new MyLocationOverlay(this, mapView);
		
		mapView.getOverlays().add(me);
		mapView.postInvalidate();
		
		mapController = mapView.getController();
		mapView.getController().setZoom(12); 
		
		mapOverlays = mapView.getOverlays(); 
		drawable = this.getResources().getDrawable(R.drawable.marker); 		
		
		typmapy = (Button)findViewById(R.id.zglaszanie_typ_mapy);
		mojalokalizacja = (Button)findViewById(R.id.zglaszanie_moja_lokalizacja);
		
		zglaszanieusterkitytulplus = (ImageButton)findViewById(R.id.zglaszanie_dodaj_tytul);
		zglaszanieusterkiopisplus = (ImageButton)findViewById(R.id.zglaszanie_dodaj_opis);
		zglaszanieusterkitypplus = (ImageButton)findViewById(R.id.zglaszanie_dodaj_typ);
		zglaszanieusterkizdjecieplus = (ImageButton)findViewById(R.id.zglaszanie_dodaj_zdjecie);
		
		zglaszanie_tytul = (TextView)findViewById(R.id.zglaszanie_tytul);
		zglaszanie_opis = (TextView)findViewById(R.id.zglaszanie_opis);
		zglaszanie_typ = (TextView)findViewById(R.id.zglaszanie_typ);
		zglaszanie_zdjecie = (TextView)findViewById(R.id.zglaszanie_zdjecie);
		
		wyborreczny = (CheckBox)findViewById(R.id.zglaszanie_usterki_recznie);
		
		zglaszanieusterkiopisplus.setOnClickListener(this);
		zglaszanieusterkitytulplus.setOnClickListener(this);
		zglaszanieusterkitypplus.setOnClickListener(this);
		zglaszanieusterkizdjecieplus.setOnClickListener(this);
		
		typmapy.setOnClickListener(this);
		mojalokalizacja.setOnClickListener(this);
		
		mapView.setOnTouchListener(new OnTouchListener() {
		    @Override
		    public boolean onTouch(View v, MotionEvent event) {
		        switch (event.getAction()) {
		            case MotionEvent.ACTION_CANCEL:
		                hsv.requestDisallowInterceptTouchEvent(false);
		                break;
		            case MotionEvent.ACTION_UP:
		                hsv.requestDisallowInterceptTouchEvent(true);
		                break;		                
		            case MotionEvent.ACTION_DOWN:		
		            	if(wyborreczny.isChecked())
		            	{
			            		mapView.getOverlays().clear();
			    				mapView.getOverlays().add(me);
		            		
		            			GeoPoint p = mapView.getProjection().fromPixels(
		                        (int) event.getX(),
		                        (int) event.getY());
		                        //Toast.makeText(getBaseContext(),p.getLatitudeE6() / 1E6 + "," + p.getLongitudeE6() /1E6, Toast.LENGTH_SHORT).show();
		                        punkt = new OverlayItem(p, "Twoja usterka", "");
		                        	    
		                        
		                        itemizedoverlay = new ObjectItemizedOverlay(drawable, context);
		                        itemizedoverlay.addOverlay(punkt);
		            			itemizedoverlay.populateNow();
		            			mapOverlays.add(itemizedoverlay);
		            			
		            			wyborreczny.setChecked(false);
		            	}
		            	hsv.requestDisallowInterceptTouchEvent(true);
		            	break;
		                
		            case MotionEvent.ACTION_MOVE:
		                hsv.requestDisallowInterceptTouchEvent(true);
		                break;           
		        }
		        return mapView.onTouchEvent(event);
		    }
		});	
	}

	
	@Override
	protected boolean isRouteDisplayed() {
		// TODO Auto-generated method stub
		return false;
	}


	@Override
	public void onClick(View v) {
		switch(v.getId())
		{
			case R.id.zglaszanie_dodaj_tytul:
				dodawanieTytuluOpisu(1); //1 - dla tytulu
				break;
			case R.id.zglaszanie_dodaj_opis:
				dodawanieTytuluOpisu(2); //2 - dla opisu
				break;
			case R.id.dialog_anuluj:
				dialog.dismiss();
				break;
			case R.id.zglaszanie_dodaj_typ:
				dodawanieTypu();
				break;
			case R.id.zglaszanie_dodaj_zdjecie:
				dodawanieZdjecia();
				break;
			case R.id.zglaszanie_typ_mapy:
				mapView.setSatellite(!mapView.isSatellite());
				break;
			case R.id.zglaszanie_moja_lokalizacja:
				szukanieMojejLokalizacji();
				break;
		}
		
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) 
	{     
		if (resultCode == RESULT_OK) 
		{         
			if (requestCode == SELECT_PICTURE) 
			{              
				Uri selectedImageUri = data.getData();      
				selectedImagePath = getPath(selectedImageUri);      
				zglaszanie_zdjecie.setText(getFileName(selectedImagePath));  			
			}    
			else if(requestCode == SELECT_APARAT)
			{
				//kiedys dokonczyc
			}
			
		}   	
	}
	
	@Override
	protected void onResume() {
		me.enableMyLocation();
		super.onResume();
	}
	
	@Override
	protected void onPause() {
		me.disableMyLocation();
		super.onPause();
	}
	
	public void szukanieMojejLokalizacji()
	{		
		if(me.getMyLocation()!=null)
		{
			mapController.animateTo(me.getMyLocation());
			mapController.setZoom(14);
		}
		else
		{
			Toast.makeText(getApplicationContext(), "Nie mo¿emy ustaliæ twojej lokalizacji", Toast.LENGTH_SHORT).show();
		}
	}
	
	public void dodawanieTytuluOpisu(final int co)
	{
		dialog = new Dialog(this);
		dialog.setContentView(R.layout.dialog_tytul_zgloszenia);
		
		final EditText wprowadzanie = (EditText) dialog.findViewById(R.id.edit_text);
		Button zatwierdz = (Button) dialog.findViewById(R.id.dialog_zatwierdz);
		Button anuluj = (Button) dialog.findViewById(R.id.dialog_anuluj);	
		
		if(co==1)
		{
			dialog.setTitle("WprowadŸ tytu³: ");
			wprowadzanie.setText(tytul);
		}
		else
		{
			dialog.setTitle("WprowadŸ opis: ");
			wprowadzanie.setText(opis);
		}
		
		wprowadzanie.setSelection(wprowadzanie.getText().length());
		
		zatwierdz.setOnClickListener(new OnClickListener() {		
			@Override
			public void onClick(View v) {
				if(co==1)
				{
					tytul=wprowadzanie.getText().toString();	
					zglaszanie_tytul.setText(tytul);
				}
				else
				{
					opis=wprowadzanie.getText().toString();
					zglaszanie_opis.setText(opis);
				}
				dialog.dismiss();
			}
		});
		anuluj.setOnClickListener(this);
		
		dialog.show();
	  }
	
	public void dodawanieTypu()
	{
		AlertDialog.Builder builder = new AlertDialog.Builder(this);    
		builder.setTitle("Wybierz typ usterki: ");
		builder.setItems(items, new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				typ=items[which];
				zglaszanie_typ.setText(typ);
			}
		});
		
		builder.create();
		builder.show();
	}

	public void dodawanieZdjecia()
	{
		AlertDialog.Builder builder = new AlertDialog.Builder(this);    
		builder.setItems(opcje, new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				
				if(opcje[which].equalsIgnoreCase("galeria"))
				{
					dodawanieZdjeciaGaleria();
				}
				else
				{
					dodawanieZdjeciaAparat();
				}
			}
		});
		
		builder.create();
		builder.show();
		
	}
	
	public void dodawanieZdjeciaGaleria()
	{
		Intent intent = new Intent(); 
		intent.setType("image/*");    
		intent.setAction(Intent.ACTION_GET_CONTENT);
		startActivityForResult(Intent.createChooser(intent,"Wybierz zdjêcie"), SELECT_PICTURE);
	}
	
	public void dodawanieZdjeciaAparat()
	{
		//dokonczyc
	}
	
	public String getPath(Uri uri) 
	{      
		try
		{
			Cursor cursor = getContentResolver()
		               .query(uri, null, null, null, null); 
		    cursor.moveToFirst(); 
		    int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA); 
		    return cursor.getString(idx); 
		}
		catch (Exception e)
        {
            return uri.getPath();
        }
	}

	public String getFileName(String file)
	{
		int lastslash = file.lastIndexOf('/');

		if( lastslash > 0 ) 
		{ 
		  return file.substring(lastslash + 1);
		}
		else
		{
			return file;
		}
	}

}
