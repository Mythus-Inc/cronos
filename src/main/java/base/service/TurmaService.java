package base.service;

import java.io.Serializable;

import javax.inject.Inject;

import ac.modelo.AlunoTurma;
import base.modelo.Curso;
import base.modelo.Tipo;
import base.modelo.Turma;
import dao.GenericDAO;
import util.Transacional;

public class TurmaService implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@Inject
	private GenericDAO<Turma> daoTurma;
	
	@Transacional
	public void inserirAlterar(Turma turma){
		if(turma.getId()==null){
			daoTurma.inserir(turma);
		}else{
			daoTurma.alterar(turma);
		}
	}
	
	@Transacional
	public void update(String valor){
		daoTurma.update(valor);
	}

}
