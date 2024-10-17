package protocolo.controle;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.enterprise.context.SessionScoped;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.EntityManager;

import org.hibernate.Session;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.UploadedFile;

import util.ChamarRelatorio;
import util.EnviarEmail;
import util.ExibirMensagem;
import util.FecharDialog;
import util.GeradorSenhas;
import util.Mensagem;
import util.PermiteInativar;
import ac.controle.UsuarioSessaoMB;
import ac.modelo.AlunoTurma;
import ac.modelo.Certificado;
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
import protocolo.service.DocumentosAnexoService;
import protocolo.service.EncaminhamentoService;
import protocolo.service.ProtocoloService;
import protocolo.service.RequerimentoService;
import protocolo.service.ServidorSetorService;
import protocolo.service.SetorService;

@ViewScoped
@Named("protocoloSecretariaMB")
public class ProtocoloSecretariaMB implements Serializable {

	private static final long serialVersionUID = 1L;

	private List<Requerimento> listRequerimento;
	private List<Encaminhamento> listEncaminhamento;
	String requerimentoBusca;
	String setorBusca;
	private boolean escondeBotao = true;
	private List<Protocolo> listProtocolo;
	private boolean verificaSecretariar = false;
	private List<DocumentosAnexo> listAnexo;
	private List<Protocolo> listProtocoloAtraso;
	private List<Protocolo> listProtocoloEncerrado;
	private Protocolo protocolo;
	private Protocolo protocoloDetalhe;
	private Protocolo protocoloServidor;
	private Protocolo protocoloExterno;
	private Servidor servidor;
	private Servidor servidorAtendente;
	private String textoEmail;
	private String cpf;
	private boolean externoAtivo = true;
	private boolean exibeNormal = true;
	private boolean exibeExterno = true;
	private Encaminhamento encaminhamento;
	private Requerimento requerimentoBuscaObj;
	private Encaminhamento encaminhamentoParecer;
	private Encaminhamento reencaminhamento;
	private Servidor pessoaReencaminha;
	private boolean permitePDF = false;
	private int quantidadeArquivos;
	private DocumentosAnexo anexos;
	private List<DocumentosAnexo> listDocumentosAnexo;

	private Pessoa pessoaEncaminhado;

	@Inject
	private GenericDAO<Requerimento> daoRequerimento;

	@Inject
	private GenericDAO<AlunoTurma> daoAlunoTurma;

	@Inject
	private GenericDAO<Setor> daoSetor;

	@Inject
	private GenericDAO<PessoaSetor> daoAPessoaSetor;

	@Inject
	private GenericDAO<Aluno> daoAluno;

	@Inject
	private GenericDAO<Encaminhamento> daoEncaminhamento;

	@Inject
	private GenericDAO<Pessoa> daoPessoa;

	@Inject
	private GenericDAO<DocumentosAnexo> daoDocumentosAnexo;

	@Inject
	private GenericDAO<Servidor> daoServidor;

	@Inject
	private GenericDAO<Permissao> daoPermissao;

	@Inject
	private GenericDAO<Protocolo> daoProtocolo;

	@Inject
	private UsuarioSessaoMB usuarioSessao;

	@Inject
	private ProtocoloService protocoloService;

	@Inject
	private PessoaService pessoaService;

	@Inject
	private EncaminhamentoService encaminhamentoService;

	@Inject
	private DocumentosAnexoService documentosAnexoService;

	@Inject
	private EntityManager manager;

	@PostConstruct
	public void inicializar() {
		listRequerimento = new ArrayList<>();
		protocolo = new Protocolo();
		listEncaminhamento = new ArrayList<>();
		listProtocoloEncerrado = new ArrayList<>();
		listProtocoloAtraso = new ArrayList<>();
		listProtocolo = new ArrayList<>();
		servidorAtendente = new Servidor();
		protocoloServidor = new Protocolo();
		protocoloExterno = new Protocolo();
		protocoloDetalhe = new Protocolo();
		encaminhamento = new Encaminhamento();
		pessoaEncaminhado = new Pessoa();
		encaminhamentoParecer = new Encaminhamento();
		reencaminhamento = new Encaminhamento();
		servidor = new Servidor();
		anexos = new DocumentosAnexo();
		listDocumentosAnexo = new ArrayList<>();
		quantidadeArquivos = 0;
		pessoaReencaminha = new Servidor();
		requerimentoBuscaObj = new Requerimento();
		buscarRequerimento();
		preencherProfessor();
		buscar();
		listAnexo = new ArrayList<>();

	}

	public void criarNovoAnexo() {
		anexos = new DocumentosAnexo();
		listDocumentosAnexo = new ArrayList<>();
		quantidadeArquivos = 0;
	}

	public void imprimirProtocolo(Protocolo protocolo) {

		try {

			if (protocolo.getPessoa() == null) {

				HashMap parametro = new HashMap<>();
				parametro.put("TURMA", protocolo.getId());
				// parametro.put("IDSERVIDORLOGADo", servidorAtendente.getId());
				ChamarRelatorio ch = new ChamarRelatorio("relatorioProtocoloExterno.jasper", parametro,
						"Comprovante Protocolo");
				Session sessions = manager.unwrap(Session.class);
				sessions.doWork(ch);
			} else {

				HashMap parametro = new HashMap<>();
				parametro.put("TURMA", protocolo.getId());
				// parametro.put("IDSERVIDORLOGADo", servidorAtendente.getId());
				ChamarRelatorio ch = new ChamarRelatorio("relatorioProtocolo.jasper", parametro,
						"Comprovante Protocolo");
				Session sessions = manager.unwrap(Session.class);
				sessions.doWork(ch);
			}

		} catch (Exception e) {
			ExibirMensagem.exibirMensagem(Mensagem.ERRO);
		}
	}

	public void imprimirProtocoloEncerrado(Protocolo protocolo) {

		try {

			if (protocolo.getPessoa() == null) {

				HashMap parametro = new HashMap<>();
				parametro.put("TURMA", protocolo.getId());
				// parametro.put("IDSERVIDORLOGADo", servidorAtendente.getId());
				ChamarRelatorio ch = new ChamarRelatorio("relatorioProtocoloExternoParecerFinal.jasper", parametro,
						"Comprovante Protocolo");
				Session sessions = manager.unwrap(Session.class);
				sessions.doWork(ch);
			} else {

				HashMap parametro = new HashMap<>();
				parametro.put("TURMA", protocolo.getId());
				// parametro.put("IDSERVIDORLOGADo", servidorAtendente.getId());
				ChamarRelatorio ch = new ChamarRelatorio("relatorioProtocoloParecerFinal.jasper", parametro,
						"Comprovante Protocolo");
				Session sessions = manager.unwrap(Session.class);
				sessions.doWork(ch);
			}

		} catch (Exception e) {
			ExibirMensagem.exibirMensagem(Mensagem.ERRO);
		}
	}

	public void upload(FileUploadEvent evento) {

		try {
			UploadedFile arquivoUpload = evento.getFile();
			if (!arquivoUpload.getFileName().isEmpty()) {
				Path arquivoTemp = Files.createTempFile(null, null);
				Files.copy(arquivoUpload.getInputstream(), arquivoTemp, StandardCopyOption.REPLACE_EXISTING);

				anexos.setCaminho(arquivoTemp.toString());
				salvarAnexo();

			}

		} catch (Exception e) {
			System.err.println("Erro em: upload");
			e.printStackTrace();
		}
	}

	public void buscarAnexos(Protocolo protocolo) {
		listAnexo = daoDocumentosAnexo.listarCodicaoLivre(DocumentosAnexo.class,
				" protocolo = '" + protocolo.getId() + "'");
	}

	public void buscarAnexos() {
		listAnexo = daoDocumentosAnexo.listarCodicaoLivre(DocumentosAnexo.class,
				" protocolo = '" + encaminhamentoParecer.getProtocolo().getId() + "'");
	}

	public void removerEncaminhamento(Encaminhamento encaminhamentoRemove) {
		System.out.println("encaminhamento passadop " + encaminhamentoRemove);
		encaminhamentoService.remover(encaminhamentoRemove);
		buscarEncaminhamentos(encaminhamentoRemove.getProtocolo());
	}

	public void buscarAnexosVer() {
		listAnexo = daoDocumentosAnexo.listarCodicaoLivre(DocumentosAnexo.class,
				" protocolo = '" + protocoloDetalhe.getId() + "'");
	}

	public void salvarAnexo() {
		listDocumentosAnexo.add(anexos);
		anexos = new DocumentosAnexo();
		permitePDF = false;
		quantidadeArquivos = listDocumentosAnexo.size();
	}

	public void buscaPorRequerimento() {
		listProtocolo = daoProtocolo.listar(Protocolo.class,
				"requerimento.nome  = '" + requerimentoBuscaObj.getNome() + "'");
		requerimentoBuscaObj = new Requerimento();
	}

	public void buscaPorSetorDestino() {

		System.out.println("passado " + requerimentoBuscaObj.getSetorDestino().getDescricao());

		listProtocolo = daoProtocolo.listar(Protocolo.class, " requerimento.setorDestino.descricao = '"
				+ requerimentoBuscaObj.getSetorDestino().getDescricao() + "'");
		requerimentoBuscaObj = new Requerimento();

	}

	public void criarNovoObjetoEncaminhar() {
		pessoaReencaminha = new Servidor();
	}

	public void validarAnexo(DocumentosAnexo protocolo) {
		protocolo.setAutenticado(1);
		documentosAnexoService.inserirAlterar(protocolo);
		buscarAnexos(protocolo.getProtocolo());
	}

	public void penderAnexo(DocumentosAnexo protocolo) {
		protocolo.setAutenticado(0);
		documentosAnexoService.inserirAlterar(protocolo);
		buscarAnexos(protocolo.getProtocolo());
	}

	public void invalidarAnexo(DocumentosAnexo protocolo) {
		protocolo.setAutenticado(2);
		documentosAnexoService.inserirAlterar(protocolo);
	}

	public void finalizaProtocolo() {
		if (protocoloDetalhe.getParecerFinal().equals("")) {
			ExibirMensagem.exibirMensagem(Mensagem.PARECER);
		} else {

			protocoloDetalhe.setDataResultado(new Date());

			protocoloDetalhe.setResponsavelFinaliza(servidorAtendente);
			protocoloService.inserirAlterar(protocoloDetalhe);

			if (protocoloDetalhe.getPessoa() == null) {
				enviaEmail(protocoloDetalhe.getEmail(), "Seu protocolo de c�digo: " + protocoloDetalhe.getId() + " foi "
						+ protocoloDetalhe.getSituacao(), protocoloDetalhe.getNome());
			} else {
				enviaEmail(
						protocoloDetalhe.getPessoa().getUsuario(), "Seu protocolo de c�digo: "
								+ protocoloDetalhe.getId() + " foi " + protocoloDetalhe.getSituacao(),
						protocoloDetalhe.getPessoa().getNome());
			}
			buscarEncaminhamentos(protocoloDetalhe);
			buscar();
			FecharDialog.fecharDialogParecerFinal();
			FecharDialog.fecharDialogMovimentacaoProtocolo();
			ExibirMensagem.exibirMensagem(Mensagem.SUCESSO);
		}
	}

	public void removerDocumento(DocumentosAnexo docAnexo) {
		listDocumentosAnexo.remove(docAnexo);
		quantidadeArquivos = listDocumentosAnexo.size();
	}

	public void criarNovoObjetoExterno() {
		protocoloExterno = new Protocolo();
		encaminhamento = new Encaminhamento();
		pessoaEncaminhado = new Pessoa();

	}

	public void criarNovoObjetoEncaminhamentoParecer() {
		encaminhamentoParecer = new Encaminhamento();
	}

	public void criarNovoObjetoEncaminhamentoComunicar() {
		reencaminhamento = new Encaminhamento();
	}

	public void darCiencia() {

		encaminhamentoParecer.setDataParecer(new Date());
		encaminhamentoService.inserirAlterar(encaminhamentoParecer);

		buscarEncaminhamentos(encaminhamentoParecer.getProtocolo());
		criarNovoObjetoEncaminhamentoParecer();
		ExibirMensagem.exibirMensagem(Mensagem.SUCESSO);
		FecharDialog.fecharDialogDarCiencia();

	}

	public void comunicar() {
		if (reencaminhamento.getPessoa() == null || reencaminhamento.getPrazoParecerDias() <= 0) {
			ExibirMensagem.exibirMensagem(Mensagem.SERVIDORREENVIAR);
		} else {

			reencaminhamento.setDataEncaminhamento(new Date());
			reencaminhamento.setProtocolo(protocoloDetalhe);
			reencaminhamento.setPessoaRemetente(servidorAtendente);

			encaminhamentoService.inserirAlterar(reencaminhamento);

			if (reencaminhamento.getDescricaoRemetente().equals("")) {
				enviaEmail(
						reencaminhamento.getPessoa().getUsuario(), "você possui um novo protocolo pendente, com apenas "
								+ reencaminhamento.getPrazoParecerDias() + " para ser resolvido.",
						reencaminhamento.getPessoa().getNome());
			} else {
				enviaEmail(reencaminhamento.getPessoa().getUsuario(),
						"você possui um novo protocolo pendente, com apenas " + reencaminhamento.getPrazoParecerDias()
								+ " para ser resolvido. \n OBS: " + reencaminhamento.getDescricaoRemetente(),
						reencaminhamento.getPessoa().getNome());
			}

			buscarEncaminhamentos(reencaminhamento.getProtocolo());
			criarNovoObjetoEncaminhamentoComunicar();
			ExibirMensagem.exibirMensagem(Mensagem.SUCESSO);
			FecharDialog.fecharDialogReencaminha();

		}

	}

	public void preencherProfessor() {
		try {
			servidorAtendente = usuarioSessao.recuperarSecretaria();
		} catch (Exception e) {
			System.err.println("Erro preencherProfessor");
			e.printStackTrace();
		}
	}

	public void comunicarEnvolvidos() {
		if (pessoaReencaminha == null) {
			ExibirMensagem.exibirMensagem(Mensagem.SERVIDORCOMUNICA);
		} else {

			enviaEmail(pessoaReencaminha.getUsuario(),
					"Existe uma informação referente ao protocolo: " + protocoloDetalhe.getId() + "\n " + textoEmail,
					pessoaReencaminha.getNome());

			criarNovoObjetoEncaminhar();
			ExibirMensagem.exibirMensagem(Mensagem.SUCESSO);
			FecharDialog.fecharDialogComunica();

		}

	}

	public void buscar() {

		try {
			// primeiro eu to buscando se esse servidor � respons�vel por algum
			// setor 
			List<PessoaSetor> listPessoaSetor = daoAPessoaSetor.listar(PessoaSetor.class,
					"  responsavel is true and servidor = '" + servidorAtendente.getId() + "' ");
			// agora eu to buscando as permiss�es desse servidor
			List<Permissao> listPermissao = daoPermissao.listar(Permissao.class,
					" servidor = '" + servidorAtendente.getId() + "'");
			// verifica as permiss�es q trouxe, se alguma � de secret�ria, caso
			// seja seta true para a vari�vel verificaSecretariar
			for (Permissao per : listPermissao) {
				if (per.getTipo().getDescricao().equals("secretaria")) {
					verificaSecretariar = true;
				}
			}
			// se encontrou a permiss�o de secret�ria, quer dizer q ele pode
			// visualizar todos **lista todos os encaminhados,
			// **lista todos os encerrados,
			if (verificaSecretariar) {
				listProtocolo = daoProtocolo.listar(Protocolo.class, " situacao = 'encaminhado' ");
				listProtocoloEncerrado = daoProtocolo.listar(Protocolo.class,
						" situacao = 'Deferido' or situacao = 'Indeferido' or situacao = 'Parcialmente Deferido'");

				// aqui pega todos os q est�o em aberto e manda verificar a data
				// ** pega um por um e vai passando.
				for (Protocolo p : listProtocolo) {
					 
					verificaPrazo(p);
				}
				// se o cara n�o tem permiss�o de secret�ria.
			} else {
				// busca se ele � respons�vel por algum setor, caso ele seja...
				if (listPessoaSetor.size() > 0) {
				 
					// busca os setores dele.
					for (PessoaSetor p : listPessoaSetor) {
						// busca promeiro os protocolos em aberto
						listProtocolo.addAll(listaProtocolo(p));
						listProtocoloEncerrado.addAll(listaProtocoloEncerrado(p));
						listProtocoloEncerrado.addAll(listaProtocoloEncerradoIndeferido(p));
						listProtocoloEncerrado.addAll(listaProtocoloEncerradoParcialmenteDeferido(p));

					}
					// agora mando verificar o prazo dos que est�o em aberto
					for (Protocolo p : listProtocolo) {
						 
						verificaPrazo(p);
					}
				}
				// se ele n�o � respons�vel nem tem permiss�o de secretaria, n�o
				// deixa entrar na p�gina
				else {

					FacesContext.getCurrentInstance().getExternalContext().redirect("../../negado.jsf");

				}

			}

		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	public void verificaPrazo(Protocolo protocoloVerifica) {

		try {
			String formato = "yyyy-MM-dd";
			SimpleDateFormat dataFormatada = new SimpleDateFormat(formato);
			String dataProtocolado = String.valueOf(protocoloVerifica.getData()).replace("-", "/");

			Date dia = new Date();
			Date dataProtocolacao = new Date(dataProtocolado);
			dataProtocolacao
					.setDate(dataProtocolacao.getDate() + protocoloVerifica.getRequerimento().getPrazoTotalDias());

			String formatadaDataDia = dataFormatada.format(dia);
			String formataDataVencimento = dataFormatada.format(dataProtocolacao);

			Date dataVencimento = dataFormatada.parse(formataDataVencimento);
			Date dataDia = dataFormatada.parse(formatadaDataDia);
			
			System.out.println(dataDia.before(dataVencimento));
			
			if (dataDia.before(dataVencimento) == false ) {

				listProtocoloAtraso.add(protocoloVerifica);
			}

		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public List<Protocolo> listaProtocolo(PessoaSetor p) {
		List<Protocolo> lisProtocolo = new ArrayList<>();
		lisProtocolo = daoProtocolo.listar(Protocolo.class,
				" situacao = 'encaminhado' and requerimento.setorDestino = '" + p.getSetor().getId() + "'");
		return lisProtocolo;
	}

	public List<Protocolo> listaProtocoloEncerrado(PessoaSetor p) {

		List<Protocolo> lisProtocolo = new ArrayList<>();
		lisProtocolo = daoProtocolo.listar(Protocolo.class,
				" requerimento.setorDestino = '" + p.getSetor().getId() + "' and situacao = 'Deferido'");

		return lisProtocolo;
	}

	public List<Protocolo> listaProtocoloEncerradoIndeferido(PessoaSetor p) {

		List<Protocolo> lisProtocolo = new ArrayList<>();
		lisProtocolo = daoProtocolo.listar(Protocolo.class,
				" requerimento.setorDestino = '" + p.getSetor().getId() + "' and situacao = 'Indeferido'");

		return lisProtocolo;
	}

	public List<Protocolo> listaProtocoloEncerradoParcialmenteDeferido(PessoaSetor p) {

		List<Protocolo> lisProtocolo = new ArrayList<>();
		lisProtocolo = daoProtocolo.listar(Protocolo.class,
				" requerimento.setorDestino = '" + p.getSetor().getId() + "' and situacao = 'Parcialmente Deferido'");

		return lisProtocolo;
	}

	public void carregaDadosProtocolo(Protocolo protocolo) {
		if (protocolo.getPessoa() == null) {

			exibeExterno = true;
			exibeNormal = false;
			protocoloDetalhe = protocolo;
		} else {

			exibeExterno = false;
			exibeNormal = true;
			protocoloDetalhe = protocolo;
		}

	}

	public void salvarExterno() {

		try {

			protocoloExterno.setData(new Date());
			protocoloExterno.setSituacao("encaminhado");
			protocoloExterno.setStatus(true);
			protocoloExterno.setServidorAtendente(servidorAtendente);
			protocoloService.inserirAlterar(protocoloExterno);

			if (!enviaEmail(pessoaEncaminhado.getUsuario(),
					"voc� possui um novo protocolo pendente, com apenas "
							+ protocoloExterno.getRequerimento().getPrazoParecerDias()
							+ " para ser resolvido. acesse o link: https://200.17.98.122:8443/cronos/protocolos/gerenciamento/protocolo.jsf ",
					pessoaEncaminhado.getNome())) {
				ExibirMensagem.exibirMensagem(Mensagem.ERROENVIAEMAIL + pessoaEncaminhado.getUsuario());
			}

			if (listDocumentosAnexo.size() > 0) {

				if (!enviaEmail(protocoloExterno.getEmail(),
						"O c�digo do seu protocolo � : " + protocoloExterno.getId() + " , você anexou "
								+ listDocumentosAnexo.size()
								+ " documentos, diriga-se até a secretaria acadêmica para autenticar. ",
						protocoloExterno.getNome())) {
					ExibirMensagem.exibirMensagem(Mensagem.ERROENVIAEMAIL + protocoloExterno.getEmail());
				}

				File diretorio = new File("c:/certificado");
				if (!diretorio.exists()) {
					diretorio.mkdirs();
				}
				// File diretorio = new File("/home/certificados");**
				for (DocumentosAnexo doc : listDocumentosAnexo) {
					String nomeDocumento = gerarNomeAnexo() + GeradorSenhas.gerarSenha();
					Path origem = Paths.get(doc.getCaminho());
					Path destino = Paths.get(diretorio.getCanonicalFile() + "//" + nomeDocumento + ".pdf");
					Files.copy(origem, destino, StandardCopyOption.REPLACE_EXISTING);
					doc.setCaminho(nomeDocumento);
					doc.setProtocolo(protocoloExterno);
					doc.setAutenticado(0);
					documentosAnexoService.inserirAlterar(doc);

					doc = new DocumentosAnexo();
				}

				criarNovoAnexo();
				quantidadeArquivos = 0;
				listDocumentosAnexo = new ArrayList<>();

			} else {
				if (!enviaEmail(protocoloExterno.getEmail(),
						"O código do seu protocolo é : " + protocoloExterno.getId(), protocoloExterno.getNome())) {
					ExibirMensagem.exibirMensagem(Mensagem.ERROENVIAEMAIL + protocoloExterno.getEmail());
				}

			}

			ExibirMensagem.exibirMensagem(Mensagem.SUCESSO);
			FecharDialog.fecharDialogPessoaExterno();
			criarNovoObjetoExterno();
			buscar();

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public static String gerarNomeAnexo() {
		SimpleDateFormat momento = new SimpleDateFormat("yyyyMMddHHmmssSSS");
		return momento.format(new Date());
	}

	public boolean enviaEmail(String destinatario, String mensagem, String nomePessoa) {

		if (EnviarEmail.enviarEmail(destinatario, "Cronos, Protocolo ", "Olá " + nomePessoa + "," + "\n" + mensagem)) {
			return true;
		} else {
			ExibirMensagem.exibirMensagem(Mensagem.ERROEMAIL);
			return false;
		}
	}

	public void preencherDadosServidor() {
		servidor = (Servidor) protocoloServidor.getPessoa();

	}

	public void buscarEncaminhamentos(Protocolo encaminhamento) {
		listEncaminhamento = daoEncaminhamento.listarCodicaoLivre(Encaminhamento.class,
				" protocolo = " + encaminhamento.getId());
		protocoloDetalhe = encaminhamento;
		encaminhamentoParecer = new Encaminhamento();

		List<PessoaSetor> pessoaVerificar = new ArrayList<>();
		pessoaVerificar = daoAPessoaSetor.listar(PessoaSetor.class,
				" status is true and responsavel is true and setor = '"
						+ encaminhamento.getRequerimento().getSetorDestino().getId() + "' and servidor = '"
						+ servidorAtendente.getId() + "'");
		if (pessoaVerificar.size() > 0) {
			escondeBotao = true;
		} else {
			escondeBotao = false;
		}

	}

	public void buscarEncaminhamentosFinalizados(Protocolo encaminhamento) {
		listEncaminhamento = daoEncaminhamento.listarCodicaoLivre(Encaminhamento.class,
				" protocolo = " + encaminhamento.getId());
		protocoloDetalhe = encaminhamento;
		encaminhamentoParecer = new Encaminhamento();

		escondeBotao = false;

	}

	public void preencherDadosRequerimento() {
		PessoaSetor p = new PessoaSetor();
		p = daoAPessoaSetor.buscarCondicao(PessoaSetor.class, " status = true and  setor = "
				+ protocolo.getRequerimento().getSetorDestino().getId() + " and responsavel = true");

		pessoaEncaminhado = p.getServidor();
	}

	public void preencherDadosRequerimentoExterno() {
		PessoaSetor p = new PessoaSetor();
		p = daoAPessoaSetor.buscarCondicao(PessoaSetor.class, " status = true and  setor = "
				+ protocoloExterno.getRequerimento().getSetorDestino().getId() + " and responsavel = true");

		pessoaEncaminhado = p.getServidor();
	}

	public void criarNovoObjeto() {
		protocoloServidor = new Protocolo();
		protocolo = new Protocolo();
		encaminhamento = new Encaminhamento();
		pessoaEncaminhado = new Pessoa();
	}

	public void remover() {
		ExibirMensagem.exibirMensagem(Mensagem.SUCESSO);
		FecharDialog.fecharDialogRequerimentoExcluir();
		criarNovoObjeto();
		buscarRequerimento();

	}

	public void validaFinaliza() {

		boolean valida = true;
		for (Encaminhamento e : listEncaminhamento) {
			if (e.getDataParecer() == null) {
				valida = false;
			}
		}
		if (valida == true) {
			FecharDialog.abrirDialogFinaliza();
		} else {
			ExibirMensagem.exibirMensagem(Mensagem.FINALIZA);
		}
	}

	public void salvarAlunoServidor() {

		try {
			if (protocolo.getId() == null) {
				protocolo.setData(new Date());
				protocolo.setSituacao("encaminhado");
				protocolo.setStatus(true);
				protocolo.setServidorAtendente(servidorAtendente);
				protocoloService.inserirAlterar(protocolo);

				if (!enviaEmail(pessoaEncaminhado.getUsuario(),
						"voc� possui um novo protocolo pendente, com apenas "
								+ protocolo.getRequerimento().getPrazoTotalDias()
								+ " para ser resolvido. acesse o link: https://200.17.98.122:8443/cronos/protocolos/gerenciamento/protocolo.jsf ",
						pessoaEncaminhado.getNome())) {
					ExibirMensagem.exibirMensagem(Mensagem.ERROENVIAEMAIL + pessoaEncaminhado.getUsuario());
				}

				if (listDocumentosAnexo.size() > 0) {

					if (!enviaEmail(protocolo.getPessoa().getUsuario(),
							"O código do seu protocolo é : " + protocolo.getId() + " , você anexou "
									+ listDocumentosAnexo.size()
									+ " documentos, diriga-se até a secretaria acadêmica para autenticar. ",
							protocolo.getPessoa().getNome())) {
						ExibirMensagem.exibirMensagem(Mensagem.ERROENVIAEMAIL + protocolo.getEmail());
					}

					File diretorio = new File("c:/certificado");
					if (!diretorio.exists()) {
						diretorio.mkdirs();
					}
					// File diretorio = new File("/home/certificados");**
					for (DocumentosAnexo doc : listDocumentosAnexo) {
						String nomeDocumento = gerarNomeAnexo() + GeradorSenhas.gerarSenha();
						Path origem = Paths.get(doc.getCaminho());
						Path destino = Paths.get(diretorio.getCanonicalFile() + "//" + nomeDocumento + ".pdf");
						Files.copy(origem, destino, StandardCopyOption.REPLACE_EXISTING);
						doc.setCaminho(nomeDocumento);
						doc.setProtocolo(protocolo);
						doc.setAutenticado(0);
						documentosAnexoService.inserirAlterar(doc);

						doc = new DocumentosAnexo();
					}

					criarNovoAnexo();
					quantidadeArquivos = 0;
					listDocumentosAnexo = new ArrayList<>();

				} else {
					if (!enviaEmail(protocolo.getPessoa().getUsuario(),
							"O código do seu protocolo é : " + protocolo.getId(), protocolo.getPessoa().getNome())) {
						ExibirMensagem.exibirMensagem(Mensagem.ERROENVIAEMAIL + protocolo.getPessoa().getUsuario());
					}
				}

				ExibirMensagem.exibirMensagem(Mensagem.SUCESSO);
				FecharDialog.fecharDialogAdicionaProtocolo();
				criarNovoObjeto();
				buscar();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public void buscarRequerimento() {
		listRequerimento = daoRequerimento.listaComStatus(Requerimento.class);
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

	public List<Setor> completeSetor(String query) {
		List<Setor> listaSetor = new ArrayList<>();

		listaSetor = daoSetor.listaComStatus(Setor.class);

		List<Setor> results = new ArrayList<>();
		for (Setor e : listaSetor) {
			if (e.getDescricao().toLowerCase().startsWith(query)) {
				results.add(e);
			}
		}

		return results;
	}

	public List<Requerimento> getListRequerimento() {
		return listRequerimento;
	}

	public void setListRequerimento(List<Requerimento> listRequerimento) {
		this.listRequerimento = listRequerimento;
	}

	public Protocolo getProtocolo() {
		return protocolo;
	}

	public void setProtocolo(Protocolo protocolo) {
		this.protocolo = protocolo;
	}

	public Servidor getServidorAtendente() {
		return servidorAtendente;
	}

	public void setServidorAtendente(Servidor servidorAtendente) {
		this.servidorAtendente = servidorAtendente;
	}

	public List<Protocolo> getListProtocolo() {
		return listProtocolo;
	}

	public void setListProtocolo(List<Protocolo> listProtocolo) {
		this.listProtocolo = listProtocolo;
	}

	public Protocolo getProtocoloServidor() {
		return protocoloServidor;
	}

	public void setProtocoloServidor(Protocolo protocoloServidor) {
		this.protocoloServidor = protocoloServidor;
	}

	public Servidor getServidor() {
		return servidor;
	}

	public void setServidor(Servidor servidor) {
		this.servidor = servidor;
	}

	public String getCpf() {
		return cpf;
	}

	public Protocolo getProtocoloExterno() {
		return protocoloExterno;
	}

	public void setProtocoloExterno(Protocolo protocoloExterno) {
		this.protocoloExterno = protocoloExterno;
	}

	public void setCpf(String cpf) {
		this.cpf = cpf;
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

	public Protocolo getProtocoloDetalhe() {
		return protocoloDetalhe;
	}

	public void setProtocoloDetalhe(Protocolo protocoloDetalhe) {
		this.protocoloDetalhe = protocoloDetalhe;
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

	public Encaminhamento getReencaminhamento() {
		return reencaminhamento;
	}

	public void setReencaminhamento(Encaminhamento reencaminhamento) {
		this.reencaminhamento = reencaminhamento;
	}

	public boolean isPermitePDF() {
		return permitePDF;
	}

	public void setPermitePDF(boolean permitePDF) {
		this.permitePDF = permitePDF;
	}

	public int getQuantidadeArquivos() {
		return quantidadeArquivos;
	}

	public void setQuantidadeArquivos(int quantidadeArquivos) {
		this.quantidadeArquivos = quantidadeArquivos;
	}

	public DocumentosAnexo getAnexos() {
		return anexos;
	}

	public void setAnexos(DocumentosAnexo anexos) {
		this.anexos = anexos;
	}

	public String getTextoEmail() {
		return textoEmail;
	}

	public void setTextoEmail(String textoEmail) {
		this.textoEmail = textoEmail;
	}

	public Servidor getPessoaReencaminha() {
		return pessoaReencaminha;
	}

	public void setPessoaReencaminha(Servidor pessoaReencaminha) {
		this.pessoaReencaminha = pessoaReencaminha;
	}

	public List<DocumentosAnexo> getListDocumentosAnexo() {
		return listDocumentosAnexo;
	}

	public void setListDocumentosAnexo(List<DocumentosAnexo> listDocumentosAnexo) {
		this.listDocumentosAnexo = listDocumentosAnexo;
	}

	public List<Protocolo> getListProtocoloAtraso() {
		return listProtocoloAtraso;
	}

	public void setListProtocoloAtraso(List<Protocolo> listProtocoloAtraso) {
		this.listProtocoloAtraso = listProtocoloAtraso;
	}

	public List<Protocolo> getListProtocoloEncerrado() {
		return listProtocoloEncerrado;
	}

	public void setListProtocoloEncerrado(List<Protocolo> listProtocoloEncerrado) {
		this.listProtocoloEncerrado = listProtocoloEncerrado;
	}

	public List<DocumentosAnexo> getListAnexo() {
		return listAnexo;
	}

	public void setListAnexo(List<DocumentosAnexo> listAnexo) {
		this.listAnexo = listAnexo;
	}

	public String getRequerimentoBusca() {
		return requerimentoBusca;
	}

	public void setRequerimentoBusca(String requerimentoBusca) {
		this.requerimentoBusca = requerimentoBusca;
	}

	public String getSetorBusca() {
		return setorBusca;
	}

	public void setSetorBusca(String setorBusca) {
		this.setorBusca = setorBusca;
	}

	public boolean isVerificaSecretariar() {
		return verificaSecretariar;
	}

	public void setVerificaSecretariar(boolean verificaSecretariar) {
		this.verificaSecretariar = verificaSecretariar;
	}

	public boolean isEscondeBotao() {
		return escondeBotao;
	}

	public void setEscondeBotao(boolean escondeBotao) {
		this.escondeBotao = escondeBotao;
	}

	public Requerimento getRequerimentoBuscaObj() {
		return requerimentoBuscaObj;
	}

	public void setRequerimentoBuscaObj(Requerimento requerimentoBuscaObj) {
		this.requerimentoBuscaObj = requerimentoBuscaObj;
	}

}
