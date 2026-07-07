package com.study.Apple.healthcare.exercise;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

/**
 * 運動ToDo管理（デイリー運動ToDoリスト機能・継続記録機能）に関わるDBアクセスを実現するクラスです。
 *
 * <p>
 * 以下の処理を行います。
 * <ul>
 * <li>指定日の検索</li>
 * <li>ユーザ全件の検索（カレンダー表示・達成率算出用）</li>
 * <li>追加</li>
 * <li>削除</li>
 * <li>更新</li>
 * </ul>
 * <p>
 * 処理が継続できない場合は、呼び出し元へ例外をスローします。<br>
 * <strong>呼び出し元では適切な例外処理を行ってください。</strong>
 *
 * @author 情報太郎
 */
@Repository
public class ExerciseRepository {

  /** SQL 指定日の運動タスクを取得 */
  private static final String SQL_SELECT_BY_DATE = "SELECT * FROM exercise_t WHERE user_id = :userId AND exercise_date = :exerciseDate ORDER BY id";

  /** SQL ユーザの運動タスクを全件取得（カレンダー・達成率・レベル算出用） */
  private static final String SQL_SELECT_ALL = "SELECT * FROM exercise_t WHERE user_id = :userId ORDER BY exercise_date";

  /** SQL 1件追加 */
  private static final String SQL_INSERT_ONE = "INSERT INTO exercise_t(id, user_id, title, load_level, exercise_date, complete) VALUES((SELECT COALESCE(MAX(id), 0) + 1 FROM exercise_t), :userId, :title, :loadLevel, :exerciseDate, false)";

  /** SQL 1件削除 */
  private static final String SQL_DELETE_ONE = "DELETE FROM exercise_t WHERE id = :id";

  /** SQL 1件更新（完了） */
  private static final String SQL_UPDATE_ONE = "UPDATE exercise_t SET complete = true WHERE id = :id";

  /** 予想更新件数(ハードコーディング防止用) */
  private static final int EXPECTED_UPDATE_COUNT = 1;

  @Autowired
  private NamedParameterJdbcTemplate jdbc;

  /**
   * 指定されたユーザーIDと日付に合致する運動タスクを検索します。
   *
   * @param userId       ユーザーID
   * @param exerciseDate 実施予定日（yyyy-MM-dd）
   * @return 検索結果のリスト（マップのリスト）
   */
  public List<Map<String, Object>> findByDate(String userId, java.sql.Date exerciseDate) {
    Map<String, Object> params = new HashMap<String, Object>();
    params.put("userId", userId);
    params.put("exerciseDate", exerciseDate);

    return jdbc.queryForList(SQL_SELECT_BY_DATE, params);
  }

  /**
   * 指定されたユーザーIDに関連するすべての運動タスクを検索します。
   * カレンダー表示、達成率、レベル算出に利用します。
   *
   * @param userId ユーザーID
   * @return 検索結果のリスト（マップのリスト）
   */
  public List<Map<String, Object>> findAll(String userId) {
    Map<String, Object> params = new HashMap<String, Object>();
    params.put("userId", userId);

    return jdbc.queryForList(SQL_SELECT_ALL, params);
  }

  /**
   * 運動タスクデータを保存します。
   *
   * @param exerciseData 保存する運動タスクデータ
   * @return 更新された行数
   * @throws SQLException 更新に失敗した場合にスローされる例外
   */
  public int save(ExerciseData exerciseData) throws SQLException {
    Map<String, Object> params = new HashMap<String, Object>();
    params.put("userId", exerciseData.getUserId());
    params.put("title", exerciseData.getTitle());
    params.put("loadLevel", exerciseData.getLoadLevel().name());
    params.put("exerciseDate", exerciseData.getExerciseDate());

    int updateRow = jdbc.update(SQL_INSERT_ONE, params);
    if (updateRow != EXPECTED_UPDATE_COUNT) {
      throw new SQLException("更新に失敗しました 件数:" + updateRow);
    }
    return updateRow;
  }

  /**
   * 指定されたIDの運動タスクを削除します。
   *
   * @param id 削除するデータのID
   * @return 更新された行数
   * @throws SQLException 更新に失敗した場合にスローされる例外
   */
  public int delete(int id) throws SQLException {
    Map<String, Object> params = new HashMap<String, Object>();
    params.put("id", id);

    int updateRow = jdbc.update(SQL_DELETE_ONE, params);
    if (updateRow != EXPECTED_UPDATE_COUNT) {
      throw new SQLException("更新に失敗しました 件数:" + updateRow);
    }
    return updateRow;
  }

  /**
   * 指定されたIDの運動タスクを完了状態に更新します。
   *
   * @param id 更新するデータのID
   * @return 更新された行数
   * @throws SQLException 更新に失敗した場合にスローされる例外
   */
  public int complete(int id) throws SQLException {
    Map<String, Object> params = new HashMap<String, Object>();
    params.put("id", id);

    int updateRow = jdbc.update(SQL_UPDATE_ONE, params);
    if (updateRow != EXPECTED_UPDATE_COUNT) {
      throw new SQLException("更新に失敗しました 件数:" + updateRow);
    }
    return updateRow;
  }
}
