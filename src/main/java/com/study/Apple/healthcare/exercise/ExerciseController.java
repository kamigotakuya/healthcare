package com.study.Apple.healthcare.exercise;

import com.study.Apple.healthcare.setting.UserSettingData;
import com.study.Apple.healthcare.setting.UserSettingService;
import com.study.Apple.healthcare.user.LoginService;
import java.time.LocalDate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * 運動ToDo管理機能を表す。
 *
 * <p>
 * 提案スライドの「デイリー運動ToDoリスト機能」「継続記録・カレンダー機能」
 * 「レベルアップ・進捗グラフ機能」に対応する画面遷移を制御します。
 *
 * <p>
 * 入力チェックを実施し、正しいデータ項目のみ処理します。
 * クライアント側でも入力チェックを実施することを推奨します。
 *
 * @author 情報太郎
 */
@Controller
public class ExerciseController {

  /** ログインチェック用サービス */
  @Autowired
  private LoginService loginService;

  /** 運動ToDo機能の業務ロジッククラス */
  @Autowired
  private ExerciseService exerciseService;

  /** 通知設定機能の業務ロジッククラス */
  @Autowired
  private UserSettingService userSettingService;

  /**
   * ログイン中のユーザに紐づく、今日の運動ToDo一覧画面を表示します。
   *
   * @param model Viewに値を渡すオブジェクト(null不可)
   * @return 運動ToDo一覧画面へのパス(null不可)
   */
  @GetMapping("/exercise")
  public String getExerciseList(Model model) {
    if (!loginService.isLogin())
      return "login";

    String userId = loginService.getLoginUserId();
    ExerciseEntity exerciseEntity = exerciseService.selectToday(userId);
    Progressinfo progress = exerciseService.calculateProgress(userId);
    UserSettingData notifySetting = userSettingService.select(userId);

    model.addAttribute("exerciseEntity", exerciseEntity);
    model.addAttribute("progress", progress);
    model.addAttribute("today", LocalDate.now().toString());
    model.addAttribute("loadLevels", LoadLevel.values());
    model.addAttribute("notifySetting", notifySetting);

    return "exercise/list";
  }

  /**
   * 入力された運動タスクをDBへ登録します。1日3件までの上限があります。
   *
   * @param title     運動内容(null不可)
   * @param loadLevel 負荷レベル(null不可)
   * @param date      実施予定日(null不可)
   * @param model     Viewに値を渡すオブジェクト(null不可)
   * @return 運動ToDo一覧画面へのパス(null不可)
   */
  @PostMapping("/exercise/insert")
  public String insertExercise(
      @RequestParam(name = "title") String title,
      @RequestParam(name = "loadLevel") String loadLevel,
      @RequestParam(name = "date") String date,
      Model model) {
    if (!loginService.isLogin())
      return "login";

    boolean isValid = exerciseService.validate(title, loadLevel, date);
    if (!isValid) {
      model.addAttribute("errorMessage", "入力項目に不備があります");
      return getExerciseList(model);
    }

    boolean isSuccess = exerciseService.insert(loginService.getLoginUserId(), title, loadLevel, date);
    if (isSuccess) {
      model.addAttribute("message", "正常に登録されました");
    } else {
      model.addAttribute("errorMessage", "1日に登録できる運動は3件までです。もしくは登録に失敗しました");
    }

    return getExerciseList(model);
  }

  /**
   * 指定された運動タスクIDをDBから削除します。
   *
   * @param id    運動タスクIDの文字列を格納(null不可)
   * @param model Viewに値を渡すオブジェクト(null不可)
   * @return 運動ToDo一覧画面へのパス(null不可)
   */
  @PostMapping("/exercise/delete")
  public String deleteExercise(@RequestParam(name = "id") String id, Model model) {
    if (!loginService.isLogin())
      return "login";

    boolean isValid = exerciseService.validate(id);
    if (!isValid) {
      return "index";
    }

    boolean isSuccess = exerciseService.delete(id);
    if (isSuccess) {
      model.addAttribute("message", "正常に削除されました");
    } else {
      model.addAttribute("errorMessage", "削除できませんでした。再度お試しください");
    }
    return getExerciseList(model);
  }

  /**
   * 指定された運動タスクIDの状態を完了に変更します。
   *
   * @param id    運動タスクIDの文字列を格納(null不可)
   * @param model Viewに値を渡すオブジェクト(null不可)
   * @return 運動ToDo一覧画面へのパス(null不可)
   */
  @PostMapping("/exercise/complete")
  public String completeExercise(@RequestParam(name = "id") String id, Model model) {
    if (!loginService.isLogin())
      return "login";

    boolean isValid = exerciseService.validate(id);
    if (!isValid) {
      return "index";
    }

    boolean isSuccess = exerciseService.complete(id);
    if (isSuccess) {
      model.addAttribute("message", "よくできました！次のレベルまであと少しです");
    } else {
      model.addAttribute("errorMessage", "更新できませんでした。再度お試しください");
    }
    return getExerciseList(model);
  }

  /**
   * 継続記録・カレンダー機能の画面を表示します。
   *
   * @param model Viewに値を渡すオブジェクト(null不可)
   * @return カレンダー画面へのパス(null不可)
   */
  @GetMapping("/exercise/calendar")
  public String getCalendar(Model model) {
    if (!loginService.isLogin())
      return "login";

    String userId = loginService.getLoginUserId();
    Progressinfo progress = exerciseService.calculateProgress(userId);
    model.addAttribute("progress", progress);
    model.addAttribute("yearMonth", java.time.YearMonth.now().toString());

    return "exercise/calendar";
  }
}
