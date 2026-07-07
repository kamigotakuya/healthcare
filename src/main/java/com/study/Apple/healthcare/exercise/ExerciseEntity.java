package com.study.Apple.healthcare.exercise;

import java.util.ArrayList;
import java.util.List;

/**
 * 複数件の運動タスク情報を保持します。
 *
 * <p>
 * DBとController間を本クラスでモデル化します。<br>
 * DBから運動タスク情報が取得できない場合は、リストが空となります。
 * <p>
 * <strong>リストにnullは含まれません</strong>
 *
 * @author 情報太郎
 */
public class ExerciseEntity {

  /** 1日あたりの運動タスク登録上限（項目のコンセプト準拠） */
  public static final int DAILY_TASK_LIMIT = 3;

  /** その日の運動タスク一覧（最大3件） */
  private List<ExerciseData> exerciseList = new ArrayList<ExerciseData>();

  /** エラーメッセージ(表示用) */
  private String errorMessage;

  public List<ExerciseData> getExerciseList() {
    return exerciseList;
  }

  public void setExerciseList(List<ExerciseData> exerciseList) {
    this.exerciseList = exerciseList;
  }

  public String getErrorMessage() {
    return errorMessage;
  }

  public void setErrorMessage(String errorMessage) {
    this.errorMessage = errorMessage;
  }

  /**
   * その日の運動タスクがすでに上限（3件）に達しているかを判定します。
   *
   * @return 上限に達している場合はtrue
   */
  public boolean isFull() {
    return exerciseList.size() >= DAILY_TASK_LIMIT;
  }
}
