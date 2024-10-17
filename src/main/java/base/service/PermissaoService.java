package base.service;

import java.io.Serializable;

import javax.inject.Inject;

import ac.modelo.AlunoTurma;
import ac.modelo.Permissao;
import base.modelo.Curso;
import base.modelo.Tipo;
import dao.GenericDAO;
import util.Transacional;

public class PermissaoService implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@Inject
	private GenericDAO<Permissao> daoService;
	
	@Transacional
	public void inserirAlterar(Permissao permissao){
		if(permissao.getId()==null){
			daoService.inserir(permissao);
		}else{
			daoService.alterar(permissao);
		}
	}
	
	@Transacional
	public void update(String valor){
		daoService.update(valor);
	}

}
