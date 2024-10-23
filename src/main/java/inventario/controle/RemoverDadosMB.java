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
import inventario.service.SetorLocalService;

import org.hibernate.loader.custom.Return;

import util.CriptografiaSenha;
import util.ExibirMensagem;
import util.FecharDialog;
import util.Mensagem;
import util.PermiteInativar;
import ac.controle.UsuarioSessaoMB;
import ac.modelo.AtividadeTurma;
import ac.modelo.Certificado;
import ac.modelo.Grupo;
import ac.services.GrupoService;
import base.modelo.Servidor;
import cope.controle.UsuarioLogadoMB;
import dao.GenericDAO;
import inventario.modelo.Equipamento;
import inventario.modelo.EquipamentoInventario;
import inventario.modelo.EstadoConservacao;
import inventario.modelo.Inventario;
import inventario.modelo.Locais;
import inventario.modelo.LocalInventario;
import inventario.modelo.Localidades;
import inventario.modelo.Movimentacoes;
import inventario.modelo.MovimentacoesTombamento;
import inventario.modelo.SetorLocal;
import inventario.modelo.Tombamento;
import inventario.service.EquipamentoInventarioService;
import inventario.service.EquipamentoService;
import inventario.service.LocaisInventarioService;
import inventario.service.LocalService;
import inventario.service.LocalidadeService;
import inventario.service.MovimentacoesService;
import inventario.service.MovimentacoesTombamentoService;
import inventario.service.TombamentoService;
import protocolo.service.SetorService;

@ViewScoped
@Named("removerDadosMB")
public class RemoverDadosMB implements Serializable {

	private static final long serialVersionUID = 1L;

	private List<Tombamento> listTombamento;

	private int quantidadeTombamentos = 0;

	private Servidor servidorLocado;

	private boolean esconderSenhaErrada;

	private String senha;

	@Inject
	private GenericDAO<Tombamento> daoTombamento;

	@Inject
	private GenericDAO<EquipamentoInventario> daoEquipamentoInventario;

	@Inject
	private GenericDAO<Equipamento> daoEquipamento;

	@Inject
	private GenericDAO<LocalInventario> daoLocaisInventario;

	@Inject
	private GenericDAO<MovimentacoesTombamento> daoMovimentacoesTombamento;

	@Inject
	private GenericDAO<Movimentacoes> daoMovimentacoes;

	@Inject
	private GenericDAO<Inventario> daoInventario;

	@Inject
	private GenericDAO<Locais> daoLocais;

	@Inject
	private GenericDAO<Localidades> daoLocalidades;

	@Inject
	private GenericDAO<SetorLocal> daoSetorLocal;

	@Inject
	private EquipamentoService equipamentoService;

	@Inject
	private EquipamentoInventarioService equipamentoInventarioService;

	@Inject
	private UsuarioSessaoMB recuperarServidor;

	@Inject
	private TombamentoService tombamentoService;

	@Inject
	private SetorLocalService SetorLocalService;

	@Inject
	private LocaisInventarioService locaisInventarioService;

	@Inject
	private MovimentacoesTombamentoService movimentacoesTombamentoService;

	@Inject
	private MovimentacoesService movimentacoesService;

	@Inject
	private LocalService localidadeService;

	@Inject
	private LocalidadeService locaisService;

	@PostConstruct
	public void inicializar() {

		servidorLocado = new Servidor();
		esconderSenhaErrada = false;

	}

	public void buscarDados() {

		servidorLocado = (Servidor) recuperarServidor.recuperarServidor();

	}

	public void removerDados() {

		if (CriptografiaSenha.decptrografar(senha, servidorLocado.getSenha())) {
			List<EquipamentoInventario> listEquipamentoInventario = daoEquipamentoInventario
					.listarComOrdenacao(EquipamentoInventario.class, " id");

			if (listEquipamentoInventario.size() > 0) {
				for (EquipamentoInventario t : listEquipamentoInventario) {
					equipamentoInventarioService.remover(t);
				}
			}

			List<LocalInventario> listLocalInventario = daoLocaisInventario.listarComOrdenacao(LocalInventario.class,
					" id");

			if (listLocalInventario.size() > 0) {
				for (LocalInventario t : listLocalInventario) {
					locaisInventarioService.remover(t);
				}
			}

			List<MovimentacoesTombamento> listMovimentacoesTombamento = daoMovimentacoesTombamento
					.listarComOrdenacao(MovimentacoesTombamento.class, " id");

			if (listMovimentacoesTombamento.size() > 0) {
				for (MovimentacoesTombamento t : listMovimentacoesTombamento) {
					movimentacoesTombamentoService.remover(t);
				}
			}

			List<Movimentacoes> listMovimentacoes = daoMovimentacoes.listarComOrdenacao(Movimentacoes.class, " id");

			if (listMovimentacoes.size() > 0) {
				for (Movimentacoes t : listMovimentacoes) {
					movimentacoesService.remover(t);
				}
			}

			List<Tombamento> listTombamento = daoTombamento.listarComOrdenacao(Tombamento.class, " id");

			if (listTombamento.size() > 0) {
				for (Tombamento t : listTombamento) {
					tombamentoService.remover(t);
				}
			}

			List<Equipamento> listEquipamento = daoEquipamento.listarComOrdenacao(Equipamento.class, " id");

			if (listEquipamento.size() > 0) {
				for (Equipamento t : listEquipamento) {
					equipamentoService.remover(t);
				}
			}

			List<Localidades> listLocalidades = daoLocalidades.listarComOrdenacao(Localidades.class, " id");

			if (listLocalidades.size() > 0) {
				for (Localidades t : listLocalidades) {
					localidadeService.remover(t);
				}
			}

			List<SetorLocal> listUnidadeResponsavel = daoSetorLocal.listarComOrdenacao(SetorLocal.class, " id");

			if (listUnidadeResponsavel.size() > 0) {
				for (SetorLocal t : listUnidadeResponsavel) {
					SetorLocalService.remover(t);
				}
			}

			List<Locais> listLocais = daoLocais.listarComOrdenacao(Locais.class, " id");

			if (listLocais.size() > 0) {
				for (Locais t : listLocais) {
					locaisService.remover(t);
				}
			}
			ExibirMensagem.exibirMensagem(Mensagem.SUCESSO);
			FecharDialog.fechaDialogRemove();

		} else {
			esconderSenhaErrada = true;
			senha = "";
		}

	}

	public Servidor getServidorLocado() {
		return servidorLocado;
	}

	public void setServidorLocado(Servidor servidorLocado) {
		this.servidorLocado = servidorLocado;
	}

	public boolean isEsconderSenhaErrada() {
		return esconderSenhaErrada;
	}

	public void setEsconderSenhaErrada(boolean esconderSenhaErrada) {
		this.esconderSenhaErrada = esconderSenhaErrada;
	}

	public String getSenha() {
		return senha;
	}

	public void setSenha(String senha) {
		this.senha = senha;
	}

}
