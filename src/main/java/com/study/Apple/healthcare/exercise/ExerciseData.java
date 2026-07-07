package com.study.Apple.healthcare.exercise;

import java.util.Date;

/**
 * 1件分の運動タスク情報です。
 *
 * <p>
 * 「デイリー運動ToDoリスト機能」で扱うデータです。
 * 各データ構造については、データベース定義をご覧ください。
 *
 * @author 情報太郎
 */
public class ExerciseData {

  /**
   * 運動タスクID
   * 主キー、SQLにて自動採番
   */
  private int id;

  /**
   * ユーザID（メールアドレス）
   * Userテーブルの主キーと紐づく、ログイン情報から取得
   */
  private String userId;

  /**
   * 件名（運動内容）
   * 必須入力
   */
  private String title;

  /**
   * 負荷レベル
   * LIGHT(ライト) / NORMAL(ふつう) / HARD(ハード)
   */
  private LoadLevel loadLevel;

  /**
   * 実施予定日
   * 必須入力
   */
  private Date exerciseDate;

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

  public LoadLevel getLoadLevel() {
    return loadLevel;
  }

  public void setLoadLevel(LoadLevel loadLevel) {
    this.loadLevel = loadLevel;
  }

  public Date getExerciseDate() {
    return exerciseDate;
  }

  public void setExerciseDate(Date exerciseDate) {
    this.exerciseDate = exerciseDate;
  }

  public boolean isComplate() {
    return isComplate;
  }

  public void setComplate(boolean isComplate) {
    this.isComplate = isComplate;
  }
}
