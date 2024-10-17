package ac.controle;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.enterprise.context.SessionScoped;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import util.RecuperarRelacoesProfessor;
import ac.modelo.AlunoTurma;
import base.modelo.Aluno;

import dao.GenericDAO;

@ViewScoped
@Named("movimentacaoMB")
public class MovimentacaoMB implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private List<AlunoTurma> movimentacaosSecretaria;
	private List<AlunoTurma> movimentacaosAluno;
	private List<AlunoTurma> movimentacaosProfessor;
	private Aluno aluno;
	
	@Inject
	private GenericDAO<AlunoTurma> daoAlunoTurma; 
	
	@Inject
	private UsuarioSessaoMB usuarioSessao;
	
	@Inject
	private RecuperarRelacoesProfessor recuperarRelacoesProfessor;

	@PostConstruct
	public void inicializar() {
		
		movimentacaosSecretaria = new ArrayList<>();
		movimentacaosAluno = new ArrayList<>();
		movimentacaosProfessor = new ArrayList<>();

	}

	public void preencherMovimentacaoSecretaria() {
		movimentacaosSecretaria = daoAlunoTurma.listaComStatus(AlunoTurma.class);
	}

	public void preencherMovimentacaoAluno() {
		aluno = new Aluno();
		aluno = (Aluno) usuarioSessao.recuperarAluno();
		movimentacaosAluno = daoAlunoTurma.listar(AlunoTurma.class, " aluno.id = " + aluno.getId());
	
	}

	public void preencherMovimentacaoProfessor() {		
		
		//movimentacaosProfessor = new ArrayList<>();
		//movimentacaosProfessor = recuperarRelacoesProfessor.recuperarTodasMovimentacoesAtivas();
		
		
	}

	public List<AlunoTurma> getMovimentacaosSecretaria() {
		return movimentacaosSecretaria;
	}

	public void setMovimentacaosSecretaria(List<AlunoTurma> movimentacaosSecretaria) {
		this.movimentacaosSecretaria = movimentacaosSecretaria;
	}

	public List<AlunoTurma> getMovimentacaosAluno() {
		
		return movimentacaosAluno;
	}

	public void setMovimentacaosAluno(List<AlunoTurma> movimentacaosAluno) {
		this.movimentacaosAluno = movimentacaosAluno;
	}

	public List<AlunoTurma> getMovimentacaosProfessor() {
		return movimentacaosProfessor;
	}

	public void setMovimentacaosProfessor(List<AlunoTurma> movimentacaosProfessor) {
		this.movimentacaosProfessor = movimentacaosProfessor;
	}

}
