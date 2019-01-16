import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Mysql {

	public static void main(String[] args) throws SQLException {
		// TODO Auto-generated method stub
		
Cone ok=new Cone();
ok.MySQLConnect();
String tabla = "nombre";
String Query = "SELECT * FROM " + tabla;
ok.comando = ok.conexion.createStatement();
try {
ok.registro = ok.comando.executeQuery(Query);

while (ok.registro.next()) {
String a=ok.registro.getString(1);

System.out.println(a);
    System.out.println("Nombre: " + ok.registro.getString(1));

    System.out.println("------------------------------------------");
}
	}  catch (SQLException ex) {
        Logger.getLogger(Cone.class.getName()).log(Level.SEVERE, null, ex);
    }

	}
}