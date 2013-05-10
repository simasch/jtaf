package org.netbeans.rest.application.config;

import java.util.Set;
import javax.ws.rs.core.Application;

@javax.ws.rs.ApplicationPath("res")
public class ApplicationConfig extends Application {

    @Override
    public Set<Class<?>> getClasses() {
        return getRestResourceClasses();
    }

    /**
     * Do not modify this method. It is automatically generated by NetBeans REST
     * support.
     */
    private Set<Class<?>> getRestResourceClasses() {
        Set<Class<?>> resources = new java.util.HashSet<Class<?>>();
        resources.add(ch.jtaf.boundry.CategoryResource.class);
        resources.add(ch.jtaf.boundry.ClubResource.class);
        resources.add(ch.jtaf.boundry.CompetitionResource.class);
        resources.add(ch.jtaf.boundry.AthleteResource.class);
        resources.add(ch.jtaf.boundry.SeriesResource.class);
        resources.add(ch.jtaf.boundry.EventResource.class);
        try {
            Class<?> jacksonProvider = Class.forName("org.codehaus.jackson.jaxrs.JacksonJsonProvider");
            resources.add(jacksonProvider);
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(getClass().getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        return resources;
    }
}
