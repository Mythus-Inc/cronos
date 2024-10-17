package inventario.service;

import java.io.Serializable;

import javax.inject.Inject;
 
 
import dao.GenericDAO;
import inventario.modelo.Localidades;
import inventario.modelo.Locais;
import util.Transacional;

public class LocalidadeService implements Serializable{
	

	private static final long serialVersionUID = 1L;
	@Inject
	private GenericDAO<Locais> daoLocal;
	
	@Transacional
	public void inserirAlterar(Locais obj){
	
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
	public void remover(Locais obj){
	
		 
			daoLocal.remover(obj);
		 
		
	}

}
