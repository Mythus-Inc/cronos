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
import org.apache.commons.collections.map.HashedMap;
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
import base.modelo.Servidor;
import base.modelo.Turma;
import base.service.AlunoTurmaService;
import base.service.CertificadoService;
import base.service.MovimentacaoService;
import dao.FiltrosDAO;
import dao.GenericDAO;
import inventario.modelo.Equipamento;
import inventario.modelo.Locais;
import inventario.modelo.Localidades;
import inventario.modelo.Movimentacoes;
import inventario.modelo.SetorLocal;
import inventario.service.LocaisInventarioService;
import inventario.service.LocalService;
import inventario.service.LocalidadeService;
import inventario.service.SetorLocalService;

@ViewScoped
@Named("importarDadosInventarioMB")
public class ImportarDadosInventarioMB implements Serializable {

	private static final long serialVersionUID = 1L;

	private Localidades localidade;
	private Locais locais;
	private Locais locaisReferencia;
	private List<Locais> listLocais;
	
	private List<Servidor> listServidor;
	private SetorLocal setorLocal;
	private SetorLocal setorLocalReferencia;
	private List<SetorLocal> listSetorLocal;
	
	private List<Localidades> listLocalidades;
	private DefaultStreamedContent download;

	private String SOURCE_FOLDER = CaminhoArquivos.caminhoCertificados();

	@Inject
	private GenericDAO<Localidades> daoLocalidades;
	
	@Inject
	private GenericDAO<SetorLocal> daoSetorLocal;
	
	@Inject
	private GenericDAO<Locais> daoLocais;

	@Inject
	private LocalService localidadeService;

	@Inject
	private LocalidadeService localService;
	
	@Inject
	private SetorLocalService setorLocalService;

	@Inject
	private GenericDAO<Servidor> daoServidor;
	
	@PostConstruct
	public void inicializar() {

		listLocais = new ArrayList<>();
		listSetorLocal = new ArrayList<>();
		listLocalidades = new ArrayList<>();
		listServidor = daoServidor.listaComStatus(Servidor.class);

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

		

		int linhas = sheet.getRows();
		for (int i = 1; i < linhas; i++) {

			Cell bloco = sheet.getCell(1, i);
			Cell codigoUnidade = sheet.getCell(2, i);
			Cell nomeLocalidade = sheet.getCell(3, i);
			Cell codigoUnidadeResponsavel = sheet.getCell(4, i);
			Cell nomeResponsavelUnidade = sheet.getCell(5, i);
			Cell nomeUnidadeResponsavel = sheet.getCell(6, i);

			String bloco1 = bloco.getContents();
			String codigoUnidade1 = codigoUnidade.getContents();
			String nomeLocalidade1 = nomeLocalidade.getContents();
			String codigoUnidadeResponsavel1 = ""+codigoUnidadeResponsavel.getContents();
			String codigoUnidadeResponsavel12 = "."+codigoUnidadeResponsavel.getContents();
			String nomeResponsavelUnidade1 = nomeResponsavelUnidade.getContents();
			String nomeUnidadeResponsavel1 = nomeUnidadeResponsavel.getContents();
			
			
			
			String cs= codigoUnidadeResponsavel.getContents();
			
		 
			

			
			  
	 

			if (!bloco1.equals("-") || !codigoUnidade1.equals("-") || !nomeLocalidade1.equals("-")
					|| !codigoUnidadeResponsavel1.equals("-") || !nomeResponsavelUnidade1.equals("-")) {
				
				localidade = new Localidades();
				setorLocal = new SetorLocal();
				locais = new Locais();
				
				
				locais.setStatus(true);
				locais.setDescricao(bloco1);
				
				setorLocal.setStatus(true); 
				setorLocal.setCodigoUnidade(String.valueOf(codigoUnidadeResponsavel1));			 
				setorLocal.setDescricao(nomeUnidadeResponsavel1);
				
			 
				
				setorLocal.setServidorResponsavel(buscaServidor(nomeResponsavelUnidade1));
				
				

				localidade.setStatus(true); 
				localidade.setLocais(locais);
				localidade.setSetorLocal(setorLocal); 
				localidade.setCodigoLocalidade(String.valueOf(codigoUnidade1));
				localidade.setDescricao(nomeLocalidade1);		 
				listLocalidades.add(localidade);
			}

		}

		System.out.println("tamanho " + listLocalidades.size());

		workbook.close();

		removBackup(nomeArquivo);

	}
	
	public Servidor buscaServidor(String nome){
		
		for(Servidor s : listServidor){
			if(s.getNome().equalsIgnoreCase(nome))
				return s;
		}
		return null;
	}
	
	
	public boolean addLocais(String locais) {

		
		for (Locais eq : listLocais) {
			if (eq.getDescricao().equals(locais)) {
				locaisReferencia = new Locais();
				locaisReferencia = eq;
				return false;
			}
		}
		return true;
	}

	public boolean addUnidadeResponsavel(String codigoUnidade) {

		for (SetorLocal eq : listSetorLocal) {
			if (eq.getCodigoUnidade().equals(codigoUnidade)) {
				setorLocalReferencia = new SetorLocal();
				setorLocalReferencia = eq;
				return false;
			}
		}
		return true;
	}
	
	public void importa() {
		
		listLocais = daoLocais.listaComStatus(Locais.class);
		listSetorLocal = daoSetorLocal.listaComStatus(SetorLocal.class);
		
		int inserido = 0;
		for (Localidades l : listLocalidades) {
			
			if (addLocais(l.getLocais().getDescricao())) {
				localService.inserirAlterar(l.getLocais());
				listLocais.add(l.getLocais());
			} else {
				l.setLocais(locaisReferencia);
			}
			
			if(addUnidadeResponsavel(l.getSetorLocal().getCodigoUnidade())){
				setorLocalService.inserirAlterar(l.getSetorLocal());
				listSetorLocal.add(l.getSetorLocal());
			}else{
				l.setSetorLocal(setorLocalReferencia);
			}

					
			
			
			localidadeService.inserirAlterar(l);
			inserido += 1;
		}
		ExibirMensagem.exibirMensagem(Mensagem.SUCESSO + "  - " + inserido + " localidades importadas");

		listLocalidades = new ArrayList<>();
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

	public List<Localidades> getListLocalidades() {
		return listLocalidades;
	}

	public void setListLocalidades(List<Localidades> listLocalidades) {
		this.listLocalidades = listLocalidades;
	}

}
