package protocolo.service;

import java.io.Serializable;

import javax.inject.Inject;
 

 
import dao.GenericDAO;
import protocolo.modelo.Protocolo;
import util.Transacional;

public class ProtocoloService implements Serializable{
	

	private static final long serialVersionUID = 1L;
	@Inject
	private GenericDAO<Protocolo> dao;
	
	@Transacional
	public void inserirAlterar(Protocolo protocolo){
	
		if(protocolo.getId()==null){
			dao.inserir(protocolo);
		}else{
			dao.alterar(protocolo);
		}
		
	}
	
	@Transacional
	public void update(String valor){
		dao.update(valor);
	}

}
