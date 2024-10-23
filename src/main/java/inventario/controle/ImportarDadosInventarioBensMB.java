package inventario.controle;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import jxl.Cell;
import jxl.Sheet;
import jxl.Workbook;
import jxl.WorkbookSettings;
import jxl.read.biff.BiffException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.zip.Deflater;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipOutputStream;
import javax.annotation.PostConstruct;
import javax.enterprise.context.SessionScoped;
import javax.faces.application.Application;
import javax.faces.application.ViewHandler;
import javax.faces.component.UIViewRoot;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.EnumType;

import org.apache.commons.collections.map.HashedMap;
import org.eclipse.persistence.sessions.DatasourceLogin;
import org.primefaces.context.RequestContext;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;
import org.primefaces.model.UploadedFile;
import util.CalculoEquivalencia;
import util.CaminhoArquivos;
import util.CriptografiaSenha;
import util.ExibirMensagem;
import util.FecharDialog;
import util.GeradorSenhas;
import util.Mensagem;
import util.ValidaCPF;
import util.ValidaTopoAtividade;
import util.ValidaTopoGrupo;
import util.ZipUtils;
import ac.modelo.AlunoTurma;
import ac.modelo.AtividadeTurma;
import ac.modelo.Certificado;
import ac.modelo.GrupoTurma;
import ac.modelo.Pessoa;
import ac.services.AlunoService;
import ac.services.PessoaService;
import base.modelo.Aluno;
import base.modelo.Turma;
import base.service.AlunoTurmaService;
import base.service.CertificadoService;
import base.service.MovimentacaoService;
import dao.FiltrosDAO;
import dao.GenericDAO;
import inventario.modelo.Equipamento;
import inventario.modelo.EstadoConservacao;
import inventario.modelo.Locais;
import inventario.modelo.Localidades;
import inventario.modelo.Movimentacoes;
import inventario.modelo.SetorLocal;
import inventario.modelo.Tombamento;
import inventario.service.EquipamentoInventarioService;
import inventario.service.EquipamentoService;
import inventario.service.LocalService;
import inventario.service.LocalidadeService;
import inventario.service.TombamentoService;

@ViewScoped
@Named("importarDadosInventarioBensMB")
public class ImportarDadosInventarioBensMB implements Serializable {

	private static final long serialVersionUID = 1L;

	private Equipamento equipamento;
	private Localidades localidade;
	private Tombamento tombamentos;
	private Equipamento equipamentoReferencia;
	private List<Equipamento> listEquipamento;
	private List<Tombamento> listTombamento;
	private List<Tombamento> listTombamentoNaoAdd;
	private List<Localidades> listaLocalidades;

	private String SOURCE_FOLDER = CaminhoArquivos.caminhoCertificados();

	@Inject
	private GenericDAO<Equipamento> daoEquipamento;

	@Inject
	private GenericDAO<Localidades> daoLocalidade;

	@Inject
	private EquipamentoService equipamentoService;

	@Inject
	private TombamentoService tombamentoService;

	@PostConstruct
	public void inicializar() {

		listEquipamento = new ArrayList<>();
		listTombamento = new ArrayList<>();
		listTombamentoNaoAdd = new ArrayList<>();

	}

	public void removBackup(String nomeArquivo) {

		File file = new File(CaminhoArquivos.caminhoCertificados() + "/" + nomeArquivo);
		file.delete();
	}

	public void uploadDados(FileUploadEvent evento) {
		try {
			UploadedFile arquivoUpload = evento.getFile();
			if (!arquivoUpload.getFileName().isEmpty()) {

				Path arquivoTemp = Files.createTempFile(null, null);
				Files.copy(arquivoUpload.getInputstream(), arquivoTemp, StandardCopyOption.REPLACE_EXISTING);
				Path origem = Paths.get(arquivoTemp.toString());

				removBackup(String.valueOf(arquivoUpload.getFileName()));

				File diretorio = new File(CaminhoArquivos.caminhoCertificados());
				if (!diretorio.exists()) {
					diretorio.mkdirs();
				}

				Path destino = Paths.get(diretorio.getCanonicalFile() + "//" + evento.getFile().getFileName());
				Files.copy(origem, destino, StandardCopyOption.REPLACE_EXISTING);

				ExibirMensagem.exibirMensagem(Mensagem.UPLOAD);

				importaDados(String.valueOf(arquivoUpload.getFileName()));

			}

		} catch (Exception e) {
			System.err.println("Erro em: upload");
			e.printStackTrace();
		}
	}

	public void chamarFechar() {

		FecharDialog.fecharDialogAbreStatus();
	}

	public void importaDados(String nomeArquivo) throws IOException, BiffException {
		WorkbookSettings ws = new WorkbookSettings(); 
		ws.setEncoding("Cp1252");

		Workbook workbook = Workbook.getWorkbook(new File(CaminhoArquivos.caminhoCertificados() + "/" + nomeArquivo),ws);

		Sheet sheet = workbook.getSheet(0);

		listaLocalidades = daoLocalidade.listaComStatus(Localidades.class);
		boolean controle;
		int linhas = sheet.getRows();

		for (int i = 1; i < linhas; i++) {

			Cell tombamento = sheet.getCell(0, i);
			Cell descricaoEquipamento = sheet.getCell(1, i);
			Cell codigoLocalidade = sheet.getCell(2, i);
			Cell nomeLocalidade = sheet.getCell(3, i);

			String tombamento1 = tombamento.getContents();
			String descricaoEquipamento1 = descricaoEquipamento.getContents();
			String codigoLocalidade1 = codigoLocalidade.getContents();
			String nomeLocalidade1 = nomeLocalidade.getContents();
			

			if (!tombamento1.equals("")) {

				//if (addEquipamento(descricaoEquipamento1)) {
					equipamento = new Equipamento();
					equipamento.setStatus(true);
					equipamento.setDescricao(descricaoEquipamento1);
					//listEquipamento.add(equipamento);
				//}

				tombamentos = new Tombamento();
				tombamentos.setCodigo(tombamento1);
				tombamentos.setEquipamento(equipamento);
				tombamentos.setEstado(EstadoConservacao.BOM);
				tombamentos.setStatus(true);
				if (localidade(codigoLocalidade1)) {
					tombamentos.setLocal(localidade);
					listTombamento.add(tombamentos);
				} else {
					Localidades localBem = new Localidades();
					localBem.setDescricao(nomeLocalidade1);
					tombamentos.setLocal(localBem);
					listTombamentoNaoAdd.add(tombamentos);
				}
				System.out.println("lendo " + i);

			}

		}

		System.out.println("tamanho da lista " + listTombamento.size());

		workbook.close();

		removBackup(nomeArquivo);

	}

	public boolean localidade(String localidade) {

		for (Localidades l : listaLocalidades) {
			if (l.getCodigoLocalidade().trim().equals(localidade.trim())) {
				this.localidade = l;
				return true;
			}
		}
		return false;

	}

	public boolean addEquipamento(String equipamento) {
		System.out.println(listEquipamento.size());
		for (Equipamento eq : listEquipamento) {
			if (eq.getDescricao().trim().equals(equipamento.trim())) {
				equipamentoReferencia = new Equipamento();
				equipamentoReferencia = eq;
				return false;
			}
		}
		return true;
	}

	public void importa() {
		int inserido = 0;

		try {
			for (Tombamento t : listTombamento) {

				if (addEquipamento(t.getEquipamento().getDescricao())) {
					// erro aqui
					//System.out.println(t.getEquipamento());
					Equipamento e = new Equipamento();
					e.setDescricao(t.getEquipamento().getDescricao());
					e.setStatus(true);
					equipamentoService.inserirAlterar(e);
					t.setEquipamento(e);
					//System.out.println(t.getEquipamento());
					
					listEquipamento.add(e);
				} else {
					//System.out.println("Referência: "+equipamentoReferencia);
					t.setEquipamento(equipamentoReferencia);
				}
				//System.out.println("Antes tombamento: "+t.getEquipamento());

				tombamentoService.inserirAlterar(t);
				inserido += 1;

			}

			ExibirMensagem.exibirMensagem(Mensagem.SUCESSO + "  - " + inserido + " localidades importadas");
			listTombamento = new ArrayList<>();

		} catch (Exception e) {
			e.printStackTrace();
			ExibirMensagem.exibirMensagem("" + e);
		}

	}

	public Date retornaData(String dataConverte, Aluno aluno) {
		try {

			SimpleDateFormat in = new SimpleDateFormat("dd/MM/yyyy");
			SimpleDateFormat out = new SimpleDateFormat("yyyy-MM-dd");

			String result = out.format(in.parse(dataConverte.toString()));

			Date datse = (Date) out.parse(result);
			return datse;

		} catch (ParseException e) {
			return null;

		}

	}

	public void refresh() {
		FacesContext context = FacesContext.getCurrentInstance();
		Application application = context.getApplication();
		ViewHandler viewHandler = application.getViewHandler();
		UIViewRoot viewRoot = viewHandler.createView(context, context.getViewRoot().getViewId());
		context.setViewRoot(viewRoot);

	}

	public List<Tombamento> getListTombamento() {
		return listTombamento;
	}

	public void setListTombamento(List<Tombamento> listTombamento) {
		this.listTombamento = listTombamento;
	}

	public List<Tombamento> getListTombamentoNaoAdd() {
		return listTombamentoNaoAdd;
	}

	public void setListTombamentoNaoAdd(List<Tombamento> listTombamentoNaoAdd) {
		this.listTombamentoNaoAdd = listTombamentoNaoAdd;
	}

}
