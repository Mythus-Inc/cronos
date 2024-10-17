package base.controle;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import org.hibernate.Session;
import javax.persistence.EntityManager;

import base.modelo.Curso;
import base.modelo.Turma;
import dao.GenericDAO;
import util.ChamarRelatorio;
import util.ExibirMensagem;
import util.Mensagem;

@ViewScoped
@Named("relatorioBaseMB")
public class RelatorioBaseMB implements Serializable{

	private static final long serialVersionUID = 1L;

	private Curso curso;
	private Turma turma;
	private List<Turma> listTurma;
	private List<Curso> listCurso;
	
	@Inject
	private GenericDAO<Turma> daoTurma;
	
	@Inject
	private GenericDAO<Curso> daoCurso;
	
	@Inject
	private EntityManager manager;
	
	@PostConstruct
	public void inicializar() {
		listTurma = new ArrayList<>();
		listCurso = new ArrayList<>();
		curso = null;
		turma = null;
	}
	
	public void imprimirRelatorioAlunoPorTurma() {
		if(curso == null || turma == null || curso.getId() != turma.getCurso().getId()) {
			ExibirMensagem.exibirMensagem(Mensagem.ERRO);
		}else {
			
			try {
			
				HashMap parametro = new HashMap<>();
				parametro.put("IDTURMA", turma.getId());
				parametro.put("NOME_CURSO", turma.getCurso().getNome());
				parametro.put("ABREVIACAO_TURMA", turma.getAbreviacaoTurma());
				
				ChamarRelatorio ch = new ChamarRelatorio("alunosBuscadosPorTurma.jasper", 
						parametro, "relatorio_AlunosPorTurma ");
				
				Session session = manager.unwrap(Session.class);
				session.doWork(ch);
				
			}catch(Exception e) {
				e.printStackTrace();
				ExibirMensagem.exibirMensagem(Mensagem.ERRO);
			}
			
		}
	}
	
	public List<Turma> preencherListaTurmaPorCurso() {
		if(curso == null) {
			listTurma.clear();
		}else {
			listTurma = daoTurma.listarCodicaoLivre(Turma.class,
					"status = 1 and id_curso = "+curso.getId());
		}
		return listTurma;
	}
	
	public List<Curso> preencherListaCurso() {
		listCurso = daoCurso.listaComStatus(Curso.class);
		return listCurso;
	}
	
	public Curso getCurso() {
		return curso;
	}

	public void setCurso(Curso curso) {
		this.curso = curso;
	}

	public Turma getTurma() {
		return turma;
	}

	public void setTurma(Turma turma) {
		this.turma = turma;
	}

	public List<Turma> getListTurma() {
		return listTurma;
	}

	public void setListTurma(List<Turma> listTurma) {
		this.listTurma = listTurma;
	}

	public List<Curso> getListCurso() {
		return listCurso;
	}

	public void setListCurso(List<Curso> listCurso) {
		this.listCurso = listCurso;
	}
	
}
