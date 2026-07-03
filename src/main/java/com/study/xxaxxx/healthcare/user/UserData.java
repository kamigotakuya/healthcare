package com.study.xxaxxx.healthcare.user;

/**
 * 1件分のユーザ情報です。
 *
 * <p>各項目のデータ構造については、データベース定義をご覧ください。
 * @author 情報太郎
 */
public class UserData {

  /**
   * ユーザID（メールアドレス）
   * 主キー、必須入力、メールアドレス形式
   */
  private String userId;

  /**
   * パスワード
   * 必須入力、長さ4から100桁まで、半角英数字のみ
   */
  private String password;

  /**
   * アカウント有効性
   * - 有効 : true
   * - 無効 : false
   * ユーザ作成時はtrue固定
   */
  private boolean enabled;

  /**
   * ユーザ名
   * 必須入力
   */
  private String userName;

  /**
   * 権限
   * - 管理 : "ROLE_ADMIN"
   * - 上位： "ROLE_TOP"
   * - 一般 : "ROLE_GENERAL"
   * 必須入力
   */
  private String role;

  public UserData() {}

  public UserData(String userId, String password, String userName, String role, boolean enabled) {
    this.userId = userId;
    this.password = password;
    this.userName = userName;
    this.role = role;
    this.enabled = enabled;
  }

  public String getUserId() {
    return userId;
  }

  public void setUserId(String userId) {
    this.userId = userId;
  }

  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  public boolean isEnabled() {
    return enabled;
  }

  public void setEnabled(boolean enabled) {
    this.enabled = enabled;
  }

  public String getUserName() {
    return userName;
  }

  public void setUserName(String userName) {
    this.userName = userName;
  }

  public String getRole() {
    return role;
  }

  public void setRole(String role) {
    this.role = role;
  }
}
