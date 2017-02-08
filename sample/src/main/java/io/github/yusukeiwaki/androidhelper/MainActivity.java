package io.github.yusukeiwaki.androidhelper;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;
import io.github.yusukeiwaki.helper.okhttp3.RxHttpClient;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.functions.Predicate;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {
  RxHttpClient client;

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    String url = "http://www.yahoo.co.jp/";

    client = new RxHttpClient(new OkHttpClient());
    client.execute(new Request.Builder().url(url).build())
        .filter(new Predicate<Response>() {
          @Override public boolean test(Response response) throws Exception {
            return response.isSuccessful();
          }
        })
        .map(new Function<Response, String>() {
          @Override public String apply(Response response) throws Exception {
            return response.body().string();
          }
        })
        .subscribe(new Consumer<String>() {
          @Override public void accept(String s) throws Exception {
            TextView textView = (TextView) findViewById(R.id.text);
            textView.setText(s);
          }
        });
  }
}
