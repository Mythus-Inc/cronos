package questionario.service;

import java.io.Serializable;

import javax.inject.Inject;

import ac.modelo.AlunoTurma;
import base.modelo.Tipo;
import dao.GenericDAO;
import questionario.modelo.Formulario;
import util.Transacional;

public class FormularioService implements Serializable{
	

	private static final long serialVersionUID = 1L;
	@Inject
	private GenericDAO<Formulario> daoFormulario;
	
	@Transacional
	public void inserirAlterar(Formulario formulario){
	
			daoFormulario.inserir(formulario);
		
	}
	
	@Transacional
	public void update(String valor){
		daoFormulario.update(valor);
	}

}
