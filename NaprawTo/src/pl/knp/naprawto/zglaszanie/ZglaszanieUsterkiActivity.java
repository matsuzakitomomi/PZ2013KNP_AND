package pl.knp.naprawto.zglaszanie;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONObject;

import pl.knp.naprawto.R;
import pl.knp.naprawto.user.UserDane;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
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
	Button zglosusterke;
	
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
	int tpy_int;
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
		zglosusterke = (Button)findViewById(R.id.zglaszanie_wyslij);
		
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
		zglosusterke.setOnClickListener(this);
		
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
		            		
		            			geoPoint = mapView.getProjection().fromPixels(
		                        (int) event.getX(),
		                        (int) event.getY());
		                        //Toast.makeText(getBaseContext(),p.getLatitudeE6() / 1E6 + "," + p.getLongitudeE6() /1E6, Toast.LENGTH_SHORT).show();
		                        punkt = new OverlayItem(geoPoint, "Twoja usterka", "");
		                        	    
		                        
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
			case R.id.zglaszanie_wyslij:
				wyslijZgloszenie();
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
				tpy_int=which;
				zglaszanie_typ.setText(typ);
			}
		});
		
		builder.create();
		builder.show();
	}

	public void dodawanieZdjecia()
	{
//		AlertDialog.Builder builder = new AlertDialog.Builder(this);    
//		builder.setItems(opcje, new DialogInterface.OnClickListener() {
//			
//			@Override
//			public void onClick(DialogInterface dialog, int which) {
//				
//				if(opcje[which].equalsIgnoreCase("galeria"))
//				{
					dodawanieZdjeciaGaleria();
//				}
//				else
//				{
//					dodawanieZdjeciaAparat();
//				}
//			}
//		});
//		
//		builder.create();
//		builder.show();
		
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

	public void wyslijZgloszenie()
	{
		String wiadomosc="";
		if(tytul==null || opis==null || typ==null || geoPoint==null)
		{
			Toast.makeText(this, "Nie wszytkie infromacje zosta³y podane...", Toast.LENGTH_SHORT).show();
		}
		else
		{
			if(tytul.length()<10) wiadomosc+="Tytu³ musi posiadaæ conajmniej 10 znaków\n";
			if(opis.length()<30) wiadomosc+="Opis musi posiadaæ conajmniej 30 znaków\n";
			
			if(wiadomosc.length()>0)
			{
				Toast.makeText(this, wiadomosc, Toast.LENGTH_SHORT).show();
			}
			else
			{
				new WysylanieZgloszenia().execute(tytul,opis,typ,Integer.toString(geoPoint.getLatitudeE6()),Integer.toString(geoPoint.getLongitudeE6()),UserDane.email);
			}
		}
	}
	
	private class WysylanieZgloszenia extends AsyncTask<String, Void, Void> {

		boolean blad=false;
		boolean wynik=false;
		
		private final ProgressDialog dialog = new ProgressDialog(ZglaszanieUsterkiActivity.this);	
		
		protected void onPreExecute() {
	         this.dialog.setMessage("Wysy³adnie zg³oszenia,\n proszê czekaæ...");
	         this.dialog.show();
	      }	

		@Override
		protected Void doInBackground(String... params) {
			this.dialog.setCancelable(false);
		    InputStream is = null;
		    String result = "";

		    try{
   	            HttpClient httpclient = new DefaultHttpClient();
   	            HttpPost httppost = new HttpPost("http://darmowephp.cba.pl/naprawto/json/wysylaniezgloszenia/zglos.php?email="+UserDane.email+"&title="
   	            +convertURL(tytul)+"&dir=inne&description="+convertURL(opis)+"&longitude="+Integer.toString(geoPoint.getLongitudeE6())+"&latitude="+Integer.toString(geoPoint.getLatitudeE6())+"&typ="+Integer.toString(tpy_int));
   	            HttpResponse response = httpclient.execute(httppost);
   	            HttpEntity entity = response.getEntity();
   	            is = entity.getContent();
		    }catch(Exception e){
   	            Log.e("log_tag", "Error in http connection "+e.toString());
   	            blad=true;
		    }
		    
		    try{
	            BufferedReader reader = new BufferedReader(new InputStreamReader(is,"ISO-8859-2"),8);
	            StringBuilder sb = new StringBuilder();
	            String line = null;
	            
	            while ((line = reader.readLine()) != null) {
	                    sb.append(line + "\n");
	            }
	            
	            is.close();
	            result=sb.toString();
	            
	            JSONObject jsonObiekt = new JSONObject(result);        
	            wynik=jsonObiekt.getBoolean("wynik");
	            
		    }catch(Exception e){
	            Log.e("log_tag", "Error converting result "+e.toString());
	            blad=true;
		    }
		    
			return null;
		}
		protected void onPostExecute(final Void unused) 
	      {
	         if (this.dialog.isShowing()) 
	         {
	            this.dialog.dismiss();
	            if(blad)
	            {
	            	Toast.makeText(context, "Coœ posz³o nie tak, spróbuj ponownie póŸniej", Toast.LENGTH_SHORT).show();
	            }
	            else
	            {
	            	if(wynik)
	            	{
	            		Toast.makeText(context, "Zg³oszenie zosta³o przyjête...", Toast.LENGTH_SHORT).show();
	            		finish();
	            	}
	            	else
	            	{
	            		Toast.makeText(context, "Niestety nie mogliœmy przetworzyæ Twojego zg³oszenia...", Toast.LENGTH_SHORT).show();
	            	}
	            }
	         }
	      }
	}
	
	public static String convertURL(String str) {

	    String url = null;
	    try{
	    url = new String(str.trim().replace(" ", "%20").replace("&", "%26")
	            .replace(",", "%2c").replace("(", "%28").replace(")", "%29")
	            .replace("!", "%21").replace("=", "%3D").replace("<", "%3C")
	            .replace(">", "%3E").replace("#", "%23").replace("$", "%24")
	            .replace("'", "%27").replace("*", "%2A").replace("-", "%2D")
	            .replace(".", "%2E").replace("/", "%2F").replace(":", "%3A")
	            .replace(";", "%3B").replace("?", "%3F").replace("@", "%40")
	            .replace("[", "%5B").replace("\\", "%5C").replace("]", "%5D")
	            .replace("_", "%5F").replace("`", "%60").replace("{", "%7B")
	            .replace("|", "%7C").replace("}", "%7D"));
	    }catch(Exception e){
	        e.printStackTrace();
	    }
	    return url;
	}
}
