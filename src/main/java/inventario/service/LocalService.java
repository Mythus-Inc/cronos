package inventario.service;

import java.io.Serializable;

import javax.inject.Inject;
 
 
import dao.GenericDAO;
import inventario.modelo.Localidades; 
import util.Transacional;

public class LocalService implements Serializable{
	

	private static final long serialVersionUID = 1L;
	@Inject
	private GenericDAO<Localidades> daoLocal;
	
	@Transacional
	public void inserirAlterar(Localidades obj){
	
		if(obj.getId()==null){
			daoLocal.inserir(obj);
		}else{
			daoLocal.alterar(obj);
		}
		
	}
	
	@Transacional
	public void update(String valor){
		daoLocal.update(valor);
	}
	
	@Transacional
	public void remover(Localidades obj){
	
		 
			daoLocal.remover(obj);
		 
		
	}

}
