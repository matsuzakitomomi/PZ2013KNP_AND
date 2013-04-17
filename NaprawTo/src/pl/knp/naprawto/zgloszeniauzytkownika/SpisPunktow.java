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

import pl.knp.naprawto.OpisUsterki;
import pl.knp.naprawto.R;
import pl.knp.naprawto.user.UserDane;
import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
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
	public static ArrayList<UsterkaListaMapa> areas = new ArrayList<UsterkaListaMapa>();
	Boolean loadingMore = false;
	static Boolean endrecords = false;
	ArrayAdapter<UsterkaListaMapa> adapter;
	static int page = 1;
	
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

				if((totalItemCount >= 8 || totalItemCount == 0) && (lastInScreen >= totalItemCount - 1) && !(loadingMore) && !(endrecords)) {					
					Thread thread =  new Thread(null, loadMoreListItems);
			        thread.start();
				}
			}
		});
		
		
	}
	
    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
    	    	    		
    		Intent intent = new Intent(this, OpisUsterki.class);
//    		intent.putExtra("title", item.getTitle());
//    		intent.putExtra("image", item.getImagePath());
//    		intent.putExtra("id", Integer.toString(item.getId()));
//    		intent.putExtra("typ", item.getType());
    		startActivity(intent);

    		    	
    	super.onListItemClick(l, v, position, id);
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
				Log.i("URL",url);
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
			ViewHolder holder;
			View v = convertView;
			if (v == null) {
				LayoutInflater vi = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				v = vi.inflate(R.layout.row_spis_usterek, null);
				holder = new ViewHolder();
				holder.tytul = (TextView) v.findViewById(R.id.tytul);
				holder.data = (TextView)v.findViewById(R.id.data);
				v.setTag(holder);
			}
			else
			{
				holder = (ViewHolder)v.getTag();
			}
			UsterkaListaMapa a = usterki.get(position);

			holder.data.setText(a.data);
			holder.tytul.setText(a.title);

			
			return v;
		}

		
	}
	
	static class ViewHolder
	{
		TextView tytul;
		TextView data;
	}
	
	public static void wyczyscPunkty()
	{
		areas.clear();
		endrecords = false;
		page = 1;
	}

}
