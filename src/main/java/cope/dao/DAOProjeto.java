package cope.dao;

import java.io.Serializable;
import java.util.List;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.Query;

import cope.modelo.Projeto;

public class DAOProjeto implements Serializable{
	
	private static final long serialVersionUID = 1L;
	
	@Inject
	private EntityManager manager;

	@SuppressWarnings("unchecked")
	public List<Projeto> listarProjetoPorPessoa(Long idPessoa) {
		String hql = "SELECT pro FROM Participante as p INNER JOIN p.projeto as pro WHERE p.status is true and p.pessoa.id = :idPessoa";
		Query q = manager.createQuery(hql);
		q.setParameter("idPessoa", idPessoa);
		return q.getResultList();
	}
	
}
