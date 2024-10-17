package protocolo.controle;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.enterprise.context.SessionScoped;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import ac.controle.UsuarioSessaoMB;
import ac.modelo.AlunoTurma;
import base.modelo.Aluno;
import base.modelo.Servidor;
import dao.GenericDAO;
import protocolo.modelo.PessoaSetor;
import protocolo.modelo.Protocolo;
import protocolo.modelo.Requerimento;
import protocolo.service.ProtocoloService;
import util.ExibirMensagem;
import util.FecharDialog;
import util.Mensagem;

@ViewScoped
@Named("protocoloServidorMB")
public class ProtocoloServidorMB implements Serializable{
	
	
	private static final long serialVersionUID = 1L;
	
 
 
	private String ResponsavelSetor;
	private Servidor servidor;
	private List<Protocolo> listProtocolo;
	private List<Protocolo> listProtocoloAndamento;
	private List<Protocolo> listProtocoloFinalizado;
	private Protocolo protocoloServidor;
	
	@Inject
	private GenericDAO<Aluno> daoAluno;
	
	@Inject
	private GenericDAO<PessoaSetor> daoPessoaSetor;
	
	@Inject
	private GenericDAO<Requerimento> daoRequerimento;
	
	@Inject
	private GenericDAO<Protocolo> daoProtocolo;
	
	@Inject
	private UsuarioSessaoMB usuarioSessao;
	
	@Inject
	private ProtocoloService protocoloService;

	@PostConstruct
	public void inicializar() {
		servidor = new Servidor(); 
		ResponsavelSetor = null;
		listProtocolo = new ArrayList<>();
		listProtocoloAndamento = new ArrayList<>();
		listProtocoloFinalizado = new ArrayList<>();
		protocoloServidor = new Protocolo();
		buscarServidor();
		buscar();
	}
	
	public void criarNovoObjeto(){
		
		protocoloServidor = new Protocolo(); 
		ResponsavelSetor = null;
	}
	
	private void buscar(){
		listProtocolo = daoProtocolo.listar(Protocolo.class, " pessoa = "+servidor.getId());
		listProtocoloAndamento = daoProtocolo.listar(Protocolo.class, " situacao = 'encaminhado' and pessoa = "+servidor.getId());
		listProtocoloFinalizado = daoProtocolo.listar(Protocolo.class, " situacao = 'finalizado' and pessoa = "+servidor.getId());
	}
	
	public List<Requerimento> completeRequerimento(String query) {
		List<Requerimento> listaRequerimento = new ArrayList<>();

		listaRequerimento = daoRequerimento.listaComStatus(Requerimento.class);

		List<Requerimento> results = new ArrayList<>();
		for (Requerimento e : listaRequerimento) {
			if (e.getNome().toLowerCase().startsWith(query)) {
				results.add(e);
			}
		}

		return results;
	}
	
	public void salvarServidor() {

		if (protocoloServidor.getId() == null) {
			protocoloServidor.setData(new Date());
			protocoloServidor.setPessoa(servidor);
			protocoloServidor.setSituacao("encaminhado");
			protocoloServidor.setStatus(true); 
			protocoloService.inserirAlterar(protocoloServidor);

			ExibirMensagem.exibirMensagem(Mensagem.SUCESSO);
			FecharDialog.fecharDialogAdicionaProtocoloServidor();
			criarNovoObjeto();
			buscar();
		}
	}

	
	public void preencherDadosRequerimentoServidor() {
		PessoaSetor p = new PessoaSetor();
		p = daoPessoaSetor.buscarCondicao(PessoaSetor.class, " status = true and  setor = "
				+ protocoloServidor.getRequerimento().getSetorDestino().getId() + " and responsavel = true");
		ResponsavelSetor = p.getServidor().getNome();
	}
	
	 
	
	public void buscarServidor(){
		servidor =(Servidor) usuarioSessao.recuperarServidor();
	}

	 


	public String getResponsavelSetor() {
		return ResponsavelSetor;
	}


	public void setResponsavelSetor(String responsavelSetor) {
		ResponsavelSetor = responsavelSetor;
	}

	public List<Protocolo> getListProtocolo() {
		return listProtocolo;
	}

	public void setListProtocolo(List<Protocolo> listProtocolo) {
		this.listProtocolo = listProtocolo;
	}

	public List<Protocolo> getListProtocoloAndamento() {
		return listProtocoloAndamento;
	}

	public void setListProtocoloAndamento(List<Protocolo> listProtocoloAndamento) {
		this.listProtocoloAndamento = listProtocoloAndamento;
	}

	public List<Protocolo> getListProtocoloFinalizado() {
		return listProtocoloFinalizado;
	}

	public void setListProtocoloFinalizado(List<Protocolo> listProtocoloFinalizado) {
		this.listProtocoloFinalizado = listProtocoloFinalizado;
	}

	public Servidor getServidor() {
		return servidor;
	}

	public void setServidor(Servidor servidor) {
		this.servidor = servidor;
	}

	public Protocolo getProtocoloServidor() {
		return protocoloServidor;
	}

	public void setProtocoloServidor(Protocolo protocoloServidor) {
		this.protocoloServidor = protocoloServidor;
	}
	
	
	

}
