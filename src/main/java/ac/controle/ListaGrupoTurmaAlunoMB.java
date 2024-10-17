package ac.controle;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;

import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import ac.modelo.AlunoTurma;
import ac.modelo.AtividadeTurma;
import ac.modelo.GrupoTurma;
import base.modelo.Aluno;
import dao.FiltrosDAO;
import dao.GenericDAO;

@ViewScoped
@Named("listaGrupoTurmaAlunoMB")
public class ListaGrupoTurmaAlunoMB implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private List<AtividadeTurma> atividadesTurmas;
	private List<GrupoTurma> gruposTurmas;

	private AlunoTurma alunoTurma;
	private List<AlunoTurma> listAlunoTurmas;
	private GrupoTurma grupoTurma;
	private AtividadeTurma atividadeTurma;

	private AlunoTurma alunoTurmaSelecionada;
	
	@Inject
	private FiltrosDAO daoFiltros;
	
	@Inject
	private UsuarioSessaoMB usuarioSessao;
	
    @Inject
	private GenericDAO<Aluno> daoAluno;
	
    @Inject
	private GenericDAO<AlunoTurma> daoAlunoTurma;
	
	@PostConstruct
	public void inicializar() {


		alunoTurma = new AlunoTurma();
		atividadesTurmas = new ArrayList<>();
		gruposTurmas = new ArrayList<>();
		
		grupoTurma = new GrupoTurma();
		listAlunoTurmas = new ArrayList<>();
		alunoTurmaSelecionada = new AlunoTurma();
		atividadeTurma = new AtividadeTurma();
	
	
	}

	private Long recuperarTurmaAluno() {
		alunoTurma = (AlunoTurma) daoFiltros.buscarTurmaAluno(usuarioSessao.recuperarAluno().getId()).get(0);
		
		return alunoTurma.getId();
		
	}

	public void preencherGruposTurmas() {
		ListaAtividadesTurmaAlunosMB atividade = new ListaAtividadesTurmaAlunosMB();
	
		gruposTurmas = daoFiltros.gruposTurmaAluno(alunoTurmaSelecionada.getTurma().getMatriz().getId());	
		atividadesTurmas = daoFiltros.atividadesTurmaAluno(alunoTurmaSelecionada.getTurma().getMatriz().getId());

	}
	
	public List<GrupoTurma> getGruposTurmas() {
		return gruposTurmas;
	}
	public void setGruposTurmas(List<GrupoTurma> gruposTurmas) {
		this.gruposTurmas = gruposTurmas;
	}
	public GrupoTurma getGrupoTurma() {
		return grupoTurma;
	}
	public void setGrupoTurma(GrupoTurma grupoTurma) {
		this.grupoTurma = grupoTurma;
	}

	public AlunoTurma getAlunoTurmaSelecionada() {
		return alunoTurmaSelecionada;
	}

	public void setAlunoTurmaSelecionada(AlunoTurma alunoTurmaSelecionada) {
		this.alunoTurmaSelecionada = alunoTurmaSelecionada;
	}

	public List<AlunoTurma> getListAlunoTurmas() {
		
		Aluno  aluno = daoAluno.buscarPorId(Aluno.class, usuarioSessao.recuperarAluno().getId());
		
	    listAlunoTurmas =  daoAlunoTurma.listar(AlunoTurma.class, " controle = 1 and aluno  = "+aluno.getId());
		
		return listAlunoTurmas;
	} 

	public void setListAlunoTurmas(List<AlunoTurma> listAlunoTurmas) {
		this.listAlunoTurmas = listAlunoTurmas;
	}

	public List<AtividadeTurma> getAtividadesTurmas() {
		return atividadesTurmas;
	}

	public void setAtividadesTurmas(List<AtividadeTurma> atividadesTurmas) {
		this.atividadesTurmas = atividadesTurmas;
	}

	public AtividadeTurma getAtividadeTurma() {
		return atividadeTurma;
	}

	public void setAtividadeTurma(AtividadeTurma atividadeTurma) {
		this.atividadeTurma = atividadeTurma;
	}

	
	
}
