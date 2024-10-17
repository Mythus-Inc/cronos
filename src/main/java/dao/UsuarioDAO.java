package dao;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Query;
import javax.inject.Inject;
import javax.persistence.EntityManager;

import ac.modelo.AlunoTurma;
import ac.modelo.Permissao;
import ac.modelo.ProfessorCurso;

public class UsuarioDAO implements Serializable{

 
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@Inject
	private EntityManager manager;
	

	public List<ProfessorCurso> buscarCursosProfessor(String condicao) {
		
		Query q = null;
		List<ProfessorCurso> lista = new ArrayList<ProfessorCurso>();
	
			q = manager.createQuery(
					"from ProfessorCurso where " + condicao + " and curso.status is true and status is true");
			lista = q.getResultList();
		

		return lista;
	}
	

	
	
	public List<Permissao> buscarPermissaoServidor(String condicao) {

		Query q = null;
		List<Permissao> lista = new ArrayList<Permissao>();
	
			q = manager.createQuery(
					"from Permissao where " + condicao + " and tipo.status is true and status is true");
			lista = q.getResultList();
	

		return lista;
	}
	
//	public List<Movimentacao> buscarMovimentacaoAluno(String condicao) {
//
//		Query q = null;
//		List<Movimentacao> lista = new ArrayList<Movimentacao>();
//		 
//			q = manager.createQuery(
//					"from Movimentacao where " + condicao + " and status is true");
//			lista = q.getResultList();
//		 
//
//		return lista;
//	}

	public List retornarLogado(Class classe, String usuario) {
		Query q = null;
 
			q = manager.createQuery(
					"from " + classe.getSimpleName() + " where status = true and usuario = '" + usuario + "'");

		 
		return q.getResultList();
	}

}
