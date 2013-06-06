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

import pl.knp.imagedownload.ImageLoader;
import pl.knp.naprawto.OpisUsterki;
import pl.knp.naprawto.R;
import pl.knp.naprawto.user.UserDane;
import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

public class SpisPunktow extends ListActivity {
	
	ListView listView;
	public static ArrayList<UsterkaListaMapa> areas = new ArrayList<UsterkaListaMapa>();
	Boolean loadingMore = false;
	static Boolean endrecords = false;
	ArrayAdapter<UsterkaListaMapa> adapter;
	static int page = 1;
	
	ProgressBar progressBar;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.spis_usterek);
		
		listView = getListView();
		progressBar = (ProgressBar)findViewById(R.id.progressBar);
		
		adapter = new PunktyListAdapter(this,areas);
		setListAdapter(adapter);
		
		listView.setOnScrollListener(new OnScrollListener(){
			
			public void onScrollStateChanged(AbsListView view, int scrollState) {}
					
			public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

				int lastInScreen = firstVisibleItem + visibleItemCount;				

				if((totalItemCount >= 8 || totalItemCount == 0) && (lastInScreen >= totalItemCount - 1) && !(loadingMore) && !(endrecords)) {					
					new PobieranieElementow().execute("");
					loadingMore=true;
					progressBar.setVisibility(View.VISIBLE);
				}
			}
		});
		
		
	}
	
    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
    	    	    		
    		Intent intent = new Intent(this, OpisUsterki.class);    		
    		intent.putExtra("item", adapter.getItem(position));
    		startActivity(intent);

    		    	
    	super.onListItemClick(l, v, position, id);
    }
		
	private class PobieranieElementow extends AsyncTask<String, Void, Void> {

		@Override
		protected Void doInBackground(String... arg0) {
			try {
				String url = "http://naprawto.elektro-land.pl/json/pobieraniezgloszen/twojezgloszenia.php?email="+UserDane.email+"&max="+Integer.toString(page*10);
				HttpClient hc = new DefaultHttpClient();
				HttpGet get = new HttpGet(url);
				HttpResponse rp = hc.execute(get);
				if (rp.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
					String result = EntityUtils.toString(rp.getEntity());
					JSONArray sessions = new JSONArray(result);
					if(sessions.length()==0) endrecords=true;
					for (int i = 0; i < sessions.length(); i++) {
						JSONObject json = sessions.getJSONObject(i);
						UsterkaListaMapa usterka = new UsterkaListaMapa(json);
						areas.add(usterka);				
					}
				}
			} catch (Exception e) {
				Log.e("AreasFeedActivity", "Error loading areas JSON", e);
			}        	

			return null;
		}
			
		
		protected void onPostExecute(final Void unused) 
	    {
			 progressBar.setVisibility(View.INVISIBLE);
			 adapter.notifyDataSetChanged();
	         loadingMore = false;
	         page++;
	    }
		
	}
		
	private class PunktyListAdapter extends ArrayAdapter<UsterkaListaMapa> {
		
		private ArrayList<UsterkaListaMapa> usterki;   
		public ImageLoader imageLoader;
	
		public PunktyListAdapter(Context context, ArrayList<UsterkaListaMapa> items) {
			super(context, 0,items);		
			
			this.usterki = items;
			imageLoader = new ImageLoader(context);
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder holder;
			View v = convertView;
			if (v == null) {
				LayoutInflater vi = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				v = vi.inflate(R.layout.row_spis_usterek, null);
				holder = new ViewHolder();
				holder.tytul = (TextView) v.findViewById(R.id.tytul);
				holder.data = (TextView)v.findViewById(R.id.data);
				holder.photo = (ImageView)v.findViewById(R.id.photo);
				v.setTag(holder);
			}
			else
			{
				holder = (ViewHolder)v.getTag();
			}
			UsterkaListaMapa a = usterki.get(position);

			holder.data.setText(a.data);
			holder.tytul.setText(a.title);

			imageLoader.DisplayImage("http://naprawto.elektro-land.pl//images/"+a.dir.replace(".jpg", "")+"/"+a.dir, holder.photo);
			return v;
		}

		
	}
	
	static class ViewHolder
	{
		TextView tytul;
		TextView data;
		ImageView photo;
	}
	
	public static void wyczyscPunkty()
	{
		areas.clear();
		endrecords = false;
		page = 1;
	}

}
