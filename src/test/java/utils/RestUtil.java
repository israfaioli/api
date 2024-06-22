package utils;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import java.io.FileReader;
import java.util.Map;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class RestUtil {

    public static RequestSpecification requestSpecification;

    public static void setarBaseUrl(String baseUrl, String path) {
        System.out.println("baseUrl ".concat(baseUrl).concat(path));
        RestAssured.baseURI = baseUrl;
        RestAssured.basePath = path;
        RestAssured.useRelaxedHTTPSValidation();
        requestSpecification = RestAssured.given();
    }

    public static void setarHeaders(Map<String, String> contentHeader) {
        requestSpecification.headers(contentHeader).log().all();
    }

    public static void definirPathParam(String parameter, String value) {
        requestSpecification.pathParam(parameter, value).log().all();
    }

    public static void setarQueryParam(String key, String value) {
        requestSpecification.queryParam(key, value);
    }

    public static void setarQueryParams(Map<String, String> parameters) {
        requestSpecification.queryParams(parameters);
    }

    public static void setarBody(Map<Object, Object> contentBody) {
        System.out.println("ENVIANDO CORPO DA REQUISICAO: " + requestSpecification.body(contentBody).log().all());
        requestSpecification.body(contentBody);
    }

    public static Response requisicaoGet() {
        return requestSpecification.get();
    }

    public static Response requisicaoPost() {
        return requestSpecification.post();
    }

    public static Response requisicaoDelete() {
        return requestSpecification.delete();
    }

    public static JSONObject lerJson(String jsonPath, String jsonPayload) {
        JSONParser parser = new JSONParser();
        JSONObject jsonObject = null;

        try {
            jsonObject = (JSONObject) parser.parse(new FileReader(jsonPath + jsonPayload));
        } catch (ParseException e) {
            e.printStackTrace();
        } catch (Exception e) {
            System.out.println("Erro na leitura do arquivo");
        }

        return jsonObject;
    }
}
