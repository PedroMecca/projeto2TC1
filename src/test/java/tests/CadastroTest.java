package tests;

import com.github.javafaker.Faker;
import org.junit.jupiter.api.*;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.example.pages.CadastroPage;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;

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
        String telefone = faker.phoneNumber().cellPhone();

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
        String telefone = faker.phoneNumber().cellPhone();

        cadastroPage.preencherNome(nome);
        cadastroPage.preencherEmail(email);
        cadastroPage.preencherTelefone(telefone);
        cadastroPage.clicarCadastrar();

        assertTrue(cadastroPage.obterMensagemSucesso().contains("Contato salvo com sucesso!"));
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
        String telefone = faker.phoneNumber().cellPhone();

        cadastroPage.preencherNome(nome);
        cadastroPage.preencherEmail(email);
        cadastroPage.preencherTelefone(telefone);
        cadastroPage.clicarCadastrar();

        assertTrue(cadastroPage.obterMensagemSucesso().contains("Contato salvo com sucesso!"));
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

        assertFalse(cadastroPage.obterMensagemSucesso().contains("Contato salvo com sucesso!"));
    }

    @Test
    public void testNomeComCaracteresEspeciais() {
        String nome = "@#%$*! Nome123";
        String email = "pebonellimecca@gmail.com";
        String telefone = "12 3456789";

        cadastroPage.preencherNome(nome);
        cadastroPage.preencherEmail(email);
        cadastroPage.preencherTelefone(telefone);
        cadastroPage.clicarCadastrar();

        assertTrue(cadastroPage.obterMensagemSucesso().contains("Contato salvo com sucesso!"));
    }

    @Test
    public void testNomeMuitoLongo() {
        String nome = faker.lorem().characters(300);
        String email = "alkfhnlkg@gmail";
        String telefone = "12 223232323";

        cadastroPage.preencherNome(nome);
        cadastroPage.preencherEmail(email);
        cadastroPage.preencherTelefone(telefone);
        cadastroPage.clicarCadastrar();

        assertFalse(cadastroPage.obterMensagemSucesso().contains("Contato salvo com sucesso!"));
    }

    @Test
    public void testTelefoneCurto() {
        String nome = faker.name().fullName();
        String email = faker.internet().emailAddress();
        String telefone = "(11) 1234-567";

        cadastroPage.preencherNome(nome);
        cadastroPage.preencherEmail(email);
        cadastroPage.preencherTelefone(telefone);
        cadastroPage.clicarCadastrar();

        assertFalse(cadastroPage.obterMensagemSucesso().contains("Contato salvo com sucesso!"));
    }

    @Test
    public void testEmailComDominioInvalido() {
        String nome = faker.name().fullName();
        String email = "usuario@email";
        String telefone = faker.phoneNumber().cellPhone();

        cadastroPage.preencherNome(nome);
        cadastroPage.preencherEmail(email);
        cadastroPage.preencherTelefone(telefone);
        cadastroPage.clicarCadastrar();

        assertFalse(cadastroPage.obterMensagemSucesso().contains("Contato salvo com sucesso!"));
    }

    @Test
    public void testCamposComEspacosEmBrancoDevemSerRejeitados() {
        cadastroPage.preencherFormulario("     ", "     ", "     ");
        cadastroPage.submeterFormulario();
        assertTrue(driver.getPageSource().contains("erro") || driver.getPageSource().contains("preenchimento"));
    }


    @Test
    public void testSubmissaoDuplicada() {
        String nome = "Duplicado Teste";
        String email = "duplicado@teste.com";
        String telefone = "11912345678";

        cadastroPage.preencherFormulario(nome, email, telefone);
        cadastroPage.submeterFormulario();

        cadastroPage.preencherFormulario(nome, email, telefone);
        cadastroPage.submeterFormulario();

        driver.get("https://projeto2-seven-sandy.vercel.app/index.html");
        List<WebElement> cards = driver.findElements(By.className("card"));

        long count = cards.stream().filter(c -> {
            return c.getText().contains(nome);
        }).count();
        assertTrue(count <= 1, "Cadastro duplicado foi aceito!");
    }

    @Test
    public void testNomeComQuebraDeLinha_DeveSerRejeitado() {
        cadastroPage.preencherFormulario("Paulo\nHenrique", "paulo.linha@teste.com", "11998706670");
        cadastroPage.submeterFormulario();

        driver.get("https://projeto2-seven-sandy.vercel.app/contatos.html");

        List<WebElement> cards = driver.findElements(By.className("card"));
        boolean nomeComQuebraFoiSalvo = cards.stream()
                .anyMatch(c -> c.getText().contains("Paulo") && c.getText().contains("Henrique"));

        assertFalse(nomeComQuebraFoiSalvo, "Erro: nome com quebra de linha foi aceito e salvo!");
    }


}
