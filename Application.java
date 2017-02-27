import javafx.application.Platform;
import javafx.embed.swing.JFXPanel;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

/* This is the class that contains the main method. As well as setting everything in motion, it also contains a method to stop the program running. */
public class Application
{
    /*The program's database connection is maintained by the DatabaseConnection class.
      database points to the DatabaseConnection class*/
    public static DatabaseConnection database;

    public static void main(String args[])//This is the main method.
    {       
        /* JavaFX apps run in a different processing thread to the main method so that they keep running
           after the main method has completed. The 'start' method is invoked when this thread starts. */
        JFXPanel panel = new JFXPanel();        
        Platform.runLater(() -> start());               
    }

    private static void start() 
    {
        try
        {         
            database = new DatabaseConnection("musicLibrary.db");        // Initiate the database connection.

            // Load the first fxml file that will create the first scene. 
            FXMLLoader loader = new FXMLLoader(Application.class.getResource("PrimaryScene.fxml"));

            // The following creates and displays the stage. 
            Stage stage = new Stage();
            stage.setTitle("Music Library");
            stage.setScene(new Scene(loader.load()));
            stage.show();           

            /* After the startup, the program runs the prepare stage events method 
             * in the PrimarySceneController class to have direct access to the 
             * stage to the stage its scene is displayed.*/
            PrimarySceneController controller = loader.getController();
            controller.prepareStageEvents(stage);
        }
        catch (Exception ex)    // If anything goes wrong starting the application, terminate method is called.
        {
            System.out.println(ex.getMessage());
            terminate();
        }
    }

    // The following method can be called from any controller class and will terminate the application. 
    public static void terminate()
    {
        System.out.println("Closing database connection and terminating application...");                                

        if (database != null) database.disconnect();    // Close the connection to the database 
        System.exit(0);                                 // Finally, terminate the entire application.
    }

}