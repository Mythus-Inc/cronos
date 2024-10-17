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

import util.EnviarEmail;
import util.ExibirMensagem;
import util.FecharDialog;
import util.Mensagem;
import util.PermiteInativar;
import ac.controle.UsuarioSessaoMB;
import ac.modelo.AlunoTurma;
import ac.modelo.Grupo;
import ac.modelo.Permissao;
import ac.modelo.Pessoa;
import ac.services.GrupoService;
import ac.services.PessoaService;
import base.modelo.Aluno;
import base.modelo.Curso;
import base.modelo.Servidor;
import dao.GenericDAO;
import protocolo.modelo.DocumentosAnexo;
import protocolo.modelo.Encaminhamento;
import protocolo.modelo.PessoaSetor;
import protocolo.modelo.Protocolo;
import protocolo.modelo.Requerimento;
import protocolo.modelo.Setor;
import protocolo.service.EncaminhamentoService;
import protocolo.service.ProtocoloService;
import protocolo.service.RequerimentoService;
import protocolo.service.ServidorSetorService;
import protocolo.service.SetorService;

@ViewScoped
@Named("protocoloEncaminhamentoMB")
public class ProtocoloEncaminhamentoMB implements Serializable {

	private static final long serialVersionUID = 1L;
 
	private List<Encaminhamento> listEncaminhamento;
	private List<Encaminhamento> listEncaminhamentoTodas;
	private List<Protocolo> listProtocolo;  
	private Servidor servidorLoado;
	private List<DocumentosAnexo> listAnexo;
	private long idServidorLogado ;
	private boolean externoAtivo = true;
	private boolean exibeNormal = true;
	private boolean exibeExterno = true;
	private Encaminhamento encaminhamento;
	private Encaminhamento encaminhamentoParecer;
	private Encaminhamento reencaminhamento;
	private boolean exibirReencaminhar = true; 
	private Pessoa pessoaEncaminhado;
	

	@Inject
	private GenericDAO<Requerimento> daoRequerimento;

	@Inject
	private GenericDAO<AlunoTurma> daoAlunoTurma;

	@Inject
	private GenericDAO<PessoaSetor> daoAPessoaSetor;

	@Inject
	private GenericDAO<Aluno> daoAluno;

	@Inject
	private GenericDAO<Encaminhamento> daoEncaminhamento;

	@Inject
	private GenericDAO<Pessoa> daoPessoa;

	@Inject
	private GenericDAO<Servidor> daoServidor;

	@Inject
	private GenericDAO<Protocolo> daoProtocolo;

	@Inject
	private UsuarioSessaoMB usuarioSessao;

	@Inject
	private ProtocoloService protocoloService;

	@Inject
	private PessoaService pessoaService;
	
	@Inject
	private GenericDAO<DocumentosAnexo> daoDocumentosAnexo;

	@Inject
	private EncaminhamentoService encaminhamentoService;

	@PostConstruct
	public void inicializar() { 
		listEncaminhamento = new ArrayList<>();
		listEncaminhamentoTodas = new ArrayList<>();
		listProtocolo = new ArrayList<>(); 
		encaminhamento = new Encaminhamento();
		pessoaEncaminhado = new Pessoa();
		encaminhamentoParecer = new Encaminhamento();
		reencaminhamento = new Encaminhamento();   
		listAnexo =  new ArrayList<>();
		preencherProfessor();
		buscarEncaminhamento();
		 

	}

	public void criarNovoObjetoExterno() { 
		encaminhamento = new Encaminhamento();
		pessoaEncaminhado = new Pessoa();

	}
	
	public void buscarAnexos(Encaminhamento encaminhamento){
		listAnexo = daoDocumentosAnexo.listarCodicaoLivre(DocumentosAnexo.class, " protocolo = '"+encaminhamento.getProtocolo().getId()+"'");
	}

	public void criarNovoObjetoEncaminhamento() {
		encaminhamentoParecer = new Encaminhamento();
		exibirReencaminhar = false;
		reencaminhamento = new Encaminhamento();
	}

	public void controleReenviar() {
		exibirReencaminhar = true;
	}

	public void reencaminhar() {
		if (reencaminhamento.getPessoa() == null || reencaminhamento.getPrazoParecerDias() <= 0) {
			ExibirMensagem.exibirMensagem(Mensagem.SERVIDORREENVIAR);
		} else {

			encaminhamentoParecer.setDataParecer(new Date());
			encaminhamentoService.inserirAlterar(encaminhamentoParecer);

			reencaminhamento.setDataEncaminhamento(new Date());
			reencaminhamento.setProtocolo(encaminhamentoParecer.getProtocolo());

			encaminhamentoService.inserirAlterar(reencaminhamento);

			enviaEmail(reencaminhamento.getPessoa().getUsuario(), "você possui um novo protocolo pendente, com "+reencaminhamento.getPrazoParecerDias()+" para ser resolvido.",
					reencaminhamento.getPessoa().getNome());

			buscarEncaminhamentos(encaminhamentoParecer);
			criarNovoObjetoEncaminhamento();
			ExibirMensagem.exibirMensagem(Mensagem.SUCESSO);
			FecharDialog.fecharDialogParecer();
			FecharDialog.fecharDialogMovimentacaoProtocolo();
			buscarEncaminhamento();

		}

	}
	
	
	public void darCiencia() {

		List<PessoaSetor> lisPessoaSetor = new ArrayList<>();
		
		encaminhamentoParecer.setDataParecer(new Date());
		encaminhamentoService.inserirAlterar(encaminhamentoParecer);
		
		
		 
		lisPessoaSetor = daoAPessoaSetor.listar(PessoaSetor.class, " responsavel is true and setor = '"+encaminhamentoParecer.getProtocolo().getRequerimento().getSetorDestino().getId()+"'");
		
		if(lisPessoaSetor.size() > 0){
			PessoaSetor pessoa = lisPessoaSetor.get(0);
			
			enviaEmail(pessoa.getServidor().getUsuario(), "O protocolo de código: "+ encaminhamentoParecer.getProtocolo().getId() +" foi dado um parecer.",
					pessoa.getServidor().getNome());
		}
		
		

		buscarEncaminhamentos(encaminhamentoParecer.getProtocolo());
		criarNovoObjetoEncaminhamento();
		buscarEncaminhamento();
		ExibirMensagem.exibirMensagem(Mensagem.SUCESSO);
		FecharDialog.fecharDialogDarCiencia();
		FecharDialog.fecharDialogDarCienciaMovimentacao();

	}
	
 
	
	public void buscarEncaminhamentos(Protocolo encaminhamento) {
		listEncaminhamento = daoEncaminhamento.listarCodicaoLivre(Encaminhamento.class,
				" protocolo = " + encaminhamento.getId());
		  

	}

	public void preencherProfessor() {
		try {
			servidorLoado = usuarioSessao.recuperarSecretaria();
			idServidorLogado = servidorLoado.getId();
		} catch (Exception e) {
			System.err.println("Erro preencherProfessor");
			e.printStackTrace();
		}
	}
 
	
	
	public void buscarEncaminhamento(){
		System.out.println("servidor logado "+servidorLoado.getNome());
		listEncaminhamentoTodas =  daoEncaminhamento.listarCodicaoLivre(Encaminhamento.class, " dataParecer = null and pessoa = '"+servidorLoado.getId()+"'");
	}

 

	public void resolverProtocolo() { 
		if (encaminhamentoParecer.getDescricao().equals("")) {
			ExibirMensagem.exibirMensagem(Mensagem.PARECER);
		} else { 
			encaminhamentoParecer.setDataParecer(new Date());
			encaminhamentoService.inserirAlterar(encaminhamentoParecer);

			buscarEncaminhamentos(encaminhamentoParecer);
			criarNovoObjetoEncaminhamento();
			ExibirMensagem.exibirMensagem(Mensagem.SUCESSO);
			FecharDialog.fecharDialogParecer();
			
			FecharDialog.fecharDialogMovimentacaoProtocolo();
			buscarEncaminhamento();
		}
	}

 
 

	public void enviaEmail(String destinatario, String mensagem, String nomePessoa) {

		if (EnviarEmail.enviarEmail(destinatario, "Cronos, Protocolo ", "Olá " + nomePessoa + "," + "\n" + mensagem)) {
		} else {
			ExibirMensagem.exibirMensagem(Mensagem.ERROEMAIL);
		}
	}

 
	public void buscarEncaminhamentos(Encaminhamento encaminhamento) {  
		listEncaminhamento = daoEncaminhamento.listarCodicaoLivre(Encaminhamento.class, " protocolo = " + encaminhamento.getProtocolo().getId());
		encaminhamentoParecer = new Encaminhamento(); 
	}

 

	 

	public void criarNovoObjeto() { 
		encaminhamento = new Encaminhamento();
		pessoaEncaminhado = new Pessoa();
	}
 
	public List<Pessoa> completeAluno(String query) {
		List<Pessoa> listaPessoa = new ArrayList<>();

		listaPessoa = daoPessoa.listaComStatus(Pessoa.class);

		List<Pessoa> results = new ArrayList<>();
		for (Pessoa e : listaPessoa) {
			if (e.getNome().toLowerCase().startsWith(query)) {
				results.add(e);
			}
		}

		return results;
	}

	public List<Servidor> completeAServidor(String query) {
		List<Servidor> listServidor = new ArrayList<>();

		listServidor = daoServidor.listaComStatus(Servidor.class);

		List<Servidor> results = new ArrayList<>();
		for (Servidor e : listServidor) {
			if (e.getNome().toLowerCase().startsWith(query)) {
				results.add(e);
			}
		}

		return results;
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

	   

	public boolean isExternoAtivo() {
		return externoAtivo;
	}

	public void setExternoAtivo(boolean externoAtivo) {
		this.externoAtivo = externoAtivo;
	}

	public boolean isExibeNormal() {
		return exibeNormal;
	}

	public void setExibeNormal(boolean exibeNormal) {
		this.exibeNormal = exibeNormal;
	}

	public boolean isExibeExterno() {
		return exibeExterno;
	}

	public void setExibeExterno(boolean exibeExterno) {
		this.exibeExterno = exibeExterno;
	}

 
	public List<Encaminhamento> getListEncaminhamento() {
		return listEncaminhamento;
	}

	public void setListEncaminhamento(List<Encaminhamento> listEncaminhamento) {
		this.listEncaminhamento = listEncaminhamento;
	}

	public Encaminhamento getEncaminhamentoParecer() {
		return encaminhamentoParecer;
	}

	public void setEncaminhamentoParecer(Encaminhamento encaminhamentoParecer) {
		this.encaminhamentoParecer = encaminhamentoParecer;
	}

	public boolean isExibirReencaminhar() {
		return exibirReencaminhar;
	}

	public void setExibirReencaminhar(boolean exibirReencaminhar) {
		this.exibirReencaminhar = exibirReencaminhar;
	}

	public Encaminhamento getReencaminhamento() {
		return reencaminhamento;
	}

	public void setReencaminhamento(Encaminhamento reencaminhamento) {
		this.reencaminhamento = reencaminhamento;
	}

	public Encaminhamento getEncaminhamento() {
		return encaminhamento;
	}

	public void setEncaminhamento(Encaminhamento encaminhamento) {
		this.encaminhamento = encaminhamento;
	}

	public List<Encaminhamento> getListEncaminhamentoTodas() {
		return listEncaminhamentoTodas;
	}

	public Servidor getServidorLoado() {
		return servidorLoado;
	}

	public void setServidorLoado(Servidor servidorLoado) {
		this.servidorLoado = servidorLoado;
	}

	public long getIdServidorLogado() {
		return idServidorLogado;
	}

	public void setIdServidorLogado(long idServidorLogado) {
		this.idServidorLogado = idServidorLogado;
	}

	public void setListEncaminhamentoTodas(List<Encaminhamento> listEncaminhamentoTodas) {
		this.listEncaminhamentoTodas = listEncaminhamentoTodas;
	}

	public List<DocumentosAnexo> getListAnexo() {
		return listAnexo;
	}

	public void setListAnexo(List<DocumentosAnexo> listAnexo) {
		this.listAnexo = listAnexo;
	}

}
