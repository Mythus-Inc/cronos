package protocolo.controle;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.event.AjaxBehaviorEvent;

import ac.controle.UsuarioSessaoMB;
import ac.modelo.AlunoTurma;
import ac.modelo.Pessoa;
import base.modelo.Aluno;
import protocolo.modelo.Protocolo;
import protocolo.modelo.Requerimento;
import util.CriptografiaSenha;
import util.ExibirMensagem;
import util.Mensagem;
import util.ValidacoesGerirUsuarios;



@SessionScoped
@ManagedBean
public class BuscarDadosAlunoMBB {

	private Aluno aluno;
	private AlunoTurma turma;
//	private DAOGenerico dao;
	private ValidacoesGerirUsuarios validacoesGerirUsuarios;
	private UsuarioSessaoMB usuarioSessao;
	private List<Requerimento> listRequerimento;
	private List<Pessoa> listPessoa;
	private List<Protocolo> listProtocolo;
	private Protocolo protocolo;
	private String senha;
	private String senha2;
	private boolean controle;

	public BuscarDadosAlunoMBB() {
		
		aluno = new Aluno();
	//	dao = new DAOGenerico();
		validacoesGerirUsuarios = new ValidacoesGerirUsuarios();
		usuarioSessao = new UsuarioSessaoMB();
		senha = "";
		senha2 = "";
		listRequerimento = new ArrayList<>();
		protocolo = new Protocolo();
		listProtocolo = new ArrayList<>();
		listPessoa = new ArrayList<>();
		
	 //   preencherRequerimento();
		preencherAluno();
		preencherTurma();
		
	}
	
public void salvar(){
	
	
	
	//Pessoa pessoaInserir =(Pessoa) dao.buscarPorId(Pessoa.class, aluno.getId());
	protocolo.setData(new Date());
	
	//protocolo.setPessoa(pessoaInserir);
//	protocolo.setPessoa(aluno);
	//protocolo.setDataResultado(dataResultado);  ? pergunta na lista 
//	protocolo.setStatus(status);
//	protocolo.setSituacao(situacao);
//	protocolo.setObservacoesAnexo(observacoesAnexo);
//	protocolo.getParecerFinal();
//	protocolo.setDataFinalizacao(dataFinalizacao);
//	protocolo.setOrigem(origem);
	
	//dao.inserir(protocolo);
	//ExibirMensagem.exibirMensagem(Mensagem.SUCESSO);
	//FecharDialog.fecharDialogProtocolo();
	criarNovoObjeto();
	carregar();
	
	

}


public void controle(){
	//Requerimento rr = (Requerimento)dao.buscarPorId(Requerimento.class, protocolo.getRequerimento().getId());
//	controle = ! rr.isCienciaEmailProfessores();

}

public void carregar(){
//	listProtocolo = dao.listar(Protocolo.class);
}

public void criarNovoObjeto(){
	protocolo = new Protocolo();
}



	public List<Pessoa> completeText(String query) {

	//	listPessoa = dao.listar(Pessoa.class);

		List<Pessoa> results = new ArrayList<>();
		for (Pessoa e : listPessoa) {
			if (e.getNome().toLowerCase().startsWith(query)) {
				results.add(e);
			}
		}


		return results;
	}
	
	

	
	
	
	// @PostConstruct
    // invoca um método toda vez que o managed beans for criado. 
	

	public void alterarAluno() {
		try {
			aluno.setUsuario(aluno.getUsuario().replace("'", "").replace("=", "").replace("<", "").replace("&", ""));
			if ((validacoesGerirUsuarios.buscarUsuarios(aluno))
					&& (validacoesGerirUsuarios.buscarUsuarioAlterar(aluno))) {
				ExibirMensagem.exibirMensagem(Mensagem.USUARIO);
			} else {
				if (senha != "") {
					aluno.setSenha(senha);
					aluno.setSenha(CriptografiaSenha.criptografar(aluno.getSenha()));
				}
	//			dao.alterar(aluno);
				ExibirMensagem.exibirMensagem(Mensagem.SUCESSO);
				preencherAluno();
			}
		} catch (Exception e) {
			ExibirMensagem.exibirMensagem(Mensagem.ERRO);
			System.err.println("Erro alterarAluno");
			e.printStackTrace();
		}
	}

	public void preencherAluno() {
		try {
			aluno = (Aluno) usuarioSessao.recuperarAluno();
		} catch (Exception e) {
			System.err.println("Erro preencherAluno");
			e.printStackTrace();
		}
	}
	public void preencherTurma(){
		try{
			
			//turma = (AlunoTurma) dao.buscarCondicao(AlunoTurma.class, "id_pessoa", aluno.getId());
		}catch (Exception e) {
		System.err.println("Erro preencherCurso");
		}
            
		
	}

	public Aluno getAluno() {
		return aluno;
	}

	public void setAluno(Aluno aluno) {
		this.aluno = aluno;
	}

	public String getSenha() {
		return senha;
	}

	public void setSenha(String senha) {
		this.senha = senha;
	}

	public AlunoTurma getTurma() {
		return turma;
	}

	public void setTurma(AlunoTurma turma) {
		this.turma = turma;
	}

	public String getSenha2() {
		return senha2;
	}

	public void setSenha2(String senha2) {
		this.senha2 = senha2;
	}


	public List<Requerimento> getListRequerimento() {
	//	listRequerimento = dao.listar(Requerimento.class);
		
		return listRequerimento;
	}



	public void setListRequerimento(List<Requerimento> listRequerimento) {
		this.listRequerimento = listRequerimento;
	}



	public Protocolo getProtocolo() {
		return protocolo;
	}



	public boolean isControle() {
		return controle;
	}

	public void setControle(boolean controle) {
		this.controle = controle;
	}

	public List<Protocolo> getListProtocolo() {
	//	listProtocolo = dao.listar(Protocolo.class);
		
		return listProtocolo;
	}

	public List<Pessoa> getListPessoa() {
		return listPessoa;
	}

	public void setListPessoa(List<Pessoa> listPessoa) {
		this.listPessoa = listPessoa;
	}

	public void setListProtocolo(List<Protocolo> listProtocolo) {
		this.listProtocolo = listProtocolo;
	}

	public void setProtocolo(Protocolo protocolo) {
		this.protocolo = protocolo;
	}


	
}
