package protocolo.controle;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.enterprise.context.SessionScoped;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import util.ExibirMensagem;
import util.FecharDialog;
import util.Mensagem;
import util.PermiteInativar;
import ac.modelo.Grupo;
import ac.modelo.Permissao;
import ac.services.GrupoService;
import base.modelo.Servidor;
import dao.GenericDAO;
import protocolo.modelo.PessoaSetor;
import protocolo.modelo.Requerimento;
import protocolo.modelo.Setor;
import protocolo.service.RequerimentoService;
import protocolo.service.ServidorSetorService;
import protocolo.service.SetorService;

@ViewScoped
@Named("requerimentoMB")
public class RequerimentoMB implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Requerimento requerimento; 
	private List<Requerimento> listRequerimento;
	
	/*/private String valor;
	private String valor1;
	private String valor2;
	private String valor3;
	private String valor4;
	private String valor5;
	private String valor6;
	private String valor7;
	*/
 
	@Inject
	private GenericDAO<Requerimento> daoRequerimento;
	
	@Inject
	private GenericDAO<Setor> daoSetor;
	 
	@Inject
	private RequerimentoService requerimentoService;
	
 

	@PostConstruct
	public void inicializar() {
		requerimento = new Requerimento(); 
		listRequerimento = new ArrayList<>();
		buscarRequerimento();
	}
	
	
	public void criarNovoObjeto(){
	 requerimento = new Requerimento();
	}
	
	public void remover(){
		requerimento.setStatus(false);
		requerimentoService.inserirAlterar(requerimento);
		ExibirMensagem.exibirMensagem(Mensagem.SUCESSO);
		FecharDialog.fecharDialogRequerimentoExcluir();
		criarNovoObjeto();
		buscarRequerimento();
		
	}
 
	
	public void abrirDetalhado(Requerimento requerimento){
		this.requerimento = requerimento;
	}
	
	
	public void salvar(){
		
		
		if(requerimento.getId() == null){
		requerimento.setStatus(true);
		requerimentoService.inserirAlterar(requerimento);
		ExibirMensagem.exibirMensagem(Mensagem.SUCESSO);
		FecharDialog.fecharDialogRequerimento();

		}else{
		System.out.println("else");
		requerimentoService.inserirAlterar(requerimento);
		ExibirMensagem.exibirMensagem(Mensagem.SUCESSO);
		FecharDialog.fecharDialogRequerimentoEditar();
		}
			
		criarNovoObjeto();
		buscarRequerimento();
		
		
		
		
	}
	
	/*public void metodo() {

		if (valor.equals("valor1") == true) {
			requerimento.setCienciaEmailProfessores(true);
		
		} else {
			requerimento.setCienciaEmailProfessores(false);
		
		}
		if (valor1.equals("valor1") == true) {
			requerimento.setCienciaEmailSecretaria(true);
		
		} else {
			requerimento.setCienciaEmailSecretaria(false);
			
		}
		if (valor2.equals("valor1") == true) {
			requerimento.setCienciaEmailCoordenador(true);
		
		} else {
			requerimento.setCienciaEmailCoordenador(false);
			
		}
		if (valor3.equals("valor1") == true) {
			requerimento.setCienciaEmailDirecaoEnsino(true);
		
		} else {
			requerimento.setCienciaEmailDirecaoEnsino(false);
		
		}
		if (valor4.equals("valor1") == true) {
			requerimento.setCienciaEmailcoordenacaoPedagogica(true);
		
		} else {
			requerimento.setCienciaEmailcoordenacaoPedagogica(false);
	
		}
		if (valor5.equals("valor1") == true) {
			requerimento.setCienciaEmailRequerente(true);
	
		} else {
			requerimento.setCienciaEmailRequerente(false);
		
		}
		if (valor6.equals("sim") == true) {
			requerimento.setAutenticacaoNecessariaDocumentos(true);
		
		} else {
			requerimento.setAutenticacaoNecessariaDocumentos(false);
			
		}
		if (valor7.equals("sim") == true) {
			requerimento.setNotificarSecretariaParecerFinal(true);
			
		} else {
			requerimento.setNotificarSecretariaParecerFinal(false);
		
		}

	}
	
	*/
	
	
	public void buscarRequerimento(){
		listRequerimento = daoRequerimento.listaComStatus(Requerimento.class);
	}
	
	
	public List<Setor> completeSetor(String query) {
		List<Setor> listaSetor = new ArrayList<>();
		
		listaSetor = daoSetor.listaComStatus(Setor.class);
		
		List<Setor> results = new ArrayList<>();
		for (Setor e : listaSetor) {
			if (e.getDescricao().toLowerCase().startsWith(query)) {
				results.add(e);
			}
		}

		return results;
	}


	public Requerimento getRequerimento() {
		return requerimento;
	}


	public void setRequerimento(Requerimento requerimento) {
		requerimento = requerimento;
	}


	public List<Requerimento> getListRequerimento() {
		return listRequerimento;
	}


	public void setListRequerimento(List<Requerimento> listRequerimento) {
		this.listRequerimento = listRequerimento;
	}


 
	


}
