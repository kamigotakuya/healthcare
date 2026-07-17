package com.study.Apple.healthcare.stress;

/**
 * ストレスチェック機能の入力値（フォームから受け取った文字列そのまま）を保持します。
 *
 * <p>
 * 質問は10問あり、それぞれ1〜4の4段階（1:ほとんどない、2:ときどきある、3:しばしばある、4:いつもある）で回答します。
 *
 * <p>
 * 数値変換・妥当性チェックは {@link StressService} で行います。
 *
 * @author 情報太郎
 */
public class StressInputData {

  /** 質問1（よく眠れないことがある） 必須入力 */
  private String q1;

  /** 質問2（イライラすることが多い） 必須入力 */
  private String q2;

  /** 質問3（食欲不振や過食がある） 必須入力 */
  private String q3;

  /** 質問4（集中力が続かない） 必須入力 */
  private String q4;

  /** 質問5（疲れがとれない） 必須入力 */
  private String q5;

  /** 質問6（頭痛や肩こりを感じる） 必須入力 */
  private String q6;

  /** 質問7（些細なことで落ち込む） 必須入力 */
  private String q7;

  /** 質問8（やる気が出ない） 必須入力 */
  private String q8;

  /** 質問9（人と話すのが億劫に感じる） 必須入力 */
  private String q9;

  /** 質問10（将来に対して不安を感じる） 必須入力 */
  private String q10;

  public String getQ1() {
    return q1;
  }

  public void setQ1(String q1) {
    this.q1 = q1;
  }

  public String getQ2() {
    return q2;
  }

  public void setQ2(String q2) {
    this.q2 = q2;
  }

  public String getQ3() {
    return q3;
  }

  public void setQ3(String q3) {
    this.q3 = q3;
  }

  public String getQ4() {
    return q4;
  }

  public void setQ4(String q4) {
    this.q4 = q4;
  }

  public String getQ5() {
    return q5;
  }

  public void setQ5(String q5) {
    this.q5 = q5;
  }

  public String getQ6() {
    return q6;
  }

  public void setQ6(String q6) {
    this.q6 = q6;
  }

  public String getQ7() {
    return q7;
  }

  public void setQ7(String q7) {
    this.q7 = q7;
  }

  public String getQ8() {
    return q8;
  }

  public void setQ8(String q8) {
    this.q8 = q8;
  }

  public String getQ9() {
    return q9;
  }

  public void setQ9(String q9) {
    this.q9 = q9;
  }

  public String getQ10() {
    return q10;
  }

  public void setQ10(String q10) {
    this.q10 = q10;
  }
}
