package com.study.Apple.healthcare.body;

import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 体重・BMI推移記録機能の業務ロジックを処理します。
 *
 * <p>
 * 本クラスは以下の処理を主に行います。
 * <ul>
 * <li>データの相互変換</li>
 * <li>BMIの算出・判定</li>
 * <li>例外処理</li>
 * <li>Repositoryクラスへの問い合わせ</li>
 * <li>Controllerクラスへのリターン</li>
 * </ul>
 *
 * @author 情報太郎
 */
@Transactional
@Service
public class BodyRecordService {

  /** 低体重の判定しきい値（この値未満は「低体重（やせ型）」） */
  private static final double UNDERWEIGHT_THRESHOLD = 18.5;

  /** 肥満の判定しきい値（この値以上は「肥満」） */
  private static final double OBESITY_THRESHOLD = 25.0;

  @Autowired
  private BodyRecordRepository bodyRecordRepository;

  /**
   * ユーザIDに合致する体重・BMI記録一覧（記録日昇順）を取得します。
   *
   * <p>
   * あわせて、最新の記録・初回記録からの体重変化量も算出します。<br>
   * DBエラーが発生した場合は、空の記録一覧を設定して呼び出し元へ返却します。
   *
   * @param userId ユーザID(null不可)
   * @return 記録一覧・サマリ情報
   */
  public BodyRecordEntity selectAll(String userId) {
    List<Map<String, Object>> resultSet = bodyRecordRepository.findAll(userId);
    BodyRecordEntity entity = mappingSelectResult(resultSet);

    List<BodyRecordData> recordList = entity.getRecordList();
    if (!recordList.isEmpty()) {
      // 記録日昇順のため、末尾が最新記録
      BodyRecordData latest = recordList.get(recordList.size() - 1);
      entity.setLatestRecord(latest);

      if (recordList.size() >= 2) {
        BodyRecordData first = recordList.get(0);
        double change = latest.getWeightKg() - first.getWeightKg();
        entity.setWeightChange(Math.round(change * 10) / 10.0);
      }
    }

    entity.setChartDataJson(buildChartDataJson(recordList));

    List<BodyRecordData> recordListDesc = new ArrayList<BodyRecordData>(recordList);
    Collections.reverse(recordListDesc);
    entity.setRecordListDesc(recordListDesc);

    return entity;
  }

  /**
   * 推移グラフ描画用に、記録リストをJSON文字列へ整形します。
   *
   * <p>
   * 値はすべて数値・日付のみのため、エスケープ処理は行っていません。
   *
   * @param recordList 記録日昇順の記録リスト
   * @return [{"date":"MM/dd","weight":00.0,"bmi":00.0}, ...] 形式のJSON文字列
   */
  private String buildChartDataJson(List<BodyRecordData> recordList) {
    SimpleDateFormat format = new SimpleDateFormat("MM/dd");
    StringBuilder json = new StringBuilder("[");

    for (int i = 0; i < recordList.size(); i++) {
      BodyRecordData data = recordList.get(i);
      if (i > 0) {
        json.append(",");
      }
      json.append("{\"date\":\"").append(format.format(data.getRecordDate())).append("\",")
          .append("\"weight\":").append(data.getWeightKg()).append(",")
          .append("\"bmi\":").append(data.getBmi()).append("}");
    }

    json.append("]");
    return json.toString();
  }

  /**
   * 体重・BMI記録を保存します。
   *
   * <p>
   * BMIはサーバー側で算出するため、身長・体重のみ受け取ります。<br>
   * DBエラーが発生した場合は、呼び出し元に失敗の通知を行います。
   *
   * @param userId   ユーザID(null不可)
   * @param height   身長(cm)の文字列(null不可)
   * @param weight   体重(kg)の文字列(null不可)
   * @param recordDay 記録日の文字列(null不可)
   * @return 成功可否
   */
  public boolean insert(String userId, String height, String weight, String recordDay) {
    BodyRecordData data = refillToData(userId, height, weight, recordDay);

    try {
      bodyRecordRepository.save(data);
    } catch (SQLException e) {
      return false;
    }
    return true;
  }

  /**
   * 体重・BMI記録を削除します。
   *
   * <p>
   * DBエラーが発生した場合は、呼び出し元に失敗の通知を行います。
   *
   * @param id     記録ID
   * @param userId ユーザID（本人の記録のみ削除可能とするため）
   * @return 成功可否
   */
  public boolean delete(String id, String userId) {
    int i = Integer.parseInt(id);
    try {
      bodyRecordRepository.delete(i, userId);
    } catch (SQLException e) {
      return false;
    }
    return true;
  }

  /**
   * 入力チェックを行います。
   *
   * @param height    身長(cm)の文字列
   * @param weight    体重(kg)の文字列
   * @param recordDay 記録日の文字列(yyyy-MM-dd)
   * @return 入力が正しい場合はtrue
   */
  boolean validate(String height, String weight, String recordDay) {
    // null・必須チェック
    if (height == null || height.isBlank() || weight == null || weight.isBlank()
        || recordDay == null || recordDay.isBlank()) {
      return false;
    }

    // 数値形式チェック
    double heightValue;
    double weightValue;
    try {
      heightValue = Double.parseDouble(height);
      weightValue = Double.parseDouble(weight);
    } catch (NumberFormatException e) {
      return false;
    }

    // 現実的な範囲チェック（身長30〜250cm、体重10〜300kg）
    if (heightValue < 30 || heightValue > 250) {
      return false;
    }
    if (weightValue < 10 || weightValue > 300) {
      return false;
    }

    // 日付形式チェック
    try {
      SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
      format.setLenient(false);
      format.parse(recordDay);
    } catch (ParseException e) {
      return false;
    }

    return true;
  }

  /**
   * IDの入力チェックを行います。
   *
   * @param id ID文字列
   * @return 入力が正しい場合はtrue
   */
  boolean validate(String id) {
    Pattern p = Pattern.compile("^\\d{1,9}$");
    if (id == null || id.isBlank()) {
      return false;
    } else if (!p.matcher(id).find()) {
      return false;
    }
    return true;
  }

  /**
   * BMIを算出します。
   *
   * @param heightCm 身長(cm)
   * @param weightKg 体重(kg)
   * @return BMI値（小数第1位で四捨五入）
   */
  double calculateBmi(double heightCm, double weightKg) {
    double heightM = heightCm / 100.0;
    double bmi = weightKg / (heightM * heightM);
    return Math.round(bmi * 10) / 10.0;
  }

  /**
   * BMI値から判定区分を決定します。
   *
   * @param bmi BMI値
   * @return 判定区分（低体重（やせ型） / 普通体重 / 肥満）
   */
  String judgeCategory(double bmi) {
    if (bmi < UNDERWEIGHT_THRESHOLD) {
      return "低体重（やせ型）";
    } else if (bmi < OBESITY_THRESHOLD) {
      return "普通体重";
    } else {
      return "肥満";
    }
  }

  /**
   * BMI判定区分に対応する画像ファイル名を決定します。
   *
   * @param bmi BMI値
   * @return 画像ファイル名（/img/bmi/配下）
   */
  String judgeImage(double bmi) {
    if (bmi < UNDERWEIGHT_THRESHOLD) {
      return "gari.png";
    } else if (bmi < OBESITY_THRESHOLD) {
      return "normal.png";
    } else {
      return "puni.png";
    }
  }

  private BodyRecordData refillToData(String userId, String height, String weight, String recordDay) {
    BodyRecordData data = new BodyRecordData();
    data.setUserId(userId);

    double heightValue = Double.parseDouble(height);
    double weightValue = Double.parseDouble(weight);
    data.setHeightCm(heightValue);
    data.setWeightKg(weightValue);
    data.setBmi(calculateBmi(heightValue, weightValue));

    try {
      SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
      data.setRecordDate(format.parse(recordDay));
    } catch (ParseException e) {
      // 何もしない（入力チェック済みのため、変換エラーは起こり得ない）
    }

    return data;
  }

  private BodyRecordEntity mappingSelectResult(List<Map<String, Object>> resultList) {
    BodyRecordEntity entity = new BodyRecordEntity();

    for (Map<String, Object> map : resultList) {
      BodyRecordData data = new BodyRecordData();
      data.setId((Integer) map.get("id"));
      data.setUserId((String) map.get("user_id"));
      data.setRecordDate((Date) map.get("record_date"));
      data.setHeightCm(((Number) map.get("height_cm")).doubleValue());
      data.setWeightKg(((Number) map.get("weight_kg")).doubleValue());

      double bmi = ((Number) map.get("bmi")).doubleValue();
      data.setBmi(bmi);
      data.setBmiCategory(judgeCategory(bmi));
      data.setBmiImage(judgeImage(bmi));

      entity.getRecordList().add(data);
    }
    return entity;
  }
}
