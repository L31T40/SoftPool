package com.example.softpool.softpool;

import android.util.Log;

import static android.content.ContentValues.TAG;

public class infoBoleias {
    private String idboleia;
    private String origem;
    private String destino;
    private String dtapartida;
    private String dtachegada;
    private String horapartida;
    private String horachegada;
    private String lugaresdisponiveis;
    private String objetivo;
    private String imgobjetivo;
    private String imgestado;
    private String estado;
    private String nome;
    private String idcondutor;
    private String img;

    infoBoleias() {
    }

    public infoBoleias(String idboleia,
                       String origem,
                       String destino,
                       String dtapartida,
                       String dtachegada,
                       String horapartida,
                       String horachegada,
                       String lugaresdisponiveis,
                       String objetivo,
                       String imgobjetivo,
                       String imgestado,
                       String estado,
                       String nome,
                       String idcondutor,
                       String img) {

        this.idboleia=idboleia;
        this.origem=origem;
        this.destino=destino;
        this.dtapartida=dtapartida;
        this.dtachegada=dtachegada;
        this.horapartida=horapartida;
        this.horachegada=horachegada;
        this.lugaresdisponiveis=lugaresdisponiveis;
        this.objetivo=objetivo;
        this.imgobjetivo=imgobjetivo;
        this.imgestado=imgestado;
        this.estado=estado;
        this.nome=nome;
        this.idcondutor=idcondutor;
        this.img=img;

    }

    private static infoBoleias instance = null;

    public static infoBoleias getInstance() {
        if (instance == null) {
            instance = new infoBoleias();
        }
        return instance;
    }

    public String getIDboleia() { return idboleia; }

    public void setIDboleia(String idboleia) {
        this.idboleia = idboleia;
    }

    public String getOrigem() { return origem; }

    public void setOrigem(String origem) {
        this.origem = origem;
    }


    public String getDestino() {
        return destino;
    }

    public void setDestino(String destino) {
        this.destino = destino;
    }

    public String getDtapartida() {
        return dtapartida;
    }

    public void setDtapartida(String dtapartida) {
        this.dtapartida = dtapartida;
    }

    public String getDtachegada() {
        return dtachegada;
    }

    public void setDtachegada(String dtachegada) {
        this.dtachegada = dtachegada;
    }

    public String getHorapartida() {
        return horapartida;
    }

    public void setHorapartida(String horapartida) {
        this.horapartida = horapartida;
    }

    public String getHorachegada() {
        return horachegada;
    }

    public void setHorachegada(String horachegada) {
        this.horachegada = horachegada;
    }

    public String getLugaresdisponiveis() {
        return lugaresdisponiveis;
    }

    public void setLugaresdisponiveis(String lugaresdisponiveis) {
        this.lugaresdisponiveis = lugaresdisponiveis;
    }

    public String getImgObjetivo() {
        return imgobjetivo;
    }

    public void setImgObjetivo(String imgobjetivo) {
        this.imgobjetivo = imgobjetivo;
    }

    public String getObjetivo() {
        return objetivo;
    }

    public void setObjetivo(String objetivo) {
        this.objetivo = objetivo;
    }

    public String getImgestado() {
        return imgestado;
    }

    public void setImgestado(String imgestado) {
        this.imgestado = imgestado;
    }


    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }


    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getIDcondutor() {
        return idcondutor;
    }

    public void setIDcondutor(String idcondutor) {
        this.idcondutor = idcondutor;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }


}
