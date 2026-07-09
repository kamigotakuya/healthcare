package com.study.Apple.healthcare.body;

import java.util.Date;

/**
 * 1件分の体重・BMI記録情報です。
 *
 * <p>各データ構造については、データベース定義をご覧ください。
 *
 * @author 情報太郎
 */
public class BodyRecordData {

  /** 記録ID 主キー、SQLにて自動採番 */
  private int id;

  /** ユーザID（メールアドレス） Userテーブルの主キーと紐づく、ログイン情報から取得 */
  private String userId;

  /** 記録日 必須入力 */
  private Date recordDate;

  /** 身長(cm) 必須入力 */
  private double heightCm;

  /** 体重(kg) 必須入力 */
  private double weightKg;

  /** BMI値 身長・体重から算出（登録時にサーバー側で計算） */
  private double bmi;

  /** BMI判定区分（低体重・普通体重・肥満 など） 表示用にサーバー側で判定 */
  private String bmiCategory;

  /** BMI判定に対応するイメージ画像のファイル名（表示用） */
  private String bmiImage;

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public String getUserId() {
    return userId;
  }

  public void setUserId(String userId) {
    this.userId = userId;
  }

  public Date getRecordDate() {
    return recordDate;
  }

  public void setRecordDate(Date recordDate) {
    this.recordDate = recordDate;
  }

  public double getHeightCm() {
    return heightCm;
  }

  public void setHeightCm(double heightCm) {
    this.heightCm = heightCm;
  }

  public double getWeightKg() {
    return weightKg;
  }

  public void setWeightKg(double weightKg) {
    this.weightKg = weightKg;
  }

  public double getBmi() {
    return bmi;
  }

  public void setBmi(double bmi) {
    this.bmi = bmi;
  }

  public String getBmiCategory() {
    return bmiCategory;
  }

  public void setBmiCategory(String bmiCategory) {
    this.bmiCategory = bmiCategory;
  }

  public String getBmiImage() {
    return bmiImage;
  }

  public void setBmiImage(String bmiImage) {
    this.bmiImage = bmiImage;
  }
}
