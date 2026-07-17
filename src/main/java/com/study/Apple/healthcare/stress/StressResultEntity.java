package com.study.Apple.healthcare.stress;

/**
 * ストレスチェック機能の算出結果を保持します。
 *
 * <p>
 * 本結果はあくまで健康啓発を目的とした簡易セルフチェックであり、
 * 医学的な診断を行うものではありません。
 *
 * @author 情報太郎
 */
public class StressResultEntity {

  /** 合計点（10〜40点） */
  private int totalScore;

  /** ストレスレベルの表示ラベル（例：「ストレスは低めです」） */
  private String levelLabel;

  /** ストレスレベルに応じた表示色クラス（success / primary / warning / danger） */
  private String levelClass;

  /** ストレスレベルに応じたアドバイスメッセージ */
  private String adviceMessage;

  public int getTotalScore() {
    return totalScore;
  }

  public void setTotalScore(int totalScore) {
    this.totalScore = totalScore;
  }

  public String getLevelLabel() {
    return levelLabel;
  }

  public void setLevelLabel(String levelLabel) {
    this.levelLabel = levelLabel;
  }

  public String getLevelClass() {
    return levelClass;
  }

  public void setLevelClass(String levelClass) {
    this.levelClass = levelClass;
  }

  public String getAdviceMessage() {
    return adviceMessage;
  }

  public void setAdviceMessage(String adviceMessage) {
    this.adviceMessage = adviceMessage;
  }
}
