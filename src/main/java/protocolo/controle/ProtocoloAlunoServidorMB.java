package protocolo.controle;

import java.io.File;
import java.io.Serializable;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.enterprise.context.SessionScoped;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.UploadedFile;

import ac.controle.UsuarioSessaoMB;
import ac.modelo.AlunoTurma;
import ac.modelo.Pessoa;
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
import util.EnviarEmail;
import util.ExibirMensagem;
import util.FecharDialog;
import util.GeradorSenhas;
import util.Mensagem;

@ViewScoped
@Named("protocoloAlunoServidorMB")
public class ProtocoloAlunoServidorMB implements Serializable {

	private static final long serialVersionUID = 1L;

	private Pessoa alunoServidor;
	private Protocolo protocolo;
	private Pessoa pessoaEncaminhamento;
	private List<Protocolo> listProtocolo;
	private List<Protocolo> listProtocoloAndamento;
	private List<Protocolo> listProtocoloFinalizado;
	private Encaminhamento encaminhamento;
	private boolean permitePDF = false;
	private int quantidadeArquivos;
	private DocumentosAnexo anexos;
	private List<DocumentosAnexo> listDocumentosAnexo;

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

	@Inject
	private DocumentosAnexoService documentosAnexoService;
	
	@Inject
	private EncaminhamentoService encaminhamentoService;

	@PostConstruct
	public void inicializar() {
		alunoServidor = new Pessoa();
		protocolo = new Protocolo();
		pessoaEncaminhamento = new Pessoa();
		listProtocolo = new ArrayList<>();
		listProtocoloAndamento = new ArrayList<>();
		listProtocoloFinalizado = new ArrayList<>();
		listDocumentosAnexo = new ArrayList<>();
		quantidadeArquivos = 0;
		encaminhamento = new Encaminhamento();
		anexos = new DocumentosAnexo();
		buscarAluno();
		buscar();
	}

	public void criarNovoObjeto() {
		encaminhamento = new Encaminhamento();
		protocolo = new Protocolo();
		pessoaEncaminhamento = new Pessoa();
	}

	public void criarNovoAnexo() {
		anexos = new DocumentosAnexo();
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

	private void buscar() {
		listProtocolo = daoProtocolo.listar(Protocolo.class, " situacao = 'encaminhado' and pessoa = '" + alunoServidor.getId()+"' ORDER BY id desc");
		listProtocoloAndamento = daoProtocolo.listar(Protocolo.class,
				" situacao = 'encaminhado' and pessoa = " + alunoServidor.getId());
		listProtocoloFinalizado = daoProtocolo.listar(Protocolo.class,
				" situacao != 'encaminhado' and pessoa = " + alunoServidor.getId());
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

	public void preencherDadosRequerimento() {
		PessoaSetor p = new PessoaSetor();
		p = daoPessoaSetor.buscarCondicao(PessoaSetor.class, " status = true and  setor = "
				+ protocolo.getRequerimento().getSetorDestino().getId() + " and responsavel = true");
		pessoaEncaminhamento = p.getServidor();
	}

	public void salvarAlunoServidor() {

		try {
			if (protocolo.getId() == null) {
				protocolo.setPessoa(alunoServidor);
				protocolo.setData(new Date());
				protocolo.setSituacao("encaminhado");
				protocolo.setStatus(true);
				protocoloService.inserirAlterar(protocolo);
				
				if (!enviaEmail(pessoaEncaminhamento.getUsuario(), "Você possui um novo protocolo pendente, com apenas " + protocolo.getRequerimento().getPrazoParecerDias() + " para ser resolvido. acesse o link: https://200.17.98.122:8443/cronos/protocolos/gerenciamento/protocolo.jsf ",
						pessoaEncaminhamento.getNome())) {
					ExibirMensagem.exibirMensagem(Mensagem.ERROENVIAEMAIL + pessoaEncaminhamento.getUsuario());
				}
				
				  
				

				if (listDocumentosAnexo.size() > 0) {
					
					if (!enviaEmail(alunoServidor.getUsuario(), "o código do seu protocolo é: " +protocolo.getId() +" , você anexou "+listDocumentosAnexo.size() +" documentos, diriga-se até a secretaria acadêmica para autenticar. ", alunoServidor.getNome())) {
						ExibirMensagem.exibirMensagem(Mensagem.ERROENVIAEMAIL + pessoaEncaminhamento.getUsuario());
					}
					// File diretorio = new File("/home/certificados");**
					File diretorio = new File(CaminhoArquivos.caminhoProtocolo());
					if (!diretorio.exists()) {
						diretorio.mkdirs();
					}
					
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

				}else{
					if (!enviaEmail(alunoServidor.getUsuario(), "o código do seu protocolo é: " +protocolo.getId(), alunoServidor.getNome())) {
						ExibirMensagem.exibirMensagem(Mensagem.ERROENVIAEMAIL + pessoaEncaminhamento.getUsuario());
					}
				}
 
				ExibirMensagem.exibirMensagem(Mensagem.SUCESSO);
				FecharDialog.fecharDialogProtocoloAluno();
				criarNovoObjeto();
				buscar();
			}
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

	public void buscarAluno() {
		alunoServidor = (Pessoa) usuarioSessao.recuperarPessoa();
	}

	public Pessoa getAlunoServidor() {
		return alunoServidor;
	}

	public void setAlunoServidor(Pessoa alunoServidor) {
		this.alunoServidor = alunoServidor;
	}

	public void setAlunoServidor(Aluno alunoServidor) {
		this.alunoServidor = alunoServidor;
	}

	public Protocolo getProtocolo() {
		return protocolo;
	}

	public void setProtocolo(Protocolo protocolo) {
		this.protocolo = protocolo;
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

	public List<DocumentosAnexo> getListDocumentosAnexo() {
		return listDocumentosAnexo;
	}

	public void setListDocumentosAnexo(List<DocumentosAnexo> listDocumentosAnexo) {
		this.listDocumentosAnexo = listDocumentosAnexo;
	}

}
