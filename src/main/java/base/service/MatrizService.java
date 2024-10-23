package base.service;

import java.io.Serializable;

import javax.inject.Inject;

import ac.modelo.AlunoTurma;
import base.modelo.Matriz;
import base.modelo.Tipo;
import dao.GenericDAO;
import util.Transacional;

public class MatrizService implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@Inject
	private GenericDAO<Matriz> daoMatriz;
	
	@Transacional
	public void inserirAlterar(Matriz matriz){
		if(matriz.getId()==null){
			daoMatriz.inserir(matriz);
		}else{
			daoMatriz.alterar(matriz);
		}
	}
	
	@Transacional
	public void update(String valor){
		daoMatriz.update(valor);
	}

}
