package pl.knp.naprawto.user;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONObject;

import pl.knp.naprawto.R;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;



public class UserRejestracjaActivity extends Activity implements View.OnClickListener{

	Context context;
	
	EditText email;
	EditText haslo;
	EditText powtorzhaslo;
	
	Button utworzkonto;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.user_rejestracja);
		
		context=this;
		
		email = (EditText)findViewById(R.id.user_edit_email);
		haslo = (EditText)findViewById(R.id.user_edit_haslo);
		powtorzhaslo = (EditText)findViewById(R.id.user_edit_powtorz);
		utworzkonto = (Button)findViewById(R.id.user_btn_uwtowrz);
		
		utworzkonto.setOnClickListener(this);
		
	}

	@Override
	public void onClick(View v) {
		switch(v.getId())
		{
			case R.id.user_btn_uwtowrz:
				if(isEmailValid(email.getText().toString()))
				{
					if(sprawdzPoprawnoscHasel(haslo.getText().toString(), powtorzhaslo.getText().toString()))
					{
						if(sprawdzDlugoscHasla(haslo.getText().toString()))
						{
							new Rejestrowanie().execute(email.getText().toString(),md5(haslo.getText().toString()));
						}
						else
						{
							Toast.makeText(this, "Has³o jest zbyt krótkie", Toast.LENGTH_SHORT).show();
						}
					}
					else
					{
						Toast.makeText(this, "Has³a nie s¹ jednakowe", Toast.LENGTH_SHORT).show();
					}
				}
				else
				{
					Toast.makeText(this, "E-mail jest niepoprawny", Toast.LENGTH_SHORT).show();
				}
				break;
		}
		
	}	
	private boolean sprawdzPoprawnoscHasel(String haslo,String powtorzhaslo)
	{
		if(haslo.equalsIgnoreCase(powtorzhaslo)) return true;
		return false;
	}
	private boolean sprawdzDlugoscHasla(String haslo)
	{
		if(haslo.length()<7) return false;
		return true;
	}	
	private static final String md5(final String s) {
	    try {
	        // Create MD5 Hash
	        MessageDigest digest = java.security.MessageDigest.getInstance("MD5");
	        digest.update(s.getBytes());
	        byte messageDigest[] = digest.digest();

	        // Create Hex String
	        StringBuffer hexString = new StringBuffer();
	        for (int i = 0; i < messageDigest.length; i++) {
	            String h = Integer.toHexString(0xFF & messageDigest[i]);
	            while (h.length() < 2)
	                h = "0" + h;
	            hexString.append(h);
	        }
	        return hexString.toString();

	    } catch (NoSuchAlgorithmException e) {
	        e.printStackTrace();
	    }
	    return "";
	}
	boolean isEmailValid(CharSequence email) {
		   return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
		}
	
	private class Rejestrowanie extends AsyncTask<String, Void, Void> {

		private final ProgressDialog dialog = new ProgressDialog(UserRejestracjaActivity.this);	
		private boolean wynik=false;
		private boolean blad=false;
		
		protected void onPreExecute() {
	         this.dialog.setMessage("Rejestracja, proszê czekaæ...");
	         this.dialog.show();
	      }	
		@Override
		protected Void doInBackground(String... params) {
			this.dialog.setCancelable(false);
		    InputStream is = null;
		    String result = "";
		    
		    try{
   	            HttpClient httpclient = new DefaultHttpClient();
   	            HttpPost httppost = new HttpPost("http://darmowephp.cba.pl/naprawto/json/rejestracja/rejestracja.php?email="+params[0]+"&haslo="+params[1]);
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
	            if(!blad)
	            {
	            	if(wynik)
		            {
		            	Toast.makeText(context, "Twoje konto zosta³o utworzone pomyœlnie", Toast.LENGTH_LONG).show();
		            	finish();
		            }
		            else
		            {
		            	Toast.makeText(context, "Na podany email zosta³o zarejestrowane ju¿ konto", Toast.LENGTH_SHORT).show();
		            }
	            }
	            else
	            {
	            	Toast.makeText(context, "Coœ posz³o nie tak, spróbuj ponownie póŸniej", Toast.LENGTH_SHORT).show();
	            }
	         }
	      }
	}
}
