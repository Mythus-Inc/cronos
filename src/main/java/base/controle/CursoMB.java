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

import ac.modelo.GrupoTurma;
import base.modelo.Curso;
import base.modelo.Tipo;
import base.service.CursoService;
import base.service.TipoService;
import util.ExibirMensagem;
import util.FecharDialog;
import util.Mensagem;
import util.PermiteInativar;
import dao.GenericDAO;

@ViewScoped
@Named("cursoMB")
public class CursoMB implements Serializable {

	private static final long serialVersionUID = 1L;

	private Curso curso;
	private List<Curso> cursos;
	private Date dataFechamento;

	@Inject
	private GenericDAO<Curso> daoCurso; // faz as buscas

	@Inject
	private PermiteInativar permiteInativar;

	@Inject
	private CursoService cursoService; // inserir no banco

	@PostConstruct
	public void inicializar() {
		// dao = new DAOGenerico();
		cursos = new ArrayList<>();
		criarNovoCurso();
		preencherListaCurso();
	}

	public void salvar() {

		try {
			if (curso.getId() == null) {
				if (verificarCursoIguais(curso)) {
					ExibirMensagem.exibirMensagem(Mensagem.CADASTROCURSO);
				} else {

					curso.setNome(curso.getNome().toUpperCase());
					curso.setAbreviacaoCurso(curso.getAbreviacaoCurso().toUpperCase());
					curso.setStatus(true);
					curso.setDataCadastro(new Date());
					cursoService.inserirAlterar(curso);
					criarNovoCurso();
					ExibirMensagem.exibirMensagem(Mensagem.SUCESSO);
					preencherListaCurso();
					FecharDialog.fecharDialogCurso();
				}
			} else {
				
				
				
				if (verificarCursoIguais(curso) && verificarCursoIguaisAlterar(curso)) {
					ExibirMensagem.exibirMensagem(Mensagem.CADASTROCURSO);
					 
				} else { 
					curso.setNome(curso.getNome().toUpperCase());
					curso.setAbreviacaoCurso(curso.getAbreviacaoCurso().toUpperCase());
					cursoService.inserirAlterar(curso);
					ExibirMensagem.exibirMensagem(Mensagem.SUCESSO);
					criarNovoCurso();
					preencherListaCurso();
					FecharDialog.fecharDialogCurso();
				}
			}
		}

		catch (Exception e) {
			ExibirMensagem.exibirMensagem(Mensagem.ERRO);
		}

	}

	public Boolean verificarCursoIguais(Curso curso) {
		try {
			List<Curso> verificador = new ArrayList<>();
			verificador = daoCurso.listar(Curso.class, " nome = '" + curso.getNome().toUpperCase() + "'");
			if (verificador.isEmpty())
				return false;
		} catch (Exception e) {
			System.err.println("Erro no metodo verificarGrupoTurmaIguais");
			e.printStackTrace();
		}
		return true;
	}

	public Boolean verificarCursoIguaisAlterar(Curso curso) {
		try {
			List<Curso> verificador = new ArrayList<>();
			verificador = daoCurso.listar(Curso.class,
					" nome = '" + curso.getNome().toUpperCase() + "' and id = " + curso.getId());
			if (verificador.isEmpty())
				return true;
		} catch (Exception e) {
			System.err.println("Erro no metodo verificarGrupoTurmaIguais");
			e.printStackTrace();
		}
		return false;
	}

	public void inativar(Curso curso) {

		try {
			if (permiteInativar.verificarCursoComTurma(curso.getId())) {
				if (permiteInativar.verificarCursoComProfessorCurso(curso.getId())) {
					curso.setStatus(false);
					cursoService.inserirAlterar(curso);
					ExibirMensagem.exibirMensagem(Mensagem.SUCESSO);
					preencherListaCurso();
				} else {
					ExibirMensagem.exibirMensagem(Mensagem.CURSO_COM_PROFESSOR_CURSO);
				}
			} else {
				ExibirMensagem.exibirMensagem(Mensagem.CURSO_COM_TURMA);
			}
		} catch (Exception e) {
			ExibirMensagem.exibirMensagem(Mensagem.ERRO);
		}
	}

	public void preencherListaCurso() {
		cursos = daoCurso.listaComStatus(Curso.class);
	}

	public void criarNovoCurso() {
		curso = new Curso();
	}

	public Curso getCurso() {
		return curso;
	}

	public void setCurso(Curso curso) {
		this.curso = curso;
	}

	public List<Curso> getCursos() {
		return cursos;
	}

	public void setCursos(List<Curso> cursos) {
		this.cursos = cursos;
	}

	public Date getDataFechamento() {
		return dataFechamento;
	}

	public void setDataFechamento(Date dataFechamento) {
		this.dataFechamento = dataFechamento;
	}
}
