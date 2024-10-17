package base.controle;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.enterprise.context.SessionScoped;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import util.ExibirMensagem;
import util.FecharDialog;
import util.Mensagem;
import util.PermiteInativar;
import util.RecuperarRelacoesProfessor;
import base.modelo.Curso;
import base.modelo.Matriz;
import base.modelo.Turma;
import base.service.CursoService;
import base.service.TurmaService;
import dao.GenericDAO;

@ViewScoped
@Named("turmaMB")
public class TurmaMB implements Serializable {

	private static final long serialVersionUID = 1L;

	private Turma turma;
	private List<Turma> turmas;
	private List<Curso> cursos;
	private List<Turma> turmasProfessor;
	private List<Matriz> listMatriz;
	private List<Curso> cursosProfessor;

	@Inject
	private PermiteInativar permiteInativar;

	@Inject
	private GenericDAO<Turma> daoTurma;

	@Inject
	private GenericDAO<Matriz> daoMatriz;

	@Inject
	private RecuperarRelacoesProfessor relacoesProfessor;

	@Inject
	private GenericDAO<Curso> daoCurso; // faz as buscas

	@Inject
	private TurmaService turmaService; // inserir no banco

	@PostConstruct
	public void inicializar() {

		turma = new Turma();
		turmas = new ArrayList<>();
		cursos = new ArrayList<>();
		turmasProfessor = new ArrayList<>();
		cursosProfessor = new ArrayList<>();
		listMatriz = new ArrayList<>();
		criarNovaTurma();
		preencherListaTurma();

	}

	public void salvar() {

		List<Turma> listTurma = new ArrayList<>();
		if (validarDataCursoComTurma().getDataAbertura().after(turma.getDataInicioTurma())) {
			ExibirMensagem.exibirMensagem(Mensagem.DATA_TURMA);
		} else {

			try {
				if (turma.getId() == null) {
					if (verificarTurmaIguais(turma)) {
						ExibirMensagem.exibirMensagem(Mensagem.TURMA);
					} else {
						turma.setDescricao(turma.getDescricao().toUpperCase());
						turma.setAbreviacaoTurma(turma.getAbreviacaoTurma().toUpperCase());
						turma.setDataCadastro(new Date());
						turma.setStatus(true);
						turmaService.inserirAlterar(turma);
						ExibirMensagem.exibirMensagem(Mensagem.SUCESSO);
						FecharDialog.fecharDialogTurma();
						preencherListaTurma();
					}
				} else {
					if (verificarTurmaIguais(turma) && verificarTurmaIguaisAlterar(turma)) {
						ExibirMensagem.exibirMensagem(Mensagem.TURMA);
					} else {
						turma.setDescricao(turma.getDescricao().toUpperCase());
						turma.setAbreviacaoTurma(turma.getAbreviacaoTurma().toUpperCase());
						turmaService.inserirAlterar(turma);
						ExibirMensagem.exibirMensagem(Mensagem.SUCESSO);
						FecharDialog.fecharDialogTurma();
						preencherListaTurma();

					}
				}

			} catch (Exception e) {
				ExibirMensagem.exibirMensagem(Mensagem.ERRO);
				FecharDialog.fecharDialogTurma();
			}
			criarNovaTurma();
		}

	}



	public Boolean verificarTurmaIguais(Turma turma) {
		try {
			List<Turma> verificador = new ArrayList<>();
			verificador = daoTurma.listar(Turma.class, " descricao = '" + turma.getDescricao().toUpperCase()
					+ "' and matriz = " + turma.getMatriz().getId());
			if (verificador.isEmpty())
				return false;
		} catch (Exception e) {
			System.err.println("Erro no metodo verificarGrupoTurmaIguais");
			e.printStackTrace();
		}
		return true;
	}

	public Boolean verificarTurmaIguaisAlterar(Turma turma) {
		try {
			List<Turma> verificador = new ArrayList<>();
			verificador = daoTurma.listar(Turma.class, " descricao = '" + turma.getDescricao().toUpperCase()
					+ "' and matriz = " + turma.getMatriz().getId() + " and id = " + turma.getId());
			if (verificador.isEmpty())
				return true;
		} catch (Exception e) {
			System.err.println("Erro no metodo verificarGrupoTurmaIguais");
			e.printStackTrace();
		}
		return false;
	}

	public void inativar(Turma turma) {

		try {
			if (permiteInativar.verificarTurmaComAlunoTurma(turma.getId())) {
				if (permiteInativar.verificarTurmaComAtividadeTurma(turma.getId())) {
					if (permiteInativar.verificarTurmaComGrupoTurma(turma.getId())) {
						turma.setStatus(false);
						turmaService.inserirAlterar(turma);
						ExibirMensagem.exibirMensagem(Mensagem.SUCESSO);
						preencherListaTurma();
					} else {
						ExibirMensagem.exibirMensagem(Mensagem.TURMA_COM_GRUPO_TURMA);
					}
				} else {
					ExibirMensagem.exibirMensagem(Mensagem.TURMA_COM_ATIVIDADE_TURMA);
				}
			} else {
				ExibirMensagem.exibirMensagem(Mensagem.TURMA_COM_ALUNO_TURMA);
			}
		} catch (Exception e) {
			ExibirMensagem.exibirMensagem(Mensagem.ERRO);
		}
	}

	public void criarNovaTurma() {
		turma = new Turma();
	}

	public void preencherListaTurma() {
		turmas = daoTurma.listaComStatus(Turma.class);
	}

	public void preencherListaCurso() {
		cursos = daoCurso.listaComStatus(Curso.class);
	}

	public void preencherListaMatriz() {
		listMatriz = daoMatriz.listaComStatus(Matriz.class);
	}

	// public void preencherListaCursoProfessor() {
	//
	// cursosProfessor = relacoesProfessor.recuperarCursosProfessor();
	// }

	public Curso validarDataCursoComTurma() {
		Curso curso = new Curso();
		curso = daoCurso.listar(Curso.class, " id = " + turma.getCurso().getId()).get(0);
		return curso;
	}

	public List<Curso> completarCursos(String str) {
		preencherListaCurso();
		List<Curso> cursosSelecionados = new ArrayList<>();
		for (Curso cur : cursos) {
			if (cur.getNome().toLowerCase().startsWith(str)) {
				cursosSelecionados.add(cur);
			}
		}
		return cursosSelecionados;
	}

	public List<Matriz> completarMatriz(String str) {
		preencherListaMatriz();
		List<Matriz> matrizSelecionados = new ArrayList<>();
		for (Matriz cur : listMatriz) {
			if (cur.getDescricao().toLowerCase().startsWith(str)) {
				matrizSelecionados.add(cur);
			}
		}
		return matrizSelecionados;
	}

	public List<Curso> completarCursosProfessor(String str) {
		// preencherListaCursoProfessor();
		List<Curso> cursosSelecionados = new ArrayList<>();
		for (Curso cur : cursosProfessor) {
			if (cur.getNome().toLowerCase().startsWith(str)) {
				cursosSelecionados.add(cur);
			}
		}
		return cursosSelecionados;
	}

	public Turma getTurma() {
		return turma;
	}

	public void setTurma(Turma turma) {
		this.turma = turma;
	}

	public List<Turma> getTurmas() {
		return turmas;
	}

	public void setTurmas(List<Turma> turmas) {
		this.turmas = turmas;
	}

	public List<Curso> getCursos() {
		return cursos;
	}

	public void setCursos(List<Curso> cursos) {
		this.cursos = cursos;
	}

	public List<Turma> getTurmasProfessor() {
		return turmasProfessor;
	}

	public void setTurmasProfessor(List<Turma> turmasProfessor) {
		this.turmasProfessor = turmasProfessor;
	}

}
