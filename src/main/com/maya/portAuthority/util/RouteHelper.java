package com.maya.portAuthority.util;

import com.amazon.speech.slu.Intent;
import com.amazon.speech.slu.Slot;
import com.amazon.speech.speechlet.Session;
import com.maya.portAuthority.InvalidInputException;
import com.maya.portAuthority.api.Message;
import com.maya.portAuthority.api.TrueTimeMessageParser;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author brown
 *
 */
public class RouteHelper extends DataHelper {
	public static final String INTENT_NAME="RouteBusIntent";
	public static final String NAME = "Route";
	public static final String ROUTE_NAME = "Route_Name";
	public static final String SPEECH ="Which bus line would you like arrival information for?";

	private static Logger log = LoggerFactory.getLogger(RouteHelper.class);
	
	//private Intent intent;
	private Session session;

	public RouteHelper(Session s){
		log.trace("constructor");
		this.session=s;
	}

	public void putValuesInSession(Intent intent) throws InvalidInputException{
		log.trace("putValuesInSession"+intent.getName());

		String routeID=getValueFromIntentSlot(intent);

		if (routeID!=null){
			routeID=routeID.replaceAll("\\s+","");
			Message route = getMatchedRoute(routeID.toUpperCase());
			if (route!=null){
				session.setAttribute(NAME, route.getRouteID());
				session.setAttribute(ROUTE_NAME, route.getRouteName()); 	
			} else {
				log.error("putValuesInSession:"+intent.getName()+" route is null");
				throw new InvalidInputException("Route does not match API", "Could not find the bus line "+routeID+"."+ SPEECH);
			}
		}
	} 

	public String getValueFromIntentSlot(Intent intent) {
		log.trace("getValuesInSession"+intent.getName());
		Slot slot = intent.getSlot(NAME);
		return (slot!=null) ? slot.getValue() : null;
	}
	
	public String getValueFromSession(){
		log.trace("getValueFromSession");
		if (session.getAttributes().containsKey(NAME)) {
			return (String) session.getAttribute(NAME);
		} else {
			return null;
		}
	}
	
	public String getName(){
		return NAME;
	}
	
	public  String getIntentName(){
		return INTENT_NAME;
	}
	
	public  String getSpeech(){
		return SPEECH;
	}
	
	private Message getMatchedRoute(String routeID){
		List<Message> routes = TrueTimeMessageParser.getRoutes();
		Iterator<Message> iterator = routes.iterator();
		while (iterator.hasNext()){
			Message element=(Message)iterator.next();
			if (element.getMessageType().equalsIgnoreCase("error")){
				return null;
			}
			if (element.getMessageType().equalsIgnoreCase("route")){		
				if (routeID.equalsIgnoreCase(element.getRouteID())){
					return element;
				}
			}
		}
		return null;
	}
	
	
}
