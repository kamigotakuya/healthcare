package com.study.xxaxxx.healthcare;

import com.study.xxaxxx.healthcare.user.LoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * ポータル画面を制御する
 */
@Controller
public class PortalController {

  /** ログインチェック用サービス */
  @Autowired
  private LoginService loginService;

  /**
   * ポータル画面を表示する
   * @return ポータル画面
   */
  @GetMapping("/")
  public String index() {
    // ログインチェック
    if (!loginService.isLogin()) return "login";

    return "index";
  }
}
