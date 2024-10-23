package inventario.controle;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.EntityManager;

import org.hibernate.Session;

import util.ChamarRelatorio;
import util.ExibirMensagem;
import util.Mensagem;
import util.RecuperarRelacoesProfessor;
import util.VerificaSituacaoTurma;
import ac.modelo.AlunoTurma;
import ac.modelo.AtividadeTurma;
import ac.modelo.Certificado;
import ac.modelo.GrupoTurma;
import ac.services.AlunoService;
import base.modelo.Turma;
import dao.GenericDAO;
import inventario.modelo.EquipamentoInventario;
import inventario.modelo.Inventario;
import inventario.modelo.LocalInventario;
import inventario.modelo.Localidades;
import inventario.modelo.Tombamento;

@ViewScoped
@Named("relatorioIventarioMB")
public class RelatorioIventarioMB implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

 
	private List<EquipamentoInventario> listEquipamentoInventario;
	private EquipamentoInventario equipamentoInventario;
	private Inventario inventario;
	private LocalInventario localInventairo;

	@Inject
	private GenericDAO<EquipamentoInventario> daoEquipamentoInventario;
	
	@Inject
	private GenericDAO<Inventario> daoInventario;
	
	@Inject
	private GenericDAO<Tombamento> daoTombamento;
	
	@Inject
	private GenericDAO<LocalInventario> daoLocalInventario;
	
 
 
	@Inject
	private EntityManager manager;
 
	@PostConstruct
	public void inicializar() { 
		listEquipamentoInventario = new ArrayList<>(); 
		inventario = new Inventario();
		localInventairo = new LocalInventario();
	}

	public void imprimirRelatorioBensLocalErrado() {
 
		try {
			List<EquipamentoInventario> equipamentoInventario = daoEquipamentoInventario.listar(EquipamentoInventario.class,
					" pertenceLocal = false and localInventario.Inventario.id ='" + inventario.getId()+"'");

			if (equipamentoInventario.size()>0) {
				 

				HashMap parametro = new HashMap<>();
				parametro.put("IDINVENTARIO", inventario.getId());
				ChamarRelatorio ch = new ChamarRelatorio("bensComDivegenciaLocal.jasper", parametro,
						"relat�rio_" + "Relat�rio de Bens em locais errado ");
				Session sessions = manager.unwrap(Session.class);
				sessions.doWork(ch);

			} else {
				ExibirMensagem.exibirMensagem(Mensagem.NADA_ENCONTRADO);
			}
		} catch (Exception e) {
			e.printStackTrace();
			ExibirMensagem.exibirMensagem(Mensagem.ERRO);
		}
	}
	
	
	public void imprimirRelatorioBensGeral() {
		 
		try {
			List<EquipamentoInventario> equipamentoInventario = daoEquipamentoInventario.listar(EquipamentoInventario.class,
					"localInventario.Inventario.id ='" + inventario.getId()+"'");

			if (equipamentoInventario.size()>0) {
				 

				HashMap parametro = new HashMap<>();
				parametro.put("IDINVENTARIO", inventario.getId());
				ChamarRelatorio ch = new ChamarRelatorio("bensTombadosGeralOutro.jasper", parametro,
						"relatório_" + "Relatório de Bens Geral ");
				Session sessions = manager.unwrap(Session.class);
				sessions.doWork(ch);

			} else {
				ExibirMensagem.exibirMensagem(Mensagem.NADA_ENCONTRADO);
			}
		} catch (Exception e) {
			e.printStackTrace();
			ExibirMensagem.exibirMensagem(Mensagem.ERRO);
		}
	}
	
	
	public void imprimirRelatorioBensGeralNaoTombado() {
		 
		try {
			List<EquipamentoInventario> equipamentoInventario = daoEquipamentoInventario.listar(EquipamentoInventario.class,
					" naoTombado is true and localInventario.Inventario.id ='" + inventario.getId()+"'");

			if (equipamentoInventario.size()>0) {
				 

				HashMap parametro = new HashMap<>();
				parametro.put("IDLOCALINVENTARIO", inventario.getId());
				ChamarRelatorio ch = new ChamarRelatorio("bensNaoTombadosGeralNovo.jasper", parametro,
						"relatório_" + "BENS NÃO TOMBADOS - RELATÓRIO GERAL ");
				Session sessions = manager.unwrap(Session.class);
				sessions.doWork(ch);

			} else {
				ExibirMensagem.exibirMensagem(Mensagem.NADA_ENCONTRADO);
			}
		} catch (Exception e) {
			e.printStackTrace();
			ExibirMensagem.exibirMensagem(Mensagem.ERRO);
		}
	}
	
	
	

	
	
	public void imprimirRelatorioBensTombadoNaoLocalizado() {
		 
		try {
			/*List<EquipamentoInventario> equipamentoInventario = daoEquipamentoInventario.listar(EquipamentoInventario.class,
					" naoTombado is true and localInventario.Inventario.id ='" + inventario.getId()+"'");

			if (equipamentoInventario.size()>0) {*/
				 

				HashMap parametro = new HashMap<>();
				parametro.put("IDINVENTARIO", inventario.getId());
				parametro.put("descricao", inventario.getDescricao());
				ChamarRelatorio ch = new ChamarRelatorio("bensTombadoNaoEncontrado.jasper", parametro,
						"relatório_" + "BENS TOMBADOS E NÃO ENCONTRADO");
				Session sessions = manager.unwrap(Session.class);
				sessions.doWork(ch);

			/*} else {
				ExibirMensagem.exibirMensagem(Mensagem.NADA_ENCONTRADO);
			}*/
		} catch (Exception e) {
			e.printStackTrace();
			ExibirMensagem.exibirMensagem(Mensagem.ERRO);
		}
	}
	
	
	public void imprimirTombamentosTotal() {
		 
		try {
			List<Tombamento> listaTombamento = daoTombamento.listaComStatus(Tombamento.class);

			if (listaTombamento.size()>0) {
				 

				HashMap parametro = new HashMap<>(); 
				ChamarRelatorio ch = new ChamarRelatorio("bensTotal.jasper", parametro,
						"relatório_" + "BENS CADASTRADOS");
				Session sessions = manager.unwrap(Session.class);
				sessions.doWork(ch);

			} else {
				ExibirMensagem.exibirMensagem(Mensagem.NADA_ENCONTRADO);
			}
		} catch (Exception e) {
			e.printStackTrace();
			ExibirMensagem.exibirMensagem(Mensagem.ERRO);
		}
	}
	
	
	public void imprimirRelatorioTrocaEtiqueta() {
		 
		try {
			List<EquipamentoInventario> equipamentoInventario = daoEquipamentoInventario.listar(EquipamentoInventario.class,
					" naoTombado is false and trocarEtiqueta = 'sim' and localInventario.Inventario.id ='" + inventario.getId()+"'");

			if (equipamentoInventario.size()>0) {
				 

				HashMap parametro = new HashMap<>();
				parametro.put("IDINVENTARIO", inventario.getId());
				ChamarRelatorio ch = new ChamarRelatorio("reimpressaoEtiqueta.jasper", parametro,
						"relatório_" + "Reimpressão de etiqueta ");
				Session sessions = manager.unwrap(Session.class);
				sessions.doWork(ch);

			} else {
				ExibirMensagem.exibirMensagem(Mensagem.NADA_ENCONTRADO);
			}
		} catch (Exception e) {
			e.printStackTrace();
			ExibirMensagem.exibirMensagem(Mensagem.ERRO);
		}
	}
	
	
	public void imprimirRelatorioBensEmDuplicidade() {
		 
		try {
			List<EquipamentoInventario> equipamentoInventario = daoEquipamentoInventario.listar(EquipamentoInventario.class,
					" naoTombado is false and bensEmDuplicidade is true and localInventario.Inventario.id ='" + inventario.getId()+"'");

			if (equipamentoInventario.size()>0) {
				 

				HashMap parametro = new HashMap<>();
				parametro.put("IDINVENTARIO", inventario.getId());
				ChamarRelatorio ch = new ChamarRelatorio("bensEmDuplicidade.jasper", parametro,
						"relatório_" + "Bens em Duplicidade ");
				Session sessions = manager.unwrap(Session.class);
				sessions.doWork(ch);

			} else {
				ExibirMensagem.exibirMensagem(Mensagem.NADA_ENCONTRADO);
			}
		} catch (Exception e) {
			e.printStackTrace();
			ExibirMensagem.exibirMensagem(Mensagem.ERRO);
		}
	}
	
	
	public void imprimirRelatorioBensOcioso() {
		 
		try {
			List<EquipamentoInventario> equipamentoInventario = daoEquipamentoInventario.listar(EquipamentoInventario.class,
					" naoTombado is false and estado = 'OCIOSO' and localInventario.Inventario.id ='" + inventario.getId()+"'");

			if (equipamentoInventario.size()>0) {
				 

				HashMap parametro = new HashMap<>();
				parametro.put("IDINVENTARIO", inventario.getId());
				ChamarRelatorio ch = new ChamarRelatorio("bensOcioso.jasper", parametro,
						"relatório_" + "Reimpressão de Bens Ocioso ");
				Session sessions = manager.unwrap(Session.class);
				sessions.doWork(ch);

			} else {
				ExibirMensagem.exibirMensagem(Mensagem.NADA_ENCONTRADO);
			}
		} catch (Exception e) {
			e.printStackTrace();
			ExibirMensagem.exibirMensagem(Mensagem.ERRO);
		}
	}
	
	public void imprimirRelatorioBensINSERVIVEL() {
		 
		try {
			List<EquipamentoInventario> equipamentoInventario = daoEquipamentoInventario.listar(EquipamentoInventario.class,
					" naoTombado is false and estado = 'INSERVIVEL' and localInventario.Inventario.id ='" + inventario.getId()+"'");

			if (equipamentoInventario.size()>0) {
				 

				HashMap parametro = new HashMap<>();
				parametro.put("IDINVENTARIO", inventario.getId());
				ChamarRelatorio ch = new ChamarRelatorio("bensInservivel.jasper", parametro,
						"relatório_" + "Reimpressão de Bens Ocioso ");
				Session sessions = manager.unwrap(Session.class);
				sessions.doWork(ch);

			} else {
				ExibirMensagem.exibirMensagem(Mensagem.NADA_ENCONTRADO);
			}
		} catch (Exception e) {
			e.printStackTrace();
			ExibirMensagem.exibirMensagem(Mensagem.ERRO);
		}
	}
	
	public void imprimirRelatorioBensRemovidos() {
		 
		try {
			List<EquipamentoInventario> equipamentoInventario = daoEquipamentoInventario.listarCodicaoLivre(EquipamentoInventario.class,
					" status is false and localInventario.Inventario.id ='" + inventario.getId()+"'");

			if (equipamentoInventario.size()>0) {
				 

				HashMap parametro = new HashMap<>();
				parametro.put("IDINVENTARIO", inventario.getId());
				ChamarRelatorio ch = new ChamarRelatorio("bensRemovido.jasper", parametro,
						"relatório_" + "Bens conferidos e removidos ");
				Session sessions = manager.unwrap(Session.class);
				sessions.doWork(ch);

			} else {
				ExibirMensagem.exibirMensagem(Mensagem.NADA_ENCONTRADO);
			}
		} catch (Exception e) {
			e.printStackTrace();
			ExibirMensagem.exibirMensagem(Mensagem.ERRO);
		}
	}
	
	
	public void imprimirRelatorioBensTombadosPorLocalidade() {
		 
		try {
			List<EquipamentoInventario> equipamentoInventario = daoEquipamentoInventario.listar(EquipamentoInventario.class,
					" localInventario = '"+localInventairo.getId()+"'");

			if (equipamentoInventario.size()>0) {
				 

				HashMap parametro = new HashMap<>();
				parametro.put("IDLOCALINVENTARIO", localInventairo.getId());
				ChamarRelatorio ch = new ChamarRelatorio("bensTombadosPorLocalidade.jasper", parametro,
						"relatório_" + "Relatório de Bens tombados por localidade ");
				Session sessions = manager.unwrap(Session.class);
				sessions.doWork(ch);

			} else {
				ExibirMensagem.exibirMensagem(Mensagem.NADA_ENCONTRADO);
			}
		} catch (Exception e) {
			e.printStackTrace();
			ExibirMensagem.exibirMensagem(Mensagem.ERRO);
		}
	}
	
	
	public void imprimirRelatorioBensNaoTombadosPorLocalidade() {
		 
		try {
			List<EquipamentoInventario> equipamentoInventario = daoEquipamentoInventario.listar(EquipamentoInventario.class,
					" naoTombado is true and localInventario = '"+localInventairo.getId()+"'");

			if (equipamentoInventario.size()>0) {
				 

				HashMap parametro = new HashMap<>();
				parametro.put("IDLOCALINVENTARIO", localInventairo.getId());
				ChamarRelatorio ch = new ChamarRelatorio("bensNaoTombadosPorLocalidade.jasper", parametro,
						"relatório_" + "Relatório de Bens tombados por localidade ");
				Session sessions = manager.unwrap(Session.class);
				sessions.doWork(ch);

			} else {
				ExibirMensagem.exibirMensagem(Mensagem.NADA_ENCONTRADO);
			}
		} catch (Exception e) {
			e.printStackTrace();
			ExibirMensagem.exibirMensagem(Mensagem.ERRO);
		}
	}
	
	
	
 
	public List<LocalInventario> completaLocalInventario(){
		List<Localidades> listLocal = new ArrayList<>(); 
		return daoLocalInventario.listar(LocalInventario.class, " Inventario = '"+inventario.getId()+"'");
		
	}
	
	public List<LocalInventario> completarLocal(String str) {
 
		List<LocalInventario> listLocalAdd = new ArrayList<>();
		for (LocalInventario at : completaLocalInventario()) {
			if (at.getLocal().getDescricao().toLowerCase().startsWith(str)) {
				listLocalAdd.add(at);
			}
		}
		return listLocalAdd;
	}
 
	
 

	public List<Inventario> completarInventario(String str) {
		List<Inventario> lisInventario = new ArrayList<>();
		lisInventario = daoInventario.listaComStatus(Inventario.class);
		List<Inventario> elecionado = new ArrayList<>();
		for (Inventario g : lisInventario) {
			if (g.getDescricao().toLowerCase().startsWith(str)) {
				elecionado.add(g);
			}
		}
		return elecionado;
	}

	public Inventario getInventario() {
		return inventario;
	}

	public void setInventario(Inventario inventario) {
		this.inventario = inventario;
	}

	public LocalInventario getLocalInventairo() {
		return localInventairo;
	}

	public void setLocalInventairo(LocalInventario localInventairo) {
		this.localInventairo = localInventairo;
	}
	
	
 
}
