package com.study.Apple.healthcare.exercise;

/**
 * 運動タスクの負荷レベルです。
 *
 * <p>
 * 提案スライドの「項目のコンセプト」に基づき、
 * その日の疲れ具合に合わせて負荷レベルを選択できるようにします。
 *
 * @author 情報太郎
 */
public enum LoadLevel {

  /** ライト（気軽に始められる負荷） */
  LIGHT("ライト"),

  /** ふつう（標準的な負荷） */
  NORMAL("ふつう"),

  /** ハード（しっかり追い込む負荷） */
  HARD("ハード");

  /** 画面表示用のラベル */
  private final String label;

  LoadLevel(String label) {
    this.label = label;
  }

  public String getLabel() {
    return label;
  }

  /**
   * DBに保存されている文字列からLoadLevelへ変換します。
   *
   * @param value DB格納値（LIGHT/NORMAL/HARD）
   * @return 対応するLoadLevel。マッチしない場合はNORMAL
   */
  public static LoadLevel fromString(String value) {
    for (LoadLevel level : values()) {
      if (level.name().equalsIgnoreCase(value)) {
        return level;
      }
    }
    return NORMAL;
  }
}
