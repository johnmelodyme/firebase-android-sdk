// Copyright 2020 Google LLC
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//      http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

package com.google.firebase.ml.modeldownloader.internal;

import androidx.annotation.NonNull;
import com.google.android.datatransport.Encoding;
import com.google.android.datatransport.Event;
import com.google.android.datatransport.Transport;
import com.google.android.datatransport.TransportFactory;

/**
 * This class is responsible for sending FirebaseMl Stats to Firebase through Google DataTransport.
 *
 * <p>These will be equivalent to FirebaseMlLogEvent.proto internally.
 *
 * @hide
 */
public class DataTransportMlStatsSender {
  private static final String FIREBASE_ML_STATS_NAME = "FIREBASE_ML_LOG_SDK";
  private final Transport<FirebaseMlStat> transport;

  @NonNull
  public static DataTransportMlStatsSender create(TransportFactory transportFactory) {
    final Transport<FirebaseMlStat> transport =
        transportFactory.getTransport(
            FIREBASE_ML_STATS_NAME,
            FirebaseMlStat.class,
            Encoding.of("json"),
            FirebaseMlStat.getFirebaseMlJsonTransformer());
    return new DataTransportMlStatsSender(transport);
  }

  DataTransportMlStatsSender(Transport<FirebaseMlStat> transport) {
    this.transport = transport;
  }

  public void sendStats(@NonNull FirebaseMlStat stat) {
    // Thoughts? Use .send or .schedule - which gives back task of logging progress? Not sure how
    // strongly we feel about tracking these?
    transport.send(Event.ofData(stat));
  }
}
