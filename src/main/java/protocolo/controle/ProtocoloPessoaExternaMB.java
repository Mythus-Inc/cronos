package protocolo.controle;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Scanner;

import javax.annotation.PostConstruct;
import javax.enterprise.context.SessionScoped;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.UploadedFile;

import ac.controle.UsuarioSessaoMB;
import ac.modelo.AlunoTurma;
import ac.modelo.Pessoa;
import ac.services.PessoaService;
import base.modelo.Aluno;
import dao.GenericDAO;
import protocolo.modelo.DocumentosAnexo;
import protocolo.modelo.Encaminhamento;
import protocolo.modelo.PessoaSetor;
import protocolo.modelo.Protocolo;
import protocolo.modelo.Requerimento;
import protocolo.service.DocumentosAnexoService;
import protocolo.service.EncaminhamentoService;
import protocolo.service.ProtocoloService;
import util.CaminhoArquivos;
import util.CriptografiaSenha;
import util.EnviarEmail;
import util.ExibirMensagem;
import util.FecharDialog;
import util.GeradorSenhas;
import util.Mensagem;

@ViewScoped
@Named("protocoloPessoaExternaMB")
public class ProtocoloPessoaExternaMB implements Serializable {

	private static final long serialVersionUID = 1L;

	private List<Protocolo> listProtocolo; 
	private List<Encaminhamento> listEncaminhamento;
	private List<Protocolo> listProtocoloAndamento;
	private List<Protocolo> listProtocoloFinalizado;
	private Protocolo pessoaExterna;
	private Encaminhamento encaminhamento;
	private String cpf;
	private String protocoloBusca;
	private DocumentosAnexo anexos;
	private List<DocumentosAnexo> listDocumentosAnexo;
	private boolean permitePDF = false;
	private int quantidadeArquivos;

	private Pessoa pessoaEncaminhado;

	@Inject
	private GenericDAO<Aluno> daoAluno;

	@Inject
	private GenericDAO<PessoaSetor> daoPessoaSetor;
	
	@Inject
	private GenericDAO<Encaminhamento> daoEncaminhamento;

	@Inject
	private GenericDAO<Requerimento> daoRequerimento;

	@Inject
	private GenericDAO<Pessoa> daoPessoa;

	@Inject
	private GenericDAO<Protocolo> daoProtocolo;

	@Inject
	private ProtocoloService protocoloService;

	@Inject
	private PessoaService pessoaService;

	@Inject
	private EncaminhamentoService encaminhamentoService;

	@Inject
	private DocumentosAnexoService documentosAnexoService;

	@PostConstruct
	public void inicializar() {

		listProtocolo = new ArrayList<>();
		listProtocoloAndamento = new ArrayList<>();
		listProtocoloFinalizado = new ArrayList<>();
		listDocumentosAnexo = new ArrayList<>();
		pessoaExterna = new Protocolo();
		encaminhamento = new Encaminhamento();
		pessoaEncaminhado = new Pessoa();
		anexos = new DocumentosAnexo();
		quantidadeArquivos = 0;
		listEncaminhamento = new ArrayList<>();
	}

	public void criarNovoObjeto() {
		pessoaExterna = new Protocolo();
		encaminhamento = new Encaminhamento();
		pessoaEncaminhado = new Pessoa();

	}
	
	public void buscarEncaminhamento(){
		listEncaminhamento = daoEncaminhamento.listarCadastro(Encaminhamento.class,	" protocolo = '" +protocoloBusca + "'");
	}

	public void criarNovoAnexo() {
		anexos = new DocumentosAnexo();
	}

	public List<Encaminhamento> getListEncaminhamento() {
		return listEncaminhamento;
	}

	public void setListEncaminhamento(List<Encaminhamento> listEncaminhamento) {
		this.listEncaminhamento = listEncaminhamento;
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

	public void salvarAnexo() {
		listDocumentosAnexo.add(anexos);
		anexos = new DocumentosAnexo();
		permitePDF = false;
		quantidadeArquivos = listDocumentosAnexo.size();
	}

	public void removerDocumento(DocumentosAnexo docAnexo) {
		listDocumentosAnexo.remove(docAnexo);
		quantidadeArquivos = listDocumentosAnexo.size();
	}

	public void salvarExterno() {
		try {
			pessoaExterna.setData(new Date());
			pessoaExterna.setStatus(true);
			pessoaExterna.setSituacao("encaminhado");

			protocoloService.inserirAlterar(pessoaExterna);

			if (!enviaEmail(pessoaEncaminhado.getUsuario(),
					"voc� possui um novo protocolo pendente, com apenas "
							+ pessoaExterna.getRequerimento().getPrazoParecerDias()
							+ " para ser resolvido. acesse o link: https://200.17.98.122:8443/cronos/protocolos/gerenciamento/protocolo.jsf ",
					pessoaEncaminhado.getNome())) {
				ExibirMensagem.exibirMensagem(Mensagem.ERROENVIAEMAIL + pessoaEncaminhado.getUsuario());
			}

			if (listDocumentosAnexo.size() > 0) {

				if (!enviaEmail(pessoaExterna.getEmail(),
						"O código do seu protocolo é : " + pessoaExterna.getId() + " , você anexou "
								+ listDocumentosAnexo.size()
								+ " documentos, diriga-se até a secretaria acadêmica para autenticar. ",
						pessoaExterna.getNome())) {
					ExibirMensagem.exibirMensagem(Mensagem.ERROENVIAEMAIL + pessoaExterna.getEmail());
				}

				File diretorio = new File(CaminhoArquivos.caminhoProtocolo());
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
					doc.setProtocolo(pessoaExterna);
					doc.setAutenticado(0);
					documentosAnexoService.inserirAlterar(doc);

					doc = new DocumentosAnexo();
				}

				criarNovoAnexo();
				quantidadeArquivos = 0;
				listDocumentosAnexo = new ArrayList<>();
			} else {
				if (!enviaEmail(pessoaExterna.getEmail(), "O código do seu protocolo é : " + pessoaExterna.getId(),
						pessoaExterna.getNome())) {
					ExibirMensagem.exibirMensagem(Mensagem.ERROENVIAEMAIL + pessoaExterna.getEmail());
				}
			}

			ExibirMensagem.exibirMensagem(Mensagem.SUCESSO);
			criarNovoObjeto();
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

	public void preencherDadosRequerimentoExterno() {
		PessoaSetor p = new PessoaSetor();
		p = daoPessoaSetor.buscarCondicao(PessoaSetor.class, " status = true and  setor = "
				+ pessoaExterna.getRequerimento().getSetorDestino().getId() + " and responsavel = true");
		pessoaEncaminhado = p.getServidor();
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

	public String getCpf() {
		return cpf;
	}

	public void setCpf(String cpf) {
		this.cpf = cpf;
	}

	public DocumentosAnexo getAnexos() {
		return anexos;
	}

	public void setAnexos(DocumentosAnexo anexos) {
		this.anexos = anexos;
	}

	public Protocolo getPessoaExterna() {
		return pessoaExterna;
	}

	public boolean isPermitePDF() {
		return permitePDF;
	}

	public void setPermitePDF(boolean permitePDF) {
		this.permitePDF = permitePDF;
	}

	public void setPessoaExterna(Protocolo pessoaExterna) {
		this.pessoaExterna = pessoaExterna;
	}

	public List<DocumentosAnexo> getListDocumentosAnexo() {
		return listDocumentosAnexo;
	}

	public void setListDocumentosAnexo(List<DocumentosAnexo> listDocumentosAnexo) {
		this.listDocumentosAnexo = listDocumentosAnexo;
	}

	public int getQuantidadeArquivos() {
		return quantidadeArquivos;
	}

	public void setQuantidadeArquivos(int quantidadeArquivos) {
		this.quantidadeArquivos = quantidadeArquivos;
	}

	public String getProtocoloBusca() {
		return protocoloBusca;
	}

	public void setProtocoloBusca(String protocoloBusca) {
		this.protocoloBusca = protocoloBusca;
	}
	
	 

}
