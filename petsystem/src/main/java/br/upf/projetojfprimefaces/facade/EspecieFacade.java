
package br.upf.projetojfprimefaces.facade;

import br.upf.projetojfprimefaces.entity.EspecieEntity;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

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

