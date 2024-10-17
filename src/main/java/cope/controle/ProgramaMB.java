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
import cope.modelo.Programas;
import cope.modelo.Projeto;
import cope.modelo.enums.StatusSubmissao;
import cope.modelo.enums.TipoArquivoProjeto;
import cope.service.ArquivoProjetoService;
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
@Named("programaMB")
public class ProgramaMB implements Serializable {

	private static final long serialVersionUID = 1L;

	private Programas programa;
	private List<Programas> listaPrograma;

	@Inject
	private GenericDAO<Programas> daoPrograma;

	@Inject
	private ProgramaService programaService;

	@PostConstruct
	public void inicializar() {

		criarNovoObjt();
		buscaPrograma();
	}

	public void buscaPrograma() {
		listaPrograma = daoPrograma.listaComStatus(Programas.class);
	}

	public void cadastrar() {

		if (programa.getId() == null) {

			if (verificarCadastroInserir(programa)) {
				programa.setStatus(true);
				programaService.inserirAlterar(programa);
				ExibirMensagem.exibirMensagem(Mensagem.SUCESSO);
				FecharDialog.fecharDialogCadastroPrograma();
			} else {
				ExibirMensagem.exibirMensagem(Mensagem.PROGRAMA);
			}
		} else {
			if (verificarCadastroInserir(programa) && verificarCadastroAlterar(programa)) {
				ExibirMensagem.exibirMensagem(Mensagem.PROGRAMA);
			} else {

				FecharDialog.fecharDialogCadastroPrograma();
				programaService.inserirAlterar(programa);
				ExibirMensagem.exibirMensagem(Mensagem.SUCESSO);
			}
		}

		buscaPrograma();
	}

	public void inativar(Programas programa) {
		try {
			programa.setStatus(false);
			programaService.inserirAlterar(programa);
			ExibirMensagem.exibirMensagem(Mensagem.SUCESSO);
		} catch (Exception e) {
			ExibirMensagem.exibirMensagem(Mensagem.ERRO);
		}
		buscaPrograma();
		criarNovoObjt();
	}

	public boolean verificarCadastroAlterar(Programas programa) {
		List<Programas> ProgramaBusca = daoPrograma.listar(Programas.class,
				" descricao = '" + programa.getDescricao() + "' and id = " + programa.getId());

		if (ProgramaBusca.isEmpty())
			return false;

		return true;

	}

	public boolean verificarCadastroInserir(Programas programa) {
		List<Programas> ProgramaBusca = daoPrograma.listar(Programas.class,
				" descricao = '" + programa.getDescricao() + "'");

		if (ProgramaBusca.isEmpty())
			return true;

		return false;

	}

	public void criarNovoObjt() {
		programa = new Programas();
	}

	public void carregarPrograma(Programas programa) {
		this.programa = programa;
	}

	public Programas getPrograma() {
		return programa;
	}

	public void setPrograma(Programas programa) {
		this.programa = programa;
	}

	public List<Programas> getListaPrograma() {
		return listaPrograma;
	}

	public void setListaPrograma(List<Programas> listaPrograma) {
		this.listaPrograma = listaPrograma;
	}

}
