package weatherTeam.com.forecast.model;

import com.google.gson.annotations.SerializedName;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class Forecast {

    private static final String TAG = Forecast.class.getSimpleName();

    @SerializedName("id")
    private Integer id;
    @SerializedName("name")
    private String name;
    @SerializedName("main")
    private mainResults main;
    @SerializedName("dt")
    private int dt;


    public Forecast(Integer id, String name, mainResults main, int dt) {
        this.id = id;
        this.name = name;
        this.main = main;
        this.dt = dt;
    }

    public String getFormattedTime(){
        Date dateTime = new Date(getDt() * 1000);
        SimpleDateFormat inputFormat = new SimpleDateFormat("MM/dd, HH:mm aa", Locale.ENGLISH);
        String timeString = inputFormat.format(dateTime);
        return timeString;
    }

    public int getDt() {
        return dt;
    }

    public void setDt(int dt) {
        this.dt = dt;
    }

    public static String getTAG() {
        return TAG;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public mainResults getMain() {
        return main;
    }

    public void setMain(mainResults main) {
        this.main = main;
    }

}
