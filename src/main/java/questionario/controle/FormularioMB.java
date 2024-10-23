package questionario.controle;

import java.io.IOException;
import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import ac.controle.UsuarioSessaoFormularioMB;
import ac.controle.UsuarioSessaoMB;
import ac.modelo.AlunoTurma;
import ac.modelo.Permissao;
import ac.modelo.Pessoa;
import ac.services.AlunoService;
import base.modelo.Aluno;
import base.modelo.Curso;
import base.service.AlunoTurmaService;
import dao.GenericDAO;
import questionario.modelo.Email;
import questionario.modelo.Formulario;
import questionario.service.EmailService;
import questionario.service.FormularioService;
import util.ExibirMensagem;
import util.Mensagem;

@SessionScoped
@Named("FormularioMB")
public class FormularioMB implements Serializable {

	private static final long serialVersionUID = 1L;

	private Formulario formulario;
	private Curso curso;
	private Aluno aluno = new Aluno();
	private String cpf;
	private List<Curso> cursos;
	private Email email;
	private boolean controleP1 = true;
	private boolean controleP2 = true;
	private boolean controleP3 = true;
	private boolean controleL1;
	private boolean cont = false;
	private boolean controleL2;

	@Inject
	private GenericDAO<Aluno> daoAluno;

	@Inject
	private GenericDAO<AlunoTurma> daoAlunoTurma;



	@Inject
	private UsuarioSessaoFormularioMB usuarioSessaoFormulario;

	@Inject
	private GenericDAO<Curso> daoCurso;

	@Inject
	private FormularioService formularioService;

	@Inject
	private EmailService emailService;

	@Inject
	private AlunoTurmaService alunoTurmaService;
	
	 
	@PostConstruct
	public void inicializar() {
	
		cursos = new ArrayList<>();
		formulario = new Formulario();
		email = new Email();
		controleL1 = true;
		controleL2 = true;
		curso = new Curso();


	}

	public void controle1() {
 
			if (formulario.getIp1().equals("sim")) {

				controleP1 = false;
			} else {
				controleP1 = true;
			}
		
//		if (formulario.getIp1().equals("sim")) {
//			cont = true;
//		}else{
//			cont = false;
//		}
		
		

	}
	
	public void verf() throws IOException{
	
		try {
			if(aluno.getId() == null){
			FacesContext.getCurrentInstance().getExternalContext().redirect("inicio.jsf"); 
			System.out.println("if");
			}
		} catch (IOException e) { 
			e.printStackTrace();
			if(e.equals("java.lang.IllegalStateException")){
				FacesContext.getCurrentInstance().getExternalContext().redirect("inicio.jsf"); 
			}
		}
		
		
	}

	public void controle2() {
		if (formulario.getIp2().equals("sim")) {

			controleP2 = false;
		} else {
			controleP2 = true;
		}
	}

	public void controle3() {
		if (formulario.getIp3().equals("sim")) {

			controleP3 = false;
		} else {
			controleP3 = true;
		}
	}

	public void verificarCpf() {
		List<Aluno> listAluno = new ArrayList<>();
		listAluno = daoAluno.listar(Aluno.class, " cpf ='" + cpf + "'");

		try {
			if (listAluno.size() > 0) {

				for (Aluno a : listAluno) {

					List<AlunoTurma> listAlunoTurma = new ArrayList<>();
					listAlunoTurma = daoAlunoTurma.listar(AlunoTurma.class,
							" liberado = true and aluno.id=" + a.getId());
					if (listAlunoTurma.size() > 0) {

						
						FacesContext.getCurrentInstance().getExternalContext().redirect("formulario.jsf");
						aluno = new Aluno();
						aluno = a;

					} else {
						ExibirMensagem.exibirMessageWarn();
					}
				}
			} else {
				ExibirMensagem.exibirMessageFatal();

			}

		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	public void voltar() throws IOException {
		FacesContext.getCurrentInstance().getExternalContext().redirect("inicio.jsf");
	}

	public List<Curso> completarCursos(String str) { 

		List<AlunoTurma> alunoTurma = new ArrayList<>();
		alunoTurma = daoAlunoTurma.listar(AlunoTurma.class,
				" id_pessoa='" + aluno.getId() + "' and situacao = '5' and liberado = true");

		cursos.clear();
		for (AlunoTurma p : alunoTurma) {
			cursos.add(p.getTurma().getCurso());
		}

		List<Curso> cursosSelecionados = new ArrayList<>();
		for (Curso cur : cursos) {
			if (cur.getNome().toLowerCase().startsWith(str)) {
				cursosSelecionados.add(cur);
			}
		}
		return cursosSelecionados;
	}

	public void criarNovoObjeto() {
		formulario = new Formulario();
		aluno = new Aluno();
		email = new Email();
	}

	public void salvar() {

		try {
			List<AlunoTurma> alunoAlterarLiberar = new ArrayList<>();
			alunoAlterarLiberar = daoAlunoTurma.listar(AlunoTurma.class,
					" liberado = true and turma.curso.id= " + curso.getId() + " and aluno.id= " + aluno.getId());

			for (AlunoTurma a : alunoAlterarLiberar) {

				formulario.setDataResposta(new Date());
				formulario.setStatus(true);
				formulario.setAlunoTurma(a);
				formularioService.inserirAlterar(formulario);

				a.setLiberado(false); // buscar por isso aqui /| ou deixar o
										// liberar no aluno
				alunoTurmaService.inserirAlterar(a);
			}

			criarNovoObjeto();
			FacesContext.getCurrentInstance().getExternalContext().redirect("../../j_spring_security_logout");
			ExibirMensagem.exibirMensagem(Mensagem.QUESTIONARIO);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public boolean isCont() {
		
		System.out.println("valor do cont" + cont);
		return cont;
	}

	public void setCont(boolean cont) {
		this.cont = cont;
	}

	public Aluno getAluno() {
		return aluno;
	}

	public String getCpf() {
		return cpf;
	}

	public void setCpf(String cpf) {
		this.cpf = cpf;
	}

	public boolean isControleP1() {
		return controleP1;
	}

	public void setControleP1(boolean controleP1) {
		this.controleP1 = controleP1;
	}

	public boolean isControleL1() {
		return controleL1;
	}

	public void setControleL1(boolean controleL1) {
		this.controleL1 = controleL1;
	}

	public boolean isControleP2() {
		return controleP2;
	}

	public Curso getCurso() {
		return curso;
	}

	public void setCurso(Curso curso) {
		this.curso = curso;
	}

	public void setControleP2(boolean controleP2) {
		this.controleP2 = controleP2;
	}

	public boolean isControleP3() {
		return controleP3;
	}

	public boolean isControleL2() {
		return controleL2;
	}

	public void setControleL2(boolean controleL2) {
		this.controleL2 = controleL2;
	}

	public void setControleP3(boolean controleP3) {
		this.controleP3 = controleP3;
	}

	public void setAluno(Aluno aluno) {
		this.aluno = aluno;
	}

	public Formulario getFormulario() {
		return formulario;
	}

	public void setFormulario(Formulario formulario) {
		this.formulario = formulario;
	}

}
