package cope.dao;

import java.io.Serializable;
import java.util.List;

import javax.inject.Inject;
import javax.persistence.EntityManager;

import javax.persistence.Query;

import cope.modelo.Participante;
import cope.modelo.Projeto;

public class DAOParticipante implements Serializable {

	private static final long serialVersionUID = 1L;

	@Inject
	private EntityManager manager;

	public Long cargaHorariaTotalPorPessoa(Long idPessoa, Long idParticipante) {

		Query query = null;
		query = manager.createQuery("SELECT SUM(cargaHoraria) FROM Participante WHERE status is true and pessoa.id = '"
				+ idPessoa + "' AND id != '" + idParticipante + "'");
		return (Long) query.getSingleResult();
	}

	@SuppressWarnings("unchecked")
	public List<Participante> listarParticipanteProjeto(Projeto projeto) {

		String sql = "FROM Participante WHERE status is true and projeto = :projeto";
		Query q = manager.createQuery(sql);
		q.setParameter("projeto", projeto);

		return (List<Participante>) q.getResultList();
	}

}
