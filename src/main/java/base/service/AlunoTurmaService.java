package base.service;

import java.io.Serializable;

import javax.inject.Inject;

import ac.modelo.AlunoTurma;
import dao.GenericDAO;
import util.Transacional;

public class AlunoTurmaService implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@Inject
	private GenericDAO<AlunoTurma> daoALunoTurma;
	
	@Transacional
	public void inserirAlterar(AlunoTurma aluno){
		if(aluno.getId()==null){
			daoALunoTurma.inserir(aluno);
		}else{
			daoALunoTurma.alterar(aluno);
		}
	}
	
	@Transacional
	public void update(String valor){
		daoALunoTurma.update(valor);
	}

}
