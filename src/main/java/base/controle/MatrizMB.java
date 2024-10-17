package base.controle;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import ac.services.AlunoService;
import base.modelo.Matriz;
import base.modelo.Tipo;
import base.service.MatrizService;
import base.service.TipoService;
import util.ExibirMensagem;
import util.FecharDialog;
import util.Mensagem;
import util.ValidacoesGerirUsuarios;
import dao.GenericDAO;

@ViewScoped
@Named("matrizMB")
public class MatrizMB implements Serializable {

	private static final long serialVersionUID = 1L;

	private Matriz matriz;
	private List<Matriz> listMatriz;

	@Inject
	private GenericDAO<Matriz> daoMatriz; // faz as buscas

	@Inject
	private MatrizService matrizService; // inserir no banco

	@PostConstruct
	public void inicializar() {

		matriz = new Matriz();

		listMatriz = new ArrayList<>();
		listMatriz = daoMatriz.listaComStatus(Matriz.class);

	}

	public void preencherListaMatriz(Matriz t) {
		this.matriz = t;

	}

	public void inativarMatriz(Matriz t) {
		matrizService.update(" Matriz set status = false where id = " + t.getId());
		criarNovoObjeto();
		ExibirMensagem.exibirMensagem(Mensagem.SUCESSO);
		carregarLista();
	}

	public void salvar() {

		try {

			if (matriz.getId() == null) {
				if (verificarMatriz(matriz)) {
					ExibirMensagem.exibirMensagem(Mensagem.MATRIZCADASTRO);
				} else {

					matriz.setStatus(true);
					matriz.setDataCadastro(new Date());
					matrizService.inserirAlterar(matriz);

					criarNovoObjeto();
					ExibirMensagem.exibirMensagem(Mensagem.SUCESSO);
					FecharDialog.fecharDialogMatriz();
					carregarLista();
				}

			} else {
				if (verificarMatriz(matriz) && verificarMatrizIguaisAlterar(matriz)) {
					ExibirMensagem.exibirMensagem(Mensagem.TURMA);
				} else {

					matrizService.inserirAlterar(matriz);

					criarNovoObjeto();
					ExibirMensagem.exibirMensagem(Mensagem.SUCESSO);
					FecharDialog.fecharDialogMatriz();
					carregarLista();
				}
			}
		} catch (Exception e) {
			ExibirMensagem.exibirMensagem(Mensagem.ERRO);
			e.printStackTrace();
		}

	}

	public Boolean verificarMatriz(Matriz matriz) {
		try {
			List<Matriz> verificador = new ArrayList<>();
			verificador = daoMatriz.listar(Matriz.class, " descricao = '" + matriz.getDescricao()+"'");
			if (verificador.isEmpty())
				return false;
		} catch (Exception e) {
			System.err.println("Erro no metodo verificarGrupoTurmaIguais");
			e.printStackTrace();
		}
		return true;
	}

	public Boolean verificarMatrizIguaisAlterar(Matriz matriz) {
		try {
			List<Matriz> verificador = new ArrayList<>();
			verificador = daoMatriz.listar(Matriz.class,
					" descricao = '" + matriz.getDescricao() + "' and id = " + matriz.getId());
			if (verificador.isEmpty())
				return true;
		} catch (Exception e) {
			System.err.println("Erro no metodo verificarGrupoTurmaIguais");
			e.printStackTrace();
		}
		return false;
	}

	public void criarNovoObjeto() {
		matriz = new Matriz();
	}

	public void carregarLista() {
		listMatriz = daoMatriz.listaComStatus(Matriz.class);
	}

	public Matriz getMatriz() {
		return matriz;
	}

	public void setMatriz(Matriz matriz) {
		this.matriz = matriz;
	}

	public List<Matriz> getListMatriz() {
		return listMatriz;
	}

	public void setListMatriz(List<Matriz> listMatriz) {
		this.listMatriz = listMatriz;
	}

	public GenericDAO<Matriz> getDaoMatriz() {
		return daoMatriz;
	}

	public void setDaoMatriz(GenericDAO<Matriz> daoMatriz) {
		this.daoMatriz = daoMatriz;
	}

	public MatrizService getMatrizService() {
		return matrizService;
	}

	public void setMatrizService(MatrizService matrizService) {
		this.matrizService = matrizService;
	}

}
