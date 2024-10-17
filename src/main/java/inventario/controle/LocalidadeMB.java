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
import inventario.service.LocalService;
import inventario.service.LocalidadeService;

@ViewScoped
@Named("localidadeMB")
public class LocalidadeMB implements Serializable {

	private static final long serialVersionUID = 1L;

	private Locais localidade;
	private List<Locais> listLocalidade;

  
	@Inject
	private GenericDAO<Locais> daoLocalidade;

	@Inject
	private LocalidadeService localidadeService;

	@PostConstruct
	public void inicializar() {
		localidade = new Locais();
		listLocalidade = new ArrayList<>();
		buscarLocal();
	}

	public void salvar() {

		try {

			if (localidade.getId() == null) {
				if (verificarCadastro(localidade)) {
					localidade.setStatus(true);
					localidadeService.inserirAlterar(localidade);
					FecharDialog.fechaDialogLocalidade();
					buscarLocal();
					ExibirMensagem.exibirMensagem(Mensagem.SUCESSO);
				} else {
					ExibirMensagem.exibirMensagem(Mensagem.LOCALCADASTRO);
				}

			} else {
				if (verificarCadastro(localidade) && verificarLocalCadastro(localidade)) {
					ExibirMensagem.exibirMensagem(Mensagem.LOCALCADASTRO);
				} else {

					localidadeService.inserirAlterar(localidade);
					
					buscarLocal();
					ExibirMensagem.exibirMensagem(Mensagem.SUCESSO);
					FecharDialog.fechaDialogLocalidade();
				}

			}

		} catch (Exception e) {
			ExibirMensagem.exibirMensagem(Mensagem.ERRO);
		}
	}
	
	
	public void inativar(Locais local) {
		try {
			local.setStatus(false);
			localidadeService.inserirAlterar(local);
			ExibirMensagem.exibirMensagem(Mensagem.SUCESSO);
		} catch (Exception e) {
			ExibirMensagem.exibirMensagem(Mensagem.ERRO);
		}
		buscarLocal();
		//criarNovoObjeto();
	}

	public boolean verificarCadastro(Locais local) {
		List<Locais> LocalBusca = daoLocalidade.listar(Locais.class, " descricao = '" + local.getDescricao() + "'");

		if (LocalBusca.isEmpty())
			return true;

		return false;

	}

	public boolean verificarLocalCadastro(Locais local) {
		List<Locais> LocalBusca = daoLocalidade.listar(Locais.class,
				" descricao = '" + local.getDescricao() + "' and id = " + local.getId());

		if (LocalBusca.isEmpty())
			return false;

		return true;

	}

	public void buscarLocal(Locais local) {
		this.localidade = local;
	}

 
	public void buscarLocal() {
		listLocalidade = daoLocalidade.listaComStatus(Locais.class);
	}

 

	 

	public void criarNovoObjeto() {
		localidade = new Locais();
	}

	public Locais getLocalidade() {
		return localidade;
	}

	public void setLocalidade(Locais localidade) {
		this.localidade = localidade;
	}

	public List<Locais> getListLocalidade() {
		return listLocalidade;
	}

	public void setListLocalidade(List<Locais> listLocalidade) {
		this.listLocalidade = listLocalidade;
	}

 

}
