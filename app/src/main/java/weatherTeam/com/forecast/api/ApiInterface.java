package weatherTeam.com.forecast.api;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;
import weatherTeam.com.forecast.model.ForecastResponse;

public interface ApiInterface {
    @GET("find?&cnt=2&&cnt=2&appid= {YOUR API KEY} &units=imperial")
    Call<ForecastResponse> SearchCoords( @Query("lat") double lat,
                                              @Query("lon") double lon);
}
