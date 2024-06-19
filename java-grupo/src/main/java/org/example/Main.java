package org.example;

import org.example.pc.Computador;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {

//        Conexao conexao = new Conexao(1);
//        JdbcTemplate con = conexao.getConexaoDoBanco();
//
//        con.execute("     INSERT INTO usuario VALUES\n" +
//                "        ( 'recepção', 'KET', 'ket@netmet.com', '1234', 1, 1);");

//    Computador com = new Computador();
//
//    com.teste();
//        Looca looca = new Looca();
//        List<Volume> teste = looca.getGrupoDeDiscos().getVolumes();
//        Integer qtdVolumesVolume = looca.getGrupoDeDiscos().getQuantidadeDeVolumes();
//        System.out.println(qtdVolumesVolume - 1);
//
//        for (int i = 1; i < qtdVolumesVolume; i++) {
//
//            //            VOLUME
//            String UUIDVolume = teste.get(i).getUUID();
//            String nomeVolume = teste.get(i).getNome();
//            Long totalVolume = teste.get(i).getTotal();
//            Long disponivelVolume = teste.get(i).getDisponivel();
//            String tipoVolume = teste.get(i).getTipo();
//
//            System.out.println(nomeVolume);
//        }
//


//        Conexao conexao = new Conexao();
//        JdbcTemplate con = conexao.getConexaoDoBanco();


//        Computador sla = new Computador(1, "matteus-Nitro-AN515-57", true, 1);
//
//        System.out.println(sla);

//        idMaquina=1, hostname='matteus-Nitro-AN515-57', ativo=true, empresa=1, listaComponentes=[org.example.pc.componentes.RamCp@11981797, org.example.pc.componentes.DiscoCp@774698ab, org.example.pc.componentes.ProcessadorCp@3543df7d, org.example.pc.componentes.RedeCp@17a87e37, org.example.pc.componentes.SistemaCp@20f12539, org.example.pc.componentes.VolumeCp@57459491], rede=org.example.pc.componentes.RedeCp@17a87e37, disco=org.example.pc.componentes.DiscoCp@774698ab, processador=org.example.pc.componentes.ProcessadorCp@3543df7d, ram=org.example.pc.componentes.RamCp@11981797, volume=org.example.pc.componentes.VolumeCp@57459491, sistema=org.example.pc.componentes.SistemaCp@20f12539}



//        con10.criarBanco();


        ConexaoMysql con10 = new ConexaoMysql();
        Scanner input = new Scanner(System.in);
        Scanner inputText = new Scanner(System.in);
        ConexaoSqlserver con = new ConexaoSqlserver();

        System.out.println("""
                    ----------------------------------------
                    |                                      |
                    |           Digite o seu email:        |
                    |                                      |
                    ----------------------------------------""");

        String emailLogin = inputText.nextLine();


        System.out.println("""
                    ----------------------------------------
                    |                                      |
                    |           Digite a sua senha:        |
                    |                                      |
                    ----------------------------------------""");
        String senhaLogin = inputText.nextLine();

        System.out.println("""
                    ----------------------------------------
                    |                                      |
                    |        Confirmar a sua senha:        |
                    |                                      |
                    ----------------------------------------""");
        String confirmarSenhaLogin = inputText.nextLine();

//        Computador computador = new Computador();
        try {
            Usuario userSqlserver = con.buscarCredenciais(emailLogin, senhaLogin);
            Usuario userMysql = con10.buscarCredenciais(emailLogin, senhaLogin);
//            Computador computador = con10.computadorExiste(3, false, userMysql);

            try {
//                con10.buscarCredenciais(emailLogin, senhaLogin);

                System.out.println("""
                                Usuario Logado com sucesso
                                ..............................""");


                Computador computador = con10.computadorExiste(1, false, userSqlserver);
                Computador computador1 = con.computadorExiste(1, true, userSqlserver);
                System.out.println("Main1: " + computador1);
                System.out.println("Main: " + computador);

                while(true) {
                    try {
                        computador1.buscarInfos(1, true);
                        computador.buscarInfos(1, false);
                    }catch (Exception erro){
                        System.out.println(erro);
                    }
                }

            }catch (Exception erro){
                System.out.println(erro);
            }

        }catch (Exception erro){
            System.out.println(erro);
            System.out.println("""
                                Erro ao fazer Login no SQL SERVER, tente novamente
                                .......................................""");
            if (erro.getCause() != null){

//                try {
//                    con10.buscarCredenciais(emailLogin, senhaLogin);
//
//                    System.out.println("""
//                                        Usuario Logado com sucesso localmente
//                                        ..............................""");
//
//
//                    Computador computador1 = con10.computadorExiste(1, false);
//                    System.out.println("main + " + computador1);
//
//                }catch (Exception erro2){
//                    System.out.println("erro no main");
//                    System.out.println(erro2);
//                }
            }
        }

    }
}
//insert into usuario values
//        (null, 'representante', 'Raimunda Neto', 'raimunda@netmed.com' ,'Raim@123', 1, 1);
//insert into usuario values
//        (null, 'representante', 'Cleiton Olivaras', 'cleiton@netmed.com' ,'Clei@123', 2, 1);
//insert into usuario values
//        (null, 'representante', 'Alberto Maverique', 'alberto@netmed.com' ,'Albe@123', 3, 1);
