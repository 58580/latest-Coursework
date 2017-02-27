import javafx.fxml.FXML;
import javafx.stage.Stage;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.event.EventHandler;
import javafx.stage.WindowEvent;
import java.util.List;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javax.swing.JOptionPane;
import javafx.stage.FileChooser;
import java.io.File;
import java.awt.event.ActionEvent;

public class SecondarySceneController
{
    /* The stage that the scene belongs to, required to catch stage events and test for duplicate controllers. */
    private Stage stage;
    private PrimarySceneController parent;

    /* These FXML variables exactly corrispond to the controls that make up the scene, as designed in Scene 
     * Builder. It is important to ensure that these match perfectly or the controls won't be interactive. */
    @FXML   private Button addButton;
    @FXML   private Button browseButton;
    @FXML   private Button removeButton;    
    @FXML   private Button saveButton;
    @FXML   private TextField trackNameTextField;
    @FXML   private TextField artistNameTextField;
    @FXML   private TextField albumNameTextField;    
    @FXML   private TextField pathTextField;
    @FXML   private ChoiceBox genreChoiceBox;    
    @FXML   private Button cancelButton;

    private Track track;

    public SecondarySceneController() // The constructor method is called first when the scene is loaded.
    {
        System.out.println("Initialising controllers...");
    } 

    /* In order to catch stage events (the main example being the close (X) button being clicked)event handlers is needed.
     * This happens after the constructor and the initialize methods. Once this is
     * complete, the scene is fully loaded and ready to use. */
    public void prepareStageEvents(Stage stage)
    {
        System.out.println("Preparing stage events...");

        this.stage = stage;

        stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
                public void handle(WindowEvent we) {
                    System.out.println("Close button was clicked!");
                    stage.close();
                }
            });
    }         

    @FXML   void initialize() // This method automatically called after the constructor.
    {            
        /* The following assertions check to see if the JavaFX controls exists. If one of these fails, the
         * application won't work. If the control names in Scene Builder don't match the variables this fails. */
        System.out.println("Asserting controls...");
        try
        {
            assert trackNameTextField != null : "Can't find trackNameTextField";
            assert albumNameTextField != null : "Can't find albumNameTextField";
            assert artistNameTextField != null : "Can't find artistNameTextField";
            assert pathTextField != null : "Can't find pathTextField";
            assert genreChoiceBox != null : "Can't find genreChoiceBox";
            assert saveButton != null : "Can't find saveButton";
            assert cancelButton != null : "Can't find cancelButton";
            assert browseButton != null : "Can't find browseButton";

        }
        catch (AssertionError ae)
        {
            System.out.println("FXML assertion failure: " + ae.getMessage());
            Application.terminate();
        }
       // Next, load the list of track from the database and populate the listView.
        System.out.println("Populating scene with items from the database...");        
        @SuppressWarnings("unchecked")
        List<Genre> targetList = genreChoiceBox.getItems();  // Grab a reference to the listView's current item list.
        Genre.readAll(targetList);       
        genreChoiceBox.getSelectionModel().selectFirst();
    }

    public void setParent(PrimarySceneController parent)//sets PrimarySceneController as parent so SecondarySceneController can inherit from it
    {
        this.parent = parent;
    }

    public void loadItem(int id)
    {        
        track = Track.getById(id);

        trackNameTextField.setText(track.trackName);
        List<Genre> targetList = genreChoiceBox.getItems();
        for(Genre c : targetList)
        {
            if (c.genreID == track.genreId)
            {
                genreChoiceBox.getSelectionModel().select(c);
            }                
        }

    }

    /*@FXML  public void onEnter(ActionEvent ae)throws Exception{
        System.out.println("Hello World!");
        saveButtonClicked();
    }*/

    /* The next three methods are event handlers for clicking on the buttons. 
     * The names of these methods are set in Scene Builder so they work automatically. */ 
    @FXML  void saveButtonClicked() 
    {
        System.out.println("Save button clicked!");        

        if (track == null)
        {   
            track = new Track(0, "", 0);
        }

        if (trackNameTextField.getText().equals("")){       //checks if the textfield is empty, error shown if it is
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("Error Dialog");
            alert.setHeaderText("Error!!!!");
            alert.setContentText("Texfields can't be empty");
            alert.showAndWait();
        }else{      //otherwise carries on saving
            track.trackName = trackNameTextField.getText();
            Genre selectedGenre = (Genre) genreChoiceBox.getSelectionModel().getSelectedItem();        
            track.genreId = selectedGenre.genreID;
            track.save();
            parent.initialize();
            stage.close();
        }

    }

    @FXML   void cancelButtonClicked()//simply closes the stage
    {
        System.out.println("Cancel button clicked!");        
        stage.close();
    }

    @FXML   void browseButtonClicked()// opens a FileChooser, lets you choose what track to upload
    {
        System.out.println("Browse button clicked!");        
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Pick a file");
        List<File> list = fileChooser.showOpenMultipleDialog(stage);

        for (File file : list) {
            System.out.println(" >>>>>> " + file.getName());
        }
    }

}
