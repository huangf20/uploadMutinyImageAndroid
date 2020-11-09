package com.example.statebartest;

import java.util.Map;

import io.reactivex.Observable;
import okhttp3.RequestBody;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.PartMap;

public interface ApiService {
    String baseUrl="http://172.18.2.151:8080/";
    @Multipart
    @POST("Service/hello")
    Observable<String>upload(@PartMap Map<String, RequestBody> params);
}
