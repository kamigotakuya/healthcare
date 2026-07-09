package com.study.Apple.healthcare.body;

import com.study.Apple.healthcare.user.LoginService;
import java.time.LocalDate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * 体重・BMI推移記録機能を表す。
 *
 * <p>
 * 本機能は、体重・BMIの記録に関わるCRUDと、推移の可視化を実現します。
 *
 * <p>
 * 入力チェックを実施し、正しいデータ項目のみ処理します。
 * クライアント側でも入力チェックを実施することを推奨します。
 *
 * @author 情報太郎
 */
@Controller
public class BodyRecordController {

  /* ログインチェック用サービス */
  @Autowired
  private LoginService loginService;

  /* 体重・BMI記録機能の業務ロジッククラス */
  @Autowired
  private BodyRecordService bodyRecordService;

  /**
   * ログイン中のユーザに紐づく、体重・BMI推移記録画面を表示します。
   *
   * @param model Viewに値を渡すオブジェクト(null不可)
   * @return 体重・BMI推移記録画面へのパス(null不可)
   */
  @GetMapping("/body")
  public String getBodyRecordList(Model model) {
    // ログインチェック
    if (!loginService.isLogin())
    return "login";

    BodyRecordEntity bodyRecordEntity = bodyRecordService.selectAll(loginService.getLoginUserId());
    model.addAttribute("bodyRecordEntity", bodyRecordEntity);
    model.addAttribute("today", LocalDate.now().toString());

    return "body/list";
  }

  /**
   * 入力された身長・体重をもとにBMIを算出し、DBへ登録します。
   *
   * @param height 身長(cm)の文字列を格納(null不可)
   * @param weight 体重(kg)の文字列を格納(null不可)
   * @param date   記録日の文字列を格納(null不可)
   * @param model  Viewに値を渡すオブジェクト(null不可)
   * @return 体重・BMI推移記録画面へのパス(null不可)
   */
  @PostMapping("/body/insert")
  public String insertBodyRecord(
      @RequestParam(name = "height") String height,
      @RequestParam(name = "weight") String weight,
      @RequestParam(name = "date") String date,
      Model model) {
    // 入力チェック
    boolean isValid = bodyRecordService.validate(height, weight, date);
    if (!isValid) {
      model.addAttribute("errorMessage", "入力項目に不備があります（身長30〜250cm、体重10〜300kgの範囲で入力してください）");
      return getBodyRecordList(model);
    }

    boolean isSuccess = bodyRecordService.insert(loginService.getLoginUserId(), height, weight, date);
    if (isSuccess) {
      model.addAttribute("message", "正常に登録されました");
    } else {
      model.addAttribute("errorMessage", "登録できませんでした。再度登録し直してください");
    }

    return getBodyRecordList(model);
  }

  /**
   * 指定された記録IDをDBから削除します。
   *
   * @param id    記録IDの文字列を格納(null不可)
   * @param model Viewに値を渡すオブジェクト(null不可)
   * @return 体重・BMI推移記録画面へのパス(null不可)
   */
  @PostMapping("/body/delete")
  public String deleteBodyRecord(@RequestParam(name = "id") String id, Model model) {
    boolean isValid = bodyRecordService.validate(id);
    if (!isValid) {
      return "index";
    }

    boolean isSuccess = bodyRecordService.delete(id, loginService.getLoginUserId());
    if (isSuccess) {
      model.addAttribute("message", "正常に削除されました");
    } else {
      model.addAttribute("errorMessage", "削除できませんでした。再度登録し直してください");
    }
    return getBodyRecordList(model);
  }
}
