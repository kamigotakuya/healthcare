package com.study.xxaxxx.healthcare;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

/**
 * システム全体の設定を行うための管理クラスです。
 *
 * <p>DIの設定やシステム環境設定、システム全体に関わる定数を
 * 設定するために利用し、その他の設定に関しては
 * application.propertiesファイルに記述します。
 *
 * @author 情報太郎
 *
 */
@Configuration
public class WebConfig {

  /**
   * RestTemplateライブラリのインスタンスを生成します。
   *
   * <p>こちらはDIのために利用されることを想定しています。
   * <p><strong><code>controller</code>や<code>service</code>から呼び出さないでください。</strong><br>
   * 設定することで、<code>@Autowired</code>が設定されたプロパティへ自動的にインスタンスが設定されます。
   *
   * @return RestTemplateインスタンス
   */
  @Bean
  public RestTemplate restTemplate() {
    return new RestTemplate();
  }
}
