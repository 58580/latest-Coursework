import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.PreparedStatement;
import java.util.List;

// accessing each table in the database requires a model class, like this
public class Genre
{
    /* First, map each of the fields (columns) in your table to some public variables. */
    public int genreID;
    public String genreName;

    /* Next, prepare a constructor that takes each of the fields as arguements. */
    public Genre(int genreID, String genreName)
    {
        this.genreID = genreID;
        this.genreName = genreName;
    }

    /* A toString method is vital so that your model items can be sensibly displayed as text. */
    @Override public String toString()
    {
        return genreName;
    }

    /* Different models will require different read and write methods. Here is an example 'readAll' method 
     * which is passed the target list object to populate. */
    public static void readAll(List<Genre> list)
    {
        list.clear();       // Clear the target list first.

        /* Create a new prepared statement object with the desired SQL query. */
        PreparedStatement statement = Application.database.newStatement("SELECT genreID, genreName FROM genres ORDER BY genreID"); 

        if (statement != null)      // Assuming the statement correctly initated...
        {
            ResultSet results = Application.database.runQuery(statement);       // ...run the query!

            if (results != null)        // If some results are returned from the query...
            {
                try {								// ...add each one to the list.
                    while (results.next()) {        			                           
                        list.add( new Genre(results.getInt("genreID"), results.getString("genreName")));
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
