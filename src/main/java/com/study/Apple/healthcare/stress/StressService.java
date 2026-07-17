package com.study.Apple.healthcare.stress;

import java.util.List;
import org.springframework.stereotype.Service;

/**
 * ストレスチェック機能の業務ロジックを処理します。
 *
 * <p>
 * 本クラスは以下の処理を主に行います。
 * <ul>
 * <li>入力チェック</li>
 * <li>合計点の算出とストレスレベルの判定</li>
 * </ul>
 *
 * <p>
 * <strong>注意：</strong>算出結果は健康啓発を目的とした簡易セルフチェックであり、
 * 医学的な診断を行うものではありません。強いストレスが続く場合は、
 * 医療機関や専門の相談窓口への相談を検討してください。
 *
 * @author 情報太郎
 */
@Service
public class StressService {

  /** 各質問の回答の最小値（1:ほとんどない） */
  static final int ANSWER_MIN = 1;

  /** 各質問の回答の最大値（4:いつもある） */
  static final int ANSWER_MAX = 4;

  /** 「ストレスは低め」と判定する合計点の上限 */
  private static final int THRESHOLD_LOW = 17;

  /** 「軽度のストレス」と判定する合計点の上限 */
  private static final int THRESHOLD_MILD = 25;

  /** 「中等度のストレス」と判定する合計点の上限 */
  private static final int THRESHOLD_MODERATE = 33;

  /**
   * 入力された内容をもとに、ストレスチェックの合計点とレベルを算出します。
   *
   * @param input 入力値(null不可)
   * @return 算出結果
   */
  public StressResultEntity calculate(StressInputData input) {
    StressResultEntity result = new StressResultEntity();

    int totalScore = toAnswerList(input).stream().mapToInt(Integer::parseInt).sum();
    result.setTotalScore(totalScore);

    if (totalScore <= THRESHOLD_LOW) {
      result.setLevelLabel("ストレスは低めです");
      result.setLevelClass("success");
      result.setAdviceMessage("今の生活リズムを維持しましょう。適度な休養と睡眠を心がけてください。");
    } else if (totalScore <= THRESHOLD_MILD) {
      result.setLevelLabel("軽度のストレスがみられます");
      result.setLevelClass("primary");
      result.setAdviceMessage("軽い疲れやストレスのサインが出ています。休息の時間を意識的に確保しましょう。");
    } else if (totalScore <= THRESHOLD_MODERATE) {
      result.setLevelLabel("中等度のストレスがみられます");
      result.setLevelClass("warning");
      result.setAdviceMessage("ストレスがやや高い状態です。生活リズムの見直しや、気分転換の時間を増やすことをおすすめします。");
    } else {
      result.setLevelLabel("高度のストレスがみられます");
      result.setLevelClass("danger");
      result.setAdviceMessage("ストレスがかなり高い状態です。一人で抱え込まず、周囲の人や専門の相談窓口・医療機関に相談することを検討してください。");
    }

    return result;
  }

  /**
   * 入力チェックを行います。
   *
   * @param input 入力値
   * @return 入力が正しい場合はtrue
   */
  boolean validate(StressInputData input) {
    if (input == null) {
      return false;
    }

    List<String> answers = toAnswerList(input);

    for (String answer : answers) {
      if (answer == null || answer.isBlank()) {
        return false;
      }

      int answerValue;
      try {
        answerValue = Integer.parseInt(answer);
      } catch (NumberFormatException e) {
        return false;
      }

      if (answerValue < ANSWER_MIN || answerValue > ANSWER_MAX) {
        return false;
      }
    }

    return true;
  }

  /**
   * 入力値を回答一覧（10問分）に変換します。
   *
   * @param input 入力値(null不可)
   * @return 回答一覧
   */
  private List<String> toAnswerList(StressInputData input) {
    return List.of(
        input.getQ1(), input.getQ2(), input.getQ3(), input.getQ4(), input.getQ5(),
        input.getQ6(), input.getQ7(), input.getQ8(), input.getQ9(), input.getQ10());
  }
}
