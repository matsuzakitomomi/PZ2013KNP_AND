package pl.knp.naprawto.zglaszanie;

import java.util.ArrayList;

import pl.knp.naprawto.OpisUsterki;
import pl.knp.naprawto.zgloszeniauzytkownika.SpisPunktow;
import pl.knp.zgloszenia.SpisPunktowWszystkich;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.graphics.drawable.Drawable;

import com.google.android.maps.ItemizedOverlay;
import com.google.android.maps.OverlayItem;

public class ObjectItemizedOverlay extends ItemizedOverlay<OverlayItem> {
	
	private ArrayList<OverlayItem> mOverlays = new ArrayList<OverlayItem>();  
	Context mContext; 
	boolean mapa_z_punktami;
	boolean typ;
	
	
	public ObjectItemizedOverlay(Drawable defaultMarker) {
		super(boundCenterBottom(defaultMarker));
	}
	
	public ObjectItemizedOverlay(Drawable defaultMarker,Context context) {
		super(boundCenterBottom(defaultMarker));
		mContext = context;
	}
	
	public ObjectItemizedOverlay(Drawable defaultMarker,Context context,boolean mapa_z_punktami,boolean typ) {
		super(boundCenterBottom(defaultMarker));
		mContext = context;
		this.mapa_z_punktami=true;
		this.typ=typ;
	}

	@Override
	protected OverlayItem createItem(int i) {
		return mOverlays.get(i);
	}

	@Override
	public int size() {
		return mOverlays.size();
	}
	
	public void addOverlay(OverlayItem overlay) {
	    mOverlays.add(overlay);
	}
	
	@Override
	protected boolean onTap(final int index) {
	  if(mapa_z_punktami)
	  {
		  AlertDialog.Builder dialog = new AlertDialog.Builder(mContext);
		  dialog.setTitle(mOverlays.get(index).getTitle());
		  dialog.setPositiveButton("Zobacz", new OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				Intent intent = new Intent(mContext, OpisUsterki.class);    		
	    		intent.putExtra("item", typ?SpisPunktow.areas.get(index):SpisPunktowWszystkich.areas.get(index));
	    		mContext.startActivity(intent);
				
			}
		});
		  dialog.setNegativeButton("Anuluj", new OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				
			}
		});
		  
		  dialog.show();
	  }
	  return true;
	}
	
	public void populateNow()
	{
	    populate(); 
	}

}

