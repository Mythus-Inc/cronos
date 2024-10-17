package cope.controle;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.Serializable;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.mail.Part;

import org.primefaces.context.RequestContext;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;
import org.primefaces.model.UploadedFile;

import ac.controle.UsuarioSessaoMB;
import ac.modelo.Grupo;
import ac.modelo.Permissao;
import ac.modelo.Pessoa;
import base.modelo.Servidor;
import cope.dao.DAOParticipante;
import cope.dao.DAOProjeto;
import cope.modelo.ArquivoProjeto;
import cope.modelo.Avaliacao;
import cope.modelo.Participante;
import cope.modelo.ProducaoRelatorio;
import cope.modelo.Programas;
import cope.modelo.Projeto;
import cope.modelo.TiposProducao;
import cope.modelo.enums.StatusSubmissao;
import cope.modelo.enums.SubStatusSubmissao;
import cope.modelo.enums.TipoArquivoProjeto;
import cope.service.ArquivoProjetoService;
import cope.service.AvaliacaoService;
import cope.service.ParticipanteService;
import cope.service.ProducaoRelatorioService;
import cope.service.ProjetoService;
import dao.GenericDAO;
import util.CaminhoArquivos;
import util.ExibirMensagem;
import util.FecharDialog;
import util.Mensagem;

@ViewScoped
@Named("projetoMB")
public class ProjetoMB implements Serializable {

	private static final long serialVersionUID = 1L;

	private CaminhoArquivos caminhoArquivo;
	private ProducaoRelatorio producaoRelatorio;
	private Projeto projeto;
	private ArquivoProjeto arquivoProjetoRelatorio;
	private ArquivoProjeto arquivoProjetoRemover;
	private List<Avaliacao> listaAvaliacao;
	private List<ArquivoProjeto> listaArquivo;
	private Projeto projetoSubmeter;
	private List<Participante> listaParticipanteProjetoAtivos;
	private String descricao;
	private boolean relatorioFinal;
	private boolean sumirSalvar = false;
	private boolean desabilitaDescricao = true;
	private Participante participante; // variavel usada quando seleciona um
										// participante do projeto ou para criar
										// um participante especifico
	private List<Participante> listaParticipanteProjeto;

	private List<Projeto> listaProjeto;

	private List<ProducaoRelatorio> listaProducaoRelatorio;

	private boolean termosSubmissao;

	private boolean botaoPrograma = true;
	private boolean controleRemove = false;

	private boolean botaoProgramaCiencia = true;
	private boolean botaoBolsista = true;
	private boolean controleSumirEnviar = false;
	private boolean desabilitaCaraterizacao = true;
	private boolean desabilitaDataTermino = false;
	private boolean desabilitaCampus = true;
	private boolean desabilitaCargaHoraria = false;
	private boolean desabilitaUpload = true;

	// variaveis para controlar a visualizacao da view
	private boolean cadastrar;

	@Inject
	private GenericDAO<Participante> daoParticipantes;

	@Inject
	private GenericDAO<Permissao> daoPermissao;

	@Inject
	private GenericDAO<TiposProducao> daoTiposProducao;

	@Inject
	private GenericDAO<Avaliacao> daoAvaliacao;

	@Inject
	private GenericDAO<ProducaoRelatorio> daoProducaoRelatorio;

	@Inject
	private GenericDAO<ArquivoProjeto> daoArquivoProjeto;

	@Inject
	private GenericDAO<Projeto> daoProjeto;

	@Inject
	private GenericDAO<Programas> daoPrograma;

	@Inject
	private GenericDAO<Participante> daoParticipante;

	@Inject
	private GenericDAO<Pessoa> daoPessoa;

	@Inject
	private DAOProjeto daoProjetoFiltro;

	@Inject
	private DAOParticipante daoParticipanteBusca;

	@Inject
	private ProjetoService projetoService;

	// @Inject
	// private ParametroSistemaCDI parametro;

	@Inject
	private ArquivoProjetoService arquivoProjetoService;

	@Inject
	private ParticipanteService participanteService;

	@Inject
	private UsuarioSessaoMB usuarioSessao;

	@Inject
	private AvaliacaoService avaliacaoService;

	@Inject
	private ProducaoRelatorioService producaoRelatorioService;

	@PostConstruct
	public void inicializar() {

		// parametro = ParametroSistemaCDI.getInstance();
		listaAvaliacao = new ArrayList<>();
		atualizarListaProjeto();
		projetoSubmeter = new Projeto();
		producaoRelatorio = new ProducaoRelatorio();
		arquivoProjetoRelatorio = new ArquivoProjeto();
		desabilitaDescricao = true;
		desabilitaUpload = true;
	}

	public StreamedContent downloadProjeto(Long idProjeto) throws FileNotFoundException {
		// String path = parametro.getParametro("DIRETORIO_PROJETO_COPE") + "/"
		// + usuarioSessao.recuperarPessoa().getId()

		String path = caminhoArquivo.caminhoPDFCope() + "/" + usuarioSessao.recuperarPessoa().getId() + "/" + idProjeto
				+ ".pdf";
		FileInputStream stream = new FileInputStream(path);
		return new DefaultStreamedContent(stream, "application/pdf", "projeto.pdf");
	}

	public void criaDesativar() {
		controleSumirEnviar = false;
	}

	public StreamedContent downloadArquivoProjeto(ArquivoProjeto arquivo) throws FileNotFoundException {
		/*
		 * try{ resolver aqui amanha }
		 */
		String path = caminhoArquivo.caminhoPDFCope() + "/" + arquivo.getProjeto().getId() + "/" + arquivo.getId()
				+ ".pdf";
		FileInputStream stream = new FileInputStream(path);
		return new DefaultStreamedContent(stream, "application/pdf", "projeto.pdf");
	}

	public void inserirProducaoRelatorio() {
		producaoRelatorio.setArquivoProjeto(arquivoProjetoRelatorio);
		producaoRelatorio.setStatus(true);
		producaoRelatorioService.inserirAlterar(producaoRelatorio);
		preencherListaProducoesRelatorio(producaoRelatorio.getArquivoProjeto());
		producaoRelatorio = new ProducaoRelatorio();
		ExibirMensagem.exibirMensagem(Mensagem.SUCESSO);

		// testar o inserir e a busca

	}

	public void preencherListaProducoesRelatorio(ArquivoProjeto arquivo) {
		listaProducaoRelatorio = daoProducaoRelatorio.listar(ProducaoRelatorio.class,
				" arquivoProjeto = '" + arquivo.getId() + "'");
	}

	@SuppressWarnings("unchecked")
	public void alterar(Projeto projeto) {
		this.projeto = projeto;
		listaParticipanteProjeto = daoParticipantes.listar(Participante.class, "projeto.id = " + projeto.getId());
		listaArquivo = daoArquivoProjeto.listarCodicaoLivre(ArquivoProjeto.class,
				" status is true and projeto.id = " + projeto.getId() + " order by versao desc");
		cadastrar = true;
		desabilitaDescricao = true;
		descricao = "";
		if (projeto.isFluxoContinuo())
			desabilitaDataTermino = true;
		else
			desabilitaDataTermino = false;

		controleSumirEnviar = true;
		for (ArquivoProjeto a : listaArquivo) {

			if (a.getTipo().toString().equals("ARQUIVO_PROJETO") && a.getSituacao().toString().equals("APROVADO")
					|| a.getTipo().toString().equals("ARQUIVO_PROJETO")
							&& a.getSituacao().toString().equals("APROVADORESSALVAS")) {
				controleSumirEnviar = false;
				break;
			}
		}
		if (listaArquivo.size() == 0)
			controleSumirEnviar = false;

		System.out.println("valor do sumir " + controleSumirEnviar);
	}

	@SuppressWarnings("unchecked")
	public void visualizar(Projeto projeto) {
		this.projeto = projeto;
		listaParticipanteProjeto = daoParticipantes.listar(Participante.class, "projeto.id = " + projeto.getId());
		listaArquivo = daoArquivoProjeto.listarCodicaoLivre(ArquivoProjeto.class,
				" status is true and projeto.id = " + projeto.getId() + " order by id desc");
	}

	public void carregarTiposProducaoRelatorio(ArquivoProjeto arquivo) {
		arquivoProjetoRelatorio = arquivo;
		preencherListaProducoesRelatorio(arquivo);
	}

	public void alteraDescricao() {
		desabilitaDescricao = false;
		sumirSalvar = false;
	}

	public void desabilitarCaracterizacao() {
		if (projeto.getTipoProjeto().equals("pesquisa"))
			desabilitaCaraterizacao = true;
		else
			desabilitaCaraterizacao = false;

	}

	public void desabilitarDataTermino() {
		if (projeto.isFluxoContinuo() == true)
			desabilitaDataTermino = true;
		else
			desabilitaDataTermino = false;
	}

	public void desabilitarCampus() {
		if (participante.getCampus().equals("outro")) {

			desabilitaCampus = false;
		} else {
			participante.setNomeCampus(null);
			desabilitaCampus = true;
		}
	}

	public void controleProgramaCiencia() {

		if (projeto.isCiencia() == true) {
			botaoProgramaCiencia = false;
		} else {
			botaoProgramaCiencia = true;

		}
	}

	public void buscarParecer(ArquivoProjeto arquivo) {

		if (arquivo.getAvaliacao() != null) {
			FecharDialog.abrirdlgVisualizaParecer();

			listaAvaliacao = daoAvaliacao.listarCodicaoLivre(Avaliacao.class,
					" id = '" + arquivo.getAvaliacao().getId() + "'");
		} else {
			ExibirMensagem.exibirMensagem(Mensagem.PROJETOAVALIACAO);
		}
	}

	public void salvarProjeto() throws IOException {
		// caso projeto nao tenha numero de processo ele é um projeto novo

		// m�todo que verifica se a carga hor�ria de algum participante que n�o
		// seja eventual est� como 0.*

		try {

			if (verificaCargaHoraria()) {
				if (projeto.isFluxoContinuo() == true && projeto.getDataTermino() == null) {
					FecharDialog.fecharDialogStatus();
					ExibirMensagem.exibirMensagem(Mensagem.VERIFICARDATA);
				} else {
					if (projeto.getId() == null) {
						// verifica se fez upload de arquivo
						if (listaArquivo.size() == 1) {
							// aqui vai entrar na parte que verifica se um
							// projeto �
							// ciencia ou j� em andamento "pq esses dois tipos
							// tem
							// um
							// fluxo diferente"
							if (projeto.isProjetoAntigo() == true || projeto.isCiencia() == true) {
								// apenas faz isso para garantir eventuais bugs
								validarCienciaAndamento();

							} else {
								// aqui come�a para projetos que n�o s�o de
								// aceite e
								// nem
								// em andamento
								boolean aceiteParticipante = true;
								// esse for faz a verificação se o participante
								// �
								// aluno,
								// pq se for ele n�o precisa de dar o aceite
								// dele
								for (Participante obj : listaParticipanteProjeto) {
									if (!obj.getIdentificacao().equals("aluno voluntário")
											&& !obj.getIdentificacao().equals("aluno bolsista")) {
										aceiteParticipante = false;

									}
								}
								// se todos os participantes encontrados forem
								// aluno,
								// entra nesse if para setar o status como
								// pronto
								// para
								// submeter
								if (aceiteParticipante) {
									projeto.setSituacao(StatusSubmissao.PRONTO_PARA_SUBMISSAO);
								} else {
									if (listaParticipanteProjeto.stream().filter(obj -> !obj.isAceite()).count() > 0)
										projeto.setSituacao(StatusSubmissao.AGUARDANDO_ACEITE);
									else
										projeto.setSituacao(StatusSubmissao.PRONTO_PARA_SUBMISSAO);
								}
								projeto.setSituacaoProjeto(StatusSubmissao.EM_ANALISE);

								// Salvando projeto
								if (projeto.getTipoProjeto().equals("pesquisa"))
									projeto.setIsPrograma(null); // aqui
								if (projeto.isFluxoContinuo())
									projeto.setDataTermino(null);
								projetoService.inserirAlterar(projeto);
								projeto.setBloqueiaAlterar(false);
								arquivoProjetoService.inserirAlterar(listaArquivo.get(0));
								salvarArquivoProjeto();
								// salvando participantes verificando se �
								// aluno, se
								// for
								// aluno seta o aceite como true.
								for (Participante obj : listaParticipanteProjeto) {
									if (obj.getIdentificacao().equals("aluno volunt�rio")
											|| obj.getIdentificacao().equals("aluno bolsista"))
										obj.setAceite(true);

									participanteService.inserirAlterar(obj);

								}
								// sucesso

								// alterarStatusProjeto(projeto);

								cadastrar = false;
								FecharDialog.fecharDialogStatus();
								ExibirMensagem.exibirMensagem(Mensagem.SUCESSO);
							}

						} else {
							FecharDialog.fecharDialogStatus();
							ExibirMensagem.exibirMensagem(Mensagem.UPLOADARQUIVO);
						}
						atualizarListaProjeto();
					} else {
						// aqui � para salvar altera��es de um projeto j�
						// submetido,
						// se
						// adicionou um novo participante entra aqui.

						List<ArquivoProjeto> listArquivoProjeto = new ArrayList<>();
						listArquivoProjeto = daoArquivoProjeto.listar(ArquivoProjeto.class,
								" tipo = 'ARQUIVO_PROJETO' and situacao = 'APROVADO' and projeto = '"
										+ listaParticipanteProjeto.get(0).getProjeto().getId() + "'");
						boolean enviar = false;
						if (listArquivoProjeto.size() > 0)
							enviar = true;

						if (projeto.isProjetoAntigo() == true || projeto.isCiencia() == true) {
							validarCienciaAndamento();
						}

						for (Participante participante : listaParticipanteProjeto) {
							if (participante.getId() == null) {
								if (enviar) {
									participante.setAceite(true);
									participante.setDataInicioAtividade(new Date());
									participanteService.inserirAlterar(participante);
									enviarEmailNovoMembro(participante);
								} else {
									// quando eu altero n�o precisa de aceitar,
									// mas
									// se
									// ainda n�o foi submetido precisa, q � o q
									// est�
									// sendo feito aqui
									// mas se for um aluno n�o precisa de
									// aceitar.

									if (participante.getIdentificacao().equals("aluno voluntário")
											|| participante.getIdentificacao().equals("aluno bolsista")) {
										participante.setAceite(true);
										participanteService.inserirAlterar(participante);
									} else {
										participante.setAceite(false);
										participanteService.inserirAlterar(participante);
										projeto.setSituacao(StatusSubmissao.AGUARDANDO_ACEITE);
										projetoService.inserirAlterar(projeto);
									}
								}
							} else {
								participanteService.inserirAlterar(participante);
							}
						}
						// insere novos relat�rios
						for (ArquivoProjeto arquivo : listaArquivo) {
							if (arquivo.getId() == null) {
								arquivoProjetoService.inserirAlterar(arquivo);
								projeto.setSituacao(StatusSubmissao.PRONTO_PARA_SUBMISSAO);
								projetoService.inserirAlterar(projeto);
							}
						}
						if (projeto.getTipoProjeto().equals("pesquisa"))
							projeto.setIsPrograma(null); // aqui
						if (projeto.isFluxoContinuo())
							projeto.setDataTermino(null);
						projetoService.inserirAlterar(projeto);
						salvarArquivoProjeto();
						if (controleRemove) {
							arquivoProjetoRemover.setStatus(false);
							arquivoProjetoService.inserirAlterar(arquivoProjetoRemover);
							visualizar(arquivoProjetoRemover.getProjeto());
						}
						cadastrar = false;
						// alterarStatusProjeto(projeto);
						FecharDialog.fecharDialogStatus();
						ExibirMensagem.exibirMensagem(Mensagem.SUCESSO);

					}
					FecharDialog.fecharDialogStatus();
					atualizarListaProjeto();

				}

			} else {
				FecharDialog.fecharDialogStatus();
				ExibirMensagem.exibirMensagem(Mensagem.CARGAHORARIA);
			}
		} catch (Exception e) {
			e.printStackTrace();
			ExibirMensagem.exibirMensagem("Error " + e);
		}

	}

	public void verificarAlteracoesParticipante() {

	}

	/*
	 * public void alterarStatusProjeto(Projeto p) {
	 * 
	 * List<ArquivoProjeto> listProjeto =
	 * daoArquivoProjeto.listar(ArquivoProjeto.class,
	 * "situacao = 'APROVADO' and tipo = 'ARQUIVO_PROJETO' and projeto ='" +
	 * p.getId() + "'");
	 * 
	 * if (listProjeto.size() > 0) {
	 * p.setSituacaoProjeto(StatusSubmissao.APROVADO); } else
	 * p.setSituacaoProjeto(p.getSituacao());
	 * 
	 * projetoService.inserirAlterar(p);
	 * 
	 * }
	 */

	public void validarCienciaAndamento() {

		try {

			if (projeto.isCiencia() == false) {
				projeto.setProgramaCiencia(null);
			}
			// seta o status do projeto como aprovado
			projeto.setDataAprovacao(new Date());
			projeto.setBloqueiaAlterar(false);
			projeto.setSituacao(StatusSubmissao.PRONTO_PARA_SUBMISSAO);
			projeto.setSituacaoProjeto(StatusSubmissao.EM_ANALISE);
			if (projeto.getTipoProjeto().equals("pesquisa"))
				projeto.setIsPrograma(null); // aqui
			if (projeto.isFluxoContinuo())
				projeto.setDataTermino(null);
			// Salvando projeto
			projetoService.inserirAlterar(projeto);

			List<Permissao> permissoes = new ArrayList<>();
			permissoes = daoPermissao.listar(Permissao.class,
					" status is true and tipo.descricao = 'cope_responsavel' ");

			Avaliacao avaliacao = new Avaliacao();
			avaliacao.setAutenticacaoPresidente(false); // o
														// presidente
														// precisa
														// vistar e
														// setar a
														// data do
														// prox
														// relat�rio
			avaliacao.setDataParecer1(new Date());
			avaliacao.setDataPrazoAvaliacao(new Date());
			avaliacao.setObservacoes1("Aprovado por projeto já em andamento");
			avaliacao.setParecer1("ACEITO");
			if (permissoes.size() > 0)
				avaliacao.setAvaliador1(permissoes.get(0).getServidor());

			avaliacaoService.inserirAlterar(avaliacao);

			// seta o status do arquivo como aprovado e insere o
			// arquivo
			listaArquivo.get(0).setSituacao(StatusSubmissao.AGUARDANDO_VISTO);
			listaArquivo.get(0).setAvaliacao(avaliacao);
			arquivoProjetoService.inserirAlterar(listaArquivo.get(0));

			salvarArquivoProjeto();

			// salvando participantes e setando o aceite deles como
			// true
			for (Participante obj : listaParticipanteProjeto) {
				obj.setAceite(true);
				obj.setDataInicioAtividade(new Date());
				participanteService.inserirAlterar(obj);
			}

			// sucesso
			cadastrar = false;
			FecharDialog.fecharDialogStatus();
			ExibirMensagem.exibirMensagem(Mensagem.SUCESSO);

		} catch (IOException e) {
			ExibirMensagem.exibirMensagem(Mensagem.ERROVALIDA);
			e.printStackTrace();
		}
	}

	public boolean verificaCargaHoraria() {
		for (Participante obj : listaParticipanteProjeto) {
			if (obj.getCargaHoraria() == 0 && obj.isEventual() == false)
				return false;

		}
		return true;
	}

	public void enviarEmailNovoMembro(Participante participante) {
		List<Permissao> permissoes = new ArrayList<>();
		permissoes = daoPermissao.listar(Permissao.class, " status is true and tipo.descricao = 'cope_responsavel' ");

		for (Permissao p : permissoes) {

			if (p.getServidor().getUsuario() != null) {
				util.EnviarEmail.enviarEmail(p.getServidor().getUsuario(), "Novo membro ",
						"Aten��o, o " + participante.getIdentificacao() + " " + participante.getPessoa().getNome()
								+ " foi incluido no projeto " + participante.getProjeto().getNome() + ".");

				System.out.println("enviado para " + p.getServidor().getNome());
			} else {
				ExibirMensagem
						.exibirMensagem("Não foi possível encontrar o email do servidor " + p.getServidor().getNome());
			}
		}
	}

	public void setarDescricao() {
		descricao = descricao;
		if (descricao.equals("Relatório Final"))
			relatorioFinal = true;
		else
			relatorioFinal = false;

		desabilitaUpload = false;

	}

	public void salvarArquivoProjeto() throws IOException {
		// salvando arquivo do projeto

		String diretorio = caminhoArquivo.caminhoPDFCope();
		for (ArquivoProjeto arquivo : listaArquivo) {

			if (arquivo.getLocalArquivo().endsWith(".pdf"))
				continue;
			String nome = arquivo.getId() + ".pdf";

			File dir = new File(diretorio + "/" + arquivo.getProjeto().getId());
			if (!dir.exists())
				dir.mkdirs();
			Path origem = Paths.get(arquivo.getLocalArquivo());
			Path destino = Paths.get(dir.getCanonicalFile() + "/" + nome);
			Files.copy(origem, destino, StandardCopyOption.REPLACE_EXISTING);
			arquivo.setLocalArquivo(destino.toString());

			arquivo.setStatus(true);
			arquivo.setControleRemover(false);

			System.out.println("antes de salvar mostra a versão " + arquivo.getVersao());
			System.out.println("ssssv  " + listaArquivo.size());
			arquivoProjetoService.inserirAlterar(arquivo);
		}
	}

	public void removerArquivoProjeto(ArquivoProjeto arquivo) {

		controleRemove = false;

		if (arquivo.isControleRemover()) {
			ExibirMensagem.exibirMensagem(Mensagem.REMOVERARQUIVO);
		} else {

			if (projeto.getId() == null) {
				listaArquivo.remove(arquivo);
				System.out.println("removeu pelo if ");
			} else {
				controleRemove = true;
				listaArquivo.remove(arquivo);
				arquivoProjetoRemover = arquivo;
				controleSumirEnviar = false;
				if (listaArquivo.size() > 0)
					sumirSalvar = false;
				else
					sumirSalvar = true;
				renomear();
				System.out.println("removeu pelo else");
				ExibirMensagem.exibirMensagem(Mensagem.NOVOARQUIVO);
			}
		}
	}

	public void renomear() {

		for (int i = 0; i < listaArquivo.size(); i++) {

			if (listaArquivo.get(i).getId() == null) {

				System.out.println("vers�o pega " + listaArquivo.get(i).getVersao());

				ArquivoProjeto a = listaArquivo.get(i);
				System.out.println("vv " + a.getVersao());
				a.setVersao(listaArquivo.get(i).getVersao() - 1);
				System.out.println("nova " + a.getVersao());
				listaArquivo.set(i, a);
				System.out.println("depois de arrumado " + listaArquivo.get(i).getVersao());
			}
		}

		for (ArquivoProjeto a : listaArquivo) {
			System.out.println("novas versões " + a.getVersao());
		}
	}

	public void novoProjeto() {
		projeto = new Projeto();
		participante = new Participante();
		listaParticipanteProjeto = new ArrayList<Participante>();
		listaParticipanteProjetoAtivos = new ArrayList<Participante>();
		listaArquivo = new ArrayList<ArquivoProjeto>();
		Participante usuarioLogado = new Participante();
		usuarioLogado.setAceite(true);
		usuarioLogado.setFuncao("coordenador");
		usuarioLogado.setIdentificacao("docente");
		usuarioLogado.setPessoa(usuarioSessao.recuperarPessoa());
		usuarioLogado.setProjeto(projeto);
		usuarioLogado.setCargaHoraria(0);
		listaParticipanteProjeto.add(usuarioLogado);
	}

	public void controlePrograma() {

		if (participante.getIdentificacao().equals("aluno bolsista")) {
			botaoPrograma = false;
			botaoBolsista = false;

		} else {
			botaoPrograma = true;
			botaoBolsista = true;

		}
	}

	public void carregarParticipante(Participante participante) {
		this.participante = participante;

		controlePrograma();
		verificaEventual();
	}

	public void carregaProjetoSubmeter(Projeto projeto) {
		this.projetoSubmeter = projeto;
	}

	public void submeterProjeto() {

		if (termosSubmissao != true) {
			ExibirMensagem.exibirMensagem(Mensagem.TERMOSUBMISSAO);
		} else {

			if (projetoSubmeter.isCiencia() || projetoSubmeter.isProjetoAntigo()) {

				projetoSubmeter.setSituacao(StatusSubmissao.EM_AVALIACAO);
				projetoSubmeter.setBloqueiaAlterar(true);
				alterSituacaoExibir(projetoSubmeter);
				projetoService.inserirAlterar(projetoSubmeter);

				List<Permissao> permissoes = new ArrayList<>();
				permissoes = daoPermissao.listar(Permissao.class,
						" status is true and tipo.descricao = 'cope_responsavel' ");

				for (Permissao p : permissoes) {

					if (projeto.isProjetoAntigo()) {
						if (p.getServidor().getUsuario() != null) {
							util.EnviarEmail.enviarEmail(p.getServidor().getUsuario(), "Projeto em Andamento ",
									"Aten��o, O projeto " + projeto.getNome()
											+ " foi cadastrado no sistema como em andamento");
						}
					}
					if (projeto.isCiencia()) {
						if (p.getServidor().getUsuario() != null) {
							util.EnviarEmail.enviarEmail(p.getServidor().getUsuario(), "Projeto Ciência ",
									"Aten��o, O projeto " + projeto.getNome()
											+ " foi cadastrado no sistema como ci�ncia");
						}
					}

				}

				ExibirMensagem.exibirMensagem(Mensagem.SUBMISSAO);
				FecharDialog.fechaDialogCorfirmar();

			} else {

				if (projetoSubmeter.getSituacao().getCodigo() == StatusSubmissao.PRONTO_PARA_SUBMISSAO.getCodigo()) {
					visualizar(projetoSubmeter);
					if (listaParticipanteProjeto.stream().filter(obj -> !obj.isAceite()).count() > 0)
						ExibirMensagem.exibirMensagem(Mensagem.ERROACEITE);
					else {
						// TODO: implementar aviso aos avaliadores
						projetoSubmeter.setSituacao(StatusSubmissao.AGUARDANDO_AVALIACAO);
						alterSituacaoExibir(projetoSubmeter);
						projetoSubmeter.setBloqueiaAlterar(true);
						projetoService.inserirAlterar(projetoSubmeter);
						// alterarStatusProjeto(projetoSubmeter);
						ExibirMensagem.exibirMensagem(Mensagem.SUBMISSAO);
						FecharDialog.fechaDialogCorfirmar();
					}
				} else {
					ExibirMensagem.exibirMensagem(Mensagem.ERROSUBMETER);
				}
			}
		}
	}

	public void alterSituacaoExibir(Projeto projeto) {
		List<ArquivoProjeto> arquivoProjeto = new ArrayList<>();

		arquivoProjeto = daoArquivoProjeto.listarCodicaoLivre(ArquivoProjeto.class,
				" projeto = '" + projeto.getId() + "'");

		for (ArquivoProjeto a : arquivoProjeto) {
			a.setControleRemover(true);
			arquivoProjetoService.inserirAlterar(a);
		}
	}

	public void novoParticipante() {
		participante = new Participante();
		botaoPrograma = true;
		botaoBolsista = true;
		desabilitaCargaHoraria = false;
	}

	public void adicionarParticipante() {
		boolean add = true;
		Long cargaHoraria;

		if (participante.getCargaHoraria() == 0 && participante.isEventual() == false) {
			ExibirMensagem.exibirMensagem(Mensagem.CARGHORARIA);
		} else {

			if (participante.isEventual())
				participante.setCargaHoraria(0);

			if (participante.getId() == null)
				cargaHoraria = daoParticipanteBusca.cargaHorariaTotalPorPessoa(participante.getPessoa().getId(), 0L);
			else
				cargaHoraria = daoParticipanteBusca.cargaHorariaTotalPorPessoa(participante.getPessoa().getId(),
						participante.getId());

			cargaHoraria = (cargaHoraria == null ? 0L : cargaHoraria);

			if (17 <= cargaHoraria + participante.getCargaHoraria() && !participante.isEventual()) {

				listaParticipanteProjetoAtivos = daoParticipante.listar(Participante.class,
						" pessoa.id = '" + participante.getPessoa().getId() + "'");

				ExibirMensagem.exibirMensagem(
						"Participante já atingiu sua carga horária, esse participante está vinculado aos seguintes Projetos: ");

				if (listaParticipanteProjetoAtivos.size() > 0) {
					for (Participante p : listaParticipanteProjetoAtivos) {
						ExibirMensagem.exibirMensagem("" + p.getProjeto().getNome());
					}
				}

			} else {

				List<Participante> lis = new ArrayList<>();
				if (participante.getId() != null) {

					lis = daoParticipante.listar(Participante.class, " pessoa = '" + participante.getPessoa().getId()
							+ "' and projeto ='" + participante.getProjeto().getId() + "'");
				}

				// System.out.println(participante.isEventual() +
				// "++"+lis.get(0).isEventual());
				//
				// if(participante.isEventual() == lis.get(0).isEventual()){
				// System.out.println("1");
				// }
				// if(participante.getCargaHoraria() ==
				// lis.get(0).getCargaHoraria()){
				// System.out.println("2");
				// }
				// if (participante.getFuncao().equals(lis.get(0).getFuncao()))
				// {
				// System.out.println("3");
				// }

				if (participante.getId() != null
						&& participante.getProjeto().getSituacaoProjeto().equals(StatusSubmissao.APROVADO)
						&& (participante.isEventual() != lis.get(0).isEventual()
								|| !participante.getCargaHoraria().equals(lis.get(0).getCargaHoraria())
								|| !participante.getFuncao().equals(lis.get(0).getFuncao()))) {

					int valorCargaHoraria = participante.getCargaHoraria();
					participante.setCargaHoraria(lis.get(0).getCargaHoraria());
					participante.setStatus(false);
					participante.setDataFimAtividade(new Date());
					participanteService.inserirAlterar(participante);

					participante.setCargaHoraria(valorCargaHoraria);
					participante.setId(null);
					participante.setStatus(true);
					participante.setDataFimAtividade(null);
					participante.setDataInicioAtividade(new Date());

					listaParticipanteProjeto = daoParticipante.listar(Participante.class,
							" projeto = '" + participante.getProjeto().getId() + "'");
					participanteService.inserirAlterar(participante);

					listaParticipanteProjeto = daoParticipante.listar(Participante.class,
							" projeto = '" + participante.getProjeto().getId() + "'");

					RequestContext.getCurrentInstance().execute("PF('dlgAddParticipante').hide();");
					novoParticipante();
				} else {

					for (Participante obj : listaParticipanteProjeto) {
						if (obj.getPessoa().getId() == participante.getPessoa().getId()) {
							add = false;
							break;
						}
					}
					if (add) {
						if (participante.getPessoa().getId() == usuarioSessao.recuperarPessoa().getId())
							participante.setAceite(true);

						participante.setProjeto(projeto);
						listaParticipanteProjeto.add(participante);
					}
					novoParticipante();
					RequestContext.getCurrentInstance().execute("PF('dlgAddParticipante').hide();");

				}
			}
		}

	}

	public void verificaEventual() {

		if (participante.isEventual())
			desabilitaCargaHoraria = true;

		else
			desabilitaCargaHoraria = false;
	}

	public boolean isDesabilitaCargaHoraria() {
		return desabilitaCargaHoraria;
	}

	public void setDesabilitaCargaHoraria(boolean desabilitaCargaHoraria) {
		this.desabilitaCargaHoraria = desabilitaCargaHoraria;
	}

	public void removerParticipante(Participante participante) {

		if (projeto.getId() == null) {
			listaParticipanteProjeto.remove(participante);
		} else {
			for (Participante p : listaParticipanteProjeto) {
				if (p.getId() == participante.getId()) {
					p.setStatus(false);
					p.setDataFimAtividade(new Date());
					participanteService.inserirAlterar(p);
				}
			}
			listaParticipanteProjeto = daoParticipantes.listar(Participante.class, "projeto.id = " + projeto.getId());
		}
	}

	public void uploadProjetoPDF(FileUploadEvent e) throws IOException {
		UploadedFile arquivoUpload = e.getFile();
		Path arquivoTemp = Files.createTempFile(null, null);
		Files.copy(arquivoUpload.getInputstream(), arquivoTemp, StandardCopyOption.REPLACE_EXISTING);

		ArquivoProjeto arquivo = new ArquivoProjeto(projeto, arquivoTemp.toString(), new Date(), 1,
				e.getFile().getFileName(), TipoArquivoProjeto.ARQUIVO_PROJETO, StatusSubmissao.AGUARDANDO_AVALIACAO);

		List<ArquivoProjeto> listArquiv = new ArrayList<>();

		if (projeto.getId() == null) {
			if (listaArquivo.size() == 0) {
				arquivo.setNomeArquivoTmp("1.0");
				arquivo.setDescricao(descricao);
				arquivo.setRelatorioFinal(relatorioFinal);
				listaArquivo.add(arquivo);
			} else
				arquivo.setDescricao(descricao);
			arquivo.setRelatorioFinal(relatorioFinal);
			arquivo.setNomeArquivoTmp("1.0");
			listaArquivo.set(0, arquivo);
		} else {
			if (listaArquivo.size() > 0) {

				listArquiv = daoArquivoProjeto.listar(ArquivoProjeto.class,
						" situacao = 'APROVADO' and tipo = 'ARQUIVO_PROJETO' and projeto = '" + projeto.getId() + "'");

				arquivo.setNomeArquivoTmp("" + (listaArquivo.size() + 1) + ".0");
				arquivo.setVersao(listaArquivo.size() + 1);
				arquivo.setDescricao(descricao);
				arquivo.setRelatorioFinal(relatorioFinal);

				if (listArquiv.size() == 0)
					arquivo.setTipo(TipoArquivoProjeto.ARQUIVO_PROJETO);
				else
					arquivo.setTipo(TipoArquivoProjeto.RELATORIO_PROJETO);

				listaArquivo.add(0, arquivo);
			} else {
				arquivo.setDescricao(descricao);
				arquivo.setRelatorioFinal(relatorioFinal);
				arquivo.setNomeArquivoTmp("1.0");
				listaArquivo.add(arquivo);
			}
		}

		descricao = null;
		relatorioFinal = false;
	}

	// @SuppressWarnings("unchecked")
	// public List<Pessoa> completaPessoa(String query) {
	// return daoPessoa.listarCodicaoLivre(Pessoa.class, "usuario like '" +
	// query + "%'");
	// }

	public List<Pessoa> completaPessoa(String str) {
		List<Pessoa> listaPessoa = daoPessoa.listaComStatus(Pessoa.class);
		List<Pessoa> PessoaSelecionados = new ArrayList<>();
		for (Pessoa gru : listaPessoa) {
			if (gru.getNome().toLowerCase().startsWith(str)) {
				PessoaSelecionados.add(gru);
			}
		}
		return PessoaSelecionados;
	}

	public List<Programas> completaPrograma(String str) {
		List<Programas> listaProgramas = daoPrograma.listaComStatus(Programas.class);
		List<Programas> ProgramaSelecionados = new ArrayList<>();
		for (Programas gru : listaProgramas) {
			if (gru.getDescricao().toLowerCase().startsWith(str)) {
				ProgramaSelecionados.add(gru);
			}
		}
		return ProgramaSelecionados;
	}

	public List<TiposProducao> completaTiposProducao(String str) {
		List<TiposProducao> listaTiposProducao = daoTiposProducao.listaComStatus(TiposProducao.class);
		List<TiposProducao> TiposProducaoSelecionados = new ArrayList<>();
		for (TiposProducao gru : listaTiposProducao) {
			if (gru.getDescricao().toLowerCase().startsWith(str)) {
				TiposProducaoSelecionados.add(gru);
			}
		}
		return TiposProducaoSelecionados;
	}

	@SuppressWarnings("unchecked")
	public void atualizarListaProjeto() {

		listaProjeto = daoProjetoFiltro.listarProjetoPorPessoa(usuarioSessao.recuperarPessoa().getId());
		for (Projeto projeto : listaProjeto) {
			if (projeto.getSituacao().equals(StatusSubmissao.AGUARDANDO_ACEITE)) {
				if (daoParticipantes
						.listarCodicaoLivre(Participante.class, " status is true and projeto.id = " + projeto.getId())
						.stream().filter(obj -> !((Participante) obj).isAceite()).count() == 0) {
					projeto.setSituacao(StatusSubmissao.PRONTO_PARA_SUBMISSAO);
					projetoService.inserirAlterar(projeto);
					// alterarStatusProjeto(projeto);
				}
			}
		}
	}

	public boolean verificaStatusParticipacao(Projeto projeto) {

		return daoParticipanteBusca.listarParticipanteProjeto(projeto).stream()
				.filter(obj -> (long) obj.getPessoa().getId() == (long) usuarioSessao.recuperarPessoa().getId())
				.findAny().orElse(null).isAceite();

	}

	public boolean verificaPermissao(Projeto projeto, String acao) {

		Participante participanteLogado = new Participante();
		participanteLogado = daoParticipante.listar(Participante.class, " projeto ='" + projeto.getId() + "'").stream()
				.filter(obj -> (long) obj.getPessoa().getId() == (long) usuarioSessao.recuperarPessoa().getId())
				.findAny().orElse(null);

		boolean retorno = false;

		switch (acao) {
		case "submeter":
			retorno = participanteLogado.getFuncao().equals("coordenador");
			break;
		case "alterar":
			retorno = participanteLogado.getFuncao().equals("coordenador");
			break;
		}
		return !retorno;
	}

	public void verifica(Participante p) {
		System.out.println("part ==== " + p);
		System.out.println("nome do projeto " + p.getProjeto().getNome());
		System.out.println("participante recebido " + p.getPessoa().getId());
		System.out.println("participante logado " + usuarioSessao.recuperarPessoa().getId());

		if (p.getPessoa().getId() == usuarioSessao.recuperarPessoa().getId()) {
			System.out.println("entro no if");
		}

		System.out.println("-------------------------");
		long idParticipanteProjeto = p.getPessoa().getId();
		long idUserLogado = usuarioSessao.recuperarPessoa().getId();

		System.out.println("id passado " + idParticipanteProjeto + " id logado " + idUserLogado);

		if (idParticipanteProjeto == idUserLogado) {
			System.out.println("são iguais ");
		} else {
			System.out.println("são diferentes");
		}

	}

	public void aceitarProjeto(Projeto projeto) {
		Participante participanteLogado = daoParticipanteBusca.listarParticipanteProjeto(projeto).stream()
				.filter(obj -> obj.getPessoa().getId() == usuarioSessao.recuperarPessoa().getId()).findAny()
				.orElse(null);

		participanteLogado.setAceite(true);
		participanteService.inserirAlterar(participanteLogado);
		ExibirMensagem.exibirMensagem("Operação Realizada com Sucesso");
		atualizarListaProjeto();
	}

	// GET SET

	public void verificaDesabilitar(ArquivoProjeto arquivo) {

		System.out.println("chamou aqui no met");

		System.out.println("id da arq proj " + arquivo.getId());
		System.out.println("situação do arquivo projeto " + arquivo.getSituacao());
		System.out.println("situação do projeto  " + arquivo.getProjeto().getSituacao());

	}

	public Projeto getProjeto() {
		return projeto;
	}

	public void setProjeto(Projeto projeto) {
		this.projeto = projeto;
	}

	public Participante getParticipante() {
		return participante;
	}

	public void setParticipante(Participante participante) {
		this.participante = participante;
	}

	public List<Participante> getListaParticipanteProjeto() {
		return listaParticipanteProjeto;
	}

	public void setListaParticipanteProjeto(List<Participante> listaParticipanteProjeto) {
		this.listaParticipanteProjeto = listaParticipanteProjeto;
	}

	public List<ArquivoProjeto> getListaArquivo() {
		return listaArquivo;
	}

	public void setListaArquivo(List<ArquivoProjeto> listaArquivo) {
		this.listaArquivo = listaArquivo;
	}

	public boolean isCadastrar() {
		return cadastrar;
	}

	public void setCadastrar(boolean cadastrar) {
		if (cadastrar)
			novoProjeto();
		this.cadastrar = cadastrar;
	}

	public List<Projeto> getListaProjeto() {
		return listaProjeto;
	}

	public List<ProducaoRelatorio> getListaProducaoRelatorio() {
		return listaProducaoRelatorio;
	}

	public void setListaProducaoRelatorio(List<ProducaoRelatorio> listaProducaoRelatorio) {
		this.listaProducaoRelatorio = listaProducaoRelatorio;
	}

	public void setListaProjeto(List<Projeto> listaProjeto) {
		this.listaProjeto = listaProjeto;
	}

	public boolean isBotaoPrograma() {
		return botaoPrograma;
	}

	public void setBotaoPrograma(boolean botaoPrograma) {
		this.botaoPrograma = botaoPrograma;
	}

	public boolean isBotaoProgramaCiencia() {
		return botaoProgramaCiencia;
	}

	public void setBotaoProgramaCiencia(boolean botaoProgramaCiencia) {
		this.botaoProgramaCiencia = botaoProgramaCiencia;
	}

	public boolean isBotaoBolsista() {
		return botaoBolsista;
	}

	public void setBotaoBolsista(boolean botaoBolsista) {
		this.botaoBolsista = botaoBolsista;
	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	public boolean isDesabilitaDescricao() {
		return desabilitaDescricao;
	}

	public void setDesabilitaDescricao(boolean desabilitaDescricao) {
		this.desabilitaDescricao = desabilitaDescricao;
	}

	public List<Avaliacao> getListaAvaliacao() {
		return listaAvaliacao;
	}

	public void setListaAvaliacao(List<Avaliacao> listaAvaliacao) {
		this.listaAvaliacao = listaAvaliacao;
	}

	public ProducaoRelatorio getProducaoRelatorio() {
		return producaoRelatorio;
	}

	public void setProducaoRelatorio(ProducaoRelatorio producaoRelatorio) {
		this.producaoRelatorio = producaoRelatorio;
	}

	public boolean isTermosSubmissao() {
		return termosSubmissao;
	}

	public void setTermosSubmissao(boolean termosSubmissao) {
		this.termosSubmissao = termosSubmissao;
	}

	public boolean isControleSumirEnviar() {
		return controleSumirEnviar;
	}

	public void setControleSumirEnviar(boolean controleSumirEnviar) {
		this.controleSumirEnviar = controleSumirEnviar;
	}

	public boolean isSumirSalvar() {
		return sumirSalvar;
	}

	public boolean isDesabilitaCaraterizacao() {
		return desabilitaCaraterizacao;
	}

	public void setDesabilitaCaraterizacao(boolean desabilitaCaraterizacao) {
		this.desabilitaCaraterizacao = desabilitaCaraterizacao;
	}

	public void setSumirSalvar(boolean sumirSalvar) {
		this.sumirSalvar = sumirSalvar;
	}

	public boolean isRelatorioFinal() {
		return relatorioFinal;
	}

	public boolean isDesabilitaDataTermino() {
		return desabilitaDataTermino;
	}

	public boolean isDesabilitaCampus() {
		return desabilitaCampus;
	}

	public boolean isDesabilitaUpload() {
		return desabilitaUpload;
	}

	public void setDesabilitaUpload(boolean desabilitaUpload) {
		this.desabilitaUpload = desabilitaUpload;
	}

	public void setDesabilitaCampus(boolean desabilitaCampus) {
		this.desabilitaCampus = desabilitaCampus;
	}

	public void setDesabilitaDataTermino(boolean desabilitaDataTermino) {
		this.desabilitaDataTermino = desabilitaDataTermino;
	}

	public void setRelatorioFinal(boolean relatorioFinal) {
		this.relatorioFinal = relatorioFinal;
	}

}
