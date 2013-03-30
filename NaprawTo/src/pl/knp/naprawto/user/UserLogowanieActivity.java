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

import pl.knp.naprawto.MenuGlowneActivity;
import pl.knp.naprawto.R;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import android.util.Log;
import android.view.View;

public class UserLogowanieActivity extends Activity implements View.OnClickListener{
	
	Context context;
	
	EditText email;
	EditText haslo;
	
	Button zaloguj;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.user_logowanie);
		
		context = this;
		
		email = (EditText)findViewById(R.id.user_edit_email);
		haslo = (EditText)findViewById(R.id.user_edit_haslo);
		
		zaloguj = (Button)findViewById(R.id.user_btn_zaloguj);
		
		zaloguj.setOnClickListener(this);
		
	}

	@Override
	protected void onResume() {
		email.setText(UserDane.email);
		super.onResume();
	}
	
	@Override
	public void onClick(View v) {
		switch(v.getId())
		{
			case R.id.user_btn_zaloguj:
				new Logowanie().execute(email.getText().toString(),md5(haslo.getText().toString()));
				break;
		}
		
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
	private class Logowanie extends AsyncTask<String, Void, Void> {

		private final ProgressDialog dialog = new ProgressDialog(UserLogowanieActivity.this);	
		private boolean wynik=false;
		private boolean blad=false;
		
		protected void onPreExecute() {
	         this.dialog.setMessage("Logowanie, proszê czekaæ...");
	         this.dialog.show();
	      }	
		@Override
		protected Void doInBackground(String... params) {
			this.dialog.setCancelable(false);
		    InputStream is = null;
		    String result = "";
		    
		    try{
   	            HttpClient httpclient = new DefaultHttpClient();
   	            HttpPost httppost = new HttpPost("http://darmowephp.cba.pl/naprawto/json/logowanie/logowanie.php?email="+params[0]+"&haslo="+params[1]);
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
	            		Toast.makeText(context, "Zosta³eœ zalogowany", Toast.LENGTH_SHORT).show();
	            		UserDane.email=email.getText().toString();
	            		finish();
	            		Intent intent = new Intent(context,MenuGlowneActivity.class);
	            		startActivity(intent);
	            	}
	            	else
	            	{
	            		Toast.makeText(context, "Poda³eœ z³e dane", Toast.LENGTH_SHORT).show();
	            	}
	            }
	         }
	      }
	}
}
