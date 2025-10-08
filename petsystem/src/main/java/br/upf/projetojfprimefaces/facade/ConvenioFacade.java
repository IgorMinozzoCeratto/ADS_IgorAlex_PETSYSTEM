
package br.upf.projetojfprimefaces.facade;

import br.upf.projetojfprimefaces.entity.ConvenioEntity;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

@Stateless
public class ConvenioFacade extends AbstractFacade<ConvenioEntity> {

    @PersistenceContext(unitName = "PetSystemPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public ConvenioFacade() {
        super(ConvenioEntity.class);
    }
    
}

