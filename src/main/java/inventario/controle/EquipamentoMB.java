package inventario.controle;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.Local;
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
import inventario.modelo.Equipamento;
import inventario.modelo.EstadoConservacao;
import inventario.modelo.Inventario;
import inventario.modelo.Localidades;
import inventario.modelo.Tombamento;
import inventario.service.EquipamentoService;
import inventario.service.LocalService;
import inventario.service.TombamentoService;

@ViewScoped
@Named("equipamentoMB")
public class EquipamentoMB implements Serializable {

	private static final long serialVersionUID = 1L;

	private Equipamento equipamento;
	private Tombamento tombamento;
	private List<Equipamento> listEquipamento;
	private List<Tombamento> listTombamento;
	private List<Tombamento> listTombamentoAdd;
	private Localidades localSalvar;

	@Inject
	private GenericDAO<Equipamento> daoEquipamento;

	@Inject
	private GenericDAO<Localidades> daoLocais;

	@Inject
	private GenericDAO<Tombamento> daoTombamento;

	@Inject
	private EquipamentoService equipamentoService;

	@Inject
	private TombamentoService tombamentoService;

	@PostConstruct
	public void inicializar() {
		equipamento = new Equipamento();
		listEquipamento = new ArrayList<>();
		listTombamento = new ArrayList<>();
		listTombamentoAdd = new ArrayList<>();
		listTombamentoAdd = new ArrayList<>();
		criarObjTombamento();
		buscarEquipamento();
	}
	


	public void salvar() {

		try {

			if (equipamento.getId() == null) {
				if (verificarCadastroInserir(equipamento)) {
					equipamento.setStatus(true);
					equipamentoService.inserirAlterar(equipamento);
					FecharDialog.fechaDialogEquipamento();
					buscarEquipamento();
					ExibirMensagem.exibirMensagem(Mensagem.SUCESSO);
				} else {
					ExibirMensagem.exibirMensagem(Mensagem.LOCALCADASTRO);
				}

			} else {
				if (verificarCadastroInserir(equipamento) && verificarLocalCadastroAlterar(equipamento)) {
					ExibirMensagem.exibirMensagem(Mensagem.LOCALCADASTRO);
				} else {

					equipamentoService.inserirAlterar(equipamento);
					FecharDialog.fechaDialogEquipamento();
					buscarEquipamento();
					ExibirMensagem.exibirMensagem(Mensagem.SUCESSO);
				}

			}

		} catch (Exception e) {
			ExibirMensagem.exibirMensagem(Mensagem.ERRO);
		}
	}
	
	
	public void remover(Tombamento tombamento){		
			 listTombamentoAdd.remove(tombamento);		
	}

	public void incluirTombamento() {

		for (Tombamento t : listTombamentoAdd) {
			t.setLocal(localSalvar);
			tombamentoService.inserirAlterar(t);
		}
		ExibirMensagem.exibirMensagem(Mensagem.SUCESSO);
		buscarTombamento(equipamento);
		criarObjTombamento();
		FecharDialog.fechaDialogTombamenot();
		listTombamentoAdd = new ArrayList<>();

	}

	public void addTombamento() {

		List<Tombamento> listTombamento = daoTombamento.listar(Tombamento.class,
				" codigo ='" + tombamento.getCodigo() + "'");

		boolean add = true;
		for (Tombamento t : listTombamentoAdd) {
			if (t.getCodigo().equals(tombamento.getCodigo())) {
				add = false;
			}
		}

		if (listTombamento.size() == 0 && add) {

			tombamento.setStatus(true);
			tombamento.setEquipamento(equipamento);
			listTombamentoAdd.add(tombamento);
			ExibirMensagem.exibirMensagem(Mensagem.SUCESSO);
			criarObjTombamento();

		} else {
			criarObjTombamento();
			ExibirMensagem.exibirMensagem(Mensagem.TOMBAMENTOCADASTRADO);
		}

	}

	public void buscarTombamento(Equipamento equipamento) {
		listTombamento = daoTombamento.listar(Tombamento.class, " equipamento = " + equipamento.getId());
	}

	public List<Localidades> completarLocais(String str) {

		List<Localidades> listLocal = new ArrayList<>();
		listLocal = daoLocais.listaComStatus(Localidades.class);
		List<Localidades> listLocalAdd = new ArrayList<>();
		for (Localidades at : listLocal) {
			if (at.getDescricao().toLowerCase().startsWith(str)) {
				listLocalAdd.add(at);
			}
		}
		return listLocalAdd;
	}

	public void criarObjTombamento() {
		tombamento = new Tombamento();
	}

	public void preencheEquipamento(Equipamento equipamento) {
		this.equipamento = equipamento;
		criarObjTombamento();
		listTombamentoAdd = new ArrayList<>();
		buscarTombamento(equipamento);
	}

	public void inativarTombamento(Tombamento tombamento) {
		tombamento.setStatus(false);
		tombamentoService.inserirAlterar(tombamento);
		ExibirMensagem.exibirMensagem(Mensagem.SUCESSO);
		buscarTombamento(tombamento.getEquipamento());
	}

	public void inativar(Equipamento equipamento) {
		try {
			equipamento.setStatus(false);
			equipamentoService.inserirAlterar(equipamento);
			ExibirMensagem.exibirMensagem(Mensagem.SUCESSO);
		} catch (Exception e) {
			ExibirMensagem.exibirMensagem(Mensagem.ERRO);
		}
		buscarEquipamento();
		criarNovoObj();
	}

	public boolean verificarCadastroInserir(Equipamento equipamento) {
		List<Equipamento> equipamentoBusca = daoEquipamento.listar(Equipamento.class,
				" descricao = '" + equipamento.getDescricao() + "'");

		if (equipamentoBusca.isEmpty())
			return true;

		return false;

	}

	public boolean verificarLocalCadastroAlterar(Equipamento equipamento) {
		List<Equipamento> equipamentoBusca = daoEquipamento.listar(Equipamento.class,
				" descricao = '" + equipamento.getDescricao() + "' and id = " + equipamento.getId());

		if (equipamentoBusca.isEmpty())
			return false;

		return true;

	}

	public void criarNovoObj() {
		equipamento = new Equipamento();
	}

	public void preencherEquipamento(Equipamento equipamento) {
		this.equipamento = equipamento;
	}

	public EstadoConservacao[] getEstadoConservacao() {
		return EstadoConservacao.values();
	}

	public void buscarEquipamento() {
		listEquipamento = daoEquipamento.listaComStatus(Equipamento.class);
	}

	public Equipamento getEquipamento() {
		return equipamento;
	}

	public void setEquipamento(Equipamento equipamento) {
		this.equipamento = equipamento;
	}

	public List<Equipamento> getListEquipamento() {
		return listEquipamento;
	}

	public void setListEquipamento(List<Equipamento> listEquipamento) {
		this.listEquipamento = listEquipamento;
	}

	public Tombamento getTombamento() {
		return tombamento;
	}

	public void setTombamento(Tombamento tombamento) {
		this.tombamento = tombamento;
	}

	public List<Tombamento> getListTombamento() {
		return listTombamento;
	}

	public void setListTombamento(List<Tombamento> listTombamento) {
		this.listTombamento = listTombamento;
	}
	
	

	public Localidades getLocalSalvar() {
		return localSalvar;
	}

	public void setLocalSalvar(Localidades localSalvar) {
		this.localSalvar = localSalvar;
	}

	public List<Tombamento> getListTombamentoAdd() {
		return listTombamentoAdd;
	}

	public void setListTombamentoAdd(List<Tombamento> listTombamentoAdd) {
		this.listTombamentoAdd = listTombamentoAdd;
	}

}
