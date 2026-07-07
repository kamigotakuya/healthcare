/* 開発用にデータ削除を追加 : リリース時は消す
DROP TABLE task_t;
DROP TABLE user_m;
 */
/* ユーザマスタ */
CREATE TABLE IF NOT EXISTS
  user_m (
    user_id VARCHAR(50) PRIMARY KEY,
    PASSWORD VARCHAR(100),
    user_name VARCHAR(50),
    ROLE VARCHAR(50),
    enabled BOOLEAN
  );

/* タスクテーブル */
CREATE TABLE IF NOT EXISTS
  task_t (
    id INT PRIMARY KEY,
    user_id VARCHAR(50),
    title VARCHAR(50),
    limitday DATE,
    complete BOOLEAN
  );

/* 運動ToDoテーブル（デイリー運動ToDoリスト機能） */
CREATE TABLE IF NOT EXISTS
  exercise_t (
    id INT PRIMARY KEY,
    user_id VARCHAR(50),
    title VARCHAR(50),
    load_level VARCHAR(10), /* LIGHT, NORMAL, HARD */
    exercise_date DATE,
    complete BOOLEAN
  );

/* 通知設定テーブル（帰宅後リマインド通知機能） */
CREATE TABLE IF NOT EXISTS
  user_setting_t (
    user_id VARCHAR(50) PRIMARY KEY,
    notify_time VARCHAR(5), /* HH:mm形式 */
    notify_enabled BOOLEAN
  );
