package org.integrallis.drools.genealogy;

import java.util.Collection;
import java.util.Iterator;

import org.drools.KnowledgeBase;
import org.drools.agent.KnowledgeAgent;
import org.drools.agent.KnowledgeAgentConfiguration;
import org.drools.agent.KnowledgeAgentFactory;
import org.drools.definition.type.FactType;
import org.drools.io.ResourceFactory;
import org.drools.runtime.StatefulKnowledgeSession;

public class GuvnorClient {

	public static void main(String[] args) {
		try {
			// Scanner and notifier work as services. They must be started
			ResourceFactory.getResourceChangeNotifierService().start();
			ResourceFactory.getResourceChangeScannerService().start();

			KnowledgeAgentConfiguration kaconf = KnowledgeAgentFactory.newKnowledgeAgentConfiguration();
			kaconf.setProperty("drools.agent.scanDirectories", "false"); 
			kaconf.setProperty("drools.agent.newInstance", "true");

			KnowledgeAgent kagent = KnowledgeAgentFactory.newKnowledgeAgent("test agent", kaconf);

			kagent.applyChangeSet(ResourceFactory.newClassPathResource("changeset.xml"));

			KnowledgeBase kbase = kagent.getKnowledgeBase();

			StatefulKnowledgeSession knowledgeSession = kbase.newStatefulKnowledgeSession();
			System.out.println("Created knowledge session...");

			FactType personType = kbase.getFactType("Genealogy", "Person");

			System.out.println("Retrieved FactType... (" + personType + ")");

			Object ruby = personType.newInstance();
			personType.set(ruby, "name", "Ruby Smith");

			Object roland = personType.newInstance();
			personType.set(roland, "name", "Roland Bodden");

			Object arlene = personType.newInstance();
			personType.set(arlene, "name", "Arlene Bodden");
			personType.set(arlene, "mother", ruby);
			personType.set(arlene, "father", roland);

			Object gil = personType.newInstance();
			personType.set(gil, "name", "Gil Sam");

			Object juanita = personType.newInstance();
			personType.set(juanita, "name", "Juanita Domaschko");

			Object johnGampa = personType.newInstance();
			personType.set(johnGampa, "name", "John Domaschko");

			Object johnSr = personType.newInstance();
			personType.set(johnSr, "name", "John S. Domaschko");
			personType.set(johnSr, "mother", juanita);
			personType.set(johnSr, "father", johnGampa);

			Object jane = personType.newInstance();
			personType.set(jane, "name", "Jane Domaschko Vest");

			Object brian = personType.newInstance();
			personType.set(brian, "name", "Brian Sam-Bodden");
			personType.set(brian, "mother", arlene);
			personType.set(brian, "father", gil);

			Object anne = personType.newInstance();
			personType.set(anne, "name", "Anne Sam-Bodden");
			personType.set(anne, "mother", jane);
			personType.set(anne, "father", johnSr);

			Object michael = personType.newInstance();
			personType.set(michael, "name", "Michael Sam-Bodden");
			personType.set(michael, "mother", anne);
			personType.set(michael, "father", brian);

			Object steve = personType.newInstance();
			personType.set(steve, "name", "Stephen Sam-Bodden");
			personType.set(steve, "mother", anne);
			personType.set(steve, "father", brian);

			Object johnJr = personType.newInstance();
			personType.set(johnJr, "name", "John V. Domaschko");
			personType.set(johnJr, "mother", jane);
			personType.set(johnJr, "father", johnSr);

			Object sarah = personType.newInstance();
			personType.set(sarah, "name", "Sarah Brown Domaschko");

			Object lauren = personType.newInstance();
			personType.set(lauren, "name", "Lauren Domaschko");
			personType.set(lauren, "mother", sarah);
			personType.set(lauren, "father", johnJr);

			Object[] everybody = new Object[] { ruby, roland, arlene, gil,
					juanita, johnGampa, johnSr, jane, brian, anne, michael,
					steve, johnJr, sarah, lauren };

			for (Object person : everybody) {
				knowledgeSession.insert(person);
			}

			System.out.println("Asserted all facts");

			knowledgeSession.fireAllRules();

			System.out.println("After fireAllRules");

			Collection<Object> allObjects = knowledgeSession.getObjects();

			System.out.println("Got " + allObjects.size() + " objects out");

			for (Iterator<Object> iterator = allObjects.iterator(); iterator.hasNext();) {
				Object relationship = iterator.next();
				System.out.println(relationship);
			}
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}

	}

}
