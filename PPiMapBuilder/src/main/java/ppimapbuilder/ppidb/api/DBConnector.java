package ppimapbuilder.ppidb.api;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Set;

/**
 *
 * @author CORNUT, CRESSANT, DUPUIS, GRAVOUIL
 *
 */
public class DBConnector {

    private static DBConnector _instance = null; // Instance of the dbconnnector to prevent several instances 
    private Connection con = null;
    private PreparedStatement pst = null;
    private ResultSet rs = null;
    private String url;
    private String user;
    private String password;
    private String query;

    /**
     * Default constructor
     */
    private DBConnector() throws SQLException, IOException {
        this.getServerConfig();
        con = DriverManager.getConnection(this.url, this.user, this.password);
        this.query = "select distinct"
                + "    interaction.id as \"id\","
                + "    p1.uniprot_id as \"uniprotidA\","
                + "    p1.gene_name as \"interactorA\","
                + "    p2.uniprot_id as \"uniprotidB\","
                + "    p2.gene_name as \"interactorB\","
                + "    db.name as \"srcdb\","
                + "    org.name as \"orga\","
                + "    expsys.name as \"expsys\","
                + "    pub.pubmed_id as \"pubmed\""
                + "from interaction"
                + "    join protein as \"p1\" on interaction.protein_id1 = p1.id"
                + "    join protein as \"p2\" on interaction.protein_id2 = p2.id"
                + "    join link_data_interaction as \"lnk\" on lnk.interaction_id = interaction.id"
                + "    join interaction_data as \"intdata\" on lnk.interaction_data_id = intdata.id"
                + "    join source_database as \"db\" on intdata.db_source_name = db.name"
                + "    join organism as \"org\" on intdata.organism_tax_id = org.tax_id"
                + "    join experimental_system as \"expsys\" on expsys.name = intdata.experimental_system"
                + "    join publication as \"pub\" on pub.pubmed_id = intdata.pubmed_id"
                + "where p1.uniprot_id = ?"
                + "or p2.uniprot_id = ?";
        pst = con.prepareStatement(this.query);
    }

    private void getServerConfig() throws IOException {
        // Open the file that is the first command line parameter
        // & Get the object of DataInputStream
        BufferedReader br = new BufferedReader(new InputStreamReader(new DataInputStream(new FileInputStream("resources/server.cfg"))));

        this.url = br.readLine();
        this.user = br.readLine();
        this.password = br.readLine();

        //Close the input stream
        br.close();
    }

    /**
     * Method to access the unique instance of DBconnector
     *
     * @return _instance
     */
    public static DBConnector Instance() throws SQLException, IOException {
        if (_instance == null) {
            _instance = new DBConnector();
        }
        return _instance;
    }

    /**
     * Give a HashMap of available organisms in the PPiMapBuilder database.
     *
     * @return HashMap<OrganismName, TaxID>
     * @throws SQLExeception
     */
    public LinkedHashMap<String, Integer> getOrganisms() throws SQLException {
        LinkedHashMap<String, Integer> orga = new LinkedHashMap<String, Integer>();
        ResultSet rs = this.con.createStatement().executeQuery("SELECT tax_id AS \"id\", name AS \"organism\" FROM organism");
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
        ResultSet rs = this.con.createStatement().executeQuery("SELECT initcap(name) as \"db\" FROM source_database");
        while (rs.next()) {
            db.add(rs.getString("db"));
        }
        return db;
    }

    /**
     * Get data about a protein identified by its UniprotID.
     *
     * @param uniprot UniprotID
     * @return SQLResult
     * @throws SQLExeception
     */
    public SQLResult getAllData(String uniprot) throws SQLException {
        pst.setString(1, uniprot);
        pst.setString(2, uniprot);
        rs = pst.executeQuery();
        return new SQLResult(rs);
    }

    public Set<String> getKeys(SQLResult sqlr) {
        return sqlr.keySet();
    }

    @Override
    protected void finalize() throws Throwable {
        super.finalize();
        try {
            if (rs != null) {
                rs.close();
            }
            if (pst != null) {
                pst.close();
            }
            if (con != null) {
                con.close();
            }

        } catch (SQLException ex) {
            System.err.println("<html>Error while disconnecting : " + ex.getLocalizedMessage());
        }
    }

    public String getQuery() {
        return query;
    }
}