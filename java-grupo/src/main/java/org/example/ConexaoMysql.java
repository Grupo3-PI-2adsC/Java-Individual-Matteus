package org.example;

import com.github.britooo.looca.api.core.Looca;
import org.apache.commons.dbcp2.BasicDataSource;
import org.example.pc.Computador;
import org.example.ConexaoMysql;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConexaoMysql extends Conexao {


    private JdbcTemplate conexaoDoBanco;
    private String url;
    private String username;
    private String password;
    private Usuario userAtual = null;
    private Connection connectionn;


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

    public Connection getConnectionn() {
        return connectionn;
    }

    public ConexaoMysql() {
        BasicDataSource dataSource = new BasicDataSource();

        this.url = "jdbc:mysql://localhost:3306/netmed"; //mysql-container
        this.username = "Netmed";
        this.password = "Netmed#1@@";
        configurarDataSource();
//        try{
//        dataSource.setDriverClassName("com.mysql.cj.jdbc.Driver");
//        dataSource.setUrl(url);
//        dataSource.setUsername(username);
//        dataSource.setPassword(password);
//        this.conexaoDoBanco = new JdbcTemplate(dataSource);
//        } catch (Exception e) {
//            System.err.println("Falha ao conectar ao banco de dados:");
//            e.printStackTrace();
//        }


        /*
             Exemplo de string de conexões:
                jdbc:mysql://localhost:3306/mydb <- EXEMPLO PARA MYSQL
                jdbc:sqlserver://localhost:1433;database=mydb <- EXEMPLO PARA SQL SERVER
        */


    }

    private void configurarDataSource() {
        try {
            BasicDataSource dataSource = new BasicDataSource();
            dataSource.setDriverClassName("com.mysql.cj.jdbc.Driver");
            dataSource.setUrl(url);
            dataSource.setUsername(username);
            dataSource.setPassword(password);
            this.conexaoDoBanco = new JdbcTemplate(dataSource);
        } catch (Exception e) {
            System.err.println("Falha ao conectar ao banco de dados:");
            e.printStackTrace();
        }
    }

    @Override
    public Usuario buscarCredenciais(String email, String senha) {
        System.out.println("""
                |
                |
              MYSQL
                |
                |""");

        JdbcTemplate con = conexaoDoBanco;

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

        System.out.println(userAtual);
        return userAtual;
    }

    @Override
    public Computador computadorExiste(Integer vez, Boolean servidor, Usuario usuario) {
        userAtual = usuario;
        System.out.println("""
                |
                |
              MYSQL
                |
                |""");
        Computador computadorMonitorado;

        System.out.println("""
                            Verificando se o computador já existe na nossa base de dados
                            .............................................................""");

        ConexaoMysql conexao = new ConexaoMysql();
        JdbcTemplate con = conexao.getConexaoDoBanco();


        Looca teste = new Looca();
        String hostname = teste.getRede().getParametros().getHostName();
        Integer arquitetura = teste.getSistema().getArquitetura();
        RowMapper<Computador> computadorRowMapper = (resultSet, i) ->new Computador(resultSet.getString("idMaquina"), resultSet.getString("hostname"),
                resultSet.getBoolean("ativo"), resultSet.getInt("fkEmpresa"));

        String sql = String.format("SELECT idMaquina, hostname, ativo, fkEmpresa FROM maquina WHERE hostName = '%s';", hostname);

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
              MYSQL
                |
                |""");
        System.out.println("Exec");

        System.out.println("""
                            Executando query:
                            %s
                            ........................""".formatted(query));


        JdbcTemplate con = conexaoDoBanco;


        con.execute(query);
    }

//    public void criarBanco(){
//
//        JdbcTemplate con = conexaoDoBanco;
////
//        con.execute("""
//
//                CREATE DATABASE IF NOT EXISTS netmed;
//                """);
//
//        con.execute("""
//                USE netmed;
//                    """);
//
//        con.execute("""
//                CREATE TABLE IF NOT EXISTS empresa (
//                            idEmpresa INT AUTO_INCREMENT PRIMARY KEY,
//                            nomeFantasia VARCHAR(100),
//                    razaoSocial VARCHAR(100),
//                    apelido VARCHAR(60),
//                    cnpj CHAR(14)
//                                );""");
//
//        con.execute("""
//
//                    insert into empresa values
//                            (null, 'Arcos Dourados', 'Fast Food', 'EME GIGANTE', '12345678901234');""");
//
//        con.execute("""
//
//
//                    CREATE TABLE IF NOT EXISTS endereco (
//                            idEndereco INT AUTO_INCREMENT PRIMARY KEY,
//                            cep CHAR(8),
//                    rua VARCHAR(60),
//                    bairro VARCHAR(60),
//                    cidade VARCHAR(60),
//                    unidadeFederativa VARCHAR(60),
//                    numero int,
//                    complemento VARCHAR(90),
//                    atual BOOLEAN,
//                    fkEmpresa INT,
//                    FOREIGN KEY (fkEmpresa) REFERENCES empresa(idEmpresa)
//                            );""");
//
//        con.execute("""
//
//
//
//                    CREATE TABLE IF NOT EXISTS usuario (
//                            idUsuario INT AUTO_INCREMENT PRIMARY KEY,
//                            tipoUsuario VARCHAR(45),
//                    nome VARCHAR(100),
//                    email VARCHAR(100) UNIQUE,
//                    senha VARCHAR(30),
//                    ativo Boolean,
//                    fkEmpresa INT,
//                    FOREIGN KEY (fkEmpresa) REFERENCES empresa(idEmpresa)
//                            );""");
//
//        con.execute("""
//
//
//                    insert into usuario values
//                            (null, 'recepção', 'Raimunda', 'raimunda@netmet.com' ,'1234', 1, 1);""");
//
//
//        con.execute("""
//
//
//                    CREATE TABLE IF NOT EXISTS manuais (
//                            idManual INT AUTO_INCREMENT PRIMARY KEY,
//                            tituloManual VARCHAR(45),
//                    descricaoManual VARCHAR(100),
//                    usuárioUltimaAlteracao VARCHAR(100) UNIQUE,
//                    dtUltimaAlteracao DATE,
//                    fkUsuarioCriador INT,
//                    dtCriacao DATE,
//                    FOREIGN KEY (fkUsuarioCriador) REFERENCES usuario(idUsuario)
//                            );""");
//
//
//        con.execute("""
//
//
//                    CREATE TABLE IF NOT EXISTS maquina (
//                            idMaquina INT AUTO_INCREMENT PRIMARY KEY,
//                            hostName VARCHAR(45) unique,
//                    ativo Boolean,
//                    arquitetura int,
//                    validado boolean,
//                    fkEmpresa INT,
//                    FOREIGN KEY (fkEmpresa) REFERENCES empresa(idEmpresa)
//                            );""");
//
//        con.execute("""
//
//
//                    CREATE TABLE IF NOT EXISTS servicos(
//                            PID int,
//                            nome VARCHAR(50),
//                    estado boolean,
//                    fkMaquina int,
//                    foreign key (fkMaquina) references maquina(idMaquina),
//                    primary key (PID, fkMaquina)
//                                );""");
//
//        con.execute("""
//
//
//                    CREATE TABLE IF NOT EXISTS tipoComponente (
//                            idTipoComponente INT primary key,
//                            nomeComponente VARCHAR(45),
//                    unidadeMedida VARCHAR(10),
//                    metricaEstabelecida DOUBLE
//                                );""");
//
//        con.execute("""
//
//                    INSERT INTO tipoComponente VALUES
//                            (1, 'Sistema operacional', 'bits', null),
//                                    (2, 'Memoria', 'GiB', 70.0),
//                                            (3, 'Processador', 'GHz', 2.0),
//                                            (4, 'Rede', 'Pacotes', 500000.0),
//                                            (5, 'Disco ', 'Gib', 7988696064.0),
//                                            (6, 'Volume  ', 'Gib', null),
//                                            (7, 'Serviços   ', '%', null);""");
//
//        con.execute("""
//
//
//                    CREATE TABLE IF NOT EXISTS dadosFixos (
//                            idDadosFixos INT NOT NULL auto_increment,
//                            fkMaquina INT,
//                            fkTipoComponente INT,
//                            nomeCampo VARCHAR(45),
//                    valorCampo VARCHAR(150),
//                    descricao varchar(200),
//                    PRIMARY KEY (idDadosFixos, fkMaquina, fkTipoComponente),
//                    FOREIGN KEY (fkMaquina) REFERENCES maquina(idMaquina),
//                    FOREIGN KEY (fkTipoComponente) REFERENCES tipoComponente(idTipoComponente)
//                            );""");
//
//        con.execute("""
//
//
//
//                    CREATE TABLE IF NOT EXISTS dadosTempoReal (
//                            idDadosTempoReal INT AUTO_INCREMENT,
//                            fkDadosFixos INT,
//                            fkMaquina INT,
//                            fkTipoComponente INT,
//                            dataHora DATETIME,
//                            nomeCampo VARCHAR(45),
//                    valorCampo VARCHAR(150),
//                    PRIMARY KEY  (idDadosTempoReal, fkDadosFixos, fkMaquina, fkTipoComponente),
//                    FOREIGN KEY (fkDadosFixos) REFERENCES dadosFixos(idDadosFixos),
//                    FOREIGN KEY (fkTipoComponente) REFERENCES tipoComponente(idTipoComponente),
//                    FOREIGN KEY (fkMaquina) REFERENCES maquina(idMaquina)
//                            );""");
//
//        con.execute("""
//
//
//                    CREATE TABLE IF NOT EXISTS fixosRede (
//                            idFixosRede INT AUTO_INCREMENT,
//                            fkMaquina INT,
//                            nomeCampo VARCHAR(45),
//                    valorCampo VARCHAR(255),
//                    PRIMARY KEY (idFixosRede, fkMaquina),
//                    FOREIGN KEY (fkMaquina) REFERENCES maquina(idMaquina)
//                            );""");
//
//        con.execute("""
//
//
//                    CREATE TABLE IF NOT EXISTS variaveisRede (
//                            idVariaveisRede INT AUTO_INCREMENT,
//                            fkFixosRede INT,
//                            fkMaquina INT,
//                            dataHora DATETIME,
//                            nomeCampo VARCHAR(45),
//                    valorCampo VARCHAR(45),
//                    PRIMARY KEY (idVariaveisRede, fkFixosRede, fkMaquina),
//                    FOREIGN KEY (fkFixosRede) REFERENCES fixosRede(idFixosRede),
//                    FOREIGN KEY (fkMaquina) REFERENCES maquina(idMaquina)
//                            );""");
//
//
//    }

}
