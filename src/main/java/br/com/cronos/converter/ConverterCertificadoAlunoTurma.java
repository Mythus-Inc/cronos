package br.com.cronos.converter;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.ConverterException;
import javax.faces.convert.FacesConverter;
import javax.inject.Inject;
import javax.inject.Named;

import ac.modelo.AlunoTurma;
import base.modelo.Curso; 
import dao.GenericDAO;
import util.Mensagem;


@Named("alunoTurmaConverte")
public class ConverterCertificadoAlunoTurma implements Converter{
	
	@Inject
	private GenericDAO<AlunoTurma> dao;

	 @Override
	    public Object getAsObject(FacesContext fc, UIComponent uic, String value) {
		 if (value != null && value.trim().length() > 0) {
				try {
	        	
	            return  dao.buscarPorId(AlunoTurma.class, Long.parseLong(value));
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
	    public String getAsString(FacesContext fc, UIComponent uic, Object o) {
	        if (o != null && (o instanceof AlunoTurma)) {
	            return String.valueOf(((AlunoTurma) o).getId());
	        }

	        return null;
	    }

}