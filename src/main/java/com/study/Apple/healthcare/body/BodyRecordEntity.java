package com.study.Apple.healthcare.body;

import java.util.ArrayList;
import java.util.List;

/**
 * 複数件の体重・BMI記録情報を保持します。
 *
 * <p>
 * DBとController間を本クラスでモデル化します。<br>
 * DBから記録情報が取得できない場合は、リストが空となります。
 * <p>
 * <strong>リストにnullは含まれません</strong>
 *
 * @author 情報太郎
 */
public class BodyRecordEntity {

  /** 記録日の古い順に並んだ、体重・BMI記録のリスト（推移グラフ表示用） */
  private List<BodyRecordData> recordList = new ArrayList<BodyRecordData>();

  /** 記録日の新しい順に並んだ、体重・BMI記録のリスト（一覧表示用） */
  private List<BodyRecordData> recordListDesc = new ArrayList<BodyRecordData>();

  /** もっとも新しい記録（一覧の先頭に表示するため） */
  private BodyRecordData latestRecord;

  /** 直近の体重変化量(kg)。記録が2件未満の場合はnull（最初の記録と最新記録の差） */
  private Double weightChange;

  /** 推移グラフ描画用に整形したJSON文字列（記録日昇順の [{date, weight, bmi}, ...] 形式） */
  private String chartDataJson = "[]";

  /** エラーメッセージ(表示用) */
  private String errorMessage;

  public List<BodyRecordData> getRecordList() {
    return recordList;
  }

  public void setRecordList(List<BodyRecordData> recordList) {
    this.recordList = recordList;
  }

  public List<BodyRecordData> getRecordListDesc() {
    return recordListDesc;
  }

  public void setRecordListDesc(List<BodyRecordData> recordListDesc) {
    this.recordListDesc = recordListDesc;
  }

  public BodyRecordData getLatestRecord() {
    return latestRecord;
  }

  public void setLatestRecord(BodyRecordData latestRecord) {
    this.latestRecord = latestRecord;
  }

  public Double getWeightChange() {
    return weightChange;
  }

  public void setWeightChange(Double weightChange) {
    this.weightChange = weightChange;
  }

  public String getChartDataJson() {
    return chartDataJson;
  }

  public void setChartDataJson(String chartDataJson) {
    this.chartDataJson = chartDataJson;
  }

  public String getErrorMessage() {
    return errorMessage;
  }

  public void setErrorMessage(String errorMessage) {
    this.errorMessage = errorMessage;
  }
}
