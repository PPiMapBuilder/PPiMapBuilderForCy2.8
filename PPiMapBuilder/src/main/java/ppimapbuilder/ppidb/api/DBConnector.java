package ppimapbuilder.ppidb.api;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.LinkedHashMap;

/**
 * 
 * @author CORNUT, CRESSANT, DUPUIS, GRAVOUIL
 *
 */
public class DBConnector {

    private Connection con = null;
    private Statement st = null;
    private ResultSet rs = null;
    private String url = "jdbc:postgresql://jesuisunegrossepatateetjetediszutfacedepot";
    private String user = "ppimapbuilder";
    private String password = "ppimapbuilder";

    public DBConnector() {
        try {
            con = DriverManager.getConnection(this.url, this.user, this.password);
            st = con.createStatement();

        } catch (SQLException ex) {
            System.err.println("Error while connecting : " + ex.getLocalizedMessage());
        }
    }

    /**
     * Give a HashMap of available organisms in the PPiMapBuilder database.
     *
     * @return HashMap<OrganismName, TaxID>
     * @throws SQLExeception
     */
    public LinkedHashMap<String, Integer> getOrganisms() throws SQLException {
        LinkedHashMap<String, Integer> orga = new LinkedHashMap<String, Integer>();
        rs = st.executeQuery("SELECT tax_id AS \"id\", name AS \"organism\" FROM organism");

        while (rs.next()) {
            orga.put(rs.getString("organism"), rs.getInt("id"));
        }

        return orga;
    }

    /**
     * Give a list of reference databases available in the PPiMapBuilder
     * database.
     *
     * @return ArrayList<String>
     * @throws SQLExeception
     */
    public ArrayList<String> getDatabases() throws SQLException {
        ArrayList<String> db = new ArrayList<String>();
        rs = st.executeQuery("SELECT initcap(name) as \"db\" FROM source_database");

        while (rs.next()) {
            db.add(rs.getString("db"));
        }

        return db;
    }

    /**
     * Get data about a protein identified by its UniprotID and the chosen
     * reference organism.
     *
     * @param uniprot UniprotID
     * @param organism Taxonomic ID
     * @return SQLResult
     * @throws SQLExeception
     */
    public SQLResult getAllData(String uniprot, int organism) throws SQLException {

        //TODO: faire une belle requete des familles pour récuperer toutes les infos de l'uniprot/organism donné.

        String query = "select * from protein limit 5";


        rs = st.executeQuery(query);
        SQLResult sqlr = new SQLResult(rs, "id");
        return sqlr;

    }

    @Override
    protected void finalize() throws Throwable {
        super.finalize();
        try {
            if (rs != null) {
                rs.close();
            }
            if (st != null) {
                st.close();
            }
            if (con != null) {
                con.close();
            }

        } catch (SQLException ex) {
            System.err.println("Error while disconnecting : " + ex.getLocalizedMessage());
        }
    }
}
