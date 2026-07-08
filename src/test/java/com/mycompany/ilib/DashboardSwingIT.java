package com.mycompany.ilib;

import com.mycompany.models.Books;
import com.mycompany.models.Lendings;
import com.mycompany.models.Users;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.io.File;
import javax.imageio.ImageIO;
import javax.swing.JButton;
import org.assertj.swing.core.BasicRobot;
import org.assertj.swing.core.Robot;
import org.assertj.swing.edt.FailOnThreadViolationRepaintManager;
import org.assertj.swing.edt.GuiActionRunner;
import org.assertj.swing.edt.GuiQuery;
import org.assertj.swing.edt.GuiTask;
import org.assertj.swing.fixture.FrameFixture;
import org.assertj.swing.fixture.JTableFixture;
import org.assertj.swing.fixture.JTextComponentFixture;
import org.assertj.swing.data.TableCell;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestWatcher;
import org.junit.runner.Description;
import static org.junit.Assert.*;

public class DashboardSwingIT extends IntegrationTestSupport {

    private FrameFixture window;
    private Robot robot;

    @Rule
    public TestWatcher screenshotOnFailure = new TestWatcher() {
        @Override
        protected void failed(Throwable e, Description description) {
            takeScreenshot(description.getMethodName());
        }
    };

    @BeforeClass
    public static void installThreadChecks() {
        FailOnThreadViolationRepaintManager.install();
    }

    @Before
    public void setUp() throws Exception {
        cleanAutomationData();
        robot = BasicRobot.robotWithNewAwtHierarchy();
        Dashboard frame = GuiActionRunner.execute(new GuiQuery<Dashboard>() {
            @Override
            protected Dashboard executeInEDT() {
                return new Dashboard();
            }
        });
        window = new FrameFixture(robot, frame);
        window.show();
    }

    @After
    public void tearDown() throws Exception {
        if (window != null) {
            window.cleanUp();
        }
        if (robot != null) {
            robot.cleanUp();
        }
        cleanAutomationData();
    }

    @Test
    public void dashboardPermiteNavegarPorModulosPrincipales() {
        window.panel("principal.panel").requireVisible();

        clickButton("dashboard.usersButton");
        window.panel("users.panel").requireVisible();

        clickButton("dashboard.booksButton");
        window.panel("books.panel").requireVisible();

        clickButton("dashboard.lendingsButton");
        window.panel("lendings.panel").requireVisible();

        clickButton("dashboard.returnsButton");
        window.panel("returns.panel").requireVisible();

        clickButton("dashboard.reportsButton");
        window.panel("reports.panel").requireVisible();
    }

    @Test
    public void registrarUsuarioDesdeLaInterfaz() throws Exception {
        String userName = UI_PREFIX + "Usuario_UI";

        clickButton("dashboard.usersButton");
        window.panel("users.panel").requireVisible();
        clickButton("users.addButton");
        window.panel("upUsers.panel").requireVisible();

        replaceText("upUsers.nameField", userName);
        replaceText("upUsers.lastNamePField", "Paterno");
        replaceText("upUsers.lastNameMField", "Materno");
        replaceText("upUsers.addressField", "Direccion UI");
        replaceText("upUsers.phoneField", "999111222");
        window.button("upUsers.submitButton").click();
        closeAvisoDialog();

        Users saved = findUserByName(userName);
        assertNotNull(saved);
        assertEquals("999111222", saved.getTel());
    }

    @Test
    public void registrarLibroDesdeLaInterfaz() throws Exception {
        String title = UI_PREFIX + "Libro_UI";

        clickButton("dashboard.booksButton");
        window.panel("books.panel").requireVisible();
        clickButton("books.addButton");
        window.panel("upBooks.panel").requireVisible();

        replaceText("upBooks.titleField", title);
        replaceText("upBooks.dateField", "2026");
        replaceText("upBooks.authorField", "Autor UI");
        replaceText("upBooks.categoryField", "Automatizacion");
        replaceText("upBooks.editorialField", "Primera");
        replaceText("upBooks.languageField", "ES");
        replaceText("upBooks.pagesField", "100");
        replaceText("upBooks.descriptionField", "Libro creado por UI");
        replaceText("upBooks.stockField", "3");
        replaceText("upBooks.availableField", "3");
        replaceText("upBooks.copiesField", "UI-001");
        window.button("upBooks.submitButton").click();
        closeAvisoDialog();

        Books saved = findBookByTitle(title);
        assertNotNull(saved);
        assertEquals(3, saved.getAvailable());
    }

    @Test
    public void prestarYDevolverLibroDesdeLaInterfaz() throws Exception {
        Users user = createUser(UI_PREFIX + "Usuario_Flujo");
        Books book = createBook(UI_PREFIX + "Libro_Flujo", 1, 1);

        clickButton("dashboard.lendingsButton");
        window.panel("lendings.panel").requireVisible();
        replaceText("lendings.userIdField", String.valueOf(user.getId()));
        replaceText("lendings.bookIdField", String.valueOf(book.getId()));
        window.button("lendings.submitButton").click();
        closeAvisoDialog();

        Books borrowedBook = new DAOBooksImpl().getBookById(book.getId());
        assertEquals(0, borrowedBook.getAvailable());
        Lendings activeLending = new DAOLendingsImpl().getLending(user, book);
        assertNotNull(activeLending);

        clickButton("dashboard.returnsButton");
        window.panel("returns.panel").requireVisible();
        replaceText("returns.userIdField", String.valueOf(user.getId()));
        replaceText("returns.bookIdField", String.valueOf(book.getId()));
        window.button("returns.submitButton").click();
        closeAvisoDialog();

        Books returnedBook = new DAOBooksImpl().getBookById(book.getId());
        assertEquals(1, returnedBook.getAvailable());
        assertNull(new DAOLendingsImpl().getLending(user, book));
    }

    @Test
    public void busquedaDeUsuariosYLibrosMuestraResultadosEnTablas() throws Exception {
        Users user = createUser(UI_PREFIX + "Usuario_Busqueda");
        Books book = createBook(UI_PREFIX + "Libro_Busqueda", 2, 2);

        clickButton("dashboard.usersButton");
        window.panel("users.panel").requireVisible();
        replaceText("users.searchField", user.getName());
        clickButton("users.searchButton");
        JTableFixture usersTable = window.table("users.table");
        usersTable.requireRowCount(1);
        usersTable.cell(TableCell.row(0).column(1)).requireValue(user.getName());

        clickButton("dashboard.booksButton");
        window.panel("books.panel").requireVisible();
        replaceText("books.searchField", book.getTitle());
        clickButton("books.searchButton");
        JTableFixture booksTable = window.table("books.table");
        booksTable.requireRowCount(1);
        booksTable.cell(TableCell.row(0).column(1)).requireValue(book.getTitle());
    }

    private void replaceText(String fieldName, String value) {
        JTextComponentFixture textBox = window.textBox(fieldName);
        textBox.deleteText();
        textBox.enterText(value);
    }

    private void clickButton(String buttonName) {
        final JButton button = window.button(buttonName).target();
        GuiActionRunner.execute(new GuiTask() {
            @Override
            protected void executeInEDT() {
                button.doClick();
            }
        });
        robot.waitForIdle();
    }

    private void closeAvisoDialog() {
        window.optionPane().okButton().click();
    }

    private void takeScreenshot(String methodName) {
        try {
            File outputDir = new File("target/screenshots");
            if (!outputDir.exists()) {
                outputDir.mkdirs();
            }

            Rectangle screen = new Rectangle(Toolkit.getDefaultToolkit().getScreenSize());
            BufferedImage image = new java.awt.Robot().createScreenCapture(screen);
            ImageIO.write(image, "png", new File(outputDir, methodName + ".png"));
        } catch (Exception ignored) {
            // The test failure itself is more important than a best-effort screenshot.
        }
    }
}
