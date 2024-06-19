package org.example;

import com.github.britooo.looca.api.core.Looca;
import org.apache.commons.dbcp2.BasicDataSource;
import org.example.pc.Computador;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

public class ConexaoSqlserver extends Conexao {


    private JdbcTemplate conexaoDoBanco;
    private String url;
    private String username;
    private String password;
    private Usuario userAtual = null;

    public String getUrl() {
        return url;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public Usuario getUserAtual() {
        return userAtual;
    }

    public JdbcTemplate getConexaoDoBanco() {
        return conexaoDoBanco;
    }


    public ConexaoSqlserver() {
        BasicDataSource dataSource = new BasicDataSource();
        this.url = "jdbc:sqlserver://184.73.201.251:1433;databaseName=netmed;trustServerCertificate=true";
        this.username = "sa";
        this.password = "urubu100";


        dataSource.setDriverClassName("com.microsoft.sqlserver.jdbc.SQLServerDriver");


        dataSource.setUrl(url);
        dataSource.setUsername(username);
        dataSource.setPassword(password);

        this.conexaoDoBanco = new JdbcTemplate(dataSource);

    }

    @Override
    public Usuario buscarCredenciais(String email, String senha) {
        System.out.println("""
                |
                |
              SERVER
                |
                |""");
        ConexaoSqlserver conexao = new ConexaoSqlserver();
        JdbcTemplate con = conexao.getConexaoDoBanco();

//        RowMapper<Usuario> userTesteRowMapper = (rs, rowNum) -> new Usuario(rs.getInt("id"),
//                rs.getString("email"), rs.getString("senha"));)
        RowMapper<Usuario> usuarioRowMapper = (resultSet, i) ->new Usuario(
                resultSet.getInt("idUsuario"), resultSet.getString("tipoUsuario"), resultSet.getString("nome"),
                resultSet.getString("email"), resultSet.getString("senha"), resultSet.getInt("fkEmpresa"));

        String sql = "select * from usuario where email = '%s' and senha = '%s';".formatted(email,senha);

        System.out.println("""
                    Executando a query:
                    %s
                    ...........................................""".formatted(sql));

        userAtual = con.queryForObject(sql,
                usuarioRowMapper);


        return userAtual;
    }

    @Override
    public Computador computadorExiste(Integer vez, Boolean servidor, Usuario usuario) {
        userAtual = usuario;
        System.out.println("""
                |
                |
              SERVER
                |
                |""");
        Computador computadorMonitorado;

        System.out.println("""
                            Verificando se o computador já existe na nossa base de dados
                            .............................................................""");

        ConexaoSqlserver conexao = new ConexaoSqlserver();
        JdbcTemplate con = conexao.getConexaoDoBanco();


        Looca teste = new Looca();
        String hostname = teste.getRede().getParametros().getHostName();
        Integer arquitetura = teste.getSistema().getArquitetura();
        RowMapper<Computador> computadorRowMapper = (resultSet, i) ->new Computador(resultSet.getString("idMaquina"), resultSet.getString("hostname"),
                resultSet.getBoolean("ativo"), resultSet.getInt("fkEmpresa"));

        String sql = "select * from maquina where idMaquina = '%s';".formatted(hostname);

        try {

            System.out.println("""
                    Executando a query:
                    %s
                    ...........................................""".formatted(sql));

            computadorMonitorado = con.queryForObject(sql,
                    computadorRowMapper);
//        System.out.println(computadorMonitorado + " asdasd");
            System.out.println("""
                    A Maquina em execução existe na base de dados
                    ...............................................""");

            String sqlAtivo = "update maquina set ativo = 1 where idMaquina = '%s';".formatted(computadorMonitorado.getIdMaquina());
            conexao.executarQuery(sqlAtivo);
//            Computador já tem cadastrado os componentes
            System.out.println("vez:" + vez);
            if (vez == 1){

                computadorMonitorado.atualizarFixos(servidor);

                return computadorMonitorado;
            }else if (vez == 0){

                System.out.println("""
                Cadastrando os Componentes fixos do computador
                ............................................""");
//                o computador acabou de ser cadastrado e ainda não possui componente

                computadorMonitorado.cadastrarFixos(servidor);
                System.out.println("|Dados fixos cadastrados|");

                Computador computadorMonitorado2 = con.queryForObject(sql,
                        computadorRowMapper);

                System.out.println(computadorMonitorado2);
                return computadorMonitorado2;
//                computadorMonitorado.buscarInfos(1, servidor);
            }
        }catch (Exception erro) {
            System.out.println(erro);
            if (erro.getCause() == null) {

                System.out.println("""
                                    Computador não existe. Vamos cadastralo agora
                                    ..............................................""");

                String sqlMaquina = "INSERT INTO maquina VALUES ('%s', '%s', %d, %d, %d, %d);".formatted(
                        hostname,
                        hostname,
                        1,
                        arquitetura,
                        0,
                        userAtual.getFkEmpresa()
                );

                try {
                    con.execute(sqlMaquina);
                    System.out.println("""
                                        Computador Cadastrado com sucesso
                                        ...................................""");

                    computadorMonitorado = con.queryForObject(sql, computadorRowMapper);
                    System.out.println("Computador monitorado após cadastro: " + computadorMonitorado);

                    return this.computadorExiste(0, servidor, userAtual);

                } catch (Exception erro2) {
                    System.out.println("""
                                          Erro: %s
                                          Causa do erro: %s
                                         .....................""".formatted(erro2, erro2.getCause()));
                }
            }
        }
            return null;
    }

    @Override
    public void executarQuery(String query) {
        System.out.println("""
                |
                |
              SERVER
                |
                |""");
        System.out.println("Exec");

        System.out.println("""
                            Executando query:
                            %s
                            ........................""".formatted(query));

        ConexaoSqlserver conexao = new ConexaoSqlserver();
        JdbcTemplate con = conexao.getConexaoDoBanco();


        con.execute(query);
    }
}
