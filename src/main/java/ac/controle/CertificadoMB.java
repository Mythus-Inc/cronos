package ac.controle;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.Serializable;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.zip.Deflater;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipOutputStream;

import javax.annotation.PostConstruct;
import javax.enterprise.context.SessionScoped;
import javax.faces.application.Application;
import javax.faces.application.ViewHandler;
import javax.faces.component.UIViewRoot;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.apache.commons.collections.map.HashedMap;
import org.primefaces.context.RequestContext;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;
import org.primefaces.model.UploadedFile;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import util.CalculoEquivalencia;
import util.CaminhoArquivos;
import util.ExibirMensagem;
import util.FecharDialog;
import util.GeradorSenhas;
import util.Mensagem;
import util.ValidaTopoAtividade;
import util.ValidaTopoGrupo;
import util.ZipUtils;
import ac.modelo.AlunoTurma;
import ac.modelo.AtividadeTurma;
import ac.modelo.Certificado;
import ac.modelo.CertificadosSistemaCertificados;
import ac.modelo.GrupoTurma;
import ac.modelo.Pessoa;
import ac.services.PessoaService;
import base.modelo.Aluno;
import base.modelo.Servidor;
import base.service.CertificadoService;
import dao.FiltrosDAO;
import dao.GenericDAO;

@SessionScoped
@Named("certificadoMB")
public class CertificadoMB implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private List<String> fileList;
	private DefaultStreamedContent download;
	private Certificado certificado;
	private List<Certificado> certificadosAlunos;
	private List<AtividadeTurma> atividadeTurmas;
	private List<Certificado> certificadoTodos;
	private AlunoTurma alunoTurma;
	private Aluno aluno;
	private List<AlunoTurma> cursos;
	private Boolean permitePDF;
	private List<AlunoTurma> listAlunoTurma;
	private boolean controle = true;
	private boolean controleAltera = false;
	private String teste;
	private String SOURCE_FOLDER = CaminhoArquivos.caminhoCertificados();
	private boolean controlar;
	private boolean controleURL = true;
	private List<CertificadosSistemaCertificados> listaCertificadosSistemaCertificados = new ArrayList<>();
	private String mensagemCertificadoImportado = "";

	@Inject
	private CertificadoService certificadoService;

	@Inject
	private GenericDAO<AlunoTurma> daoAlunoTurma;

	@Inject
	private GenericDAO<GrupoTurma> daoGrupoTurma;

	@Inject
	private GenericDAO<Certificado> daoCertificado;

	@Inject
	private GenericDAO<Aluno> daoAluno;

	@Inject
	private GenericDAO<Pessoa> daoPessoa;

	@Inject
	private UsuarioSessaoMB usuarioSessao;

	@Inject
	private ValidaTopoAtividade validaTopoAtividade;



	@Inject
	private PessoaService pessoaService;

	@Inject
	private FiltrosDAO daoFiltros;

	@Inject
	private CalculoEquivalencia calculoEquivalencia;

	@Inject
	private ValidaTopoGrupo validaTopoGrupo;

	@PostConstruct
	public void inicializar() {

		mensagemCertificadoImportado = "";
		criarNovoObjetoCertificado();
		permitePDF = false;

		certificadosAlunos = new ArrayList<>();
		atividadeTurmas = new ArrayList<>();

		alunoTurma = new AlunoTurma();

		preencherListaCertificadoAlunos();
		preencherListaCertificadoTodos();

		removBackup();
		controlar = true;
		cursos = new ArrayList<>();
		listAlunoTurma = new ArrayList<>();
		// System.out.println("iniciar");
		aluno = new Aluno();

		fileList = new ArrayList<String>();
		buscarCertificadosSistemaCertificados();

	}

	private void mostrarDialogConfirmacao() {

	}

	public void buscarCertificadosSistemaCertificados() {
		String cpf = usuarioSessao.recuperarAluno().getCpf();
		String jsonRetorno = null;
		try {
			URL url = new URL("https://certificados.paranavai.ifpr.edu.br/rest/service/certificados?cpf=" + cpf);
			InputStream stream = url.openStream();
			BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(stream, "UTF-8"));
			StringBuilder builder = new StringBuilder();

			String linha;
			while ((linha = bufferedReader.readLine()) != null) {
				builder.append(linha);
			}
			jsonRetorno = builder.toString();
			// System.out.println(jsonRetorno);
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		Gson gson = new Gson();
		listaCertificadosSistemaCertificados = gson.fromJson(jsonRetorno,
				new TypeToken<List<CertificadosSistemaCertificados>>() {
				}.getType());
		// for (CertificadosSistemaCertificados cs :
		// listaCertificadosSistemaCertificados) {
		// cs.setTexto(cs.getTexto().replace("memememememememememe", "<"));
		// cs.setTexto(cs.getTexto().replace("mamamamamamamamamama", ">"));
		// cs.setTexto(cs.getTexto().replace("igigigigigigigigigig", "="));
		// cs.setTexto(cs.getTexto().replace("ecececececececececec", "&"));
		// }
	}

	public void selecionarCertificadoSistemaCertificado(CertificadosSistemaCertificados c) {
		if (c.getAutenticacao() != null && c.getAutenticacao().trim().length() > 2) {
			List<Certificado> cert = daoFiltros.certificadosAlunosCodigoValidacao(c.getAutenticacao());
			if (cert.size() < 1) {
				certificado.setUrl("paranavai.ifpr.edu.br/");
				certificado.setInstituicao("IFPR Campus Paranavaí");
				certificado.setCodigoAutenticacao(c.getAutenticacao());
				certificado.setCodigoAutenticacaoSistemaCertificados(c.getAutenticacao());
				certificado.setCertificadoInterno("sim");
				certificado.setDescricao(c.getTexto());
				certificado.setSituacao(1);
				certificado.setQuantidadeMaximaHora(c.getQuantidadeHoras());
				certificado.setAutenticadoSecretaria(
						c.getTipo() + " inserido a partir do sistema de Certificados e Declarações");
				certificado.setCertificadoInterno("sim");
				certificado.setQuantidadeHorasImportacao(c.getQuantidadeHoras());
				certificado.setAtividadeSugerida(c.getAtividadeComplementar());

			} else {
				ExibirMensagem.exibirMensagem("Estranho, parece que você já utilizou este/a " + c.getTipo()
						+ ". Acho que agora não permitirei a utilização!");
			}
		} else {
			ExibirMensagem.exibirMensagem("Este/a " + c.getTipo() + " está estranho, tente novamente!");
		}

	}

	public boolean baixa() throws FileNotFoundException {

		// String path =
		// FacesContext.getCurrentInstance().getExternalContext().getRealPath("");
		// File file = new File(path +
		// "/certificadoUpload/certificados-cronos.zip");
		// File file = new File("c:/certificado/certificados-cronos.zip");**
		File file = new File(CaminhoArquivos.caminhoCertificados() + "/certificados-cronos.zip");
		InputStream input = new FileInputStream(file);
		ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
		setDownload(new DefaultStreamedContent(input, externalContext.getMimeType(file.getName()), file.getName()));
		return true;
	}

	public void removBackup() {
		ExibirMensagem.exibirMensagem("no remover");
		// String path =
		// FacesContext.getCurrentInstance().getExternalContext().getRealPath("");
		// File file = new File(path +
		// "certificadoUpload/certificados-cronos.zip");
		// File file = new File("c:/certificado/certificados-cronos.zip");**
		File file = new File(CaminhoArquivos.caminhoCertificados() + "/certificados-cronos.zip");
		file.delete();
	}

	// M�todo que faz o download do anexo
	public void baixarArquivo() throws IOException {

		try {
			// String path =
			// FacesContext.getCurrentInstance().getExternalContext().getRealPath("");
			// SOURCE_FOLDER = "c:/certificado"; // leia os arquivos da // pasta
			// **
			// SOURCE_FOLDER = "c:/certificado"; // leia os arquivos da // pasta
			// String OUTPUT_FILE = "c:/certificado/certificados-cronos.zip"; //
			// salve nessa pasta com esse nome**
			String OUTPUT_FILE = CaminhoArquivos.caminhoCertificados() + "/certificados-cronos.zip"; // salve
																										// nessa
																										// pasta
																										// com
																										// esse
																										// nome
			generateFileList(new File(SOURCE_FOLDER));
			zipIt(OUTPUT_FILE);
			baixa();
		} catch (Exception e) {
			e.printStackTrace();
			ExibirMensagem.exibirMensagem("" + e);
		}

	}

	public void uploadBackup(FileUploadEvent evento) {
		try {
			UploadedFile arquivoUpload = evento.getFile();
			if (!arquivoUpload.getFileName().isEmpty()) {

				Path arquivoTemp = Files.createTempFile(null, null);
				Files.copy(arquivoUpload.getInputstream(), arquivoTemp, StandardCopyOption.REPLACE_EXISTING);
				Path origem = Paths.get(arquivoTemp.toString());

				// String path =
				// FacesContext.getCurrentInstance().getExternalContext().getRealPath("");

				// File diretorio = new File("c:/certificado");**
				File diretorio = new File(CaminhoArquivos.caminhoCertificados());
				if (!diretorio.exists()) {
					diretorio.mkdirs();
				}

				Path destino = Paths.get(diretorio.getCanonicalFile() + "//" + evento.getFile().getFileName());
				Files.copy(origem, destino, StandardCopyOption.REPLACE_EXISTING);

				ExibirMensagem.exibirMensagem(Mensagem.UPLOAD);

			}
		} catch (Exception e) {
			// System.err.println("Erro em: upload");
			e.printStackTrace();
		}
	}

	// public void uploadBackup(FileUploadEvent event) {
	//
	// try {
	// Path arquivoTemp = Files.createTempFile(null, null);
	// Files.copy(arquivoUpload.getInputstream(), arquivoTemp,
	// StandardCopyOption.REPLACE_EXISTING);
	// Path origem = Paths.get(arquivoTemp.toString());
	// String path =
	// FacesContext.getCurrentInstance().getExternalContext().getRealPath("");
	// File diretorio = new File(path + "/certificadoUpload");
	// if (!diretorio.exists()) {
	// diretorio.mkdirs();
	// }
	//
	// Path destino = Paths.get(diretorio.getCanonicalFile() + "//" +
	// event.getFile().getFileName());
	// Files.copy(origem, destino, StandardCopyOption.REPLACE_EXISTING);
	//
	// ExibirMensagem.exibirMensagem(Mensagem.UPLOAD);
	// } catch (IOException e) {
	// // TODO Auto-generated catch block
	// e.printStackTrace();
	// }
	//
	// }

	public void generateFileList(File node) {

		if (node.isFile()) {
			fileList.add(generateZipEntry(node.getAbsoluteFile().toString()));
		}

		if (node.isDirectory()) {
			String[] subNote = node.list();
			for (String filename : subNote) {
				generateFileList(new File(node, filename));
			}
		}

	}

	private String generateZipEntry(String file) {
		return file.substring(SOURCE_FOLDER.length() + 1, file.length());
	}

	public void zipIt(String zipFile) {

		byte[] buffer = new byte[1024];

		try {

			FileOutputStream fos = new FileOutputStream(zipFile);
			ZipOutputStream zos = new ZipOutputStream(fos);

			for (String file : this.fileList) {

				ZipEntry ze = new ZipEntry(file);
				zos.putNextEntry(ze);

				FileInputStream in = new FileInputStream(SOURCE_FOLDER + File.separator + file);

				int len;
				while ((len = in.read(buffer)) > 0) {
					zos.write(buffer, 0, len);
				}

				in.close();
			}

			zos.closeEntry();

			zos.close();

			// ExibirMensagem.exibirMensagem(Mensagem.BACKUP);
		} catch (IOException ex) {
			ex.printStackTrace();
			ExibirMensagem.exibirMensagem("zipIt" + ex);
		}
	}

	public void refresh() {
		FacesContext context = FacesContext.getCurrentInstance();
		Application application = context.getApplication();
		ViewHandler viewHandler = application.getViewHandler();
		UIViewRoot viewRoot = viewHandler.createView(context, context.getViewRoot().getViewId());
		context.setViewRoot(viewRoot);

	}

	public void controle() {
		controle = false;
	}

	public void controleCancela() {
		controleAltera = false;
	}

	public void salvar() {

		if (certificado.getCertificadoInterno().equals("sim")) {
			if (certificado.getUrl().equals("") || certificado.getCodigoAutenticacao().equals("")) {
				ExibirMensagem.exibirMensagem(Mensagem.URLCODIGO);
			} else {
				if (certificado.getCodigoAutenticacaoSistemaCertificados().length() < 3
						&& certificado.getUrl().contains("certificados.paranavai")) {

					ExibirMensagem.exibirMensagem(
							"Para certificados emitidos pelo Campus, utilize a opção \"Buscar Certificados do Sistema Interno do Campus\"");
				} else {
					salvaValidado();
				}
			}
		} else {

			salvaValidado();
		}

	}

	public void salvaValidado() {

		try {
			if (certificado.getCaminhoCertificado() == null
					&& certificado.getCodigoAutenticacaoSistemaCertificados().length() < 3) {
				ExibirMensagem.exibirMensagem(Mensagem.UPLOADPDF);
			} else {

				if (certificado.getAlunoTurma().getPermiteCadastroCertificado() == 2) {
					ExibirMensagem.exibirMensagem(Mensagem.NAO_ATIVO);
				} else {
					if (certificado.getQuantidadeMaximaHora() <= 0) {
						ExibirMensagem.exibirMensagem(Mensagem.QUANTIDADE_HORAS);
					} else {
						if (!certificado.getDataInicio().after(certificado.getDataFinalizado())) {
							try {
								if (0==1) {
									ExibirMensagem.exibirMensagem(Mensagem.PERIODO_CERTIFICADO_INVALIDO);
								} else {
									if (certificado.getCodigoAutenticacaoSistemaCertificados().length() < 2) {
										certificado.setDescricao(certificado.getDescricao().replace("'", "")
												.replace("=", "").replace("<", "").replace("&", ""));
										certificado.setInstituicao(certificado.getInstituicao().replace("'", "")
												.replace("=", "").replace("<", "").replace("&", ""));
									}

									if (certificado.getId() == null) {

										certificado.setDataCadastro(new Date());
										certificado.setAluno((Aluno) usuarioSessao.recuperarAluno());
										certificado.setStatus(true);
										if (certificado.getSituacao() == null || certificado.getSituacao() != 1) {
											certificado.setSituacao(0);
										}
										certificado.setAtualizado(true);
										certificado.setBackup(false);

										if (!validaTopoAtividade.calcularTotalAtividade(certificado)) {
											ExibirMensagem.exibirMensagem(Mensagem.HORA_ATIVIDADE_MAXIMA);
										} else {
											if (!validaTopoGrupo.calcularTotalGrupo(certificado)) {
												ExibirMensagem.exibirMensagem(Mensagem.HORA_GRUPO_MAXIMO);
											} else {
												if (certificado.getSituacao() == null
														|| certificado.getSituacao() != 1) {
													certificado.setSituacao(0);
												}
												certificadoService.inserirAlterar(certificado);

												certificado.setHoraComputada(
														calculoEquivalencia.calcularHorasCertificado(certificado));

												certificado.setIdGrupoTurma(
														recuperarGrupoTurma(certificado.getAtividadeTurma()).getId());

												if (certificado.getIdGrupoTurma() == null) {
													throw new Exception("id grupoTurma nulo");
												}

												if (certificado.getCaminhoCertificado() != null
														&& certificado.getCaminhoCertificado() != "") {

													String nomeCertificado = gerarNomeCertificado()
															+ GeradorSenhas.gerarSenha();

													//System.out.println(certificado.getCaminhoCertificado());
													Path origem = Paths.get(certificado.getCaminhoCertificado());

													File diretorio = new File(CaminhoArquivos.caminhoCertificados());

													if (!diretorio.exists()) {
														diretorio.mkdirs();
													}
													Path destino = Paths.get(diretorio.getCanonicalFile() + "//"
															+ nomeCertificado + ".pdf");

													Files.copy(origem, destino, StandardCopyOption.REPLACE_EXISTING);

													certificado.setCaminhoCertificado(nomeCertificado);
													if (certificado.getSituacao() == null
															|| certificado.getSituacao() != 1) {
														certificado.setSituacao(0);
													}
													// certificadoService.inserirAlterar(certificado);

												}
												if (!certificado.getCaminhoCertificado().contains(".tmp")) {
													try {
														Boolean existeArquivo = true;
														if (certificado.getCaminhoCertificado().length() > 5) {
															File diretorio = new File(
																	CaminhoArquivos.caminhoCertificados() + "/"
																			+ certificado.getCaminhoCertificado()
																			+ ".pdf");
															String arquivo = diretorio.getAbsolutePath();
															//System.out.println("AR: " + arquivo);
															existeArquivo = diretorio.exists();
														}
														if (existeArquivo) {
															certificadoService.inserirAlterar(certificado);
															ExibirMensagem.exibirMensagem(Mensagem.SUCESSO);
															FecharDialog.fecharDialogCertificado();
															criarNovoObjetoCertificado();
															controle = true;
														} else {
															ExibirMensagem.exibirMensagem(
																	"Ocorreu um erro ao anexar o arquivo");
														}
													} catch (Exception e) {
														ExibirMensagem
																.exibirMensagem("Ocorreu um erro ao anexar o arquivo");
													}
												} else {
													ExibirMensagem.exibirMensagem("Ocorreu um erro!!");
												}
											}
										}
									} else {
										if (!validaTopoAtividade.calcularTotalAtividade(certificado)) {

											ExibirMensagem.exibirMensagem(Mensagem.HORA_ATIVIDADE_MAXIMA);
										} else {
											if (!validaTopoGrupo.calcularTotalGrupo(certificado)) {
												FecharDialog.fecharDialogCertificado();
												ExibirMensagem.exibirMensagem(Mensagem.HORA_GRUPO_MAXIMO);
											} else {

												certificado.setHoraComputada(
														calculoEquivalencia.calcularHorasCertificado(certificado));
												certificado.setIdGrupoTurma(
														recuperarGrupoTurma(certificado.getAtividadeTurma()).getId());
												certificado.setAtualizado(true);

												if (certificado.getIdGrupoTurma() == null) {
													throw new Exception("id grupoTurma nulo");
												}

												certificadoService.inserirAlterar(certificado);

												ExibirMensagem.exibirMensagem(Mensagem.SUCESSO);
												criarNovoObjetoCertificado();
												FecharDialog.fecharDialogCertificadoEdit();
												controle = true;
												controleAltera = false;
											}
										}
									}
								}
							} catch (Exception e) {
								ExibirMensagem.exibirMensagem(Mensagem.ERRO);
								e.printStackTrace();
							}
						} else {
							ExibirMensagem.exibirMensagem(Mensagem.DATA_FINALIZACAO);
						}
					}

				}
			}
		} catch (Exception e) {
			// System.err.println("salvar() - CertificadoMB");
			e.printStackTrace();
		}
		preencherListaCertificadoAlunos();

	}

	public List<AlunoTurma> completarCursos(String str) {
		cursos = daoAlunoTurma.listar(AlunoTurma.class, " aluno = " + usuarioSessao.recuperarAluno().getId());
		List<AlunoTurma> cursosAtivos = new ArrayList<>();
		for (AlunoTurma cur : cursos) {
			if (cur.getTurma().getDescricao().toLowerCase().startsWith(str)) {
				cursosAtivos.add(cur);
			}
		}
		return cursosAtivos;
	}

	public void inativar(Certificado certificado) {
		try {
			certificado.setStatus(false);
			certificadoService.inserirAlterar(certificado);
			ExibirMensagem.exibirMensagem(Mensagem.SUCESSO);
		} catch (Exception e) {
			ExibirMensagem.exibirMensagem(Mensagem.ERRO);
		}
		preencherListaCertificadoAlunos();
		criarNovoObjetoCertificado();
	}

	public static String gerarNomeCertificado() {
		SimpleDateFormat momento = new SimpleDateFormat("yyyyMMddHHmmssSSS");
		return momento.format(new Date());
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

	public void upload(FileUploadEvent evento) {

		try {
			UploadedFile arquivoUpload = evento.getFile();
			if (!arquivoUpload.getFileName().isEmpty()) {
				Path arquivoTemp = Files.createTempFile(null, null);
				Files.copy(arquivoUpload.getInputstream(), arquivoTemp, StandardCopyOption.REPLACE_EXISTING);
				certificado.setCaminhoCertificado(arquivoTemp.toString());
				certificado.setNomeArquivoAnexado(arquivoUpload.getFileName());
				// System.out.println("caminho cetado" + certificado.getCaminhoCertificado());
				// System.out.println(certificado.getNomeArquivoAnexado());
				permitePDF = true;
			}
		} catch (Exception e) {
			System.err.println("Erro em: upload");
			e.printStackTrace();
		}
	}

	public void criarNovoObjetoCertificado() {
		if (certificado != null && certificado.getCodigoAutenticacaoSistemaCertificados().length() > 2) {
			mensagemCertificadoImportado = "Opa, o documento cadastrado já está AUTENTICADO e encontra-se na página de Certificados Autenticados";

			if (certificado.getAtividadeTurma() != null
					&& certificado.getAtividadeTurma().getAtividade().getDescricao().trim()
							.equalsIgnoreCase(certificado.getAtividadeSugerida())
					&& certificado.getQuantidadeMaximaHora().equals(certificado.getQuantidadeHorasImportacao())) {
				certificado.setSituacao(3);

				certificado.setValidadoProfessor(
						"validado por equiparação de atividade com o sistema de certificados e declarações");
				certificadoService.inserirAlterar(certificado);

				mensagemCertificadoImportado = "Opa, o documento cadastrado já está AUTENTICADO e VALIDADO e encontra-se na página de Certificados Validados";
			} else {
				mensagemCertificadoImportado = "";
			}

		} else {
			mensagemCertificadoImportado = "";
		}
		permitePDF = false;
		certificado = new Certificado();
		controleURL = true;
		certificado.setCertificadoInterno("sim");

		// List<Pessoa> listPessoa = new ArrayList<>();
		// listPessoa = daoPessoa.listaComStatus(Pessoa.class);
		//
		// for(Pessoa l : listPessoa){
		//
		//
		//
		// System.out.println("pessoa : "+l.getNome());
		// l.setUsuario(l.getUsuario().toLowerCase());
		// pessoaService.inserirAlterar(l);
		//
		// System.out.println("Email alterado : "+l.getUsuario());
		// System.out.println("----------------------------------");
		//
		// }

	}

	public void controleAutenticacaoURL() {
		if (certificado.getCertificadoInterno().equals("sim")) {
			controleURL = true;
		} else {
			controleURL = false;
		}
	}

	public void preencherListaCertificadoAlunos() {

		try {

			certificadosAlunos = daoFiltros.certificadosAlunos(usuarioSessao.recuperarAluno().getId(), 0);
			RequestContext.getCurrentInstance().update("frmTabela");

		} catch (Exception e) {
			System.err.println("preencherListaCertificadoAlunos");
			e.printStackTrace();
		}

	}

	public void preencherListaCertificadoTodos() {

		try {
			certificadoTodos = daoCertificado.listar(Certificado.class, " backup = false");

		} catch (Exception e) {
			System.err.println("preencherListaCertificadoAlunos");
			e.printStackTrace();
		}

	}

	private Long recuperarTurmaAluno() {
		alunoTurma = (AlunoTurma) daoFiltros.buscarTurmaAluno(usuarioSessao.recuperarAluno().getId()).get(0);
		return alunoTurma.getTurma().getId();
	}

	public void preencherListaAtividadeTurma() {
		try {
			atividadeTurmas = daoFiltros
					.atividadesTurmaAluno(certificado.getAlunoTurma().getTurma().getMatriz().getId());
		} catch (Exception e) {
			System.err.println("preencherListaAtividadeTurma");
			e.printStackTrace();
		}
	}

	/*
	 * participa��o em programas de bolsas institucionais / matriz 3 / grupo =
	 * Atividades de Pesquisa, Extens�o e Inova��o participa��o em programas de
	 * bolsas ofertadas por agencias de fomento matriz 3 / grupo = Atividades de
	 * Pesquisa, Extens�o e Inova��o Participa��o como apresentador de trabalhos em
	 * palestras, congressos, semin�rios e / matriz 1 / grupo = Atividades de
	 * Pesquisa, Extens�o e Inova��o
	 * 
	 * 
	 * 
	 */

	public GrupoTurma recuperarGrupoTurma(AtividadeTurma atividadeTurma) {
		GrupoTurma grupoTurma = new GrupoTurma();
		try {
			grupoTurma = (GrupoTurma) daoGrupoTurma
					.listar(GrupoTurma.class, " grupo = " + atividadeTurma.getAtividade().getGrupo().getId()
							+ " and matriz = " + atividadeTurma.getMatriz().getId())
					.get(0);
		} catch (Exception e) {
			System.err.println("recuperarGrupoTurma");
			e.printStackTrace();
		}
		return grupoTurma;
	}

	public void permitirPDF(Certificado certificado) {
		controleAltera = true;
		permitePDF = false;
		try {
			if ((certificado.getCaminhoCertificado() == null) || (certificado.getCaminhoCertificado().equals(""))) {
				permitePDF = false;
			} else {
				permitePDF = true;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public Certificado getCertificado() {
		return certificado;
	}

	public void informarCertificado(Certificado certificado) {
		this.certificado = certificado;
	}

	public void setCertificado(Certificado certificado) {
		this.certificado = certificado;
	}

	public List<AtividadeTurma> getAtividadeTurmas() {
		return atividadeTurmas;
	}

	public void setAtividadeTurmas(List<AtividadeTurma> atividadeTurmas) {
		this.atividadeTurmas = atividadeTurmas;
	}

	public List<Certificado> getCertificadosAlunos() {

		return certificadosAlunos;
	}

	public void setCertificadosAlunos(List<Certificado> certificadosAlunos) {
		this.certificadosAlunos = certificadosAlunos;
	}

	public Boolean getPermitePDF() {
		return permitePDF;
	}

	public List<AlunoTurma> getListAlunoTurma() {

		aluno = daoAluno.buscarPorId(Aluno.class, usuarioSessao.recuperarAluno().getId());
		listAlunoTurma = daoAlunoTurma.listar(AlunoTurma.class, " controle = 1 and aluno  = " + aluno.getId());
		return listAlunoTurma;
	}

	public void setListAlunoTurma(List<AlunoTurma> listAlunoTurma) {
		this.listAlunoTurma = listAlunoTurma;
	}

	public void setPermitePDF(Boolean permitePDF) {
		this.permitePDF = permitePDF;
	}

	public Aluno getAluno() {
		return aluno;
	}

	public boolean isControle() {
		return controle;
	}

	public void setControle(boolean controle) {
		this.controle = controle;
	}

	public void setAluno(Aluno aluno) {
		this.aluno = aluno;
	}

	public List<AlunoTurma> getCursos() {
		return cursos;
	}

	public void setCursos(List<AlunoTurma> cursos) {
		this.cursos = cursos;
	}

	public boolean isControleAltera() {
		return controleAltera;
	}

	public void setControleAltera(boolean controleAltera) {
		this.controleAltera = controleAltera;
	}

	public DefaultStreamedContent getDownload() {
		return download;
	}

	public void setDownload(DefaultStreamedContent download) {
		this.download = download;
	}

	public List<Certificado> getCertificadoTodos() {
		return certificadoTodos;
	}

	public void setCertificadoTodos(List<Certificado> certificadoTodos) {
		this.certificadoTodos = certificadoTodos;
	}

	public boolean isControleURL() {
		return controleURL;
	}

	public void setControleURL(boolean controleURL) {
		this.controleURL = controleURL;
	}

	public List<CertificadosSistemaCertificados> getListaCertificadosSistemaCertificados() {
		return listaCertificadosSistemaCertificados;
	}

	public void setListaCertificadosSistemaCertificados(
			List<CertificadosSistemaCertificados> listaCertificadosSistemaCertificados) {
		this.listaCertificadosSistemaCertificados = listaCertificadosSistemaCertificados;
	}

	public String getMensagemCertificadoImportado() {
		return mensagemCertificadoImportado;
	}

	public void setMensagemCertificadoImportado(String mensagemCertificadoImportado) {
		this.mensagemCertificadoImportado = mensagemCertificadoImportado;
	}

}
