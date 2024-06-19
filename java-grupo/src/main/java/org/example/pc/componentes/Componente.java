package org.example.pc.componentes;

public abstract class Componente {

    protected String fkMaquina;

    public Componente(String fkMaquina) {
        this.fkMaquina = fkMaquina;
    }

    public abstract void buscarInfosFixos (Boolean integer);

    abstract public void buscarInfosVariaveis (Boolean teste);

    abstract public void atualizarFixos(Boolean servidor);
}
