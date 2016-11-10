package in.krraghavendra.microservice.session;

import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

import org.hibernate.Session;

import in.krraghavendra.microservice.productcatalog.model.Util;

public class SessionListener implements HttpSessionListener{

	@Override
	public void sessionCreated(HttpSessionEvent event) {
		HttpSession session = event.getSession();
		session.setAttribute("hibernateSession", Util.getHibernateSession());
	}

	@Override
	public void sessionDestroyed(HttpSessionEvent event) {
		HttpSession session = event.getSession();
		Session hibernateSession = (Session)session.getAttribute("hibernateSession");
		Util.closeHibernateSession(hibernateSession);
	}

}
