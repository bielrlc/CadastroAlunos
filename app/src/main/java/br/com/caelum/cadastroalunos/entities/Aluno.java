package br.com.caelum.cadastroalunos.entities;

import java.io.Serializable;

/**
 * Created by android6021 on 02/04/16.
 */
public class Aluno implements Serializable{

    private Long id;
    private String nome;
    private String telefone;
    private String endereco;
    private String site;
    private Double nota;
    private String caminhoFoto;

    public Long getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public String getTelefone() {
        return telefone;
    }

    public String getEndereco() {
        return endereco;
    }

    public String getSite() {
        return site;
    }

    public Double getNota() {
        return nota;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    public void setEndereco(String endereco) {
        this.endereco = endereco;
    }

    public void setSite(String site) {
        this.site = site;
    }

    public void setNota(Double nota) {
        this.nota = nota;
    }

    public String getCaminhoFoto() {
        return caminhoFoto;
    }

    public void setCaminhoFoto(String caminhoFoto) {
        this.caminhoFoto = caminhoFoto;
    }

    @Override
    public String toString() {
        return getId() + " - " + getNome();
    }
}
