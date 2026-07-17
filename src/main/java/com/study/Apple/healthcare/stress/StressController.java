package com.study.Apple.healthcare.stress;

import com.study.Apple.healthcare.user.LoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * ストレスチェック機能を表す。
 *
 * <p>
 * 10問の質問（それぞれ1〜4の4段階）への回答から、簡易的なストレスレベルを
 * 判定して表示します。
 *
 * <p>
 * 入力チェックを実施し、正しいデータ項目のみ処理します。
 * クライアント側でも入力チェックを実施することを推奨します。
 * <p>
 * <strong>本機能は健康啓発を目的とした簡易セルフチェックであり、医学的な診断ではありません。</strong>
 *
 * @author 情報太郎
 */
@Controller
public class StressController {

  /** ログインチェック用サービス */
  @Autowired
  private LoginService loginService;

  /** ストレスチェック機能の業務ロジッククラス */
  @Autowired
  private StressService stressService;

  /**
   * ストレスチェック画面を表示します。
   *
   * @param model Viewに値を渡すオブジェクト(null不可)
   * @return ストレスチェック画面へのパス(null不可)
   */
  @GetMapping("/stress")
  public String getStressForm(Model model) {
    // ログインチェック
    if (!loginService.isLogin())
      return "login";

    if (!model.containsAttribute("stressInputData")) {
      model.addAttribute("stressInputData", new StressInputData());
    }

    return "stress/list";
  }

  /**
   * 入力された回答をもとに、ストレスレベルを判定します。
   *
   * @param q1    質問1の回答(null不可)
   * @param q2    質問2の回答(null不可)
   * @param q3    質問3の回答(null不可)
   * @param q4    質問4の回答(null不可)
   * @param q5    質問5の回答(null不可)
   * @param q6    質問6の回答(null不可)
   * @param q7    質問7の回答(null不可)
   * @param q8    質問8の回答(null不可)
   * @param q9    質問9の回答(null不可)
   * @param q10   質問10の回答(null不可)
   * @param model Viewに値を渡すオブジェクト(null不可)
   * @return ストレスチェック画面へのパス(null不可)
   */
  @PostMapping("/stress/calculate")
  public String calculate(
      @RequestParam(name = "q1") String q1,
      @RequestParam(name = "q2") String q2,
      @RequestParam(name = "q3") String q3,
      @RequestParam(name = "q4") String q4,
      @RequestParam(name = "q5") String q5,
      @RequestParam(name = "q6") String q6,
      @RequestParam(name = "q7") String q7,
      @RequestParam(name = "q8") String q8,
      @RequestParam(name = "q9") String q9,
      @RequestParam(name = "q10") String q10,
      Model model) {
    // ログインチェック
    if (!loginService.isLogin())
      return "login";

    StressInputData stressInputData = new StressInputData();
    stressInputData.setQ1(q1);
    stressInputData.setQ2(q2);
    stressInputData.setQ3(q3);
    stressInputData.setQ4(q4);
    stressInputData.setQ5(q5);
    stressInputData.setQ6(q6);
    stressInputData.setQ7(q7);
    stressInputData.setQ8(q8);
    stressInputData.setQ9(q9);
    stressInputData.setQ10(q10);

    boolean isValid = stressService.validate(stressInputData);
    if (!isValid) {
      model.addAttribute("errorMessage", "すべての質問に回答してください。");
      model.addAttribute("stressInputData", stressInputData);
      return "stress/list";
    }

    StressResultEntity stressResultEntity = stressService.calculate(stressInputData);
    model.addAttribute("stressResultEntity", stressResultEntity);
    model.addAttribute("stressInputData", stressInputData);

    return "stress/list";
  }
}
