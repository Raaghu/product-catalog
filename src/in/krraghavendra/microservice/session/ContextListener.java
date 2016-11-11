package in.krraghavendra.microservice.session;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.hibernate.Session;

import in.krraghavendra.microservice.productcatalog.model.Util;

public class ContextListener implements ServletContextListener{

	@Override
	public void contextInitialized(ServletContextEvent sce) {
		ServletContext context = sce.getServletContext();
		context.setAttribute("hibernateSession", Util.getHibernateSession());
		System.out.println("Context Started");
	}

	@Override
	public void contextDestroyed(ServletContextEvent sce) {
		ServletContext context = sce.getServletContext();
		Session hibernateSession = (Session)context.getAttribute("hibernateSession");
		Util.closeHibernateSession(hibernateSession);
		System.out.println("Context Closed");
	}

}
