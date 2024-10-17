package base.service;

import java.io.Serializable;

import javax.inject.Inject;

import ac.modelo.AlunoTurma;
import base.modelo.Curso;
import base.modelo.Tipo;
import dao.GenericDAO;
import util.Transacional;

public class CursoService implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@Inject
	private GenericDAO<Curso> daoCurso;
	
	@Transacional
	public void inserirAlterar(Curso curso){
		if(curso.getId()==null){
			daoCurso.inserir(curso);
		}else{
			daoCurso.alterar(curso);
		}
	}
	
	@Transacional
	public void update(String valor){
		daoCurso.update(valor);
	}

}
