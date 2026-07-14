package com.study.Apple.healthcare.weather;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class WeatherController {

  // "/weather" にアクセスがあったらこのメソッドが動く
  @GetMapping("/weather")
  public String showWeather() {
    // templatesフォルダ内の "weather.html" を返す
    return "weather";
  }
}
