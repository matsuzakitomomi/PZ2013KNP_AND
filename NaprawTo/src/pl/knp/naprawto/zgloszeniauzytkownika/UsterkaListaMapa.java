package pl.knp.naprawto.zgloszeniauzytkownika;

import java.io.Serializable;

import org.json.JSONException;
import org.json.JSONObject;

public class UsterkaListaMapa  implements Serializable{

	private static final long serialVersionUID = 1L;
	public String title, dir, description, data;
	public int longitude,latitude,typ;
	
	public UsterkaListaMapa(JSONObject data) {

		try {
			this.title = data.getString("title");
			this.dir = data.getString("dir");
			this.description = data.getString("description");
			this.longitude = data.getInt("longitude");
			this.latitude = data.getInt("latitude");
			this.data=data.getString("data");
			this.typ=data.getInt("typ");
		} catch (JSONException e) {
			e.printStackTrace();
		}

    	
	}
	

}
