package br.com.cronos.converter;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.ConverterException;
import javax.faces.convert.FacesConverter;

import br.com.cronos.dao.DAOGenerico;
import br.com.cronos.modelo.AtividadeTurma;
import br.com.cronos.util.Mensagem;

@FacesConverter("converterAtividadeTurma")
public class ConverterAtividadeTurma implements Converter {

	@Override
	public Object getAsObject(FacesContext fc, UIComponent uic, String value) {
		if (value != null && value.trim().length() > 0) {
			try {
				DAOGenerico dao = new DAOGenerico();
				Object atividadeTurma = dao.buscarPorId(AtividadeTurma.class, Long.parseLong(value));
				return atividadeTurma;
			} catch (Exception e) {
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
			return String.valueOf(((AtividadeTurma) object).getId());
		} else {
			return null;
		}
	}

}
