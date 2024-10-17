package inventario.service;

import java.io.Serializable;

import javax.inject.Inject;
 
 
import dao.GenericDAO;
import inventario.modelo.Localidades;
import inventario.modelo.Locais;
import inventario.modelo.SetorLocal;
import util.Transacional;

public class SetorLocalService implements Serializable{
	

	private static final long serialVersionUID = 1L;
	@Inject
	private GenericDAO<SetorLocal> daoLocal;
	
	@Transacional
	public void inserirAlterar(SetorLocal obj){
	
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
	public void remover(SetorLocal obj){
	
		 
			daoLocal.remover(obj);
	 
		
	}
	
}
