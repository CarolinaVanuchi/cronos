package br.com.cronos.converter;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.ConverterException;
import javax.faces.convert.FacesConverter;

import br.com.cronos.dao.DAOGenerico;
import br.com.cronos.modelo.Movimentacao;
import br.com.cronos.util.Mensagem;

@FacesConverter("converterMovimentacao")
public class ConverterMovimentacao implements Converter {

	@Override
	public Object getAsObject(FacesContext fc, UIComponent uic, String value) {
		if (value != null && value.trim().length() > 0) {
			try {
				DAOGenerico dao = new DAOGenerico();
				Object movimentacao = dao.buscarPorId(Movimentacao.class, Long.parseLong(value));
				return movimentacao;
			} catch (NumberFormatException e) {
				e.printStackTrace();
				throw new ConverterException(
						new FacesMessage(FacesMessage.SEVERITY_ERROR, Mensagem.ERRO_CONVERTER, ""));
			}
		} else {
			return null;
		}
	}

	@Override
	public String getAsString(FacesContext fc, UIComponent uic, Object object) {
		if (object != null) {
			return String.valueOf(((Movimentacao) object).getId());
		} else {
			return null;
		}
	}
}
