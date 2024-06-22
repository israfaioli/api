package service;

import com.fasterxml.jackson.databind.JsonNode;
import com.github.fge.jackson.JsonLoader;
import com.github.fge.jsonschema.core.exceptions.ProcessingException;
import com.github.fge.jsonschema.core.report.ProcessingReport;
import com.github.fge.jsonschema.main.JsonSchema;
import com.github.fge.jsonschema.main.JsonSchemaFactory;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import org.json.simple.JSONObject;
import org.junit.Assert;
import org.junit.BeforeClass;
import utils.Keys;
import utils.PropertiesUtil;
import utils.RestUtil;

public class Service {

    private JSONObject arquivoJson;
    private String diretorioJson = "src/test/resources/payloads/";
    private String diretorioSchemaJson = "src/test/resources/schemas/";
    private Response resposta;
    private RequestSpecification requisicao;

    public Service() {

    }

    //---------------------------- SET URLS ---------------------------- //

    public void definirUrlBase(String endpoint) {
        RestUtil.setarBaseUrl(PropertiesUtil.obterProperties("prop.environment"), PropertiesUtil.obterProperties(endpoint));
    }

    //---------------------------- HEADERS ---------------------------- //

    public void definirHeaders() {
        HashMap<String, String> headers = new HashMap<>();
        headers.put("Content-type", Keys.APPLICATION_JSON);
        RestUtil.setarHeaders(headers);
    }

    public void definirHeadersComToken(String token) {
        HashMap<String, String> headers = new HashMap<>();
        headers.put("Content-type", Keys.APPLICATION_JSON);
        headers.put("Authorization", "Bearer " + token);
        RestUtil.setarHeaders(headers);
    }

    //---------------------------- REQUESTS ---------------------------- //

    public void fazerRequisicaoGet(String endpoint) {
        definirUrlBase(endpoint);
        definirHeaders();
        resposta = RestUtil.requisicaoGet();
        System.out.println("---------------------- BACKEND RESPOSTA ------------------------");
        resposta.prettyPrint();
    }

    public void fazerRequisicaoGetComToken(String endpoint, String token) {
        definirUrlBase(endpoint);
        definirHeadersComToken(token);
        resposta = RestUtil.requisicaoGet();
        System.out.println("---------------------- BACKEND RESPOSTA ------------------------");
        resposta.prettyPrint();
    }

    public void fazerRequisicaoGetComPathParam(String endpoint, String param, String value) {
        definirUrlBase(endpoint);
        definirHeaders();
        RestUtil.definirPathParam(param, value);
        resposta = RestUtil.requisicaoGet();
        System.out.println("---------------------- BACKEND RESPOSTA ------------------------");
        resposta.prettyPrint();
    }

    public void fazerRequisicaoPost(String endpoint, String json) {
        definirUrlBase(endpoint);
        definirHeaders();
        definirJson(json);
        resposta = RestUtil.requisicaoPost();
        System.out.println("---------------------- BACKEND RESPONSE ------------------------");
        resposta.prettyPrint();
    }

    public void fazerRequisicaoPostComToken(String endpoint, String json, String token) {
        definirUrlBase(endpoint);
        definirHeadersComToken(token);
        definirJson(json);
        resposta = RestUtil.requisicaoPost();
        System.out.println("---------------------- BACKEND RESPONSE ------------------------");
        resposta.prettyPrint();
    }

    //---------------------------- BODY ---------------------------- //

    public void definirJson(String body) {
        arquivoJson = RestUtil.lerJson(diretorioJson, body);
        RestUtil.setarBody(arquivoJson);
    }

    public int obterStatusCode() {
        System.out.println("The response status code is: " + resposta.getStatusCode());
        return resposta.getStatusCode();
    }

    public String obterStringAtributoJson(String key) {

        return resposta.getBody().jsonPath().get(key);
    }

    public Object obterAtributoJson(String key) {
        return resposta.getBody().jsonPath().get(key);
    }

    public void validarContratoAtributo(Class<?> type, String jsonAttribute) {
        Assert.assertEquals(type, resposta.jsonPath().get(jsonAttribute).getClass());
    }

    // ---------------------------- JSON Schema Validation ---------------------------- //

    public void validarJsonSchema(String arquivoJsonschema) {
        try {
            // Load JSON schema
            File schemaFile = new File(diretorioSchemaJson + arquivoJsonschema);
            JsonNode schemaNode = JsonLoader.fromReader(new FileReader(schemaFile));
            JsonSchemaFactory factory = JsonSchemaFactory.byDefault();
            JsonSchema schema = factory.getJsonSchema(schemaNode);

            // Lendo response JSON
            JsonNode jsonNode = JsonLoader.fromString(resposta.getBody().asString());

            // Validando
            ProcessingReport report = schema.validate(jsonNode);

            if (!report.isSuccess()) {
                System.out.println("Validação JSON Schema falhou: " + report.toString());
                Assert.fail("Validação JSON Schema falhou: " + report.toString());
            } else {
                System.out.println("Validação JSON Schema deu bom.");
            }
        } catch (IOException | ProcessingException e) {
            e.printStackTrace();
            Assert.fail("Uma exceção ocorreu durante a validação do JSON Schema: " + e.getMessage());
        }
    }

    @BeforeClass
    public void tearDown() {
        requisicao = null;
        resposta = null;
    }
}
