package com.study.Apple.healthcare.body;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

/**
 * 体重・BMI推移記録機能に関わるDBアクセスを実現するクラスです。
 *
 * <p>
 * 以下の処理を行います。
 * <ul>
 * <li>検索</li>
 * <li>追加</li>
 * <li>削除</li>
 * </ul>
 * <p>
 * 処理が継続できない場合は、呼び出し元へ例外をスローします。<br>
 * <strong>呼び出し元では適切な例外処理を行ってください。</strong>
 *
 * @author 情報太郎
 */
@Repository
public class BodyRecordRepository {

  /** SQL 全件取得（記録日昇順：グラフ・一覧を古い順で並べるため） */
  private static final String SQL_SELECT_ALL =
      "SELECT * FROM body_record_t WHERE user_id = :userId ORDER BY record_date";

  /** SQL 1件追加 */
  private static final String SQL_INSERT_ONE =
      "INSERT INTO body_record_t(id, user_id, record_date, height_cm, weight_kg, bmi) "
          + "VALUES((SELECT MAX(id) + 1 FROM body_record_t), :userId, :recordDate, :heightCm, :weightKg, :bmi)";

  /** SQL 1件削除（本人のデータのみ削除できるようuserIdも条件に含める） */
  private static final String SQL_DELETE_ONE =
      "DELETE FROM body_record_t WHERE id = :id AND user_id = :userId";

  /** 予想更新件数(ハードコーディング防止用) */
  private static final int EXPECTED_UPDATE_COUNT = 1;

  @Autowired
  private NamedParameterJdbcTemplate jdbc;

  /**
   * 指定されたユーザーIDに関連するすべての体重・BMI記録を検索します。
   *
   * @param userId ユーザーID
   * @return 検索結果のリスト（マップのリスト）記録日の昇順
   */
  public List<Map<String, Object>> findAll(String userId) {
    Map<String, Object> params = new HashMap<String, Object>();
    params.put("userId", userId);

    List<Map<String, Object>> resultList = jdbc.queryForList(SQL_SELECT_ALL, params);
    return resultList;
  }

  /**
   * 体重・BMI記録を保存します。
   *
   * @param data 保存する記録データ（bmiは事前に算出済みであること）
   * @return 更新された行数
   * @throws SQLException 更新に失敗した場合にスローされる例外
   */
  public int save(BodyRecordData data) throws SQLException {
    Map<String, Object> params = new HashMap<String, Object>();
    params.put("userId", data.getUserId());
    params.put("recordDate", data.getRecordDate());
    params.put("heightCm", data.getHeightCm());
    params.put("weightKg", data.getWeightKg());
    params.put("bmi", data.getBmi());

    int updateRow = jdbc.update(SQL_INSERT_ONE, params);
    if (updateRow != EXPECTED_UPDATE_COUNT) {
      throw new SQLException("更新に失敗しました 件数:" + updateRow);
    }
    return updateRow;
  }

  /**
   * 指定されたIDの記録を削除します。
   *
   * @param id     削除する記録のID
   * @param userId ユーザーID（他人の記録を削除できないようにするための確認用）
   * @return 更新された行数
   * @throws SQLException 更新に失敗した場合にスローされる例外
   */
  public int delete(int id, String userId) throws SQLException {
    Map<String, Object> params = new HashMap<String, Object>();
    params.put("id", id);
    params.put("userId", userId);

    int updateRow = jdbc.update(SQL_DELETE_ONE, params);
    if (updateRow != EXPECTED_UPDATE_COUNT) {
      throw new SQLException("更新に失敗しました 件数:" + updateRow);
    }
    return updateRow;
  }
}
