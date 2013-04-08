package pl.knp.naprawto.zgloszeniauzytkownika;

import pl.knp.naprawto.R;
import android.os.Bundle;

import com.google.android.maps.MapActivity;

public class MapaActivity extends MapActivity{

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.mapa);
		
	}
	
	@Override
	protected boolean isRouteDisplayed() {
		// TODO Auto-generated method stub
		return false;
	}

}
