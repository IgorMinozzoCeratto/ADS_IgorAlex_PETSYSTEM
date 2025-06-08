package br.upf.projetojfprimefaces.facade;

import br.upf.projetojfprimefaces.entity.FuncionarioEntity;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import java.util.ArrayList;
import java.util.List;

@Stateless
public class FuncionarioFacade extends AbstractFacade<FuncionarioEntity> {

    @PersistenceContext(unitName = "PetSystemPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public FuncionarioFacade() {
        super(FuncionarioEntity.class);
    }

    private List<FuncionarioEntity> entityList;

    /**
     * Método responsável por buscar na base de dados todos os funcionários cadastrados
     * @return 
     */
    public List<FuncionarioEntity> buscarTodos() {
        entityList = new ArrayList<>();
        try {
            Query query = getEntityManager().
                    createQuery("SELECT f FROM FuncionarioEntity f ORDER BY f.nome");
            entityList = (List<FuncionarioEntity>) query.getResultList();
        } catch (Exception e) {
            System.out.println("Erro: " + e);
        }
        return entityList;
    }
    
    /**
     * Buscar um funcionário por login e senha
     * @param login
     * @param senha
     * @return 
     */
    public FuncionarioEntity buscarPorLogin(String login, String senha) {
        FuncionarioEntity funcionario = null;
        try {
            Query query = getEntityManager()
                    .createQuery("SELECT f FROM FuncionarioEntity f WHERE f.login = :login AND f.senha = :senha");
            query.setParameter("login", login);
            query.setParameter("senha", senha);

            if (!query.getResultList().isEmpty()) {
                funcionario = (FuncionarioEntity) query.getSingleResult();
            }
        } catch (Exception e) {
            System.out.println("Erro: " + e);
        }
        return funcionario;
    }
    
    /**
     * Buscar funcionários por tipo
     * @param tipo
     * @return 
     */
    public List<FuncionarioEntity> buscarPorTipo(FuncionarioEntity.TipoFuncionario tipo) {
        entityList = new ArrayList<>();
        try {
            Query query = getEntityManager()
                    .createQuery("SELECT f FROM FuncionarioEntity f WHERE f.tipo = :tipo ORDER BY f.nome");
            query.setParameter("tipo", tipo);
            entityList = (List<FuncionarioEntity>) query.getResultList();
        } catch (Exception e) {
            System.out.println("Erro: " + e);
        }
        return entityList;
    }
}
