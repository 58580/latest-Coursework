import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.layout.Pane;
import javafx.scene.layout.BorderPane;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.control.TreeView;
import javafx.scene.layout.Pane;
import javax.swing.JOptionPane;
import javafx.event.EventHandler;
import javafx.stage.WindowEvent;
import java.util.*;
import javafx.scene.control.ChoiceBox;
import java.io.File;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javafx.scene.text.*;

public class PrimarySceneController
{    
    /* The stage that the scene belongs to, required to catch stage events and test for duplicate controllers. */
    private static Stage stage;     
    private static MediaPlayer player;
    /* These FXML variables exactly corrispond to the controls that make up the scene, as designed in Scene 
     * Builder. It is important to ensure that these match perfectly or the controls won't be interactive. */
    @FXML   private TreeView treeView;
    @FXML   private ListView mainListView;
    @FXML   private Button addButton;
    @FXML   private Button playButton;
    @FXML   private Button stopButton;
    @FXML   private Button editButton;
    @FXML   private Button deleteButton;
    @FXML   private TextField searchTextField;
    @FXML   private Button searchButton;
    @FXML   private Button clearButton;
    @FXML   private Button exitButton;
    @FXML   private Pane borderPane; 
    @FXML   private ChoiceBox searchChoiceBox;
    @FXML   private Text textTrackName;

    public PrimarySceneController() // The constructor method is called first when the scene is loaded.
    {
        System.out.println("Initialising controllers...");

        /* MusicPlayer should only have one initial scene. The following checks to see if a scene already exists (deterimed by if the stage variable has been set)
        and if so terminates the whole application with an error code. */        
        if (stage != null)
        {
            System.out.println("Error, duplicate controller - terminating application!");
            System.exit(-1);
        }        

    } 

    @FXML   void initialize() // This method automatically called after the constructor.
    {            
        /* The following assertions check to see if the JavaFX controls exists. If one of these fails, the
         * application won't work. If the control names in Scene Builder don't match the variables this fails. */ 
        System.out.println("Asserting controls...");
        try
        {
            assert treeView != null : "Can't find treeView";
            assert mainListView != null : "Can't find mainListView";
            assert addButton != null : "Can't find addButton";
            assert playButton != null : "Can't find playButton";
            assert stopButton != null : "Can't find stopButton";
            assert editButton != null : "Can't find editButton";
            assert deleteButton != null : "Can't find deleteButton";
            assert searchTextField != null : "Can't find searchTextField";
            assert searchButton != null : "Can't find searchButton";
            assert clearButton != null : "Can't find clearButton";
            assert exitButton != null : "Can't find exitButton";
            assert borderPane != null : "Can't find border pane.";   
        }
        catch (AssertionError ae)
        {
            System.out.println("FXML assertion failure: " + ae.getMessage());
            Application.terminate();
        }

        // Next, load the list of track from the database and populate the listView. 
        System.out.println("Populating scene with items from the database...");        
        @SuppressWarnings("unchecked")
        List<Track> targetList = mainListView.getItems();  // Grab a reference to the listView's current item list.
        Track.readAll(targetList);                     // Hand over control to the track model to populate this list.*/    

    }

    /* In order to catch stage events (the main example being the close (X) button being clicked)event handlers is needed.
     * This happens after the constructor and the initialize methods. Once this is
     * complete, the scene is fully loaded and ready to use. */
    public void prepareStageEvents(Stage stage)
    {
        System.out.println("Preparing stage events...");

        PrimarySceneController.stage = stage;

        stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
                public void handle(WindowEvent we) {
                    System.out.println("Close button was clicked!");
                    Application.terminate();
                }
            });
    }       

    /* The next ten methods are event handlers for clicking on the buttons. 
     * The names of these methods are set in Scene Builder so they work automatically. */    
    @FXML   void addClicked()// this calls the openNewScene which opens the second scene.
    {
        System.out.println("Add was clicked, opening secondary scene.");
        openNewScene(0);
    }

    @FXML   void editClicked()// this calls the openNewScene which opens the second scene with the details of the selected track.
    {
        System.out.println("Edit was clicked, opening secondary scene.");
        Track selectedItem = (Track) mainListView.getSelectionModel().getSelectedItem(); 
        openNewScene(selectedItem.trackID);     //populates scene with details of the track selected.
    }

    @FXML   void deleteClicked()//this method deletes track
    {
        int n = JOptionPane.showConfirmDialog(
                null, "Are you sure you want to delete this?","Delete",JOptionPane.YES_NO_OPTION); //opens a ConfirmDialogBox to confirm whether to deleted selected item.

        if(n == JOptionPane.YES_OPTION)// if yes clicked,deletes the selected item.
        {
            System.out.println("Delete was clicked!");

            Track selectedItem = (Track) mainListView.getSelectionModel().getSelectedItem();
            Track.deleteById(selectedItem.trackID);
            initialize();
            JOptionPane.showMessageDialog(null, "File Deleted");
        }

    }

    @FXML   void clearClicked()
    {
        System.out.println("Clear was clicked - this feature is not yet implemented!");        
    }

    @FXML   void playClicked()//this method plays the selected track
    {
        stopButton.setDisable(false);
        playButton.setDisable(true);
        Track selectedItem = (Track) mainListView.getSelectionModel().getSelectedItem();//object to get the selected Item from listview
        String trackSelected = selectedItem.trackName; //get name from listview
        try{
            PreparedStatement readPath = Application.database.newStatement("SELECT trackName, path FROM tracks"); // statement to go to database
            ResultSet run = Application.database.runQuery(readPath);//hold all data
            while(run.next()){
                //System.out.println(trackSelected + " " + run.getString("trackName"));
                if(trackSelected.equals(run.getString("trackName"))){// compares the track selected with the database track name
                    String uriString = new File(run.getString("path")).toURI().toString();//holds the filepath of the tracks from the database
                    player = new MediaPlayer( new Media(uriString));//loads the filepath of the track you want to play
                    player.play();//plays the song
                    return;
                }
            }
        }catch(SQLException e){
            System.out.println("error playing song");
        }
    }

    @FXML   void stopClicked()//this stops the playing song
    {
        System.out.println("Stop was clicked");
        stopButton.setDisable(true);
        playButton.setDisable(false);
        player.stop();
    }

    @FXML   void searchChoiceBoxClicked()
    {
        System.out.println("searchChoiceBox was clicked - this feature is not yet implemented!");        
    }

    @FXML   void goClicked()// suppose to search, DIDN'T get it to work.
    {
        System.out.println("Go was clicked");
        Iterator<Track> filter = mainListView.getItems().iterator();
        try{
            PreparedStatement readtrackName = Application.database.newStatement("SELECT trackName FROM tracks"); // statement to go to database
            ResultSet run = Application.database.runQuery(readtrackName);//hold all data
            while (filter.hasNext()){
                Track t = filter.next();
                if (searchChoiceBox.equals(run.getString("trackName"))) filter.remove();

            }
        }catch(SQLException e){
            System.out.println("no");
        }
    }

    @FXML   void exitClicked()
    {
        System.out.println("Exit was clicked!");        
        int n = JOptionPane.showConfirmDialog(
                null, "Are you sure you want to exit?","Exit",JOptionPane.YES_NO_OPTION); //opens a ConfirmDialogBox to confirm whether to exit ot not.

        if(n == JOptionPane.YES_OPTION)// if yes clicked,deletes the selected item.
        {
            Application.terminate();        // Call the terminate method in the main Application class.
            JOptionPane.showMessageDialog(null, "File Deleted");
        }
    }

    /* This method, set in SceneBuilder to occur when the listView is clicked, establishes which
     * item in the view is currently selected (if any) and outputs it to the console. */    
    @FXML   void listViewClicked()
    {
        Track selectedItem = (Track) mainListView.getSelectionModel().getSelectedItem();

        if (selectedItem == null)
        {
            System.out.println("Nothing selected!");
        }
        else
        {
            System.out.println(selectedItem + " (trackID: " + selectedItem.trackID + ") is selected.");

            textTrackName.setText(selectedItem.trackName);
        }
    }

    void openNewScene(int trackID)//loads second scene
    {

        FXMLLoader loader = new FXMLLoader(Application.class.getResource("SecondaryScene.fxml"));

        try
        {
            Stage stage2 = new Stage();
            stage2.setTitle("Details");
            stage2.setScene(new Scene(loader.load()));
            stage2.show();           
            SecondarySceneController controller2 = loader.getController();
            controller2.prepareStageEvents(stage2);

            controller2.setParent(this);
            if (trackID != 0) controller2.loadItem(trackID);            

        }
        catch (Exception ex)
        {
            System.out.println(ex.getMessage());
        }

    }

}

