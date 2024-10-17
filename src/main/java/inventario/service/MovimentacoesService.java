package inventario.service;

import java.io.Serializable;

import javax.inject.Inject;
 
 
import dao.GenericDAO;
import inventario.modelo.Equipamento;
import inventario.modelo.Localidades;
import inventario.modelo.Movimentacoes;
import util.Transacional;

public class MovimentacoesService implements Serializable{
	

	private static final long serialVersionUID = 1L;
	@Inject
	private GenericDAO<Movimentacoes> dao;
	
	@Transacional
	public void inserirAlterar(Movimentacoes obj){
	
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
	public void remover(Movimentacoes obj){
	
		 
			dao.remover(obj);
		 
		
	}
	

}
