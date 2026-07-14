package com.study.Apple.healthcare.smoking;

/**
 * 喫煙リスクシミュレーション機能の算出結果を保持します。
 *
 * <p>
 * 本結果はあくまで健康啓発を目的とした簡易シミュレーションであり、
 * 医学的な診断・予測を行うものではありません。
 *
 * @author 情報太郎
 */
public class SmokingResultEntity {

  /** 喫煙しなかった場合の推定寿命(歳) */
  private double baseLifeExpectancy;

  /** 喫煙により短くなったと推定される寿命(年) */
  private double yearsLost;

  /** 喫煙を続けた場合の推定寿命＝死亡想定年齢(歳) */
  private double estimatedDeathAge;

  /** 死亡想定年齢に到達する西暦年 */
  private int estimatedDeathYear;

  /** 喫煙開始年齢が20歳未満（法律上の喫煙可能年齢未満）だったか */
  private boolean underageStart;

  /** エラーメッセージ(表示用) */
  private String errorMessage;

  public double getBaseLifeExpectancy() {
    return baseLifeExpectancy;
  }

  public void setBaseLifeExpectancy(double baseLifeExpectancy) {
    this.baseLifeExpectancy = baseLifeExpectancy;
  }

  public double getYearsLost() {
    return yearsLost;
  }

  public void setYearsLost(double yearsLost) {
    this.yearsLost = yearsLost;
  }

  public double getEstimatedDeathAge() {
    return estimatedDeathAge;
  }

  public void setEstimatedDeathAge(double estimatedDeathAge) {
    this.estimatedDeathAge = estimatedDeathAge;
  }

  public int getEstimatedDeathYear() {
    return estimatedDeathYear;
  }

  public void setEstimatedDeathYear(int estimatedDeathYear) {
    this.estimatedDeathYear = estimatedDeathYear;
  }

  public boolean isUnderageStart() {
    return underageStart;
  }

  public void setUnderageStart(boolean underageStart) {
    this.underageStart = underageStart;
  }

  public String getErrorMessage() {
    return errorMessage;
  }

  public void setErrorMessage(String errorMessage) {
    this.errorMessage = errorMessage;
  }
}
