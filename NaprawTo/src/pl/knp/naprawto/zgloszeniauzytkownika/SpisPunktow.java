package pl.knp.naprawto.zgloszeniauzytkownika;

import java.util.ArrayList;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import pl.knp.naprawto.R;
import pl.knp.naprawto.user.UserDane;
import android.app.ListActivity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class SpisPunktow extends ListActivity {
	
	ListView listView;
	ArrayList<UsterkaListaMapa> areas = new ArrayList<UsterkaListaMapa>();
	Boolean loadingMore = false;
	ArrayAdapter<UsterkaListaMapa> adapter;
	int page = 1;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.spis_usterek);
		
		listView = getListView();
		adapter = new PunktyListAdapter(this,areas);
		setListAdapter(adapter);
		
		listView.setOnScrollListener(new OnScrollListener(){
			
			public void onScrollStateChanged(AbsListView view, int scrollState) {}
					
			public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

				int lastInScreen = firstVisibleItem + visibleItemCount;				

				if((totalItemCount >= 20 || totalItemCount == 0) && (lastInScreen >= totalItemCount - 1) && !(loadingMore)) {					
					Thread thread =  new Thread(null, loadMoreListItems);
			        thread.start();
				}
			}
		});
		
		
	}
	
	private Runnable loadMoreListItems = new Runnable() {	
		
		public void run() {
			loadingMore = true;						
	        runOnUiThread(returnRes);
		}
	};
	
    private Runnable returnRes = new Runnable() {

        public void run() {
        	
			try {
				String url = "http://darmowephp.cba.pl/naprawto/json/pobieraniezgloszen/twojezgloszenia.php?email="+UserDane.email+"&max="+Integer.toString(page*10);
				
				HttpClient hc = new DefaultHttpClient();
				HttpGet get = new HttpGet(url);
				HttpResponse rp = hc.execute(get);
				if (rp.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
					String result = EntityUtils.toString(rp.getEntity());
					JSONArray sessions = new JSONArray(result);
					for (int i = 0; i < sessions.length(); i++) {
						JSONObject json = sessions.getJSONObject(i);
						UsterkaListaMapa usterka = new UsterkaListaMapa(json);
						areas.add(usterka);
					}
				}
			} catch (Exception e) {
				Log.e("AreasFeedActivity", "Error loading areas JSON", e);
			}        	

            adapter.notifyDataSetChanged();
			
            loadingMore = false;
            page++;
        }
    };	
	
	private class PunktyListAdapter extends ArrayAdapter<UsterkaListaMapa> {
		
		private ArrayList<UsterkaListaMapa> usterki;    
	
		public PunktyListAdapter(Context context, ArrayList<UsterkaListaMapa> items) {
			super(context, 0,items);		
			
			this.usterki = items;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {

			View v = convertView;
			if (v == null) {
				LayoutInflater vi = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				v = vi.inflate(R.layout.row_spis_usterek, null);
			}
			UsterkaListaMapa a = usterki.get(position);
			TextView tytul = (TextView) v.findViewById(R.id.tytul);

			tytul.setText(a.title);

			
			return v;
		}

		
	}

}
