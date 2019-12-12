/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ec.edu.monster.entidades;

import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Named;


@Named
@ApplicationScoped
public class ThemeService {
     
    private List themes;
     
    @PostConstruct
    public void init() {
        themes = new ArrayList<>();
    }
     
    public List getThemes() {
        return themes;
    } 
}