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
import inventario.modelo.SetorLocal;
import inventario.service.LocalService;
import inventario.service.SetorLocalService;

@ViewScoped
@Named("setorLocalMB")
public class SetorLocalMB implements Serializable {

	private static final long serialVersionUID = 1L;

	private SetorLocal setorLocal;
	private List<SetorLocal> listSetorLocal;

 

	@Inject
	private GenericDAO<SetorLocal> daoSetorLocal;
	
	@Inject
	private GenericDAO<Servidor> daoServidor;

	@Inject
	private SetorLocalService setorLocalService;

	@PostConstruct
	public void inicializar() {
		setorLocal = new SetorLocal();
		listSetorLocal = new ArrayList<>();
		buscarLocal();
	}

	public void salvar() {

		try {

			if (setorLocal.getId() == null) {
				if (verificarCadastro(setorLocal)) {
					setorLocal.setStatus(true);
					setorLocalService.inserirAlterar(setorLocal);
					FecharDialog.fechaDialogSetorLocalidade();
					buscarLocal();
					ExibirMensagem.exibirMensagem(Mensagem.SUCESSO);
				} else {
					ExibirMensagem.exibirMensagem(Mensagem.LOCALSETOR);
				}

			} else {
				if (verificarCadastro(setorLocal) && verificarLocalCadastro(setorLocal)) {
					ExibirMensagem.exibirMensagem(Mensagem.LOCALCADASTRO);
				} else {

					setorLocalService.inserirAlterar(setorLocal);
					FecharDialog.fechaDialogSetorLocalidade();
					buscarLocal();
					ExibirMensagem.exibirMensagem(Mensagem.SUCESSO);
				}

			}

		} catch (Exception e) {
			ExibirMensagem.exibirMensagem(Mensagem.ERRO);
		}
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
	
	
	public void inativar(SetorLocal setorLocal) {
		try {
			setorLocal.setStatus(false);
			setorLocalService.inserirAlterar(setorLocal);
			ExibirMensagem.exibirMensagem(Mensagem.SUCESSO);
		} catch (Exception e) {
			ExibirMensagem.exibirMensagem(Mensagem.ERRO);
		}
		buscarLocal();
		criarNovoObjeto();
	}

	public boolean verificarCadastro(SetorLocal local) {
		List<SetorLocal> LocalBusca = daoSetorLocal.listar(SetorLocal.class, " descricao = '" + local.getDescricao() + "'");

		if (LocalBusca.isEmpty())
			return true;

		return false;

	}

	public boolean verificarLocalCadastro(SetorLocal local) {
		List<SetorLocal> LocalBusca = daoSetorLocal.listar(SetorLocal.class,
				" descricao = '" + local.getDescricao() + "' and id = " + local.getId());

		if (LocalBusca.isEmpty())
			return false;

		return true;

	}

	public void buscarLocal(SetorLocal local) {
		this.setorLocal = local; 
	}
  

	public void buscarLocal() {
		listSetorLocal = daoSetorLocal.listaComStatus(SetorLocal.class);
	} 
	
	public void criarNovoObjeto() {
		setorLocal = new SetorLocal();
	}

	public SetorLocal getSetorLocal() {
		return setorLocal;
	}

	public void setSetorLocal(SetorLocal setorLocal) {
		this.setorLocal = setorLocal;
	}

	public List<SetorLocal> getListSetorLocal() {
		return listSetorLocal;
	}

	public void setListSetorLocal(List<SetorLocal> listSetorLocal) {
		this.listSetorLocal = listSetorLocal;
	}

 
}
