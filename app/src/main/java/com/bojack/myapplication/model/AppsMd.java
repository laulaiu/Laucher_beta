package com.bojack.myapplication.model;

import android.graphics.drawable.Drawable;

public class AppsMd {

    String nome;
    String package_app;
    Drawable icone;

    public AppsMd(String nome, String package_app, Drawable icone) {
        this.nome = nome;
        this.package_app = package_app;
        this.icone = icone;
    }

    public String getPackage_app() {
        return package_app;
    }

    public void setPackage_app(String package_app) {
        this.package_app = package_app;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public Drawable getIcone() {
        return icone;
    }

    public void setIcone(Drawable icone) {
        this.icone = icone;
    }
}
