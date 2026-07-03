package com.study.xxaxxx.healthcare.user;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

/**
 * ユーザ管理に関わるDBアクセスを実現するクラスです。
 *
 * <p>以下の処理を行います。
 * <ul>
 * <li>全件取得</li>
 * <li>検索</li>
 * <li>追加</li>
 * <li>削除</li>
 * <li>更新</li>
 * </ul>
 * <p>処理が継続できない場合は、呼び出し元へ例外をスローします。<br>
 * <strong>呼び出し元では適切な例外処理を行ってください。</strong>
 *
 * @author 情報太郎
 */
@Repository
public class UserRepository {

  /** SQL ログインチェック */
  private static final String SQL_LOGIN =
    "SELECT * FROM user_m WHERE user_id = :userId AND password = :password AND enabled = true";

  @Autowired
  private NamedParameterJdbcTemplate jdbc;

  /**
   * 指定されたユーザーIDとパスワードにマッチするユーザーデータを取得するメソッドです。
   * ユーザーIDとパスワードは引数として受け取ります。
   * @param userId 取得するユーザーデータのユーザーID
   * @param password 取得するユーザーデータのパスワード
   * @return 指定されたユーザーIDのユーザーデータのリスト
   */
  public List<Map<String, Object>> login(String userId, String password) {
    // パラメータを格納するためのマップを作成
    Map<String, Object> params = new HashMap<>();
    params.put("userId", userId);
    params.put("password", password);

    // データベースのクエリを実行し、結果を取得
    List<Map<String, Object>> resultList = jdbc.queryForList(SQL_LOGIN, params);

    // 取得したユーザーデータのリストを返す
    return resultList;
  }

}
