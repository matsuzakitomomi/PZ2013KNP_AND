package pl.knp.naprawto.zglaszanie;

import pl.knp.naprawto.R;
import android.app.Dialog;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ScrollView;
import android.widget.TextView;

import com.google.android.maps.MapActivity;
import com.google.android.maps.MapView;

public class ZglaszanieUsterkiActivity extends MapActivity implements View.OnClickListener{
	
	MapView mapView;
	ScrollView hsv;
	Dialog dialog;
	
	ImageButton zglaszanieusterkitytulplus;
	ImageButton zglaszanieusterkiopisplus;
	
	TextView zglaszanie_tytul;
	TextView zglaszanie_opis;
	
	String tytul;
	String opis;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);	
		setContentView(R.layout.zglaszanie_usterki);
		
		mapView = (MapView)findViewById(R.id.mapview);
		hsv = (ScrollView)findViewById(R.id.hsv);
		zglaszanieusterkitytulplus = (ImageButton)findViewById(R.id.zglaszanie_dodaj_tytul);
		zglaszanieusterkiopisplus = (ImageButton)findViewById(R.id.zglaszanie_dodaj_opis);
		
		zglaszanie_tytul = (TextView)findViewById(R.id.zglaszanie_tytul);
		zglaszanie_opis = (TextView)findViewById(R.id.zglaszanie_opis);
		
		zglaszanieusterkiopisplus.setOnClickListener(this);
		zglaszanieusterkitytulplus.setOnClickListener(this);
		
		mapView.setOnTouchListener(new OnTouchListener() {
		    @Override
		    public boolean onTouch(View v, MotionEvent event) {
		        switch (event.getAction()) {
		            case MotionEvent.ACTION_MOVE:
		                hsv.requestDisallowInterceptTouchEvent(true);
		                break;
		            case MotionEvent.ACTION_UP:
		            case MotionEvent.ACTION_CANCEL:
		                hsv.requestDisallowInterceptTouchEvent(false);
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
			dialog.setTitle("Wprowadü tytu≥: ");
			wprowadzanie.setText(tytul);
		}
		else
		{
			dialog.setTitle("Wprowadü opis: ");
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
	
		

}
