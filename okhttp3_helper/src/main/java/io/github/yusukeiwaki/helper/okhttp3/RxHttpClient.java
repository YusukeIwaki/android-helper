/*
 * Copyright 2017. @YusukeIwaki
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.github.yusukeiwaki.helper.okhttp3;

import io.reactivex.Single;
import io.reactivex.SingleEmitter;
import io.reactivex.SingleOnSubscribe;
import io.reactivex.functions.Cancellable;
import java.io.IOException;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public final class RxHttpClient {
  private final OkHttpClient client;
  public RxHttpClient(OkHttpClient client) {
    this.client = client;
  }

  public Single<Response> execute(final Request request) {
    return Single.create(new SingleOnSubscribe<Response>() {
      @Override public void subscribe(final SingleEmitter<Response> singleEmitter) throws Exception {
        final Call newCall = client.newCall(request);
        newCall.enqueue(new Callback() {
          @Override public void onFailure(Call call, IOException e) {
            singleEmitter.onError(e);
          }

          @Override public void onResponse(Call call, Response response) throws IOException {
            singleEmitter.onSuccess(response);
          }
        });

        singleEmitter.setCancellable(new Cancellable() {
          @Override public void cancel() throws Exception {
            newCall.cancel();
          }
        });
      }
    });
  }
}
