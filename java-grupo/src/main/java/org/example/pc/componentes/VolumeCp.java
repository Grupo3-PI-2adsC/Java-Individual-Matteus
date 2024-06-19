package org.example.pc.componentes;

import com.github.britooo.looca.api.core.Looca;
import org.example.Conexao;
import org.example.ConexaoMysql;
import org.example.ConexaoSqlserver;

public class VolumeCp extends Componente {

    public VolumeCp(String fkMaquina) {
        super(fkMaquina);
    }

    @Override
    public void buscarInfosFixos(Boolean integer) {




        Looca looca = new Looca();

        Integer qtdVolumesVolume = looca.getGrupoDeDiscos().getQuantidadeDeVolumes();

        String queryVolume2 = """
                            INSERT INTO dadosFixos(
                    fkMaquina
                    ,fkTipoComponente
                    ,nomeCampo
                    ,valorCampo
                    ,descricao)  VALUES
                                ('%s', 6, 'qtdVolumes', '%s', 'quantidade de volumes no computador');
                """.formatted(
                fkMaquina,
                qtdVolumesVolume);

        if (integer == true){
            ConexaoSqlserver con1 = new ConexaoSqlserver();
            con1.executarQuery(queryVolume2);
        }else{
            ConexaoMysql con = new ConexaoMysql();
            con.executarQuery(queryVolume2);

        }

        for (int i = 1; i < qtdVolumesVolume; i++) {

            //            VOLUME
            String nomeVolume = looca.getGrupoDeDiscos().getVolumes().get(i).getNome();
            String UUIDVolume = looca.getGrupoDeDiscos().getVolumes().get(i).getUUID();
            Long totalVolume = looca.getGrupoDeDiscos().getVolumes().get(i).getTotal();
            Long disponivelVolume = looca.getGrupoDeDiscos().getVolumes().get(i).getDisponivel();
            String tipoVolume = looca.getGrupoDeDiscos().getVolumes().get(i).getTipo();

            String queryVolume = """
                            INSERT INTO dadosFixos (
                            fkMaquina
                            ,fkTipoComponente
                            ,nomeCampo
                            ,valorCampo
                            ,descricao) 
                            VALUES
                                                    ( '%s', 6, 'UUID do volume', '%s', 'UUID do volume'),
                                                    ( '%s', 6, 'nome do volume', '%s', 'nome do volume'),
                                                    ( '%s', 6, 'tamanho total do volume', '%s', 'tamanho total do volume'),
                                                    ( '%s', 6, 'tamanho disponivel do volume', '%s', 'tamanho disponivel do volume'),
                                                    ( '%s', 6, 'tipo do volume', '%s', 'tipo do volume')
                        """.formatted(
                    fkMaquina,
                    UUIDVolume,
                    fkMaquina,
                    nomeVolume,
                    fkMaquina,
                    totalVolume,
                    fkMaquina,
                    disponivelVolume,
                    fkMaquina,
                    tipoVolume
            );

            if (integer == true){
                ConexaoSqlserver con1 = new ConexaoSqlserver();
                con1.executarQuery(queryVolume);
            }else{
                ConexaoMysql con = new ConexaoMysql();
                con.executarQuery(queryVolume);

            }
        }
    }

    @Override
    public void buscarInfosVariaveis(Boolean teste) {

    }

    @Override
    public void atualizarFixos(Boolean servidor) {

        Looca looca = new Looca();

        Integer qtdVolumesVolume = looca.getGrupoDeDiscos().getQuantidadeDeDiscos();

        String sql = """
                                
                UPDATE dadosFixos SET valorCampo = '%s' where fkMaquina = '%s' and fkTipoComponente = '%d' and nomeCampo = 'quantidade de volumes no computador';
                """.formatted(
                qtdVolumesVolume,
                fkMaquina,
                6);


        if (servidor){
            ConexaoSqlserver con = new ConexaoSqlserver();
            con.executarQuery(sql);;

        }else{
            ConexaoMysql con1 = new ConexaoMysql();
            con1.executarQuery(sql);

        }

        for (int i = 1; i < qtdVolumesVolume; i++) {
            String nomeVolume = looca.getGrupoDeDiscos().getVolumes().get(i).getNome();
            String UUIDVolume = looca.getGrupoDeDiscos().getVolumes().get(i).getUUID();
            Long totalVolume = looca.getGrupoDeDiscos().getVolumes().get(i).getTotal();
            Long disponivelVolume = looca.getGrupoDeDiscos().getVolumes().get(i).getDisponivel();
            String tipoVolume = looca.getGrupoDeDiscos().getVolumes().get(i).getTipo();



            String sql1 = """
                                    
                    UPDATE dadosFixos SET valorCampo = '%s' where fkMaquina = '%s' and fkTipoComponente = '%d' and nomeCampo = 'tipo do volume';
                    """.formatted(
                    tipoVolume,
                    fkMaquina,
                    6);


            String sql2 = """
                    
                    UPDATE dadosFixos SET valorCampo = '%s' where fkMaquina = '%s' and fkTipoComponente = '%d' and nomeCampo = 'tamanho total do volume';
                    """.formatted(
                    totalVolume,
                    fkMaquina,
                    6);


            String sql3 = """
                
                UPDATE dadosFixos SET valorCampo = '%s' where fkMaquina = '%s' and fkTipoComponente = '%d' and nomeCampo = 'tamanho disponivel do volume';
                """.formatted(
                    disponivelVolume,
                    fkMaquina,
                    6);


            String sql4 = """
                
                UPDATE dadosFixos SET valorCampo = '%s' where fkMaquina = '%s' and fkTipoComponente = '%d' and nomeCampo = 'nome do volume';
                """.formatted(
                    nomeVolume,
                    fkMaquina,
                    6);


            String sql5 = """
                
                UPDATE dadosFixos SET valorCampo = '%s' where fkMaquina = '%s' and fkTipoComponente = '%d' and nomeCampo = 'UUID do volume';
                """.formatted(
                    UUIDVolume,
                    fkMaquina,
                    6);



            if (servidor){
                ConexaoSqlserver con = new ConexaoSqlserver();
                con.executarQuery(sql1);
                con.executarQuery(sql2);
                con.executarQuery(sql3);
                con.executarQuery(sql4);
                con.executarQuery(sql5);

            }else{
                ConexaoMysql con1 = new ConexaoMysql();
                con1.executarQuery(sql1);
                con1.executarQuery(sql2);
                con1.executarQuery(sql3);
                con1.executarQuery(sql4);
                con1.executarQuery(sql5);

            }
        }


    }
}
