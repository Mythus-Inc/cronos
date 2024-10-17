package cope.controle;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
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
import cope.modelo.Participante;
import cope.modelo.Projeto;
import dao.GenericDAO;

@ViewScoped
@Named("relatorioCopeMB")
public class RelatorioCopeMB implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	

	@Inject
	private GenericDAO<Participante> daoParticipante;
	@Inject
	private GenericDAO<Projeto> daoProjeto;

	@Inject
	private EntityManager manager;

	@Inject
	private VerificaSituacaoTurma verificaSituacaoTurma;

	private Date dataInicio;
	private Date DataFim;

	@PostConstruct
	public void inicializar() {

	}

	public void imprimirRelatorioProfessor() {
		try {
			List<Participante> listParticipante = daoParticipante.listar(Participante.class, " funcao = 'coordenador'");

			if (!listParticipante.isEmpty()) {

				HashMap parametro = new HashMap<>();
				ChamarRelatorio ch = new ChamarRelatorio("projetoServidor.jasper", parametro,
						"Relatório" + "Relatório Projetos Servidor ");
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

	public void imprimirRelatorioProjetoAndamento() {
		try {
			List<Projeto> listProjeto = daoProjeto.listarCodicaoLivre(Projeto.class, " situacao != 'FINALIZADO'");

			if (!listProjeto.isEmpty()) {

				HashMap parametro = new HashMap<>();
				ChamarRelatorio ch = new ChamarRelatorio("projetoAndamento.jasper", parametro,
						"Relat�rio" + "Relat�rio Projetos Em Andamento ");
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

	public void imprimirRelatorioProjetoAtraso() {
		try {

			Date data = new Date();

			// LocalDate hoje = LocalDate.now();
			// String formatada =
			// hoje.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));

			// List<Projeto> listProjeto =
			// daoProjeto.listarCodicaoLivre(Projeto.class, " situacao !=
			// 'FINALIZADO' and proximoRelatorio < '"+formatada+" 00:00:00.0'");

			/*
			 * List<Projeto> listProjetos =
			 * daoProjeto.listarCodicaoLivre(Projeto.class,
			 * "situacao != 'FINALIZADO'"); for(Projeto p : listProjetos){
			 * System.out.println("d "+p.getProximoRelatorio()); }
			 * 
			 * if (!listProjeto.isEmpty()) {
			 */
			// System.out.println("dia "+data.getDay() +"m�s "+data.getMonth()
			// +" ano "+data.getYear() );

			HashMap parametro = new HashMap<>();
			parametro.put("DATA", data);
			ChamarRelatorio ch = new ChamarRelatorio("projetoComRelaAtrasado.jasper", parametro,
					"Relatório" + "Relatório Projetos Em Atraso ");
			Session sessions = manager.unwrap(Session.class);
			sessions.doWork(ch);

			/*
			 * } else { ExibirMensagem.exibirMensagem(Mensagem.NADA_ENCONTRADO);
			 * }
			 */
		} catch (Exception e) {
			e.printStackTrace();
			ExibirMensagem.exibirMensagem(Mensagem.ERRO);
		}
	}

	public void imprimirRelatorioBolsista() {
		try {

			// LocalDate hoje = LocalDate.now();
			// String formatada =
			// hoje.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));

			// List<Projeto> listProjeto =
			// daoProjeto.listarCodicaoLivre(Projeto.class, " situacao !=
			// 'FINALIZADO' and proximoRelatorio < '"+formatada+" 00:00:00.0'");

			/*
			 * List<Projeto> listProjetos =
			 * daoProjeto.listarCodicaoLivre(Projeto.class,
			 * "situacao != 'FINALIZADO'"); for(Projeto p : listProjetos){
			 * System.out.println("d "+p.getProximoRelatorio()); }
			 * 
			 * if (!listProjeto.isEmpty()) {
			 */
			// System.out.println("dia "+data.getDay() +"m�s "+data.getMonth()
			// +" ano "+data.getYear() );

			Date d = new Date();
			
			System.out.println("data "+d);
			 
			
			System.out.println("inicio "+dataInicio);
			
			java.util.Date utilStartDate = dataInicio;
			java.sql.Date sqlStartDate = new java.sql.Date(utilStartDate.getTime());
			
			java.util.Date utilStartDateFim = DataFim;
			java.sql.Date sqlStartDateFim = new java.sql.Date(utilStartDateFim.getTime());
			
			HashMap parametro = new HashMap<>();
			parametro.put("DATAINICIO", sqlStartDate);
			parametro.put("DATAFIM", sqlStartDateFim);
			ChamarRelatorio ch = new ChamarRelatorio("projetoBolsista.jasper", parametro,
					"Relatório" + "Relatório Bolsistas ");
			Session sessions = manager.unwrap(Session.class);
			sessions.doWork(ch);

			/*
			 * } else { ExibirMensagem.exibirMensagem(Mensagem.NADA_ENCONTRADO);
			 * }
			 */
		} catch (Exception e) {
			e.printStackTrace();
			ExibirMensagem.exibirMensagem(Mensagem.ERRO);
		}
	}

	public Date getDataInicio() {
		return dataInicio;
	}

	public void setDataInicio(Date dataInicio) {
		this.dataInicio = dataInicio;
	}

	public Date getDataFim() {
		return DataFim;
	}

	public void setDataFim(Date dataFim) {
		DataFim = dataFim;
	}

}
