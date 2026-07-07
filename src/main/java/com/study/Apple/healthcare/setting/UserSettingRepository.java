package com.study.Apple.healthcare.setting;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

/**
 * 通知設定（帰宅後リマインド通知機能）に関わるDBアクセスを実現するクラスです。
 *
 * @author 情報太郎
 */
@Repository
public class UserSettingRepository {

  /** SQL 設定取得 */
  private static final String SQL_SELECT_ONE = "SELECT * FROM user_setting_t WHERE user_id = :userId";

  /** SQL 新規作成（初回設定時） */
  private static final String SQL_INSERT_ONE = "INSERT INTO user_setting_t(user_id, notify_time, notify_enabled) VALUES(:userId, :notifyTime, :notifyEnabled)";

  /** SQL 更新（2回目以降の設定変更時） */
  private static final String SQL_UPDATE_ONE = "UPDATE user_setting_t SET notify_time = :notifyTime, notify_enabled = :notifyEnabled WHERE user_id = :userId";

  /** 予想更新件数(ハードコーディング防止用) */
  private static final int EXPECTED_UPDATE_COUNT = 1;

  @Autowired
  private NamedParameterJdbcTemplate jdbc;

  /**
   * 指定されたユーザーIDの通知設定を取得します。
   *
   * @param userId ユーザーID
   * @return 検索結果のリスト（0件または1件）
   */
  public List<Map<String, Object>> find(String userId) {
    Map<String, Object> params = new HashMap<String, Object>();
    params.put("userId", userId);

    return jdbc.queryForList(SQL_SELECT_ONE, params);
  }

  /**
   * 通知設定を保存します。既に設定が存在する場合は更新、存在しない場合は新規作成します。
   *
   * @param settingData 保存する通知設定データ
   * @return 更新された行数
   * @throws SQLException 更新に失敗した場合にスローされる例外
   */
  public int save(UserSettingData settingData) throws SQLException {
    Map<String, Object> params = new HashMap<String, Object>();
    params.put("userId", settingData.getUserId());
    params.put("notifyTime", settingData.getNotifyTime());
    params.put("notifyEnabled", settingData.isNotifyEnabled());

    List<Map<String, Object>> existing = find(settingData.getUserId());
    int updateRow;
    if (existing.isEmpty()) {
      updateRow = jdbc.update(SQL_INSERT_ONE, params);
    } else {
      updateRow = jdbc.update(SQL_UPDATE_ONE, params);
    }

    if (updateRow != EXPECTED_UPDATE_COUNT) {
      throw new SQLException("更新に失敗しました 件数:" + updateRow);
    }
    return updateRow;
  }
}
