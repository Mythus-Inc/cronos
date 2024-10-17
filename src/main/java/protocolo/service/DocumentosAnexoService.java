package protocolo.service;

import java.io.Serializable;

import javax.inject.Inject;
 

 
import dao.GenericDAO;
import protocolo.modelo.DocumentosAnexo; 
import util.Transacional;

public class DocumentosAnexoService implements Serializable{
	

	private static final long serialVersionUID = 1L;
	@Inject
	private GenericDAO<DocumentosAnexo> dao;
	
	@Transacional
	public void inserirAlterar(DocumentosAnexo documentosAnexo){
	
		if(documentosAnexo.getId()==null){
			dao.inserir(documentosAnexo);
		}else{
			dao.alterar(documentosAnexo);
		}
		
	}
	
	@Transacional
	public void update(String valor){
		dao.update(valor);
	}

}
