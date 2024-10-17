package base.controle;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.enterprise.context.SessionScoped;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import util.AtualizaHorasCertificado;
import util.CriptografiaSenha;
import util.ExibirMensagem;
import util.FecharDialog;
import util.Mensagem;
import util.RecuperarRelacoesProfessor;
import util.ValidaCPF;
import util.ValidacoesGerirUsuarios;
import ac.modelo.AlunoTurma;
import ac.modelo.Certificado;
import ac.modelo.Permissao;
import ac.services.AlunoService;
import base.modelo.Aluno;
import base.modelo.Curso;
import base.modelo.Turma;
import base.service.AlunoTurmaService;
import base.service.CertificadoService;
import base.service.MovimentacaoService;
import dao.GenericDAO;
import dao.MovimentacaoAlunoDAO;
import questionario.modelo.Email;
import questionario.service.EmailService;

//@ViewScoped
@ViewScoped
@Named("alunoMB")
public class AlunoMB implements Serializable {

	private static final long serialVersionUID = 1L;
//	UPDATE `tab_aluno_turma` SET `data_mudanca` = "2017-02-01" WHERE `id_turma` = 

//	select m.`id_movimentacao`, m.`data_movimentacao` from `tab_movimentacao` m inner join `tab_aluno_turma` al on m.`id_pessoa` = al.`id_aluno_turma` inner join `tab_turma` t on al.`id_turma` = t.`id_turma` where t.`id_turma` = 

//	SELECT * FROM  `tab_aluno_turma`  WHERE `id_turma` = 

//	SELECT * FROM  `tab_aluno_turma`  WHERE `id_turma` = 
//
//			UPDATE `tab_aluno_turma` SET `data_mudanca` = "2016-02-01" WHERE `id_turma` = 
//
//			select m.`id_movimentacao`, m.`data_movimentacao` from `tab_movimentacao` m inner join `tab_aluno_turma` al on m.`id_pessoa` = al.`id_aluno_turma` inner join `tab_turma` t on al.`id_turma` = t.`id_turma` where t.`id_turma` = 
//		

//			UPDATE `tab_aluno_turma` SET `data_mudanca` = "2017-02-01"   

	// select * from `tab_movimentacao` m inner join `tab_aluno_turma` al on
	// m.`id_pessoa` = al.`id_aluno_turma` inner join `tab_turma` t on al.`id_turma`
	// = t.`id_turma` where t.`id_turma` = 1

//					UPDATE `tab_movimentacao` SET  `data_movimentacao` = "2017-02-01" from `tab_movimentacao` m inner join `tab_aluno_turma` al on
//
//					m.`id_pessoa` = al.`id_aluno_turma` inner join `tab_turma` t  on al.`id_turma` =
//
//					t.`id_turma` where t.`id_turma` = 1

	private Aluno aluno;
	private List<Aluno> alunos;
	private AlunoTurma alunoTurmaAlterar;

	private List<Turma> turmas;
	private AlunoTurma alunoTurmaAltera;
	private Email email;

	private List<AlunoTurma> alunosAtivos;
	// private Movimentacao auxMovimentacao;
	private AlunoTurma alunoTurma;
	private List<AlunoTurma> listAlunoTurma;

	private List<Turma> turmasProfessor;
	private List<AlunoTurma> alunosAtivosProfessor;
	// private List<Movimentacao> alunosTrancadosProfessor;
	private String alunosImportar = "";
	
	private List<Certificado> certificadosAluno;

	private Permissao permissao;
	// private Movimentacao movs;

	@Inject
	private AlunoService alunoService;

	@Inject
	private ValidacoesGerirUsuarios validacoesGerirUsuarios;

	@Inject
	private AlunoTurmaService alunoTurmaService;

	@Inject
	private MovimentacaoService movimentacaoService;

	@Inject
	private GenericDAO<Aluno> daoAluno;

	@Inject
	private GenericDAO<AlunoTurma> daoAlunoTurma;

	@Inject
	private GenericDAO<Turma> daoTurma;

	@Inject
	private GenericDAO<Curso> daoCurso;

	@Inject
	private MovimentacaoAlunoDAO movimentacaoAlunoDAO;

	@Inject
	private EmailService emailService;

	@Inject
	private CertificadoService certificadoService;

	@Inject
	private RecuperarRelacoesProfessor relacoesProfessor;

	@Inject
	private AtualizaHorasCertificado atualizaHorasCertificado;
	
	@Inject
	private GenericDAO<Certificado> daoCertificado;

	@PostConstruct
	public void inicializar() {
		// System.out.println("No Inicializar");
		criarNovoObjetoAluno();
		// dao = new DAOGenerico();
		// daoMovimentacaoAluno = new DAOMovimentacaoAluno();
		alunos = daoAluno.listaComStatus(Aluno.class);

		turmasProfessor = new ArrayList<>();
		permissao = new Permissao();
		turmas = new ArrayList<>();
		alunosAtivos = new ArrayList<>();
		alunoTurmaAltera = new AlunoTurma();
		alunoTurma = new AlunoTurma();
		email = new Email();
		alunosAtivosProfessor = new ArrayList<>();
		alunoTurmaAlterar = new AlunoTurma();

		listAlunoTurma = new ArrayList<>();
		atualizarListas();
		certificadosAluno = new ArrayList<Certificado>();

	}
	
	public void buscarCertificadosAluno(Aluno aluno) {
		certificadosAluno = daoCertificado.listar(Certificado.class, "alunoTurma.aluno.id="+aluno.getId());
	}

	public void importarAlunos() {
		String[] linhas = alunosImportar.split("\n");
		String mensagem = "";
		for (int x = 0; x < linhas.length; x++) {

			String[] separados = linhas[x].split("\t");

			if (separados.length == 6) {
				String nome = separados[0].trim();
				String ra = separados[1].trim();
				String cpf = separados[2].trim();
				String email = separados[3].trim();
				String dataMatricula = separados[4].trim();
				String descricaoTurma = separados[5].trim();

				List<Turma> lt = daoTurma.listar(Turma.class, "descricao like '%" + descricaoTurma + "%'");

				if (lt.size() > 0) {
					Turma turma = lt.get(0);

					if (cpf.length() == 11) {
						cpf = cpf.substring(0, 3) + "." + cpf.substring(3, 6) + "." + cpf.substring(6, 9) + "-"
								+ cpf.substring(9, 11);
					}
					String cpfLimpo = cpf.replace(".", "").replace("-", "");
					List<Aluno> la = daoAluno.listar(Aluno.class, "cpf='" + cpf + "' or cpf='" + cpfLimpo + "'");
					Aluno aluno = new Aluno();
					if (la.size() > 0) {
						aluno = la.get(0);
						aluno.setUsuario(email);
					} else {
						aluno.setNome(nome);
						aluno.setCpf(cpf);
						aluno.setUsuario(email);
						aluno.setSenha("000");
						aluno.setPerfilAluno("aluno");
						if (ValidaCPF.isCPF(cpfLimpo)) {
							alunoService.inserirAlterar(aluno);
						} else {
							mensagem += "\n" + nome + " CPF " + cpf + " INVÁLIDO";
						}
						alunoService.inserirAlterar(aluno);
						// System.out.println(aluno);
					}
					// System.out.println(aluno.getId());
					List<AlunoTurma> at = daoAlunoTurma.listar(AlunoTurma.class,
							"aluno.id=" + aluno.getId() + " and turma.id=" + turma.getId());

					if (at.size() == 0) {
						AlunoTurma alunoTurma = new AlunoTurma();
						alunoTurma.setRa(ra);
						alunoTurma.setAluno(aluno);
						alunoTurma.setTurma(turma);
						Date dat = new Date();
						try {
							SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
							dat = formatter.parse(dataMatricula);
							alunoTurma.setDataMudanca(dat);
						} catch (ParseException e) {
							e.printStackTrace();
						}
						alunoTurmaService.inserirAlterar(alunoTurma);
						// System.out.println(alunoTurma);

//						Movimentacao movimentacao = new Movimentacao();
//						movimentacao.setDataMovimentacao(alunoTurma.getDataMudanca());
//						movimentacao.setSituacao(1);
//						movimentacao.setAlunoTurma(alunoTurma);
//						movimentacao.setStatus(true);
//						movimentacao.setControle(true);
//
//						movimentacaoService.inserirAlterar(movimentacao);
						// System.out.println(movimentacao);
					} else {
						alunoTurma.setRa(ra);
						alunoTurmaService.inserirAlterar(alunoTurma);
						mensagem += "\n" + nome + " - Aluno Turma já cadastrado! \n";
					}
				} else {
					mensagem += "\n" + nome + " - turma " + descricaoTurma + " não encontrada \n";
				}
			}
		}
		if (mensagem.length() == 0) {
			mensagem = "Aparentemente todos os alunos foram importados";
		}
		setAlunosImportar(mensagem);
		atualizarListas();
	}

	public void buscarRaAluno(AlunoTurma m) {
		alunoTurma = daoAlunoTurma.buscarPorId(AlunoTurma.class, m.getId());
		aluno = daoAluno.buscarPorId(Aluno.class, alunoTurma.getAluno().getId());
	}

	public void adicionarTurma() {

		if (listAlunoTurma.size() == 0) {
			listAlunoTurma.add(alunoTurma);
			alunoTurma = new AlunoTurma();
		} else {

			if (validarRelaciomanetoAluno(alunoTurma)) {
				listAlunoTurma.add(alunoTurma);
				alunoTurma = new AlunoTurma();
			} else {
				ExibirMensagem.exibirMensagem(Mensagem.CURSOSELECIONADO);
			}
		}
	}

	public void tirarCurso(AlunoTurma alunoRemove) {

		listAlunoTurma.remove(alunoRemove);
	}

	public boolean validarRelaciomanetoAluno(AlunoTurma alunoturma) {
		for (AlunoTurma tt : listAlunoTurma) {
			if (tt.getTurma().getCurso().getNome().equals(alunoturma.getTurma().getCurso().getNome())) {
				return false;
			}
		}

		return true;
	}

	public void abrirCurso() {
		FecharDialog.abrirDialogSalvarAluno();

	}

	public void alterar(Aluno aluno) {
		this.aluno = aluno;
		// this.movimentacao = movimentacao;
		// listAlunoTurma = daoAlunoTurma.listar(AlunoTurma.class,
		// " controle = 1 and aluno = " +
		// movimentacao.getAlunoTurma().getAluno().getId());
		// aluno = daoAluno.buscarPorId(Aluno.class,
		// movimentacao.getAlunoTurma().getAluno().getId());
	}

	public void excluirAlunoTurma(AlunoTurma al) {
		al.setStatus(false);
		alunoTurmaService.inserirAlterar(al);
		buscarTurmasAluno(al.getAluno());
	}

	public void alterarCurso(Aluno aluno) {
		this.aluno = aluno;
		// this.movimentacao = movimentacao;

		buscarTurmasAluno(aluno);
	}

	private void buscarTurmasAluno(Aluno al) {
		listAlunoTurma = daoAlunoTurma.listar(AlunoTurma.class,
				"aluno.id = " + al.getId() + " order by dataMudanca desc");
	}

	public void alterarDataMudanca(AlunoTurma al) {

		// this.movimentacao = movimentacao;
		alunoTurmaAltera = al;

	}

	public void alterarTurmaCursoAluno(AlunoTurma at) {
		this.alunoTurma = at;
	}

	public String formatarData(Date dataFormatar) {
		DateFormat format = new SimpleDateFormat("dd/MM/yyyy");
		return format.format(dataFormatar);
	}

	public void salvarTurma() {

		if (this.alunoTurma.getId() != null && this.alunoTurma.getId() > 0) {
			//System.out.println("ALTERAR");
			alunoTurmaService.inserirAlterar(alunoTurma);

			// atualizaHorasCertificado.alterarHoras(a);

			this.buscarTurmasAluno(alunoTurma.getAluno());
			this.alunoTurma = new AlunoTurma();
			ExibirMensagem.exibirMensagem("Alterado!");
		} else {

			try {

				if (validacoesGerirUsuarios.buscarRA(alunoTurma)) {
					ExibirMensagem.exibirMensagem(Mensagem.RA);
				} else {

					if (validarRelaciomanetoAluno(alunoTurma)) {

						if (validarRelacionarCursosAluno(alunoTurma)) {

							alunoTurma.setAluno(aluno);
							alunoTurma.setControle(1);
							alunoTurma.setStatus(true);
							alunoTurma.setPermiteCadastroCertificado(1); // coloquei aqui

							alunoTurmaService.inserirAlterar(alunoTurma);

							// atualizaHorasCertificado.alterarHoras(a);

							this.buscarTurmasAluno(alunoTurma.getAluno());
							this.alunoTurma = new AlunoTurma();
							ExibirMensagem.exibirMensagem("Curso atribuído!" + "");

						}

					} else {
						ExibirMensagem.exibirMensagem(Mensagem.CURSOSELECIONADO);
					}

				}
			} catch (Exception e) {
				ExibirMensagem.exibirMensagem(Mensagem.ERRO);
				e.printStackTrace();
			}
		}
	}

	public void fecharDialog() {
		FecharDialog.fecharDialogAlunoCursoEditar();
		FecharDialog.fecharDialogEditarAluno();
		ExibirMensagem.exibirMensagem(Mensagem.SUCESSO);
	}

	public void salvar() {
		try {

			if (listAlunoTurma.size() == 0) {
				ExibirMensagem.exibirMensagem(Mensagem.CURSOCADASTRO);
			} else {

				if (validacoesGerirUsuarios.buscarUsuarios(aluno)) {
					ExibirMensagem.exibirMensagem(Mensagem.USUARIO);
				} else if (validacoesGerirUsuarios.buscarRA(alunoTurma)) {
					ExibirMensagem.exibirMensagem(Mensagem.RA);
				} else {

					if (aluno.getSenha().isEmpty()) {
						aluno.setSenha("123asdds25");
					}

					aluno.setDataCadastro(new Date());
					aluno.setStatus(true);
					aluno.setPerfilAluno("aluno");
					aluno.setSenha(CriptografiaSenha.criptografar(aluno.getSenha()));
					// dao.inserir(aluno);

					alunoService.inserirAlterar(aluno);

					for (AlunoTurma a : listAlunoTurma) {

						a.setAluno(aluno);
						a.setControle(1);
						a.setStatus(true);
						a.setPermiteCadastroCertificado(1); // coloquei aqui
						alunoTurmaService.inserirAlterar(a);

//						movimentacao = new Movimentacao();
//
//						movimentacao.setDataMovimentacao(a.getDataMudanca());
//						movimentacao.setSituacao(1);
//						movimentacao.setAlunoTurma(a);
//						movimentacao.setStatus(true);
//						movimentacao.setControle(true);
//
//						movimentacaoService.inserirAlterar(movimentacao);

					}
					FecharDialog.fecharDialogAlunoCurso();
					FecharDialog.fecharDialogAluno();
					ExibirMensagem.exibirMensagem(Mensagem.SUCESSO);
					criarNovoObjetoAluno();
					atualizarListas();
				}

			}

		} catch (Exception e) {
			ExibirMensagem.exibirMensagem(Mensagem.ERRO);
			e.printStackTrace();
		}
	}

	private Boolean validarRelacionarCursosAluno(AlunoTurma alTurma) {
		List<AlunoTurma> alunoBusca = new ArrayList<>();
		try {
			alunoBusca = daoAlunoTurma.listar(AlunoTurma.class,
					" turma.id = " + alTurma.getTurma().getId() + " and status = true and aluno.id = " + aluno.getId());
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (alunoBusca.isEmpty())
			return true;
		return false;
	}

	public Boolean validarCadastrar() {

		if (!ValidaCPF.isCPF(aluno.getCpf().trim())) {
			ExibirMensagem.exibirMessageFatal("CPF Inválido");
			return false;
		}

		String cpfLimpo = aluno.getCpf().replace(".", "").replace("-", "").trim();
		List<Aluno> la = daoAluno.listar(Aluno.class, "cpf='" + aluno.getCpf().trim() + "' or cpf='" + cpfLimpo + "'");
		if (la.size() > 0) {
			ExibirMensagem.exibirMessageFatal("CPF já cadastrado");
			return false;
		}

		return true;
	}

	public void salvarEditar() {
	
		aluno.setPerfilAluno("aluno");
		if (aluno.getId() == null) {
			if (validarCadastrar()) {
				try {
					if ((validacoesGerirUsuarios.buscarUsuarios(aluno))
							&& (validacoesGerirUsuarios.buscarUsuarioAlterar(aluno))) {
						ExibirMensagem.exibirMessageFatal(Mensagem.USUARIO);
					} else {

						aluno.setDataCadastro(new Date());
						aluno.setStatus(true);
						alunoService.inserirAlterar(aluno);

						// FecharDialog.fecharDialogEditarAluno();
						ExibirMensagem.exibirMensagem(Mensagem.SUCESSO);

					}

				} catch (Exception e) {
					ExibirMensagem.exibirMensagem(Mensagem.ERRO);
					e.printStackTrace();
				}
			}
		} else {
			try {
				if ((validacoesGerirUsuarios.buscarUsuarios(aluno))
						&& (validacoesGerirUsuarios.buscarUsuarioAlterar(aluno))) {
					ExibirMensagem.exibirMessageFatal(Mensagem.USUARIO);
				} else {

					alunoService.inserirAlterar(aluno);

					// FecharDialog.fecharDialogEditarAluno();
					ExibirMensagem.exibirMensagem(Mensagem.SUCESSO);

				}

			} catch (Exception e) {
				ExibirMensagem.exibirMensagem(Mensagem.ERRO);
				e.printStackTrace();
			}
		}

		atualizarListas();
	}

	public void editarData() {
//		select * from `tab_movimentacao` m inner join `tab_aluno_turma` al on m.`id_pessoa` = al.`id_aluno_turma` inner join `tab_turma` t on al.`id_turma` = t.`id_turma` where t.`id_turma` = 1
		// **
		alunoTurmaService.inserirAlterar(alunoTurmaAltera);

		// movimentacao.setDataMovimentacao(alunoTurmaAltera.getDataMudanca());
		// movimentacaoService.inserirAlterar(movimentacao);

		FecharDialog.fecharDialogDATAAluno();
		ExibirMensagem.exibirMensagem(Mensagem.SUCESSO);
		alunoTurmaAltera = new AlunoTurma();
		// movimentacao = new Movimentacao();

		atualizarListas();
	}

	public void salvarSenha() {
		try {

			aluno.setSenha(CriptografiaSenha.criptografar(aluno.getSenha()));
			aluno.setPerfilAluno("aluno");
			alunoService.inserirAlterar(aluno);

			FecharDialog.fecharDialogEditarSenha();
			ExibirMensagem.exibirMensagem(Mensagem.SUCESSO);
			atualizarListas();

		} catch (Exception e) {
			ExibirMensagem.exibirMensagem(Mensagem.ERRO);
			e.printStackTrace();
		}
	}

	public void inativar(Aluno alunoExcluir) {
		
		if(alunoExcluir!=null && alunoExcluir.getId()!=null) {
			try {
		
			List<AlunoTurma> le = daoAlunoTurma.listar(AlunoTurma.class, "aluno.id="+alunoExcluir.getId());
			for(AlunoTurma a:le) {
				a.setStatus(false);
				alunoTurmaService.inserirAlterar(a);
			}
			
			alunoExcluir.setStatus(false);
			alunoService.inserirAlterar(alunoExcluir);
			ExibirMensagem.exibirMensagem("Aluno Excluído!");
			this.atualizarListas();
			}catch (Exception e) {
				// TODO: handle exception
			}
		}
//		try {
//
//			List<AlunoTurma> listAlunoTurmas = new ArrayList<>();
//			listAlunoTurmas = daoAlunoTurma.listar(AlunoTurma.class,
//					" aluno.id =" + movimentacao.getAlunoTurma().getAluno().getId());
//			List<Movimentacao> m = new ArrayList<>();
//
//			for (AlunoTurma a : listAlunoTurmas) {
//				inavitarMovimentacao(a);
//			}
//
//		} catch (Exception e) {
//			ExibirMensagem.exibirMensagem(Mensagem.ERRO);
//		}
//		criarNovoObjetoAluno();
	}

	public void inavitarMovimentacao(AlunoTurma aluno) {

//		List<Movimentacao> listMov = new ArrayList<>();
//		listMov = daoMovimentacao.listar(Movimentacao.class, " alunoTurma.id = " + aluno.getId());
//		for (Movimentacao m : listMov) {
//
//			m.setSituacao(0);
//			m.setControle(true); // excluir
//			m.setDataMovimentacao(new Date());
//			m.setStatus(false);
//			m.getAlunoTurma().getAluno().setStatus(false);
//			movimentacaoService.inserirAlterar(m);
//			alunoService.inserirAlterar(m.getAlunoTurma().getAluno());
//
//			inativarCertificados(m.getAlunoTurma().getId());
//			inativarMovimentacoes(m.getAlunoTurma().getId());
//			inativarAlunoTurma(m.getAlunoTurma().getAluno().getId());
//
//		}
//		ExibirMensagem.exibirMensagem(Mensagem.SUCESSO);
//		atualizarListas();
	}

	public List<Turma> completarTurma(String str) {
		preencherListaTurma();
		List<Turma> turmasSelecionadas = new ArrayList<>();
		for (Turma t : turmas) {
			if (t.getDescricao().startsWith(str)) {
				turmasSelecionadas.add(t);
			}
		}
		return turmasSelecionadas;
	}

	public void criarNovoObjetoAluno() {
		aluno = new Aluno();

		alunoTurma = new AlunoTurma();
		listAlunoTurma = new ArrayList<>();
	}

	public void preencherListaAlunos() {
		alunos = daoAluno.listaComStatus(Aluno.class);
	}

	public void preencherListaTurma() {
		turmas = new ArrayList<>();
		turmas = daoTurma.listaComStatus(Turma.class);
	}

	public void atualizarListas() {
		preencherListaAlunos();

	}

	public void inativarCertificados(Long id) {
		try {
			certificadoService.update(" Certificado set status = false where alunoTurma = " + id);
			ExibirMensagem.exibirMensagem(Mensagem.INATIVAR_CERTIFICADOS);
		} catch (Exception e) {
			ExibirMensagem.exibirMensagem(Mensagem.ERRO_INATIVAR_CERTIFICADOS);
		}

	}

	public void inativarMovimentacoes(Long id) {

		try {
			movimentacaoService.update(" Movimentacao set status = false where alunoTurma = " + id);

			ExibirMensagem.exibirMensagem(Mensagem.INATIVAR_MOVIMENTACOES);
		} catch (Exception e) {
			ExibirMensagem.exibirMensagem(Mensagem.ERRO_INATIVAR_MOVIMENTACOES);
		}

	}

	public void inativarAlunoTurma(Long id) {

		try {
			alunoTurmaService.update(" AlunoTurma set status = false where aluno = " + id);

			ExibirMensagem.exibirMensagem(Mensagem.INATIVAR_ALUNO_TURMA);
		} catch (Exception e) {
			ExibirMensagem.exibirMensagem(Mensagem.ERRO_INATIVAR_ALUNO_TURMA);
		}

	}

	public Turma validarDataTurmaComAluno() {
		Turma turma = new Turma();
		turma = daoTurma.listar(Turma.class, " id = " + alunoTurma.getTurma().getId()).get(0);
		return turma;
	}

	public AlunoTurma permitirCadastrarAlunoTurma(Aluno aluno) {
		AlunoTurma alunoTurma = new AlunoTurma();

		alunoTurma = movimentacaoAlunoDAO.buscarMaiorAlunoTurma(aluno.getId()).get(0);
		return alunoTurma;
	}

	public List<Turma> completarTurmaProfessor(String str) {
		preencherListaTurmaProfessor();
		List<Turma> turmasSelecionadas = new ArrayList<>();
		for (Turma t : turmasProfessor) {
			if (t.getDescricao().startsWith(str)) {
				turmasSelecionadas.add(t);
			}
		}
		return turmasSelecionadas;
	}

	public void preencherListaTurmaProfessor() {

		turmasProfessor = relacoesProfessor.recuperarTurmasProfessor();
	}

//	public void preencherListaAlunosAtivosProfessor() {
//		alunosAtivosProfessor = new ArrayList<>();
//		alunosAtivosProfessor = relacoesProfessor.recuperarAlunoMovimentacaoAtivo();
//}

//	public void preencherListaAlunosTrancadosProfessor() {
//		alunosTrancadosProfessor = new ArrayList<>();
//		alunosTrancadosProfessor = relacoesProfessor.recuperarAlunoMovimentacaoTrancados();

//	}

	public Aluno getAluno() {
		return aluno;
	}

	public void setAluno(Aluno aluno) {
		this.aluno = aluno;
	}

	public List<Turma> getTurmas() {
		return turmas;
	}

	public void setTurmas(List<Turma> turmas) {
		this.turmas = turmas;
	}

	public AlunoTurma getAlunoTurma() {
		return alunoTurma;
	}

	public void setAlunoTurma(AlunoTurma alunoTurma) {
		this.alunoTurma = alunoTurma;
	}

	public List<Turma> getTurmasProfessor() {
		return turmasProfessor;
	}

	public void setTurmasProfessor(List<Turma> turmasProfessor) {
		this.turmasProfessor = turmasProfessor;
	}

	public AlunoTurma getAlunoTurmaAltera() {
		return alunoTurmaAltera;
	}

	public void setAlunoTurmaAltera(AlunoTurma alunoTurmaAltera) {
		this.alunoTurmaAltera = alunoTurmaAltera;
	}

	public List<AlunoTurma> getListAlunoTurma() {
		return listAlunoTurma;
	}

	public void setListAlunoTurma(List<AlunoTurma> listAlunoTurma) {
		this.listAlunoTurma = listAlunoTurma;
	}

	public AlunoTurma getAlunoTurmaAlterar() {
		return alunoTurmaAlterar;
	}

	public void setAlunoTurmaAlterar(AlunoTurma alunoTurmaAlterar) {
		this.alunoTurmaAlterar = alunoTurmaAlterar;
	}

	public String getAlunosImportar() {
		return alunosImportar;
	}

	public void setAlunosImportar(String alunosImportar) {
		this.alunosImportar = alunosImportar;
	}

	public List<Aluno> getAlunos() {
		return alunos;
	}

	public void setAlunos(List<Aluno> alunos) {
		this.alunos = alunos;
	}

	public List<Certificado> getCertificadosAluno() {
		return certificadosAluno;
	}

	public void setCertificadosAluno(List<Certificado> certificadosAluno) {
		this.certificadosAluno = certificadosAluno;
	}
	
	

}
