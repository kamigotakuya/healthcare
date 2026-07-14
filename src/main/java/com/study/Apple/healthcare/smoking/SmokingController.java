package com.study.Apple.healthcare.smoking;

import com.study.Apple.healthcare.user.LoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * 喫煙リスクシミュレーション機能を表す。
 *
 * <p>
 * 性別・体重・喫煙開始年齢・1日の喫煙本数・現在の年齢から、喫煙による推定寿命の
 * 短縮をシミュレーションし表示します。
 *
 * <p>
 * 入力チェックを実施し、正しいデータ項目のみ処理します。
 * クライアント側でも入力チェックを実施することを推奨します。
 * <p>
 * <strong>本機能は健康啓発を目的とした簡易シミュレーションであり、医学的な診断・予測ではありません。</strong>
 *
 * @author 情報太郎
 */
@Controller
public class SmokingController {

  /** ログインチェック用サービス */
  @Autowired
  private LoginService loginService;

  /** 喫煙リスクシミュレーション機能の業務ロジッククラス */
  @Autowired
  private SmokingService smokingService;

  /**
   * 喫煙リスクシミュレーション画面を表示します。
   *
   * @param model Viewに値を渡すオブジェクト(null不可)
   * @return 喫煙リスクシミュレーション画面へのパス(null不可)
   */
  @GetMapping("/smoking")
  public String getSmokingForm(Model model) {
    // ログインチェック
    if (!loginService.isLogin())
      return "login";

    if (!model.containsAttribute("smokingInputData")) {
      model.addAttribute("smokingInputData", new SmokingInputData());
    }

    return "smoking/list";
  }

  /**
   * 入力された内容をもとに、喫煙による推定寿命の短縮をシミュレーションします。
   *
   * @param gender           性別("male"または"female")の文字列(null不可)
   * @param weightKg         体重(kg)の文字列(null不可)
   * @param smokingStartAge  喫煙を開始した年齢の文字列(null不可)
   * @param cigarettesPerDay 1日に吸うタバコの本数の文字列(null不可)
   * @param currentAge       現在の年齢の文字列(null不可)
   * @param model            Viewに値を渡すオブジェクト(null不可)
   * @return 喫煙リスクシミュレーション画面へのパス(null不可)
   */
  @PostMapping("/smoking/calculate")
  public String calculate(
      @RequestParam(name = "gender") String gender,
      @RequestParam(name = "weightKg") String weightKg,
      @RequestParam(name = "smokingStartAge") String smokingStartAge,
      @RequestParam(name = "cigarettesPerDay") String cigarettesPerDay,
      @RequestParam(name = "currentAge") String currentAge,
      Model model) {
    // ログインチェック
    if (!loginService.isLogin())
      return "login";

    SmokingInputData smokingInputData = new SmokingInputData();
    smokingInputData.setGender(gender);
    smokingInputData.setWeightKg(weightKg);
    smokingInputData.setSmokingStartAge(smokingStartAge);
    smokingInputData.setCigarettesPerDay(cigarettesPerDay);
    smokingInputData.setCurrentAge(currentAge);

    boolean isValid = smokingService.validate(smokingInputData);
    if (!isValid) {
      model.addAttribute("errorMessage",
          "入力項目に不備があります（体重10〜300kg、年齢1〜120歳、1日の本数0〜100本の範囲で、"
              + "喫煙開始年齢は現在の年齢以下で入力してください）");
      model.addAttribute("smokingInputData", smokingInputData);
      return "smoking/list";
    }

    SmokingResultEntity smokingResultEntity = smokingService.calculate(smokingInputData);
    model.addAttribute("smokingResultEntity", smokingResultEntity);
    model.addAttribute("smokingInputData", smokingInputData);

    return "smoking/list";
  }
}
