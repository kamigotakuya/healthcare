package com.study.xxaxxx.healthcare.task;

import java.util.Date;

/**
 * 1件分のタスク情報です。
 *
 * <p>各データ構造については、データベース定義をご覧ください。
 * @author 情報太郎
 */
public class TaskData {

  /**
   * タスクID
   * 主キー、SQLにて自動採番
   */
  private int id;

  /**
   * ユーザID（メールアドレス）
   * Userテーブルの主キーと紐づく、ログイン情報から取得
   */
  private String userId;

  /**
   * 件名
   * 必須入力
   */
  private String title;

  /**
   * 期限日
   * 必須入力
   */
  private Date limitday;

  /**
   * 完了フラグ
   * デフォルト値は、false(未完了)
   */
  private boolean isComplate;

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

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public Date getLimitday() {
    return limitday;
  }

  public void setLimitday(Date limitday) {
    this.limitday = limitday;
  }

  public boolean isComplate() {
    return isComplate;
  }

  public void setComplate(boolean isComplate) {
    this.isComplate = isComplate;
  }
}
