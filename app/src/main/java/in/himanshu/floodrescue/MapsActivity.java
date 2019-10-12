package in.himanshu.floodrescue;

import androidx.annotation.DrawableRes;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMapOptions;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import in.himanshu.floodrescue.models.LocationModel;
import in.himanshu.floodrescue.models.LocationPoint;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, LocationListener {

    private GoogleMap mMap;
    LocationManager manager;
    RequestQueue requestQueue;
    GoogleMapOptions googleMapOptions;

    EditText mymapedittext;
    ImageView mapsearchbutton;

    String url1 = "https://api.opencagedata.com/geocode/v1/json?q=";
    String url1Key = "&key=697bd67e3af841aaba3802530733b04d";

    String newUrl = "http://www.mapquestapi.com/geocoding/v1/address?key=AkMUFNnmLPpEcN01liRMkGRticnOaysq&location=";

    double centerX, centerY;

    ArrayList<LocationModel> locationModels;

    ArrayList<LocationModel> locationModelsTemp;

    double northLat, southLat, northLng, southLng;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        mymapedittext = findViewById(R.id.mymapedittext);
        mapsearchbutton = findViewById(R.id.mapsearchbutton);

        manager = (LocationManager) getSystemService(LOCATION_SERVICE);

        requestQueue = Volley.newRequestQueue(MapsActivity.this);

        mapsearchbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getTheCity();
            }
        });

        googleMapOptions = new GoogleMapOptions();

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        manager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, (LocationListener) MapsActivity.this);

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);

//        getLocationList();

//        Collections.sort(locations);


//        Toast.makeText(this, "" + locations.size(), Toast.LENGTH_SHORT).show();
//

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        mMap.setMyLocationEnabled(true);

    }

    @Override
    public void onLocationChanged(Location location) {

    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {

    }

    @Override
    public void onProviderEnabled(String s) {

    }

    @Override
    public void onProviderDisabled(String s) {

    }



    private void getTheCity() {

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, newUrl + mymapedittext.getText().toString().trim().toLowerCase(), new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                try {

                    JSONArray results = response.getJSONArray("results");

                    JSONObject object = results.getJSONObject(0);

                    JSONArray locations = object.getJSONArray("locations");

                    JSONObject newObject = locations.getJSONObject(0);

                    JSONObject latLng = newObject.getJSONObject("latLng");

                    String lat = latLng.getString("lat");
                    String lng = latLng.getString("lng");

                    centerX = Double.parseDouble(lat);
                    centerY = Double.parseDouble(lng);

                    getTheLocations();

                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });

        requestQueue.add(request);

    }


    private void getTheLocations() {

//        Toast.makeText(this, "working", Toast.LENGTH_SHORT).show();

        StringBuilder url = new StringBuilder("https://api.jawg.io/elevations?locations=");
        String token = "&access-token=ulf4xCeZpdesSZEFYP6GFjeg0Xb840XCwX3DxQ1ODaGLpuHPeqBUbaL3QDyjpS4t";


        //******************************circular search****************************************************//

//        for(double x = 0; x < 0.5; x++) {
//
////            drawCircle(centerX, centerY, x);
//
//
//
//
//
//        }



        northLat = centerX + 0.06;
        southLat = centerX - 0.06;
        northLng = centerY + 0.06;
        southLng = centerY - 0.06;

        //*******************************rectangular search**************************************************//

        for(double x = northLat>southLat?northLat:southLat; x > (northLat<southLat?northLat:southLat); x-=0.01) {

            for(double y = northLng>southLng?northLng:southLng; y > (northLng<southLng?northLng:southLng); y-=0.02) {

                url.append(x).append(",").append(y).append("%7c");

            }

        }

        url = new StringBuilder(url.substring(0, url.length() - 3));

        url.append(token);

        final double[] avg = new double[1];

        final double[] min = {10000};
        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, url.toString(), new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {

                locationModels = new ArrayList<>();

                try {
                    for (int i = 0; i < response.length() ; i++) {

                        JSONObject obj = response.getJSONObject(i);
                        String elevation = obj.getString("elevation");
                        JSONObject loc = obj.getJSONObject("location");
                        String longitude = loc.getString("lng");
                        String latitude = loc.getString("lat");

                        locationModels.add(new LocationModel(Double.parseDouble(elevation), Double.parseDouble(longitude), Double.parseDouble(latitude)));

                        avg[0] += Double.parseDouble(elevation);
                        if(Double.parseDouble(elevation) < min[0]) {
                            min[0] = Double.parseDouble(elevation);
                        }

                    }

                } catch (Exception e) {

                }

                locationModelsTemp = new ArrayList<>();


                double comp = avg[0]/locationModels.size();

//                Collections.sort(locationModels);

                Collections.sort(locationModels, new Comparator<LocationModel>() {
                    @Override
                    public int compare(LocationModel lm1, LocationModel lm2) {
                        return Double.compare(lm1.getElevation(), lm2.getElevation());
                    }
                });

                for(int i = 0; i < (8<locationModels.size()?8:locationModels.size()); i++) {
                    locationModelsTemp.add(locationModels.get(i));
                }

                getTheMapDone();

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Toast.makeText(MapsActivity.this, "Not working", Toast.LENGTH_SHORT).show();

            }
        });

        requestQueue.add(request);


    }

    private void getTheMapDone() {

        LatLng needed = new LatLng(locationModelsTemp.get(0).getLatitude(), locationModelsTemp.get(0).getLongitude());

//        Collections.sort(MainActivity.locationModelsTemp);

        int count = 0;
        for(LocationModel x : locationModelsTemp) {
            count++;
            LatLng sydney = new LatLng(x.getLatitude(), x.getLongitude());


            switch (count) {

                case 1:
                    mMap.addMarker(new MarkerOptions().position(sydney).title("Marked " + count + " : " + x.getElevation()).icon(bitmapDescriptorFromVector(MapsActivity.this,R.drawable.ic_one)));
                    break;
                case 2:
                    mMap.addMarker(new MarkerOptions().position(sydney).title("Marked " + count + " : " + x.getElevation()).icon(bitmapDescriptorFromVector(MapsActivity.this,R.drawable.ic_two)));

                    break;
                case 3:
                    mMap.addMarker(new MarkerOptions().position(sydney).title("Marked " + count + " : " + x.getElevation()).icon(bitmapDescriptorFromVector(MapsActivity.this,R.drawable.ic_three)));

                    break;
                case 4:
                    mMap.addMarker(new MarkerOptions().position(sydney).title("Marked " + count + " : " + x.getElevation()).icon(bitmapDescriptorFromVector(MapsActivity.this,R.drawable.ic_four)));

                    break;
                case 5:
                    mMap.addMarker(new MarkerOptions().position(sydney).title("Marked " + count + " : " + x.getElevation()).icon(bitmapDescriptorFromVector(MapsActivity.this,R.drawable.ic_five)));

                    break;
                case 6:
                    mMap.addMarker(new MarkerOptions().position(sydney).title("Marked " + count + " : " + x.getElevation()).icon(bitmapDescriptorFromVector(MapsActivity.this,R.drawable.ic_six)));

                    break;
                case 7:
                    mMap.addMarker(new MarkerOptions().position(sydney).title("Marked " + count + " : " + x.getElevation()).icon(bitmapDescriptorFromVector(MapsActivity.this,R.drawable.ic_seven)));

                    break;
                case 8:
                    mMap.addMarker(new MarkerOptions().position(sydney).title("Marked " + count + " : " + x.getElevation()).icon(bitmapDescriptorFromVector(MapsActivity.this,R.drawable.ic_eight)));

                    break;


            }



        }

        CameraUpdate zoom=CameraUpdateFactory.zoomTo(15);
        mMap.moveCamera(CameraUpdateFactory.newLatLng(needed));
        mMap.animateCamera(zoom);


    }


    private BitmapDescriptor bitmapDescriptorFromVector(Context context, int vectorResId) {
        Drawable vectorDrawable = ContextCompat.getDrawable(context, vectorResId);
        vectorDrawable.setBounds(0, 0, vectorDrawable.getIntrinsicWidth(), vectorDrawable.getIntrinsicHeight());
        Bitmap bitmap = Bitmap.createBitmap(vectorDrawable.getIntrinsicWidth(), vectorDrawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        vectorDrawable.draw(canvas);
        return BitmapDescriptorFactory.fromBitmap(bitmap);
    }

}
