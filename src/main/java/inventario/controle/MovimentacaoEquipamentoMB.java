package inventario.controle;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.enterprise.context.SessionScoped;
import javax.enterprise.inject.New;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.hibernate.loader.custom.Return;

import util.ExibirMensagem;
import util.FecharDialog;
import util.Mensagem;
import util.PermiteInativar;
import ac.controle.UsuarioSessaoMB;
import ac.modelo.AtividadeTurma;
import ac.modelo.Certificado;
import ac.modelo.Grupo;
import ac.services.GrupoService;
import ac.services.PessoaService;
import base.modelo.Servidor;
import dao.GenericDAO;
import inventario.modelo.EquipamentoInventario;
import inventario.modelo.EstadoConservacao;
import inventario.modelo.Inventario;
import inventario.modelo.Localidades;
import inventario.modelo.LocalInventario;
import inventario.modelo.Movimentacoes;
import inventario.modelo.MovimentacoesTombamento;
import inventario.modelo.Tombamento;
import inventario.service.EquipamentoInventarioService;
import inventario.service.LocalService;
import inventario.service.MovimentacoesService;
import inventario.service.MovimentacoesTombamentoService;
import inventario.service.TombamentoService;

@ViewScoped
@Named("movimentacaoEquipamentoMB")
public class MovimentacaoEquipamentoMB implements Serializable {

	private static final long serialVersionUID = 1L;

	private List<Tombamento> listTombamentos;
	private List<Tombamento> listTombamentoMovimenta;
	private String codigo = "";
	private Movimentacoes movimentacoes;
	private String descricao;
	private Localidades localOrigem;
	private Localidades localDestino;

	@Inject
	private UsuarioSessaoMB usuarioSessao;

	@Inject
	private MovimentacoesService movimentacoesService;

	@Inject
	private MovimentacoesTombamentoService movimentacoesTombamentoService;

	@Inject
	private TombamentoService tombamentoService;

	public String getCodigo() {
		return codigo;
	}

	public void setCodigo(String codigo) {
		this.codigo = codigo;
	}

	@Inject
	private GenericDAO<Localidades> daoLocal;

	@Inject
	private GenericDAO<Tombamento> daoTombamento;

	@PostConstruct
	public void inicializar() {
		listTombamentos = new ArrayList<>();
		listTombamentoMovimenta = new ArrayList<>();
		localOrigem = new Localidades();
		localDestino = new Localidades();
	}

	public void buscarTombamentosOrigem() {
		listTombamentos = daoTombamento.listar(Tombamento.class, " local = '" + localOrigem.getId() + "'");

	}

	public void buscarTombamentosPorCodigo() {
		listTombamentos = daoTombamento.listar(Tombamento.class, "  codigo = '" + codigo + "'");
		if (listTombamentos.size() == 0)
			ExibirMensagem.exibirMensagem(Mensagem.TOMBAMENTO);

	}

	public void submeteDados() {
		System.out.println("destino " + localDestino.getDescricao());
	}

	public void criarObjeto() {
		movimentacoes = new Movimentacoes();

	}

	public List<Localidades> completarLocais(String str) {

		List<Localidades> listLocal = new ArrayList<>();
		listLocal = daoLocal.listaComStatus(Localidades.class);
		List<Localidades> listLocalAdd = new ArrayList<>();
		for (Localidades at : listLocal) {
			if (at.getDescricao().toLowerCase().startsWith(str)) {
				listLocalAdd.add(at);
			}
		}
		return listLocalAdd;
	}

	public void movimentar() {

		if (localDestino.getId() == null) {
			ExibirMensagem.exibirMensagem(Mensagem.LOCALDESTINO);
		} else {

			if (localOrigem.getId() == localDestino.getId()) {
				ExibirMensagem.exibirMensagem(Mensagem.DESTINOORIGEM);
			} else {

				movimentacoes.setStatus(true);
				movimentacoes.setLocalOrigem(localOrigem);
				movimentacoes.setLocalDestino(localDestino);
				movimentacoes.setServidor((Servidor) usuarioSessao.recuperarServidor());
				movimentacoesService.inserirAlterar(movimentacoes);

				MovimentacoesTombamento movimentacoesTombamento = new MovimentacoesTombamento();

				for (Tombamento t : listTombamentoMovimenta) {
					movimentacoesTombamento.setStatus(true);
					movimentacoesTombamento.setDataMovimentacao(new Date());
					movimentacoesTombamento.setLocal(localOrigem);
					movimentacoesTombamento.setMovimentacoes(movimentacoes);
					movimentacoesTombamento.setTombamento(t);
					t.setLocal(localDestino);

					tombamentoService.inserirAlterar(t);
					movimentacoesTombamentoService.inserirAlterar(movimentacoesTombamento);

					movimentacoesTombamento = new MovimentacoesTombamento();
				}

				ExibirMensagem.exibirMensagem(Mensagem.SUCESSO);
				FecharDialog.fechaDialogMovimentar();
				localDestino = new Localidades();
				localOrigem = new Localidades();
				listTombamentos = new ArrayList<>();
			}
		}

	}

	public List<Tombamento> getListTombamentos() {
		return listTombamentos;
	}

	public void setListTombamentos(List<Tombamento> listTombamentos) {
		this.listTombamentos = listTombamentos;
	}

	public Movimentacoes getMovimentacoes() {
		return movimentacoes;
	}

	public void setMovimentacoes(Movimentacoes movimentacoes) {
		this.movimentacoes = movimentacoes;
	}

	public List<Tombamento> getListTombamentoMovimenta() {
		return listTombamentoMovimenta;
	}

	public void setListTombamentoMovimenta(List<Tombamento> listTombamentoMovimenta) {
		this.listTombamentoMovimenta = listTombamentoMovimenta;
	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	public Localidades getLocalOrigem() {
		return localOrigem;
	}

	public void setLocalOrigem(Localidades localOrigem) {
		this.localOrigem = localOrigem;
	}

	public Localidades getLocalDestino() {
		return localDestino;
	}

	public void setLocalDestino(Localidades localDestino) {
		this.localDestino = localDestino;
	}

}
