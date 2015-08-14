package conexion;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;


public class ConexionMysql {
    public String nameDB = "saedpracticas";
    public String login = "siwapp";
    public String password = "siwapp";
    //public String url = "jdbc:mysql://192.168.53.159:3306/"+nameDB;
    public String url = "jdbc:mysql://192.168.0.39:3306/"+nameDB;
    public Connection Link = null;

    public boolean conectar()
    {
        try
        {
            
            // Class.forName("org.gjt.mm.mysql.Driver");
            Class.forName("com.mysql.jdbc.Driver");
            this.Link = DriverManager.getConnection(this.url, this.login, this.password);
            if(this.Link != null)
            {
                return true;
            }
        }
        catch(Exception ex)
        {
            System.out.println("ERROR DE CONECCION A: "+this.url);
        }

        return false;
    }
    public void desconectar()
    {
     //   this.Link = null;
        try {
            this.Link.close();
        } catch (SQLException e) {
            System.out.println("ERROR Close: "+e);
        }
    }
    public Connection GetConection()
    {
        return this.Link;
    }
    public boolean Ejecutar(String SQL)
    {
        try
        {
            PreparedStatement Query = this.GetConection().prepareStatement(SQL);
            Query.execute();
            return true;
        }
        catch(SQLException ex)
        {
            System.out.println("ERROR: "+ex);
            return false;
        }

    }
    public ResultSet Consultar(String SQL)
    {
        try
        {
            PreparedStatement Query = this.GetConection().prepareStatement(SQL);
            ResultSet Resultado = Query.executeQuery();

            return Resultado;
        }
        catch(SQLException ex)
        {
            System.out.println("ERROR: "+ex);
            return null;
        }

    }
  /*  public static void main(String[] args) throws SQLException
    {
        Test test = new Test();

        System.out.println("Conectado: "+test.conectar());

        System.out.println("Insercionde datos: "+test.Ejecutar("INSERT INTO Movie(Nombre,Trailer,Trama,Caratula,Tipo) VALUE('Epic: El reino secreto', 'http://youtube.com/?v=W31R5-7d_q', 'Un grupo de bichos intentan salvar un jardín...','http://caratula.net/img/lerh.jpg','Pelicula');"));
        System.out.println("Insercionde datos: "+test.Ejecutar("INSERT INTO Movie(Nombre,Trailer,Trama,Caratula,Tipo) VALUE('El cosmonauta', 'http://youtube.com/?v=g31R5-7d_q', 'En 1975, el primer cosmonauta ruso que llegó a la Luna no consiguió regresar...','http://caratula.net/img/hjtf.jpg','Pelicula');"));

        ResultSet Resultado = test.Consultar("SELECT COUNT(*) cantidad FROM Movie;");
        if(Resultado.next())
        {
            System.out.println("LA CANTIDAD DE DATOS ES: "+Resultado.getInt("cantidad"));
        }

        Resultado = test.Consultar("SELECT * FROM Movie;");
        while(Resultado.next())
        {
            System.out.println("\nNOMBRE: "+Resultado.getString("Nombre")+" ("+Resultado.getString("Tipo")+")");
            System.out.println("SINOPSIS: "+Resultado.getString("Trama"));
        }
    }*/
}