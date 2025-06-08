package br.upf.projetojfprimefaces.facade;

import br.upf.projetojfprimefaces.entity.TutorEntity;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import java.util.ArrayList;
import java.util.List;

@Stateless
public class TutorFacade extends AbstractFacade<TutorEntity> {

    @PersistenceContext(unitName = "PetSystemPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public TutorFacade() {
        super(TutorEntity.class);
    }

    private List<TutorEntity> entityList;

    /**
     * Método responsável por buscar na base de dados todos os tutores cadastrados
     * @return 
     */
    public List<TutorEntity> buscarTodos() {
        entityList = new ArrayList<>();
        try {
            Query query = getEntityManager().
                    createQuery("SELECT t FROM TutorEntity t ORDER BY t.nome");
            entityList = (List<TutorEntity>) query.getResultList();
        } catch (Exception e) {
            System.out.println("Erro: " + e);
        }
        return entityList;
    }
    
    /**
     * Buscar um tutor por email e senha
     * @param email
     * @param senha
     * @return 
     */
    public TutorEntity buscarPorEmail(String email, String senha) {
        TutorEntity tutor = null;
        try {
            Query query = getEntityManager()
                    .createQuery("SELECT t FROM TutorEntity t WHERE t.email = :email AND t.senha = :senha");
            query.setParameter("email", email);
            query.setParameter("senha", senha);

            if (!query.getResultList().isEmpty()) {
                tutor = (TutorEntity) query.getSingleResult();
            }
        } catch (Exception e) {
            System.out.println("Erro: " + e);
        }
        return tutor;
    }
    
    /**
     * Buscar tutores por parte do nome
     * @param nome
     * @return 
     */
    public List<TutorEntity> buscarPorNome(String nome) {
        entityList = new ArrayList<>();
        try {
            Query query = getEntityManager()
                    .createQuery("SELECT t FROM TutorEntity t WHERE LOWER(t.nome) LIKE LOWER(:nome) ORDER BY t.nome");
            query.setParameter("nome", "%" + nome + "%");
            entityList = (List<TutorEntity>) query.getResultList();
        } catch (Exception e) {
            System.out.println("Erro: " + e);
        }
        return entityList;
    }
}
