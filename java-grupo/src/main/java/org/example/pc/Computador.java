package org.example.pc;

import org.example.pc.componentes.*;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class Computador {
    private String idMaquina;
    private String hostname;
    private Boolean ativo;
    private Integer empresa;
    private List<Componente> listaComponentes;
    private RedeCp rede;
    private DiscoCp disco;
    private ProcessadorCp processador;
    private RamCp ram;
    private VolumeCp volume;
    private SistemaCp sistema;

    public Computador(String idMaquina, String hostname, Boolean ativo, Integer empresa) {
        this.idMaquina = idMaquina;
        this.hostname = hostname;
        this.ativo = ativo;
        this.empresa = empresa;
        this.listaComponentes =  new ArrayList<>();
        this.disco       = new DiscoCp(idMaquina);
        this.rede        = new RedeCp(idMaquina);
        this.processador = new ProcessadorCp(idMaquina);
        this.ram         = new RamCp(idMaquina);
        this.volume      = new VolumeCp(idMaquina);
        this.sistema     = new SistemaCp(idMaquina);
        listaComponentes.add(ram);
        listaComponentes.add(disco);
        listaComponentes.add(processador);
        listaComponentes.add(rede);
        listaComponentes.add(sistema);
        listaComponentes.add(volume);

    }

    public String getIdMaquina() {
        return idMaquina;
    }

    public void setIdMaquina(String idMaquina) {
        this.idMaquina = idMaquina;
    }

    public String getHostname() {
        return hostname;
    }

    public void setHostname(String hostname) {
        this.hostname = hostname;
    }

    public Boolean getAtivo() {
        return ativo;
    }

    public void setAtivo(Boolean ativo) {
        this.ativo = ativo;
    }

    public Integer getEmpresa() {
        return empresa;
    }

    public void setEmpresa(Integer empresa) {
        this.empresa = empresa;
    }

    public List<Componente> getListaComponentes() {
        return listaComponentes;
    }

    public void setListaComponentes(List<Componente> listaComponentes) {
        this.listaComponentes = listaComponentes;
    }

    public RedeCp getRede() {
        return rede;
    }

    public void setRede(RedeCp rede) {
        this.rede = rede;
    }

    public DiscoCp getDisco() {
        return disco;
    }

    public void setDisco(DiscoCp disco) {
        this.disco = disco;
    }

    public ProcessadorCp getProcessador() {
        return processador;
    }

    public void setProcessador(ProcessadorCp processador) {
        this.processador = processador;
    }

    public RamCp getRam() {
        return ram;
    }

    public void setRam(RamCp ram) {
        this.ram = ram;
    }

    public VolumeCp getVolume() {
        return volume;
    }

    public void setVolume(VolumeCp volume) {
        this.volume = volume;
    }

    public SistemaCp getSistema() {
        return sistema;
    }

    public void setSistema(SistemaCp sistema) {
        this.sistema = sistema;
    }

    public void buscarInfos(Integer primeiro, Boolean teste) {

        try {
            TimeUnit.SECONDS.sleep(10);
//            System.out.println("asldnaoçsndfçlasndopansdlkjahs dklahsb d");

            System.out.println("""
                    pegando dados em tempo real
                    ...............................""");
            for (Componente componenteAtual : listaComponentes) {
                System.out.println(componenteAtual);
                if (teste){
                    componenteAtual.buscarInfosVariaveis(teste);
                }
                else {
                    componenteAtual.buscarInfosVariaveis(teste);
                }
            }
        } catch (InterruptedException e) {
            System.out.println(e);
        }
    }

    public void cadastrarFixos(Boolean servidor){
        for (Componente componenteAtual : listaComponentes) {
            System.out.println(componenteAtual);
            componenteAtual.buscarInfosFixos(servidor);
        }
    }

    public void atualizarFixos(Boolean servidor){
        System.out.println("""
                Atualizando Componentes fixos do computador
                ............................................""");
        for (Componente componenteAtual : listaComponentes) {
            componenteAtual.atualizarFixos(servidor);
        }
    }


    @Override
    public String toString() {
        return "Computador{" +
                "idMaquina=" + idMaquina +
                ", hostname='" + hostname + '\'' +
                ", ativo=" + ativo +
                ", empresa=" + empresa +
                ", listaComponentes=" + listaComponentes +
                ", rede=" + rede +
                ", disco=" + disco +
                ", processador=" + processador +
                ", ram=" + ram +
                ", volume=" + volume +
                ", sistema=" + sistema +
                '}';
    }
}
