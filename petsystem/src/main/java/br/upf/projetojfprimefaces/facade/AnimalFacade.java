package br.upf.projetojfprimefaces.facade;

import br.upf.projetojfprimefaces.entity.AnimalEntity;
import br.upf.projetojfprimefaces.entity.TutorEntity;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import java.util.ArrayList;
import java.util.List;

@Stateless
public class AnimalFacade extends AbstractFacade<AnimalEntity> {

    @PersistenceContext(unitName = "PetSystemPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public AnimalFacade() {
        super(AnimalEntity.class);
    }

    private List<AnimalEntity> entityList;

    /**
     * Método responsável por buscar na base de dados todos os animais cadastrados
     * @return 
     */
    public List<AnimalEntity> buscarTodos() {
        entityList = new ArrayList<>();
        try {
            Query query = getEntityManager().
                    createQuery("SELECT a FROM AnimalEntity a ORDER BY a.nome");
            entityList = (List<AnimalEntity>) query.getResultList();
        } catch (Exception e) {
            System.out.println("Erro: " + e);
        }
        return entityList;
    }
    
    /**
     * Buscar animais por tutor
     * @param tutor
     * @return 
     */
    public List<AnimalEntity> buscarPorTutor(TutorEntity tutor) {
        entityList = new ArrayList<>();
        try {
            Query query = getEntityManager()
                    .createQuery("SELECT a FROM AnimalEntity a WHERE a.tutor = :tutor ORDER BY a.nome");
            query.setParameter("tutor", tutor);
            entityList = (List<AnimalEntity>) query.getResultList();
        } catch (Exception e) {
            System.out.println("Erro: " + e);
        }
        return entityList;
    }
    
    /**
     * Buscar animais por parte do nome
     * @param nome
     * @return 
     */
    public List<AnimalEntity> buscarPorNome(String nome) {
        entityList = new ArrayList<>();
        try {
            Query query = getEntityManager()
                    .createQuery("SELECT a FROM AnimalEntity a WHERE LOWER(a.nome) LIKE LOWER(:nome) ORDER BY a.nome");
            query.setParameter("nome", "%" + nome + "%");
            entityList = (List<AnimalEntity>) query.getResultList();
        } catch (Exception e) {
            System.out.println("Erro: " + e);
        }
        return entityList;
    }
    
    /**
     * Buscar animais por espécie
     * @param especie
     * @return 
     */
    public List<AnimalEntity> buscarPorEspecie(AnimalEntity.EspecieAnimal especie) {
        entityList = new ArrayList<>();
        try {
            Query query = getEntityManager()
                    .createQuery("SELECT a FROM AnimalEntity a WHERE a.especie = :especie ORDER BY a.nome");
            query.setParameter("especie", especie);
            entityList = (List<AnimalEntity>) query.getResultList();
        } catch (Exception e) {
            System.out.println("Erro: " + e);
        }
        return entityList;
    }
}
