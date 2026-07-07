/* 開発用にデータ削除を追加 : リリース時は消す */
DELETE FROM user_m;

DELETE FROM task_t;

DELETE FROM exercise_t;

DELETE FROM user_setting_t;

/* ユーザマスタのデータ（ADMIN権限） */
INSERT INTO
  user_m (user_id, PASSWORD, user_name, ROLE, enabled)
VALUES
  (
    'taro@xxx.co.jp',
    'p@ss',
    '情報太郎',
    'ROLE_ADMIN',
    TRUE
  );

/* ユーザマスタのデータ（上位権限） */
INSERT INTO
  user_m (user_id, PASSWORD, user_name, ROLE, enabled)
VALUES
  (
    'hanako@xxx.co.jp',
    'p@ss',
    '情報花子',
    'ROLE_TOP',
    TRUE
  );

/* ユーザマスタのデータ（一般権限） */
INSERT INTO
  user_m (user_id, PASSWORD, user_name, ROLE, enabled)
VALUES
  (
    'goro@xxx.co.jp',
    'p@ss',
    '情報五郎',
    'ROLE_GENERAL',
    TRUE
  );

/* タスクテーブルのデータ */
INSERT INTO
  task_t (id, user_id, title, limitday, complete)
VALUES
  (1, 'admin', 'このレコードは消さないこと', '2022-11-11', TRUE);

INSERT INTO
  task_t (id, user_id, title, limitday, complete)
VALUES
  (
    2,
    'goro@xxx.co.jp',
    '食材を購入するためのリストを作成',
    '2023-06-24',
    FALSE
  );

INSERT INTO
  task_t (id, user_id, title, limitday, complete)
VALUES
  (
    3,
    'hanako@xxx.co.jp',
    '明日の夕食にレストランを予約しているので、確認の電話をかける',
    '2023-07-11',
    FALSE
  );

INSERT INTO
  task_t (id, user_id, title, limitday, complete)
VALUES
  (
    4,
    'taro@xxx.co.jp',
    '近所のスポーツクラブに登録するため、必要な書類と登録費用を準備する',
    '2024-02-27',
    FALSE
  );

/* 運動ToDoのサンプルデータ */
INSERT INTO
  exercise_t (id, user_id, title, load_level, exercise_date, complete)
VALUES
  (1, 'goro@xxx.co.jp', '腕立て伏せ 10回', 'LIGHT', CURRENT_DATE, TRUE);

INSERT INTO
  exercise_t (id, user_id, title, load_level, exercise_date, complete)
VALUES
  (2, 'goro@xxx.co.jp', 'スクワット 20回', 'NORMAL', CURRENT_DATE, FALSE);

INSERT INTO
  exercise_t (id, user_id, title, load_level, exercise_date, complete)
VALUES
  (3, 'goro@xxx.co.jp', 'ストレッチ 5分', 'LIGHT', CURRENT_DATE - 1, TRUE);

/* 通知設定のサンプルデータ */
INSERT INTO
  user_setting_t (user_id, notify_time, notify_enabled)
VALUES
  ('goro@xxx.co.jp', '19:00', TRUE);
