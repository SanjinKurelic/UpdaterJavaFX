package updater.gui;

import java.awt.image.BufferedImage;
import java.io.File;
import java.net.URL;
import java.util.HashMap;
import java.util.ResourceBundle;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.embed.swing.SwingFXUtils;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javax.swing.Icon;
import javax.swing.filechooser.FileSystemView;
import updater.programs.Program;
import updater.programs.ProgramStatus;
import updater.programs.server.GetProgramFromServer;
import updater.programs.server.GetServerProgramVersion;

/**
 * @author Sanjin
 */
public class WindowController implements Initializable {

    private static final int NUMBER_OF_THREADS = 10;

    @FXML
    private VBox list;
    ObservableList observableList = FXCollections.observableArrayList();

    @FXML
    private Button close;

    @FXML
    private Button update;

    private HashMap<String, ProgressBar> programProgress;

    @Override
    public void initialize(URL url, ResourceBundle rb)
    {
        programProgress = new HashMap<>();
        populateListItems();
    }

    @FXML
    private void update()
    {
        close.setDisable(true);
        update.setDisable(true);

        for (ProgressBar progress : programProgress.values())
        {
            progress.setProgress(getProgressStatus(ProgressBarStatus.CHECKING_VERSION));
        }

        ExecutorService executorServerProgramVersion = Executors.newFixedThreadPool(NUMBER_OF_THREADS);
        for (int threadCount = 0; threadCount < NUMBER_OF_THREADS; threadCount++)
        {
            executorServerProgramVersion.execute(new GetServerProgramVersion(this));
        }

        ExecutorService executorServerProgramDownload = Executors.newFixedThreadPool(NUMBER_OF_THREADS);
        for (int threadCount = 0; threadCount < NUMBER_OF_THREADS; threadCount++)
        {
            executorServerProgramDownload.execute(new GetProgramFromServer(this));
        }

    }

    @FXML
    private void close()
    {
        ProgramStatus.savePrograms();
        System.exit(0);
    }

    public synchronized void refreshGui(ProgressBarStatus status, String programName)
    {
        Platform.runLater(() -> refresGuiElements(status, programName));
    }

    private synchronized void refresGuiElements(ProgressBarStatus status, String programName)
    {
        programProgress.get(programName).setProgress(getProgressStatus(status));
        for (ProgressBar progress : programProgress.values())
        {
            if (Double.compare(progress.getProgress(), 1.0) != 0)
            {
                return;
            }
        }
        close.setDisable(false);
        new Alert(Alert.AlertType.INFORMATION, "All programs updated").showAndWait();
    }

    private void populateListItems()
    {
        for (Program program : ProgramStatus.getAllPrograms())
        {
            list.getChildren().add(getProgramItem(program));
        }
    }

    private Node getProgramItem(Program program)
    {
        Pane pane = new Pane();
        pane.prefWidthProperty().bind(list.widthProperty());
        pane.getStylesheets().add(getClass().getResource("style.css").toString());

        GridPane gp = new GridPane();
        gp.prefWidthProperty().bind(list.widthProperty());
        gp.getStyleClass().add("grid");

        gp.add(new ImageView(getImage(program)), 0, 0);
        gp.add(new Label(program.getName()), 1, 0);

        ProgressBar progress = new ProgressBar();
        progress.setProgress(getProgressStatus(ProgressBarStatus.NONE));
        progress.prefWidthProperty().bind(list.widthProperty());
        programProgress.put(program.getName(), progress);
        gp.add(progress, 0, 1, 2, 1);
        pane.getChildren().add(gp);

        return pane;
    }

    private Image getImage(Program program)
    {
        File programFile = new File(program.getPath());
        Icon icon = FileSystemView.getFileSystemView().getSystemIcon(programFile);
        BufferedImage bufferedImage = new BufferedImage(icon.getIconWidth(),
                                                        icon.getIconHeight(), BufferedImage.TYPE_INT_ARGB);
        icon.paintIcon(null, bufferedImage.getGraphics(), 0, 0);
        return SwingFXUtils.toFXImage(bufferedImage, null);
    }

    private double getProgressStatus(ProgressBarStatus status)
    {
        switch (status)
        {
            case CHECKING_VERSION:
                return ProgressBar.INDETERMINATE_PROGRESS;
            case START_UPDATING:
                return 0.1;
            case UPDATING:
                return 0.5;
            case UPDATED:
                return 1.0;
            case NONE:
            default:
                return 0;
        }
    }
}
