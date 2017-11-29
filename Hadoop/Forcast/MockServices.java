package test;

import java.util.ArrayList;
import java.util.List;

import com.investor.Contactpoint;
import com.investor.Country;
import com.investor.Electronicaddress;
import com.investor.Genericidentification;
import com.investor.InverstorAggriate;
import com.investor.Partyidentificationinformation;
import com.investor.Partyname;
import com.investor.Person;
import com.investor.Personidentification;
import com.investor.Personname;
import com.investor.Phoneaddress;
import com.investor.Postaladdress;

public class MockServices {
	
	public static List<InverstorAggriate> mockinv(){
		List<InverstorAggriate> inverstorAggriates = new ArrayList<InverstorAggriate>();
		
		InverstorAggriate inverstorAggriate = new InverstorAggriate();
		inverstorAggriate.setPartytype("Individual");

		List<Person> persons = new ArrayList<Person>();
		Person person = new Person();
		// person.person.personidentification.partyidentificationinformation.genericidentification.identification where partytype = "Individual or Retail"
		List<Personidentification> personidentifications = new ArrayList<Personidentification>();
		Personidentification personidentification = new Personidentification();
		Partyidentificationinformation partyidentificationinformation = new Partyidentificationinformation();
		Genericidentification genericidentification = new Genericidentification();
		genericidentification.setIdentification("12345");
		partyidentificationinformation.setGenericidentification(genericidentification);
		personidentification.setPartyidentificationinformation(partyidentificationinformation);
		//person.person.personidentification.personname.partyname and  person.person.personidentification.personname.partyname.partynametype
		List<Personname> personnames = new ArrayList<Personname>();
		Personname personname = new Personname();
		Partyname partyname = new Partyname();
		partyname.setPartynametype("Mailing Name");
		partyname.setName("Test Name");
		personname.setNameprefix("MR.");
		personname.setPartyname(partyname);
		personname.setGivenname("Test");
		personname.setLastname("Name");
		personnames.add(personname);
		personidentification.setPersonname(personnames);
		personidentifications.add(personidentification);
		person.setPersonidentification(personidentifications);
		
		//person.contactpoint.phoneaddress.phoneaddresstype == "Business Telephoner or Home"  >> person.contactpoint.phoneaddress.phonenumber >> ELSE<<  person.contactpoint.phoneaddress.phonenumber2 
		List<Contactpoint> contactpoints = new ArrayList<Contactpoint>();
		Contactpoint contactpoint = new Contactpoint();
		List<Phoneaddress> phoneaddress = new ArrayList<Phoneaddress>();
		Phoneaddress phoneaddressObj = new Phoneaddress();
		phoneaddressObj.setPhoneaddresstype("Home");
		phoneaddressObj.setPhonenumber("12342424");
		phoneaddress.add(phoneaddressObj);

		// person.contactpoint.phoneaddress.phoneaddresstype == "Fax"  >> person.contactpoint.phoneaddress.phonenumber >>ELSE<<  person.contactpoint.phoneaddress.faxnumber 
		phoneaddressObj = new Phoneaddress();
		phoneaddressObj.setPhoneaddresstype("Fax");
		phoneaddressObj.setPhonenumber("12342424");
		phoneaddress.add(phoneaddressObj);
		contactpoint.setPhoneaddress(phoneaddress);
		
		//person.contactpoint.electronicaddress.electronicaddresspurposetype == "Home or Business"  >> person.contactpoint.electronicaddress.emailaddress >>ELSE<<  person.contactpoint.electronicaddress.emailaddress 
		List<Electronicaddress> electronicaddress = new ArrayList<Electronicaddress>();
		Electronicaddress electronicaddressObj = new Electronicaddress();
		electronicaddressObj.setEmailaddress("test@abc.com");
		electronicaddressObj.setElectronicaddresspurposetype("Home");
		electronicaddress.add(electronicaddressObj);
		contactpoint.setElectronicaddress(electronicaddress);
	
		/*Investor service aggregate >> where partytype = "Individual or Retail" >> person.contactpoint.postaladdress.addresstype == "Home" >> 
		 * person.contactpoint.postaladdress.genericidentification.issuedate = "Date" and person.contactpoint.postaladdress.genericidentification.expirydate = "3999-12-31" >>
		 *  person.contactpoint.postaladdress.addresslineone, person.contactpoint.postaladdress.addresslinetwo, person.contactpoint.postaladdress.addresslinethree,
		 *   person.contactpoint.postaladdress.addresslinefour, person.contactpoint.postaladdress.addresslinefive, person.contactpoint.postaladdress.addresslinesix,
		 *    person.contactpoint.postaladdress.addresslineseven, person.contactpoint.postaladdress.province, perrson.contactpoint.postaladdress.townname
		 *     and person.contactpoint.postaladdress.postcodeidentification<<ELSE>> person.contactpoint.postaladdress.genericidentification.issuedate = "Date" 
		 *     and person.contactpoint.postaladdress.genericidentification.expirydate = "3999-12-31" >> person.contactpoint.postaladdress.addresslineone,
		 *      person.contactpoint.postaladdress.addresslinetwo, person.contactpoint.postaladdress.addresslinethree, person.contactpoint.postaladdress.addresslinefour, 
		 *      person.contactpoint.postaladdress.addresslinefive, person.contactpoint.postaladdress.addresslinesix, person.contactpoint.postaladdress.addresslineseven, 
		 *      person.contactpoint.postaladdress.province, perrson.contactpoint.postaladdress.townname and person.contactpoint.postaladdress.postcodeidentification
*/
		
		List<Postaladdress> postaladdress = new ArrayList<Postaladdress>();
		
		Postaladdress postaladdressObj = new Postaladdress();
		postaladdressObj.setAddresstype("Home");
		genericidentification =new Genericidentification();
		genericidentification.setIssuedate("2017-01-30");
		genericidentification.setExpirydate("3999-12-31");
		postaladdressObj.setGenericidentification(genericidentification);
		postaladdressObj.setAddresslineone("1 n");
		postaladdressObj.setAddresslinetwo("2 n");
		postaladdressObj.setAddresslinethree("3 n");
		postaladdressObj.setAddresslinefour("4 n");
		postaladdressObj.setAddresslinefive("5n");
		postaladdressObj.setAddresslineseven("6n");
		// person.contactpoint.postaladdress.country.name
		Country country = new Country();
		country.setName("abc");
		postaladdressObj.setCountry(country);;
		
		postaladdress.add(postaladdressObj);
		
		postaladdressObj = new Postaladdress();
		postaladdressObj.setAddresstype("Home");
		genericidentification =new Genericidentification();
		genericidentification.setIssuedate("2015-01-30");
		genericidentification.setExpirydate("2017-01-29");
		postaladdressObj.setGenericidentification(genericidentification);
		postaladdressObj.setAddresslineone("1 o");
		postaladdressObj.setAddresslinetwo("2 o");
		postaladdressObj.setAddresslinethree("3 o");
		postaladdressObj.setAddresslinefour("4 o");
		postaladdressObj.setAddresslinefive("5o");
		postaladdressObj.setAddresslineseven("6o");
		// person.contactpoint.postaladdress.country.name
		country = new Country();
		country.setName("abc");
		postaladdressObj.setCountry(country);;
		postaladdress.add(postaladdressObj);
		
		postaladdressObj = new Postaladdress();
		postaladdressObj.setAddresstype("Home");
		genericidentification =new Genericidentification();
		genericidentification.setIssuedate("2014-01-30");
		genericidentification.setExpirydate("2015-01-29");
		postaladdressObj.setGenericidentification(genericidentification);
		postaladdressObj.setAddresslineone("1 oo");
		postaladdressObj.setAddresslinetwo("2 oo");
		postaladdressObj.setAddresslinethree("3 oo");
		postaladdressObj.setAddresslinefour("4 oo");
		postaladdressObj.setAddresslinefive("5oo");
		postaladdressObj.setAddresslineseven("6oo");
		// person.contactpoint.postaladdress.country.name
		country = new Country();
		country.setName("abc");
		postaladdressObj.setCountry(country);;
		postaladdress.add(postaladdressObj);
		
		contactpoint.setPostaladdress(postaladdress);

		contactpoints.add(contactpoint);
		
		person.setContactpoint(contactpoints);
		
		persons.add(person);
		inverstorAggriate.setPerson(persons);
		
		inverstorAggriates.add(inverstorAggriate);
		
		return inverstorAggriates;
	}
}
