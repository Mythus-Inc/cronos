package util;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import ac.modelo.AlunoTurma;
import ac.modelo.Atividade;
import ac.modelo.AtividadeTurma;
import ac.modelo.Certificado;
import ac.modelo.GrupoTurma;
import ac.modelo.ProfessorCurso;
import base.modelo.Tipo;
import base.modelo.Turma; 
import dao.GenericDAO;

public class PermiteInativar implements Serializable {
	

	private static final long serialVersionUID = 1L;
	
	@Inject
	private GenericDAO<Turma> daoTurma; //faz as buscas
	
	@Inject
	private GenericDAO<ProfessorCurso> daoProfessorCurso; //faz as buscas
	
	@Inject
	private GenericDAO<AlunoTurma> daoAlunoTurma; //faz as buscas
	
	@Inject
	private GenericDAO<AtividadeTurma> daoAtividadeTurma; //faz as buscas
	
	@Inject
	private GenericDAO<Atividade> daoAtividade; //faz as buscas
	
	@Inject
	private GenericDAO<GrupoTurma> daoGrupoTurma; //faz as buscas
	
	@Inject
	private GenericDAO<Certificado> daoCertificado; //faz as buscas
	
	
	public Boolean verificarCursoComTurma(Long id) {
		
		List<Turma> turmas = new ArrayList<>();
		try {
			turmas = daoTurma.listar(Turma.class, " curso.id = " + id + " and status is true ");
			if (turmas.isEmpty()) {
				return true;
			}
		} catch (Exception e) {
			System.err.println("Erro verificarCursoComTurma ");
			e.printStackTrace();
		}
		return false;
	}

	public Boolean verificarCursoComProfessorCurso(Long id) {
		
		List<ProfessorCurso> professorCursos = new ArrayList<>();
		try {
			professorCursos = daoProfessorCurso.listar(ProfessorCurso.class, " curso.id = " + id + " and status is true ");
			if (professorCursos.isEmpty()) {
				return true;
			}
		} catch (Exception e) {
			System.err.println("Erro verificarCursoComProfessorCurso ");
			e.printStackTrace();
		}
		return false;
	}

	public Boolean verificarTurmaComAtividadeTurma(Long id) {
	
		List<AtividadeTurma> atividadesTurmas = new ArrayList<>();
		try {
			atividadesTurmas = daoAtividadeTurma.listar(AtividadeTurma.class, " turma.id = " + id + " and status is true ");
			if (atividadesTurmas.isEmpty()) {
				return true;
			}
		} catch (Exception e) {
			System.err.println("Erro verificarTurmaComAtividadeTurma ");
			e.printStackTrace();
		}
		return false;
	}

	public Boolean verificarTurmaComGrupoTurma(Long id) {
	
		List<GrupoTurma> gruposTurmas = new ArrayList<>();
		try {
			gruposTurmas = daoGrupoTurma.listar(GrupoTurma.class, " turma.id = " + id + " and status is true ");
			if (gruposTurmas.isEmpty()) {
				return true;
			}
		} catch (Exception e) {
			System.err.println("Erro verificarTurmaComGrupoTurma ");
			e.printStackTrace();
		}
		return false;
	}

	public Boolean verificarTurmaComAlunoTurma(Long id) {
		
		List<AlunoTurma> alunosTurmas = new ArrayList<>();
		try {
			alunosTurmas = daoAlunoTurma.listar(AlunoTurma.class, " turma.id = " + id + " and aluno.status is true ");
			if (alunosTurmas.isEmpty()) {
				return true;
			}
		} catch (Exception e) {
			System.err.println("Erro verificarTurmaComAlunoTurma");
			e.printStackTrace();
		}
		return false;
	}

	public Boolean verificarGrupoComGrupoTurma(Long id) {
		
		List<GrupoTurma> gruposTurmas = new ArrayList<>();
		try {
			gruposTurmas = daoGrupoTurma.listar(GrupoTurma.class, " grupo.id = " + id + " and status is true ");
			if (gruposTurmas.isEmpty()) {
				return true;
			}
		} catch (Exception e) {
			System.err.println("Erro verificarGrupoComGrupoTurma ");
			e.printStackTrace();
		}
		return false;
	}

	public Boolean verificarGrupoComAtividade(Long id) {
		
		List<Atividade> atividades = new ArrayList<>();
		try {
			atividades = daoAtividade.listar(Atividade.class, " grupo.id = " + id + " and status is true ");
			if (atividades.isEmpty()) {
				return true;
			}
		} catch (Exception e) {
			System.err.println("Erro verificarGrupoComAtividade ");
			e.printStackTrace();
		}
		return false;
	}

	public Boolean verificarAtividadeComAtividadeTurma(Long id) {
		

		List<AtividadeTurma> atividadesTurmas = new ArrayList<>();
		try {
			atividadesTurmas = daoAtividadeTurma.listar(AtividadeTurma.class, " atividade.id = " + id + " and status is true ");
			if (atividadesTurmas.isEmpty()) {
				return true;
			}
		} catch (Exception e) {
			System.err.println("Erro verificarAtividadeComAtividadeTurma ");
			e.printStackTrace();
		}
		return false;
	}

	public Boolean verificarAtividadeTurmaComCertificado(Long id) {

		List<Certificado> certificados = new ArrayList<>();
		try {
			certificados = daoCertificado.listar(Certificado.class, " atividadeTurma.id = " + id + " and status is true ");
			if (certificados.isEmpty()) {
				return true;
			}
		} catch (Exception e) {
			System.err.println("Erro verificarAtividadeTurmaComCertificado ");
			e.printStackTrace();
		}
		return false;
	}
	
	public Boolean verificarGrupoTurmaComAtividadeTurma(GrupoTurma grupoTurma) {
		
		List<AtividadeTurma> atividadesTurmas = new ArrayList<>();
		try {
			atividadesTurmas = daoAtividadeTurma.listar(AtividadeTurma.class, " atividade.grupo.id = " + grupoTurma.getGrupo().getId() + 
					" and matriz.id = "+grupoTurma.getMatriz().getId() + " and status is true ");
			if (atividadesTurmas.isEmpty()) {
				return true;
			}
		} catch (Exception e) {
			System.err.println("Erro verificarGrupoTurmaComAtividadeTurma");
			e.printStackTrace();
		}
		return false;
	}
}
