package org.example;
import org.example.pc.Computador;
import org.springframework.jdbc.core.JdbcTemplate;
//import  com.microsoft.sqlserver.jdbc.SQLServerDriver;


public abstract class Conexao {



    private JdbcTemplate conexaoDoBanco;
    private String url;
    private String username;
    private String password;
    private Usuario userAtual = null;


    public abstract Usuario buscarCredenciais(String email, String senha);

    public abstract Computador computadorExiste(Integer vez, Boolean integer, Usuario user);

    public abstract void executarQuery(String query);

}
