
package br.upf.projetojfprimefaces.facade;

import br.upf.projetojfprimefaces.entity.ConvenioEntity;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

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

