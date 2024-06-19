package org.example.pc.componentes;

import com.github.britooo.looca.api.core.Looca;
import com.github.britooo.looca.api.group.discos.Disco;
import org.example.Conexao;
import org.example.ConexaoMysql;
import org.example.ConexaoSqlserver;
import org.springframework.jdbc.core.JdbcTemplate;

public class DiscoCp extends Componente {

    private Integer idDadoFixo;

    private Integer fkTipoComponente;
    private String nomeCampo;
    private String valorCampo;
    private String descricao;


    public DiscoCp(String fkMaquina) {
        super(fkMaquina);
    }

    public DiscoCp(Integer idDadoFixo, String fkMaquina, Integer fkTipoComponente, String nomeCampo, String valorCampo, String descricao) {
        super(fkMaquina);
        this.idDadoFixo = idDadoFixo;
        this.fkTipoComponente =  fkTipoComponente;
        this.nomeCampo = nomeCampo;
        this.valorCampo =  valorCampo;
        this.descricao =  descricao;
    }

    @Override
    public void buscarInfosFixos(Boolean integer) {



        Looca looca = new Looca();

        Integer qtdDiscosDisco = looca.getGrupoDeDiscos().getQuantidadeDeDiscos();


        String queryDisco2 = """
                     INSERT INTO dadosFixos(
                    fkMaquina
                    ,fkTipoComponente
                    ,nomeCampo
                    ,valorCampo
                    ,descricao)  VALUES
                    ( '%s', 5, 'Quantidade de disco no computador', '%s', 'Quantidade de disco no computador');
                """ .formatted(
                fkMaquina,
                qtdDiscosDisco);


        if (integer == true){
            ConexaoSqlserver con1 = new ConexaoSqlserver();
            con1.executarQuery(queryDisco2);
        }else {
            ConexaoMysql con =  new ConexaoMysql();
            con.executarQuery(queryDisco2);
        }



        for (int i = 0; i < qtdDiscosDisco; i++) {


            //            DISCO
            String nomeDisco = looca.getGrupoDeDiscos().getDiscos().get(i).getNome();
            Long tamanhoDisco = (looca.getGrupoDeDiscos().getDiscos().get(i).getTamanho() / 1000000000);

            String queryDisco = """
                        
                     INSERT INTO dadosFixos(
                    fkMaquina
                    ,fkTipoComponente
                    ,nomeCampo
                    ,valorCampo
                    ,descricao)  
                    VALUES
                        ( '%s', 5, 'Nome do disco', '%s', 'Nome do disco'),
                        ( '%s', 5, 'tamanho do disco', '%s', 'tamanho do disco')
                    """.formatted(
                    fkMaquina,
                    nomeDisco,
                    fkMaquina,
                    tamanhoDisco
            );

            if (integer == true){
                ConexaoSqlserver con1 = new ConexaoSqlserver();
                con1.executarQuery(queryDisco);
            }
            else{
                ConexaoMysql con =  new ConexaoMysql();
                con.executarQuery(queryDisco);
            }
        }



    }

    @Override
    public void buscarInfosVariaveis(Boolean teste) {


        try {

            String queryFk = """
                    select * from dadosFixos where fkMaquina = '%s' and fkTipoComponente = 5 and nomeCampo = 'Nome do disco'""".formatted(fkMaquina);
            System.out.println(queryFk);
            if(teste) {
                ConexaoSqlserver conexao1 = new ConexaoSqlserver();
                JdbcTemplate con1 = conexao1.getConexaoDoBanco();
                con1.query(queryFk, (resultSet) -> {
                    Integer fk = resultSet.getInt("idDadosFixos");
                    System.out.println(fk);
                    Looca looca = new Looca();

                    Long leiturasDisco = (looca.getGrupoDeDiscos().getDiscos().get(0).getLeituras());
                    Long escritarDisco = (looca.getGrupoDeDiscos().getDiscos().get(0).getEscritas());

                    var queryDisco = """
                                 iNSERT INTO dadosTempoReal(
                                  fkDadosFixos
                                  ,fkMaquina
                                  ,fkTipoComponente
                                  ,dataHora
                                  ,nomeCampo
                                  ,valorCampo)VALUES
                                    (%d, '%s', 5, current_timestamp,'Leituras', '%s'),
                                                        (%d, '%s', 5, current_timestamp,'Leituras', '%s');
                            """.formatted(
                            fk,
                            fkMaquina,
                            leiturasDisco,
                            fk,
                            fkMaquina,
                            escritarDisco
                    );
//                System.out.println(queryDisco);
                    conexao1.executarQuery(queryDisco);


                    while (resultSet.next()) {
//                        String fk = resultSet.getString("valorCampo");

                        for (Disco discoAtual : looca.getGrupoDeDiscos().getDiscos()) {
                            if (discoAtual.getNome().equals(fk)) {

                                var queryDisco2 = """
                                                 iNSERT INTO dadosTempoReal(
                                                  fkDadosFixos
                                                  ,fkMaquina
                                                  ,fkTipoComponente
                                                  ,dataHora
                                                  ,nomeCampo
                                                  ,valorCampo)  VALUES
                                                  ( %d, '%s', 5, current_timestamp,'Leituras', '%s'),
                                                                    ( %d, '%s', 5, current_timestamp,'Leituras', '%s');
                                        """.formatted(
                                        fk,
                                        fkMaquina,
                                        leiturasDisco,
                                        fk,
                                        fkMaquina,
                                        escritarDisco
                                );
                                conexao1.executarQuery(queryDisco2);
                                break;
                            }
                        }
                        ;
                    }


                });
            }else {

                ConexaoMysql conexao = new ConexaoMysql();
                JdbcTemplate con = conexao.getConexaoDoBanco();
                con.query(queryFk, (resultSet) -> {
                    Integer fk = resultSet.getInt("idDadosFixos");
                    System.out.println(fk);
                    Looca looca = new Looca();

                    Long leiturasDisco = (looca.getGrupoDeDiscos().getDiscos().get(0).getLeituras());
                    Long escritarDisco = (looca.getGrupoDeDiscos().getDiscos().get(0).getEscritas());

                    var queryDisco = """
                            iNSERT INTO dadosTempoReal(
                             fkDadosFixos
                             ,fkMaquina
                             ,fkTipoComponente
                             ,dataHora
                             ,nomeCampo
                             ,valorCampo)
                                                       VALUES  ( %d, '%s', 5, current_timestamp(),'Leituras', '%s'),
                                                               ( %d, '%s', 5, current_timestamp(),'Escritas', '%s');
                                   """.formatted(
                            fk,
                            fkMaquina,
                            leiturasDisco,
                            fk,
                            fkMaquina,
                            escritarDisco
                    );
//                System.out.println(queryDisco);
                    conexao.executarQuery(queryDisco);



                    while (resultSet.next()){
//                        String fk = resultSet.getString("valorCampo");

                        for (Disco discoAtual : looca.getGrupoDeDiscos().getDiscos()){
                            if (discoAtual.getNome().equals(fk)){

                                var queryDisco2 = """
                            iNSERT INTO dadosTempoReal(
                             fkDadosFixos
                             ,fkMaquina
                             ,fkTipoComponente
                             ,dataHora
                             ,nomeCampo
                             ,valorCampo)
                                                       VALUES  ( %d, '%s', 5, current_timestamp(),'Leituras', '%s'),
                                                               ( %d, '%s', 5, current_timestamp(),'Escritas', '%s');
                                           """.formatted(
                                        fk,
                                        fkMaquina,
                                        leiturasDisco,
                                        fk,
                                        fkMaquina,
                                        escritarDisco
                                );
                                conexao.executarQuery(queryDisco2);
                                break;
                            }
                        };
                    }


                });

            }
        }catch (Exception erro){
            System.out.println(erro);
        }



    }

    @Override
    public void atualizarFixos(Boolean servidor) {
        Looca looca = new Looca();

        Integer qtdDiscosDisco = looca.getGrupoDeDiscos().getQuantidadeDeDiscos();
        for (int i = 0; i < qtdDiscosDisco; i++) {
            String nomeDisco = looca.getGrupoDeDiscos().getDiscos().get(i).getNome();
            Long tamanhoDisco = (looca.getGrupoDeDiscos().getDiscos().get(i).getTamanho() / 1000000000);

            String sql6 = """
                
                UPDATE dadosFixos SET valorCampo = '%s' where fkMaquina = '%s' and fkTipoComponente = '%d' and nomeCampo = 'tamanho do disco';
                """.formatted(
                    tamanhoDisco,
                    fkMaquina,
                    5);


            String sql7 = """
                
                UPDATE dadosFixos SET valorCampo = '%s' where fkMaquina = '%s' and fkTipoComponente = '%d' and nomeCampo = 'Nome do disco';
                """.formatted(
                    nomeDisco,
                    fkMaquina,
                    5);

            if (servidor){

                ConexaoSqlserver con1 = new ConexaoSqlserver();
                con1.executarQuery(sql6);
                con1.executarQuery(sql7);
            }else{
                ConexaoMysql con = new ConexaoMysql();
                con.executarQuery(sql6);
                con.executarQuery(sql7);

            }
        }

        String sql8 = """
                
                UPDATE dadosFixos SET valorCampo = '%s' where fkMaquina = '%s' and fkTipoComponente = '%d' and nomeCampo = 'Quantidade de disco no computador';
                """.formatted(
                qtdDiscosDisco,
                fkMaquina,
                5);


        if (servidor){
            ConexaoSqlserver con1 = new ConexaoSqlserver();
            con1.executarQuery(sql8);

        }else {
            ConexaoMysql con = new ConexaoMysql();
            con.executarQuery(sql8);

        }

    }
}
