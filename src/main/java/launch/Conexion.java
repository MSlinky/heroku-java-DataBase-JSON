
package launch;

import java.net.URISyntaxException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.json.simple.JSONValue;

public class Conexion {
    private static final String DRIVER = "org.postgresql.Driver";     
    private static final String URL = "jdbc:postgresql://ec2-54-83-26-65.compute-1.amazonaws.com:5432/d92kc9soga83vu?ssl=true&sslfactory=org.postgresql.ssl.NonValidatingFactory"; 
    private static final String USERNAME = "azqltajhusfqbq";   
    private static final String PASSWORD = "c34000a59c87aa08c4adc0bce3f5a255835aedc862e1321bb3eeeb3219ca8f70";
    
    String Error = "";
    Connection connection = null;
    private static Connection getConnection() throws SQLException{   
        try {   
            Class.forName(DRIVER);
            return DriverManager.getConnection(URL, USERNAME, PASSWORD);
        } catch (ClassNotFoundException ex) {   
            System.out.println("Where is your PostgreSQL JDBC Driver? "   
            + "Include in your library path!");   
            return null;   
        }
    }

    public Conexion(){
        try {
            connection = getConnection();   
        } catch (SQLException ex) {
            Error = "Connection Failed! Check output console";
        }   
        if (connection != null) {
            Error = "You made it, take control your database now!";
        } else {
            Error = "Failed to make connection!";
        }
    }
    
    public String executeQuery(String query, String name){
        Statement st;
        ResultSet rs;
        try {
            st = connection.createStatement();
            rs = st.executeQuery(query);
            
            String respuesta = getJSONFromResultSet(rs, name);
            
            st.close();
            rs.close();
            
            return respuesta;
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return "Error executeQuery";
    }
    
    public int executeInsert(String query){
        Statement st;
        int rs;
        try {
            st = connection.createStatement();
            rs = st.executeUpdate(query);
            
            st.close();
            
            return rs;
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return 0;
    }
    
    public String getJSONFromResultSet(ResultSet rs,String keyName) {
        Map json = new HashMap(); 
        List list = new ArrayList();
        if(rs!=null){
            try {
                ResultSetMetaData metaData = rs.getMetaData();
                while(rs.next()){
                    Map<String,Object> columnMap = new HashMap<String, Object>();
                    for(int columnIndex=1;columnIndex<=metaData.getColumnCount();columnIndex++){
                        if(rs.getString(metaData.getColumnName(columnIndex))!=null)
                            columnMap.put(metaData.getColumnLabel(columnIndex),     rs.getString(metaData.getColumnName(columnIndex)));
                        else
                            columnMap.put(metaData.getColumnLabel(columnIndex), "");
                    }
                    list.add(columnMap);
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
            json.put(keyName, list);
        }
        return JSONValue.toJSONString(json);
    }
    
    public String Error(){
        return Error;
    }
    
    public void Close() throws SQLException{
        connection.close();
    }
}
