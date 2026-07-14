package com.study.Apple.healthcare.smoking;

/**
 * 喫煙リスクシミュレーション機能の入力値（フォームから受け取った文字列そのまま）を保持します。
 *
 * <p>
 * 数値変換・妥当性チェックは {@link SmokingService} で行います。
 *
 * @author 情報太郎
 */
public class SmokingInputData {

  /** 性別（"male" または "female"） 必須入力 */
  private String gender;

  /** 体重(kg) 必須入力 */
  private String weightKg;

  /** 喫煙を開始した年齢 必須入力 */
  private String smokingStartAge;

  /** 1日に吸うタバコの本数 必須入力 */
  private String cigarettesPerDay;

  /** 現在の年齢 必須入力 */
  private String currentAge;

  public String getGender() {
    return gender;
  }

  public void setGender(String gender) {
    this.gender = gender;
  }

  public String getWeightKg() {
    return weightKg;
  }

  public void setWeightKg(String weightKg) {
    this.weightKg = weightKg;
  }

  public String getSmokingStartAge() {
    return smokingStartAge;
  }

  public void setSmokingStartAge(String smokingStartAge) {
    this.smokingStartAge = smokingStartAge;
  }

  public String getCigarettesPerDay() {
    return cigarettesPerDay;
  }

  public void setCigarettesPerDay(String cigarettesPerDay) {
    this.cigarettesPerDay = cigarettesPerDay;
  }

  public String getCurrentAge() {
    return currentAge;
  }

  public void setCurrentAge(String currentAge) {
    this.currentAge = currentAge;
  }
}
