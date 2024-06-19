package org.example.pc.componentes;

import com.github.britooo.looca.api.core.Looca;
import org.example.Conexao;
import org.example.ConexaoMysql;
import org.example.ConexaoSqlserver;

import java.time.Instant;

public class SistemaCp extends Componente {
    public SistemaCp(String fkMaquina) {
        super(fkMaquina);
    }

    @Override
    public void buscarInfosFixos(Boolean integer) {




        Looca looca = new Looca();

        //            SISTEMA
        String modeloSistema = looca.getSistema().getSistemaOperacional();
        Instant inicializadoSistema = looca.getSistema().getInicializado();

        String querySistema = """
                    INSERT INTO dadosFixos(
                    fkMaquina
                    ,fkTipoComponente
                    ,nomeCampo
                    ,valorCampo
                    ,descricao)  VALUES
                                            ( '%s', 1, 'modelo do Sistema', '%s', 'modelo do Sistema'),
                                            ( '%s', 1, 'inicialização do sistema', '%s', 'inicialização do sistema');
                """.formatted(
                fkMaquina,
                modeloSistema,
                fkMaquina,
                inicializadoSistema
        );

        if (integer == true){
            ConexaoSqlserver con1 = new ConexaoSqlserver();
            con1.executarQuery(querySistema);
        }else{
            ConexaoMysql con = new ConexaoMysql();
            con.executarQuery(querySistema);

        }
    }

    @Override
    public void buscarInfosVariaveis(Boolean teste) {

    }

    @Override
    public void atualizarFixos(Boolean servidor) {

        Looca looca = new Looca();

        String modeloSistema = looca.getSistema().getSistemaOperacional();
        Instant inicializadoSistema = looca.getSistema().getInicializado();


        String sql = """
                
                UPDATE dadosFixos SET valorCampo = '%s' where fkMaquina = '%s' and fkTipoComponente = '%d' and nomeCampo = 'modelo do Sistema';
        
                """.formatted(
                modeloSistema,
                fkMaquina,
                1

        );


        String sql22 = """
                
                UPDATE dadosFixos SET valorCampo = '%s' where fkMaquina = '%s' and fkTipoComponente = '%d' and nomeCampo = 'inicialização do sistema';
                """.formatted(
                inicializadoSistema,
                fkMaquina,
                1);


        if (servidor){
            ConexaoSqlserver con1 = new ConexaoSqlserver();
            con1.executarQuery(sql);
            con1.executarQuery(sql22);

        }else{
            ConexaoMysql con = new ConexaoMysql();
            con.executarQuery(sql);
            con.executarQuery(sql22);

        }

    }
}
