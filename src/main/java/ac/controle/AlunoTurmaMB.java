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
@Named("alunoTurmaMB")
public class AlunoTurmaMB implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private List<AlunoTurma> alunoTurmaSecretaria;
	private List<AlunoTurma> alunoTurmaAluno;
	private List<AlunoTurma> alunoTurmaProfessor;
	
	private Aluno aluno;
	
	
	@Inject
	private GenericDAO<AlunoTurma> daoAlunoTurma; 
	
	@Inject
	private UsuarioSessaoMB usuarioSessao;
	
    @Inject
	private RecuperarRelacoesProfessor recuperarRelacoesProfessor;

	@PostConstruct
	public void inicializar (){
	
		alunoTurmaSecretaria = new ArrayList<>();
		alunoTurmaAluno = new ArrayList<>();
		
		
	}

	public void preencherAlunoTurmasSecretaria() {
		alunoTurmaSecretaria = daoAlunoTurma.listaComStatus(AlunoTurma.class);
	}

	public void preencherAlunoTurmasAluno() {
		
		aluno = new Aluno();
		aluno = (Aluno) usuarioSessao.recuperarAluno();
		
		alunoTurmaAluno = daoAlunoTurma.listar(AlunoTurma.class, " aluno = " + aluno.getId());
		
	
	}

	public void preencherAlunoTurmasProfessor() {
		
		alunoTurmaProfessor = recuperarRelacoesProfessor.recuperarTodosAlunosTurmasAtivas();
	}

	public List<AlunoTurma> getAlunoTurmaSecretaria() {
		return alunoTurmaSecretaria;
	}

	public void setAlunoTurmaSecretaria(List<AlunoTurma> alunoTurmaSecretaria) {
		this.alunoTurmaSecretaria = alunoTurmaSecretaria;
	}

	public List<AlunoTurma> getAlunoTurmaAluno() {
		
		return alunoTurmaAluno;
	}

	public void setAlunoTurmaAluno(List<AlunoTurma> alunoTurmaAluno) {
		this.alunoTurmaAluno = alunoTurmaAluno;
	}

	public List<AlunoTurma> getAlunoTurmaProfessor() {
		return alunoTurmaProfessor;
	}

	public void setAlunoTurmaProfessor(List<AlunoTurma> alunoTurmaProfessor) {
		this.alunoTurmaProfessor = alunoTurmaProfessor;
	}
}
