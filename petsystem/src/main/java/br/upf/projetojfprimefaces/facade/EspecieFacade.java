
package br.upf.projetojfprimefaces.facade;

import br.upf.projetojfprimefaces.entity.EspecieEntity;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

@Stateless
public class EspecieFacade extends AbstractFacade<EspecieEntity> {

    @PersistenceContext(unitName = "PetSystemPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public EspecieFacade() {
        super(EspecieEntity.class);
    }
    
}

