package algonquin.cst2335.macc0112;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URLEncoder;

import algonquin.cst2335.macc0112.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {


    protected String cityName;
    RequestQueue queue = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        queue =Volley.newRequestQueue(this);

        ActivityMainBinding binding = ActivityMainBinding.inflate( getLayoutInflater() );
        setContentView(binding.getRoot());

        binding.forecastButton.setOnClickListener(click -> {
            cityName = binding.cityTextField.getText().toString();
            String stringURL = "https://api.openweathermap.org/data/2.5/weather?q="
                + URLEncoder.encode(cityName)
                + "&appid=7e943c97096a9784391a981c4d878b22&units=metric";

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, stringURL, null,
                (response) -> {
                    try {

                        JSONObject coord = response.getJSONObject("coord");

                        JSONArray weatherArray = response.getJSONArray("weather");
                        JSONObject position0 = weatherArray.getJSONObject(0);

                        String description = position0.getString("description");
                        String iconName = position0.getString("icon");

                        JSONObject mainObject = response.getJSONObject("main");
                        double current = mainObject.getDouble("temp");
                        double min = mainObject.getDouble("temp_min");
                        double max = mainObject.getDouble("temp_max");
                        int humidity = mainObject.getInt("humidity");

                        String imageUrl = "https://openweathermap.org/img/w/01d.png";

                        ImageRequest imgReq = new ImageRequest(imageUrl, new Response.Listener>Bitmap<() {
                            @Override
                            public void onResponse(Bitmap bitmap) {
                                int i = 0;

                            }
                        }, 1024, 1024, ImageView.ScaleType.CENTER, null, (error ) -> {
                            int i = 0;
                        });

                        queue.add(imgReq);
                    } catch (JSONException e){
                        e.printStackTrace();
                    }

                },
                (error) -> {
                int i = 0;
                });
        queue.add(request);

    });


    }
}