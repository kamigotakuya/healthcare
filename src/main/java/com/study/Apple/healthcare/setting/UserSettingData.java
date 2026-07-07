package com.study.Apple.healthcare.setting;

/**
 * 1件分の通知設定情報です。
 *
 * <p>
 * 「帰宅後リマインド通知機能」で扱うデータです。
 *
 * @author 情報太郎
 */
public class UserSettingData {

  /**
   * ユーザID（メールアドレス）
   * Userテーブルの主キーと紐づく
   */
  private String userId;

  /**
   * 通知時刻（HH:mm形式）
   * 例）仕事終わりの時間帯 "19:00"
   */
  private String notifyTime;

  /**
   * 通知ON/OFF
   */
  private boolean notifyEnabled;

  public String getUserId() {
    return userId;
  }

  public void setUserId(String userId) {
    this.userId = userId;
  }

  public String getNotifyTime() {
    return notifyTime;
  }

  public void setNotifyTime(String notifyTime) {
    this.notifyTime = notifyTime;
  }

  public boolean isNotifyEnabled() {
    return notifyEnabled;
  }

  public void setNotifyEnabled(boolean notifyEnabled) {
    this.notifyEnabled = notifyEnabled;
  }
}
