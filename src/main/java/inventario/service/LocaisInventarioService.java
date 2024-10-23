package inventario.service;

import java.io.Serializable;

import javax.inject.Inject;
 
 
import dao.GenericDAO;
import inventario.modelo.Inventario;
import inventario.modelo.LocalInventario;
import util.Transacional;

public class LocaisInventarioService implements Serializable{
	

	private static final long serialVersionUID = 1L;
	@Inject
	private GenericDAO<LocalInventario> dao;
	
	@Transacional
	public void inserirAlterar(LocalInventario obj){
	
		if(obj.getId()==null){
			dao.inserir(obj);
		}else{
			dao.alterar(obj);
		}
		
	}
	
	@Transacional
	public void update(String valor){
		dao.update(valor);
	}
	
	
	@Transacional
	public void remover(LocalInventario obj){
	 
			dao.remover(obj);
	 
		
	}

}
