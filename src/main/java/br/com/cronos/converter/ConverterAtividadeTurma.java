package br.com.cronos.converter;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.ConverterException;
import javax.faces.convert.FacesConverter;
import javax.inject.Inject;
import javax.inject.Named;
 
import dao.GenericDAO;
import ac.modelo.AtividadeTurma;
import base.modelo.Curso;
import util.Mensagem;

@Named("converterAtividadeTurma")
public class ConverterAtividadeTurma implements Converter {
	
	@Inject
	private GenericDAO<AtividadeTurma> dao;

	@Override
	public Object getAsObject(FacesContext fc, UIComponent uic, String value) {
		if (value != null && value.trim().length() > 0) {
			try {
			
				return dao.buscarPorId(AtividadeTurma.class, Long.parseLong(value));
				
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
