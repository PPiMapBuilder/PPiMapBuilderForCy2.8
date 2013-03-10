package ppimapbuilder.ppidb.api;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Set;

/**
 *
 * @author CORNUT, CRESSANT, DUPUIS, GRAVOUIL
 *
 */
public class SQLResult {

    /**
     * The field name which is the identifier (typically "id")
     */
    private String idFieldName;
    /**
     * Store the SQL result as <id, <field, value>>
     */
    private LinkedHashMap<String, LinkedHashMap<String, String>> ret = new LinkedHashMap();

    public SQLResult(ResultSet rs) throws SQLException {
        this(rs, "id");
    }

    public SQLResult(ResultSet rs, String id) throws SQLException {
        this.idFieldName = id;
        this.convert(rs);
    }

    private void convert(ResultSet rs) throws SQLException {
        while (rs.next()) {
            LinkedHashMap<String, String> tmpMap = new LinkedHashMap<String, String>();
            for (int i = 1; i <= rs.getMetaData().getColumnCount(); i++) {
                tmpMap.put(rs.getMetaData().getColumnName(i), rs.getString(i));
            }
            this.ret.put(rs.getString(this.idFieldName), tmpMap);
        }
    }

    /**
     * Get all data. Result is organized as LinkedHashMap<Id,
     * <LinkedHashMap<FieldName, Value>>
     *
     * @param id protein ID
     * @return HashMap<String,String>
     */
    public LinkedHashMap getAllData() {
        return this.ret;
    }

    /**
     * Get data for a specific ID. Result is organized as
     * LinkedHashMap<FieldName, Value>
     *
     * @param id protein ID
     * @return HashMap<String,String>
     */
    public LinkedHashMap getData(String id) {
        return this.ret.get(id);
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

    public int size() {
        return ret.size();
    }

    public Set<String> keySet() {
        return ret.keySet();
    }

    public boolean isEmpty() {
        return ret.isEmpty();
    }

    public boolean containsKey(Object key) {
        return ret.containsKey(key);
    }

    public boolean containsValue(Object value) {
        return ret.containsValue(value);
    }

    public Collection<LinkedHashMap<String, String>> values() {
        return ret.values();
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
