package com.es.rulesengine;

import java.net.URL;

import org.json.JSONObject;
import org.kie.api.KieBase;
import org.kie.api.KieBaseConfiguration;
import org.kie.api.KieServices;
import org.kie.api.builder.KieBuilder;
import org.kie.api.builder.KieFileSystem;
import org.kie.api.builder.KieModule;
import org.kie.api.builder.KieScanner;
import org.kie.api.builder.Message;
import org.kie.api.builder.ReleaseId;
import org.kie.api.builder.Results;
import org.kie.api.builder.model.KieBaseModel;
import org.kie.api.builder.model.KieModuleModel;
import org.kie.api.builder.model.KieSessionModel;
import org.kie.api.conf.EqualityBehaviorOption;
import org.kie.api.conf.EventProcessingOption;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;
import org.kie.api.runtime.conf.ClockTypeOption;
import org.kie.internal.KnowledgeBase;
import org.kie.internal.agent.*;
import org.kie.internal.builder.InternalKieBuilder;
import org.kie.internal.io.ResourceFactory;

import com.es.rulesengine.model.*;

public class RulesEngine {

	private static String newline = System.getProperty("line.separator");

	public static void main(String[] args) {

		// Test xml convert

		/*
		 * String xmlMessage =
		 * "<?xml version='1.0' encoding='UTF-8'?><package name='com.es.rulesengine'  xmlns='http://drools.org/drools-5.0'           xmlns:xs='http://www.w3.org/2001/XMLSchema-instance'          xs:schemaLocation='http://drools.org/drools-5.0 drools-5.0.xsd' >    <import name='com.sample.DroolsTest.Message' />    <rule name='Hello World'><lhs>    <pattern identifier='m' object-type='Message' > <expr> status == Message.HELLO </expr> <expr> myMessage : message </expr>    </pattern></lhs><rhs>    System.out.println( myMessage );    m.setMessage( 'Goodbye cruel world' );    m.setStatus( Message.GOODBYE );    update( m );</rhs></rule><rule name='GoodBye'><lhs>    <pattern object-type='Message' > <expr> status == Message.GOODBYE </expr> <expr> myMessage : message </expr>    </pattern></lhs><rhs>    System.out.println( myMessage );</rhs></rule></package>"
		 * ; String drl = DroolsConvert.xmlStringToDrl(xmlMessage);
		 * System.out.println(drl);
		 */

		// String message = "<?xml version='1.0' encoding='UTF-8'?><package
		// name='com.sample' xmlns='http://drools.org/drools-5.2'
		// xmlns:xs='http://www.w3.org/2001/XMLSchema-instance'
		// xs:schemaLocation='http://drools.org/drools-5.2
		// drools.org/drools-5.2.xsd'> <import
		// name='com.sample.DroolsTest.Message' /> <rule name='Hello
		// World'><lhs> <pattern identifier='m' object-type='Message' > <expr>
		// status == Message.HELLO </expr> <expr> myMessage : message </expr>
		// </pattern></lhs><rhs> System.out.println( myMessage ); m.setMessage(
		// 'Goodbye cruel world' ); m.setStatus( Message.GOODBYE ); update( m
		// );</rhs></rule><rule name='GoodBye'><lhs> <pattern
		// object-type='Message' > <expr> status == Message.GOODBYE </expr>
		// <expr> myMessage : message </expr> </pattern></lhs><rhs>
		// System.out.println( myMessage );</rhs></rule></package>";
		/*
		 * String xmlMessage =
		 * "<?xml version='1.0' encoding='UTF-8'?><package name='com.sample'  xmlns:xs='http://www.w3.org/2001/XMLSchema-instance' >    <import name='com.sample.DroolsTest.Message' />    <rule name='Hello World'><lhs>    <pattern identifier='m' object-type='Message' > <expr> status == Message.HELLO </expr> <expr> myMessage : message </expr>    </pattern></lhs><rhs>    System.out.println( myMessage );    m.setMessage( 'Goodbye cruel world' );    m.setStatus( Message.GOODBYE );    update( m );</rhs></rule><rule name='GoodBye'><lhs>    <pattern object-type='Message' > <expr> status == Message.GOODBYE </expr> <expr> myMessage : message </expr>    </pattern></lhs><rhs>    System.out.println( myMessage );</rhs></rule></package>"
		 * ;
		 * 
		 * xmlMessage =
		 * " <rule name='simple_rule'>  <rule-attribute name='salience' value='10' />  <rule-attribute name='no-loop' value='true' />  <rule-attribute name='agenda-group' value='agenda-group' />  <rule-attribute name='activation-group' value='activation-group' />    <lhs>      <pattern identifier='cheese' object-type='Cheese'>          <from>              <accumulate>                  <pattern object-type='Person'></pattern>                  <init>                      int total = 0;                  </init>                  <action>                      total += $cheese.getPrice();                  </action>                  <result>                      new Integer( total ) );                  </result>              </accumulate>          </from>      </pattern>        <pattern identifier='max' object-type='Number'>          <from>              <accumulate>                  <pattern identifier='cheese' object-type='Cheese'></pattern>                  <external-function evaluator='max' expression='$price'/>              </accumulate>          </from>      </pattern>  </lhs>  <rhs>      list1.add( $cheese );  </rhs>  </rule>"
		 * ;
		 * 
		 * xmlMessage =
		 * "<?xml version='1.0' encoding='UTF-8'?>    <package name='com.sample'           xmlns='http://drools.org/drools-4.0'           xmlns:xs='http://www.w3.org/2001/XMLSchema-instance'           xs:schemaLocation='http://drools.org/drools-4.0 drools-4.0.xsd'>    <import name='java.util.HashMap' />  <import name='org.drools.*' />    <global identifier='x' type='com.sample.X' />  <global identifier='yada' type='com.sample.Yada' />    <function return-type='void' name='myFunc'>      <parameter identifier='foo' type='Bar' />      <parameter identifier='bada' type='Bing' />        <body>       System.out.println('hello world');      </body>  </function>    <rule name='simple_rule'>  <rule-attribute name='salience' value='10' />  <rule-attribute name='no-loop' value='true' />  <rule-attribute name='agenda-group' value='agenda-group' />  <rule-attribute name='activation-group' value='activation-group' />    <lhs>      <pattern identifier='foo2' object-type='Bar' >              <or-constraint-connective>                  <and-constraint-connective>                      <field-constraint field-name='a'>                          <or-restriction-connective>                              <and-restriction-connective>                                  <literal-restriction evaluator='max' value='60' />                                  <literal-restriction evaluator='min' value='70' />                              </and-restriction-connective>                              <and-restriction-connective>                                  <literal-restriction evaluator='min' value='50' />                                  <literal-restriction evaluator='max' value='55' />                              </and-restriction-connective>                          </or-restriction-connective>                      </field-constraint>                        <field-constraint field-name='a3'>                          <literal-restriction evaluator='==' value='black' />                      </field-constraint>                  </and-constraint-connective>                    <and-constraint-connective>                      <field-constraint field-name='a'>                          <literal-restriction evaluator='==' value='40' />                      </field-constraint>                        <field-constraint field-name='a3'>                          <literal-restriction evaluator='==' value='pink' />                      </field-constraint>                  </and-constraint-connective>                    <and-constraint-connective>                      <field-constraint field-name='a'>                          <literal-restriction evaluator='==' value='12'/>                      </field-constraint>                        <field-constraint field-name='a3'>                          <or-restriction-connective>                              <literal-restriction evaluator='==' value='yellow'/>                              <literal-restriction evaluator='==' value='blue' />                          </or-restriction-connective>                      </field-constraint>                  </and-constraint-connective>              </or-constraint-connective>          </pattern>            <not>              <pattern object-type='Person'>                  <field-constraint field-name='likes'>                      <variable-restriction evaluator='==' identifier='type'/>                  </field-constraint>              </pattern>                <exists>                  <pattern object-type='Person'>                      <field-constraint field-name='likes'>                          <variable-restriction evaluator='==' identifier='type'/>                      </field-constraint>                  </pattern>                              </exists>          </not>            <or-conditional-element>              <pattern identifier='foo3' object-type='Bar' >                  <field-constraint field-name='a'>                      <or-restriction-connective>                          <literal-restriction evaluator='==' value='3' />                          <literal-restriction evaluator='==' value='4' />                      </or-restriction-connective>                  </field-constraint>                  <field-constraint field-name='a3'>                      <literal-restriction evaluator='==' value='hello' />                  </field-constraint>                  <field-constraint field-name='a4'>                      <literal-restriction evaluator='==' value='null' />                  </field-constraint>              </pattern>                <pattern identifier='foo4' object-type='Bar' >                  <field-binding field-name='a' identifier='a4' />                  <field-constraint field-name='a'>                      <literal-restriction evaluator='!=' value='4' />                      <literal-restriction evaluator='!=' value='5' />                  </field-constraint>              </pattern>          </or-conditional-element>            <pattern identifier='foo5' object-type='Bar' >              <field-constraint field-name='b'>                  <or-restriction-connective>                      <return-value-restriction evaluator='==' >a4 + 1</return-value-restriction>                      <variable-restriction evaluator='>' identifier='a4' />                      <qualified-identifier-restriction evaluator='=='>                          org.drools.Bar.BAR_ENUM_VALUE                      </qualified-identifier-restriction>                  </or-restriction-connective>              </field-constraint>                      </pattern>            <pattern identifier='foo6' object-type='Bar' >              <field-binding field-name='a' identifier='a4' />              <field-constraint field-name='b'>                  <literal-restriction evaluator='==' value='6' />              </field-constraint>          </pattern>    </lhs>   <rhs>      if ( a == b ) {        assert( foo3 );      } else {        retract( foo4 );      }      System.out.println( a4 );     </rhs>  </rule>    </package>"
		 * ; //message =
		 * "<?xml version='1.0' encoding='UTF-8'?>    <package name='com.sample'           xmlns='http://drools.org/drools-4.0'           xmlns:xs='http://www.w3.org/2001/XMLSchema-instance'           xs:schemaLocation='http://drools.org/drools-4.0 drools-4.0.xsd'>    <import name='java.util.HashMap' />  <import name='org.drools.*' />    <global identifier='x' type='com.sample.X' />  <global identifier='yada' type='com.sample.Yada' />    <function return-type='void' name='myFunc'>      <parameter identifier='foo' type='Bar' />      <parameter identifier='bada' type='Bing' />        <body>       System.out.println('hello world');      </body>  </function>    <rule name='simple_rule'>  <rule-attribute name='salience' value='10' />  <rule-attribute name='no-loop' value='true' />  <rule-attribute name='agenda-group' value='agenda-group' />  <rule-attribute name='activation-group' value='activation-group' />    <lhs>      <pattern identifier='foo2' object-type='Bar' >              <or-constraint-connective>                  <and-constraint-connective>                      <field-constraint field-name='a'>                          <or-restriction-connective>                              <and-restriction-connective>                                  <literal-restriction evaluator='>' value='60' />                                  <literal-restriction evaluator='<' value='70' />                              </and-restriction-connective>                              <and-restriction-connective>                                  <literal-restriction evaluator='<' value='50' />                                  <literal-restriction evaluator='>' value='55' />                              </and-restriction-connective>                          </or-restriction-connective>                      </field-constraint>                        <field-constraint field-name='a3'>                          <literal-restriction evaluator='==' value='black' />                      </field-constraint>                  </and-constraint-connective>                    <and-constraint-connective>                      <field-constraint field-name='a'>                          <literal-restriction evaluator='==' value='40' />                      </field-constraint>                        <field-constraint field-name='a3'>                          <literal-restriction evaluator='==' value='pink' />                      </field-constraint>                  </and-constraint-connective>                    <and-constraint-connective>                      <field-constraint field-name='a'>                          <literal-restriction evaluator='==' value='12'/>                      </field-constraint>                        <field-constraint field-name='a3'>                          <or-restriction-connective>                              <literal-restriction evaluator='==' value='yellow'/>                              <literal-restriction evaluator='==' value='blue' />                          </or-restriction-connective>                      </field-constraint>                  </and-constraint-connective>              </or-constraint-connective>          </pattern>            <not>              <pattern object-type='Person'>                  <field-constraint field-name='likes'>                      <variable-restriction evaluator='==' identifier='type'/>                  </field-constraint>              </pattern>                <exists>                  <pattern object-type='Person'>                      <field-constraint field-name='likes'>                          <variable-restriction evaluator='==' identifier='type'/>                      </field-constraint>                  </pattern>                              </exists>          </not>            <or-conditional-element>              <pattern identifier='foo3' object-type='Bar' >                  <field-constraint field-name='a'>                      <or-restriction-connective>                          <literal-restriction evaluator='==' value='3' />                          <literal-restriction evaluator='==' value='4' />                      </or-restriction-connective>                  </field-constraint>                  <field-constraint field-name='a3'>                      <literal-restriction evaluator='==' value='hello' />                  </field-constraint>                  <field-constraint field-name='a4'>                      <literal-restriction evaluator='==' value='null' />                  </field-constraint>              </pattern>                <pattern identifier='foo4' object-type='Bar' >                  <field-binding field-name='a' identifier='a4' />                  <field-constraint field-name='a'>                      <literal-restriction evaluator='!=' value='4' />                      <literal-restriction evaluator='!=' value='5' />                  </field-constraint>              </pattern>          </or-conditional-element>            <pattern identifier='foo5' object-type='Bar' >              <field-constraint field-name='b'>                  <or-restriction-connective>                      <return-value-restriction evaluator='==' >a4 + 1</return-value-restriction>                      <variable-restriction evaluator='>' identifier='a4' />                      <qualified-identifier-restriction evaluator='=='>                          org.drools.Bar.BAR_ENUM_VALUE                      </qualified-identifier-restriction>                  </or-restriction-connective>              </field-constraint>                      </pattern>            <pattern identifier='foo6' object-type='Bar' >              <field-binding field-name='a' identifier='a4' />              <field-constraint field-name='b'>                  <literal-restriction evaluator='==' value='6' />              </field-constraint>          </pattern>    </lhs>   <rhs>      if ( a == b ) {        assert( foo3 );      } else {        retract( foo4 );      }      System.out.println( a4 );     </rhs>  </rule>    </package>"
		 * ;
		 * 
		 * String drl = DroolsConvert.xmlStringToDrl(xmlMessage);
		 * 
		 * System.out.println(drl);
		 */

		/********** MESSAGES ***********/
		boolean active = false;

		if (active) {

			String broker = "tcp://192.168.160.122";
			String clientID = "Drools";

			MessageClient mc = new MessageClient(broker, clientID);
			mc.runClient();

			int qos = 0; // Quality of service. 0, 1 or 2.
			String topic = "notify"; // "T1";
			// mc.subscribeTopic(topic,qos);

			String rulesTopic = "rules";
			mc.subscribeTopic(rulesTopic, qos);

			// mc.publishMessage(topic,"Teste Drools",qos); //932032424

			JSONObject obj = new JSONObject(
					"{'operation':'wamessage','parameters':{ 'destination': 351939777059,'scheduleTime': '2012-04-23T18:25:43.511Z','message': 'teste!!','responseTopic': 'NotifyResponse','originalId': '1'}}");

			JSONObject obj2 = obj.getJSONObject("parameters");

			// JSONObject obj = new JSONObject("{'operation': 'SMS',
			// 'destination': 932032424}");
			mc.publishMessage(topic, obj.toString(), qos);
			// mc.publishMessage(topic, obj.toString(), qos);
			/*
			 * mc.publishMessage(topic, obj.toString(), qos);
			 * mc.publishMessage(topic, obj.toString(), qos);
			 * mc.publishMessage(topic, obj.toString(), qos);
			 * mc.publishMessage(topic, obj.toString(), qos);
			 * mc.publishMessage(topic, obj.toString(), qos);
			 * mc.publishMessage(topic, obj.toString(), qos);
			 * mc.publishMessage(topic, obj.toString(), qos);
			 * mc.publishMessage(topic, obj.toString(), qos);
			 */

			System.out.println("Message sent!");

		}
		/*******************************/

		/*********** RULES *************/
		boolean rules = true;

		if (rules) {

			try {

				// KieServices ks = KieServices.Factory.get();
				// ReleaseId releaseId1 = ks.newReleaseId( "org.kie", "drools",
				// "1" );
				// KieModule km = createAndDeployJar( ks, releaseId1, header );
				// KieContainer kc = ks.newKieContainer( km.getReleaseId() );

				

				// load up the knowledge base
				KieServices ks = KieServices.Factory.get();

				//ReleaseId releaseID = ks.newReleaseId("com.es.rulesengine", "RulesEngineService", "0.0.1-SNAPSHOT");

				KieModuleModel kieModuleModel = ks.newKieModuleModel();

				KieBaseModel kieBaseModel1 = kieModuleModel.newKieBaseModel("rules").setDefault(true)
						.setEqualsBehavior(EqualityBehaviorOption.EQUALITY)
						.setEventProcessingMode(EventProcessingOption.CLOUD);

				KieSessionModel kieSessionModel1 = kieBaseModel1.newKieSessionModel("ksession-rules").setDefault(true)
						.setType(KieSessionModel.KieSessionType.STATEFUL).setClockType(ClockTypeOption.get("realtime"));

				// init Module
				KieBaseConfiguration kieBaseConf = ks.newKieBaseConfiguration();
				KieFileSystem kfs = ks.newKieFileSystem();
				System.out.println(kieModuleModel.toXML());
				kfs.writeKModuleXML(kieModuleModel.toXML());
				
				
				// attach kfs to kieBuilder
				KieBuilder kieBuilder = ks.newKieBuilder(kfs).buildAll();
			    //assertEquals(0, kieBuilder.getResults().getMessages(Message.Level.ERROR).size());
				
				
				
				KieContainer kieContainer = ks.newKieContainer(ks.getRepository().getDefaultReleaseId());
			    KieBase kieBase = kieContainer.newKieBase(kieBaseConf);
			    KieSession kSession = kieBase.newKieSession();
			    KieScanner kieScanner = ks.newKieScanner(kieContainer);
				
			   
			    String drl = generateDRLString();
			    kfs.write("src/main/resources/rules/ruleSet.drl", generateDRLString());
			    kieBuilder.buildAll();
			    //kieBuilder.incrementalBuild();
			    
			    //System.out.println(kieBuilder.getResults().getMessages(Message.Level.ERROR));
			    
			    kieScanner.scanNow();
			    
			    
			    kSession.fireAllRules();
			    
			    

				// KieContainer kContainer = ks.getKieClasspathContainer();
				// KieContainer kContainer = ks.newKieContainer(releaseID);
				// KieSession kSession =
				// kieContainer.newKieSession("ksession-rules");

				/*
				 * KieBuilder kieBuilder = ks.newKieBuilder(kfs).buildAll();
				 * Results results = kieBuilder.getResults(); if
				 * (results.hasMessages(Message.Level.ERROR)) {
				 * System.out.println(results.getMessages()); throw new
				 * IllegalStateException("### errors ###"); }
				 * 
				 * KieBase kieBase = kieContainer.getKieBase(); KieSession
				 * kSession = kieContainer.newKieSession();
				 * 
				 * KieScanner kieScanner = ks.newKieScanner(kieContainer);
				 * 
				 * KieContainer kieContainer = ks.newKieContainer(releaseID);
				 * 
				 * // Add Rules
				 * 
				 * int ruleID = 0;
				 * 
				 * // String RulesPath = //
				 * RulesEngine.class.getResource("/rules/Sample.drl").getPath().
				 * toString();
				 * 
				 * kfs.write(RulesEngine.class.getResource("/ruleSet" + ruleID +
				 * ".drl").getPath().toString(), generateDRLString());
				 * 
				 * // kfs.write("src/main/resources/KBase1/ruleSet" + ruleID +
				 * // ".drl", generateDRLString()); ruleID++;
				 * kieBuilder.buildAll(); // assertEquals(0, //
				 * kieBuilder.getResults().getMessages(Message.Level.ERROR).size
				 * ()); kieScanner.scanNow();
				 */

				// go !
				/*
				 * Message message = new Message(); message.setMessage(
				 * "Hello World"); message.setStatus(Message.HELLO);
				 * kSession.insert(message); kSession.fireAllRules();
				 */

				// TV example
				TV tv = new TV();
				kSession.insert(tv);

				tv.setNextStatus(TV.ON);
				kSession.fireAllRules();

				tv.setAction(TV.ACTION_NEXT_CHANNEL);
				tv.setNextStatus(TV.OFF);

				// tv.setAction(TV.ACTION_PREV_CHANNEL);
				// tv.setAction(TV.ACTION_VOL_DOWN);
				// tv.setAction(TV.ACTION_VOL_UP);
				kSession.fireAllRules();

				// this will parse and compile in one step
				// kbuilder.add(ResourceFactory.newClassPathResource("HelloWorld.drl",
				// HelloWorldExample.class), ResourceType.DRL);

				// go !
				/*
				 * Message message = new Message(); message.setMessage(
				 * "Hello World" ); message.setStatus(Message.HELLO);
				 * kSession.insert(message); kSession.fireAllRules();
				 */

				/*
				 * String url =
				 * "http://localhost:8080/drools-5.1.1-guvnor/org.drools.guvnor.Guvnor/package/es_rules/LATEST";
				 * 
				 * KnowledgeAgent kagent =
				 * KnowledgeAgentFactory.newKnowledgeAgent("MyAgent");
				 * 
				 * kagent.applyChangeSet(ResourceFactory.newUrlResource(url));
				 * 
				 * KieBase kbase = kagent.getKnowledgeBase();
				 */

			} catch (Throwable t) {
				t.printStackTrace();
			}
		}
		/*******************************/

	}
	
	
	/*private String generateDRLString()
	{
		String header = "package com.es.rulesengine.rules\n";
		String drl1 = "rule R1 when\n" + 
				"   $m : Message( message == \"Hello World1\" )\n" + 
				"then\n"+ "end\n";

		StringBuilder sbuilder = new StringBuilder();
		sbuilder.append(header);
		sbuilder.append(drl1);
		
		return sbuilder.toString();
	}*/

	//public void addRule(String myRuleStatement, String myPackage, RuleBase myRuleBase) {
		// PackageBuilder packageBuilder = new PackageBuilder(new
		// Package(myPackage));
		// packageBuilder.addPackgeFromDrl(new StringReader(myRuleStatement));
		// myRuleBase.addPackage(packageBuilder.getPackage());
	//}

	private static String generateDRLString() {
		StringBuilder sb;
		sb = new StringBuilder();

		/*
		 * sb.append("package com.es.es.rulesengine.rules;" + newline); // for
		 * (int i = start; i <= end; i++) { sb.append("rule \"R\"" + newline);
		 * sb.append("when" + newline); sb.append("then" + newline); sb.append(
		 * "System.out.println(\"rule fired!\");" + newline); sb.append("end" +
		 * newline); // }
		 */

		sb.append("package com.es.es.rulesengine.rules;" + newline);
		sb.append("rule \"R\"" + newline);
		sb.append("when" + newline);
		sb.append("then" + newline);
		sb.append("System.out.println(\"rule fired!\");" + newline);
		sb.append("end" + newline);

		return sb.toString();
	}

}
