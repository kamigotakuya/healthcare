package com.study.Apple.healthcare.setting;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 通知設定機能（帰宅後リマインド通知機能）の業務ロジックを処理します。
 *
 * <p>
 * 実際のプッシュ通知はブラウザのNotification APIをクライアント側で利用して実現します。
 * 本クラスは「何時に通知してほしいか」というユーザ設定の保存・取得のみを担当します。
 *
 * @author 情報太郎
 */
@Transactional
@Service
public class UserSettingService {

  /** デフォルトの通知時刻（初回アクセス時） */
  private static final String DEFAULT_NOTIFY_TIME = "19:00";

  @Autowired
  private UserSettingRepository userSettingRepository;

  /**
   * ユーザの通知設定を取得します。未設定の場合はデフォルト値を返却します。
   *
   * @param userId ユーザID(null不可)
   * @return 通知設定データ
   */
  public UserSettingData select(String userId) {
    List<Map<String, Object>> resultList = userSettingRepository.find(userId);

    UserSettingData data = new UserSettingData();
    data.setUserId(userId);

    if (resultList.isEmpty()) {
      data.setNotifyTime(DEFAULT_NOTIFY_TIME);
      data.setNotifyEnabled(false);
      return data;
    }

    Map<String, Object> row = resultList.get(0);
    data.setNotifyTime((String) row.get("notify_time"));
    data.setNotifyEnabled((boolean) row.get("notify_enabled"));
    return data;
  }

  /**
   * ユーザの通知設定を保存します。
   *
   * @param userId        ユーザID(null不可)
   * @param notifyTime    通知時刻（HH:mm）(null不可)
   * @param notifyEnabled 通知ON/OFF
   * @return 成功可否
   */
  public boolean save(String userId, String notifyTime, boolean notifyEnabled) {
    UserSettingData data = new UserSettingData();
    data.setUserId(userId);
    data.setNotifyTime(notifyTime);
    data.setNotifyEnabled(notifyEnabled);

    try {
      userSettingRepository.save(data);
    } catch (SQLException e) {
      return false;
    }
    return true;
  }

  /**
   * 通知時刻の形式チェックを行います（HH:mm）。
   *
   * @param notifyTime 通知時刻
   * @return チェックOKの場合true
   */
  public boolean validate(String notifyTime) {
    if (notifyTime == null || notifyTime.isBlank()) {
      return false;
    }
    return notifyTime.matches("^([01]\\d|2[0-3]):[0-5]\\d$");
  }
}
