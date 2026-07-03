package com.study.xxaxxx.healthcare.task;

import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * タスク機能の業務ロジックを処理します。
 *
 * <p>
 * 本クラスは以下の処理を主に行います。
 * <ul>
 * <li>データの相互変換</li>
 * <li>例外処理</li>
 * <li>Repositoryクラスへの問い合わせ</li>
 * <li>Controllerクラスへのリターン</li>
 * </ul>
 *
 * @author 情報太郎
 */
@Transactional
@Service
public class TaskService {

  @Autowired
  private TaskRepository taskRepository;

  /**
   * ユーザIDに合致するタスク一覧を取得します。
   *
   * <p>
   * DBエラーが発生した場合は、空のタスク一覧を設定して呼び出し元へ返却します。
   *
   * @param userId ユーザID(null不可)
   * @return タスク一覧
   */
  public TaskEntity selectAll(String userId) {
    List<Map<String, Object>> resultSet;
    resultSet = taskRepository.findAll(userId);
    TaskEntity taskEntity = mappingSelectResult(resultSet);
    return taskEntity;
  }

  /**
   * タスクを保存します。
   *
   * <p>
   * DBエラーが発生した場合は、呼び出し元に失敗の通知を行います。
   *
   * @param userId   ユーザID(null不可)
   * @param title    タイトル(null不可)
   * @param limitday 期限日(null不可)
   * @return 成功可否
   */
  public boolean insert(String userId, String title, String limitday) {
    // TaskData型へ詰め替える
    TaskData taskData = refillToData(userId, title, limitday);

    try {
      taskRepository.save(taskData);
    } catch (SQLException e) {
      return false;
    }
    return true;
  }

  /**
   * タスクを削除します。
   *
   * <p>
   * DBエラーが発生した場合は、呼び出し元に失敗の通知を行います。
   *
   * @param id タスクID
   * @return 成功可否
   */
  public boolean delete(String id) {
    int i = Integer.parseInt(id);
    try {
      taskRepository.delete(i);
    } catch (SQLException e) {
      return false;
    }
    return true;
  }

  /**
   * タスクを完了状態にします。
   *
   * <p>
   * DBエラーが発生した場合は、呼び出し元に失敗の通知を行います。
   *
   * @param id タスクID
   * @return 成功可否
   */
  public boolean complete(String id) {
    int i = Integer.parseInt(id);
    try {
      taskRepository.update(i);
    } catch (SQLException e) {
      return false;
    }
    return true;
  }
  boolean validate(String comment, String limitday) {
    // nullチェック、必須チェック、50文字超過チェック
    if (comment == null || comment.isBlank() || comment.length() > 50) {
      return false;
    }

    // nullチェック、必須チェック
    if (limitday == null || limitday.isBlank()) {
      return false;
    }

    // 日付形式チェック(SimpleDateFomatの変換可否を利用)
    try {
      SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
      format.parse(limitday);
    } catch (ParseException e) {
      return false;
    }

    return true;
  }

  boolean validate(String id) {
    // 数値チェック(1桁～Intの最大値)
    Pattern p = Pattern.compile("^\\d{1,9}$");
    if (id.isBlank()) {
      return false;
    } else if (!p.matcher(id).find()) {
      return false;
    }
    return true;
  }


  private TaskData refillToData(String userId, String title, String limitDay) {
    TaskData taskData = new TaskData();
    taskData.setUserId(userId);
    taskData.setTitle(title);
    taskData.setComplate(false);

    // String型からDate型へ変換する
    try {
      SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
      taskData.setLimitday(format.parse(limitDay));
    } catch (ParseException e) {
      // 何もしない（入力チェック済みのため、変換エラーは起こり得ない）
    }

    return taskData;
  }

  private TaskEntity mappingSelectResult(List<Map<String, Object>> resultList) {
    TaskEntity entity = new TaskEntity();

    for (Map<String, Object> map : resultList) {
      TaskData data = new TaskData();
      data.setId((Integer) map.get("id"));
      data.setUserId((String) map.get("user_id"));
      data.setTitle((String) map.get("title"));
      data.setLimitday((Date) map.get("limitday"));
      data.setComplate((boolean) map.get("complete"));

      entity.getTaskList().add(data);
    }
    return entity;
  }
}
