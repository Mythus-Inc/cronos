package protocolo.controle;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.enterprise.context.SessionScoped;

import javax.inject.Inject;
import javax.inject.Named;

import util.ExibirMensagem;
import util.FecharDialog;
import util.Mensagem;
import util.PermiteInativar;
import ac.modelo.Grupo;
import ac.modelo.Permissao;
import ac.services.GrupoService;
import base.modelo.Curso;
import base.modelo.Servidor;
import dao.GenericDAO;
import protocolo.modelo.PessoaSetor;
import protocolo.modelo.Setor;
import protocolo.service.ServidorSetorService;
import protocolo.service.SetorService;

@SessionScoped
@Named("setorMB")
public class SetorMB implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Setor setor;
	private PessoaSetor pessoaSetor;
	private PessoaSetor pessoaSetorAdiciona;
	private List<Servidor> listServidor;
	private List<Servidor> listServidorAdiciona;
	private List<Setor> listSetor;
	private List<PessoaSetor> listPessoaSetor;

	@Inject
	private GenericDAO<Servidor> daoServidor;

	@Inject
	private GenericDAO<PessoaSetor> daoPessoaSetor;

	@Inject
	private GenericDAO<Setor> daoSetor;
	
	@Inject
	private GenericDAO<Curso> daoCurso;

	@Inject
	private SetorService setorService;

	@Inject
	private ServidorSetorService servidorSetorService;

	@PostConstruct
	public void inicializar() {
		setor = new Setor();
		pessoaSetor = new PessoaSetor();
		pessoaSetorAdiciona = new PessoaSetor();
		listServidor = new ArrayList<>();
		listSetor = new ArrayList<>();
		listPessoaSetor = new ArrayList<>();
		listServidorAdiciona = new ArrayList<>();
		buscarSetor();
	}

	public void criarNovoObjeto() {
		setor = new Setor();
		pessoaSetor = new PessoaSetor();
		listServidor = new ArrayList<>();
		pessoaSetorAdiciona = new PessoaSetor();
	}

	public void salvar() {

		if (setor.getId() == null) {

			setor.setDataCadastro(new Date());
			setor.setStatus(true);
			setorService.inserirAlterar(setor);

			pessoaSetor.setResponsavel(true);
			pessoaSetor.setVisualizaProtocolo(true);
			pessoaSetor.setDataEntrada(new Date());
			pessoaSetor.setSetor(setor);
			pessoaSetor.setStatus(true);

			servidorSetorService.inserirAlterar(pessoaSetor);

		} else {

			if (validaServidor(pessoaSetor.getServidor(), setor)) {
				setorService.inserirAlterar(setor);
				
				
				PessoaSetor servidorAlterar = new PessoaSetor();
				servidorAlterar = daoPessoaSetor.buscarCondicao(PessoaSetor.class,
						" responsavel is true and status is true and setor = " + setor.getId()); 
				
				servidorAlterar.setStatus(false);
				servidorAlterar.setDataSaida(new Date()); 
				servidorSetorService.inserirAlterar(servidorAlterar);
		 
				
				PessoaSetor servidorIncluir = new PessoaSetor();
				servidorIncluir.setResponsavel(true);
				servidorIncluir.setVisualizaProtocolo(true);
				servidorIncluir.setDataEntrada(new Date());
				servidorIncluir.setSetor(setor);
				servidorIncluir.setStatus(true);
				servidorIncluir.setServidor(pessoaSetor.getServidor());
				
				servidorSetorService.inserirAlterar(servidorIncluir);
				
			} else {
				setorService.inserirAlterar(setor);
			}
		}

		buscarSetor();
		ExibirMensagem.exibirMensagem(Mensagem.SUCESSO);
		FecharDialog.fecharDialogSetor();

	}

	// public boolean velidaResponsavel(){
	// List<PessoaSetor> pessoaSetor = new ArrayList<>();
	// pessoaSetor = daoPessoaSetor.listar(PessoaSetor.class, "");
	//
	//
	//
	// }

	public void adicionaServidor() {

		if (validaServidor(pessoaSetorAdiciona.getServidor(), setor)) {
			pessoaSetorAdiciona.setStatus(true);
			pessoaSetorAdiciona.setResponsavel(false);
			pessoaSetorAdiciona.setVisualizaProtocolo(true);
			pessoaSetorAdiciona.setSetor(setor);
			servidorSetorService.inserirAlterar(pessoaSetorAdiciona);

			buscarServidoresVinculados(setor);
			ExibirMensagem.exibirMensagem(Mensagem.SUCESSO);
			pessoaSetorAdiciona = new PessoaSetor();
			buscarSetor();

		} else {
			ExibirMensagem.exibirMensagem(Mensagem.SETORSERVIDOR);
		}

	}

	public boolean validaServidor(Servidor pessoa, Setor setorAdd) {
		List<PessoaSetor> servidor = new ArrayList<>();
		servidor = daoPessoaSetor.listar(PessoaSetor.class,
				" servidor = " + pessoa.getId() + " and setor = " + setorAdd.getId());
		if (servidor.size() > 0) {
			return false;
		}
		return true;
	}

	public void abrirDetalhes(Setor setorCadastra) {

		this.setor = setorCadastra;
		pessoaSetor = daoPessoaSetor.buscarCondicao(PessoaSetor.class,
				" status = true and  setor = " + setorCadastra.getId() + " and responsavel = true");
		buscarServidores();
		buscarServidoresVinculados(setorCadastra);
		listServidorAdiciona.clear();
		for (PessoaSetor p : daoPessoaSetor.listarCodicaoLivre(PessoaSetor.class,
				" status is true and responsavel is false and setor = " + setorCadastra.getId())) {
			listServidorAdiciona.add(p.getServidor());
		}

		pessoaSetorAdiciona = new PessoaSetor();

	}

	public void buscarServidoresVinculados(Setor setorCadastra) {
		listPessoaSetor = daoPessoaSetor.listarCodicaoLivre(PessoaSetor.class,
				" status is true and responsavel is false and setor = " + setorCadastra.getId());
	}

	public void remover() {
		setor.setStatus(false);
		setorService.inserirAlterar(setor);
		ExibirMensagem.exibirMensagem(Mensagem.SUCESSO);
		FecharDialog.fecharDialogRemove();
		criarNovoObjeto();
		buscarSetor();
	}

	public void removerServidor(PessoaSetor pessoaSetor) {

		pessoaSetor.setStatus(false);
		servidorSetorService.inserirAlterar(pessoaSetor);
		buscarServidoresVinculados(pessoaSetor.getSetor());
		ExibirMensagem.exibirMensagem(Mensagem.SUCESSO);

	}

	public void buscarSetor() {
		listSetor = daoSetor.listaComStatus(Setor.class);
	}

	public void buscarServidores() {
		listServidor = daoServidor.listaComStatus(Servidor.class);
	}

	public List<Servidor> completeServidor(String query) {
		buscarServidores();
		List<Servidor> results = new ArrayList<>();
		for (Servidor e : listServidor) {
			if (e.getNome().toLowerCase().startsWith(query)) {
				results.add(e);
			}
		}

		return results;
	}
	
	public List<Curso> completeCurso(String query) {
		List<Curso> listCurso = new ArrayList<>();
		listCurso = daoCurso.listaComStatus(Curso.class);
		List<Curso> results = new ArrayList<>();
		for (Curso e : listCurso) {
			if (e.getNome().toLowerCase().startsWith(query)) {
				results.add(e);
			}
		}

		return results;
	}

	public PessoaSetor getPessoaSetor() {
		return pessoaSetor;
	}

	public void setPessoaSetor(PessoaSetor pessoaSetor) {
		this.pessoaSetor = pessoaSetor;
	}

	public List<Setor> getListSetor() {
		return listSetor;
	}

	public List<Servidor> getListServidor() {
		return listServidor;
	}

	public void setListServidor(List<Servidor> listServidor) {
		this.listServidor = listServidor;
	}

	public List<Servidor> getListServidorAdiciona() {
		return listServidorAdiciona;
	}

	public void setListServidorAdiciona(List<Servidor> listServidorAdiciona) {
		this.listServidorAdiciona = listServidorAdiciona;
	}

	public List<PessoaSetor> getListPessoaSetor() {
		return listPessoaSetor;
	}

	public void setListPessoaSetor(List<PessoaSetor> listPessoaSetor) {
		this.listPessoaSetor = listPessoaSetor;
	}

	public void setListSetor(List<Setor> listSetor) {
		this.listSetor = listSetor;
	}

	public PessoaSetor getPessoaSetorAdiciona() {
		return pessoaSetorAdiciona;
	}

	public void setPessoaSetorAdiciona(PessoaSetor pessoaSetorAdiciona) {
		this.pessoaSetorAdiciona = pessoaSetorAdiciona;
	}

	public Setor getSetor() {
		return setor;
	}

	public void setSetor(Setor setor) {
		this.setor = setor;
	}

}
