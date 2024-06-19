package org.example;

public class Usuario {
    private Integer id;
    private String tipo;
    private String nome;
    private String email;
    private String senha;
    private Integer fkEmpresa;

    public Usuario(Integer id, String tipo, String nome, String email, String senha, Integer fkEmpresa) {
        this.id = id;
        this.tipo = tipo;
        this.nome = nome;
        this.email = email;
        this.senha = senha;
        this.fkEmpresa = fkEmpresa;
    }

    public Integer getId() {
        return id;
    }

    public String getTipo() {
        return tipo;
    }

    public String getNome() {
        return nome;
    }

    public String getEmail() {
        return email;
    }

    public String getSenha() {
        return senha;
    }

    public Integer getFkEmpresa() {
        return fkEmpresa;
    }

    @Override
    public String toString() {
        return "Usuario{" +
                "id=" + id +
                ", tipo='" + tipo + '\'' +
                ", nome='" + nome + '\'' +
                ", email='" + email + '\'' +
                ", senha='" + senha + '\'' +
                ", fkEmpresa=" + fkEmpresa +
                '}';
    }
}