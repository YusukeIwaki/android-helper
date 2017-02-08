package io.github.yusukeiwaki.helper.okhttp3;

import bolts.Task;
import bolts.TaskCompletionSource;
import java.io.IOException;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public final class BoltsHttpClient {
  private final OkHttpClient client;
  public BoltsHttpClient(OkHttpClient client) {
    this.client = client;
  }

  public Task<Response> execute(Request request) {
    final TaskCompletionSource<Response> tcs = new TaskCompletionSource<>();

    client.newCall(request).enqueue(new Callback() {
      @Override public void onFailure(Call call, IOException e) {
        tcs.setError(e);
      }

      @Override public void onResponse(Call call, Response response) throws IOException {
        if (response.isSuccessful()) {
          tcs.setResult(response);
        } else {
          tcs.setError(new HttpError(response));
        }
      }
    });

    return tcs.getTask();
  }

  public class HttpError extends Exception {
    private final Response response;
    private String bodyCache;

    public HttpError(Response response) {
      this.response = response;
    }

    public int code() {
      return response.code();
    }

    public String body() {
      if (bodyCache == null) {
        bodyCache = fetchBody();
      }
      return bodyCache;
    }

    private String fetchBody() {
      try {
        String body = response.body().string();
        if (body == null) {
          return "";
        } else {
          return body;
        }
      } catch (IOException e) {
        return "";
      }
    }
  }
}