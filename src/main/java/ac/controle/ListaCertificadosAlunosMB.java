package ac.controle;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.enterprise.context.SessionScoped;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import ac.modelo.Certificado;
import base.service.CertificadoService;
import dao.FiltrosDAO;
import dao.GenericDAO;
import util.ExibirMensagem;
import util.Mensagem;

@ViewScoped
@Named("listaCertificadosAlunosMB")
public class ListaCertificadosAlunosMB implements Serializable{

	private static final long serialVersionUID = 1L;
	
	private List<Certificado> certificadosValidados;
	private List<Certificado> certificadosAutenticados;
	private List<Certificado> certificadosInvalidados;
	private List<Certificado> certificadosNaoAuntenticados;
	private Certificado certificado;
	private Certificado certificadoBuscado;
	
	@Inject
	private FiltrosDAO daoFiltros;

	@Inject
	private UsuarioSessaoMB usuarioSessao;
	
	@Inject
	private GenericDAO<Certificado> daoCertificado ;
	
	@Inject
	private CertificadoService certificadoService;

	@PostConstruct
	public void inicializar() {
		
		certificadosValidados = new ArrayList<>();
		certificadosAutenticados = new ArrayList<>();
		certificadosInvalidados = new ArrayList<>();
		certificadosNaoAuntenticados = new ArrayList<>();
		certificado = new Certificado();
		certificadoBuscado = new Certificado();
		
		preencherListaCertificadoValidados();
		preencherListaCertificadoAutenticados();
		preencherListaCertificadoInvalidados();
		preencherListaCertificadoNaoAutenticados();
		
		
	}
	
	public void inativar(Certificado certificado) {
		try {
			certificado.setStatus(false);
			certificadoService.inserirAlterar(certificado);
			ExibirMensagem.exibirMensagem(Mensagem.SUCESSO);
		} catch (Exception e) {
			ExibirMensagem.exibirMensagem(Mensagem.ERRO);
		}
		preencherListaCertificadoAutenticados();
	}

	public void busca(Certificado cet){
		certificadoBuscado = daoCertificado.buscarPorId(Certificado.class, cet.getId());
	
	}
	public void preencherListaCertificadoValidados() {
		
		certificadosValidados = daoFiltros.certificadosAlunos(usuarioSessao.recuperarAluno().getId(), 3);
	}

	public void preencherListaCertificadoAutenticados() {
		certificadosAutenticados = daoFiltros.certificadosAlunos(usuarioSessao.recuperarAluno().getId(), 1);
		
		
	}
	

	public void preencherListaCertificadoInvalidados() {
		
	
		
		certificadosInvalidados = daoFiltros.certificadosAlunos(usuarioSessao.recuperarAluno().getId(), 4);
	}

	public void preencherListaCertificadoNaoAutenticados() {
		certificadosNaoAuntenticados = daoFiltros.certificadosAlunos(usuarioSessao.recuperarAluno().getId(), 2);
	}

	public List<Certificado> getCertificadosValidados() {
		return certificadosValidados;
	}

	public void setCertificadosValidados(List<Certificado> certificadosValidados) {
		this.certificadosValidados = certificadosValidados;
	}

	public List<Certificado> getCertificadosAutenticados() {
		return certificadosAutenticados;
	}

	public void setCertificadosAutenticados(List<Certificado> certificadosAutenticados) {
		this.certificadosAutenticados = certificadosAutenticados;
	}

	public List<Certificado> getCertificadosInvalidados() {
		return certificadosInvalidados;
	}

	public void setCertificadosInvalidados(List<Certificado> certificadosInvalidados) {
		this.certificadosInvalidados = certificadosInvalidados;
	}

	public List<Certificado> getCertificadosNaoAuntenticados() {
		return certificadosNaoAuntenticados;
	}

	public void setCertificadosNaoAuntenticados(List<Certificado> certificadosNaoAuntenticados) {
		this.certificadosNaoAuntenticados = certificadosNaoAuntenticados;
	}

	public Certificado getCertificado() {
		return certificado;
	}

	public void setCertificado(Certificado certificado) {
		this.certificado = certificado;
	}

	public Certificado getCertificadoBuscado() {
		return certificadoBuscado;
	}

	public void setCertificadoBuscado(Certificado certificadoBuscado) {
		this.certificadoBuscado = certificadoBuscado;
	}
	
}
