package ppimapbuilder.ppidb.api;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Set;

/**
 *
 * @author CORNUT, CRESSANT, DUPUIS, GRAVOUIL
 *
 */
public class DBConnector {

    /**
     * Instance of the dbconnnector to prevent several instances
     */
    private static DBConnector _instance = null;
    /**
     * Connection to the PPiDB
     */
    private Connection con = null;
    /**
     * Main SELECT query
     */
    private Statement st = null;
    /**
     * ResultSet of getOrganims() and getDatabases() methods
     */
    private ResultSet rs = null;
    /**
     * URL used by JDBC. Must me stored in a "server.cfg" at the first line,
     * located in the resources folder. It looks like
     * "jdbc:postgresql://<localhost>/<dbname>"
     */
    private final String url;
    /**
     * Database user used by JDBC. Must me stored in a "server.cfg" at the
     * second line,located in the resources folder.
     */
    private final String user;
    /**
     * Database password used by JDBC. Must me stored in a "server.cfg",at the
     * third line , located in the resources folder.
     */
    private final String password;
    /**
     * Prepared statement used to retrieve homologies.
     */
    private final PreparedStatement pstmt;

    /**
     * Default constructor
     */
    private DBConnector() throws SQLException, IOException {

        // get server config
        BufferedReader br = null;
        try {
            br = new BufferedReader(new InputStreamReader(getClass().getResourceAsStream("/server.cfg")));
        } catch (Exception e) {
            throw new IOException();
        }

        this.url = br.readLine();
        this.user = br.readLine();
        this.password = br.readLine();

        br.close();

        // connect the database then prepare statement
        this.con = DriverManager.getConnection(this.url, this.user, this.password);
        this.st = con.createStatement();
        this.pstmt = this.con.prepareStatement(""
                + "select\n"
                + "	protein.id as \"ptn_id\",\n"
                + "	protein.uniprot_id as \"ptn_uniprot_id\",\n"
                + "	protein.gene_name as \"ptn_gene_name\",\n"
                + "	protein.organism_id as \"ptn_tax_id\" \n"
                + "from homology\n"
                + "join protein on protein.id = homology.ptn_id\n"
                + "where homology.h_id in (\n"
                + "	select h.h_id\n"
                + "	from protein as \"p\"\n"
                + "	join homology as \"h\" on p.id = h.ptn_id\n"
                + "	where p.id = ?\n"
                + ")\n"
                + "AND protein.organism_id = ?");
    }

    /**
     * DOCUMENTE ME!
     *
     * @param val
     * @return
     */
    private String formatInClause(ArrayList<?> val) {
        StringBuilder strb = new StringBuilder();
        for (Object v : val) {
            if (v instanceof String) {
                strb.append("'").append(((String) v).toLowerCase()).append("'").append(",");
            } else {
                strb.append(v).append(",");
            }
        }
        strb.deleteCharAt(strb.length() - 1);
        return strb.toString();
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
        ResultSet res = this.con.createStatement().executeQuery("SELECT tax_id AS \"id\", name AS \"organism\" FROM organism");
        while (res.next()) {
            orga.put(res.getString("organism"), res.getInt("id"));
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
        ResultSet res = this.con.createStatement().executeQuery("SELECT initcap(name) as \"db\" FROM source_database");
        while (res.next()) {
            db.add(res.getString("db"));
        }
        return db;
    }

    /**
     * Get data about a protein identified by its UniprotID from a list of
     * source databases and a list of organisms. Available fiels: p1_id,
     * p1_gene_name, p1_uniprot_id, p1_taxid, p1_org_name, p2_id, p2_gene_name,
     * p2_uniprot_id, p2_taxid, p2_org_name srcdb, expsys, pubmed
     *
     * @param uniprot
     * @param taxIdRef
     * @param dbs
     * @param orgs
     * @return
     * @throws SQLException
     */
    public SQLResult getAllData(String uniprot, int taxIdRef, ArrayList<String> dbs, ArrayList<Integer> orgs) throws SQLException {
        String q = ""
                + "	SELECT\n"
                + "		interaction.id AS \"id\",\n"
                + "		p1.id AS \"p1_id\",\n"
                + "		p1.gene_name AS \"p1_gene_name\",\n"
                + "		p1.uniprot_id AS \"p1_uniprot_id\",\n"
                + "		p1.organism_id AS \"p1_taxid\",\n"
                + "		org1.name AS \"p1_org_name\",\n"
                + "		p2.id AS \"p2_id\",\n"
                + "		p2.gene_name AS \"p2_gene_name\",\n"
                + "		p2.uniprot_id AS \"p2_uniprot_id\",\n"
                + "		p2.organism_id AS \"p2_taxid\",\n"
                + "		org2.name AS \"p2_org_name\",\n"
                + "		db.name AS \"srcdb\",\n"
                + "		expsys.name AS \"expsys\",\n"
                + "		pub.pubmed_id AS \"pubmed\"\n"
                + "	FROM interaction\n"
                + "		join protein AS \"p1\" ON interaction.protein_id1 = p1.id\n"
                + "		join protein AS \"p2\" ON interaction.protein_id2 = p2.id\n"
                + "		join organism AS \"org1\" ON p1.organism_id = org1.tax_id\n"
                + "		join organism AS \"org2\" ON p2.organism_id = org2.tax_id\n"
                + "		join link_data_interaction AS \"lnk\" ON lnk.interaction_id = interaction.id\n"
                + "		join interaction_data AS \"intdata\" ON lnk.interaction_data_id = intdata.id\n"
                + "		join source_database AS \"db\" ON intdata.db_source_name = db.name\n"
                + "		join experimental_system AS \"expsys\" ON expsys.name = intdata.experimental_system\n"
                + "		join publication AS \"pub\" ON pub.pubmed_id = intdata.pubmed_id\n"
                + "	WHERE interaction.protein_id1 IN (\n"
                + "		SELECT protein.id\n"
                + "		FROM homology\n"
                + "		JOIN protein on protein.id = homology.ptn_id\n"
                + "		WHERE homology.h_id IN (\n"
                + "			SELECT h.h_id\n"
                + "			FROM protein AS \"p\"\n"
                + "			JOIN homology AS \"h\" ON p.id = h.ptn_id\n"
                + "			WHERE p.uniprot_id = '" + uniprot + "'\n"
                + "		)\n"
                + "		UNION\n"
                + "		SELECT protein.id\n"
                + "		FROM protein\n"
                + "		WHERE protein.uniprot_id = '" + uniprot + "'\n"
                + "	) \n"
                + "	OR interaction.protein_id2\n"
                + "	IN (\n"
                + "		SELECT protein.id\n"
                + "		FROM homology\n"
                + "		JOIN protein ON protein.id = homology.ptn_id\n"
                + "		WHERE homology.h_id IN (\n"
                + "			SELECT h.h_id\n"
                + "			FROM protein AS \"p\"\n"
                + "			JOIN homology AS \"h\" on p.id = h.ptn_id\n"
                + "			WHERE p.uniprot_id = '" + uniprot + "'\n"
                + "		)\n"
                + "		UNION\n"
                + "		SELECT protein.id\n"
                + "		FROM protein\n"
                + "		WHERE protein.uniprot_id = '" + uniprot + "'\n"
                + "	)\n"
                + "	AND ( (p1.organism_id = " + taxIdRef + ") OR (p2.organism_id = " + taxIdRef + ") )\n"
                + "	AND org1.tax_id IN (" + this.formatInClause(orgs) + ")\n"
                + "	AND org2.tax_id IN (" + this.formatInClause(orgs) + ")\n"
                + "	AND db.name IN (" + this.formatInClause(dbs) + ")\n";

        System.out.println(q);
        return new SQLResult(st.executeQuery(q));
    }

    /**
     * Get the homologous proteins for a list of protein (described with their
     * database internal ID) and a reference organism (described with its Tax
     * ID). Available column name : "ptn_id", "ptn_uniprot_id", "ptn_gene_name",
     * "ptn_tax_id".
     *
     *
     * @param proteinId
     * @param taxIdRef
     * @return ProteinID -> { column -> value }
     * @throws SQLException
     */
    private HashMap<Integer, HashMap<String, String>> getHomologous(ArrayList<Integer> proteinId, int taxIdRef) throws SQLException {

        HashMap<Integer, HashMap<String, String>> ret = new HashMap<Integer, HashMap<String, String>>();
        ResultSet res = null;

        for (Integer inte : proteinId) {
            pstmt.setInt(1, inte);
            pstmt.setInt(2, taxIdRef);
            res = pstmt.executeQuery();

            while (res.next()) {
                LinkedHashMap<String, String> tmpMap = new LinkedHashMap<String, String>();
                for (int i = 1; i <= res.getMetaData().getColumnCount(); i++) {
                    tmpMap.put(res.getMetaData().getColumnName(i), res.getString(i));
                }
                ret.put(Integer.valueOf(res.getInt("ptn_id")), tmpMap);
            }
        }
        return ret;
    }

    /**
     * Get keys of a SQLResult. These keys are the protein.id as stored in the
     * database.
     *
     * @param sqlr
     * @return
     */
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
            if (st != null) {
                st.close();
            }
            if (con != null) {
                con.close();
            }

        } catch (SQLException ex) {
            System.err.println("<html>Error while disconnecting : " + ex.getLocalizedMessage());
        }
    }
}
