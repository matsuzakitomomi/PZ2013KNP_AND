package pl.knp.naprawto.zglaszanie;

import pl.knp.naprawto.R;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.ScrollView;

import com.google.android.maps.MapActivity;
import com.google.android.maps.MapView;

public class ZglaszanieUsterkiActivity extends MapActivity{
	
	MapView mapView;
	ScrollView hsv;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);	
		setContentView(R.layout.zglaszanie_usterki);
		
		mapView = (MapView)findViewById(R.id.mapview);
		hsv = (ScrollView)findViewById(R.id.hsv);
		
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

}
