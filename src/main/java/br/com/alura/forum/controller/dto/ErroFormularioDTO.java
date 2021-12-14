package br.com.alura.forum.controller.dto;

public class ErroFormularioDTO {

    private String campo;
    private String erro;

    public ErroFormularioDTO(String campo, String erro) {
        this.campo = campo;
        this.erro = erro;
    }

    public String getCampo() {
        return campo;
    }

    public String getErro() {
        return erro;
    }
}
