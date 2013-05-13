package pl.knp.naprawto;

import pl.knp.naprawto.user.UserLogowanieActivity;
import pl.knp.naprawto.user.UserRejestracjaActivity;
import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;

public class MainActivity extends Activity implements View.OnClickListener {

	RelativeLayout prezentacja;
	
	Button rejestracja;
	Button logowanie;
	
	int reqWidth;
	int reqHeight;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.prezentacja);
		
		reqWidth = getWindowManager().getDefaultDisplay().getWidth();
		reqHeight = getWindowManager().getDefaultDisplay().getHeight();
		
		prezentacja = (RelativeLayout)findViewById(R.id.prezentacja);
		rejestracja = (Button)findViewById(R.id.prezentacja_rejestracja);
		logowanie = (Button)findViewById(R.id.prezentacja_logowanie);
		
		logowanie.setOnClickListener(this);
		rejestracja.setOnClickListener(this);

		prezentacja.setBackgroundDrawable(new BitmapDrawable(getResources(),decodeSampledBitmapFromResource(getResources(), R.drawable.prezentacja, reqWidth, reqHeight)));

		
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
	public void onClick(View v) {
		Intent intent = null;
		switch(v.getId())
		{	
			case R.id.prezentacja_rejestracja:
				intent = new Intent(this,UserRejestracjaActivity.class);
				break;
			case R.id.prezentacja_logowanie:
				intent = new Intent(this,UserLogowanieActivity.class);
				break;
		}
		startActivity(intent);
		
	}
}
