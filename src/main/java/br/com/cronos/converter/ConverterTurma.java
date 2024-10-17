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
import base.modelo.Curso;
import base.modelo.Turma;
import util.Mensagem;
 
@Named("converterTurma")
public class ConverterTurma implements Converter {

	@Inject
	private GenericDAO<Turma> dao;

	
	@Override
	public Object getAsObject(FacesContext fc, UIComponent uic, String value) {
		System.out.println("AS OB: "+value);
		if (value != null && value.trim().length() > 0) {
			try {
		    
				return dao.buscarPorId(Turma.class, Long.parseLong(value));
			
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
			return String.valueOf(((Turma) object).getId());
		} else {
			return null;
		}
	}
}
