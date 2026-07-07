package com.study.Apple.healthcare.exercise;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * 「継続記録・カレンダー機能」「レベルアップ・進捗グラフ機能」で表示する
 * 集計済みの進捗情報を保持します。
 *
 * @author 情報太郎
 */
public class Progressinfo {

  /** 1レベルアップに必要な達成回数 */
  public static final int EXP_PER_LEVEL = 5;

  /** 現在のレベル（1から開始） */
  private int level = 1;

  /** 現レベル内での達成回数（0〜EXP_PER_LEVEL-1） */
  private int currentExp = 0;

  /** 次のレベルまでに必要な達成回数 */
  private int nextLevelExp = EXP_PER_LEVEL;

  /** 現レベル内の進捗率（%） */
  private int progressPercent = 0;

  /** 通算の運動達成回数 */
  private int totalCompletedCount = 0;

  /** 当月の達成率（%） */
  private int monthlyAchievementRate = 0;

  /**
   * 当月分のカレンダー表示用データ。
   * key: 日（1〜月末日）, value: その日に運動を1件以上達成していればtrue
   */
  private Map<Integer, Boolean> calendar = new LinkedHashMap<Integer, Boolean>();

  public int getLevel() {
    return level;
  }

  public void setLevel(int level) {
    this.level = level;
  }

  public int getCurrentExp() {
    return currentExp;
  }

  public void setCurrentExp(int currentExp) {
    this.currentExp = currentExp;
  }

  public int getNextLevelExp() {
    return nextLevelExp;
  }

  public void setNextLevelExp(int nextLevelExp) {
    this.nextLevelExp = nextLevelExp;
  }

  public int getProgressPercent() {
    return progressPercent;
  }

  public void setProgressPercent(int progressPercent) {
    this.progressPercent = progressPercent;
  }

  public int getTotalCompletedCount() {
    return totalCompletedCount;
  }

  public void setTotalCompletedCount(int totalCompletedCount) {
    this.totalCompletedCount = totalCompletedCount;
  }

  public int getMonthlyAchievementRate() {
    return monthlyAchievementRate;
  }

  public void setMonthlyAchievementRate(int monthlyAchievementRate) {
    this.monthlyAchievementRate = monthlyAchievementRate;
  }

  public Map<Integer, Boolean> getCalendar() {
    return calendar;
  }

  public void setCalendar(Map<Integer, Boolean> calendar) {
    this.calendar = calendar;
  }
}
