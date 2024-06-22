import org.junit.Before;
import org.junit.Test;
import service.Service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class Tests {

    Service service;
    String token;

    @Before
    public void setUp() {

        service = new Service();

    }

    public void obterToken(String json) {
        service.fazerRequisicaoPost("login", json);
        assertEquals(200, service.obterStatusCode());
        token = service.obterStringAtributoJson("access");
        System.out.println("o token é " + token);
    }

    @Test
    public void listarTodosCrocodile() {
        service.fazerRequisicaoGet("crocodiles");
        assertEquals(200, service.obterStatusCode());
        assertEquals(1, service.obterAtributoJson("id[0]"));
        assertEquals("Bert", service.obterAtributoJson("name[0]"));
        assertEquals("M", service.obterAtributoJson("sex[0]"));
        assertEquals("2010-06-27", service.obterAtributoJson("date_of_birth[0]"));
        assertEquals(13, service.obterAtributoJson("age[0]"));
    }

    @Test
    public void obterCrocodileDoUsuario() {
        obterToken("usuario.json");
        service.fazerRequisicaoGetComToken("my_crocodile", token);
        assertEquals(200, service.obterStatusCode());
    }

    @Test
    public void criarNovoCrocodile() {
        obterToken("usuario.json");
        service.fazerRequisicaoPostComToken("my_crocodile", "novo_crocodile.json", token);
        assertEquals(201, service.obterStatusCode());
        assertNotNull(service.obterAtributoJson("id"));
        assertEquals("Crocodile novo", service.obterAtributoJson("name"));
        assertEquals("M", service.obterAtributoJson("sex"));
        assertEquals("2024-05-10", service.obterAtributoJson("date_of_birth"));
        assertNotNull(service.obterAtributoJson("age"));
    }

    @Test
    public void validateSchemaCrocodiles() {
        service.fazerRequisicaoGet("crocodiles");
        assertEquals(201, service.obterStatusCode());
        service.validarJsonSchema("all_crocodiles.json");
    }


}
