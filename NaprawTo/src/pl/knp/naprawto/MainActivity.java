package pl.knp.naprawto;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.widget.LinearLayout;

@SuppressLint("NewApi")
public class MainActivity extends Activity {

	LinearLayout prezentacja;
	BitmapDrawable tlo;
	
	int reqWidth;
	int reqHeight;
	int currentapiVersion;
	
	@SuppressWarnings("deprecation")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.prezentacja);
		
		reqWidth = getWindowManager().getDefaultDisplay().getWidth();
		reqHeight = getWindowManager().getDefaultDisplay().getHeight();
		currentapiVersion = android.os.Build.VERSION.SDK_INT;
		
		prezentacja = (LinearLayout)findViewById(R.id.prezentacja);
		tlo = new BitmapDrawable(getResources(),decodeSampledBitmapFromResource(getResources(), R.drawable.prezentacja, reqWidth, reqHeight));
		
		
		if (currentapiVersion >= android.os.Build.VERSION_CODES.JELLY_BEAN){
			prezentacja.setBackground(tlo);
		} else{
		    prezentacja.setBackgroundDrawable(tlo);
		}
		
		
		
		
	}


		
		public Bitmap decodeSampledBitmapFromResource(Resources res, int resId, int reqWidth, int reqHeight) 
		{ 
			final BitmapFactory.Options options = new BitmapFactory.Options();
			options.inJustDecodeBounds = true;
			BitmapFactory.decodeResource(res, resId, options); 
			options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight); 
			options.inJustDecodeBounds = false;  
			return BitmapFactory.decodeResource(res, resId, options);
		}
		
		public int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
			final int height = options.outHeight;
			final int width = options.outWidth;   
			int inSampleSize = 1;    
			if (height > reqHeight || width > reqWidth) 
			{
				final int heightRatio = Math.round((float) height / (float) reqHeight);
				final int widthRatio = Math.round((float) width / (float) reqWidth);   
				inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
			}
			return inSampleSize;		
		}
		
	


	@Override
	public void onBackPressed() {
	    super.onBackPressed();
	    tlo=null;
		System.gc();
	    
		finish();
	}
	
	@Override
	protected void onPause() {
		super.onPause();
		tlo=null;
		System.gc();
	}
}
