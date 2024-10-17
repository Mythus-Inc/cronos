package cope.controle;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.Serializable;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.primefaces.context.RequestContext;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;
import org.primefaces.model.UploadedFile;

import ac.controle.UsuarioSessaoMB;
import ac.modelo.Pessoa;

import cope.dao.DAOParticipante;
import cope.dao.DAOProjeto;
import cope.modelo.ArquivoProjeto;
import cope.modelo.Participante; 
import cope.modelo.Projeto;
import cope.modelo.TiposProducao;
import cope.modelo.enums.StatusSubmissao;
import cope.modelo.enums.TipoArquivoProjeto;
import cope.service.ArquivoProjetoService;
import cope.service.ParticipanteService;
import cope.service.ProgramaService;
import cope.service.ProjetoService;
import cope.service.TiposProducaoService;
import dao.GenericDAO;
import inventario.modelo.Localidades;
import protocolo.controle.BuscarDadosAlunoMBB;
import util.CaminhoArquivos;
import util.ExibirMensagem;
import util.FecharDialog;
import util.Mensagem;

@ViewScoped
@Named("tiposProducaoMB")
public class TiposProducaoMB implements Serializable {

	private static final long serialVersionUID = 1L;

	private TiposProducao tipoProducao;
	private List<TiposProducao> listaTiposProducao;

	@Inject
	private GenericDAO<TiposProducao> daoTipoProducao;

	@Inject
	private TiposProducaoService tiposProducaoService;

	@PostConstruct
	public void inicializar() {

		criarNovoObjt();
		buscaPrograma();
	}

	public void buscaPrograma() {
		listaTiposProducao = daoTipoProducao.listaComStatus(TiposProducao.class);
	}
 
	public void cadastrar() {

		if (tipoProducao.getId() == null) {

			if (verificarCadastroInserir(tipoProducao)) {
				tipoProducao.setStatus(true);
				tiposProducaoService.inserirAlterar(tipoProducao);
				ExibirMensagem.exibirMensagem(Mensagem.SUCESSO);
				FecharDialog.fecharDialogCadastroTiposProducao();
			} else {
				ExibirMensagem.exibirMensagem(Mensagem.TIPOPRODUCAO);
			}
		} else {
			if (verificarCadastroInserir(tipoProducao) && verificarCadastroAlterar(tipoProducao)) {
				ExibirMensagem.exibirMensagem(Mensagem.TIPOPRODUCAO);
			} else {

				FecharDialog.fecharDialogCadastroTiposProducao();
				tiposProducaoService.inserirAlterar(tipoProducao);
				ExibirMensagem.exibirMensagem(Mensagem.SUCESSO);
			}
		}

		buscaPrograma();
	}

	public void inativar(TiposProducao tipoProducao) {
		try {
			tipoProducao.setStatus(false);
			tiposProducaoService.inserirAlterar(tipoProducao);
			ExibirMensagem.exibirMensagem(Mensagem.SUCESSO);
		} catch (Exception e) {
			ExibirMensagem.exibirMensagem(Mensagem.ERRO);
		}
		buscaPrograma();
		criarNovoObjt();
	}

	public boolean verificarCadastroAlterar(TiposProducao programa) {
		List<TiposProducao> ProgramaBusca = daoTipoProducao.listar(TiposProducao.class,
				" descricao = '" + programa.getDescricao() + "' and id = " + programa.getId());

		if (ProgramaBusca.isEmpty())
			return false;

		return true;

	}

	public boolean verificarCadastroInserir(TiposProducao programa) {
		List<TiposProducao> ProgramaBusca = daoTipoProducao.listar(TiposProducao.class,
				" descricao = '" + programa.getDescricao() + "'");

		if (ProgramaBusca.isEmpty())
			return true;

		return false;

	}

	public void criarNovoObjt() {
		tipoProducao = new TiposProducao();
	}

	public void carregarPrograma(TiposProducao tipoProducao) {
		this.tipoProducao = tipoProducao;
	}

	public TiposProducao getTipoProducao() {
		return tipoProducao;
	}

	public void setTipoProducao(TiposProducao tipoProducao) {
		this.tipoProducao = tipoProducao;
	}

	public List<TiposProducao> getListaTiposProducao() {
		return listaTiposProducao;
	}

	public void setListaTiposProducao(List<TiposProducao> listaTiposProducao) {
		this.listaTiposProducao = listaTiposProducao;
	}
 

}
