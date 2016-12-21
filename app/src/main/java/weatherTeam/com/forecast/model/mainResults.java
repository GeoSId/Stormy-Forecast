package weatherTeam.com.forecast.model;

import com.google.gson.annotations.SerializedName;

public class mainResults {

    @SerializedName("temp")
    private double temp;

    public mainResults(double temp) {
        this.temp = temp;
    }

    public void setTemp(double temp) {
        this.temp = temp;
    }

    public int getTemp() {
        double answer = (temp - 32) * 5/9;
        return (int) Math.round(answer);
    }
}
