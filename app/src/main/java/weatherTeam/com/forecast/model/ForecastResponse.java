package weatherTeam.com.forecast.model;

import android.util.Log;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ForecastResponse {

    private static final String TAG = ForecastResponse.class.getSimpleName();

    @SerializedName("list")
    private List<Forecast> results;

    public ForecastResponse(String message, List<Forecast> results) {
        this.results = results;
    }

    public List<Forecast> getResults() {
        Log.e(TAG, results.toString());
        return results;
    }
    public void setResults(List<Forecast> results) {
        this.results = results;
    }
}
