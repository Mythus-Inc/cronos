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
import cope.modelo.Criterios;
import cope.modelo.Participante; 
import cope.modelo.Projeto;
import cope.modelo.enums.StatusSubmissao;
import cope.modelo.enums.TipoArquivoProjeto;
import cope.service.ArquivoProjetoService;
import cope.service.CriterioService;
import cope.service.ParticipanteService;
import cope.service.ProgramaService;
import cope.service.ProjetoService;
import dao.GenericDAO;
import inventario.modelo.Localidades;
import protocolo.controle.BuscarDadosAlunoMBB;
import util.CaminhoArquivos;
import util.ExibirMensagem;
import util.FecharDialog;
import util.Mensagem;

@ViewScoped
@Named("criterioMB")
public class CriterioMB implements Serializable {

	private static final long serialVersionUID = 1L;

	private Criterios criterios;
	private List<Criterios> listaCriterios;

	@Inject
	private GenericDAO<Criterios> daoCriterios;

	@Inject
	private CriterioService criteriosService;

	@PostConstruct
	public void inicializar() {

		criarNovoObjt();
		buscaCriterios();
	}

	public void buscaCriterios() {
		listaCriterios = daoCriterios.listaComStatus(Criterios.class);
	}

	public void cadastrar() {

		if (criterios.getId() == null) {

			if (verificarCadastroInserir(criterios)) {
				criterios.setStatus(true);
				criteriosService.inserirAlterar(criterios);
				ExibirMensagem.exibirMensagem(Mensagem.SUCESSO); 
				FecharDialog.fecharDialogCadastroCriterio();
			} else {
				ExibirMensagem.exibirMensagem(Mensagem.LOCALCADASTRO);
			}
		} else {
			if (verificarCadastroInserir(criterios) && verificarCadastroAlterar(criterios)) {
				ExibirMensagem.exibirMensagem(Mensagem.LOCALCADASTRO);
			} else {

				FecharDialog.fecharDialogCadastroCriterio();
				criteriosService.inserirAlterar(criterios);
				ExibirMensagem.exibirMensagem(Mensagem.SUCESSO);
			}
		}

		buscaCriterios();
	}

	public void inativar(Criterios criterios) {
		try {
			criterios.setStatus(false);
			criteriosService.inserirAlterar(criterios);
			ExibirMensagem.exibirMensagem(Mensagem.SUCESSO);
		} catch (Exception e) {
			ExibirMensagem.exibirMensagem(Mensagem.ERRO);
		}
		buscaCriterios();
		criarNovoObjt();
	}

	public boolean verificarCadastroAlterar(Criterios criterios) {
		List<Criterios> criteriosBusca = daoCriterios.listar(Criterios.class,
				" criterio = '" + criterios.getCriterio() + "' and id = " + criterios.getId());

		if (criteriosBusca.isEmpty())
			return false;

		return true;

	}

	public boolean verificarCadastroInserir(Criterios criterios) {
		List<Criterios> criteriosBusca = daoCriterios.listar(Criterios.class,
				" criterio = '" + criterios.getCriterio() + "'");

		if (criteriosBusca.isEmpty())
			return true;

		return false;

	}

	public void criarNovoObjt() {
		criterios = new Criterios();
	}

	public void carregarPrograma(Criterios criterios) {
		this.criterios = criterios;
	}

	public Criterios getCriterios() {
		return criterios;
	}

	public void setCriterios(Criterios criterios) {
		this.criterios = criterios;
	}

	public List<Criterios> getListaCriterios() {
		return listaCriterios;
	}

	public void setListaCriterios(List<Criterios> listaCriterios) {
		this.listaCriterios = listaCriterios;
	}

	 
}
