package tests;

import com.github.javafaker.Faker;
import org.junit.jupiter.api.*;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.example.pages.CadastroPage;

import static org.junit.jupiter.api.Assertions.*;

public class CadastroTest {
    private WebDriver driver;
    private CadastroPage cadastroPage;
    private Faker faker;

    @BeforeEach
    public void setUp() {
        driver = new ChromeDriver();
        cadastroPage = new CadastroPage(driver);
        faker = new Faker();
    }

    @AfterEach
    public void tearDown() {
        driver.quit();
    }

    @Test
    public void testCadastrarComDadosValidos() {
        String nome = faker.name().fullName();
        String email = faker.internet().emailAddress();
        String telefone = gerarTelefoneFaker();

        cadastroPage.preencherNome(nome);
        cadastroPage.preencherEmail(email);
        cadastroPage.preencherTelefone(telefone);
        cadastroPage.clicarCadastrar();

        assertTrue(cadastroPage.obterMensagemSucesso().contains("Contato salvo com sucesso!"));
    }

    @Test
    public void testCadastrarComDadosInvalidos() {
        String nome = faker.name().fullName();
        String emailInvalido = faker.lorem().word();
        String telefoneInvalido = "(00) " + faker.number().digits(5) + "-" + faker.number().digits(4);

        cadastroPage.preencherNome(nome);
        cadastroPage.preencherEmail(emailInvalido);
        cadastroPage.preencherTelefone(telefoneInvalido);
        cadastroPage.clicarCadastrar();

        assertFalse(cadastroPage.obterMensagemSucesso().contains("Contato salvo com sucesso!"));
    }

    @Test
    public void testCadastrarComFaker() {
        String nome = faker.name().fullName();
        String email = faker.internet().emailAddress();
        String telefone = gerarTelefoneFaker();

        cadastroPage.preencherNome(nome);
        cadastroPage.preencherEmail(email);
        cadastroPage.preencherTelefone(telefone);
        cadastroPage.clicarCadastrar();

        assertTrue(cadastroPage.obterMensagemSucesso().contains("Contato salvo com sucesso!"));
    }

    private String gerarTelefoneFaker() {
        String ddd = String.valueOf(faker.number().numberBetween(11, 99));
        String prefixo = faker.number().digits(5);
        String sufixo = faker.number().digits(4);
        return String.format("(%s) %s-%s", ddd, prefixo, sufixo);
    }
    @Test
    public void testCadastroComCamposVazios() {
        cadastroPage.preencherNome("");
        cadastroPage.preencherEmail("");
        cadastroPage.preencherTelefone("");
        cadastroPage.clicarCadastrar();

        assertFalse(cadastroPage.obterMensagemSucesso().contains("Contato salvo com sucesso!"));
    }

    @Test
    public void testEmailSemArroba() {
        String nome = faker.name().fullName();
        String email = "emailsemarroba.com";
        String telefone = gerarTelefoneFaker();

        cadastroPage.preencherNome(nome);
        cadastroPage.preencherEmail(email);
        cadastroPage.preencherTelefone(telefone);
        cadastroPage.clicarCadastrar();

        assertFalse(cadastroPage.obterMensagemSucesso().contains("Contato salvo com sucesso!"));
    }

    @Test
    public void testTelefoneComLetras() {
        String nome = faker.name().fullName();
        String email = faker.internet().emailAddress();
        String telefone = "(11) ABCDE-FGHI";

        cadastroPage.preencherNome(nome);
        cadastroPage.preencherEmail(email);
        cadastroPage.preencherTelefone(telefone);
        cadastroPage.clicarCadastrar();

        assertTrue(cadastroPage.obterMensagemSucesso().contains("Contato salvo com sucesso!"));
    }

    @Test
    public void testNomeComCaracteresEspeciais() {
        String nome = "@#%$*! Nome123";
        String email = faker.internet().emailAddress();
        String telefone = gerarTelefoneFaker();

        cadastroPage.preencherNome(nome);
        cadastroPage.preencherEmail(email);
        cadastroPage.preencherTelefone(telefone);
        cadastroPage.clicarCadastrar();

        assertFalse(cadastroPage.obterMensagemSucesso().contains("Contato salvo com sucesso!"));
    }

    
}
