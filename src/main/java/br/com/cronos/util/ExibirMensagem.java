package br.com.cronos.util;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;

public class ExibirMensagem {

	public static void exibirMensagem(String mensagem){
		FacesContext context = FacesContext.getCurrentInstance();
		context.addMessage(null, new FacesMessage(mensagem));
	}
}
