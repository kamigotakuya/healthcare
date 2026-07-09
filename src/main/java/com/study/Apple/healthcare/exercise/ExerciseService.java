package com.study.Apple.healthcare.exercise;

import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.ZoneId;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 運動ToDo機能の業務ロジックを処理します。
 *
 * <p>
 * 本クラスは以下の機能を提供します。
 * <ul>
 * <li>デイリー運動ToDoリスト機能（1日3件まで、負荷レベル選択）</li>
 * <li>継続記録・カレンダー機能（月間カレンダー・達成率）</li>
 * <li>レベルアップ・進捗グラフ機能（達成回数に応じたレベル算出）</li>
 * </ul>
 *
 * @author 情報太郎
 */
@Transactional
@Service
public class ExerciseService {

  @Autowired
  private ExerciseRepository exerciseRepository;

  /**
   * 指定日のユーザ運動タスク一覧を取得します。
   *
   * @param userId ユーザID(null不可)
   * @param date   対象日（yyyy-MM-dd）
   * @return 運動タスク一覧
   */
  public ExerciseEntity selectByDate(String userId, String date) {
    java.sql.Date sqlDate = java.sql.Date.valueOf(date);
    List<Map<String, Object>> resultSet = exerciseRepository.findByDate(userId, sqlDate);
    return mappingSelectResult(resultSet);
  }

  /**
   * 今日時点のユーザ運動タスク一覧を取得します。
   *
   * @param userId ユーザID(null不可)
   * @return 運動タスク一覧
   */
  public ExerciseEntity selectToday(String userId) {
    return selectByDate(userId, LocalDate.now().toString());
  }

  /**
   * 運動タスクを保存します。1日3件までの上限チェックを行います。
   *
   * @param userId    ユーザID(null不可)
   * @param title     運動内容(null不可)
   * @param loadLevel 負荷レベル(null不可)
   * @param date      実施予定日(null不可)
   * @return 成功可否
   */
  public boolean insert(String userId, String title, String loadLevel, String date) {
    ExerciseEntity existing = selectByDate(userId, date);
    if (existing.isFull()) {
      // 1日3件の上限に達している場合は登録しない
      return false;
    }

    ExerciseData exerciseData = refillToData(userId, title, loadLevel, date);
    try {
      exerciseRepository.save(exerciseData);
    } catch (SQLException e) {
      return false;
    }
    return true;
  }

  /**
   * 運動タスクを削除します。
   *
   * @param id 運動タスクID
   * @return 成功可否
   */
  public boolean delete(String id) {
    int i = Integer.parseInt(id);
    try {
      exerciseRepository.delete(i);
    } catch (SQLException e) {
      return false;
    }
    return true;
  }

  /**
   * 運動タスクを完了状態にします。
   *
   * @param id 運動タスクID
   * @return 成功可否
   */
  public boolean complete(String id) {
    int i = Integer.parseInt(id);
    try {
      exerciseRepository.complete(i);
    } catch (SQLException e) {
      return false;
    }
    return true;
  }

  /**
   * 入力チェックを行います（件名・負荷レベル・日付）。
   *
   * @param title     運動内容
   * @param loadLevel 負荷レベル
   * @param date      実施予定日
   * @return チェックOKの場合true
   */
  boolean validate(String title, String loadLevel, String date) {
    if (title == null || title.isBlank() || title.length() > 50) {
      return false;
    }

    if (loadLevel == null || loadLevel.isBlank()) {
      return false;
    }
    boolean isValidLevel = false;
    for (LoadLevel level : LoadLevel.values()) {
      if (level.name().equalsIgnoreCase(loadLevel)) {
        isValidLevel = true;
      }
    }
    if (!isValidLevel) {
      return false;
    }

    if (date == null || date.isBlank()) {
      return false;
    }
    try {
      SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
      format.setLenient(false);
      format.parse(date);
    } catch (ParseException e) {
      return false;
    }

    return true;
  }

  /**
   * IDの形式チェックを行います。
   *
   * @param id 運動タスクID
   * @return チェックOKの場合true
   */
  boolean validate(String id) {
    if (id == null || id.isBlank()) {
      return false;
    }
    return id.matches("^\\d{1,9}$");
  }

  /**
   * カレンダー・達成率・レベルアップ用の進捗情報を算出します。
   *
   * <p>
   * 継続記録・カレンダー機能、レベルアップ・進捗グラフ機能に対応します。
   *
   * @param userId ユーザID(null不可)
   * @return 進捗情報
   */
public Progressinfo calculateProgress(String userId) {
    List<Map<String, Object>> resultSet = exerciseRepository.findAll(userId);

    Set<LocalDate> completedDates = new HashSet<LocalDate>();
    int totalCompletedCount = 0;

    for (Map<String, Object> row : resultSet) {
      boolean complete = (boolean) row.get("complete");
      if (complete) {
        totalCompletedCount++;
        java.sql.Date exerciseDate = (java.sql.Date) row.get("exercise_date");
        completedDates.add(exerciseDate.toLocalDate());
      }
    }

    Progressinfo progress = new Progressinfo();
    progress.setTotalCompletedCount(totalCompletedCount);

    // レベル算出（EXP_PER_LEVEL件達成するごとに1レベルアップ）
    int level = (totalCompletedCount / Progressinfo.EXP_PER_LEVEL) + 1;
    int currentExp = totalCompletedCount % Progressinfo.EXP_PER_LEVEL;
    progress.setLevel(level);
    progress.setCurrentExp(currentExp);
    progress.setNextLevelExp(Progressinfo.EXP_PER_LEVEL);
    progress.setProgressPercent(currentExp * 100 / Progressinfo.EXP_PER_LEVEL);

    // 当月カレンダー・達成率の算出
    LocalDate today = LocalDate.now(ZoneId.systemDefault());
    YearMonth thisMonth = YearMonth.from(today);
    int lastDayToCount = today.getDayOfMonth();
    int achievedDaysThisMonth = 0;

    for (int day = 1; day <= thisMonth.lengthOfMonth(); day++) {
      LocalDate target = thisMonth.atDay(day);
      boolean achieved = completedDates.contains(target);
      progress.getCalendar().put(day, achieved);
      if (achieved && day <= lastDayToCount) {
        achievedDaysThisMonth++;
      }
    }
    progress.setMonthlyAchievementRate(achievedDaysThisMonth * 100 / lastDayToCount);

    // 一般的なカレンダーのように、1日を正しい曜日の位置に表示するための空白セル数を算出する。
    // DayOfWeek#getValue() は月曜=1〜日曜=7を返すため、日曜始まり(0〜6)に変換する。
    int firstDayOfWeekValue = thisMonth.atDay(1).getDayOfWeek().getValue() % 7;
    progress.setLeadingEmptyDays(firstDayOfWeekValue);

    // 最終週を7列で揃えるための末尾の空白セル数を算出する。
    int totalCells = firstDayOfWeekValue + thisMonth.lengthOfMonth();
    int trailingEmptyDays = (7 - (totalCells % 7)) % 7;
    progress.setTrailingEmptyDays(trailingEmptyDays);

    return progress;
  }

  private ExerciseData refillToData(String userId, String title, String loadLevel, String date) {
    ExerciseData exerciseData = new ExerciseData();
    exerciseData.setUserId(userId);
    exerciseData.setTitle(title);
    exerciseData.setLoadLevel(LoadLevel.fromString(loadLevel));
    exerciseData.setComplate(false);

    try {
      SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
      exerciseData.setExerciseDate(format.parse(date));
    } catch (ParseException e) {
      // 何もしない（入力チェック済みのため、変換エラーは起こり得ない）
    }

    return exerciseData;
  }

  private ExerciseEntity mappingSelectResult(List<Map<String, Object>> resultList) {
    ExerciseEntity entity = new ExerciseEntity();

    for (Map<String, Object> map : resultList) {
      ExerciseData data = new ExerciseData();
      data.setId((Integer) map.get("id"));
      data.setUserId((String) map.get("user_id"));
      data.setTitle((String) map.get("title"));
      data.setLoadLevel(LoadLevel.fromString((String) map.get("load_level")));
      data.setExerciseDate((Date) map.get("exercise_date"));
      data.setComplate((boolean) map.get("complete"));

      entity.getExerciseList().add(data);
    }
    return entity;
  }
}
