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
import inventario.modelo.Localidades;
import inventario.modelo.Locais;
import inventario.modelo.SetorLocal;
import inventario.modelo.Tombamento;
import inventario.service.LocalService;
import protocolo.modelo.Setor;

@ViewScoped
@Named("localMB")
public class LocalMB implements Serializable {

	private static final long serialVersionUID = 1L;

	private Localidades local;
	private List<Localidades> listLocal;

	
	
	@Inject
	private GenericDAO<Servidor> daoServidor;
	
	@Inject
	private GenericDAO<Tombamento> daoTombamento;
	
	@Inject
	private GenericDAO<Locais> daoLocalidade;
	
	@Inject
	private GenericDAO<SetorLocal> daoSetorLocal;

	@Inject
	private GenericDAO<Localidades> daoLocal;

	@Inject
	private LocalService localService;

	@PostConstruct
	public void inicializar() {
		local = new Localidades();
		listLocal = new ArrayList<>();
		buscarLocal();
	}

	public void salvar() {

		try {

			if (local.getId() == null) {
				if (verificarCadastro(local)) {
					local.setStatus(true);
					localService.inserirAlterar(local);
					FecharDialog.fechaDialogLocal();
					buscarLocal();
					ExibirMensagem.exibirMensagem(Mensagem.SUCESSO);
				} else {
					ExibirMensagem.exibirMensagem(Mensagem.LOCALCADASTRO);
				}

			} else {
				if (verificarCadastro(local) && verificarLocalCadastro(local)) {
					ExibirMensagem.exibirMensagem(Mensagem.LOCALCADASTRO);
				} else {

					localService.inserirAlterar(local);
					FecharDialog.fechaDialogLocal();
					buscarLocal();
					ExibirMensagem.exibirMensagem(Mensagem.SUCESSO);
				}

			}

		} catch (Exception e) {
			ExibirMensagem.exibirMensagem(Mensagem.ERRO);
		}
	}
	
	
	public void inativar(Localidades local) {
		try {
			local.setStatus(false);
			localService.inserirAlterar(local);
			ExibirMensagem.exibirMensagem(Mensagem.SUCESSO);
		} catch (Exception e) {
			ExibirMensagem.exibirMensagem(Mensagem.ERRO);
		}
		buscarLocal();
		criarNovoObjeto();
	}
	
	public int buscarTombamento(Localidades local) {
		 List<Tombamento> listaTombamento = new ArrayList<>();
		listaTombamento = daoTombamento.listar(Tombamento.class, " local = '"+local.getId()+"'");
		
		return listaTombamento.size();
	}

	public boolean verificarCadastro(Localidades local) {
		List<Localidades> LocalBusca = daoLocal.listar(Localidades.class, " descricao = '" + local.getDescricao() + "'");

		if (LocalBusca.isEmpty())
			return true;

		return false;

	}

	public boolean verificarLocalCadastro(Localidades local) {
		List<Localidades> LocalBusca = daoLocal.listar(Localidades.class,
				" descricao = '" + local.getDescricao() + "' and id = " + local.getId());

		if (LocalBusca.isEmpty())
			return false;

		return true;

	}

	public void buscarLocal(Localidades local) {
		this.local = local;
	}

	public List<Localidades> getListLocal() {
		return listLocal;
	}

	public void setListLocal(List<Localidades> listLocal) {
		this.listLocal = listLocal;
	}

	public void buscarLocal() {
		listLocal = daoLocal.listaComStatus(Localidades.class);
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
	
	public List<SetorLocal> completarSetorLocal(String str) { 
		List<SetorLocal> list = new ArrayList<>();
		list = daoSetorLocal.listaComStatus(SetorLocal.class);
		List<SetorLocal> listAdd = new ArrayList<>();
		for (SetorLocal at : list) {
			if (at.getDescricao().toLowerCase().startsWith(str)) {
				listAdd.add(at);
			}
		}
		return listAdd;
	}

	public List<Locais> completarLocalidade(String str) { 
		List<Locais> list  = new ArrayList<>();
		list = daoLocalidade.listaComStatus(Locais.class);
		List<Locais> listAdd = new ArrayList<>();
		for (Locais at : list) {
			if (at.getDescricao().toLowerCase().startsWith(str)) {
				listAdd.add(at);
			}
		}
		return listAdd;
	}

	
	public void criarNovoObjeto() {
		local = new Localidades();
	}

	public Localidades getLocal() {
		return local;
	}

	public void setLocal(Localidades local) {
		this.local = local;
	}

}
