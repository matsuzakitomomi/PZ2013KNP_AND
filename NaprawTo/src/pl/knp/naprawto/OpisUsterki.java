package pl.knp.naprawto;

import android.os.Bundle;

import com.google.android.maps.MapActivity;

public class OpisUsterki extends MapActivity {

	@Override
	protected void onCreate(Bundle icicle) {
			super.onCreate(icicle);
			setContentView(R.layout.opis_usterki);
	}
	
	@Override
	protected boolean isRouteDisplayed() {
		// TODO Auto-generated method stub
		return false;
	}

}
