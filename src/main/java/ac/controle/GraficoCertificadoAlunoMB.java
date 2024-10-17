package ac.controle;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.HashMap;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.EntityManager;

import org.hibernate.Session;
import org.primefaces.model.chart.Axis;
import org.primefaces.model.chart.AxisType;
import org.primefaces.model.chart.BarChartModel;
import org.primefaces.model.chart.ChartSeries;

import ac.modelo.AlunoTurma;
import ac.modelo.Certificado;
import ac.modelo.GrupoTurma;
import base.modelo.Aluno; 
import dao.FiltrosDAO;
import dao.GenericDAO;
import util.ChamarRelatorio;
import util.ExibirMensagem;
import util.Mensagem;
import util.VerificaSituacaoTurma;

@ViewScoped
@Named("graficoCertificadoAlunoMB")
public class GraficoCertificadoAlunoMB  implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	
	private AlunoTurma alunoTurmaSelecionada;
	private List<AlunoTurma> listAlunoTurmas;
	private BarChartModel grafico;
	private AlunoTurma alunoTurma;
	private List<Certificado> certificados;
	private List<Long> idGrupos;
	private Set<Long> idGruposSelecionados;
	private ChartSeries totalGrupo;
	private ChartSeries totalCertificado;
	private List<Long> gruposTurmas;
	
	@Inject
	private FiltrosDAO daoFiltros;
	
	@Inject
	private UsuarioSessaoMB usuarioSessao;
	
	@Inject
	private GenericDAO<Certificado> daoCertificado;
	
	@Inject
	private GenericDAO<GrupoTurma> daoGrupoTurma;
	
	@Inject
	private GenericDAO<AlunoTurma> daoAlunoTurma;
	
	@Inject
	private GenericDAO<Aluno> daoAluno;
	
	@Inject
	private EntityManager manager;
	
	@Inject
	private VerificaSituacaoTurma verificaSituacaoTurma;

	@PostConstruct
	public void inicializar() {

		alunoTurmaSelecionada = new AlunoTurma();
		listAlunoTurmas = new ArrayList<>();

		grafico = new BarChartModel();
		alunoTurma = new AlunoTurma();
		
		certificados = new ArrayList<>();
		idGrupos = new ArrayList<>();
		idGruposSelecionados = new HashSet<>();

		totalGrupo = new ChartSeries();
		totalCertificado = new ChartSeries();
		gruposTurmas = new ArrayList<>();
		calcularHoras();
	}

	public void limpa() {

		grafico.clear();
		calcularHoras();

	}

	public Long recuperarTurmaAluno() {
		alunoTurma = (AlunoTurma) daoFiltros.buscarTurmaAluno(usuarioSessao.recuperarAluno().getId()).get(0);
		return alunoTurma.getTurma().getId();
	}

	public void recuperarCertificados() {

		if (alunoTurmaSelecionada.getId() == null) {

			certificados = daoFiltros.certificadosAlunos(usuarioSessao.recuperarAluno().getId(), 3);
		} else {

			certificados = daoFiltros.certificadosAlunosImprimir(alunoTurmaSelecionada.getId(), 3);
		}
	}

	public void ordenarIdGrupoTurma() {
		recuperarCertificados();
		idGrupos.clear();
		for (Certificado c : certificados) {
			idGrupos.add(c.getIdGrupoTurma());
		}
		Collections.sort(idGrupos);

	}

	public void escolherGruposTurmas() {
		ordenarIdGrupoTurma();
		idGruposSelecionados.clear();
		for (Long i : idGrupos) {
			idGruposSelecionados.add(i);
		}

	}

	public void calcularHoras() {
		Double somaCertificados = 0.0;
		escolherGruposTurmas();
		GrupoTurma grupoTurma = new GrupoTurma();
		GrupoTurma auxGrupoTurma = new GrupoTurma();

		gruposTurmas = new ArrayList<>();
		gruposTurmas.addAll(idGruposSelecionados);
		certificados = new ArrayList<>();
		for (int i = 0; i <= gruposTurmas.size() - 1; i++) {
			if (alunoTurmaSelecionada.getId() == null) {

				certificados = daoCertificado.listar(Certificado.class, " situacao = 3 and aluno = "
						+ usuarioSessao.recuperarAluno().getId() + " and idGrupoTurma = " + gruposTurmas.get(i));
			} else {
				certificados = daoCertificado.listar(Certificado.class, " situacao = 3 and alunoTurma = "
						+ alunoTurmaSelecionada.getId() + " and idGrupoTurma = " + gruposTurmas.get(i));
			}
			for (Certificado c : certificados) {
				somaCertificados = somaCertificados + c.getHoraComputada();
				grupoTurma =  daoGrupoTurma.listar(GrupoTurma.class, " id = " + c.getIdGrupoTurma()).get(0);
			}
			if (somaCertificados > 0.0) {
				criarGrafico(somaCertificados, grupoTurma);
				somaCertificados = 0.0;
			}
			grupoTurma = new GrupoTurma();
			certificados = new ArrayList<>();
		}
	}

	public void criarGrafico(Double somaCertificado, GrupoTurma grupoTurma) {
		totalGrupo.setLabel("Horas do Grupo " + grupoTurma.getGrupo().getDescricao());
		totalGrupo.set("Grupos", grupoTurma.getMaximoHoras());

		totalCertificado.setLabel("Horas dos Certificados " + grupoTurma.getGrupo().getDescricao());
		totalCertificado.set("Grupos", somaCertificado);

		grafico.addSeries(totalGrupo);
		grafico.addSeries(totalCertificado);

		grafico.setLegendPosition("ne");
		grafico.setAnimate(true);

		Axis yAxis = grafico.getAxis(AxisType.Y);
		yAxis.setLabel("Horas");

		totalGrupo = new ChartSeries();
		totalCertificado = new ChartSeries();

	}

	public void imprimirCertificadoGrupo() {

		try {

			if (alunoTurmaSelecionada.getId() == null) {
				ExibirMensagem.exibirMensagem(Mensagem.TURMAGRUPO);
			} else {
				
				 
				verificaSituacaoTurma.recuperarCertificadosAluno(alunoTurmaSelecionada);

				List<Certificado> certificados = daoCertificado.listar(Certificado.class,
						" alunoTurma = " + alunoTurmaSelecionada.getId() + " and atividadeTurma.matriz = "
								+ alunoTurmaSelecionada.getTurma().getMatriz().getId() + " and situacao = 3");

				if (!certificados.isEmpty()) {
					Certificado cs = certificados.get(0);
					
					HashMap parametro = new HashMap<>();
					parametro.put("ALUNOTURMA", alunoTurmaSelecionada.getAluno().getId());
					ChamarRelatorio ch = new ChamarRelatorio("grupoTurmaAlunoOutroAlterado.jasper", parametro, "Relatório situação nos grupos");
					Session sessions = manager.unwrap(Session.class);
					sessions.doWork(ch);
					
					  
				} else {
					ExibirMensagem.exibirMensagem(Mensagem.NADA_ENCONTRADO);
				}

			}
		} catch (Exception e) {
			ExibirMensagem.exibirMensagem(Mensagem.ERRO);
		}
	}

	public BarChartModel getGrafico() {
		return grafico;
	}

	public void setGrafico(BarChartModel grafico) {
		this.grafico = grafico;
	}

	public AlunoTurma getAlunoTurmaSelecionada() {
		return alunoTurmaSelecionada;
	}

	public void setAlunoTurmaSelecionada(AlunoTurma alunoTurmaSelecionada) {
		this.alunoTurmaSelecionada = alunoTurmaSelecionada;
	}

	public List<AlunoTurma> getListAlunoTurmas() {

		Aluno aluno =  daoAluno.buscarPorId(Aluno.class, usuarioSessao.recuperarAluno().getId());
		listAlunoTurmas = daoAlunoTurma.listar(AlunoTurma.class, " controle = 1 and aluno  = " + aluno.getId());

		return listAlunoTurmas;
	}

	public void setListAlunoTurmas(List<AlunoTurma> listAlunoTurmas) {
		this.listAlunoTurmas = listAlunoTurmas;
	}

}