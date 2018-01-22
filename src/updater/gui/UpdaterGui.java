package updater.gui;

import java.io.IOException;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import updater.programs.ProgramStatus;

/**
 * @author Sanjin Kurelić
 */
public class UpdaterGui extends Application {    

    @Override
    public void start(Stage primaryStage) throws IOException {        
        Parent content = FXMLLoader.load(getClass().getResource("Window.fxml"));
        Scene scene = new Scene(content, 400, 400);
        
        primaryStage.setTitle("Updater");
        primaryStage.setScene(scene);
        primaryStage.show();
        
        primaryStage.setOnCloseRequest((WindowEvent event) -> {
            ProgramStatus.savePrograms();
            System.exit(0);
        });
        
        System.out.println(ProgramStatus.getJsonProgramDefinition());
    }

    public static void main(String[] args) {
        launch(args);
    }

}
