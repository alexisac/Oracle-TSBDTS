package org.example.demotsbdts.sample;

import java.util.ArrayList;
import java.util.List;

public class WeatherReportService {
    public String describeTemperature(double temperatureCelsius) {
        if (temperatureCelsius < 0) {
            return "Freezing weather";
        }

        if (temperatureCelsius < 10) {
            return "Cold weather";
        }

        if (temperatureCelsius < 25) {
            return "Mild weather";
        }

        return "Hot weather";
    }

    public double convertCelsiusToFahrenheit(double celsius) {
        return celsius * 9 / 5 + 32;
    }

    public boolean isStormWarningRequired(double windSpeedKmh, boolean heavyRain) {
        return windSpeedKmh > 80 || heavyRain;
    }

    public List<String> filterRainyDays(List<String> dailyConditions) {
        List<String> rainyDays = new ArrayList<>();

        for (String condition : dailyConditions) {
            if (condition != null && condition.toLowerCase().contains("rain")) {
                rainyDays.add(condition);
            }
        }

        return rainyDays;
    }

    public double calculateAverageTemperature(List<Double> temperatures) {
        if (temperatures == null || temperatures.isEmpty()) {
            return 0;
        }

        double sum = 0;

        for (Double temperature : temperatures) {
            if (temperature != null) {
                sum += temperature;
            }
        }

        return sum / temperatures.size();
    }
}
