package ac.controle;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.EntityManager;

import org.hibernate.Session;

import util.ChamarRelatorio;
import util.ExibirMensagem;
import util.Mensagem;
import util.RecuperarRelacoesProfessor;
import util.Transacional;
import util.VerificaSituacaoTurma;
import ac.modelo.AlunoTurma;
import ac.modelo.Certificado;
import base.modelo.Turma; 
import dao.GenericDAO;
import dao.MovimentacaoAlunoDAO;

@ViewScoped
@Named("relatorioSecretariaMB")
public class RelatorioSecretariaMB implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	
	private Turma turma;
	private List<Turma> turmas;
	

	
	@Inject
	private GenericDAO<AlunoTurma> daoAlunoTurma;
	
	@Inject
	private GenericDAO<Turma> daoTurma;
	
	@Inject
	private VerificaSituacaoTurma verificaSituacaoTurma;
	
	@Inject
	private EntityManager manager;
	
    @Transacional
	public void relatorioSecretariaMB() {
	
		turma = new Turma();
		preencherListaTurma();
		
		
	}

	public void imprimirCertificadoSituacao() {
		 
		try {
			verificaSituacaoTurma.recuperarCertificados(turma); 
			List<AlunoTurma> alunoTurmas = daoAlunoTurma.listar(AlunoTurma.class, " aluno.id is not null ");
			if (!alunoTurmas.isEmpty()) {

				HashMap parametro = new HashMap<>();
				parametro.put("TURMA", turma.getId());
				ChamarRelatorio ch = new ChamarRelatorio("situacao.jasper", parametro, "certificado_" + "Relatório de situação da turma " + turma.getDescricao());
				Session sessions = manager.unwrap(Session.class);
				sessions.doWork(ch);
		
			} else {
				ExibirMensagem.exibirMensagem(Mensagem.NADA_ENCONTRADO);
			}
		} catch (Exception e) {
			e.printStackTrace();
			ExibirMensagem.exibirMensagem(Mensagem.ERRO);
		}
	}

	public void preencherListaTurma() {
 
		
		turmas = daoTurma.listaComStatus(Turma.class);
	}

	public List<Turma> completarTurma(String str) {
		preencherListaTurma();
		List<Turma> turmasSelecionadas = new ArrayList<>();
		for (Turma t : turmas) {
			if (t.getDescricao().startsWith(str)) {
				turmasSelecionadas.add(t);
			}
		}
		return turmasSelecionadas;
	}

	public Turma getTurma() {
		return turma;
	}

	public void setTurma(Turma turma) {
		this.turma = turma;
	}
}
