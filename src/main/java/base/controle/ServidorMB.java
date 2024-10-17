package base.controle;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.enterprise.context.SessionScoped;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;


import util.CriptografiaSenha;
import util.ExibirMensagem;
import util.FecharDialog;
import util.Mensagem;
import util.ValidacoesGerirUsuarios;
import ac.controle.UsuarioSessaoMB;

import ac.modelo.Permissao;
import ac.modelo.Pessoa;
import ac.modelo.ProfessorCurso;

import base.modelo.Curso;
import base.modelo.Servidor;
import base.modelo.Tipo;
import base.service.PermissaoService;
import base.service.ProfessorCursoService;
import base.service.ServidorService; 
import dao.GenericDAO;
import dao.UsuarioDAO;



@ViewScoped
@Named("servidorMB")
public class ServidorMB implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	//private DAOGenerico dao;
	private List<Tipo> listTipoSelecionado;
	private List<Tipo> tipoSalvar;
	private Tipo permissaoAdicionada;
	
	private List<Curso> listCursoSelecionado;
	private List<Curso> listCursoCompletar;
	private Curso cursoAdicionado;
	private List<Curso> listCursoSalvar;
	private List<Servidor> listServidor;
	private boolean controle = true;
	private Servidor servidor;
	private Permissao permissao;
	private ProfessorCurso professorCurso;
	private boolean controleDesativar = true;
	private List<ProfessorCurso> professorCursoRemove;
	private List<Permissao> permissaoRemove;
	private Permissao removerPermissao;
	private Tipo permissaoParaRemove;
	private List<Permissao> permissaoRemoveSelecionada;
	private List<ProfessorCurso> professorCursoSelecionado;
	

	private boolean controleRadio = true;
	int flag = 0;
	private boolean controleSenha;
	private boolean controleAddTipo = true;
	private boolean controleAddCurso = true;
	
	@Inject
	private UsuarioDAO daoUsuarios;
	
	@Inject
	private ServidorService servidorService;
	
	@Inject
	private PermissaoService permissaoService;
	
	@Inject
	private ProfessorCursoService professorCursoService;
	
	@Inject
	private GenericDAO<Tipo> daoTipo;
	
	@Inject
	private GenericDAO<Permissao> daoPermissao;
	
	@Inject
	private GenericDAO<ProfessorCurso> daoProfessorCurso;
	
    @Inject
    private GenericDAO<Pessoa> daoPessoa;
   
    @Inject
    private GenericDAO<Servidor> daoServidor;

	@Inject
	private GenericDAO<Curso> daoCurso;
	
	@Inject
	private UsuarioSessaoMB usuarioSessao;
	
	@Inject
	private ValidacoesGerirUsuarios validacoesGerirUsuarios;

	@PostConstruct
	public void inicializar() {

		professorCursoSelecionado = new ArrayList<>();
		
		listTipoSelecionado = new ArrayList<>();
		tipoSalvar = new ArrayList<>();
		servidor = new Servidor();
		permissao = new Permissao();
		professorCurso = new ProfessorCurso();
		listServidor = new ArrayList<>();
		professorCursoRemove = new ArrayList<>();
	
	
		permissaoRemove = new ArrayList<>();
		permissaoRemoveSelecionada = new ArrayList<>();
		
		listCursoSelecionado = new ArrayList<>();
		listCursoSalvar = new ArrayList<>();
		permissaoAdicionada = new Tipo();
		cursoAdicionado = new Curso();
		listCursoCompletar = new ArrayList<>();
		permissaoParaRemove = new Tipo();
		removerPermissao = new Permissao();

	}

	public void preencherListaServidor(Servidor servid) {
		this.servidor = new Servidor();
		this.servidor = servid;
		controleSenha = false;
		controleAddTipo = true;
		controleRadio = true;
		// rem
		tipoSalvar.clear();
		for (Permissao p : daoUsuarios.buscarPermissaoServidor(" status = true and servidor = " + servidor.getId())) {
			tipoSalvar.add(p.getTipo());
		}
		
		List<String> roles = new ArrayList<>();
		
		for(Tipo t : tipoSalvar) {
			roles.add(t.getDescricao());
		}
		
		if(roles.contains("professor")) {
			controle = false;
			controleAddCurso = false;
			listCursoSelecionado.clear();
			
			for (ProfessorCurso c : daoUsuarios
					.buscarCursosProfessor(" status = true and professor = " + servidor.getId())) {
				listCursoSelecionado.add(c.getCurso());

			}
			
		}else {
			controleAddCurso = true;
			controle = true;
			listCursoSelecionado = new ArrayList<>();
		}
		
		if(!roles.contains("secretaria")) {
			controleRadio = false;
		}

	}

	public void controle() {
		controleAddTipo = false;

	}

	public void cancelar() {
		controleAddTipo = true;
		FecharDialog.fecharDialogServidor();
	}

	public void adicionaPermissao() {

		permissaoAdicionada = daoTipo.buscarPorId(Tipo.class, permissao.getTipo().getId());
		if(servidor.getId() == null){
			
			if(validarRelaciomanetoPermissao(permissaoAdicionada)){
				tipoSalvar.add(permissaoAdicionada);

				controlar();
				controleChefe();
			}else{
			   ExibirMensagem.exibirMensagem(Mensagem.PERMISSAOADICIONADA);
			}
			
		}else{
			
			if (validarRelaciomanetoPermissao(permissaoAdicionada)) {
				tipoSalvar.add(permissaoAdicionada);
				
					permissao.setDataInclusao(new Date());
					permissao.setServidor(servidor);
					permissao.setStatus(true);
					permissao.setTipo(permissaoAdicionada);
					
						permissaoService.inserirAlterar(permissao);
				        ExibirMensagem.exibirMensagem(Mensagem.SUCESSO);
					permissao = new Permissao();
				
				
			} else {
				ExibirMensagem.exibirMensagem(Mensagem.PERMISSAOADICIONADA);
			}
		


			controlar();
			controleChefe();
		
		}
	}

	public void adicionaProfessorCurso() {

		cursoAdicionado = daoCurso.buscarPorId(Curso.class, professorCurso.getCurso().getId());

		if (listCursoSelecionado.size() == 0) {
			listCursoSelecionado.add(cursoAdicionado);
		} else {

			if (validarRelaciomanetoCurso(cursoAdicionado)) {
				listCursoSelecionado.add(cursoAdicionado);
			} else {
				ExibirMensagem.exibirMensagem(Mensagem.CURSOJAADICIONADO);
			}

		}
	}

	public boolean validarRelaciomanetoPermissao(Tipo tipo) {

		for (Tipo tt : tipoSalvar) {
			if (tt.getDescricao().equals(tipo.getDescricao())) {
				return false;
			}
		}

		return true;
	}

	public boolean validarRelaciomanetoCurso(Curso curso) {

		for (Curso tt : listCursoSelecionado) {
			if (tt.getNome().equals(curso.getNome())) {
				return false;
			}
		}
		return true;
	}

	public void removerPermissao(Tipo tipo) {
		
		if (servidor.getId() == null) {
		
			if(tipo.getDescricao().equalsIgnoreCase("professor")){
				if(listCursoSelecionado.size()>0){
					ExibirMensagem.exibirMensagem(Mensagem.REMOVERCURSO);
				}else{
					tipoSalvar.remove(tipo);
					controlar();
				}
			}else{
			tipoSalvar.remove(tipo);
			controleChefe();
			}
			
		} else {
		 
			if(tipo.getDescricao().equalsIgnoreCase("professor")){
				if(listCursoSelecionado.size() >0){
					ExibirMensagem.exibirMensagem(Mensagem.REMOVERCURSO);
				}else{
					remover(tipo);
				}
			}else{
				  remover(tipo);
			}
			
	}

	}
	
	
	public void remover(Tipo tipo){
		permissaoRemove = daoPermissao.listar(Permissao.class,
				" servidor = " + servidor.getId() + " and tipo = " + tipo.getId());

		if (permissaoRemove.size() > 0) {

			for (Permissao p : permissaoRemove) {

				p.setStatus(false);
				permissaoService.inserirAlterar(p);

			}
			int flag = 1;
			ExibirMensagem.exibirMensagem(Mensagem.SUCESSO);
			preencherListaServidor(servidor);

		}
	}

	public void removerCurso(Curso curso) {

		if (servidor.getId() == null) {
			listCursoSelecionado.remove(curso);
		} else {

			List<Permissao> permiss = new ArrayList<>();
			permiss = daoPermissao.listar(Permissao.class,
					" servidor = " + servidor.getId() + " and tipo.descricao = 'professor'");

			if (permiss.size() == 0) {

//				Curso cursoRem = new Curso();
//				cursoRem =  daoCurso.buscarPorId(Curso.class, curso.getId());

				listCursoSelecionado.remove(curso);

			} else {

				List<ProfessorCurso> cursoRemov = new ArrayList<>();

				cursoRemov = daoProfessorCurso.listar(ProfessorCurso.class,
						" professor = " + servidor.getId() + " and status = true and curso = " + curso.getId());

				for (ProfessorCurso p : cursoRemov) {
					p.setStatus(false);
					professorCursoService.inserirAlterar(p);
				}

				List<ProfessorCurso> cursoTodos = new ArrayList<>();

				cursoTodos = daoProfessorCurso.listar(ProfessorCurso.class,
						" professor = " + servidor.getId() + " and status = true ");

				if (cursoTodos.size() == 0) {

					for (Tipo t : tipoSalvar) {

						if (t.getDescricao().equalsIgnoreCase("professor")) {

							List<Permissao> permissaoRemov = new ArrayList<>();
							permissaoRemov = daoPermissao.listar(Permissao.class,
									" servidor = " + servidor.getId() + " and tipo = " + t.getId());

							if (permissaoRemov.size() > 0) {

								for (Permissao p : permissaoRemov) {
									p.setStatus(false);
									permissaoService.inserirAlterar(p);
								}
							}

						}

					}

				}
				int flag = 1;
				ExibirMensagem.exibirMensagem(Mensagem.SUCESSO);
				preencherListaServidor(servidor);

			}

		}
	}

	public List<Tipo> completaPermissao(String str) {
		listTipoSelecionado = daoTipo.listaComStatus(Tipo.class);
		List<Tipo> listTipos = new ArrayList<>();
		for (Tipo cur : listTipoSelecionado) {
			if (cur.getDescricao().toLowerCase().startsWith(str)) {
				listTipos.add(cur);
			}
		}
		return listTipos;
	}

	public List<Curso> completaCurso(String str) {
		listCursoCompletar = daoCurso.listaComStatus(Curso.class);
		List<Curso> listTipos = new ArrayList<>();
		for (Curso cur : listCursoCompletar) {
			if (cur.getNome().toLowerCase().startsWith(str)) {
				listTipos.add(cur);
			}
		}
		return listTipos;
	}

	public void controlar() {

		for (Tipo l : tipoSalvar) {

			if (l.getDescricao().equals("professor") || l.getDescricao().equals("professores")) {
				controle = false;
				controleAddCurso = false;

				break;

			} else {

				controle = true;
				controleAddCurso = true;

				listCursoSelecionado = new ArrayList<>();

			}

		}
	}

	public void controleChefe() {
		for (Tipo l : tipoSalvar) {
			if (l.getDescricao().equals("secretaria")) {
				controleRadio = false;
				break;
			} else {
				controleRadio = true;
			}
		}
	}

	public void removerCurso(Servidor servidor) {

		this.servidor = servidor;
		List<Permissao> permissao = new ArrayList<>();
		permissao = daoPermissao.listar(Permissao.class,
				" servidor = " + servidor.getId() + " and status = true and tipo.descricao = 'professor'");

		if (permissao.size() > 0) {
			professorCursoRemove = daoProfessorCurso.listar(ProfessorCurso.class,
					" professor = " + servidor.getId() + " and status = true");

		} else {
			FecharDialog.fecharDialogServidorCruso();
			ExibirMensagem.exibirMensagem(Mensagem.SERVIDOR);
		}

	}

	public void buscarPermissao() {
		List<Permissao> permissao = new ArrayList<>();
		permissao = daoPermissao.listar(Permissao.class, " servidor = " + servidor.getId());

		for (Permissao pp : permissao) {

			if (pp.getTipo().getDescricao().equals("professor")) {
				removerCurso(servidor);
				inativarCursoProfessorTodos();
			}

		}
	}

	public void inativarCursoProfessorTodos() {
		for (ProfessorCurso p : professorCursoSelecionado) {
			p.setStatus(false);
		    professorCursoService.inserirAlterar(p);

			carregaLista();
			ExibirMensagem.exibirMensagem(Mensagem.SUCESSO);
			FecharDialog.fecharDialogServidorCruso();
		}
	}

	public void inativarCursoProfessor() {

		if (professorCursoRemove.size() == 1) {
			ExibirMensagem.exibirMensagem(Mensagem.CURSOPROFESSOR);
		} else {

			for (ProfessorCurso p : professorCursoSelecionado) {
				p.setStatus(false);
				professorCursoService.inserirAlterar(p);
			}
			carregaLista();

			ExibirMensagem.exibirMensagem(Mensagem.SUCESSO);
			FecharDialog.fecharDialogServidorCruso();
		}
	}

	public void salvarSenha() {
		try {

			servidor.setSenha(CriptografiaSenha.criptografar(servidor.getSenha()));
			servidorService.inserirAlterar(servidor);

			FecharDialog.fecharDialogEditarSenhaServidor();
			ExibirMensagem.exibirMensagem(Mensagem.SUCESSO);
			carregaLista();

		} catch (Exception e) {
			ExibirMensagem.exibirMensagem(Mensagem.ERRO);
			e.printStackTrace();
		}
	}

	public Boolean verificaProfessor() {
		for (Tipo t : tipoSalvar) {
			if (t.getDescricao().equalsIgnoreCase("professor")) {
				if (listCursoSelecionado.size() == 0) {
					return false;
				}
			}
		}
		return true;
	}

	public void salvar() {

		if (flag == 1) {
			criarNovoObjeto();
			carregaLista();
			ExibirMensagem.exibirMensagem(Mensagem.SUCESSO);
			FecharDialog.fecharDialogServidor();
			flag = 0;
		} else {
			try {

				if (verificaProfessor() == false) {
					ExibirMensagem.exibirMensagem(Mensagem.SELECIONEPROFESSOR);
				} else {

					if (servidor.getId() == null) {

						if (tipoSalvar.size() > 0) {

							if (validacoesGerirUsuarios.buscarUsuarios(servidor)) {
								ExibirMensagem.exibirMensagem(Mensagem.USUARIO);
							} else if (validacoesGerirUsuarios.buscarSiape(servidor)) {
								ExibirMensagem.exibirMensagem(Mensagem.SIAPE);
							} else {
								if (servidor.getSenha().isEmpty()) {
									servidor.setSenha("123");
								}

								if (servidor.getChefe() == null) {
									servidor.setChefe(false);
								}
								servidor.setDataCadastro(new Date());
								servidor.setStatus(true);
								servidor.setSenha(CriptografiaSenha.criptografar(servidor.getSenha()));
								servidorService.inserirAlterar(servidor);
								for (Tipo t : tipoSalvar) {
									permissao.setDataInclusao(new Date());
									permissao.setServidor(servidor);
									permissao.setStatus(true);
									permissao.setTipo(t);
									permissaoService.inserirAlterar(permissao);

									permissao = new Permissao();
								}
								for (Tipo t : tipoSalvar) {
									if (t.getDescricao().equals("professor")) {

										for (Curso c : listCursoSelecionado) {

											professorCurso.setStatus(true);
											professorCurso.setCurso(c);
											professorCurso.setProfessor(servidor);
											professorCursoService.inserirAlterar(professorCurso);

											professorCurso = new ProfessorCurso();
										}
									}
								}
								controleAddCurso = true;
								criarNovoObjeto();
								carregaLista();
								ExibirMensagem.exibirMensagem(Mensagem.SUCESSO);
								FecharDialog.fecharDialogServidor();

							}
						} else {

							ExibirMensagem.exibirMensagem(Mensagem.ERROPERMISSAO);
						}

					} else {
						if ((validacoesGerirUsuarios.buscarUsuarios(servidor))
								&& (validacoesGerirUsuarios.buscarUsuarioAlterar(servidor))) {
							ExibirMensagem.exibirMensagem(Mensagem.USUARIO);
						} else if ((validacoesGerirUsuarios.buscarSiape(servidor))
								&& (validacoesGerirUsuarios.buscarSiapeAlterar(servidor))) {
							ExibirMensagem.exibirMensagem(Mensagem.SIAPE);
						} else {
							if (servidor.getSenha().isEmpty()) {
								servidor.setSenha("123");
							}

							if (servidor.getChefe() == null) {
								servidor.setChefe(false);
							}
						     servidorService.inserirAlterar(servidor);
						
						     
						     
						     
							for (Tipo t : tipoSalvar) {
								if (t.getDescricao().equalsIgnoreCase("professor")) {

									for (Curso c : listCursoSelecionado) {
										professorCurso.setStatus(true);
										professorCurso.setCurso(c);
										professorCurso.setProfessor(servidor);
										if (validarRelacionamentoCursoProfessor(professorCurso)) {
											professorCursoService.inserirAlterar(professorCurso);
										}
										professorCurso = new ProfessorCurso();
									}
								}
							}
							controleAddCurso = true;
							criarNovoObjeto();
							carregaLista();
							ExibirMensagem.exibirMensagem(Mensagem.SUCESSO);
							FecharDialog.fecharDialogServidor();
						}
					}
				}
			} catch (Exception e) {
				System.out.println("erro" + e);
			}
		}
	}

	public boolean verificarEmailCadastrado() {

		Pessoa pessoa = new Pessoa();

		pessoa = (Pessoa) daoPessoa.listar(Pessoa.class, " usuario = " + servidor.getUsuario());

		if (pessoa.getUsuario().equals(servidor.getUsuario())) {
			return true;
		} else {
			return false;
		}
	}

	public boolean validarRelacionamentoPermissao(Permissao permissao) {
		List<Permissao> listPermissaoServidor = new ArrayList<>();

		try {

			listPermissaoServidor = daoPermissao.listar(Permissao.class,
					" tipo = " + permissao.getTipo().getId() + " and servidor = " + permissao.getServidor().getId());

		} catch (Exception e) {
			e.printStackTrace();
		}
		if (listPermissaoServidor.isEmpty())
			return true;
		return false;

	}

	public boolean validarRelacionamentoCursoProfessor(ProfessorCurso servidorCurso) {
		List<ProfessorCurso> listServidorCurso = new ArrayList<>();

		try {

			listServidorCurso = daoProfessorCurso.listar(ProfessorCurso.class, " professor = " + servidorCurso.getProfessor().getId()
					+ " and curso = " + servidorCurso.getCurso().getId());

		} catch (Exception e) {
			e.printStackTrace();
		}
		if (listServidorCurso.isEmpty())
			return true;
		return false;

	}

	public void inativarServidor(Servidor servidor) {

		try {
			servidor.setStatus(false);
			servidorService.inserirAlterar(servidor);
			carregaLista();
			ExibirMensagem.exibirMensagem(Mensagem.SUCESSO);
		} catch (Exception e) {
			ExibirMensagem.exibirMensagem(Mensagem.ERRO);
		}

	}

	public void carregaLista() {
		listServidor = daoServidor.listaComStatus(Servidor.class);
	}

	public void carregaPermissao() {

		listTipoSelecionado = daoTipo.listaComStatus(Tipo.class);
		listCursoSalvar = daoCurso.listaComStatus(Curso.class);
	}

	public void criarNovoObjeto() {

		controleAddTipo = true;
		servidor = new Servidor();
		permissao = new Permissao();
		listCursoSelecionado = new ArrayList<>();
		tipoSalvar = new ArrayList<>();

		controleSenha = true;
		controle = true;
		controleAddCurso = true;

	}

	public void reiniciarObjeto() {

		servidor = new Servidor();

		FecharDialog.fecharDialogServidor();

	}

	public List<Tipo> getListTipoSelecionado() {
		return listTipoSelecionado;
	}

	public void setListTipoSelecionado(List<Tipo> listTipoSelecionado) {
		this.listTipoSelecionado = listTipoSelecionado;
	}

	public boolean isControle() {
		return controle;
	}

	public void setControle(boolean controle) {
		this.controle = controle;
	}

	public Servidor getServidor() {
		return servidor;
	}

	public void setServidor(Servidor servidor) {
		this.servidor = servidor;
	}

	public Permissao getPermissao() {
		return permissao;
	}

	public void setPermissao(Permissao permissao) {
		this.permissao = permissao;
	}

	public List<Curso> getListCursoSelecionado() {
		return listCursoSelecionado;
	}

	public void setListCursoSelecionado(List<Curso> listCursoSelecionado) {
		this.listCursoSelecionado = listCursoSelecionado;
	}

	public List<Curso> getListCursoSalvar() {
		listCursoSalvar = daoCurso.listaComStatus(Curso.class);
		return listCursoSalvar;
	}

	public void setListCursoSalvar(List<Curso> listCursoSalvar) {
		this.listCursoSalvar = listCursoSalvar;
	}

	public ProfessorCurso getProfessorCurso() {
		return professorCurso;
	}

	public void setProfessorCurso(ProfessorCurso professorCurso) {
		this.professorCurso = professorCurso;
	}

	public List<Servidor> getListServidor() {
		listServidor = daoServidor.listaComStatus(Servidor.class);

		return listServidor;
	}

	public boolean verificaCurso(Servidor servidor) {

		List<Permissao> listPermissao = new ArrayList<>();
		listPermissao = daoPermissao.listar(Permissao.class,
				" servidor = " + servidor.getId() + " and tipo.descricao = 'professor'");
		if (listPermissao.size() > 0) {

			return true;
		} else {

			return false;
		}
	}

	public boolean isControleDesativar() {
		return controleDesativar;
	}

	public List<ProfessorCurso> getProfessorCursoSelecionado() {
		return professorCursoSelecionado;
	}

	public void setProfessorCursoSelecionado(List<ProfessorCurso> professorCursoSelecionado) {
		this.professorCursoSelecionado = professorCursoSelecionado;
	}

	public UsuarioSessaoMB getUsuarioSessao() {
		return usuarioSessao;
	}

	public void setUsuarioSessao(UsuarioSessaoMB usuarioSessao) {
		this.usuarioSessao = usuarioSessao;
	}

	public void setControleDesativar(boolean controleDesativar) {
		this.controleDesativar = controleDesativar;
	}

	public void setListServidor(List<Servidor> listServidor) {
		this.listServidor = listServidor;
	}

	public boolean isControleRadio() {
		return controleRadio;
	}

	public void setControleRadio(boolean controleRadio) {
		this.controleRadio = controleRadio;
	}

	public Curso getCursoAdicionado() {
		return cursoAdicionado;
	}

	public void setCursoAdicionado(Curso cursoAdicionado) {
		this.cursoAdicionado = cursoAdicionado;
	}

	public List<ProfessorCurso> getProfessorCursoRemove() {
		return professorCursoRemove;
	}

	public void setProfessorCursoRemove(List<ProfessorCurso> professorCursoRemove) {
		this.professorCursoRemove = professorCursoRemove;
	}

	public List<Permissao> getPermissaoRemove() {
		return permissaoRemove;
	}

	public void setPermissaoRemove(List<Permissao> permissaoRemove) {
		this.permissaoRemove = permissaoRemove;
	}

	public Tipo getPermissaoAdicionada() {
		return permissaoAdicionada;
	}

	public void setPermissaoAdicionada(Tipo permissaoAdicionada) {
		this.permissaoAdicionada = permissaoAdicionada;
	}

	public List<Permissao> getPermissaoRemoveSelecionada() {
		return permissaoRemoveSelecionada;
	}

	public List<Curso> getListCursoCompletar() {
		return listCursoCompletar;
	}

	public void setListCursoCompletar(List<Curso> listCursoCompletar) {
		this.listCursoCompletar = listCursoCompletar;
	}

	public void setPermissaoRemoveSelecionada(List<Permissao> permissaoRemoveSelecionada) {
		this.permissaoRemoveSelecionada = permissaoRemoveSelecionada;
	}

	public List<Tipo> getTipoSalvar() {
		return tipoSalvar;
	}

	public void setTipoSalvar(List<Tipo> tipoSalvar) {
		this.tipoSalvar = tipoSalvar;
	}

	public int getFlag() {
		return flag;
	}

	public void setFlag(int flag) {
		this.flag = flag;
	}

	public Tipo getPermissaoParaRemove() {
		return permissaoParaRemove;
	}

	public Permissao getRemoverPermissao() {
		return removerPermissao;
	}

	public void setRemoverPermissao(Permissao removerPermissao) {
		this.removerPermissao = removerPermissao;
	}

	public void setPermissaoParaRemove(Tipo permissaoParaRemove) {
		this.permissaoParaRemove = permissaoParaRemove;
	}

	public boolean isControleSenha() {
		return controleSenha;
	}

	public void setControleSenha(boolean controleSenha) {
		this.controleSenha = controleSenha;
	}

	public boolean isControleAddTipo() {
		return controleAddTipo;
	}

	public void setControleAddTipo(boolean controleAddTipo) {
		this.controleAddTipo = controleAddTipo;
	}

	public boolean isControleAddCurso() {
		return controleAddCurso;
	}

	public void setControleAddCurso(boolean controleAddCurso) {
		this.controleAddCurso = controleAddCurso;
	}

}
