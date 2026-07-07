package com.study.Apple.healthcare.setting;

import com.study.Apple.healthcare.user.LoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * 通知設定機能（帰宅後リマインド通知機能）を制御します。
 *
 * <p>
 * 運動ToDo一覧画面（/exercise）に埋め込まれた設定フォームからの
 * リクエストを受け付けます。
 *
 * @author 情報太郎
 */
@Controller
public class UserSettingController {

  /** ログインチェック用サービス */
  @Autowired
  private LoginService loginService;

  /** 通知設定機能の業務ロジッククラス */
  @Autowired
  private UserSettingService userSettingService;

  /**
   * 通知設定を更新し、運動ToDo一覧画面へ戻ります。
   *
   * @param notifyTime    通知時刻（HH:mm）(null不可)
   * @param notifyEnabled 通知ON/OFF（チェックボックス未選択時は送信されないためOptional相当）
   * @param model         Viewに値を渡すオブジェクト(null不可)
   * @return 運動ToDo一覧画面へリダイレクト
   */
  @PostMapping("/settings/notify")
  public String updateNotifySetting(
      @RequestParam(name = "notifyTime") String notifyTime,
      @RequestParam(name = "notifyEnabled", required = false, defaultValue = "false") boolean notifyEnabled,
      Model model) {
    if (!loginService.isLogin())
      return "login";

    if (!userSettingService.validate(notifyTime)) {
      return "redirect:/exercise";
    }

    userSettingService.save(loginService.getLoginUserId(), notifyTime, notifyEnabled);

    return "redirect:/exercise";
  }
}
