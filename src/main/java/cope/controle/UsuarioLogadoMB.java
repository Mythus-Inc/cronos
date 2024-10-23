package cope.controle;

import java.io.Serializable;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.enterprise.context.SessionScoped;
import javax.faces.bean.ManagedBean; 
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import ac.controle.UsuarioSessaoMB;
import ac.modelo.Permissao;
import ac.modelo.Pessoa;

import cope.modelo.Participante;
import dao.GenericDAO;


@SessionScoped
@Named("usuarioLogadoMB") 
public class UsuarioLogadoMB implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@Inject
	private GenericDAO<Participante> daoParticipantes;
	
	@Inject
	private GenericDAO<Permissao> daoPermissao;
	
	@Inject
	private UsuarioSessaoMB usuarioSessao;
	
	private Pessoa usuario;

	@PostConstruct
	public void inicializar()  {
		usuario = usuarioSessao.recuperarPessoa();
	}

	public Pessoa getUsuario() {
		if(usuario == null)
			usuario = usuarioSessao.recuperarPessoa();
		return usuario;
	}
	
	public String getFuncaoProjetoCope(Long idProjeto){
		try {
			 
			Participante participante = (Participante) daoParticipantes.listarCodicaoLivre(Participante.class, "pessoa.id = "+getUsuario().getId()+" AND projeto.id = "+idProjeto);
			return participante.getFuncao();			
		} catch (Exception e) {
			return "";
		}
	}
	
	public boolean validaPermissao(String permissao){
		 
		List<Permissao> permissoes = daoPermissao.listarCodicaoLivre(Permissao.class, "status is true and servidor.id = " + usuario.getId());
		for (Permissao p : permissoes) {
			if (p.getTipo().getDescricao().equals(permissao))
				return true;
		}
		return false;
	}
}
