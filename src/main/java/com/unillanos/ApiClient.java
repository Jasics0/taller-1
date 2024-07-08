package com.unillanos;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.List;

import com.google.gson.Gson;

public class ApiClient {
    private static final String API_BASE_URL = "http://localhost:8080/api";
    private static final HttpClient httpClient = HttpClient.newBuilder()
            .version(HttpClient.Version.HTTP_2)
            .connectTimeout(Duration.ofSeconds(10))
            .build();
    private static final Gson gson = new Gson();

    public static String sendEvaluation(Long areaId, List<ShapeArea> shapes) throws Exception {
        String url = API_BASE_URL;

        EvaluationData evaluationData = new EvaluationData(areaId, shapes);
        String jsonBody = gson.toJson(evaluationData);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(jsonBody))
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() == 200) {
            return response.body();
        } else {
            throw new Exception("Error sending evaluation: " + response.statusCode());
        }
    }

    private static class EvaluationData {
        Long areaId;
        List<ShapeArea> shapes;

        EvaluationData(Long areaId, List<ShapeArea> shapes) {
            this.areaId = areaId;
            this.shapes = shapes;
        }
    }
}