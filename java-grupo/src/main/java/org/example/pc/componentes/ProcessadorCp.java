package org.example.pc.componentes;

import com.github.britooo.looca.api.core.Looca;
import org.example.Conexao;
import org.example.ConexaoMysql;
import org.example.ConexaoSqlserver;
import org.springframework.jdbc.core.JdbcTemplate;

public class ProcessadorCp extends Componente {
    public ProcessadorCp(String fkMaquina) {
        super(fkMaquina);
    }

    @Override
    public void buscarInfosFixos(Boolean integer) {



        Looca looca = new Looca();


        String nomeProcessador = looca.getProcessador().getNome();
//        String potenciaProcessador = nomeProcessador.substring(nomeProcessador.indexOf("@") + 2, nomeProcessador.lastIndexOf("G"));
        Integer nmrPacotesFisicosProcessador = looca.getProcessador().getNumeroPacotesFisicos();
        Integer nmrCpusFisicosProcessador = looca.getProcessador().getNumeroCpusFisicas();
        Integer nmrCpusLogicasProcessador = looca.getProcessador().getNumeroCpusLogicas();

        String queryProcessador = """
                    INSERT INTO dadosFixos(
                    fkMaquina
                    ,fkTipoComponente
                    ,nomeCampo
                    ,valorCampo
                    ,descricao)  VALUES
                                            ( '%s', 3, 'Nome do processador', '%s', 'Nome do processador'),
                                            ( '%s', 3, 'Numero de pacotes físicos do processador', '%s', 'Numero de pacotes físicos do processador'),
                                            ( '%s', 3, 'Numero de CPUs físicas do processador', '%s', 'Numero de CPUs físicas do processador'),
                                            ( '%s', 3, 'Numero de CPUs Logicas do processador', '%s', 'Numero de CPUs Logicas do processador')
                """.formatted(
                fkMaquina,
                nomeProcessador,
                fkMaquina,
                nmrPacotesFisicosProcessador,
                fkMaquina,
                nmrCpusFisicosProcessador,
                fkMaquina,
                nmrCpusLogicasProcessador
        );

        if (integer == true){
            ConexaoSqlserver con1 = new ConexaoSqlserver();
            con1.executarQuery(queryProcessador);
        }else {
            ConexaoMysql con = new ConexaoMysql();
            con.executarQuery(queryProcessador);

        }
    }

    @Override
    public void buscarInfosVariaveis(Boolean teste) {


        Looca looca = new Looca();
        Double emUsoProcessador = (looca.getProcessador().getUso());

        String queryFk = """
                select idDadosFixos from dadosFixos where fkMaquina = '%s' and fkTipoComponente = 3 and nomeCampo = 'Nome do processador'""".formatted(fkMaquina);


        if (teste) {
            ConexaoSqlserver conexao = new ConexaoSqlserver();
            JdbcTemplate con = conexao.getConexaoDoBanco();


            try {
                System.out.println(queryFk);
                Integer fk = con.queryForObject(queryFk, Integer.class);
                System.out.println(fk);

                var queryMemoria = """
                        iNSERT INTO dadosTempoReal(
                         fkDadosFixos
                         ,fkMaquina
                         ,fkTipoComponente
                         ,dataHora
                         ,nomeCampo
                         ,valorCampo) values
                        ( %d, '%s', 3, current_timestamp,'emUso', '%s');
                              """.formatted(
                        fk,
                        fkMaquina,
                        emUsoProcessador
                );


                conexao.executarQuery(queryMemoria);
            } catch (Exception erro) {
                System.out.println(erro);
            }

        }else {

            ConexaoMysql conexao1 = new ConexaoMysql();
            JdbcTemplate con1 = conexao1.getConexaoDoBanco();

            try {


                Integer fk1 = con1.queryForObject(queryFk, Integer.class);

                var queryMemoria1 = """
                        iNSERT INTO dadosTempoReal(
                         fkDadosFixos
                         ,fkMaquina
                         ,fkTipoComponente
                         ,dataHora
                         ,nomeCampo
                         ,valorCampo) values
                         ( %d, '%s', 3, current_timestamp(),'emUso', '%s');
                              """.formatted(
                        fk1,
                        fkMaquina,
                        emUsoProcessador
                );

                conexao1.executarQuery(queryMemoria1);
            } catch (Exception erro) {
                System.out.println(erro);
            }
        }
    }

    @Override
    public void atualizarFixos(Boolean servidor) {

        Looca looca =  new Looca();

        String nomeProcessador = looca.getProcessador().getNome();
        Integer nmrPacotesFisicosProcessador = looca.getProcessador().getNumeroPacotesFisicos();
        String potenciaProcessador = nomeProcessador.substring(nomeProcessador.indexOf("@") + 2, nomeProcessador.lastIndexOf("G"));
        Integer nmrCpusFisicosProcessador = looca.getProcessador().getNumeroCpusFisicas();
        Integer nmrCpusLogicasProcessador = looca.getProcessador().getNumeroCpusLogicas();



        String sql17 = """
                
                UPDATE dadosFixos SET valorCampo = '%s' where fkMaquina = '%s' and fkTipoComponente = '%d' and nomeCampo = 'Numero de CPUs Logicas do processador';
                """.formatted(
                nmrCpusLogicasProcessador,
                fkMaquina,
                3);



        String sql18 = """
                
                UPDATE dadosFixos SET valorCampo = '%s' where fkMaquina = '%s' and fkTipoComponente = '%d' and nomeCampo = 'Numero de CPUs físicas do processador';
                """.formatted(
                nmrCpusFisicosProcessador,
                fkMaquina,
                3);



        String sql = """
                
                UPDATE dadosFixos SET valorCampo = '%s' where fkMaquina = '%s' and fkTipoComponente = '%d' and nomeCampo = 'Potencia do processador';
                """.formatted(
                potenciaProcessador,
                fkMaquina,
                3);



        String sql19 = """
                
                UPDATE dadosFixos SET valorCampo = '%s' where fkMaquina = '%s' and fkTipoComponente = '%d' and nomeCampo = 'Numero de pacotes físicos do processador';
                """.formatted(
                nmrPacotesFisicosProcessador,
                fkMaquina,
                3);




        String sql20 = """
                
                UPDATE dadosFixos SET valorCampo = '%s' where fkMaquina = '%s' and fkTipoComponente = '%d' and nomeCampo = 'Nome do Processador';
                """.formatted(
                nomeProcessador,
                fkMaquina,
                3);



        if (servidor){
            ConexaoSqlserver con =  new ConexaoSqlserver();
            con.executarQuery(sql17);
            con.executarQuery(sql18);
            con.executarQuery(sql);
            con.executarQuery(sql19);
            con.executarQuery(sql20);

        }
        else {
            ConexaoMysql con1 =  new ConexaoMysql();
            con1.executarQuery(sql17);
            con1.executarQuery(sql18);
            con1.executarQuery(sql);
            con1.executarQuery(sql19);
            con1.executarQuery(sql20);
        }

    }
}
