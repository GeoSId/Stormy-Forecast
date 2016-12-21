package weatherTeam.com.forecast;


import android.Manifest;
import android.app.NotificationManager;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.NotificationCompat;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import weatherTeam.com.forecast.api.ApiClient;
import weatherTeam.com.forecast.api.ApiInterface;
import weatherTeam.com.forecast.model.ForecastResponse;
import weatherTeam.com.stormy.R;
import weatherTeam.com.forecast.model.Forecast;

public class MainActivity extends AppCompatActivity implements LocationProvider.LocationCallback{

    private LocationProvider mLocationProvider;
    private Bundle mBundle;
    private double currentLatitude;
    private double currentLongitude;
    protected final static String ERROR_KEY = "error-key";

    @BindView(R.id.timeCurrent) TextView mTime;
    @BindView(R.id.temp) TextView mTemperature;
    @BindView(R.id.refreshImageView) ImageView mRefreshImage;
    @BindView(R.id.progressBar) ProgressBar mProgressBar;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        //default value London
        currentLatitude = 51.509865;
        currentLongitude =-0.118092;

        mLocationProvider = new LocationProvider(this, this);

        mBundle = new Bundle();
        mProgressBar.setVisibility(View.INVISIBLE);

        mRefreshImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mLocationProvider.connect();
                forecast();
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        mLocationProvider.connect();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mLocationProvider.connect();
        GrandPermission();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mLocationProvider.disconnect();
    }

    public void GrandPermission() {
        if (!checkPermissionExists()) {
            requestForSpecificPermission();
        }
        LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            Toast.makeText(this, R.string.GpsOn, Toast.LENGTH_SHORT).show();
        } else {
            showGPSDisabledAlertToUser();
        }
    }
    private void showGPSDisabledAlertToUser(){
        alertUserAboutError("GpsOff");
    }
    private void requestForSpecificPermission() {
        ActivityCompat.requestPermissions(this, new String[]{ Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION}, 101);
    }
    private boolean checkPermissionExists() {
        int result = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION);
        return result == PackageManager.PERMISSION_GRANTED;
    }

    public void forecast() {

        ApiInterface apiService =
                ApiClient.getClient().create(ApiInterface.class);
        Call<ForecastResponse> call = apiService.SearchCoords(currentLatitude , currentLongitude);

        toggleRefresh();
        if (isNetworkAvailable()) {

            call.enqueue(new Callback<ForecastResponse>() {
                @Override
                public void onResponse(Call<ForecastResponse> call, Response<ForecastResponse> response) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            toggleRefresh();
                        }
                    });

                    if (response.isSuccessful()) {
                        ForecastResponse json =  response.body();
                        List<Forecast> rew = json.getResults();
                        try {
                            getDetails(rew);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                //updateDisplay();
                            }
                        });
                    } else {
                        mLocationProvider.disconnect();
                        alertUserAboutError("Error Not responding ");
                    }
                }
                @Override
                public void onFailure(Call<ForecastResponse> call, Throwable t) {
                    String statusCode = t.toString();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            toggleRefresh();
                        }
                    });
                    alertUserAboutError("Error Fail ");
                }
            });
        }
    }

    private void toggleRefresh() {
        if (mProgressBar.getVisibility() == View.INVISIBLE) {
            mProgressBar.setVisibility(View.VISIBLE);
            mRefreshImage.setVisibility(View.INVISIBLE);
        }
        else {
            mProgressBar.setVisibility(View.INVISIBLE);
            mRefreshImage.setVisibility(View.VISIBLE);
        }
    }

    public ForecastResponse getDetails (List<Forecast> AllData)throws JSONException {
        mTemperature.setText(AllData.get(0).getMain().getTemp() +"");
        mTime.setText(AllData.get(0).getFormattedTime());
        return null;
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager manager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = manager.getActiveNetworkInfo();
        boolean isAvailable = false;
        if (networkInfo != null && networkInfo.isConnected()) {
            isAvailable = true;
        }
        return isAvailable;
    }

    public void alertUserAboutError(String error) {
        AlertDialogFragment dialogFragment = new AlertDialogFragment();
        mBundle.putString(ERROR_KEY, error);
        dialogFragment.setArguments(mBundle);
        dialogFragment.show(getFragmentManager(), "dialogMessage");
    }

    @Override
    public void handleNewLocation(Location location) {
        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        NotificationCompat.Builder noti = new NotificationCompat.Builder(this);
        noti.setContentTitle("Your Location");
        noti.setContentText(location.getLatitude() + ",  " + location.getLongitude());
        noti.setSmallIcon(R.drawable.rain);
        notificationManager.notify(1234, noti.build());

        currentLatitude  = location.getLatitude();
        currentLongitude = location.getLongitude();
        forecast();
    }
}
