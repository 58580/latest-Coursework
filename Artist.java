import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.PreparedStatement;
import java.util.List;

// accessing each table in the database requires a model class, like this
public class Artist
{
    /* First, map each of the fields (columns) in your table to some public variables. */
    public int artistID;
    public String artistName;

    /* Next, prepare a constructor that takes each of the fields as arguements. */
    public Artist(int artistID, String artistName)
    {
        this.artistID = artistID;
        this.artistName = artistName;
    }

    /* A toString method is vital so that your model items can be sensibly displayed as text. */
    @Override public String toString()
    {
        return artistName;
    }

    /* Different models will require different read and write methods. Here is an example 'readAll' method 
     * which is passed the target list object to populate. */
    public static void readAll(List<Artist> list)
    {
        list.clear();       // Clear the target list first.

        /* Create a new prepared statement object with the desired SQL query. */
        PreparedStatement statement = Application.database.newStatement("SELECT artistID, artistName FROM artists ORDER BY artistID"); 

        if (statement != null)      // Assuming the statement correctly initated...
        {
            ResultSet results = Application.database.runQuery(statement);       // ...run the query!

            if (results != null)        // If some results are returned from the query...
            {
                try {								// ...add each one to the list.
                    while (results.next()) {        			                           
                        list.add( new Artist(results.getInt("artistID"), results.getString("artistName")));

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
