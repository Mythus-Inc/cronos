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
import java.util.HashMap;
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
import ac.services.PessoaService;

import cope.dao.DAOParticipante;
import cope.dao.DAOProjeto;
import cope.modelo.ArquivoProjeto;
import cope.modelo.ParametroSistema;
import cope.modelo.Participante;
import cope.modelo.Projeto;
import cope.modelo.enums.StatusSubmissao;
import cope.modelo.enums.TipoArquivoProjeto;
import cope.service.ArquivoProjetoService;
import cope.service.ParticipanteService;
import cope.service.ProjetoService;
import dao.FiltrosDAO;
import dao.GenericDAO;
import util.ExibirMensagem;




 
public class ParametroSistemaCDI implements Serializable {

	private static final long serialVersionUID = 1L;

 
	@Inject
	private GenericDAO<ParametroSistema> daoParametroSistema;
	
	
	private static ParametroSistemaCDI parametroSistema;

	
	
	
	@PostConstruct
	public void inicializar() throws Exception { 
		System.out.println("primeiro esse");
		v();
 
	}
	
	
	public static ParametroSistemaCDI getInstance() throws Exception {
		
	
		
		if (parametroSistema == null){
			parametroSistema = new ParametroSistemaCDI();
			
		}
		
		 System.out.println("entro no 2");
		 System.out.println("vvvv:: "+parametroSistema);
		
		return parametroSistema;
	}
	
	private HashMap<String, Object> parametros;
	
	
	
	private ParametroSistemaCDI v()  {
		
	    parametros = new HashMap<String, Object>();

		System.out.println("entro no método++");
		
		try {
			
			List<ParametroSistema> liParametroSistema = new ArrayList<>();
			liParametroSistema = daoParametroSistema.listarSemStatus(ParametroSistema.class);
			
			for (ParametroSistema parametro : liParametroSistema) {
			parametros.remove(parametro.getChave());
			parametros.put(parametro.getChave(), parametro.getValor());
		}
	 
			 
		} catch (Exception ex) {
			throw ex;
		}
		
		return null;
	}
	
	
	public String getParametro(final String chave) {
		return String.valueOf(parametros.get(chave));
	}
	
	
	
	
	
 
	public void list(){
		
		System.out.println("mentodos");
		
 
		
		
	}
  
  
	  
 

}
