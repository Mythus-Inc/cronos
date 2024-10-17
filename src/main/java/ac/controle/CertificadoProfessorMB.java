package ac.controle;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.enterprise.context.SessionScoped;
import javax.faces.bean.ManagedBean;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import util.CalculoEquivalencia;
import util.ExibirMensagem;
import util.FecharDialog;
import util.Mensagem;
import util.RecuperarRelacoesProfessor;
import ac.modelo.AtividadeTurma;
import ac.modelo.Certificado;
import ac.modelo.GrupoTurma;
import base.modelo.Servidor;
import base.modelo.Turma;
import base.service.CertificadoService;
import dao.GenericDAO;

@ViewScoped
@Named("certificadoProfessorMB")
public class CertificadoProfessorMB implements Serializable {

	private static final long serialVersionUID = 1L;

	private Certificado certificado;

	private List<Certificado> certificadosPendentes;
	private List<Certificado> certificadosInvalidos;
	private List<Certificado> certificadosValidados;
	private List<AtividadeTurma> atividadeTurmas;
	private Boolean calculaEquivalencia;
	private Double horaCertificado;
	private List<Turma> turmas;
	private Turma turmaSelecionada=new Turma();
	private String filtroNome="";

	@Inject
	private CertificadoService certificadoService;

	@Inject
	private GenericDAO<GrupoTurma> daoGrupoTurma;

	@Inject
	private UsuarioSessaoMB usuarioSessao;

	@Inject
	private RecuperarRelacoesProfessor relacoesProfessor;

	@Inject
	private CalculoEquivalencia calculoEquivalencia;

	@PostConstruct
	public void inicializar() {
		certificado = new Certificado();
		turmas = new ArrayList<>();
		certificadosPendentes = new ArrayList<>();
		certificadosInvalidos = new ArrayList<>();
		certificadosValidados = new ArrayList<>();
		atividadeTurmas = new ArrayList<>();
		horaCertificado = 0.0;
		//preencherListaPendentes();
		//preencherListaValidados();
		//preencherListaInvalidos();
		buscarTurmas();

	}
	
	

	private void buscarTurmas() {
		turmas = relacoesProfessor.recuperarTurmasProfessor();
	}

	public void validar() {
		try {
			certificado.setSituacao(3);
			Servidor professorRecuperarSesao = (Servidor) usuarioSessao.recuperarProfessor(); // alterei professor para
																								// servidor
			certificado.setValidadoProfessor(
					professorRecuperarSesao.getNome() + ", " + professorRecuperarSesao.getUsuario());
			certificadoService.inserirAlterar(certificado);
			ExibirMensagem.exibirMensagem(Mensagem.SUCESSO);
			FecharDialog.fecharDialogCertificadoValidarProfessor();
			FecharDialog.fecharDialogCertificadoDetalhe();
			preencherListaPendentes();
			//preencherListaValidados();
			//preencherListaInvalidos();
		} catch (Exception e) {
			ExibirMensagem.exibirMensagem(Mensagem.ERRO);
		}
		criarNovoCertificado();
	}

	public void invalidar() {
		if(!certificado.getJustificativaProfessor().equals("")) {
		try {
			certificado.setSituacao(4);
			Servidor professorRecuperarSesao = (Servidor) usuarioSessao.recuperarProfessor();
			certificado.setValidadoProfessor(
					professorRecuperarSesao.getNome() + ", " + professorRecuperarSesao.getUsuario());
			certificadoService.inserirAlterar(certificado);
			ExibirMensagem.exibirMensagem(Mensagem.SUCESSO);
			FecharDialog.fecharDialogCertificadoInvalidarProfessor();
			FecharDialog.fecharDialogCertificadoDetalhe();
			preencherListaPendentes();
			//preencherListaValidados();
			//preencherListaInvalidos();
			if(certificado.getAluno().getUsuario()!=null && certificado.getAluno().getNome()!=null) {
			Thread t = new Thread(new util.EnviarEmailThread(certificado.getAluno().getUsuario(),
					 "Mensagem do CRONOS - Atividades Complementares do IFPR",
					"Olá, " + certificado.getAluno().getNome()+".<br/><br/> O seu certificado <b>"+certificado.getDescricao()+"</b> foi invalidado: "+certificado.getJustificativaProfessor()+". <br/>Acesse o sistema Cronos e faça uma nova submissão, caso necessário." ));
			t.start();
			}
		} catch (Exception e) {
			ExibirMensagem.exibirMensagem(Mensagem.ERRO);
		}
		criarNovoCertificado();
		}else {
			ExibirMensagem.exibirMensagem("Inserir uma justificativa");
		}
	}

	public void pender() {
		try {
			certificado.setSituacao(1);
			Servidor professorRecuperarSesao = (Servidor) usuarioSessao.recuperarProfessor();
			certificado.setValidadoProfessor(
					professorRecuperarSesao.getNome() + ", " + professorRecuperarSesao.getUsuario());
			certificadoService.inserirAlterar(certificado);
			ExibirMensagem.exibirMensagem(Mensagem.SUCESSO);
			FecharDialog.fecharDialogPendente();
			preencherListaPendentes();
			preencherListaValidados();
			preencherListaInvalidos();
		} catch (Exception e) {
			ExibirMensagem.exibirMensagem(Mensagem.ERRO);
		}
		criarNovoCertificado();
	}

	public void alterar(Boolean criarNovo) {
		if (certificado.getQuantidadeMaximaHora() <= 0) {
			ExibirMensagem.exibirMensagem(Mensagem.QUANTIDADE_HORAS);
		} else {
			if (!certificado.getDataInicio().after(certificado.getDataFinalizado())) {
				try {
					certificado.setDescricao(certificado.getDescricao().replace("'", "").replace("=", "")
							.replace("<", "").replace("&", ""));
					certificado.setInstituicao(certificado.getInstituicao().replace("'", "").replace("=", "")
							.replace("<", "").replace("&", ""));

					if (calculaEquivalencia) {
						certificado.setHoraComputada(calculoEquivalencia.calcularHorasCertificado(certificado));
					}

					certificado.setIdGrupoTurma(certificado.getAtividadeTurma().getGrupoTurma().getId());

					certificadoService.inserirAlterar(certificado);
					ExibirMensagem.exibirMensagem(Mensagem.SUCESSO);
					FecharDialog.fecharDialogCertificado();					
					preencherListaPendentes();
					preencherListaValidados();
if(criarNovo) {
					criarNovoCertificado();
}
				} catch (Exception e) {
					ExibirMensagem.exibirMensagem(Mensagem.ERRO);
				}
			} else {
				ExibirMensagem.exibirMensagem(Mensagem.DATA_FINALIZACAO);
			}
		}

	}

	public void buscarCertificadosTurma() {
		System.out.println("AQUIs");
		certificadosPendentes = relacoesProfessor.recuperarCertificados(1, turmaSelecionada);
		
		System.out.println(certificadosPendentes.size());
	}

	public void preencherListaPendentes() {

		certificadosPendentes = relacoesProfessor.recuperarCertificados(1, null);
	}

	public void preencherListaValidados() {
		certificadosValidados = relacoesProfessor.recuperarCertificados(3, null);
	}

	public void preencherListaInvalidos() {
		certificadosInvalidos = relacoesProfessor.recuperarCertificados(4, null);
	}

	public void preencherListaAtividadeTurma() {
		atividadeTurmas = relacoesProfessor.recuperarAtividadeTurmasProfessor();
	}

	public List<AtividadeTurma> completarAtividadeTurma(String str) {
		preencherListaAtividadeTurma();
		List<AtividadeTurma> atividadeTurmaSelecionados = new ArrayList<>();
		for (AtividadeTurma at : atividadeTurmas) {
			if (at.getAtividade().getDescricao().toLowerCase().startsWith(str)) {
				atividadeTurmaSelecionados.add(at);
			}
		}
		return atividadeTurmaSelecionados;
	}

//	public GrupoTurma recuperarGrupoTurma(AtividadeTurma atividadeTurma) {
//		GrupoTurma grupoTurma = new GrupoTurma();
//		grupoTurma = (GrupoTurma) daoGrupoTurma
//				.listar(GrupoTurma.class, " grupo = " + atividadeTurma.getAtividade().getGrupo().getId()
//						+ " and turma = " + atividadeTurma.getTurma().getId())
//				.get(0);
//		return grupoTurma;
//	}

	private void criarNovoCertificado() {
		certificado = new Certificado();
	}

	public Certificado getCertificado() {
		return certificado;
	}

	public void setCertificado(Certificado certificado) {
		this.certificado = certificado;
	}

	public List<Certificado> getCertificadosPendentes() {
		return certificadosPendentes;
	}

	public void setCertificadosPendentes(List<Certificado> certificadosPendentes) {
		this.certificadosPendentes = certificadosPendentes;
	}

	public List<Certificado> getCertificadosInvalidos() {
		return certificadosInvalidos;
	}

	public void setCertificadosInvalidos(List<Certificado> certificadosInvalidos) {
		this.certificadosInvalidos = certificadosInvalidos;
	}

	public List<Certificado> getCertificadosValidados() {
		return certificadosValidados;
	}

	public void setCertificadosValidados(List<Certificado> certificadosValidados) {
		this.certificadosValidados = certificadosValidados;
	}

	public Boolean getCalculaEquivalencia() {
		return calculaEquivalencia;
	}

	public void setCalculaEquivalencia(Boolean calculaEquivalencia) {
		this.calculaEquivalencia = calculaEquivalencia;
	}

	public Double getHoraCertificado() {
		return horaCertificado;
	}

	public void setHoraCertificado(Double horaCertificado) {
		this.horaCertificado = horaCertificado;
	}

	public Turma getTurmaSelecionada() {
		return turmaSelecionada;
	}

	public void setTurmaSelecionada(Turma turmaSelecionada) {
		this.turmaSelecionada = turmaSelecionada;
	}



	public List<Turma> getTurmas() {
		return turmas;
	}



	public void setTurmas(List<Turma> turmas) {
		this.turmas = turmas;
	}



	public String getFiltroNome() {
		return filtroNome;
	}



	public void setFiltroNome(String filtroNome) {
		this.filtroNome = filtroNome;
	}
	
	
	
}
