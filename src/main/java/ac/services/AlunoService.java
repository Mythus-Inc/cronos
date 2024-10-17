package ac.services;

import java.io.Serializable;

import javax.inject.Inject;

import base.modelo.Aluno;
import dao.GenericDAO;
import util.Transacional;

public class AlunoService implements Serializable{
	
	
	private static final long serialVersionUID = 1L;
	@Inject
	private GenericDAO<Aluno> daoALuno;
	
	@Transacional
	public void inserirAlterar(Aluno aluno){
		if(aluno.getId()==null){
			daoALuno.inserir(aluno);
		}else{
			daoALuno.alterar(aluno);
		}
	}
	
	@Transacional
	public void update(String valor){
		daoALuno.update(valor);
	}

}
