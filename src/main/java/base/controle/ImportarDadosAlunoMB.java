package base.controle;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import jxl.Cell;
import jxl.Sheet;
import jxl.Workbook;
import jxl.read.biff.BiffException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.text.DateFormat;
import java.text.ParseException;
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
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.UploadedFile;
import util.CaminhoArquivos;
import util.CriptografiaSenha;
import util.ExibirMensagem;
import util.FecharDialog;
import util.Mensagem;
import util.ValidaCPF;
import ac.modelo.AlunoTurma;
import ac.modelo.Certificado;
import ac.services.AlunoService;
import base.modelo.Aluno;
import base.modelo.Turma;
import base.service.AlunoTurmaService;
import base.service.CertificadoService;
import base.service.MovimentacaoService;
import dao.GenericDAO;

@ViewScoped
@Named("importarDadosAlunoMB")
public class ImportarDadosAlunoMB implements Serializable {

	private static final long serialVersionUID = 1L;

	private List<String> fileList;
	private DefaultStreamedContent download;
	private Certificado certificado;

	private String SOURCE_FOLDER = CaminhoArquivos.caminhoCertificados();

	@Inject
	private CertificadoService certificadoService;

	private List<Aluno> listaAluno = new ArrayList<>();
	private Aluno aluno;
	private List<AlunoTurma> listaAlunoCPFIrregular = new ArrayList<>();
	private List<AlunoTurma> listRAErrado = new ArrayList<>();
	private List<AlunoTurma> listaAlunoAlgoErrado = new ArrayList<>();
	private List<AlunoTurma> listaAlunoOutraMatricula = new ArrayList<>();
	private List<AlunoTurma> listaAlunoMatriculaRepetida = new ArrayList<>();
	private List<AlunoTurma> listaAlunoTurmaJaCadastrado = new ArrayList<>();
	private List<AlunoTurma> listaEmailJaCadastrado = new ArrayList<>();
	private AlunoTurma alunoTurma;
	private List<AlunoTurma> listAlunoInserir = new ArrayList<>();
	private boolean botaoImporta = true;
	private boolean cpfErrado = false;
	// private List<Movimentacao>

	@Inject
	private GenericDAO<AlunoTurma> daoAlunoTurma;

	@Inject
	private GenericDAO<Turma> daoTurma;

	

	@Inject
	private GenericDAO<Aluno> daoAluno;

	@Inject
	private AlunoService alunoService;

	@Inject
	private AlunoTurmaService alunoTurmaService;

	@Inject
	private MovimentacaoService movimentacaoService;

	@PostConstruct
	public void inicializar() {

		fileList = new ArrayList<String>();
		botaoImporta = true;

	}

	public void removBackup(String nomeArquivo) {

		File file = new File(CaminhoArquivos.caminhoCertificados() + "/" + nomeArquivo);
		file.delete();
	}

	public void uploadDados(FileUploadEvent evento) {
		try {
			UploadedFile arquivoUpload = evento.getFile();
			if (!arquivoUpload.getFileName().isEmpty()) {

				Path arquivoTemp = Files.createTempFile(null, null);
				Files.copy(arquivoUpload.getInputstream(), arquivoTemp, StandardCopyOption.REPLACE_EXISTING);
				Path origem = Paths.get(arquivoTemp.toString());

				removBackup(String.valueOf(arquivoUpload.getFileName()));

				File diretorio = new File(CaminhoArquivos.caminhoCertificados());
				if (!diretorio.exists()) {
					diretorio.mkdirs();
				}

				Path destino = Paths.get(diretorio.getCanonicalFile() + "//" + evento.getFile().getFileName());
				Files.copy(origem, destino, StandardCopyOption.REPLACE_EXISTING);

				ExibirMensagem.exibirMensagem(Mensagem.UPLOAD);

				importaDados(String.valueOf(arquivoUpload.getFileName()));

				botaoImporta = false;

			}
		} catch (Exception e) {
			System.err.println("Erro em: upload");
			e.printStackTrace();
		}
	}

	public void chamarFechar() {
		FecharDialog.fecharDialogAbreStatus();
	}

	public void importaDados(String nomeArquivo) throws IOException, BiffException {

		Workbook workbook = Workbook.getWorkbook(new File(CaminhoArquivos.caminhoCertificados() + "/" + nomeArquivo));

		Sheet sheet = workbook.getSheet(0);

		List<Aluno> listVerificaAlunoEmail = new ArrayList<>();

		int linhas = sheet.getRows();
		for (int i = 1; i < linhas; i++) {

			Cell nome = sheet.getCell(0, i);
			Cell matricula = sheet.getCell(1, i);
			Cell nomeCurso = sheet.getCell(2, i);
			Cell nomeTurma = sheet.getCell(3, i);
			Cell emailAluno = sheet.getCell(4, i);
			Cell cpfAluno = sheet.getCell(5, i);
			Cell dataIngresso = sheet.getCell(6, i);
			//Cell rgAluno = sheet.getCell(6, i);
			//Cell orgaoEmissor = sheet.getCell(7, i);
			//Cell sexo = sheet.getCell(8, i);
			//Cell naturalidade = sheet.getCell(9, i);
			//Cell dataNascimento = sheet.getCell(10, i);
			//Cell nomePai = sheet.getCell(11, i);
			//Cell nomeMae = sheet.getCell(12, i);
			//Cell celular = sheet.getCell(13, i);
			//Cell telefone = sheet.getCell(14, i);
			

			String nome1 = nome.getContents();
			String matricula1 = matricula.getContents();
			String nomeCurso1 = nomeCurso.getContents();
			String nomeTurma1 = nomeTurma.getContents();
			String emailAluno1 = emailAluno.getContents();
			String cpfAluno1 = cpfAluno.getContents();
			//String rgAluno1 = rgAluno.getContents();
			//String orgaoEmissor1 = orgaoEmissor.getContents();
			//String sexo1 = sexo.getContents();
			//String naturalidade1 = naturalidade.getContents();
			//String dataNascimento1 = dataNascimento.getContents();
			//String nomePai1 = nomePai.getContents();
			//String nomeMae1 = nomeMae.getContents();
			//String celular1 = celular.getContents();
			//String telefone1 = telefone.getContents();
			String dataIngresso1 = dataIngresso.getContents();

			try {
				if (!nome1.equals("")) {
					aluno = new Aluno();
					alunoTurma = new AlunoTurma();
					boolean controle = false;

					boolean emailCadastro = false;
					listVerificaAlunoEmail.clear();

					// verifica o email de cada aluno, se tiver cadastro seta
					// esses dados e adiciona na lista, sen�o tiver continua as
					// valida��o

					if (!emailAluno1.equals("")) {

						listVerificaAlunoEmail = daoAluno.listar(Aluno.class,
								" cpf != '" + cpfAluno1 + "' and  usuario ='" + emailAluno1 + "'");
					}

					if (listVerificaAlunoEmail.size() > 0) {
						aluno.setUsuario("");
						emailCadastro = true;
					}

					List<Aluno> listVerificaAluno = daoAluno.listar(Aluno.class, " cpf ='" + cpfAluno1 + "'");
					// verifica se o aluno j� tem cadastro, se tiver
					// pega os
					// dados da pessoa, sen�o tiver busca os dados da
					// planilha.
					if (listVerificaAluno.size() > 0) {
						aluno = listVerificaAluno.get(0);
						controle = true;
					} else {

						String cpfConvertido = cpfAluno1.replace(".", "").replace("-", "").trim();
						cpfErrado = false;
						if (ValidaCPF.isCPF(cpfConvertido) == false) {
							aluno.setNome(nome1);
							//aluno.setCelular(celular1);
							aluno.setCpf(cpfAluno1);
							aluno.setDataCadastro(new Date());
							//aluno.setNatalidade(naturalidade1);
							//aluno.setNomeMae(nomeMae1);
							//aluno.setNomePai(nomePai1);
							//aluno.setOrgaoRg(orgaoEmissor1);
							//aluno.setDataNascimento(retornaData(dataNascimento1, aluno));
							aluno.setPerfilAluno("aluno");
							//aluno.setRg(rgAluno1);
							aluno.setSenha(CriptografiaSenha.criptografar("fdfdfdf123"));
							//if (sexo1.equalsIgnoreCase("MASCULINO"))
							//	aluno.setSexo("M");
							//else
							//	aluno.setSexo("F");
							aluno.setStatus(true);
							//aluno.setTelefone(telefone1);
							if (!emailCadastro)
								aluno.setUsuario(emailAluno1.toLowerCase());

							cpfErrado = true;

						} else {
							

							// System.out.println("dddd " +
							// dataNascimento1
							// + "
							// iii " + dataIngresso1);
							aluno.setNome(nome1);
							//aluno.setCelular(celular1);
							aluno.setCpf(cpfAluno1);
							aluno.setDataCadastro(new Date());
							//aluno.setNatalidade(naturalidade1);
							//aluno.setNomeMae(nomeMae1);
							//aluno.setNomePai(nomePai1);
							//aluno.setOrgaoRg(orgaoEmissor1);
							//aluno.setDataNascimento(retornaData(dataNascimento1, aluno));
							aluno.setPerfilAluno("aluno");
							//aluno.setRg(rgAluno1);
							aluno.setSenha(CriptografiaSenha.criptografar("12sdads3"));
							//if (sexo1.equalsIgnoreCase("MASCULINO"))
							//	aluno.setSexo("M");
							//else
							//	aluno.setSexo("F");
							aluno.setStatus(true);
							//aluno.setTelefone(telefone1);
							if (!emailCadastro){
								aluno.setUsuario(emailAluno1.toLowerCase());
							}

							// System.out.println("id " + i + "aluno = "
							// +
							// aluno.getDataNascimento() + " data "
							// + dataNascimento1 + " -----" +
							// dataIngresso1
							// +
							// "--------");
							
						}
					}
					// busca a turma.
					List<Turma> listTurma = daoTurma.listar(Turma.class, " descricao= '" + nomeTurma1 + "'");
					if (listTurma.size() > 0) {

						List<AlunoTurma> listaAlunoVerf = new ArrayList<>();
						// busca se � um aluno novo ou se ele j� tem
						// cadastro, se ele j� tem cadastro, verifica se
						// ele
						// j� foi add nessa turma que ta na lista, se
						// foi
						// apenas adiciona na lista para mostrar na
						// tabela q
						// j� ta cadastrado, sen�o foi cadastrado ainda,
						// continua com as valida��o
						if (aluno.getId() != null) {

							listaAlunoVerf = daoAlunoTurma.listar(AlunoTurma.class,
									" aluno='" + aluno.getId() + "' and turma = '" + listTurma.get(0).getId() + "'");

						}
						if (listaAlunoVerf.size() == 0) {
							alunoTurma.setTurma(listTurma.get(0));
							alunoTurma.setAluno(aluno);
							alunoTurma.setControle(1);
							alunoTurma.setStatus(true);
							alunoTurma.setPermiteCadastroCertificado(1);
							alunoTurma.setDataMudanca(retornaData(dataIngresso1, alunoTurma.getAluno()));
							alunoTurma.setRa(matricula1);

							if (controle) {

								// pego as turmas ativas desse aluno.
								// verifico o curso se � igual, se for
								// igual, altera a turma, se for diferente
								// deixa a turma q vem da tabela.
								List<AlunoTurma> listAlunoturmaVerificaCurso = daoAlunoTurma.listar(AlunoTurma.class,
										" aluno= '" + aluno.getId() + "'");
								// System.out.println("vai buscar para o
								// aluno "+aluno.getNome() +" tamanho dos
								// curso encontrado
								// "+listAlunoturmaVerificaCurso.size() +"
								// id da turma que busco para cadastrar
								// "+alunoTurma.getTurma().getCurso().getNome());

								if (listAlunoturmaVerificaCurso.size() > 0) {
									for (AlunoTurma alunoTurmaVerifica : listAlunoturmaVerificaCurso) {
										if (alunoTurmaVerifica.getTurma().getCurso().getId() == alunoTurma.getTurma()
												.getCurso().getId()) {

											System.out.println("não insere aluno " + alunoTurma.getAluno().getNome());

										} else {

											alunoTurma
													.setDataMudanca(retornaData(dataIngresso1, alunoTurma.getAluno()));
											listaAlunoOutraMatricula.add(alunoTurma);
											System.out
													.println("insere aluno " + alunoTurmaVerifica.getAluno().getNome());
										}
									}
								}

							} else {

								if (cpfErrado) {
									listaAlunoCPFIrregular.add(alunoTurma);
								} else {
									listAlunoInserir.add(alunoTurma);
								}

							}
						} else {
							listaAlunoTurmaJaCadastrado.add(listaAlunoVerf.get(0));
						}
					} else {
						ExibirMensagem.exibirMensagem(Mensagem.TURMALUNO + " " + aluno.getNome());
					}

					// List<Aluno> listAlunoVerifica =
					// daoAluno.listar(Aluno.class,
					// "");

					// agora falta buscar se existe esse aluno
					// cadastrado,
					// se
					// tiver
					// busca ele, caso contr�rio adiciona e verifica a
					// turma
					// dele,
					// add ele no aluno turma
					// a turma do aluno tem que estar cadastrada no
					// sistema
					// exatamente da mesma forma que est� na planilha.

					// precisa inserir o aluno, inserir o aluno turma e
					// inserir
					// as
					// movimenta��es

					// o cpf dos alunos dever� estar formatado

				}

			} catch (IllegalArgumentException e) {
				AlunoTurma alunoTurmaDadosErrado = new AlunoTurma();
				aluno.setCpf(cpfAluno1);
				aluno.setNome(nome1);
				//aluno.setNomeMae(nomeMae1);
				//aluno.setNomePai(nomePai1);
				alunoTurmaDadosErrado.setAluno(aluno);

				listaAlunoAlgoErrado.add(alunoTurmaDadosErrado);
				System.out.println("no cat");
			}
		}

		workbook.close();

		removBackup(nomeArquivo);

	}

	public void importa() {
		listRAErrado = new ArrayList<>();
		int contagem = 0;

		for (AlunoTurma a : listAlunoInserir) {

			
			String cpfComPonto = a.getAluno().getCpf().substring(0, 3)+"."+a.getAluno().getCpf().substring(3, 6)+"."+a.getAluno().getCpf().substring(6, 9)+"-"+a.getAluno().getCpf().substring(9, 11);
			List<Aluno> laa = daoAluno.listar(Aluno.class, "cpf='"+a.getAluno().getCpf()+"' or cpf='"+cpfComPonto+"'");
			if(laa.size()>0){
				//String email = a.getAluno().getUsuario();
				a.setAluno(laa.get(0));
				//a.getAluno().setUsuario(email);				
			}

			alunoService.inserirAlterar(a.getAluno());
			alunoTurmaService.inserirAlterar(a);

//			movimentacao.setDataMovimentacao(a.getDataMudanca());
//			movimentacao.setSituacao(1);
//			movimentacao.setAlunoTurma(a);
//			movimentacao.setStatus(true);
//			movimentacao.setControle(true);
//
//			movimentacaoService.inserirAlterar(movimentacao);
			contagem += 1;

		}

		for (AlunoTurma a : listaAlunoOutraMatricula) {

			alunoService.inserirAlterar(a.getAluno());
			alunoTurmaService.inserirAlterar(a);

			// List<Movimentacao> mov =
			// daoMovimentacoes.listar(Movimentacao.class, " alunoTurma = '" +
			// a.getId() + "'");

			// if (mov.size() == 0) {
//			movimentacao = new Movimentacao();
//
//			movimentacao.setDataMovimentacao(a.getDataMudanca());
//			movimentacao.setSituacao(1);
//			movimentacao.setAlunoTurma(a);
//			movimentacao.setStatus(true);
//			movimentacao.setControle(true);
//
//			movimentacaoService.inserirAlterar(movimentacao);
			// contagem += 1;
			System.out.println("cria a movimentação para o aluno " + a.getAluno().getNome());
			// } else {
			// System.out.println("n�o altera a movimenta��o para o aluno " +
			// a.getAluno().getId());
			// }
		}

		listRAErrado.addAll(listaAlunoCPFIrregular);
		listRAErrado.addAll(listaAlunoAlgoErrado);
		listRAErrado.addAll(listaEmailJaCadastrado);

		listaAlunoAlgoErrado = new ArrayList<>();
		listAlunoInserir = new ArrayList<>();
		listaAlunoOutraMatricula = new ArrayList<>();
		listaAlunoCPFIrregular = new ArrayList<>();
		listaAlunoTurmaJaCadastrado = new ArrayList<>();
		listaEmailJaCadastrado = new ArrayList<>();

		/*
		 * if (listRAErrado.size() > 0) { System.out.println("enrtro no if ");
		 * FecharDialog.fechaDialogErrado(); }
		 */
		// for (AlunoTurma A : listRAErrado) {
		// System.out.println("pessoas com RA errado = " +
		// A.getAluno().getNome() + " " + A.getRa());
		// }

	}

	public Date retornaData(String dataConverte, Aluno aluno) {
		try {

			SimpleDateFormat in = new SimpleDateFormat("dd/MM/yyyy");
			SimpleDateFormat out = new SimpleDateFormat("yyyy-MM-dd");

			String result = out.format(in.parse(dataConverte.toString()));

			Date datse = (Date) out.parse(result);
			return datse;

		} catch (ParseException e) {
			// TODO Auto-generated catch block
			AlunoTurma alunoTurmaDadosErrado = new AlunoTurma();
			aluno.setCpf(aluno.getCpf());
			aluno.setNome(aluno.getNome());
			aluno.setNomeMae(aluno.getNomeMae());
			aluno.setNomePai(aluno.getNomePai());
			alunoTurmaDadosErrado.setAluno(aluno);

			listaAlunoAlgoErrado.add(alunoTurmaDadosErrado);
			System.out.println("no cat dataaaaa");

			return new Date();
		}

	}

	public void refresh() {
		FacesContext context = FacesContext.getCurrentInstance();
		Application application = context.getApplication();
		ViewHandler viewHandler = application.getViewHandler();
		UIViewRoot viewRoot = viewHandler.createView(context, context.getViewRoot().getViewId());
		context.setViewRoot(viewRoot);

	}

	public List<AlunoTurma> getListaAlunoAlgoErrado() {
		return listaAlunoAlgoErrado;
	}

	public void setListaAlunoAlgoErrado(List<AlunoTurma> listaAlunoAlgoErrado) {
		this.listaAlunoAlgoErrado = listaAlunoAlgoErrado;
	}

	public List<AlunoTurma> getListaAlunoCPFIrregular() {
		return listaAlunoCPFIrregular;
	}

	public void setListaAlunoCPFIrregular(List<AlunoTurma> listaAlunoCPFIrregular) {
		this.listaAlunoCPFIrregular = listaAlunoCPFIrregular;
	}

	public List<AlunoTurma> getListaAlunoOutraMatricula() {
		return listaAlunoOutraMatricula;
	}

	public void setListaAlunoOutraMatricula(List<AlunoTurma> listaAlunoOutraMatricula) {
		this.listaAlunoOutraMatricula = listaAlunoOutraMatricula;
	}

	public List<AlunoTurma> getListaAlunoTurmaJaCadastrado() {
		return listaAlunoTurmaJaCadastrado;
	}

	public void setListaAlunoTurmaJaCadastrado(List<AlunoTurma> listaAlunoTurmaJaCadastrado) {
		this.listaAlunoTurmaJaCadastrado = listaAlunoTurmaJaCadastrado;
	}

	public List<AlunoTurma> getListAlunoInserir() {
		return listAlunoInserir;
	}

	public void setListAlunoInserir(List<AlunoTurma> listAlunoInserir) {
		this.listAlunoInserir = listAlunoInserir;
	}

	public boolean isBotaoImporta() {
		return botaoImporta;
	}

	public void setBotaoImporta(boolean botaoImporta) {
		this.botaoImporta = botaoImporta;
	}

	public List<AlunoTurma> getListRAErrado() {
		return listRAErrado;
	}

	public void setListRAErrado(List<AlunoTurma> listRAErrado) {
		this.listRAErrado = listRAErrado;
	}

	public List<AlunoTurma> getListaEmailJaCadastrado() {
		return listaEmailJaCadastrado;
	}

	public void setListaEmailJaCadastrado(List<AlunoTurma> listaEmailJaCadastrado) {
		this.listaEmailJaCadastrado = listaEmailJaCadastrado;
	}

}
