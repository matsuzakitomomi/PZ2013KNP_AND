package pl.knp.naprawto.zglaszanie;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONObject;

import pl.knp.naprawto.R;
import pl.knp.naprawto.user.UserDane;
import pl.knp.naprawto.zgloszeniauzytkownika.LiniaOverlay;
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
	
	String olsztyn="53.8289849927508 20.4848504807726 53.8285127511943 20.4810017862769 53.8253082029491 20.4800740188962 53.8248886969936 20.4760977795749 53.8209620812445 20.477330839793 53.8204078814541 20.4716323685726 53.8130873824012 20.4581839171068 53.8212309880838 20.4473737619172 53.8243053947415 20.4470263511761 53.8281751877625 20.4358467220912 53.8274846873593 20.4310724820843 53.8290161970336 20.4273159864459 53.8193883240853 20.4226470180213 53.8194698550259 20.4169287600833 53.8216813236548 20.4173478818427 53.8210145529841 20.4132207643741 53.818695121595 20.4107718481205 53.8176969069466 20.4061961212064 53.8187801769703 20.4052414331879 53.8157597910023 20.4022446137667 53.8123141427299 20.4006340447907 53.8090948287434 20.4027575152737 53.8091046066272 20.3873926826777 53.8017794362162 20.3834247461242 53.7997702399248 20.3902339517531 53.7981901901018 20.3887846804995 53.795955456729 20.3945594655535 53.7890939075671 20.3829439402994 53.7809654694574 20.3664895383372 53.7795646108329 20.3673351871672 53.7799249565957 20.3739419147754 53.7780999189458 20.3790381431812 53.781344386333 20.3778893894559 53.778196940609 20.3979056697301 53.7685155139484 20.4009634261082 53.7606170931675 20.4079487216937 53.7571186802106 20.4140280383518 53.75214424907 20.4261688897273 53.7545372961138 20.4281689560557 53.7552908062614 20.4349470708344 53.744490996854 20.4433162833986 53.7413221387959 20.450567167244 53.7432650203344 20.4519698888407 53.7426772601437 20.4599135394471 53.7390182497172 20.46208720516 53.7389491513038 20.4690009260734 53.7355531453915 20.4691360255816 53.7357299058921 20.4717523068005 53.7338835092411 20.4718425630284 53.7338187784565 20.4800525872369 53.7241252248749 20.4751863437099 53.7312773913417 20.4964505396254 53.7329229720993 20.5101765623196 53.7343980231098 20.5260845197976 53.7420620405994 20.5235929125834 53.747505360233 20.5472813301383 53.7507424262609 20.5396007236623 53.7584973152305 20.5364706645204 53.762216615285 20.5291308960789 53.7638965546885 20.5288480382847 53.7691071393095 20.5332981461671 53.7699562090345 20.5382832336659 53.7657379682098 20.5503787083628 53.7671956906804 20.5529782125306 53.7708880481995 20.5536608070234 53.7734614605013 20.5481906526832 53.7738540830596 20.5512454414177 53.7749586236529 20.5510450412236 53.7744982367374 20.5559174331813 53.7820134136484 20.5591006283905 53.7856723587343 20.5567154323296 53.7852977774319 20.5607236621722 53.7853596714766 20.5666449689087 53.7965197154047 20.5596676387517 53.7983346577335 20.5619508796236 53.8024733489208 20.558670503494 53.8033732102666 20.5614096454617 53.8042011101837 20.5550897395608 53.806538963797 20.5529563279632 53.8102230329001 20.5392682248713 53.8096126204403 20.5321934275053 53.8132099481952 20.5350425580123 53.813588755552 20.5395094292028 53.8160775412938 20.5376031722892 53.8145777792689 20.5271756691239 53.8165068467208 20.5236476296411 53.8155621763799 20.5186034947095 53.8178186389913 20.5153976970453 53.8167887738224 20.5115526465874 53.8199917784397 20.5089103931216 53.8176149918153 20.5043535213903 53.8217498799524 20.5024668297859 53.8224210076254 20.4920022338399 53.8243723283904 20.49207420608 53.8266789607079 20.4884535296459 53.8264136720984 20.4860603987772 53.8289849927508 20.4848504807726";
	
	LiniaOverlay linia;
	List<GeoPoint> points;
	
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
		
		points = getLine(olsztyn);
		linia = new LiniaOverlay(points,true);
		
		mapOverlays = mapView.getOverlays(); 
		drawable = this.getResources().getDrawable(R.drawable.marker); 		
		
		mapOverlays.add(linia);
		
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
		                        punkt = new OverlayItem(geoPoint, "Twoja usterka", "");
		                        	    
		                        
		                        itemizedoverlay = new ObjectItemizedOverlay(drawable, context);
		                        itemizedoverlay.addOverlay(punkt);
		            			itemizedoverlay.populateNow();
		            			mapOverlays.add(itemizedoverlay);
		            			
		            			mapOverlays.add(linia);
		            			
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
