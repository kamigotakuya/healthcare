package com.study.xxaxxx.healthcare.task;

import java.util.ArrayList;
import java.util.List;

/**
 * 複数件のタスク情報を保持します。
 *
 * <p>DBとController間を本クラスでモデル化します。<br>
 * DBからタスク情報が取得できない場合は、リストが空となります。
 * <p><strong>リストにnullは含まれません</strong>
 *
 * @author 情報太郎
 */
public class TaskEntity {

  /** タスク情報のリスト */
  private List<TaskData> taskList = new ArrayList<TaskData>();

  /** エラーメッセージ(表示用) */
  private String errorMessage;

  public List<TaskData> getTaskList() {
    return taskList;
  }

  public void setTaskList(List<TaskData> taskList) {
    this.taskList = taskList;
  }

  public String getErrorMessage() {
    return errorMessage;
  }

  public void setErrorMessage(String errorMessage) {
    this.errorMessage = errorMessage;
  }
}
