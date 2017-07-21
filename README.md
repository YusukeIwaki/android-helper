# [DEPRECATED!] android-helper

Some trivial helpers for Android.

## OkHttp3 Helpers

```
repositories {
  maven { url 'https://jitpack.io' }
}

dependencies {
  compile 'io.reactivex.rxjava2:rxjava:2.x.x' // for using RxHttpClient
  // or compile 'com.parse.bolts:bolts-tasks:1.4.0' if you use BoltsHttpClient

  compile 'com.github.yusukeiwaki.android-helper:okhttp3_helper:0.0.0'
}
```

### RxHttpClient

Just wrapping OkHttp asynchronous callback with `rx.Single`.
HTTP error statuses (401, 403, 404, 500, ...) are notified with `onSuccess` callback.

#### example

```
Request req = new Request.Builder()
                .url("http://www.yahoo.co.jp/")
                .build();

RxHttpClient client = new RxHttpClient(new OkHttpClient());
client.execute(req)
    .filter(response -> response.isSuccessful())
    .map(response -> response.body().string())
    .subscribe(body -> {
        TextView textView = (TextView) findViewById(R.id.text);
        textView.setText(body);
    });
```

### BoltsHttpClient

Wrapping OkHttp asynchronous callback with `bolts.Task`.
Unlike RxHttpClient, HTTP error statuses are notified as `HttpError` callback.
