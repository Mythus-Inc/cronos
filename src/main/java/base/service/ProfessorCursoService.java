package base.service;

import java.io.Serializable;

import javax.inject.Inject;

import ac.modelo.AlunoTurma;
import ac.modelo.Permissao;
import ac.modelo.ProfessorCurso;
import base.modelo.Curso;
import base.modelo.Tipo;
import dao.GenericDAO;
import util.Transacional;

public class ProfessorCursoService implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@Inject
	private GenericDAO<ProfessorCurso> daoProfessorCurso;
	
	@Transacional
	public void inserirAlterar(ProfessorCurso professorCurso){
		if(professorCurso.getId()==null){
			daoProfessorCurso.inserir(professorCurso);
		}else{
			daoProfessorCurso.alterar(professorCurso);
		}
	}
	
	@Transacional
	public void update(String valor){
		daoProfessorCurso.update(valor);
	}

}
