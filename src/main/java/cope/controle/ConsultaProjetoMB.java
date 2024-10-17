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
@Named("consultaProjetoMB")
public class ConsultaProjetoMB implements Serializable {

	private static final long serialVersionUID = 1L;
 
	private List<Projeto> listProjeto;
	private List<ArquivoProjeto> listaarquivoProjetoSelecionado;
	private Projeto projetoSelecionado;

	@Inject
	private GenericDAO<Projeto> daoProjeto;
	
	@Inject
	private GenericDAO<ArquivoProjeto> daoArquivoProjeto;

 

	@PostConstruct
	public void inicializar() {
		projetoSelecionado = new Projeto();
		listaarquivoProjetoSelecionado = new ArrayList<>();
		buscaPrograma();
	}

	public void buscaPrograma() {
		listProjeto = daoProjeto.listarComOrdenacao(Projeto.class, "id DESC");
	}

	public List<Projeto> getListProjeto() {
		return listProjeto;
	}

	public void setListProjeto(List<Projeto> listProjeto) {
		this.listProjeto = listProjeto;
	}
	
	public void visualizarProjeto(Projeto projetoSelecionado) {

		this.projetoSelecionado = projetoSelecionado; 
		listaarquivoProjetoSelecionado = daoArquivoProjeto.listarCodicaoLivre(ArquivoProjeto.class,
				"projeto.id = " + projetoSelecionado.getId() + " order by id desc");

	}
 
    
	
	

}
