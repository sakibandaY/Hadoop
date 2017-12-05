package test;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import com.investor.Contactpoint;
import com.investor.Electronicaddress;
import com.investor.InverstorAggriate;
import com.investor.Organisation;
import com.investor.Organisationidentification;
import com.investor.Organisationname;
import com.investor.Partyidentificationinformation;
import com.investor.Person;
import com.investor.Personidentification;
import com.investor.Personname;
import com.investor.Phoneaddress;
import com.investor.Postaladdress;
import com.reporting.document.Reporting.Document;
import com.reporting.document.Reporting.Document.Recipients;
import com.reporting.document.Reporting.Document.Recipients.Recipient;
import com.reporting.document.Reporting.Document.Recipients.Recipient.Address;

public class BuildRecipentXml {

	public static void build() {

		List<InverstorAggriate> inverstorAggriates = MockServices.mockinv();
		Document document = new Document();

		String newName = null;
		
		com.reporting.document.Reporting.Document.Investor invxml=new com.reporting.document.Reporting.Document.Investor();
		
		/*for(Accountidentification ac : accountService.getaccountidentification()){
			invxml.getAccountNumber().add(ac.getnumber());
		}*/
	
	//	invxml.getAccountNumber().add("accNo");
		
		
		
		
		List<RecipientUtill> temprecipientUtills = new ArrayList<RecipientUtill>();
		List<RecipientUtill> recipientUtills = new ArrayList<RecipientUtill>();
		Contactpoint contactpoint = null;
		RecipientUtill recipientUtill = null;
		if (!inverstorAggriates.isEmpty()) {
			InverstorAggriate inverstorAggriate = inverstorAggriates.get(0);
			if (inverstorAggriate != null) {
				invxml.setInvestorType(inverstorAggriate.getPartytype());
				if(inverstorAggriate.getPartytype().equalsIgnoreCase("Individual")|| inverstorAggriate.getPartytype().equalsIgnoreCase("Retail")) {
					Person person = inverstorAggriate.getPerson().get(0);
					if(person != null) {
						Personidentification personidentification = person.getPersonidentification().get(0);
						recipientUtill = new RecipientUtill();
						recipientUtill.setReportingLanguage("en");
						recipientUtill.setChannelCode("PRINT");
						recipientUtill.setCopyType("Investor Old");
						if(personidentification != null) {
							Partyidentificationinformation partyidentificationinformation = personidentification.getPartyidentificationinformation();
							if(partyidentificationinformation != null) {
								recipientUtill.setId(partyidentificationinformation.getGenericidentification().getIdentification());
								invxml.setId(partyidentificationinformation.getGenericidentification().getIdentification());
								}
							List<Personname> personnames = personidentification.getPersonname();
							
							if(!personnames.isEmpty()) {
								for(Personname personname: personnames){
									if(partyidentificationinformation.getGenericidentification().getExpirydate().contains("3999")) {
										newName = personname.getGivenname()+" "+personname.getLastname();
										invxml.setName(newName);
										recipientUtill.setName(newName);
									}else {
										invxml.setNameOld(personname.getGivenname()+" "+personname.getLastname());
									}
									
									if(personname.getPartyname().getPartynametype().equalsIgnoreCase("Mailing Name")) {
										invxml.setContactName(personname.getPartyname().getName());
									}
									invxml.setTitle(personname.getNameprefix());
								}
							} 
						}
						invxml.setGender(person.getGender());
						contactpoint = person.getContactpoint().get(0);
						}
					
					//documentType.setInvestor(inverstorAggriate);
				}
				
				else if(inverstorAggriate.getPartytype().equalsIgnoreCase("Organization or Corporate") ||  inverstorAggriate.getPartytype().equalsIgnoreCase("Corporate")) {

					Organisation organisation = inverstorAggriate.getOrganisation().get(0);
					Person person = inverstorAggriate.getPerson().get(0);

					if(organisation != null) {
						Organisationidentification organisationidentification = organisation.getOrganisationidentification().get(0);
						recipientUtill = new RecipientUtill();
						recipientUtill.setReportingLanguage("en");
						recipientUtill.setChannelCode("PRINT");
						recipientUtill.setCopyType("Investor Old");
						if(organisationidentification != null) {
							Partyidentificationinformation partyidentificationinformation = organisationidentification.getPartyidentificationinformation();
							if(partyidentificationinformation != null) {
								recipientUtill.setId(partyidentificationinformation.getGenericidentification().getIdentification());
								invxml.setId(partyidentificationinformation.getGenericidentification().getIdentification());
								}
							//setting Name from Orginisation
							Organisationname organisationname = organisationidentification.getOrganisationname();
							if(organisationname != null) {
								if(person != null) {
									Personidentification personidentification = person.getPersonidentification().get(0);
									if(personidentification != null) {
										Partyidentificationinformation partyidentificationinformation2 = personidentification.getPartyidentificationinformation();
										List<Personname> personnames = personidentification.getPersonname();
										if(!personnames.isEmpty()) {
											for(Personname personname: personnames){
												if(partyidentificationinformation2.getGenericidentification().getExpirydate().contains("3999")) 
													invxml.setName(organisationname.getPartyname().getName());
												else 
													invxml.setNameOld(organisationname.getPartyname().getName());
											}
										} 
									}
								}
								
								if(organisationname.getPartyname().getPartynametype().equalsIgnoreCase("Mailing Name")) {
									invxml.setContactName(organisationname.getPartyname().getName());
								}
								
								//setting Name from Orginisations
								/*List<Organisationidentification> organisationidentifications = organisation.getOrganisationidentification();
								for(Organisationidentification organisationidentification2 : organisationidentifications) {
									Organisationname organisationname1 = organisationidentification2.getOrganisationname();
									Partyidentificationinformation partyidentificationinformation1 = organisationidentification2.getPartyidentificationinformation();

									if(organisationname1 != null) {
										if(partyidentificationinformation1.getGenericidentification().getExpirydate().contains("3999")) 
											invxml.setName(organisationname1.getPartyname().getName());
										else 
											invxml.setNameOld(organisationname1.getPartyname().getName());
									}
									if(organisationname1.getPartyname().getPartynametype().equalsIgnoreCase("Mailing Name")) {
										invxml.setContactName(organisationname1.getPartyname().getName());
									}
								}*/
								
								
							}
							recipientUtill.setName(newName);
						}
						contactpoint = organisation.getContactpoint().get(0);
						}
					
					//documentType.setInvestor(inverstorAggriate);
				}
				
				
				if(contactpoint !=null) {
					if(!contactpoint.getPhoneaddress().isEmpty()) {
						for(Phoneaddress phoneaddress : contactpoint.getPhoneaddress()) {
								if(phoneaddress.getPhoneaddresstype().trim().equalsIgnoreCase("Business Telephone") || phoneaddress.getPhoneaddresstype().trim().equalsIgnoreCase("Home")) {
									recipientUtill.setPhone(phoneaddress.getPhonenumber());
									break;
								}else {
									recipientUtill.setPhone(phoneaddress.getPhonenumber2());
								}
						}
						for(Phoneaddress phoneaddress : contactpoint.getPhoneaddress()) {
								if(phoneaddress.getPhoneaddresstype().trim().equalsIgnoreCase("Fax")) {
									recipientUtill.setFax(phoneaddress.getPhonenumber());
									break;
								}else {
									recipientUtill.setFax(phoneaddress.getFaxnumber());
								}
						}
						
					}
					Electronicaddress electronicaddress= contactpoint.getElectronicaddress().get(0);
					if(electronicaddress != null) {
						recipientUtill.setEmail(electronicaddress.getEmailaddress());
					}
					
					if(!contactpoint.getPostaladdress().isEmpty()) {
						for(Postaladdress postaladdress: contactpoint.getPostaladdress()) {
							RecipientUtill recipient = new RecipientUtill();
							recipient.setChannelCode(recipientUtill.getChannelCode());
							recipient.setCopyType(recipientUtill.getCopyType());
							recipient.setCountry(recipientUtill.getCountry());
							recipient.setEmail(recipientUtill.getEmail());
							recipient.setFax(recipientUtill.getFax());
							recipient.setId(recipientUtill.getId());
							recipient.setName(recipientUtill.getName());
							recipient.setPhone(recipientUtill.getPhone());
							recipient.setReportingLanguage(recipientUtill.getReportingLanguage());
							
							recipient.setAddressline1(postaladdress.getAddresslineone());
							recipient.setAddressline2(postaladdress.getAddresslinetwo());
							recipient.setAddressline3(postaladdress.getAddresslinethree());
							recipient.setAddressline4(postaladdress.getAddresslinefour());
							recipient.setAddressline5(postaladdress.getAddresslinefive());
							recipient.setAddressline6(postaladdress.getAddresslinesix());
							recipient.setAddressline7(postaladdress.getAddresslineseven());
							recipient.setCountry(postaladdress.getCountry().getName());
							
							if(postaladdress.getGenericidentification().getExpirydate() == null || postaladdress.getGenericidentification().getExpirydate().contains("399")) {
								recipient.setCopyType("Investor New");
								recipientUtills.add(recipient);
							}else {
								recipient.setExpirydate(stringToDate(postaladdress.getGenericidentification().getExpirydate()));
								temprecipientUtills.add(recipient);
							}
						}
					}
					
				}
			}
		}
		for(RecipientUtill recipientU:recipientUtills) {
			com.reporting.document.Reporting.Document.Investor.Address address = new com.reporting.document.Reporting.Document.Investor.Address();
			address.getLine().add(recipientU.getAddressline1());
			address.getLine().add(recipientU.getAddressline2());
			address.getLine().add(recipientU.getAddressline3());
			address.getLine().add(recipientU.getAddressline4());
			address.getLine().add(recipientU.getAddressline5());
			address.getLine().add(recipientU.getAddressline6());
			address.getLine().add(recipientU.getAddressline7());
			invxml.setAddress(address);
		}
		document.setInvestor(invxml);
		Collections.sort(temprecipientUtills, new Comparator<RecipientUtill>() {

			@Override
			public int compare(RecipientUtill o1, RecipientUtill o2) {
				// TODO Auto-generated method stub
				return o2.getExpirydate().compareTo(o1.getExpirydate());
			}
		});
		
		
		recipientUtills.add(temprecipientUtills.get(0));
		
		
		for(RecipientUtill recipientU : recipientUtills) {
			System.out.println(recipientU);
			Recipients recipients = new Recipients();
			Recipient recipient = new Recipient();
			recipient.setChannelCode(recipientU.getChannelCode());
			recipient.setCopyType(recipientU.getCopyType());
			recipient.setCountry(recipientU.getCountry());
			recipient.setEmail(recipientU.getEmail());
			recipient.setFax(Integer.parseInt(recipientU.getFax()));
			recipient.setId(Integer.parseInt(recipientU.getId()));
			recipient.setName(recipientU.getName());
			recipient.setPhone(Integer.parseInt(recipientU.getPhone()));
			recipient.setReportingLanguage(recipientU.getReportingLanguage());
			
			Address address = new Address();
			address.getLine().add(recipientU.getAddressline1());
			address.getLine().add(recipientU.getAddressline2());
			address.getLine().add(recipientU.getAddressline3());
			address.getLine().add(recipientU.getAddressline4());
			address.getLine().add(recipientU.getAddressline5());
			address.getLine().add(recipientU.getAddressline6());
			address.getLine().add(recipientU.getAddressline7());
			recipient.setAddress(address);
			recipients.setRecipient(recipient);
			document.getRecipients().add(recipients);
		}
		
		
	}
	
	public static Date stringToDate(String date) {
		if(date != null) {
		Date date1=null;
		try {
			date1=new SimpleDateFormat("yyyy-MM-dd").parse(date);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return date1;
		}
		return null;
	}
	public static String dateToString(Date date){
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		formatter.format(date);
		
		String str = formatter.format(date);
		return str;
	}
}
