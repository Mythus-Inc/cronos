package ac.controle;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.annotation.PostConstruct;
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
import util.VerificaSituacaoTurma;
import ac.modelo.AlunoTurma;
import ac.modelo.AtividadeTurma;
import ac.modelo.Certificado;
import ac.modelo.GrupoTurma;
import ac.services.AlunoService;
import base.modelo.Turma;
import dao.GenericDAO;

@ViewScoped
@Named("relatorioProfessorMB")
public class RelatorioProfessorMB implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private AlunoTurma aluno;
	private Turma turma;
	private List<AlunoTurma> alunosAtivosProfessor;
	
	List<AlunoTurma> alunosTurmas = new ArrayList<AlunoTurma>();
	List<AlunoTurma> alunosTurmasProfessor = new ArrayList<AlunoTurma>();

	private List<Turma> turmas;
	private List<GrupoTurma> grupoTurmas;
	private GrupoTurma grupoTurma;

	@Inject
	private GenericDAO<Certificado> daoCertificado;

	@Inject
	private GenericDAO<AtividadeTurma> daoAtividadesTurma;

	@Inject
	private GenericDAO<AlunoTurma> daoAlunoTurma;

	@Inject
	private RecuperarRelacoesProfessor relacoesProfessor;

	@Inject
	private EntityManager manager;

	@Inject
	private VerificaSituacaoTurma verificaSituacaoTurma;

	@PostConstruct
	public void inicializar() {

		turma = new Turma();
		grupoTurma = new GrupoTurma();
		turmas = new ArrayList<>();
		grupoTurmas = new ArrayList<>();
		aluno = new AlunoTurma();
		this.preencherListaTurma();
		this.buscarAlunoTurmaProfessor();
	}
	
	private void buscarAlunoTurmaProfessor() {
		alunosTurmasProfessor = new ArrayList<AlunoTurma>();
		for(Turma t:turmas) {
			List<AlunoTurma> alunoTurmaProfessor = daoAlunoTurma.listar(AlunoTurma.class, "turma.id="+t.getId());
			alunosTurmasProfessor.addAll(alunoTurmaProfessor);
		}
	}
	

	public void imprimirCertificadoGrupo() {

		verificaSituacaoTurma.recuperarCertificadosAluno(getAluno());

		try {
			List<Certificado> certificados = daoCertificado.listar(Certificado.class,
					" situacao = 3 and aluno = " + getAluno().getAluno().getId());
			System.out.println(certificados.size());
			if (!certificados.isEmpty()) {
				Certificado cs = certificados.get(0);

				HashMap parametro = new HashMap<>();
				parametro.put("ALUNO", getAluno().getAluno().getId());
				ChamarRelatorio ch = new ChamarRelatorio("grupoAlterado.jasper", parametro,
						"certificado_" + "Relatório por grupo do aluno " + aluno.getAluno().getNome());
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

	public void imprimirCertificadoSituacao() {

		try {
			verificaSituacaoTurma.recuperarCertificados(turma);
			List<AlunoTurma> alunoTurmas = daoAlunoTurma.listar(AlunoTurma.class, " aluno.id is not null ");
			if (!alunoTurmas.isEmpty()) {

				HashMap parametro = new HashMap<>();
				parametro.put("TURMA", turma.getId());
				ChamarRelatorio ch = new ChamarRelatorio("situacao.jasper", parametro,
						"certificado_" + "Relatório de situação da turma " + turma.getDescricao());
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

	public void imprimirCertificadoHorasGrupo() {

		verificaSituacaoTurma.recuperarCertificados(turma);
		try {
			List<Certificado> certificados = daoCertificado.listar(Certificado.class, " situacao = 3 ");

			if (!certificados.isEmpty()) {
				Certificado cs = certificados.get(0);

				HashMap parametro = new HashMap<>();
				parametro.put("TURMA", turma.getId());
				ChamarRelatorio ch = new ChamarRelatorio("situacaoHorasAluno.jasper", parametro,
						"certificado_" + "Relatório Horas aluno " + turma.getAbreviacaoTurma());
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

	public void imprimirCertificadoGrupoTurma() {

		try {
			List<Certificado> certificados = daoCertificado.listar(Certificado.class,
					" situacao = 3 and idGrupoTurma = " + getGrupoTurma().getId() + " and atividadeTurma.matriz.id = "
							+ turma.getMatriz().getId());

			if (!certificados.isEmpty()) {
				Certificado cs = certificados.get(0);

				HashMap parametro = new HashMap<>();
				parametro.put("GRUPO", grupoTurma.getId());
				parametro.put("TURMA", turma.getId());
				ChamarRelatorio ch = new ChamarRelatorio("detalhado-turma.jasper", parametro,
						"certificado_" + "Relatório detalhado turma " + grupoTurma.getGrupo().getDescricao() + " "
								+ turma.getDescricao());
				Session sessions = manager.unwrap(Session.class);
				sessions.doWork(ch);

			} else {
				ExibirMensagem.exibirMensagem(Mensagem.NADA_ENCONTRADO);
			}
		} catch (Exception e) {
			ExibirMensagem.exibirMensagem(Mensagem.ERRO);
		}
	}
	
	

	public void imprimirCertificadoGrupoTurmaAtividade() {

		try {
			List<AtividadeTurma> atividades = daoAtividadesTurma.listar(AtividadeTurma.class,
					"  matriz = " + turma.getMatriz().getId());

			if (!atividades.isEmpty()) {

				HashMap parametro = new HashMap<>();
				parametro.put("TURMA", turma.getMatriz().getId());
				ChamarRelatorio ch = new ChamarRelatorio("grupoTurma.jasper", parametro,
						"atividades_" + "Relatório atividades turma " + turma.getDescricao());
				Session sessions = manager.unwrap(Session.class);
				sessions.doWork(ch);

			} else {
				ExibirMensagem.exibirMensagem(Mensagem.NADA_ENCONTRADO);
			}
		} catch (Exception e) {
			//System.out.println(e);
			e.printStackTrace();

			ExibirMensagem.exibirMensagem(Mensagem.ERRO);
		}
	}

	public void imprimirGrupoAluno() {
		try {
			List<Certificado> certificados = daoCertificado.listar(Certificado.class, " situacao = 3 and aluno = "
					+ getAluno().getAluno().getId() + " and  idGrupoTurma = " + grupoTurma.getId());

			if (!certificados.isEmpty()) {
				Certificado cs = certificados.get(0);

				HashMap parametro = new HashMap<>();
				parametro.put("ALUNO_GRUPO", getAluno().getAluno().getId());
				parametro.put("GRUPO_ALUNO", grupoTurma.getId());
				ChamarRelatorio ch = new ChamarRelatorio("detalhado-aluno.jasper", parametro,
						"Relatório por grupo do aluno " + getAluno().getAluno().getNome());
				Session sessions = manager.unwrap(Session.class);
				sessions.doWork(ch);

			} else {
				ExibirMensagem.exibirMensagem(Mensagem.NADA_ENCONTRADO);
			}
		} catch (Exception e) {
			ExibirMensagem.exibirMensagem(Mensagem.ERRO);
			e.printStackTrace();
		}
	}

	public void preencherListaAlunosProfessor() {

		alunosAtivosProfessor = new ArrayList<>();
		alunosAtivosProfessor = relacoesProfessor.recuperarAlunoMovimentacaoAtivo();
	}
	
	public void selecionarAlunosTurmas() {
		//System.out.println(turma.getDescricao());
		 alunosTurmas = new ArrayList<AlunoTurma>();
		if(turma!=null) {
			alunosTurmas = daoAlunoTurma.listar(AlunoTurma.class, "turma.id="+turma.getId());
		}
	}
	
	public List<AlunoTurma> completarTodasTurmasProfessor(String str) {
				List<AlunoTurma> alunoSelecionado = new ArrayList<>();
				
					//alunosTurmas = daoAlunoTurma.listar(AlunoTurma.class, "turma.id="+turma.getId());
					for (AlunoTurma a : alunosTurmasProfessor) {
						if (a.getAluno().getNome().startsWith(str)) {
							alunoSelecionado.add(a);
						}
					}
			
			
				return alunoSelecionado;
			}

	public List<AlunoTurma> completarMovimentacaoProfessor(String str) {
//buscar somente a turma, tirar a movimentação
//somente relacionado ao grupo/turma que ele selecionou
		//System.out.println(str);
		
		
		List<AlunoTurma> alunoSelecionado = new ArrayList<>();
		if(turma!=null) {
			//alunosTurmas = daoAlunoTurma.listar(AlunoTurma.class, "turma.id="+turma.getId());
			for (AlunoTurma a : alunosTurmas) {
				if (a.getAluno().getNome().startsWith(str)) {
					alunoSelecionado.add(a);
				}
			}
		}
	
	
	
		return alunoSelecionado;
	}

	public void preencherListaTurma() {
		turmas = relacoesProfessor.recuperarTurmasProfessor();
	}

	public void preencherListaGrupoTurma() {

		grupoTurmas = relacoesProfessor.recuperarGrupoTurmasProfessor();
	}

	public List<Turma> completarTurma(String str) {
		//preencherListaTurma();
		List<Turma> turmasSelecionadas = new ArrayList<>();
		for (Turma t : turmas) {
			if (t.getDescricao().startsWith(str)) {
				turmasSelecionadas.add(t);
			}
		}
		return turmasSelecionadas;
	}

	public List<GrupoTurma> completarGrupoTurma(String str) {
		preencherListaGrupoTurma();
		List<GrupoTurma> grupoTurmaSelecionado = new ArrayList<>();
		for (GrupoTurma g : grupoTurmas) {
			if (g.getGrupo().getDescricao().toLowerCase().startsWith(str)) {
				grupoTurmaSelecionado.add(g);
			}
		}
		return grupoTurmaSelecionado;
	}
	
	

	public List<AlunoTurma> getAlunosTurmasProfessor() {
		return alunosTurmasProfessor;
	}



	public void setAlunosTurmasProfessor(List<AlunoTurma> alunosTurmasProfessor) {
		this.alunosTurmasProfessor = alunosTurmasProfessor;
	}



	public AlunoTurma getAluno() {
		return aluno;
	}

	public void setAluno(AlunoTurma aluno) {
		this.aluno = aluno;
	}

	public Turma getTurma() {
		return turma;
	}

	public void setTurma(Turma turma) {
		this.turma = turma;
	}

	public GrupoTurma getGrupoTurma() {
		return grupoTurma;
	}

	public void setGrupoTurma(GrupoTurma grupoTurma) {
		this.grupoTurma = grupoTurma;
	}

}
