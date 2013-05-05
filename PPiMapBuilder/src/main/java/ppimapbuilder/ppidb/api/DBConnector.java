package ppimapbuilder.ppidb.api;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
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
    private String url;
    /**
     * Database user used by JDBC. Must me stored in a "server.cfg" at the
     * second line,located in the resources folder.
     */
    private String user;
    /**
     * Database password used by JDBC. Must me stored in a "server.cfg",at the
     * third line , located in the resources folder.
     */
    private String password;
    /**
     * SQL query for selecting every data. .getAllData() methodes add WHERE
     * clause to this String and execute the query.
     */
    private final String query;

    /**
     * Default constructor
     */
    private DBConnector() throws SQLException, IOException {
        this.getServerConfig();

        con = DriverManager.getConnection(this.url, this.user, this.password);
        this.query = "select distinct "
                + "    interaction.id as \"id\", "
                + "    p1.uniprot_id as \"uniprotidA\", "
                + "    p1.gene_name as \"interactorA\", "
                + "    p1.organism_id as \"taxidA\", "
                + "    org1.name as \"orgaA\", "
                + "    p2.uniprot_id as \"uniprotidB\", "
                + "    p2.gene_name as \"interactorB\", "
                + "    p2.organism_id as \"taxidB\", "
                + "    org2.name as \"orgaB\", "
                + "    db.name as \"srcdb\", "
                + "    expsys.name as \"expsys\", "
                + "    pub.pubmed_id as \"pubmed\" "
                + "from interaction "
                + "    join protein as \"p1\" on interaction.protein_id1 = p1.id "
                + "    join protein as \"p2\" on interaction.protein_id2 = p2.id "
                + "    join organism as \"org1\" on p1.organism_id = org1.tax_id "
                + "    join organism as \"org2\" on p2.organism_id = org2.tax_id "
                + "    join link_data_interaction as \"lnk\" on lnk.interaction_id = interaction.id "
                + "    join interaction_data as \"intdata\" on lnk.interaction_data_id = intdata.id "
                + "    join source_database as \"db\" on intdata.db_source_name = db.name "
                + "    join experimental_system as \"expsys\" on expsys.name = intdata.experimental_system "
                + "    join publication as \"pub\" on pub.pubmed_id = intdata.pubmed_id ";

        st = con.createStatement();
    }

    /**
     * Retrieve server configuration from "server.cfg" file. This file must be
     * located in the resource folder.
     *
     * @throws IOException
     */
    private void getServerConfig() throws IOException {
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

        return new SQLResult(st.executeQuery(this.query + " where "
                + "    ( p1.uniprot_id = '" + uniprot + "' or p2.uniprot_id = '" + uniprot + "' )"
                + " AND org1.tax_id IN (3702, 6239, 7227, 9606, 10090, 4932, 4896)"
                + " AND org2.tax_id IN (3702, 6239, 7227, 9606, 10090, 4932, 4896)"
                + " AND db.name IN ('hprd','biogrid', 'intact', 'dip', 'bind', 'mint')"));
    }

    /**
     * Get data about a protein identified by its UniprotID from a list of
     * source databases and a list of organisms. Available fiels: p1_id,
     * p1_gene_name, p1_uniprot_id, p1_taxid, p2_id, p2_gene_name,
     * p2_uniprot_id, p2_taxid
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
                + "	select\n"
                + "		interaction.id as \"id\",\n"
                + "		p1.id as \"p1_id\",\n"
                + "		p1.gene_name as \"p1_gene_name\",\n"
                + "		p1.uniprot_id as \"p1_uniprot_id\",\n"
                + "		p1.organism_id as \"p1_taxid\",\n"
                + "		p2.id as \"p2_id\",\n"
                + "		p2.gene_name as \"p2_gene_name\",\n"
                + "		p2.uniprot_id as \"p2_uniprot_id\",\n"
                + "		p2.organism_id as \"p2_taxid\"\n"
                + "	from interaction\n"
                + "	join protein as \"p1\" on interaction.protein_id1 = p1.id\n"
                + "	join protein as \"p2\" on interaction.protein_id2 = p2.id\n"
                + "	join organism as \"org1\" on p1.organism_id = org1.tax_id\n"
                + "	join organism as \"org2\" on p2.organism_id = org2.tax_id\n"
                + "	where interaction.protein_id1 IN (\n"
                + "		select protein.id\n"
                + "		from homology\n"
                + "		full join protein on protein.id = homology.ptn_id\n"
                + "		where homology.h_id in (\n"
                + "			select h.h_id\n"
                + "			from protein as \"p\"\n"
                + "			join homology as \"h\" on p.id = h.ptn_id\n"
                + "			where p.uniprot_id = '" + uniprot + "'\n"
                + "		)\n"
                + "		UNION\n"
                + "		select protein.id\n"
                + "		from protein\n"
                + "		where protein.uniprot_id = '" + uniprot + "'\n"
                + "	) \n"
                + "	OR interaction.protein_id2\n"
                + "	 IN (\n"
                + "		select protein.id\n"
                + "		from homology\n"
                + "		full join protein on protein.id = homology.ptn_id\n"
                + "		where homology.h_id in (\n"
                + "			select h.h_id\n"
                + "			from protein as \"p\"\n"
                + "			join homology as \"h\" on p.id = h.ptn_id\n"
                + "			where p.uniprot_id = '" + uniprot + "'\n"
                + "		)\n"
                + "		UNION\n"
                + "		select protein.id\n"
                + "		from protein\n"
                + "		where protein.uniprot_id = '" + uniprot + "'\n"
                + "	) \n"
                + "	and ( (p1.organism_id = " + taxIdRef + ") or (p2.organism_id = " + taxIdRef + ") )";


        /*
         q += " AND org1.tax_id IN ("
         + this.formatInClause(orgs)
         + ") "
         + " AND org2.tax_id IN ("
         + this.formatInClause(orgs)
         + ") "
         + "AND db.name IN ("
         + this.formatInClause(dbs)
         + ") ";
         */

        System.out.println(q);
        return new SQLResult(st.executeQuery(q));
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

    public String getQuery() {
        return query;
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
