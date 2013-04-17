package pl.knp.naprawto;

import pl.knp.naprawto.user.UserDane;
import pl.knp.naprawto.zglaszanie.ZglaszanieUsterkiActivity;
import pl.knp.naprawto.zgloszeniauzytkownika.SpisPunktow;
import pl.knp.naprawto.zgloszeniauzytkownika.TabHostActivity;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MenuGlowneActivity extends Activity implements View.OnClickListener{
	
	TextView wyloguj;
	Button zglos_usterke;
	Button twoje_zgloszenia;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	
		setContentView(R.layout.menu_glowne);
		
		wyloguj = (TextView)findViewById(R.id.menu_wyloguj);
		wyloguj.setText(Html.fromHtml("<u>Wyloguj: "+UserDane.email+"</u>"));
		wyloguj.setOnClickListener(this);
		
		zglos_usterke = (Button)findViewById(R.id.menu_zglos_usterke);
		twoje_zgloszenia = (Button)findViewById(R.id.menu_zgloszenia_uzytkownika);
		
		twoje_zgloszenia.setOnClickListener(this);
		zglos_usterke.setOnClickListener(this);
		
	}
	
	@Override
	public void onBackPressed() {
		wyloguj();		
	}
	
	public void wyloguj()
	{
		    AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
		    alertDialog.setMessage("Czy na pewno chcesz siê wylogowaæ?");

		    alertDialog.setPositiveButton("Tak",
		            new DialogInterface.OnClickListener() {
		                public void onClick(DialogInterface dialog, int which) {
		                    dialog.cancel();
		                    SpisPunktow.wyczyscPunkty();
		                    finish();
		                }
		            });

		    alertDialog.setNegativeButton("Anuluj",
		            new DialogInterface.OnClickListener() {
		                public void onClick(DialogInterface dialog, int which) {
		                    dialog.cancel();
		                }
		            });
		    alertDialog.show();
	}

	@Override
	public void onClick(View v) {
		Intent intent;
		switch(v.getId())
		{
			case R.id.menu_wyloguj:
				wyloguj();
				break;
			case R.id.menu_zglos_usterke:
				intent = new Intent(this,ZglaszanieUsterkiActivity.class);
				startActivity(intent);
				break;
			case R.id.menu_zgloszenia_uzytkownika:
				intent = new Intent(this,TabHostActivity.class);
				startActivity(intent);
				break;
		}
		
	}

}
