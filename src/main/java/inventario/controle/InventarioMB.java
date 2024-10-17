package inventario.controle;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.enterprise.context.SessionScoped;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.hibernate.loader.custom.Return;

import util.ExibirMensagem;
import util.FecharDialog;
import util.Mensagem;
import util.PermiteInativar;
import ac.modelo.AtividadeTurma;
import ac.modelo.Certificado;
import ac.modelo.Grupo;
import ac.services.GrupoService;
import base.modelo.Servidor;
import dao.GenericDAO;
import inventario.modelo.EquipamentoInventario;
import inventario.modelo.Inventario;
import inventario.modelo.Localidades;
import inventario.modelo.LocalInventario;
import inventario.modelo.Tombamento;
import inventario.service.EquipamentoInventarioService;
import inventario.service.InventarioService;
import inventario.service.LocaisInventarioService;
import inventario.service.LocalService;

@ViewScoped
@Named("inventarioMB")
public class InventarioMB implements Serializable {

	private static final long serialVersionUID = 1L;

	private Inventario inventario;
	private LocalInventario localInventario;
	private LocalInventario localInventarioAlteraResponsavel;
	private List<Localidades> listaLocais;
	private List<Tombamento> listTombamentosNaoConferidos;
	private List<Localidades> listaLocaisSelecionados;
	private List<Inventario> listInventario;
	private List<EquipamentoInventario> listEquipamentoInventario;
	private List<LocalInventario> listLocalInventario;
	private Servidor servidorConferir;
	private List<Servidor> listaServidor = new ArrayList<>();

	@Inject
	private GenericDAO<Servidor> daoServidor;

	@Inject
	private GenericDAO<Localidades> daoLocal;

	@Inject
	private GenericDAO<EquipamentoInventario> daoEquipamentoInventario;

	@Inject
	private GenericDAO<LocalInventario> daoLocalInventario;

	@Inject
	private GenericDAO<Tombamento> daoTombamento;

	@Inject
	private GenericDAO<Inventario> daoInventario;

	@Inject
	private InventarioService inventarioService;

	@Inject
	private LocaisInventarioService locaisInventarioService;
	
	@Inject
	private EquipamentoInventarioService equipamentoInventarioService;

	@PostConstruct
	public void inicializar() {
		inventario = new Inventario();
		localInventario = new LocalInventario();
		localInventarioAlteraResponsavel = new LocalInventario();
		listInventario = new ArrayList<>();
		listaLocais = new ArrayList<>();
		listaLocaisSelecionados = new ArrayList<>();
		listLocalInventario = new ArrayList<>();
		listEquipamentoInventario = new ArrayList<>();
		listTombamentosNaoConferidos = new ArrayList<>();
		buscarInventario();

	}

	public void salvarLocalEInventario() {

		// come�ar daqui, proximo passo � inserir inventario e depois os locais.
		try {

			if (inventario.getId() == null) {
				if (verificarCadastroInserir(inventario)) {
					inventario.setStatus(true);
					inventarioService.inserirAlterar(inventario);

					localInventario = new LocalInventario();
					for (Localidades l : listaLocaisSelecionados) {
						localInventario.setSituacao("INCOMPLETO");
						localInventario.setServidorConferencia(servidorConferir);
						localInventario.setStatus(true);
						localInventario.setLocal(l);
						localInventario.setInventario(inventario);
						locaisInventarioService.inserirAlterar(localInventario);
						localInventario = new LocalInventario();
					}

					FecharDialog.fechaDialogInventario();
					FecharDialog.fechaDialogLocaisInventario();
					buscarInventario();
					ExibirMensagem.exibirMensagem(Mensagem.SUCESSO);
				} else {
					ExibirMensagem.exibirMensagem(Mensagem.INVENTARIOCADASTRO);
				}

			} else {
				if (verificarCadastroInserir(inventario) && verificarLocalCadastroAlterar(inventario)) {
					ExibirMensagem.exibirMensagem(Mensagem.INVENTARIOCADASTRO);
				} else {

					localInventario = new LocalInventario();
					
					System.out.println("tamanho  "+listaLocaisSelecionados.size());
					for (Localidades l : listaLocaisSelecionados) {
					System.out.println("ss "+l.getDescricao());
						if (!verificaLocalInventario(l)) {
							System.out.println("no inserirrr ");
							localInventario.setSituacao("INCOMPLETO");
							localInventario.setServidorConferencia(servidorConferir);
							localInventario.setStatus(true);
							localInventario.setLocal(l);
							localInventario.setInventario(inventario);
							locaisInventarioService.inserirAlterar(localInventario);
							localInventario = new LocalInventario();
						}
					}

					inventarioService.inserirAlterar(inventario);
					buscarLocaisInventario(inventario);
					FecharDialog.fechaDialogLocaisInventario();
					buscarInventario();
					ExibirMensagem.exibirMensagem(Mensagem.SUCESSO);
				}

			}

		} catch (Exception e) {
			ExibirMensagem.exibirMensagem(Mensagem.ERRO);
		}
	}
	
	
	public void atualizar() {

		// come�ar daqui, proximo passo � inserir inventario e depois os locais.
		try {

			 
				if (verificarCadastroInserir(inventario) && verificarLocalCadastroAlterar(inventario)) {
					ExibirMensagem.exibirMensagem(Mensagem.INVENTARIOCADASTRO);
				} else {
 
					inventarioService.inserirAlterar(inventario);
					buscarLocaisInventario(inventario); 
					FecharDialog.fechaDialogInventario();
					 
					ExibirMensagem.exibirMensagem(Mensagem.SUCESSO);
				}
 
		} catch (Exception e) {
			ExibirMensagem.exibirMensagem(Mensagem.ERRO);
		}
	}
	
	

	public void visualizaEquipamento(LocalInventario local) {
		listEquipamentoInventario = daoEquipamentoInventario.listar(EquipamentoInventario.class,
				" localInventario = '" + local.getId() + "'");
	}

	public void visualizaNaoEquipamento(LocalInventario local) {
		List<Tombamento> lisTodosTombamento = new ArrayList<>();
		lisTodosTombamento = daoTombamento.listar(Tombamento.class, " local = '" + local.getLocal().getId() + "'");
// falta verificar se está trazendo certo
		listTombamentosNaoConferidos.clear();
		for (Tombamento t : lisTodosTombamento) {
			if (verificaNaListaEquipamentoConferido(local, t)) {
				listTombamentosNaoConferidos.add(t);
			}
		}

	}
	
	 

	public boolean verificaNaListaEquipamentoConferido(LocalInventario local, Tombamento tombamneto) {

		List<EquipamentoInventario> lisEquipamentoConferido = new ArrayList<>();
		lisEquipamentoConferido = daoEquipamentoInventario.listar(EquipamentoInventario.class,
				" localInventario = '" + local.getId() + "' and tombamento = '" + tombamneto.getId() + "'");
		if (lisEquipamentoConferido.size() > 0) {
			return false;
		}

		return true;
	}

	public void abrirLocalInventario() {
		servidorConferir = new Servidor();
		listaLocaisSelecionados.clear();
		FecharDialog.abrirDialogLocalInventario();
	}

	public boolean verificaLocalInventario(Localidades local) {

		List<LocalInventario> listLocaisInventario = new ArrayList<>();
		listLocaisInventario = daoLocalInventario.listar(LocalInventario.class,
				" Inventario = '" + inventario.getId() + "'");

		for (LocalInventario l : listLocaisInventario) {
			if (l.getLocal().getId() == local.getId()) {
				return true;
			}
		}
		return false;
	}

	public void inativar(Inventario inventario) {
		try {
			inventario.setStatus(false);
			inventarioService.inserirAlterar(inventario);
			ExibirMensagem.exibirMensagem(Mensagem.SUCESSO);
		} catch (Exception e) {
			ExibirMensagem.exibirMensagem(Mensagem.ERRO);
		}
		buscarInventario();
		criarNovoObjeto();
	}
	
	public void inativarLocalInventario(LocalInventario local) {
		try {
			local.setStatus(false);
			locaisInventarioService.inserirAlterar(local);
			ExibirMensagem.exibirMensagem(Mensagem.SUCESSO);
		} catch (Exception e) {
			ExibirMensagem.exibirMensagem(Mensagem.ERRO);
		}
		buscarLocaisInventario(local.getInventario());
		
	}
	
	public void limparLocalInventario(LocalInventario local) {
		try {
			
			List<EquipamentoInventario> listEquipamentoLimpa = new ArrayList<>();
			listEquipamentoLimpa = daoEquipamentoInventario.listarCodicaoLivre(EquipamentoInventario.class, " localInventario = '"+local.getId()+"'");
			
			for(EquipamentoInventario e : listEquipamentoLimpa){
				 equipamentoInventarioService.remover(e);
			} 
			ExibirMensagem.exibirMensagem(Mensagem.SUCESSO);
		} catch (Exception e) {
			ExibirMensagem.exibirMensagem(Mensagem.ERRO);
		}
		buscarLocaisInventario(local.getInventario());
		
	}

	public boolean verificarCadastroInserir(Inventario inventario) {
		List<Inventario> lisInventario = daoInventario.listar(Inventario.class,
				" descricao = '" + inventario.getDescricao() + "'");

		if (lisInventario.isEmpty())
			return true;

		return false;

	}

	public boolean verificarLocalCadastroAlterar(Inventario inventario) {
		List<Inventario> listInventario = daoInventario.listar(Inventario.class,
				" descricao = '" + inventario.getDescricao() + "' and id = " + inventario.getId());

		if (listInventario.isEmpty())
			return false;

		return true;

	}

	public void preencherInventario(Inventario inventario) {
		this.inventario = inventario;
		buscarLocaisInventario(inventario);
		 
	}
	
	 

	public void buscarLocaisInventario(Inventario inventario) {
		listLocalInventario = daoLocalInventario.listar(LocalInventario.class,
				" Inventario = '" + inventario.getId() + "'");
	}

	public void localInventarioAlterarResponsavel() {
		locaisInventarioService.inserirAlterar(localInventarioAlteraResponsavel);
		buscarLocaisInventario(localInventarioAlteraResponsavel.getInventario());

		FecharDialog.fechaDialogResponsavelConferencia();
	}

	public void buscarInventario() {
		listInventario = daoInventario.listaComStatus(Inventario.class);

	}

	public List<Servidor> completarServidor(String str) {

		List<Servidor> listServidores = new ArrayList<>();
		listServidores = daoServidor.listaComStatus(Servidor.class);
		List<Servidor> listServidoresAdd = new ArrayList<>();
		for (Servidor at : listServidores) {
			if (at.getNome().toLowerCase().startsWith(str)) {
				listServidoresAdd.add(at);
			}
		}
		return listServidoresAdd;
	}

	public void criarLocais() {
		listaLocaisSelecionados = new ArrayList<>();
	}

	public void criarNovoObjeto() {
		inventario = new Inventario();
		listLocalInventario.clear();
	}

	public Inventario getInventario() {
		return inventario;
	}

	public void setInventario(Inventario inventario) {
		this.inventario = inventario;
	}

	public List<EquipamentoInventario> getListEquipamentoInventario() {
		return listEquipamentoInventario;
	}

	public void setListEquipamentoInventario(List<EquipamentoInventario> listEquipamentoInventario) {
		this.listEquipamentoInventario = listEquipamentoInventario;
	}

	public List<Inventario> getListInventario() {
		return listInventario;
	}

	public void setListInventario(List<Inventario> listInventario) {
		this.listInventario = listInventario;
	}

	public List<Localidades> getListaLocais() {
		listaLocais = daoLocal.listaComStatus(Localidades.class);
		// listaLocaisSelecionados = daoLocal.listaComStatus(Local.class);
		return listaLocais;
	}

	public void setListaLocais(List<Localidades> listaLocais) {
		this.listaLocais = listaLocais;
	}

	public List<Localidades> getListaLocaisSelecionados() {
		return listaLocaisSelecionados;
	}

	public void setListaLocaisSelecionados(List<Localidades> listaLocaisSelecionados) {
		this.listaLocaisSelecionados = listaLocaisSelecionados;
	}

	public List<LocalInventario> getListLocalInventario() {
		return listLocalInventario;
	}

	public void setListLocalInventario(List<LocalInventario> listLocalInventario) {
		this.listLocalInventario = listLocalInventario;
	}

	public LocalInventario getLocalInventarioAlteraResponsavel() {
		return localInventarioAlteraResponsavel;
	}

	public void setLocalInventarioAlteraResponsavel(LocalInventario localInventarioAlteraResponsavel) {
		this.localInventarioAlteraResponsavel = localInventarioAlteraResponsavel;
	}

	public List<Tombamento> getListTombamentosNaoConferidos() {
		return listTombamentosNaoConferidos;
	}

	public void setListTombamentosNaoConferidos(List<Tombamento> listTombamentosNaoConferidos) {
		this.listTombamentosNaoConferidos = listTombamentosNaoConferidos;
	}

	public Servidor getServidorConferir() {
		return servidorConferir;
	}

	public void setServidorConferir(Servidor servidorConferir) {
		this.servidorConferir = servidorConferir;
	}

}
