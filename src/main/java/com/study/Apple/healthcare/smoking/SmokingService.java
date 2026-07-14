package com.study.Apple.healthcare.smoking;

import org.springframework.stereotype.Service;

/**
 * 喫煙リスクシミュレーション機能の業務ロジックを処理します。
 *
 * <p>
 * 本クラスは以下の処理を主に行います。
 * <ul>
 * <li>入力チェック</li>
 * <li>推定寿命・短縮年数の算出</li>
 * </ul>
 *
 * <p>
 * <strong>注意：</strong>算出結果は健康啓発を目的とした簡易シミュレーションであり、
 * 統計的な平均値を用いた概算です。医学的な診断・予測を行うものではありません。
 *
 * @author 情報太郎
 */
@Service
public class SmokingService {

  /** 日本人男性の平均寿命(歳)の概算値 */
  private static final double BASE_LIFE_EXPECTANCY_MALE = 81.0;

  /** 日本人女性の平均寿命(歳)の概算値 */
  private static final double BASE_LIFE_EXPECTANCY_FEMALE = 87.0;

  /** 成人男性の平均体重(kg)の概算値 */
  private static final double AVG_WEIGHT_MALE = 65.0;

  /** 成人女性の平均体重(kg)の概算値 */
  private static final double AVG_WEIGHT_FEMALE = 53.0;

  /** 平均体重より10kg重いごとに寿命から減算する年数（肥満傾向による健康リスク分） */
  private static final double WEIGHT_PENALTY_PER_10KG_OVER = 0.3;

  /** 平均体重より10kg軽いごとに寿命から減算する年数（低体重傾向による健康リスク分） */
  private static final double WEIGHT_PENALTY_PER_10KG_UNDER = 0.2;

  /** タバコ1本あたりに失われるとされる時間(分)の概算値（研究知見に基づく目安） */
  private static final double MINUTES_LOST_PER_CIGARETTE = 11.0;

  /** 1年あたりの分数（換算用） */
  private static final double MINUTES_PER_YEAR = 60.0 * 24.0 * 365.0;

  /** 日本における法律上の喫煙可能年齢 */
  private static final int LEGAL_SMOKING_AGE = 20;

  /** 現在年齢からの最低残存年数（結果が現実離れしすぎないようにするための下限） */
  private static final double MIN_REMAINING_YEARS = 1.0;

  /** 体重の妥当な入力範囲(下限) */
  private static final double WEIGHT_MIN = 10.0;

  /** 体重の妥当な入力範囲(上限) */
  private static final double WEIGHT_MAX = 300.0;

  /** 年齢の妥当な入力範囲(下限) */
  private static final int AGE_MIN = 1;

  /** 年齢の妥当な入力範囲(上限) */
  private static final int AGE_MAX = 120;

  /** 1日の喫煙本数の妥当な入力範囲(上限) */
  private static final int CIGARETTES_MAX = 100;

  /**
   * 入力値をもとに、喫煙による推定寿命の短縮をシミュレーションします。
   *
   * @param input 入力値(null不可)
   * @return 算出結果
   */
  public SmokingResultEntity calculate(SmokingInputData input) {
    SmokingResultEntity result = new SmokingResultEntity();

    String gender = input.getGender();
    double weightKg = Double.parseDouble(input.getWeightKg());
    int smokingStartAge = Integer.parseInt(input.getSmokingStartAge());
    int cigarettesPerDay = Integer.parseInt(input.getCigarettesPerDay());
    int currentAge = Integer.parseInt(input.getCurrentAge());

    boolean isMale = "male".equals(gender);
    double baseLifeExpectancy = isMale ? BASE_LIFE_EXPECTANCY_MALE : BASE_LIFE_EXPECTANCY_FEMALE;
    double avgWeight = isMale ? AVG_WEIGHT_MALE : AVG_WEIGHT_FEMALE;

    // 体重による補正（平均よりの乖離に応じてわずかに増減）
    double weightDiff = weightKg - avgWeight;
    double weightAdjustment;
    if (weightDiff > 0) {
      weightAdjustment = -(weightDiff / 10.0) * WEIGHT_PENALTY_PER_10KG_OVER;
    } else {
      weightAdjustment = -(Math.abs(weightDiff) / 10.0) * WEIGHT_PENALTY_PER_10KG_UNDER;
    }
    double adjustedLifeExpectancy = baseLifeExpectancy + weightAdjustment;

    // 喫煙による寿命短縮の算出（喫煙開始から本来の寿命まで、同じ本数を吸い続けると仮定）
    double yearsSmokingProjected = Math.max(0, adjustedLifeExpectancy - smokingStartAge);
    double totalCigarettes = yearsSmokingProjected * 365.0 * cigarettesPerDay;
    double minutesLost = totalCigarettes * MINUTES_LOST_PER_CIGARETTE;
    double yearsLost = minutesLost / MINUTES_PER_YEAR;

    double estimatedDeathAge = adjustedLifeExpectancy - yearsLost;
    double floorAge = currentAge + MIN_REMAINING_YEARS;
    if (estimatedDeathAge < floorAge) {
      estimatedDeathAge = floorAge;
    }

    int currentYear = java.time.Year.now().getValue();
    int estimatedDeathYear = currentYear + (int) Math.round(estimatedDeathAge - currentAge);

    result.setBaseLifeExpectancy(round1(adjustedLifeExpectancy));
    result.setYearsLost(round1(yearsLost));
    result.setEstimatedDeathAge(round1(estimatedDeathAge));
    result.setEstimatedDeathYear(estimatedDeathYear);
    result.setUnderageStart(smokingStartAge < LEGAL_SMOKING_AGE);

    return result;
  }

  /**
   * 入力チェックを行います。
   *
   * @param input 入力値
   * @return 入力が正しい場合はtrue
   */
  boolean validate(SmokingInputData input) {
    if (input == null) {
      return false;
    }

    String gender = input.getGender();
    String weight = input.getWeightKg();
    String startAge = input.getSmokingStartAge();
    String cigarettes = input.getCigarettesPerDay();
    String currentAge = input.getCurrentAge();

    // null・必須チェック
    if (gender == null || gender.isBlank()
        || weight == null || weight.isBlank()
        || startAge == null || startAge.isBlank()
        || cigarettes == null || cigarettes.isBlank()
        || currentAge == null || currentAge.isBlank()) {
      return false;
    }

    // 性別チェック
    if (!"male".equals(gender) && !"female".equals(gender)) {
      return false;
    }

    // 数値形式チェック
    double weightValue;
    int startAgeValue;
    int cigarettesValue;
    int currentAgeValue;
    try {
      weightValue = Double.parseDouble(weight);
      startAgeValue = Integer.parseInt(startAge);
      cigarettesValue = Integer.parseInt(cigarettes);
      currentAgeValue = Integer.parseInt(currentAge);
    } catch (NumberFormatException e) {
      return false;
    }

    // 現実的な範囲チェック
    if (weightValue < WEIGHT_MIN || weightValue > WEIGHT_MAX) {
      return false;
    }
    if (currentAgeValue < AGE_MIN || currentAgeValue > AGE_MAX) {
      return false;
    }
    if (startAgeValue < AGE_MIN || startAgeValue > AGE_MAX) {
      return false;
    }
    if (cigarettesValue < 0 || cigarettesValue > CIGARETTES_MAX) {
      return false;
    }

    // 喫煙開始年齢は現在の年齢以下でなければならない
    if (startAgeValue > currentAgeValue) {
      return false;
    }

    return true;
  }

  private double round1(double value) {
    return Math.round(value * 10) / 10.0;
  }
}
