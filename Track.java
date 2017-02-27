import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.PreparedStatement;
import java.util.List;

/* accessing each table in the database requires a model class, like this example: */
public class Track
{
    /* First, map each of the fields (columns) in the table to some public variables. */
    public int trackID;
    public String trackName;
    public int genreId;

    /* Next, prepare a constructor that takes each of the fields as arguements. */
    public Track(int trackID, String trackName, int genreId)
    {
        this.trackID = trackID;        
        this.trackName = trackName;
        this.genreId = genreId;

    }

    /* A toString method is vital so that the model items can be sensibly displayed as text. */
    @Override public String toString()
    {
        return trackName;
    }

    /* Different models will require different read and write methods. Here is an example 'readAll' method 
     * which is passed the target list object to populate. */
    public static void readAll(List<Track> list)
    {
        list.clear();       // Clear the target list first.

        /* Create a new prepared statement object with the desired SQL query. */
        PreparedStatement statement = Application.database.newStatement("SELECT trackID, trackName, genreId FROM tracks ORDER BY trackID"); 

        if (statement != null)      // Assuming the statement correctly initated...
        {
            ResultSet results = Application.database.runQuery(statement);       // ...run the query!

            if (results != null)        // If some results are returned from the query...
            {
                try {                               // ...add each one to the list.
                    while (results.next()) {                                               
                        list.add( new Track(results.getInt("trackID"), results.getString("trackName"), results.getInt("genreId")));
                    }
                }
                catch (SQLException resultsexception)       // Catch any error processing the results.
                {
                    System.out.println("Database result processing error: " + resultsexception.getMessage());
                }
            }
        }

    }

    public static Track getById(int trackID)        //gets the track selected and all of its details from the database
    {
        Track track = null;

        PreparedStatement statement = Application.database.newStatement("SELECT trackID, trackName, genreId FROM tracks WHERE trackID = ?"); 

        try 
        {
            if (statement != null)
            {
                statement.setInt(1, trackID);
                ResultSet results = Application.database.runQuery(statement);

                if (results != null)
                {
                    track = new Track(results.getInt("trackID"), results.getString("trackName"), results.getInt("genreId"));
                }
            }
        }
        catch (SQLException resultsexception)
        {
            System.out.println("Database result processing error: " + resultsexception.getMessage());
        }

        return track;
    }

    public static void deleteById(int trackID)      //deletes the track selected
    {
        try 
        {

            PreparedStatement statement = Application.database.newStatement("DELETE FROM tracks WHERE trackID = ?");             
            statement.setInt(1, trackID);

            if (statement != null)
            {
                Application.database.executeUpdate(statement);
            }
        }
        catch (SQLException resultsexception)
        {
            System.out.println("Database result processing error: " + resultsexception.getMessage());
        }

    }

    public void save()      //This method is called when saving/updating the database,    
    {
        PreparedStatement statement;

        try 
        {

            if (trackID == 0)
            {

                statement = Application.database.newStatement("SELECT trackID FROM tracks ORDER BY trackID DESC");             

                if (statement != null)  
                {
                    ResultSet results = Application.database.runQuery(statement);
                    if (results != null)
                    {
                        trackID = results.getInt("trackID") + 1;
                    }
                }
                //code below is save for add 
                statement = Application.database.newStatement("INSERT INTO tracks (trackID, trackName, genreId) VALUES (?, ?, ?)");             
                statement.setInt(1, trackID);
                statement.setString(2, trackName);
                statement.setInt(3, genreId);

            }
            else
            {
                //code below is save for edit
                statement = Application.database.newStatement("UPDATE tracks SET trackName = ?, genreId = ? WHERE trackID = ?");             
                statement.setString(1, trackName);
                statement.setInt(2, genreId);   
                statement.setInt(3, trackID);

            }

            if (statement != null)
            {
                Application.database.executeUpdate(statement);
            }
        }
        catch (SQLException resultsexception)
        {
            System.out.println("Database result processing error: " + resultsexception.getMessage());
        }

    }

}
