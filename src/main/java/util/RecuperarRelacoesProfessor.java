package util;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.crypto.AEADBadTagException;
import javax.faces.bean.ManagedBean;
import javax.inject.Inject;
import javax.inject.Named;

import org.springframework.core.env.SystemEnvironmentPropertySource;

import ac.controle.UsuarioSessaoMB;
import ac.modelo.AlunoTurma;
import ac.modelo.AtividadeTurma;
import ac.modelo.Certificado;
import ac.modelo.GrupoTurma;
import ac.modelo.ProfessorCurso;
import base.modelo.Curso;
import base.modelo.Matriz;
import base.modelo.Turma;
import dao.GenericDAO;
import dao.MovimentacaoAlunoDAO;

public class RecuperarRelacoesProfessor implements Serializable {

	private static final long serialVersionUID = 1L;

	private List<ProfessorCurso> professorCursos;
	private List<Turma> turmas;
	private List<Matriz> matriz;
	private List<Curso> cursos;
	private List<GrupoTurma> grupoTurmas;
	private List<AtividadeTurma> atividadeTurmas;
	private List<Certificado> certificados;

	@Inject
	private GenericDAO<ProfessorCurso> daoProfessorCurso;

	@Inject
	private UsuarioSessaoMB usuarioSessao;

	@Inject
	private MovimentacaoAlunoDAO movimentacaoAlunoDAO;

	@Inject
	private GenericDAO<GrupoTurma> daoGrupoTurma;

	@Inject
	private GenericDAO<AtividadeTurma> daoAtividadeTurma;

	@Inject
	private GenericDAO<Turma> daoTurma;

	@Inject
	private GenericDAO<Matriz> daoMatriz;

	@Inject
	private GenericDAO<Curso> daoCurso;

	@Inject
	private GenericDAO<Certificado> daoCertificado;

	@Inject
	private GenericDAO<AlunoTurma> daoAlunoTurma;



	@PostConstruct
	public void recuperarRelacoesProfessor() {

		turmas = new ArrayList<>();
		matriz = new ArrayList<>();
		cursos = new ArrayList<>();
		grupoTurmas = new ArrayList<>();
		certificados = new ArrayList<>();
		atividadeTurmas = new ArrayList<>();

	}

	public List<Turma> recuperarTurmasProfessor() {
		try {
			turmas = new ArrayList<>();
			professorCursos = new ArrayList<>();
			professorCursos = daoProfessorCurso.listar(ProfessorCurso.class,
					" professor = " + usuarioSessao.recuperarProfessor().getId());
			for (ProfessorCurso pc : professorCursos) {

				turmas.addAll(daoTurma.listar(Turma.class, " curso = " + pc.getCurso().getId()));

			}

		} catch (Exception e) {
			System.err.println("Erro recuperarTurmasProfessor ");
			e.printStackTrace();
		}
		return turmas;
	}

	public List<Matriz> recuperarMatrizProfessor() {
		try {
			matriz = new ArrayList<>();

			for (Turma pc : turmas) {

				if (correLista(pc) == true) {
					matriz.addAll(daoMatriz.listar(Matriz.class, " id = " + pc.getMatriz().getId()));
				}

			}

		} catch (Exception e) {
			System.err.println("Erro recuperarTurmasProfessor ");
			e.printStackTrace();
		}
		return matriz;
	}

	public boolean correLista(Turma turma) {

		for (Matriz m : matriz) {
			if (m.getDescricao().equals(turma.getMatriz().getDescricao())) {
				return false;
			}
		}
		return true;

	}

	public List<GrupoTurma> recuperarGrupoTurmasProfessor() {

		try {
			recuperarTurmasProfessor();
			recuperarMatrizProfessor();
			grupoTurmas = new ArrayList<>();
			for (Matriz m : matriz) {
				grupoTurmas.addAll(daoGrupoTurma.listar(GrupoTurma.class, " matriz = " + m.getId()));
			}
		} catch (Exception e) {
			System.err.println("Erro recuperarGrupoTurmasProfessor ");
			e.printStackTrace();
		}
		return grupoTurmas;
	}

	public List<AtividadeTurma> recuperarAtividadeTurmasProfessor() {
		try {
			recuperarTurmasProfessor();
			recuperarMatrizProfessor();

			for (Matriz m : matriz) {
				atividadeTurmas.addAll(daoAtividadeTurma.listar(AtividadeTurma.class, " matriz = " + m.getId()));
			}

		} catch (Exception e) {
			System.err.println("Erro recuperarAtividadeTurmasProfessor ");
			e.printStackTrace();
		}
		return atividadeTurmas;
	}

	public List<Curso> recuperarCursosProfessor() {

		try {
			professorCursos = new ArrayList<>();
			cursos = new ArrayList<>();
			professorCursos = daoProfessorCurso.listar(ProfessorCurso.class,
					" professor = " + usuarioSessao.recuperarProfessor().getId());
			for (ProfessorCurso pc : professorCursos) {
				cursos.addAll(daoCurso.listar(Curso.class, " id = " + pc.getCurso().getId()));
			}

		} catch (Exception e) {
			System.err.println("Erro recuperarCursosProfessor ");
			e.printStackTrace();
		}
		return cursos;
	}

	public List<Certificado> recuperarCertificados(Integer situacao, Turma turmaSel) {

		try {
			recuperarTurmasProfessor();
			if (turmaSel != null) {
				turmas = new ArrayList<>();
				turmas.add(turmaSel);
			}
			certificados = new ArrayList<>();

			String query = "";
			for (Turma t : turmas) {
				if (query.length() == 0) {
					query += " and (alunoTurma.turma = " + t.getId();
				} else {
					query += " or alunoTurma.turma = " + t.getId();
				}
			}
			if (query.length() > 0) {
				query += ")";
			}
			//System.out.println("QUERY " + query);

			certificados = daoCertificado.listar(Certificado.class,
					" situacao = " + situacao + "" + query + " order by alunoTurma.turma.descricao");

			// for (Turma t : turmas) {
			// certificados.addAll(daoCertificado.listar(Certificado.class,
			// " situacao = " + situacao + " and alunoTurma.turma = " + t.getId()));
			// }
		} catch (Exception e) {
			System.err.println("Erro recuperarCertificados ");
			e.printStackTrace();
		}
		return certificados;
	}

	public List<AlunoTurma> recuperarAlunoMovimentacaoAtivo() {

		List<AlunoTurma> alunosAtivos = new ArrayList<>();
		List<AlunoTurma> alunosTurmas = new ArrayList<>();
		List<AlunoTurma> alunosAtivosProfessor = new ArrayList<>();

		List<AlunoTurma> alunosTurmasTurma = new ArrayList<>();
		turmas = new ArrayList<>();
		recuperarTurmasProfessor();
		try {
			alunosAtivos = movimentacaoAlunoDAO.buscarAtivo();
			alunosTurmas = movimentacaoAlunoDAO.listarMaioresAlunoTurma();

			for (int m = 0; m <= alunosTurmas.size() - 1; m++) {
				for (int v = 0; v <= turmas.size() - 1; v++) {
					if (alunosTurmas.get(m).getTurma().getId() == turmas.get(v).getId()) {
						alunosTurmasTurma.add(alunosTurmas.get(m));
					}
				}
			}

			for (int c = 0; c <= alunosAtivos.size() - 1; c++) {
				for (int s = 0; s <= alunosTurmasTurma.size() - 1; s++) {
					if (alunosAtivos.get(c).getAluno().getId() == alunosTurmasTurma.get(s).getAluno()
							.getId()) {
						alunosAtivosProfessor.add(alunosAtivos.get(c));
					}
				}
			}

		} catch (

		Exception e) {
			System.err.println("Erro recuperarAlunoMovimentacaoAtivo ");
			e.printStackTrace();
		}

		return alunosAtivosProfessor;
	}

//	public List<Movimentacao> recuperarAlunoMovimentacaoTrancados() {
//
//		List<Movimentacao> alunosTrancados = new ArrayList<>();
//		List<AlunoTurma> alunosTurmas = new ArrayList<>();
//		// DAOMovimentacaoAluno daoMovimentacaoAluno = new DAOMovimentacaoAluno();
//		List<AlunoTurma> alunosTurmasTurma = new ArrayList<>();
//		List<Movimentacao> alunosTrancadosProfessor = new ArrayList<>();
//		turmas = new ArrayList<>();
//		recuperarTurmasProfessor();
//
//		try {
//			alunosTrancados = movimentacaoAlunoDAO.buscarTrancado();
//
//			alunosTurmas = movimentacaoAlunoDAO.listarMaioresAlunoTurma();
//
//			for (int m = 0; m <= alunosTurmas.size() - 1; m++) {
//				for (int v = 0; v <= turmas.size() - 1; v++) {
//					if (alunosTurmas.get(m).getTurma().getId() == turmas.get(v).getId()) {
//						alunosTurmasTurma.add(alunosTurmas.get(m));
//					}
//				}
//			}
//
//			for (int c = 0; c <= alunosTrancados.size() - 1; c++) {
//				for (int s = 0; s <= alunosTurmasTurma.size() - 1; s++) {
//					if (alunosTrancados.get(c).getAlunoTurma().getAluno().getId() == alunosTurmasTurma.get(s).getAluno()
//							.getId()) {
//						alunosTrancadosProfessor.add(alunosTrancados.get(c));
//					}
//				}
//			}
//
//		} catch (Exception e) {
//			System.err.println("Erro recuperarAlunoMovimentacaoTrancados");
//			e.printStackTrace();
//		}
//
//		return alunosTrancadosProfessor;
//	}

	public List<AlunoTurma> recuperarTodosAlunosTurmasAtivas() {
		List<AlunoTurma> alunosTurmas = new ArrayList<>();
		recuperarAtividadeTurmasProfessor();
		try {
			for (Turma t : turmas) {
				alunosTurmas.addAll(daoAlunoTurma.listar(AlunoTurma.class, " turma = " + t.getId()));
			}
		} catch (Exception e) {
			System.err.println("Erro recuperarTodosAlunosTurmasAtivas");
			e.printStackTrace();
		}
		return alunosTurmas;

	}

//	public List<Movimentacao> recuperarTodasMovimentacoesAtivas() {
//		List<Movimentacao> todosAlunos = new ArrayList<>();
//		List<AlunoTurma> alunosTurmas = new ArrayList<>();
//
//		List<AlunoTurma> alunosTurmasTurma = new ArrayList<>();
//		List<Movimentacao> alunos = new ArrayList<>();
//		turmas = new ArrayList<>();
//		recuperarTurmasProfessor();
//
//		try {
//			todosAlunos = daoMovimentacao.listaComStatus(Movimentacao.class);
//
//			alunosTurmas = daoAlunoTurma.listaComStatus(AlunoTurma.class);
//
//			for (int m = 0; m <= alunosTurmas.size() - 1; m++) {
//				for (int v = 0; v <= turmas.size() - 1; v++) {
//					if (alunosTurmas.get(m).getTurma().getId() == turmas.get(v).getId()) {
//						alunosTurmasTurma.add(alunosTurmas.get(m));
//					}
//				}
//			}
//
//			for (int c = 0; c <= todosAlunos.size() - 1; c++) {
//				for (int s = 0; s <= alunosTurmasTurma.size() - 1; s++) {
//					if (todosAlunos.get(c).getAlunoTurma().getAluno().getId() == alunosTurmasTurma.get(s).getAluno()
//							.getId()) {
//						alunos.add(todosAlunos.get(c));
//					}
//				}
//			}
//
//		} catch (Exception e) {
//			System.err.println("Erro recuperarTodasMovimentacoesAtivas");
//			e.printStackTrace();
//		}
//
//		return alunos;
//	}
}
