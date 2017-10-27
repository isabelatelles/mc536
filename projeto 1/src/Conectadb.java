import java.sql.*;
import javax.swing.JOptionPane;

public class Conectadb {   
    public static Connection getConnection() {  
        String EnderecoDB = "jdbc:mysql://localhost:3306/INFO_CRIMINAIS_SP";
        String usuario = "root";  
        String senha = "SENHA";
        java.sql.Connection Conexao = null;
        
          try {   
              Class.forName("com.mysql.jdbc.Driver");
              Conexao = DriverManager.getConnection(EnderecoDB,usuario,senha);
              return  Conexao; 
          } catch (Exception e) {   
                 JOptionPane.showMessageDialog(null, e, "ERRO", JOptionPane.ERROR_MESSAGE);    
          }  
         return  Conexao;       
    }   
    
}