package dao;

import java.io.Serializable;
import java.util.List;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.Query;

public class GrupoCertificadoDAO implements Serializable{
	
	private static final long serialVersionUID = 1L;
	
	@Inject
	private EntityManager manager;
	
	
	public List recuperarGrupoAtividade(Long id) {
		Query query = null;
	
			query = manager.createQuery("from Atividade e where e.status is true and e.id = " + id);
		
		return query.getResultList();
	}

	public List recuperarGrupoTurma(Long idTurma, Long idGrupo) {
		Query query = null;
	
			query = manager.createQuery("from GrupoTurma a where a.status is true and a.matriz = " + idTurma + " and a.grupo = " + idGrupo);

		return query.getResultList();
	}

	public List recuperarAtividade(Long idGrupo) {
		Query query = null;
			query = manager.createQuery("from Atividade b where b.status is true and b.grupo = " + idGrupo);
	
		return query.getResultList();
	}

	public List recuperarAtividadeTurmas(Long idAtividade, Long idTurma) {
		Query query = null;
		
			query = manager.createQuery("from AtividadeTurma c where c.status is true and c.atividade = "
					+ idAtividade + " and c.matriz = " + idTurma);
	
		return query.getResultList();
	}

	public List recuperarCertificados(Long idAluno, Long idAtividadeTurma) {
		Query query = null;
	
			query = manager.createQuery("from Certificado d where d.status is true and d.alunoTurma = " + idAluno
					+ " and d.atividadeTurma = " + idAtividadeTurma + " and d.situacao = 3");
	
		return query.getResultList();
	}
}
