package inventario.service;

import java.io.Serializable;

import javax.inject.Inject;

import dao.GenericDAO;
import inventario.modelo.EquipamentoInventario;
import util.Transacional;

public class EquipamentoInventarioService implements Serializable {

	private static final long serialVersionUID = 1L;
	@Inject
	private GenericDAO<EquipamentoInventario> dao;

	@Transacional
	public void inserirAlterar(EquipamentoInventario obj) {

		if (obj.getId() == null) {
			dao.inserir(obj);
		} else {
			dao.alterar(obj);
		}

	}

	@Transacional
	public void update(String valor) {
		dao.update(valor);
	}

	@Transacional
	public void remover(EquipamentoInventario obj) {

		dao.remover(obj);
	}

}
