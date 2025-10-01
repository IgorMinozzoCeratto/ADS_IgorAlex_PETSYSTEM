
package br.upf.projetojfprimefaces.facade;

import br.upf.projetojfprimefaces.entity.PerfilEntity;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Stateless
public class PerfilFacade extends AbstractFacade<PerfilEntity> {

    @PersistenceContext(unitName = "PetSystemPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public PerfilFacade() {
        super(PerfilEntity.class);
    }
    
}

