package ppimapbuilder.ppidb.api;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;

/**
 * 
 * @author CORNUT, CRESSANT, DUPUIS, GRAVOUIL
 *
 */
public class SQLResult {

    private String id;
    private HashMap<String, HashMap<String, String>> ret = new HashMap();

    public SQLResult(ResultSet rs, String id) throws SQLException {
        this.id = id;
        this.convert(rs );
    }

    private void convert(ResultSet rs) throws SQLException {

        while (rs.next()) {
            HashMap<String, String> tmpMap = new HashMap();
            for (int i = 1; i <= rs.getMetaData().getColumnCount(); i++) {
                tmpMap.put(rs.getMetaData().getColumnName(i), rs.getString(i));
            }
            this.ret.put(rs.getString(this.id), tmpMap);
        }
    }

    public HashMap getData() {
        return this.ret.get(this.id);
    }

    @Override
    public String toString() {
        String str = "";

        for (String k : this.ret.keySet()) {
            str += "key=" + k + " {\n";

            for (String v : this.ret.get(k).keySet()) {
                str += "\t'" + v + "'=" + this.ret.get(k).get(v) + "\n";
            }
            str += "},\n";
        }
        return str;
    }
}

/*
 *  // get the Java type corresponding to the SQL type by column
 *  Class<?>[] types = new Class<?>[rs.getMetaData().getColumnCount()];
 *  for (int i = 1; i <= rs.getMetaData().getColumnCount(); i++) {       
 *      try {
 *          types[i - 1] = Class.forName(rs.getMetaData().getColumnClassName(i));
 *      } catch (SQLException | ClassNotFoundException e) {
 *  }
 * }
 */
