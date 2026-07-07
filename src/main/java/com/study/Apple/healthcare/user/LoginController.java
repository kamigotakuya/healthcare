package com.study.xxaxxx.healthcare.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * ログインに関する機能・画面を制御する
 */
@Controller
public class LoginController {

  /** ログインチェック用サービス */
  @Autowired
  private LoginService loginService;

  /**
   * ログイン画面を表示する
   * @return ログイン画面
   */
  @GetMapping("/login")
  public String getLogin() {
    return "login";
  }

  /**
   * ログイン処理を行う
   * @param userId ユーザID
   * @param password パスワード
   * @param model モデルオブジェクト
   * @return ログイン成功時はトップページ、失敗時はログイン画面
   */
  @PostMapping("/login")
  public String login(
    @RequestParam(name = "user_id") String userId,
    @RequestParam(name = "password") String password,
    Model model
  ) {
    // ログイン処理
    if (!loginService.login(userId, password)) {
      model.addAttribute("errorMessage", "ユーザIDまたはパスワードが違います。");
      return "login";
    }

    return "redirect:/";
  }

  /**
   * ログアウト処理を行う
   * @return ログイン画面
   */
  @GetMapping("/logout")
  public String logout() {
    // ログアウト処理
    loginService.logout();

    return "login";
  }
}
