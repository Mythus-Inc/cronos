package dao;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.Query;

import ac.modelo.AlunoTurma;


public class MovimentacaoAlunoDAO implements Serializable{
	
	private static final long serialVersionUID = 1L;
	
	@Inject
	private EntityManager manager;

	public List<AlunoTurma> buscarAtivo() {
	
		
		Query q = null;
		List<AlunoTurma> lista = new ArrayList<>();
				
			q = manager
					.createQuery("from Movimentacao a where a.dataMovimentacao = (select max(b.dataMovimentacao)"
							+ " from Movimentacao b where b.alunoTurma = a.alunoTurma) "
							+ " and a.situacao = 1 and a.status is true and a.controle = true and a.dataMovimentacaoFim = null order by a.alunoTurma.aluno.nome"); // coloquei um and dataMovi.... = null
			
			lista = q.getResultList();
		
		return lista;
	}

	public List<AlunoTurma> buscarTrancado() {	
	
		Query q = null;
		List<AlunoTurma> lista = new ArrayList<>();
		
			q = manager.createQuery(
					"from Movimentacao a where a.dataMovimentacao = (select max(b.dataMovimentacao) from Movimentacao b where b.alunoTurma = a.alunoTurma) "
							+ " and a.situacao <> 1 and a.status is true and a.alunoTurma.status = true and a.controle = false"); // coloquei um and dataMovi.... = null
			
			lista = q.getResultList();
		
		return lista;
	}

	public List<AlunoTurma> listarMaioresAlunoTurma() {
	
		Query q = null;
		List<AlunoTurma> lista = new ArrayList<>();
	
			q = manager.createQuery("from AlunoTurma a where a.dataMudanca = (select max(b.dataMudanca)"
					+ " from AlunoTurma b where b.aluno = a.aluno) " + " and a.status is true");
			
			lista = q.getResultList();
		
		return lista;
	}

	public List<AlunoTurma> buscarMaiorAlunoTurma(Long aluno) {
		
		Query q = null;
		List<AlunoTurma> lista = new ArrayList<>();
		
			q = manager.createQuery(" from AlunoTurma a where a.momentoMudanca  = (select max(b.momentoMudanca) "
					+ " from AlunoTurma b where b.aluno = a.aluno) and a.status is true and a.aluno = " + aluno);
			
			lista = q.getResultList();
		
		return lista;
	}

	public List<AlunoTurma> buscarMaiorMovimentacao(Long aluno) {
		
		Query q = null;
		List<AlunoTurma> lista = new ArrayList<>();
		
			q = manager
					.createQuery(" from Movimentacao a where a.dataMovimentacao  = (select max(b.dataMovimentacao) "
							+ " from Movimentacao b where b.alunoTurma = a.alunoTurma) and a.status is true and a.alunoTurma = "
							+ aluno);
			
			lista = q.getResultList();
			
//			for(Movimentacao a : lista){
//				System.out.print("lista " +a.getAlunoTurma().getAluno().getNome());
//				if(a.getDataMovimentacao() == null)
//				{
//					int i = 10;
//				}
//				
//			}
		
		return lista;
	}
	
	public List listarTodos(Class classe, String condicao) {
	    Query query = null;
	
			query = manager
					.createQuery("from " + classe.getSimpleName() + " a where a.status is true and " + condicao);
		
		return query.getResultList();
	}
	
	
	public Object buscarCond(Class classe, String condicao) {
	    Query query = null;
		
			query = manager
					.createQuery("from " + classe.getSimpleName() + " a where a.status is true and " + condicao);
		
		return query.getResultList();
	}

	
}
