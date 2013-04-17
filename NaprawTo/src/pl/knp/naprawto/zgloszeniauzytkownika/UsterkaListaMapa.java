package pl.knp.naprawto.zgloszeniauzytkownika;

import java.io.Serializable;

import org.json.JSONException;
import org.json.JSONObject;

public class UsterkaListaMapa  implements Serializable{

	private static final long serialVersionUID = 1L;
	String title, dir, description, data;
	int longitude,latitude;
	
	public UsterkaListaMapa(JSONObject data) {

		try {
			title = data.getString("title");
			dir = data.getString("dir");
			description = data.getString("description");
			longitude = data.getInt("longitude");
			latitude = data.getInt("latitude");
			this.data=data.getString("data");
		} catch (JSONException e) {
			e.printStackTrace();
		}

    	
	}
	

}
