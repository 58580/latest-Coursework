import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.PreparedStatement;
import java.util.List;

// accessing each table in the database requires a model class, like this
public class Album
{
    /* First, map each of the fields (columns) in your table to some public variables. */
    public int albumID;
    public String albumName;

    /* Next, prepare a constructor that takes each of the fields as arguements. */
    public Album(int albumID, String albumName)
    {
        this.albumID = albumID;
        this.albumName = albumName;
    }

    /* A toString method is vital so that your model items can be sensibly displayed as text. */
    @Override public String toString()
    {
        return albumName;
    }
  
    /* Different models will require different read and write methods. Here is an example 'readAll' method 
     * which is passed the target list object to populate. */
    public static void readAll(List<Album> list)
    {
        list.clear();       // Clear the target list first.

        /* Create a new prepared statement object with the desired SQL query. */
        PreparedStatement statement = Application.database.newStatement("SELECT albumID, albumName FROM albums ORDER BY albumID"); 

        if (statement != null)      // Assuming the statement correctly initated...
        {
            ResultSet results = Application.database.runQuery(statement);       // ...run the query!

            if (results != null)        // If some results are returned from the query...
            {
                try {								// ...add each one to the list.
                    while (results.next()) {        			                           
                        list.add( new Album(results.getInt("albumID"), results.getString("albumName")));
                    }
                }
                catch (SQLException resultsexception)       // Catch any error processing the results.
                {
                    System.out.println("Database result processing error: " + resultsexception.getMessage());
                }
            }
        }

    }

}
