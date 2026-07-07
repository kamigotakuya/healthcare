package com.study.xxaxxx.healthcare.user;

import java.util.ArrayList;
import java.util.List;

/**
 * ユーザ情報を管理するクラスです。
 *
 * <p>DBとController間を本クラスでモデル化します。<br>
 * DBからタスク情報が取得できない場合は、リストが空となります。
 * <p><strong>リストにnullは含まれません</Strong>
 */
public class UserEntity {

  /** ユーザ情報のリスト */
  private List<UserData> userlist = new ArrayList<UserData>();

  /** エラーメッセージ(表示用) */
  private String errorMessage;

  public UserEntity() {}

  public UserEntity(List<UserData> userlist, String errorMessage) {
    this.userlist = userlist;
    this.errorMessage = errorMessage;
  }

  public List<UserData> getUserlist() {
    return userlist;
  }

  public void setUserlist(List<UserData> userlist) {
    this.userlist = userlist;
  }

  public String getErrorMessage() {
    return errorMessage;
  }

  public void setErrorMessage(String errorMessage) {
    this.errorMessage = errorMessage;
  }
}
