package cope.service;

import java.io.Serializable;

import javax.inject.Inject;
 

import ac.modelo.AlunoTurma;
import base.modelo.Tipo;
import cope.modelo.ArquivoProjeto;
import cope.modelo.Projeto;
import dao.GenericDAO;
import questionario.modelo.Email;
import questionario.modelo.Formulario;
import util.Transacional;

public class ArquivoProjetoService implements Serializable{
	

	private static final long serialVersionUID = 1L;
	@Inject
	private GenericDAO<ArquivoProjeto> dao;
	
	@Transacional
	public void inserirAlterar(ArquivoProjeto obj){
	
		if(obj.getId()==null){
			dao.inserir(obj);
		}else{
			dao.alterar(obj);
		}
		
	}
	
	@Transacional
	public void update(String valor){
		dao.update(valor);
	}

}
