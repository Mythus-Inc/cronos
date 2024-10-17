package inventario.controle;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.Local;
import javax.enterprise.context.SessionScoped;
import javax.faces.application.Application;
import javax.faces.application.ViewHandler;
import javax.faces.component.UIViewRoot;
import javax.faces.context.FacesContext;
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
@Named("visualizaBensMB")
public class VisualizaBensMB implements Serializable {

	private static final long serialVersionUID = 1L;
   
	private List<Tombamento> listTombamento;  
	
	private int quantidadeTombamentos = 0;
 
 

	@Inject
	private GenericDAO<Tombamento> daoTombamento;

	@Inject
	private EquipamentoService equipamentoService;

	@Inject
	private TombamentoService tombamentoService;

	@PostConstruct
	public void inicializar() {
	 
		listTombamento = new ArrayList<>();  
		buscarEquipamento();
	}
	
	public void buscarEquipamento(){
		listTombamento = daoTombamento.listaComStatus(Tombamento.class);
		quantidadeTombamentos = listTombamento.size();
	}
	

 
	public void buscarTombamento(Equipamento equipamento) {
		listTombamento = daoTombamento.listar(Tombamento.class, " equipamento = " + equipamento.getId());
	}

 

	public void inativarTombamento(Tombamento tombamento) {
		tombamento.setStatus(false);
		tombamentoService.inserirAlterar(tombamento);
		ExibirMensagem.exibirMensagem(Mensagem.SUCESSO);
		buscarTombamento(tombamento.getEquipamento());
	}

	public void inativar(Tombamento tombamento) {
		try {
			tombamento.setStatus(false);
			tombamentoService.inserirAlterar(tombamento);
			ExibirMensagem.exibirMensagem(Mensagem.SUCESSO);
		} catch (Exception e) {
			ExibirMensagem.exibirMensagem(Mensagem.ERRO);
		}
		buscarEquipamento();
		refresh();
	 
	}
	
	public void refresh() {
		FacesContext context = FacesContext.getCurrentInstance();
		Application application = context.getApplication();
		ViewHandler viewHandler = application.getViewHandler();
		UIViewRoot viewRoot = viewHandler.createView(context, context.getViewRoot().getViewId());
		context.setViewRoot(viewRoot);
		context.renderResponse();
	}

 
	 

	 

	public int getQuantidadeTombamentos() {
		return quantidadeTombamentos;
	}

	public void setQuantidadeTombamentos(int quantidadeTombamentos) {
		this.quantidadeTombamentos = quantidadeTombamentos;
	}

	public EstadoConservacao[] getEstadoConservacao() {
		return EstadoConservacao.values();
	}

	 

	public List<Tombamento> getListTombamento() {
		return listTombamento;
	}

	public void setListTombamento(List<Tombamento> listTombamento) {
		this.listTombamento = listTombamento;
	}
	 
}
