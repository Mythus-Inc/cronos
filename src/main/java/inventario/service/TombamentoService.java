package inventario.service;

import java.io.Serializable;

import javax.inject.Inject;
 
 
import dao.GenericDAO;
import inventario.modelo.Equipamento;
import inventario.modelo.Localidades;
import inventario.modelo.Tombamento;
import util.Transacional;

public class TombamentoService implements Serializable{
	

	private static final long serialVersionUID = 1L;
	@Inject
	private GenericDAO<Tombamento> dao;
	
	@Transacional
	public void inserirAlterar(Tombamento obj){
	
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
	public void remover(Tombamento obj){		 
			dao.remover(obj);
	}

}
