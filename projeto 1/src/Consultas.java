import java.sql.Connection;
import java.sql.ResultSet;


public class Consultas {
	public static void main(String args[]){
		Qtde_ocorrencia_delegacia("A NOITE");
		BOs_circunscricao("38º D.P. VILA AMALIA");
		Qtde_Homicidio_Cidade("simples");
		Qtde_Roubo_Delegacia("TRANSEUNTE");
		Atos_Cidade("S.PAULO", "Ato infracional");
	}

	
	// BOs de certa circunscricao
	public static void BOs_circunscricao(String circunscricao) {
		try {
			Connection Conexao = Conectadb.getConnection();
			
			// Prepared Statement
			java.sql.PreparedStatement pstmt = null;
			
			String SQL = "SELECT B.NUM_BO, B.ANO_BO, B.SOLUCAO, D.NOME AS NOME_DELEGACIA, D.CIRCUNSCRICAO FROM BO B INNER JOIN DELEGACIA D " +
					"ON B.DELEGACIA = D.ID_DELEGACIA WHERE D.CIRCUNSCRICAO = ? ORDER BY B.NUM_BO";
			
			// Create PrepareStatement object
		     pstmt = Conexao.prepareStatement(SQL);
		     
		    // Set auto-commit to false
		    Conexao.setAutoCommit(false);
		    
		    // Set the variables
		    pstmt.setString(1, circunscricao);
		    
		    ResultSet result = pstmt.executeQuery();
		    
		    while(result.next()){
                int NUM_BO = result.getInt("B.NUM_BO");
                int ANO_BO = result.getInt("B.ANO_BO");
                String SOLUCAO = result.getString("B.SOLUCAO");
                String NOME_DELEGACIA = result.getString("NOME_DELEGACIA");
                String CIRCUNSCRICAO = result.getString("D.CIRCUNSCRICAO");
                
                
                
                System.out.println("Numero do BO: " + NUM_BO);
                System.out.println("Ano do BO :" + ANO_BO);
                System.out.println("Solucao do BO :" + SOLUCAO);
                System.out.println("Nome da delegacia :" + NOME_DELEGACIA);
                System.out.println("Circunscricao da delegacia :" + CIRCUNSCRICAO);
                System.out.println("-------------------------------------------");                    
            }
		    
		    Conexao.close();
			
		} catch (Exception Excecao) {
			System.out.println("SQLException:" + Excecao.getLocalizedMessage());
            System.out.println("Problemas ao tentar conectar com o banco de dados: " + Excecao);
		}
	}
	
	// Quantidade de ocorrencias em um certo periodo por delegacia
	public static void Qtde_ocorrencia_delegacia(String periodo) {
		try {
			Connection Conexao = Conectadb.getConnection();
			
			// Prepared Statement
			java.sql.PreparedStatement pstmt = null;
			
			String SQL = "SELECT BD.NOME AS NOME_DELEGACIA, COUNT(O.PERIODO_OCORRENCIA) AS QTDE_OCORRENCIA FROM (SELECT B.ID_BO, D.NOME FROM BO B INNER JOIN " +
					"DELEGACIA D ON D.ID_DELEGACIA = B.DELEGACIA) BD INNER JOIN OCORRENCIA O ON BD.ID_BO = BOLETIM_OCORRENCIA WHERE O.PERIODO_OCORRENCIA = ? " + 
					"GROUP BY O.PERIODO_OCORRENCIA, BD.NOME ORDER BY QTDE_OCORRENCIA DESC";
			
			// Create PrepareStatement object
		     pstmt = Conexao.prepareStatement(SQL);
		     
		    // Set auto-commit to false
		    Conexao.setAutoCommit(false);
		    
		    // Set the variables
		    pstmt.setString(1, periodo);
		    
		    ResultSet result = pstmt.executeQuery();
		    
		    while(result.next()){
                String NOME_DELEGACIA = result.getString("NOME_DELEGACIA");
                int QTDE_OCORRENCIA = result.getInt("QTDE_OCORRENCIA");
                
                System.out.println("Delegacia: " + NOME_DELEGACIA);
                System.out.println("Quantidade de ocorrencias " + periodo.toLowerCase() + ": " + QTDE_OCORRENCIA);
                System.out.println("-------------------------------------------");                    
            }
		    
		    Conexao.close();
			
		} catch(Exception Excecao) {
			System.out.println("SQLException:" + Excecao.getLocalizedMessage());
            System.out.println("Problemas ao tentar conectar com o banco de dados: " + Excecao);
		}
	}
	
	// Quantidade de homicidios de um certo tipo por cidade
	public static void Qtde_Homicidio_Cidade(String tipo_homicidio) {
		try {
			Connection Conexao = Conectadb.getConnection();
			
			// Prepared Statement
			java.sql.PreparedStatement pstmt = null;
			
			String SQL = "SELECT COUNT(OH.TIPO_HOMICIDIO) AS QNTD_HOM, E.CIDADE FROM ((SELECT H.TIPO_HOMICIDIO, O.ID_OCORRENCIA, O.ENDERECO_OCORRENCIA " +
					"FROM HOMICIDIO H INNER JOIN OCORRENCIA O ON O.ID_OCORRENCIA = H.ID_OCORRENCIA) OH INNER JOIN ENDERECO E ON " +
					"OH.ENDERECO_OCORRENCIA = E.ID_ENDERECO ) WHERE OH.TIPO_HOMICIDIO = ? GROUP BY E.CIDADE ORDER BY QNTD_HOM DESC";
			
			// Create PrepareStatement object
		     pstmt = Conexao.prepareStatement(SQL);
		     
		    // Set auto-commit to false
		    Conexao.setAutoCommit(false);
		    
		    // Set the variables
		    pstmt.setString(1, tipo_homicidio);
		    
		    ResultSet result = pstmt.executeQuery();
		    
		    while(result.next()){
                int QNTD_HOM = result.getInt("QNTD_HOM");
                String CIDADE = result.getString("CIDADE");
                
                System.out.println("Cidade: " + CIDADE);
                System.out.println("Quantidade de homicidios " + tipo_homicidio.toLowerCase() + ": " + QNTD_HOM);
                System.out.println("-------------------------------------------");                    
            }
		    
		    Conexao.close();
			
		} catch(Exception Excecao) {
			System.out.println("SQLException:" + Excecao.getLocalizedMessage());
            System.out.println("Problemas ao tentar conectar com o banco de dados: " + Excecao);
		}
	}
	
	public static void Qtde_Roubo_Delegacia(String objeto) {
		try {
			Connection Conexao = Conectadb.getConnection();
			
			// Prepared Statement
			java.sql.PreparedStatement pstmt = null;
			
			String SQL = "SELECT COUNT(ORO.TIPO_OBJETO) AS FOI_ROUBADO, BD.NOME, BD.CIRCUNSCRICAO FROM (SELECT OC.BOLETIM_OCORRENCIA, " +
					"OBR.TIPO_OBJETO FROM (SELECT R.ID_OCORRENCIA, OB.TIPO_OBJETO FROM OBJETO OB INNER JOIN ROUBO R ON OB.ID_OBJETO = R.OBJETO_ID) OBR " +
					"INNER JOIN OCORRENCIA OC ON OC.ID_OCORRENCIA = OBR.ID_OCORRENCIA) ORO  INNER JOIN " +
					"(SELECT D.NOME, D.CIRCUNSCRICAO, B.ID_BO FROM BO B INNER JOIN DELEGACIA D ON B.DELEGACIA = D.ID_DELEGACIA) BD " + 
					"ON ORO.BOLETIM_OCORRENCIA = BD.ID_BO WHERE ORO.TIPO_OBJETO = ? GROUP BY BD.NOME, BD.CIRCUNSCRICAO ORDER BY FOI_ROUBADO DESC";
			
			// Create PrepareStatement object
		     pstmt = Conexao.prepareStatement(SQL);
		     
		    // Set auto-commit to false
		    Conexao.setAutoCommit(false);
		    
		    // Set the variables
		    pstmt.setString(1, objeto);
		    
		    ResultSet result = pstmt.executeQuery();
		    
		    while(result.next()){
                int FOI_ROUBADO = result.getInt("FOI_ROUBADO");
                String NOME = result.getString("NOME");
                String CIRCUNSCRICAO = result.getString("CIRCUNSCRICAO");
                
                System.out.println("Delegacia: " + NOME);
                System.out.println("Circunscricao: " + CIRCUNSCRICAO);
                System.out.println("Quantidade de roubos " + objeto.toLowerCase() + ": " + FOI_ROUBADO);
                System.out.println("-------------------------------------------");                    
            }
		    
		    Conexao.close();
			
		} catch(Exception Excecao) {
			System.out.println("SQLException:" + Excecao.getLocalizedMessage());
            System.out.println("Problemas ao tentar conectar com o banco de dados: " + Excecao);
		}
	}
	
	
	public static void Atos_Cidade(String Cidade, String Especie) {
		try {
			Connection Conexao = Conectadb.getConnection();
			
			// Prepared Statement
			java.sql.PreparedStatement pstmt = null;
			
			String SQL = "SELECT OB.ID_BO, OB.ANO_BO, OB.ESPECIE, E.CIDADE, OB.DATA_INICIADO AS BO_INICIADO " +
			 "FROM (SELECT B.DATA_INICIADO, O.ESPECIE, O.ENDERECO_OCORRENCIA, B.ID_BO, B.ANO_BO FROM OCORRENCIA O " +
			 "INNER JOIN BO B ON O.BOLETIM_OCORRENCIA = B.ID_BO) OB INNER JOIN ENDERECO E ON (OB.ENDERECO_OCORRENCIA = E.ID_ENDERECO) " +
			 "WHERE E.CIDADE = ? AND OB.ESPECIE = ? ORDER BY OB.DATA_INICIADO";
			
			// Create PrepareStatement object
		     pstmt = Conexao.prepareStatement(SQL);
		     
		    // Set auto-commit to false
		    Conexao.setAutoCommit(false);
		    
		    // Set the variables
		    pstmt.setString(1, Cidade);
		    pstmt.setString(2, Especie);
		    
		    ResultSet result = pstmt.executeQuery();
		    
		    while(result.next()){
                int ID_BO = result.getInt("ID_BO");
                int ANO_BO = result.getInt("ANO_BO");
                String ESPECIE = result.getString("ESPECIE");
                String CIDADE = result.getString("CIDADE");
                String BO_INICIADO = result.getString("BO_INICIADO");
                
                System.out.println("ID_BO: " + ID_BO);
                System.out.println("ANO_BO: " + ID_BO);
                System.out.println("ESPECIE: " + ESPECIE);
                System.out.println("CIDADE: " + CIDADE);
                System.out.println("BO_INICIADO: " + BO_INICIADO);
                System.out.println("-------------------------------------------");                    
            }
		    
		    Conexao.close();
			
		} catch(Exception Excecao) {
			System.out.println("SQLException:" + Excecao.getLocalizedMessage());
            System.out.println("Problemas ao tentar conectar com o banco de dados: " + Excecao);
		}
	}
	
}
