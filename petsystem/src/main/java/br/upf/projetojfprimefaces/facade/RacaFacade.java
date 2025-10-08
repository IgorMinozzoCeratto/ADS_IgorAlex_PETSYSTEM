package br.upf.projetojfprimefaces.facade;

import br.upf.projetojfprimefaces.entity.EspecieEntity;
import br.upf.projetojfprimefaces.entity.RacaEntity;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import java.util.List;

@Stateless
public class RacaFacade extends AbstractFacade<RacaEntity> {

    @PersistenceContext(unitName = "PetSystemPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public RacaFacade() {
        super(RacaEntity.class);
    }
    
    public List<RacaEntity> findByEspecie(EspecieEntity especie) {
        return getEntityManager().createQuery("SELECT r FROM RacaEntity r WHERE r.especie = :especie", RacaEntity.class)
                .setParameter("especie", especie)
                .getResultList();
    }
}

