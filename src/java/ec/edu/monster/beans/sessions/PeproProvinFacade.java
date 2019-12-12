/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ec.edu.monster.beans.sessions;

import ec.edu.monster.entidades.PeproProvin;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author Diego
 */
@Stateless
public class PeproProvinFacade extends AbstractFacade<PeproProvin> {

    @PersistenceContext(unitName = "WebApplication8PU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public PeproProvinFacade() {
        super(PeproProvin.class);
    }
    
}
